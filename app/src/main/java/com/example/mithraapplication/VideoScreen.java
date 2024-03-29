package com.example.mithraapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.Adapters.VerticalVideoModulesAdapter;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.ModelClasses.SingleVideo;
import com.example.mithraapplication.ModelClasses.VideoModules;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Locale;

public class VideoScreen extends AppCompatActivity implements HandleServerResponse {

    public static ArrayList<VideoModules> videoModulesArrayList = new ArrayList<>();
    private RecyclerView verticalRecyclerView;
    private VerticalVideoModulesAdapter verticalVideoModulesAdapter;
    private ImageView backButton, logoutButton;
    private Button englishButton, kannadaButton;
    private TextView logoutTV, backTV, participantName;
    private MithraUtility mithraUtility = new MithraUtility();
    private TextView videoDescription;
    private RelativeLayout logoutButtonVP, backButtonVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BlurKit.init(this);
        setContentView(R.layout.activity_video_screen);
        initializeData();
        RegisterViews();
        setRecyclerView();
        onClickOfBackButton();
        onClickOfLogoutButton();
        getCurrentLocale();
        onClickOfLanguageButton();
    }

    private void initializeData(){
        SingleVideo singleVideo = new SingleVideo();
        singleVideo.setVideoName("Sample Video");
        singleVideo.setVideoPath("");
        singleVideo.setVideoDescription("Introductory Video");
        singleVideo.setVideoStatus("Completed");
        singleVideo.setVideoPlayed(true);

        SingleVideo singleVideo1 = new SingleVideo();
        singleVideo1.setVideoName("Sample Video");
        singleVideo1.setVideoPath("");
        singleVideo1.setVideoDescription("Introductory Video");
        singleVideo1.setVideoStatus("Pending");
        singleVideo1.setVideoPlayed(false);

        SingleVideo singleVideo2 = new SingleVideo();
        singleVideo2.setVideoName("Sample Video");
        singleVideo2.setVideoPath("");
        singleVideo2.setVideoDescription("Introductory Video");
        singleVideo2.setVideoStatus("Pending");
        singleVideo2.setVideoPlayed(false);

        SingleVideo singleVideo3 = new SingleVideo();
        singleVideo3.setVideoName("Sample Video");
        singleVideo3.setVideoPath("");
        singleVideo3.setVideoDescription("Introductory Video");
        singleVideo3.setVideoStatus("Pending");
        singleVideo3.setVideoPlayed(false);

        SingleVideo singleVideo4 = new SingleVideo();
        singleVideo4.setVideoName("Sample Video");
        singleVideo4.setVideoPath("");
        singleVideo4.setVideoDescription("Introductory Video");
        singleVideo4.setVideoStatus("Pending");
        singleVideo4.setVideoPlayed(false);

        SingleVideo singleVideo5 = new SingleVideo();
        singleVideo5.setVideoName("Sample Video");
        singleVideo5.setVideoPath("");
        singleVideo5.setVideoDescription("Introductory Video");
        singleVideo5.setVideoStatus("Pending");
        singleVideo5.setVideoPlayed(false);

        SingleVideo singleVideo6 = new SingleVideo();
        singleVideo6.setVideoName("Sample Video");
        singleVideo6.setVideoPath("");
        singleVideo6.setVideoDescription("Introductory Video");
        singleVideo6.setVideoStatus("Pending");
        singleVideo6.setVideoPlayed(false);

        SingleVideo singleVideo7 = new SingleVideo();
        singleVideo7.setVideoName("Sample Video");
        singleVideo7.setVideoPath("");
        singleVideo7.setVideoDescription("Introductory Video");
        singleVideo7.setVideoStatus("Pending");
        singleVideo7.setVideoPlayed(false);

        ArrayList<SingleVideo> singleVideoArrayList = new ArrayList<>();
        singleVideoArrayList.add(singleVideo);
        singleVideoArrayList.add(singleVideo1);
        singleVideoArrayList.add(singleVideo2);
        singleVideoArrayList.add(singleVideo3);

        ArrayList<SingleVideo> singleVideoArrayList1 = new ArrayList<>();
        singleVideoArrayList1.add(singleVideo4);
        singleVideoArrayList1.add(singleVideo5);
        singleVideoArrayList1.add(singleVideo6);
        singleVideoArrayList1.add(singleVideo7);

        VideoModules videoModules = new VideoModules();
        videoModules.setVideoModuleName("Module 1");
        videoModules.setSingleVideoArrayList(singleVideoArrayList);

        VideoModules videoModules1 = new VideoModules();
        videoModules1.setVideoModuleName("Module 2");
        videoModules1.setSingleVideoArrayList(singleVideoArrayList1);

        if(videoModulesArrayList.size() == 0){
            videoModulesArrayList.add(videoModules);
            videoModulesArrayList.add(videoModules1);
        }
    }

    private void RegisterViews(){
        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_participant));
        participantName = findViewById(R.id.participantNameTVVP);
        participantName.setText(participantUserName);

        verticalRecyclerView = findViewById(R.id.verticalRVVideos);
        backButton = findViewById(R.id.backButton);
        logoutButton = findViewById(R.id.logoutVPIcon);

        englishButton = findViewById(R.id.englishVPButton);
        kannadaButton = findViewById(R.id.kannadaVPButton);

        logoutTV = findViewById(R.id.logoutVPTV);
        backTV = findViewById(R.id.backButtonTV);
        videoDescription = findViewById(R.id.videoDescription);

        logoutButtonVP = findViewById(R.id.logoutButtonVP);
        backButtonVP = findViewById(R.id.backButtonVP);
    }

    private void setRecyclerView(){
        verticalVideoModulesAdapter = new VerticalVideoModulesAdapter(this, videoModulesArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        verticalRecyclerView.setLayoutManager(linearLayoutManager);
        verticalRecyclerView.setAdapter(verticalVideoModulesAdapter);
    }

    private void onClickOfBackButton(){
        backButtonVP.setOnClickListener(v -> {
            Intent intent = new Intent(VideoScreen.this, ParticipantLandingScreen.class);
            intent.putExtra("FromActivity", "VideoScreen");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void onClickOfLogoutButton(){
        logoutButtonVP.setOnClickListener(v -> {
            Intent intent = new Intent(VideoScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(v -> {
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("en");
            verticalVideoModulesAdapter.notifyDataSetChanged();
        });

        kannadaButton.setOnClickListener(v -> {
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("kn");
            verticalVideoModulesAdapter.notifyDataSetChanged();
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logoutTV.setText(R.string.logout);
        backTV.setText(R.string.back);
        if(videoDescription!=null){
            videoDescription.setText(R.string.default_text);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VideoScreen.this, ParticipantLandingScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("FromActivity", "VideoScreen");
        startActivity(intent);
        finish();
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(this, serverErrorResponse);
        }else{
            Toast.makeText(this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}