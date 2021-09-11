package com.example.saw_india.modalClasses;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.saw_india.R;
import com.example.saw_india.VideoPlayerActivity;

import java.util.LinkedList;

public class RecyclerViewAdaptorForFeeds extends RecyclerView.Adapter<RecyclerViewAdaptorForFeeds.RecyclerViewHolderForFeeds> {

    LinkedList<Video> allVideos;

    public RecyclerViewAdaptorForFeeds(LinkedList<Video> allVideos) {
        this.allVideos = allVideos;
    }

    @NonNull
    @Override
    public RecyclerViewHolderForFeeds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_layout_for_feeds, parent, false);
        return new RecyclerViewHolderForFeeds(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderForFeeds holder, final int position) {
        Glide.with(holder.thumbnail).load(allVideos.get(position).getThumbnailLink()).into(holder.thumbnail);
        Glide.with(holder.icon).load(allVideos.get(position).getIconLink()).into(holder.icon);
        holder.titleTV.setText(allVideos.get(position).getTitle());
        holder.organisationNameTV.setText(allVideos.get(position).getOrganizationName());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VideoPlayerActivity.class);
                intent.putExtra("Video", allVideos.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allVideos.size();
    }

    class RecyclerViewHolderForFeeds extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        ImageView icon;
        TextView titleTV;
        TextView organisationNameTV;
        ImageView playButton;

        public RecyclerViewHolderForFeeds(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            icon = itemView.findViewById(R.id.icon);
            titleTV = itemView.findViewById(R.id.title);
            organisationNameTV = itemView.findViewById(R.id.channel);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
