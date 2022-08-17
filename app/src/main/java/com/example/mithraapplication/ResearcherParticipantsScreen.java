package com.example.mithraapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.ParticipantScreenAdapter;
import com.example.mithraapplication.Adapters.ResearcherParticipantsAdapter;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.ResearcherParticipantsServerEvents;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class ResearcherParticipantsScreen extends AppCompatActivity implements ResearcherParticipantsServerEvents, HandleServerResponse {

    private Button englishButton, kannadaButton;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private TextView dashboardTV, participantTV, researcherTitleTV, researcherParticipantID, researcherParticipantDetails, researcherParticipantSurveysPending;
    private ImageView dashboardIcon, participantIcon;
    private RecyclerView participantsRecyclerView;
    private final MithraUtility mithraUtility = new MithraUtility();
    private PHQLocations phqLocations;
    private Dialog dialog;
    private ResearcherParticipantsAdapter researcherParticipantsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researcher_participants_list);
        startProgressBar();
        RegisterViews();
        getIntentData();
        callGetAllParticipantsDetails();
        onClickOfLanguageButton();
        getCurrentLocale();
    }

    private void RegisterViews() {
        englishButton = findViewById(R.id.englishButtonResearcher);
        kannadaButton = findViewById(R.id.kannadaButtonResearcher);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutResearcher);
        dashboardTV = findViewById(R.id.dashboardTVResearcher);
        dashboardIcon = findViewById(R.id.dashboardIconResearcher);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutResearcher);
        participantTV = findViewById(R.id.researcherParticipantsTV);
        participantIcon = findViewById(R.id.researcherParticipantsIcon);

        researcherTitleTV = findViewById(R.id.researcherParticipantsTitleTV);

        participantTV.setTextColor(getResources().getColor(R.color.text_color));
        participantLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantIcon.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black, ResearcherParticipantsScreen.this.getTheme()));


        researcherParticipantID = findViewById(R.id.researcherParticipantID);
        researcherParticipantDetails = findViewById(R.id.researcherParticipantDetails);
        researcherParticipantSurveysPending = findViewById(R.id.researcherParticipantSurveysPending);

        participantsRecyclerView = findViewById(R.id.researcherParticipantsRV);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
        }
    }

    private void startProgressBar(){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_progress_bar, null);

        ProgressBar progressbar = customLayout.findViewById(R.id.progressBar);

        dialog  = new Dialog(ResearcherParticipantsScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    private void stopProgressBar(){
        dialog.dismiss();
    }

    private void setRecyclerView(ArrayList<ParticipantDetails> registerParticipants){
        researcherParticipantsAdapter = new ResearcherParticipantsAdapter(ResearcherParticipantsScreen.this, registerParticipants, this::moveToQuestionnaireScreen);
        participantsRecyclerView.setLayoutManager(new LinearLayoutManager(ResearcherParticipantsScreen.this, LinearLayoutManager.VERTICAL, false){
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                stopProgressBar();
            }
        });
        participantsRecyclerView.setAdapter(researcherParticipantsAdapter);

    }

    private void moveToQuestionnaireScreen(ParticipantDetails registerParticipant){
        Intent participantIntent = new Intent(ResearcherParticipantsScreen.this, ResearcherQuestionnaireScreen.class);
        participantIntent.putExtra("RegisterParticipant Array", (Serializable) registerParticipant);
        participantIntent.putExtra("PHQLocations", (Serializable) phqLocations);
        startActivity(participantIntent);
    }

    private void callGetAllParticipantsDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.tracking.api.enrollstatus";
        ServerRequestAndResponse serverRequestAndResponse = new ServerRequestAndResponse();
        serverRequestAndResponse.setHandleServerResponse(this);
        serverRequestAndResponse.setResearcherParticipantsServerEvents(this);
        serverRequestAndResponse.getAllParticipantsDetailsResearcher(ResearcherParticipantsScreen.this, url);
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(v -> {
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
            changeLocalLanguage("en");
        });

        kannadaButton.setOnClickListener(v -> {
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
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
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherParticipantsScreen.this.getTheme()));
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
        participantTV.setText(R.string.participants);
        dashboardTV.setText(R.string.dashboard);
        researcherTitleTV.setText(R.string.participants);
        researcherParticipantID.setText(R.string.part_id);
        researcherParticipantDetails.setText(R.string.details);
        researcherParticipantSurveysPending.setText(R.string.survey_pending);
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(ResearcherParticipantsScreen.this, serverErrorResponse);
        }else{
            Toast.makeText(ResearcherParticipantsScreen.this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getAllParticipantsForResearcher(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ParticipantDetails>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        try{
            ArrayList<ParticipantDetails> registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
            registerParticipantsArrayList = registerParticipantsArrayList.stream()
                    .filter(ParticipantDetails -> ParticipantDetails.getShg_associate().equalsIgnoreCase(phqLocations.getSHGName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            if(registerParticipantsArrayList.size() == 0){
                stopProgressBar();
            }else{
                setRecyclerView(registerParticipantsArrayList);
            }
        }catch(Exception e){
            Toast.makeText(ResearcherParticipantsScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }
}
