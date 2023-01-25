package com.example.try1;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertDialogActivity extends AppCompatActivity {

    MainActivity mainActivity;
    MyAccessibilityService ms;

    int currentActionIndex = 0;

    String actionHint = ms.actionHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            displayAlert();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayAlert() throws JSONException {
//        JSONArray jsonArray = null;
//
//        SharedPreferences sharedPref = getSharedPreferences("ACTIONS", 0);
//        String detectedActions = sharedPref.getString("ACTION_RESULT", "default");
//
//        jsonArray = new JSONArray(detectedActions);
//
//        for (int i = currentActionIndex; i < jsonArray.length(); i++) {
//            JSONObject action = jsonArray.getJSONObject(i);
//            actionHint = action.getString("action_hint");
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need your help! Action hint is " + actionHint)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialogActivity.this.finish();
                        dialogInterface.cancel();
                        moveTaskToBack(true);

                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialogActivity.this.finish();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
