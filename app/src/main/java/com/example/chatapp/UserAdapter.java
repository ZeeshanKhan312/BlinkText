package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {


    public class UsersViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName ,lastMsg,lastMsgTime;
        RelativeLayout chatContent;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.getUserImageinImageView);
            userName=itemView.findViewById(R.id.username);
            chatContent=itemView.findViewById(R.id.chatContent);
            lastMsg=itemView.findViewById(R.id.lastMsg);
            lastMsgTime=itemView.findViewById(R.id.msgTime);
        }
    }

    private Context context;
    private ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chats,null,false);
        return new UsersViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user=users.get(position);
        holder.userName.setText(user.getName());

        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.profile)
                .into(holder.userImage);

        //to fetch the last msg and time of the chats
        String senderRoom= FirebaseAuth.getInstance().getUid() + user.getUid(); //(senderId + receiverId)
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String lastMsg=snapshot.child("lastMsg").getValue(String.class);
                            long msgTime=snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a");
                            holder.lastMsg.setText(lastMsg);
                            holder.lastMsgTime.setText(dateFormat.format(new Date(msgTime)));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

//        to open the chat room
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
