package com.example.try1;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.try1.adapter.HomeExploreAdapter;
import com.example.try1.adapter.ShortcutAdapter;
import com.example.try1.model.Explore;
import com.example.try1.model.Shortcut;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
////
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView shortcutRecycler;
    RecyclerView homeExploreRecycler;
    ShortcutAdapter shortcutAdapter;
    HomeExploreAdapter homeExploreAdapter;
    DatabaseHelper myDB;

    private Context globalContext = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //attributes
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static String selectedImagePath;

    String screenSize;
    String detectedActions;
    int REQUEST_CODE = 3;
    EditText ipv4AddressView;
    static String ipv4AddressAndPort = "118.138.90.123:5000";
    static RequestBody requestBody;
    static String postUrl;
    String getUrl;
    TextView responseText;
    List<Shortcut> shortcutList = new ArrayList<>();
    List<Explore> homeExploreList = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    public static String getSelectedImagePath() {
        return selectedImagePath;
    }


    /**
     * Use this factory method to create a new instance of /
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        shortcutList.clear();

        responseText = view.findViewById(R.id.responseText);
        SharedPreferences sharedPref = getContext().getSharedPreferences("ACTIONS", 0);
        getUrl = sharedPref.getString("URL", null);

        myDB = new DatabaseHelper(getContext());

        storeData();


        homeExploreList.add(new Explore("Turn on camera", "Action 1", R.drawable.asiafood1));
        homeExploreList.add(new Explore("Open android store", "Store opener", R.drawable.asiafood2));
        homeExploreList.add(new Explore("Order Chicago pizza from domino's","Chicago Pizza", R.drawable.asiafood1));

        homeExploreRecycler = view.findViewById(R.id.home_explore_recycler);
        RecyclerView.LayoutManager homeLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        homeExploreRecycler.setLayoutManager(homeLayoutManager);
        homeExploreAdapter = new HomeExploreAdapter(getContext(), homeExploreList);
        homeExploreRecycler.setAdapter(homeExploreAdapter);


        shortcutRecycler = view.findViewById(R.id.shortcut_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        shortcutRecycler.setLayoutManager(layoutManager);
        shortcutAdapter = new ShortcutAdapter(getContext(), shortcutList);
        shortcutRecycler.setAdapter(shortcutAdapter);

        Button button1 = (Button) view.findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo(view);
            }
        });

        Button button2 = (Button) view.findViewById(R.id.button5);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] pathParts = selectedImagePath.split("/");
                String vidName = pathParts[pathParts.length - 1];
                uploadVideo(view);



            }
        });


    }





    void storeData() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                shortcutList.add(new Shortcut(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }
    }


    public static String getPath(final Context context, final Uri uri) {

        System.out.println("in get path");
        System.out.println(context);
        System.out.println(uri);

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            System.out.println("in the first if");
            if (isExternalStorageDocument(uri)) {
                System.out.println("in isExternalStorageDocument");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    System.out.println("in primary.equalsIgnoreCase(type");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                System.out.println("here???");
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                System.out.println("in isdownload");

                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                System.out.println("in isMediaDocument(uri)");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };


                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            System.out.println("in equalsIgnoreCase(uri.getScheme()");
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            System.out.println("in le.equalsIgnoreCase(uri.getScheme()");
            return uri.getPath();
        }
        System.out.println("i shouldn't be here");
        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            System.out.println(uri);
            System.out.println(projection.length);
            System.out.println(selection);
            System.out.println(selectionArgs.length);
            if (cursor != null && cursor.moveToNext()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void uploadVideo(View v) {
        postUrl = "http://" + ipv4AddressAndPort + "/";

//        RequestBody postBody = new FormBody.Builder().add("value","hello").build();
//        postRequest(postUrl, postBody);
        System.out.println(selectedImagePath);

        if (selectedImagePath != null) {
            File file = new File(selectedImagePath);
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
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Select a video", Toast.LENGTH_LONG).show();
        }
    }


    void postRequest(String postUrl, RequestBody postBody) throws JSONException {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        TextView responseText = findViewById(R.id.responseText);
                        responseText.setText("Failed to Connect to Server");
                        Log.i("Upload", String.valueOf(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
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
                                SharedPreferences sharedPref = getContext().getSharedPreferences("ACTIONS", 0);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("URL", getUrl);
                                editor.apply();
                            }
                            responseText.setText(responseTxt);
                            Toast.makeText(getActivity().getApplicationContext(), responseText.getText().toString(), Toast.LENGTH_LONG).show();
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

    public String getScreenSize() throws JSONException {
        SharedPreferences sharedPref = getContext().getSharedPreferences("ACTIONS", 0);
        String detectedActions = sharedPref.getString("ACTION_RESULT", "default");

        JSONArray jsonArray = new JSONArray(detectedActions);
        JSONObject action = jsonArray.getJSONObject(0);

        String height = action.getString("height");
        String width = action.getString("width");

        return width+" * "+height;
    }

    public String getDetectedActions() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("ACTIONS", 0);
        String detectedActions = sharedPref.getString("ACTION_RESULT", "default");

        return detectedActions;
    }

    public void selectVideo(View v) {
        System.out.println("we are on select video");
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                pickVideo();
            } else {
                requestPermission();
            }
        } else {
            pickVideo();
        }
    }

    public void pickVideo() {
        System.out.println("we are on pickvideo");
        Intent intent = new Intent();
        intent.setType("video/mp4");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, REQUEST_CODE);

        pickResultLauncher.launch(intent);


    }

    ActivityResultLauncher<Intent> pickResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Log.i("onActivityResult", "Good");
                        Uri uri = data.getData();
                        System.out.println("this is uri:"+uri);
//                      selectedImagePath = getPath(uri);
                        selectedImagePath = getPath(getActivity().getApplicationContext(), uri);

                        Log.i("onActivityResult", "Path: " + selectedImagePath);
                        //EditText imgPath = getView().findViewById(R.id.vidPath);
                        //imgPath.setText(selectedImagePath);
                        Toast.makeText(getActivity().getApplicationContext(), "Video selected", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

//    @Override
//    public void onActivityResult(int reqCode, int resCode, Intent data) {
//        super.onActivityResult(reqCode, resCode, data);
//        Log.i("onActivityResult", "In");
//        if (resCode == getActivity().RESULT_OK && data != null && reqCode == REQUEST_CODE) {
//            Log.i("onActivityResult", "Good");
//            Uri uri = data.getData();
////            selectedImagePath = getPath(uri);
//            selectedImagePath = getPath(getContext().getApplicationContext(), uri);
//
//            Log.i("onActivityResult", "Path: " + selectedImagePath);
//            //EditText imgPath = getView().findViewById(R.id.vidPath);
//            //imgPath.setText(selectedImagePath);
//            Toast.makeText(getContext().getApplicationContext(), "Video selected", Toast.LENGTH_LONG).show();
//        }
//    }


    private void requestPermission() {
        System.out.println("we are on requestpermission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            System.out.println("is asking for context, the problem is next line");
            Toast.makeText(getActivity().getApplicationContext(), "Please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("is asking");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        System.out.println("we are on check permission");
        int result = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
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
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                responseText.setText(state);
                                Toast.makeText(getActivity().getApplicationContext(), "Result is " + state, Toast.LENGTH_LONG).show();
                            }
                        });

                        if (state.equals("SUCCESS")) {
                            JSONArray actions = object.getJSONArray("result");
                            SharedPreferences sharedPref = getContext().getSharedPreferences("ACTIONS", 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("ACTION_RESULT", actions.toString());
                            editor.apply();
                            Log.i("Json", "state is success, result saved");

                            screenSize = getScreenSize();
                            detectedActions = getDetectedActions();

                            String[] pathParts = selectedImagePath.split("/");
                            System.out.println("this is vid name>>>>>>>");
                            String vidName = pathParts[pathParts.length - 1];
                            System.out.println(vidName);

                            shortcutList.add(new Shortcut(vidName, selectedImagePath, screenSize, detectedActions));

                            Intent i = new Intent(getContext(), UploadDetailActivity.class);

                            i.putExtra("uploadVideoName", vidName);
                            i.putExtra("uploadVideoAddress", selectedImagePath);
                            i.putExtra("uploadScreenSize", screenSize);
                            i.putExtra("uploadActions", detectedActions);

                            getContext().startActivity(i);


                            myDB = new DatabaseHelper(getContext());
                            myDB.addShortcut(vidName, selectedImagePath, screenSize, detectedActions);
                            Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                            Log.i("Json", screenSize);
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