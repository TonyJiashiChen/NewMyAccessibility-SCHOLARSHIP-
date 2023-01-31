package com.example.try1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.try1.HomeFragment;
import com.example.try1.R;
import com.example.try1.ShortcutDetailsActivity;
import com.example.try1.model.Shortcut;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.List;

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutAdapter.ShortcutViewHolder> {

    Context context;
    List<Shortcut> shortcutList;


    static HomeFragment newHome = new HomeFragment();

    public ShortcutAdapter(Context context, List<Shortcut> shortcutList) {
        this.context = context;
        this.shortcutList = shortcutList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Shortcut> getShortcutList() {
        return shortcutList;
    }

    public void setShortcutList(List<Shortcut> shortcutList) {
        this.shortcutList = shortcutList;
    }

    @NonNull
    @Override
    public ShortcutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shortcut_row_item, parent, false);
        return new ShortcutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutViewHolder holder, int position) {

        //holder.shortcutImage.setImageResource(shortcutList.get(position).getImageUrl());
        holder.name.setText(shortcutList.get(position).getRestorantname());
        //holder.restorantName.setText(shortcutList.get(position).getRestorantname());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ShortcutDetailsActivity.class);

                i.putExtra("videoName", shortcutList.get(holder.getAdapterPosition()).getName());
                i.putExtra("videoAddress", shortcutList.get(holder.getAdapterPosition()).getRestorantname());
                i.putExtra("screenSize", shortcutList.get(holder.getAdapterPosition()).getScreenSize());

                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return shortcutList.size();
    }

    public static final class ShortcutViewHolder extends RecyclerView.ViewHolder {

        //ImageView shortcutImage;
        TextView name, restorantName;

        public ShortcutViewHolder(@NonNull View itemView) {
            super(itemView);

            //shortcutImage = itemView.findViewById(R.id.shortcut_image);
            name = itemView.findViewById(R.id.name);
            //restorantName = itemView.findViewById(R.id.restorant_name);


            itemView.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeFragment.selectedImagePath = (String) name.getText();
                    System.out.println(HomeFragment.selectedImagePath);
                    newHome.uploadVideo(view);
                }
            });

        }
    }
}
