package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.FullScreenVideoView;
import com.example.mithraapplication.ModelClasses.SingleVideo;
import com.example.mithraapplication.R;

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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onBindViewHolder(@NonNull HorizontalVideoAdapter.ViewHolder holder, int position) {
        holder.singleVideoViewThumbnail.setImageBitmap(generateThumbnailForVideo());
        if(singleVideoArrayList.get(position).getVideoStatus().equals("Completed")){
            holder.videoStatusTV.setText(R.string.completed);
            holder.videoStatusTV.setTextColor(context.getResources().getColor(R.color.completed_color));
            holder.videoStatusIcon.setImageDrawable(context.getDrawable(R.drawable.completed_icon));
            onClickOfVideoPlayButton(holder, singleVideoArrayList.get(position));
            holder.videoCardView.setEnabled(true);
            holder.imageOverlayCardView.setVisibility(View.GONE);
        }else{
            holder.videoStatusTV.setText(R.string.pending);
            holder.videoStatusTV.setTextColor(context.getResources().getColor(R.color.pending_color));
            holder.videoStatusIcon.setImageDrawable(context.getDrawable(R.drawable.pending_icon));
            holder.videoCardView.setEnabled(false);
            holder.imageOverlayCardView.setVisibility(View.VISIBLE);

//            RenderEffect blurEffect = RenderEffect.createBlurEffect(16, 16, Shader.TileMode.MIRROR);
//            holder.videoCardView.setRenderEffect(blurEffect);
//            Blurry.with(context).sampling(2).onto(holder.videoCardView);
        }

        if(singleVideoArrayList.get(position).isVideoPlayed()){
            onClickOfVideoPlayButton(holder, singleVideoArrayList.get(position));
            holder.videoCardView.setEnabled(true);
            holder.imageOverlayCardView.setVisibility(View.GONE);
        }

    }

    private Bitmap generateThumbnailForVideo(){
        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.mithra_introduction_video);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context.getApplicationContext(), uri);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        return thumb;
    }

    private void onClickOfVideoPlayButton(ViewHolder holder, SingleVideo singleVideo) {
        holder.videoCardView.setOnClickListener(new View.OnClickListener() {
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
        CardView videoCardView;
        View imageOverlayCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            singleVideoViewThumbnail = itemView.findViewById(R.id.singleVideoView);
            videoPlayButton = itemView.findViewById(R.id.videoPlayButtonSingle);
            videoDescription = itemView.findViewById(R.id.videoDescription);
            videoStatusTV = itemView.findViewById(R.id.videoStatusTV);
            videoStatusIcon = itemView.findViewById(R.id.videoStatusIcon);
            videoCardView = itemView.findViewById(R.id.videoCardView);
            imageOverlayCardView = itemView.findViewById(R.id.imageOverlayCardView);
        }
    }
}
