package com.example.mithraapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.PHQParticipantsAdapter;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

public class PHQParticipantsScreen extends AppCompatActivity implements HandleServerResponse{

    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV, shgParticipantsTV, phqScreenTitle, PHQParticipantIDTV,
            PHQScreeningIDTV, PHQParticipantNameTV, PHQScoreTV, PHQEligibilityTV;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private PHQParticipantsAdapter phqParticipantsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PHQParticipantDetails> phqParticipantDetailsArrayList = new ArrayList<>();
    private PHQLocations phqLocations;
    private FloatingActionButton PHQForSurvey;
    private Button englishButton, kannadaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_participants_screen);
        RegisterViews();
//        initializeData();
        getIntentData();
        moveToDashboardScreen();
        moveToParticipantsScreen();
        setRecyclerView();
        setFloatingActionButton();
        callGetAllPHQParticipantsData();
        onClickOfLanguageButton();
    }

    private void initializeData() {
        PHQParticipantDetails phqParticipantDetails = new PHQParticipantDetails();
        phqParticipantDetails.setPHQParticipantName("Name1");
        phqParticipantDetails.setPHQScreeningID("PHQ234358765");
        phqParticipantDetails.setManualID("S0012");
        phqParticipantDetails.setPHQScreeningScore(1);
        phqParticipantDetails.setScreening_ID("Pending");

        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
        phqParticipantDetailsArrayList.add(phqParticipantDetails);
    }

    private void RegisterViews() {
        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLLParticipants);
        phqScreeningTV = findViewById(R.id.phqScreeningTVParticipants);
        phqScreeningIcon = findViewById(R.id.phqScreeningIconParticipants);

        phqScreeningTV.setTextColor(getResources().getColor(R.color.text_color));
        phqScreeningLinearlayout.setBackgroundResource(R.drawable.selected_page);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQParticipants);
        dashboardTV = findViewById(R.id.dashboardTVPHQParticipants);
        dashboardIcon = findViewById(R.id.dashboardIconPHQParticipants);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQParticipants);
        participantTV = findViewById(R.id.participantsTVPHQParticipants);
        participantIcon = findViewById(R.id.participantsIconPHQParticipants);

        shgParticipantsTV = findViewById(R.id.SHGPHQParticipantsTV);
        recyclerView = findViewById(R.id.PHQParticipantsRV);

        PHQForSurvey = findViewById(R.id.floatingActionButtonPHQ);

        englishButton = findViewById(R.id.englishButtonPHQParticipants);
        kannadaButton = findViewById(R.id.kannadaButtonPHQParticipants);

        phqScreenTitle = findViewById(R.id.dashboardTitleTVPHQParticipants);

        PHQParticipantIDTV = findViewById(R.id.PHQParticipantIDTV);
        PHQScreeningIDTV = findViewById(R.id.PHQScreeningIDTV);
        PHQParticipantNameTV = findViewById(R.id.PHQParticipantNameTV);
        PHQScoreTV = findViewById(R.id.PHQScoreTV);
        PHQEligibilityTV = findViewById(R.id.PHQEligibilityTV);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
            shgParticipantsTV.setText(phqLocations.getSHGName());
        }
    }

    private void setRecyclerView(){
        phqParticipantsAdapter = new PHQParticipantsAdapter(PHQParticipantsScreen.this, phqParticipantDetailsArrayList, new PHQParticipantsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PHQParticipantDetails participantDetails) {
                moveToPHQScreeningScreen(participantDetails);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(phqParticipantsAdapter);
    }

    private void moveToPHQScreeningScreen(PHQParticipantDetails participantDetails){
        Intent intent = new Intent(PHQParticipantsScreen.this, PHQScreeningScreen.class);
        intent.putExtra("FromActivity", "PHQParticipantScreen");
        intent.putExtra("PHQParticipantDetails", (Serializable) participantDetails);
        intent.putExtra("PHQLocations", (Serializable) phqLocations);
        startActivity(intent);
        finish();
    }

    private void setFloatingActionButton(){
        PHQForSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PHQParticipantsScreen.this, PHQScreeningScreen.class);
                intent.putExtra("FromActivity", "AddPHQParticipantSurvey");
                intent.putExtra("PHQLocations", (Serializable) phqLocations);
                startActivity(intent);
                finish();
            }
        });
    }

    private void moveToParticipantsScreen(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQParticipantsScreen.this, CoordinatorSHGList.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQParticipantsScreen.this, DashboardScreen.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void callGetAllPHQParticipantsData(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.phq9_scr_sub.api.pre_screenings";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQParticipantDetails(PHQParticipantsScreen.this, url);
    }


    @Override
    public void responseReceivedSuccessfully(String message) {
        Log.i("SurveyScreen", "responseReceivedSuccessfully");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PHQParticipantDetails>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null) {
            ArrayList<PHQParticipantDetails> phqParticipantDetails = new ArrayList<>();

            try {
                phqParticipantDetails = gson.fromJson(jsonObject.get("message"), type);
                if (phqParticipantDetails.size() > 1) {
//                    phqParticipantDetails.sort(Comparator.comparingInt(participantDetails -> Integer.parseInt(participantDetails.getPHQScreeningID())));
                    phqParticipantDetailsArrayList = phqParticipantDetails.stream()
                            .filter(participantDetails -> participantDetails.getSHGName().equalsIgnoreCase(phqLocations.getName()))
                            .collect(Collectors.toCollection(ArrayList::new));
                    setRecyclerView();
                }
            } catch (Exception e) {
                Toast.makeText(PHQParticipantsScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
            Type typeFrappe = new TypeToken<FrappeResponse>(){}.getType();
            if(jsonObjectRegistration.get("data")!=null) {
                FrappeResponse frappeResponse;
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), typeFrappe);
            }
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

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
        });

        kannadaButton.setOnClickListener(v -> {
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("kn");
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

        participantTV.setText(R.string.participants);
        dashboardTV.setText(R.string.dashboard);
        phqScreenTitle.setText(R.string.phq_screening);
        phqScreeningTV.setText(R.string.phq_screening);
        PHQParticipantIDTV.setText(R.string.phq_id);
        PHQScreeningIDTV.setText(R.string.screening_id);
        PHQParticipantNameTV.setText(R.string.name);
        PHQScoreTV.setText(R.string.phq_9_score);
        PHQEligibilityTV.setText(R.string.eligibility);

        super.onConfigurationChanged(newConfig);
    }
}