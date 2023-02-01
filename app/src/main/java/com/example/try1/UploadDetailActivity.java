package com.example.try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UploadDetailActivity extends AppCompatActivity {

    TextView textUploadVideoName;
    TextView textUploadVideoAddress;
    TextView textUploadScreenSize;
    TextView textUploadActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_detail);

        textUploadVideoName = findViewById(R.id.uploadVideoName);
        textUploadVideoAddress = findViewById(R.id.uploadVideoAddress);
        textUploadScreenSize = findViewById(R.id.uploadScreenSize);
        textUploadActions = findViewById(R.id.uploadActions);

        Intent i = getIntent();
        String uploadVideoName = i.getStringExtra("uploadVideoName");
        String uploadVideoAddress = i.getStringExtra("uploadVideoAddress");
        String uploadScreenSize = i.getStringExtra("uploadScreenSize");
        String uploadActions = i.getStringExtra("uploadActions");

        textUploadVideoName.setText(uploadVideoName);
        textUploadVideoAddress.setText(uploadVideoAddress);
        textUploadScreenSize.setText(uploadScreenSize);
        textUploadActions.setText(uploadActions);
    }
}