package com.example.try1;

import static com.example.try1.HomeFragment.ipv4AddressAndPort;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.try1.model.Shortcut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {

    TextView progress;


    static RequestBody requestBody;

    String getUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progress = findViewById(R.id.progress);

        Intent i = getIntent();
        String imagePath = i.getStringExtra("imagePath");

        uploadVideo(imagePath);
    }


    public void uploadVideo(String imagePath) {
        String postUrl = "http://" + ipv4AddressAndPort + "/";

//        RequestBody postBody = new FormBody.Builder().add("value","hello").build();
//        postRequest(postUrl, postBody);
        System.out.println(imagePath);

        if (imagePath != null) {

            File file = new File(imagePath);

            try {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("video/mp4")))
                        .build();
                Log.i("Upload", String.valueOf(requestBody.contentType()));
                postRequest(postUrl, requestBody);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Upload", "failed");

            }
        }
    }


    void postRequest(String postUrl, RequestBody postBody) throws JSONException {

        progress = findViewById(R.id.progress);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        TextView responseText = findViewById(R.id.responseText);
                        progress.setText("Failed to Connect to Server");
                        Log.i("Upload", String.valueOf(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
//                        TextView responseText = findViewById(R.id.responseText);
                                                    try {
                                                        String responseData = response.body().string();
                                                        JSONArray json = new JSONArray(responseData);
                                                        String responseTxt = " Failed to upload";
                                                        JSONObject statusId = null;
                                                        if (Integer.parseInt(json.get(0).toString()) == 202) {
                                                            responseTxt = "Successfully uploaded. Please wait the video to be process";
                                                            statusId = json.getJSONObject(1);
                                                            Log.i("Json", statusId.getString("Location"));
                                                            getUrl = "http://" + ipv4AddressAndPort + "/" + statusId.getString("Location");

                                                            // save to shared preferences
                                                            SharedPreferences sharedPref = getSharedPreferences("ACTIONS", 0);
                                                            SharedPreferences.Editor editor = sharedPref.edit();
                                                            editor.putString("URL", getUrl);
                                                            editor.apply();
                                                        }
                                                        progress.setText(responseTxt);
                                                        Toast.makeText(getApplicationContext(), progress.getText().toString(), Toast.LENGTH_LONG).show();
                                                        checkProgress(null);

                                                    } catch (JSONException | IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }

                );
            }
        });


    }


    public void checkProgress(@Nullable View v) throws IOException {
//        String url_1 = "http://" + ipv4AddressAndPort + "/status/753e71d9-427a-4f50-8527-4fb289b7e42b";
//        Log.i("Json", getUrl);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while(true){
                        String result = getRequest(getUrl);
                        JSONObject object = new JSONObject(result);
                        String state = object.getString("state");
                        Log.i("Json", object.getString("state"));

                        //Your code goes here
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progress.setText(state);
                                Toast.makeText(getApplicationContext(), "Result is " + state, Toast.LENGTH_LONG).show();
                            }
                        });

                        if (state.equals("SUCCESS")) {
                            JSONArray actions = object.getJSONArray("result");
                            SharedPreferences sharedPref = getSharedPreferences("ACTIONS", 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("ACTION_RESULT", actions.toString());
                            editor.apply();
                            Log.i("Json", "state is success, result saved");


                            break;
                        }

                        Thread.sleep(30000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


//        while(!result.contains("SUCCESS")){
//            try
//            {
//                Thread.sleep(30000);
//                result = getRequest(getUrl);
//            }
//            catch(InterruptedException ex)
//            {
//                Thread.currentThread().interrupt();
//            }
//        }

    }


    public String getRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
//        final JSONObject[] responseResult = new JSONObject[1];
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response getResponse = client.newCall(request).execute();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    getResponse.close();
                    response.close();
                    // do something wih the result
                }
            }
        });
        return getResponse.body().string();

    }





}