package com.example.mithraapplication;

import static com.example.mithraapplication.VideoScreen.videoModulesArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.SingleVideo;
import java.util.ArrayList;

public class HorizontalVideoAdapter extends RecyclerView.Adapter<HorizontalVideoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SingleVideo> singleVideoArrayList;
    private int pos = 0;

    @NonNull
    @Override
    public HorizontalVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_row, parent, false);

        return new HorizontalVideoAdapter.ViewHolder(view);
    }

    public HorizontalVideoAdapter(Context context, ArrayList<SingleVideo> singleVideoArrayList, int pos){
        this.singleVideoArrayList = singleVideoArrayList;
        this.context = context;
        this.pos = pos;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalVideoAdapter.ViewHolder holder, int position) {
        holder.singleVideoViewThumbnail.setImageBitmap(generateThumbnailForVideo(singleVideoArrayList.get(position).getVideoPath()));
        if(singleVideoArrayList.get(position).getVideoStatus().equals("Completed")){
            holder.videoStatusTV.setText(R.string.completed);
            holder.videoStatusTV.setTextColor(context.getResources().getColor(R.color.completed_color));
            holder.videoStatusIcon.setImageDrawable(context.getDrawable(R.drawable.completed_icon));
            onClickOfVideoPlayButton(holder, singleVideoArrayList.get(position));
            holder.videoPlayButton.setEnabled(true);
        }else{
            holder.videoStatusTV.setText(R.string.pending);
            holder.videoStatusTV.setTextColor(context.getResources().getColor(R.color.pending_color));
            holder.videoStatusIcon.setImageDrawable(context.getDrawable(R.drawable.pending_icon));
            holder.videoPlayButton.setEnabled(false);
        }

        if(singleVideoArrayList.get(position).isVideoPlayed()){
            onClickOfVideoPlayButton(holder, singleVideoArrayList.get(position));
            holder.videoPlayButton.setEnabled(true);
        }

    }

    private Bitmap generateThumbnailForVideo(String path){
        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.mithra_introduction_video);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context.getApplicationContext(), uri);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        return thumb;
    }

    private void onClickOfVideoPlayButton(ViewHolder holder, SingleVideo singleVideo) {
        holder.videoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullScreenVideoView.class);
                intent.putExtra("ModulePosition", pos);
                intent.putExtra("VideoPosition", holder.getAbsoluteAdapterPosition());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleVideoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView singleVideoViewThumbnail, videoPlayButton;
        TextView videoDescription, videoStatusTV;
        ImageView videoStatusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            singleVideoViewThumbnail = itemView.findViewById(R.id.singleVideoView);
            videoPlayButton = itemView.findViewById(R.id.videoPlayButtonSingle);
            videoDescription = itemView.findViewById(R.id.videoDescription);
            videoStatusTV = itemView.findViewById(R.id.videoStatusTV);
            videoStatusIcon = itemView.findViewById(R.id.videoStatusIcon);
        }
    }
}
