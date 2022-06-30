package com.example.mithraapplication;

import static com.example.mithraapplication.VideoScreen.videoModulesArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.SingleVideo;

import java.util.Locale;

public class FullScreenVideoView extends AppCompatActivity {

    private ImageView backButton, logoutButton;
    private TextView backTV, logoutTV, participantName;
    private Button englishButton, kannadaButton;
    private VideoView videoViewFullScreen;
    private MithraUtility mithraUtility = new MithraUtility();
    private SingleVideo singleVideo = new SingleVideo();
    private int pos = 0, position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_full_screen_view);
        RegisterViews();
        onClickOfBackButton();
        onClickOfLogoutButton();
        onClickOfLanguageButton();
        getCurrentLocale();

        pos = getIntent().getIntExtra("ModulePosition", 0);
        position = getIntent().getIntExtra("VideoPosition", 0);
    }

    private void RegisterViews(){

        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_participant));
        participantName = findViewById(R.id.participantNameVFSTV);
        participantName.setText(participantUserName);

        backButton = findViewById(R.id.backButtonVFS);
        logoutButton = findViewById(R.id.logoutVFSIcon);

        logoutTV = findViewById(R.id.logoutVFSTV);
        backTV = findViewById(R.id.backButtonTVVFS);

        englishButton = findViewById(R.id.englishVFSButton);
        kannadaButton = findViewById(R.id.kannadaVFSButton);

        videoViewFullScreen = findViewById(R.id.fullScreenVideoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mithra_introduction_video);
        MediaController mc = new MediaController(this);
        videoViewFullScreen.setMediaController(mc);
        videoViewFullScreen.setVideoURI(uri);
        videoViewFullScreen.requestFocus();
        videoViewFullScreen.start();
        videoViewFullScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                showAlertForFeedback();
                if(!(position + 1 >= videoModulesArrayList.get(pos).getSingleVideoArrayList().size())){
                    videoModulesArrayList.get(pos).getSingleVideoArrayList().get(position + 1).setVideoPlayed(true);
                    videoModulesArrayList.get(pos).getSingleVideoArrayList().get(position).setVideoStatus("Completed");
                }
            }
        });

    }

    private Bitmap generateThumbnailForVideo(){
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mithra_introduction_video);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this.getApplicationContext(), uri);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        return thumb;
    }

    private void showAlertForFeedback(){

            final AlertDialog.Builder dialog= new AlertDialog.Builder(FullScreenVideoView.this);
            final View customLayout = getLayoutInflater().inflate(R.layout.feedback_popup, null);

            Button yesFeedbackButton = customLayout.findViewById(R.id.yesFeedbackButton);
            Button noFeedbackButton = customLayout.findViewById(R.id.noFeedbackButton);

            ImageView imageViewThumbnail = customLayout.findViewById(R.id.feedbackImageOfVideo);
            if(imageViewThumbnail!=null){
                imageViewThumbnail.setBackground(new BitmapDrawable(getResources(), generateThumbnailForVideo()));
            }

            dialog.setView(customLayout).create();
            dialog.setCancelable(false);

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

            yesFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something when clicked
                    alertDialog.dismiss();
                    Intent loginIntent = new Intent(FullScreenVideoView.this, VideoScreen.class);
                    loginIntent.putExtra("FromActivity", "FullScreenVideoView");
                    loginIntent.putExtra("VideoFile", singleVideo);
                    startActivity(loginIntent);
                    finish();
                }});

            noFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something when clicked
                    alertDialog.dismiss();
                    Intent loginIntent = new Intent(FullScreenVideoView.this, VideoScreen.class);
                    loginIntent.putExtra("FromActivity", "FullScreenVideoView");
                    startActivity(loginIntent);
                    finish();
                }});
    }

    private void onClickOfBackButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, VideoScreen.class);
                intent.putExtra("FromActivity", "FullScreenVideoView");
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickOfLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenVideoView.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButton.setBackgroundResource(R.drawable.left_selected_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                kannadaButton.setBackgroundResource(R.drawable.right_unselected_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButton.setBackgroundResource(R.drawable.right_selected_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                englishButton.setBackgroundResource(R.drawable.left_unselected_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("kn");
            }
        });
    }

    /**
     * @param selectedLanguage
     * Description : This method is used to change the content of the screen to user selected language
     */
    public void changeLocalLanguage(String selectedLanguage){
        Locale myLocale = new Locale(selectedLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    public void getCurrentLocale(){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            kannadaButton.setBackgroundResource(R.drawable.right_selected_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_unselected_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_selected_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_unselected_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
        }
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logoutTV.setText(R.string.logout);
        backTV.setText(R.string.back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FullScreenVideoView.this, VideoScreen.class);
        intent.putExtra("FromActivity", "FullScreenVideoView");
        startActivity(intent);
        finish();
    }
}
