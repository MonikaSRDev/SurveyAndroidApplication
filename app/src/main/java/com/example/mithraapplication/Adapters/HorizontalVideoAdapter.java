package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.FullScreenVideoView;
import com.example.mithraapplication.HandleFileDownloadResponse;
import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.SingleVideo;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.example.mithraapplication.VideoScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class HorizontalVideoAdapter extends RecyclerView.Adapter<HorizontalVideoAdapter.ViewHolder> implements HandleServerResponse, HandleFileDownloadResponse {

    private Context context;
    private ArrayList<SingleVideo> singleVideoArrayList;
    private int pos = 0;
    private MithraUtility mithraUtility = new MithraUtility();

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
            downloadFileFromServer(holder);
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

    private void downloadFileFromServer(ViewHolder holder){
        String path = context.getFilesDir().getAbsolutePath() + "/" + "videoplayback.mp4";
        File file = new File(path);
        if(file.exists()){
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
            holder.singleVideoViewThumbnail.setImageBitmap(bMap);
        }else{
            ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
            requestObject.setHandleFileDownloadResponse(this);
            requestObject.downloadFileRequest(context, "http://192.168.2.156:5000/return-files/");
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
                intent.putExtra("VideoPath", "http://192.168.2.156:5000/return-files/");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleVideoArrayList.size();
    }

    @Override
    public void responseReceivedSuccessfully(String message) {

    }

    @Override
    public void responseReceivedFailure(String message) {

    }

    @Override
    public void fileDownloadedSuccessfully(byte[] message)  {
        Log.i("VIDEODownload", "stop time :" + mithraUtility.getCurrentTime());
        FileOutputStream outputStream;
        String name="videoplayback.mp4";
        try{
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(message);
            outputStream.close();
            Toast.makeText(context, "File Downloaded Successfully", Toast.LENGTH_LONG).show();
        }catch(Exception ex){
           ex.printStackTrace();
        }

    }

    @Override
    public void fileDownloadFailure(String message) {
        Toast.makeText(context, "File Not Downloaded", Toast.LENGTH_LONG).show();
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
