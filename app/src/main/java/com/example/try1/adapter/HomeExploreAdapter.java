package com.example.try1.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.try1.R;
import com.example.try1.model.Explore;

import java.util.List;

public class HomeExploreAdapter extends RecyclerView.Adapter<HomeExploreAdapter.HomeExploreViewHolder> {

    Context context;
    List<Explore> homeExploreList;

    public HomeExploreAdapter(Context context, List<Explore> homeExploreList) {
        this.context = context;
        this.homeExploreList = homeExploreList;
    }

    @NonNull
    @Override
    public HomeExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_explore_row_item, parent, false);
        return new HomeExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeExploreViewHolder holder, int position) {
//        holder.image.setImageResource(homeExploreList.get(position).getImageUrl());
//        holder.name.setText(homeExploreList.get(position).getName());
        holder.webView.loadUrl(homeExploreList.get(position).getLink());

    }

    @Override
    public int getItemCount() {
        return homeExploreList.size();
    }

    public static final class HomeExploreViewHolder extends RecyclerView.ViewHolder {

//        ImageView image;
//        TextView name;

        WebView webView;

        public HomeExploreViewHolder(@NonNull View itemView) {
            super(itemView);

//            image = itemView.findViewById(R.id.imageView3);
//            name = itemView.findViewById(R.id.home_name);

            webView = itemView.findViewById(R.id.explore_video_view);
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
