package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    public class SentViewHolder extends RecyclerView.ViewHolder{
        TextView sentMsg;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMsg=itemView.findViewById(R.id.message);
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder{
        TextView receiveMsg;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            receiveMsg=itemView.findViewById(R.id.message);
        }
    }

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view= LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
            return  new SentViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.message_receive,parent,false);
            return  new ReceiveViewHolder(view);
        }
    }

    //returns the view type of the item at that position for the purpose of view recycling
    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        //if the userId and message senderId matches
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }else
            return ITEM_RECEIVE;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message =messages.get(position);
        if(holder.getClass()==SentViewHolder.class){
            SentViewHolder viewHolder=(SentViewHolder) holder;//typeCasted holder to SentViewHolder type
            viewHolder.sentMsg.setText(message.getMessage());
        }
        else{
            ReceiveViewHolder viewHolder=(ReceiveViewHolder) holder;//typeCasted holder to SentViewHolder type
            viewHolder.receiveMsg.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
