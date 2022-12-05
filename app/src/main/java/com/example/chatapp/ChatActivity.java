package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapp.Models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    TextView name;
    EditText msgBox;
    ImageView back,send;
    ArrayList<Message> messages;
    MessagesAdapter messagesAdapter;
    String senderRoom, receiverRoom;
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name= findViewById(R.id.name);
        back=findViewById(R.id.imageView2);
        send=findViewById(R.id.sendBtn);
        msgBox=findViewById(R.id.messageBox);
        recyclerView=findViewById(R.id.recyclerView);

        messages=new ArrayList<>();
        messagesAdapter=new MessagesAdapter(this,messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messagesAdapter);

        firebaseDatabase=FirebaseDatabase.getInstance();

        String senderName=getIntent().getStringExtra("name");
        String receiverUid=getIntent().getStringExtra("uid");
        String senderUid= FirebaseAuth.getInstance().getUid();

        senderRoom=senderUid+receiverUid;
        receiverRoom = receiverUid + senderUid;

        name.setText(senderName);

        //for fetching all the prvs messages
        firebaseDatabase.getReference().child("chats")
                .child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Message message=snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        messagesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //for sending a new message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String messageText=msgBox.getText().toString();
               Date date=new Date(); //to get the time stamp of the message
               Message message=new Message(messageText,senderUid,date.getTime());

                HashMap<String,Object> lastMsgObject=new HashMap<>();
                lastMsgObject.put("lastMsg",message.getMessage()); //will set the new msg as last msg of the conversation
                lastMsgObject.put("lastMsgTime",date.getTime()); //will set the last msg time
                firebaseDatabase.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObject);
                firebaseDatabase.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObject);

               firebaseDatabase.getReference().child("chats")
                       .child(senderRoom)
                       .child("messages")
                       .push() //will create a new node for each text so that the new msg doesn't replace the old msg
                       .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               //sending message to receiver room
                               firebaseDatabase.getReference().child("chats")
                                       .child(receiverRoom)

                                       .child("messages")
                                       .push() //will create a new node for each text so that the new msg doesn't replace the old msg
                                       .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {

                                           }
                                       });
                           }
                       });
               msgBox.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}