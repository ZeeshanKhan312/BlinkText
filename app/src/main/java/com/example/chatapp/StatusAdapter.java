package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.example.chatapp.Models.Status;
import com.example.chatapp.Models.UserStatus;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.statusViewHolder> {

    public class statusViewHolder extends RecyclerView.ViewHolder{
        CircularStatusView statusView;
        CircleImageView statusImage;
        TextView userName;

        public statusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusView=itemView.findViewById(R.id.circular_status_view);
            userName=itemView.findViewById(R.id.userID);
            statusImage=itemView.findViewById(R.id.statusImage);
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
        UserStatus userStatus=userStatuses.get(position);
        holder.userName.setText(userStatus.getName());

        Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size()-1);
        Glide.with(context)
                .load(lastStatus.getImageUrl()).into(holder.statusImage);

        holder.statusView.setPortionsCount(userStatus.getStatuses().size());

        holder.statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for(Status status : userStatus.getStatuses()) {
                    myStories.add(new MyStory(status.getImageUrl())); //adding all the statuses of that user
                }

                new StoryView.Builder(((HomeActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }


}
