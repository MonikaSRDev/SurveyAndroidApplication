package com.example.mithraapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class VideoFeedbackScreen extends AppCompatActivity {

    private ImageView backButton, logoutButton;
    private TextView backTV, logoutTV;
    private Button englishButton, kannadaButton;
    private ImageView videoThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_feedback_screen);
        RegisterViews();
        onClickOfBackButton();
        onClickOfLogoutButton();
    }

    private void RegisterViews(){
        backButton = findViewById(R.id.backButtonFBP);
        logoutButton = findViewById(R.id.logoutFBPIcon);

        logoutTV = findViewById(R.id.logoutFBPTV);
        backTV = findViewById(R.id.backButtonTVFBP);

        englishButton = findViewById(R.id.englishFBPButton);
        kannadaButton = findViewById(R.id.kannadaFBPButton);

        videoThumbnail = findViewById(R.id.videoThumbnailVFS);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mithra_introduction_video);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(uri.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
        videoThumbnail.setImageBitmap(thumb);
    }

    private void onClickOfBackButton(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoFeedbackScreen.this, VideoScreen.class);
                intent.putExtra("FromActivity", "VideoFeedbackScreen");
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickOfLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoFeedbackScreen.this, LoginScreen.class);
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
                englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
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
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
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
}