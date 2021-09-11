package com.example.saw_india;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.saw_india.modalClasses.Video;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Objects;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    Toolbar toolbar;
    YouTubePlayerView ytPlayer;
    String videoId;
    ImageView icon;
    TextView titleTV;
    TextView channelTv;
    TextView descriptionTV;
    ImageView warningIcon;
    TextView warningTV;
    Button proceedButton;
    TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        toolbar = findViewById(R.id.toolbar);
        ytPlayer = findViewById(R.id.ytPlayer);
        icon = findViewById(R.id.icon);
        titleTV = findViewById(R.id.titleTV);
        channelTv = findViewById(R.id.channelTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        warningIcon = findViewById(R.id.warningIcon);
        warningTV = findViewById(R.id.warningTV);
        warningText = findViewById(R.id.warningMessageTV);
        proceedButton = findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningIcon.setVisibility(View.INVISIBLE);
                warningText.setVisibility(View.INVISIBLE);
                warningTV.setVisibility(View.INVISIBLE);
                proceedButton.setVisibility(View.INVISIBLE);
                //ytPlayer.initialize(getResources().getString(R.string.YOUTUBE_API_KEY), VideoPlayerActivity.this);
            }
        });

        toolbar.setTitle("SAW INDIA");
        setActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo_for_toolbar);
//        setSupportActionBar(toolbar);
        Objects.requireNonNull(getActionBar()).setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Video video = (Video) intent.getSerializableExtra("Video");
        videoId = video.getVideoLink().substring(17);
        Glide.with(getApplicationContext()).load(video.getIconLink()).into(icon);
        titleTV.setText(video.getTitle());
        channelTv.setText(video.getOrganizationName());
        descriptionTV.setText(video.getDescription());
    }
//
//    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoId);
        youTubePlayer.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
    }
}