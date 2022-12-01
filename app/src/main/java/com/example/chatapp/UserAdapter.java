package com.example.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {


    public class UsersViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.getUserImageinImageView);
            userName=itemView.findViewById(R.id.username);
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}
