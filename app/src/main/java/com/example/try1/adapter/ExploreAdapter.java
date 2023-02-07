package com.example.try1.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.try1.DetailsActivity;
import com.example.try1.R;
import com.example.try1.model.Explore;
import com.example.try1.model.Shortcut;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {

    Context context;
    List<Explore> exploreList;

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

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("download button clicked");
            }
        });

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
