package com.example.try1.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.try1.HomeFragment;
import com.example.try1.R;
import com.example.try1.ShortcutDetailsActivity;
import com.example.try1.model.Shortcut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
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
        holder.name.setText(shortcutList.get(position).getName());
        //holder.restorantName.setText(shortcutList.get(position).getRestorantname());
        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("ACTIONS", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ACTION_RESULT", shortcutList.get(holder.getAdapterPosition()).getScreenSize());
                editor.apply();
                System.out.println("explore upload button clicked");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ShortcutDetailsActivity.class);

                i.putExtra("videoName", shortcutList.get(holder.getAdapterPosition()).getName());
                i.putExtra("videoAddress", shortcutList.get(holder.getAdapterPosition()).getRestorantname());

                i.putExtra("actions", shortcutList.get(holder.getAdapterPosition()).getActions());


                String extractedAction = "";

                String actions = shortcutList.get(holder.getAdapterPosition()).getScreenSize();
                System.out.println(actions);
                try {
                    JSONArray metaData = new JSONArray(actions);
                    int n = metaData.length();
                    for (int x=0; x<n; ++x) {
                        JSONObject singleAction = metaData.getJSONObject(x);
                        extractedAction = extractedAction + singleAction.getString("act_type") + " ";
                        ArrayList<String> list = new ArrayList<String>();
                        JSONArray actionPiece = singleAction.getJSONArray("taps");
                        for(int y=0;y<1;y++) {
                            list.add(actionPiece.get(y).toString());
                        }
                        extractedAction = extractedAction + list.get(0) + ", ";
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                i.putExtra("screenSize", extractedAction);
                i.putExtra("actual_action", shortcutList.get(holder.getAdapterPosition()).getScreenSize());
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
        Button upload;
        VideoView videoView;

        public ShortcutViewHolder(@NonNull View itemView) {
            super(itemView);

            //shortcutImage = itemView.findViewById(R.id.shortcut_image);
            name = itemView.findViewById(R.id.name);
            //restorantName = itemView.findViewById(R.id.restorant_name);
            upload = itemView.findViewById(R.id.action);
            videoView = itemView.findViewById(R.id.shortcut_video_view);

//            itemView.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    HomeFragment.selectedImagePath = (String) name.getText();
//                    System.out.println(HomeFragment.selectedImagePath);
//                    newHome.uploadVideo(view);
//                }
//            });

        }
    }
}
