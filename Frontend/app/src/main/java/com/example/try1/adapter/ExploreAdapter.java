package com.example.try1.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.try1.DetailsActivity;
import com.example.try1.R;
import com.example.try1.model.Explore;
import com.example.try1.model.Shortcut;

import java.util.List;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {

    Context context;
    List<Explore> exploreList;
    String downloadUrl;

    public ExploreAdapter(Context context, List<Explore> exploreList) {
        this.context = context;
        this.exploreList = exploreList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Explore> getExploreList() {
        return exploreList;
    }

    public void setExploreList(List<Explore> exploreList) {
        this.exploreList = exploreList;
    }

    @NonNull
    @Override
    public ExploreAdapter.ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_row_item, parent, false);
        return new ExploreAdapter.ExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapter.ExploreViewHolder holder, int position) {

//        holder.exploreImage.setImageResource(exploreList.get(position).getImageUrl());
//        holder.name.setText(exploreList.get(position).getName());
//        holder.restorantName.setText(exploreList.get(position).getRestorantname());

        holder.webView.loadUrl(exploreList.get(position).getLink());

        YouTubeUriExtractor somxt = new YouTubeUriExtractor(context) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 22;

                    try {
                        downloadUrl = ytFiles.get(itag).getUrl();
                        if (downloadUrl != null) {
                            Toast.makeText(context, "Download started....", Toast.LENGTH_SHORT).show();
                            downloadVideo(downloadUrl);
                            Log.d("Download url", "URL :- "+downloadUrl);
                        }
                    }catch (Exception e) {
                        Toast.makeText(context, "Download url could not be fetched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("download button clicked");
                String url = exploreList.get(holder.getAdapterPosition()).getDownloadLink();

                if (url!=null) {
                    somxt.extract(url);
                }
            }
        });

    }

    void downloadVideo(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("download");
        request.setDescription("You file is downloading");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "testing.mp4");

        DownloadManager manager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    public static final class ExploreViewHolder extends RecyclerView.ViewHolder {

//        ImageView exploreImage;
//        TextView name, restorantName;

        WebView webView;
        Button button;

        @SuppressLint("SetJavaScriptEnabled")
        public ExploreViewHolder(@NonNull View itemView) {
            super(itemView);

//            exploreImage = itemView.findViewById(R.id.explore_image);
//            name = itemView.findViewById(R.id.name);
//            restorantName = itemView.findViewById(R.id.restorant_name);
            webView = itemView.findViewById(R.id.video_view);
            button = itemView.findViewById(R.id.download);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient(){
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }
}
