package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.chatapp.Models.UserStatus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.statusViewHolder> {

    public class statusViewHolder extends RecyclerView.ViewHolder{
        CircularStatusView statusView;
        TextView userName;

        public statusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusView=itemView.findViewById(R.id.circular_status_view);
            userName=itemView.findViewById(R.id.userID);
        }
    }

    Context context;
    ArrayList<UserStatus> userStatuses;

    public StatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public statusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.status,parent,false);
        return new statusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }


}
