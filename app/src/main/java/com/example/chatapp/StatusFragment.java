package com.example.chatapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Status;
import com.example.chatapp.Models.User;
import com.example.chatapp.Models.UserStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class StatusFragment extends Fragment {
    CircleImageView imageView;
    StatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    RecyclerView recyclerView;
    RelativeLayout uploadStatus;
    ProgressDialog dialog;

    FirebaseDatabase database;
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_status,null);
        recyclerView=view.findViewById(R.id.recycleViewofStatus);
        uploadStatus=view.findViewById(R.id.uploadStatus);
        imageView=view.findViewById(R.id.userProfile);

        dialog=new ProgressDialog(getContext());
        dialog.setMessage("Uploading Status..");
        dialog.setCancelable(false);

        userStatuses=new ArrayList<>();
        statusAdapter=new StatusAdapter(getContext(),userStatuses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(statusAdapter);

        //to fetch the loginUserImage from Storage
        StorageReference imgreference= FirebaseStorage.getInstance().getReference();
        imgreference.child("Profiles").child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUriToken=uri.toString();
                Picasso.get().load(uri).into(imageView);
            }
        });

        database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user=snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //for uploading a new status
        uploadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,75);
            }
        });

        //for fetching the status
        database.getReference().child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userStatuses.clear();
                    for(DataSnapshot statusSnapshot:snapshot.getChildren()){ //will get the whole status of a sungle user
                        UserStatus status=new UserStatus();
                        status.setName(statusSnapshot.child("name").getValue(String.class));
                        status.setProfileImage(statusSnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(statusSnapshot.child("lastUpdated").getValue(Long.class));

                        //for adding single status of a user
                        ArrayList<Status> statuses=new ArrayList<>();
                        for(DataSnapshot singleStatus:statusSnapshot.child("statuses").getChildren()){
                            Status status1=singleStatus.getValue(Status.class);
                            statuses.add(status1);
                        }
                        status.setStatuses(statuses);
                        userStatuses.add(status);
                    }
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                dialog.show();
                FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference=firebaseStorage.getReference().child("status").child(date.getTime()+"");

                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus=new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String,Object> obj= new HashMap<>();
                                    obj.put("name",userStatus.getName());
                                    obj.put("profileImage",userStatus.getProfileImage());
                                    obj.put("lastUpdated",userStatus.getLastUpdated());

                                    String imageUri= uri.toString();
                                    Status status=new Status(imageUri,userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("status")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference()
                                            .child("status")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });

            }
        }
    }
}
