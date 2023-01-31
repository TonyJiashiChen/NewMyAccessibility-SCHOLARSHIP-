package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShortcutDetailsActivity extends AppCompatActivity {

    TextView textVideoName;
    TextView textVideoAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_details);

        textVideoName = findViewById(R.id.videoName);
        textVideoAddress = findViewById(R.id.videoAddress);

        Intent i = getIntent();
        String videoName = i.getStringExtra("videoName");
        String videoAddress = i.getStringExtra("videoAddress");

        textVideoName.setText(videoName);
        textVideoAddress.setText(videoAddress);
    }
}