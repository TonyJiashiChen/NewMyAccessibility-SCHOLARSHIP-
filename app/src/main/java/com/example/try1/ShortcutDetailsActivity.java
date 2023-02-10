package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ShortcutDetailsActivity extends AppCompatActivity {

    TextView textVideoName;
    TextView textVideoAddress;
    TextView textScreenSize;
    TextView textActions;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_details);

        textVideoName = findViewById(R.id.videoName);
        textVideoAddress = findViewById(R.id.videoAddress);
        textActions = findViewById(R.id.actions);
        textScreenSize = findViewById(R.id.screenSize);
        videoView = findViewById(R.id.shortcut_video_view);


        Intent i = getIntent();
        String videoName = i.getStringExtra("videoName");
        String videoAddress = i.getStringExtra("videoAddress");
        String screenSize = i.getStringExtra("screenSize");
        String actions = i.getStringExtra("actions");

        textVideoName.setText(videoName);
        textVideoAddress.setText(videoAddress);
        textActions.setText(actions);
        textScreenSize.setText(screenSize);


        videoView.setVideoPath(videoAddress);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}