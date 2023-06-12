package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ShortcutDetailsActivity extends AppCompatActivity {

    TextView textVideoName;
    TextView textVideoAddress;
    TextView textScreenSize;
    TextView textActions;
    VideoView videoView;
    Button action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_details);

        textVideoName = findViewById(R.id.videoName);
        textVideoAddress = findViewById(R.id.videoAddress);
        textActions = findViewById(R.id.actions);
        textScreenSize = findViewById(R.id.screenSize);
        videoView = findViewById(R.id.shortcut_video_view);
        action = findViewById(R.id.actionDetail);


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

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = ShortcutDetailsActivity.this.getSharedPreferences("ACTIONS", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ACTION_RESULT", i.getStringExtra("actual_action"));
                editor.apply();
                System.out.println("detail upload button clicked");
            }
        });
    }
}