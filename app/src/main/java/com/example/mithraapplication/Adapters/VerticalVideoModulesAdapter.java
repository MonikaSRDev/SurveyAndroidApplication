package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.VideoModules;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class VerticalVideoModulesAdapter extends RecyclerView.Adapter<VerticalVideoModulesAdapter.ViewHolder> {

    private ArrayList<VideoModules> videoModules;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_scroll_view_video, parent, false);

        return new VerticalVideoModulesAdapter.ViewHolder(view);
    }

    public VerticalVideoModulesAdapter(Context context, ArrayList<VideoModules> videoModules){
        this.videoModules = videoModules;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.moduleNameTV.setText(videoModules.get(position).getVideoModuleName());
        holder.horizontalRecyclerView.setAdapter(new HorizontalVideoAdapter(context, videoModules.get(position).getSingleVideoArrayList(), position));
        holder.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.horizontalRecyclerView.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return videoModules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView moduleNameTV;
        RecyclerView horizontalRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleNameTV = itemView.findViewById(R.id.moduleNameTV);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRVVideos);
        }
    }
}
