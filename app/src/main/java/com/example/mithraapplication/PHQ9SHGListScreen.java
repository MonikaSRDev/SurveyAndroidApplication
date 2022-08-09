package com.example.mithraapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.Adapters.PHQSHGListAdapter;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.CoordinatorSHGServerEvents;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class PHQ9SHGListScreen extends AppCompatActivity implements HandleServerResponse, CoordinatorSHGServerEvents {

    private GridView phqGridView;
    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV, phqScreenTitle;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private final MithraUtility mithraUtility = new MithraUtility();
    private ArrayList<PHQLocations> phqLocations = new ArrayList<>();
    private Button englishButton, kannadaButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_shg_list);
        RegisterViews();
        moveToParticipantsScreen();
        moveToDashboardScreen();
        callGetCoordinatorSHGList();
        onClickOfLanguageButton();
    }

    private void RegisterViews() {
        phqGridView = findViewById(R.id.SHGListPHQScreening);

        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLL);
        phqScreeningTV = findViewById(R.id.phqScreeningTV);
        phqScreeningIcon = findViewById(R.id.phqScreeningIcon);

        phqScreeningTV.setTextColor(getResources().getColor(R.color.text_color));
        phqScreeningLinearlayout.setBackgroundResource(R.drawable.selected_page);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQ);
        dashboardTV = findViewById(R.id.dashboardTVPHQ);
        dashboardIcon = findViewById(R.id.dashboardIconPHQ);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQ);
        participantTV = findViewById(R.id.participantsTVPHQ);
        participantIcon = findViewById(R.id.participantsIconPHQ);

        englishButton = findViewById(R.id.englishButtonPHQ);
        kannadaButton = findViewById(R.id.kannadaButtonPHQ);

        phqScreenTitle = findViewById(R.id.dashboardTitleTVPHQ);
        phqScreenTitle.setText(R.string.phq_screening);

    }

    private void moveToParticipantsScreen(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, CoordinatorSHGList.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, DashboardScreen.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void moveToPHQParticipantsScreen(PHQLocations phqLocations){
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, PHQParticipantsScreen.class);
            participantIntent.putExtra("PHQLocations", (Serializable) phqLocations);
            startActivity(participantIntent);
    }

    private void setOnClickGridItemListener(){
        phqGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToPHQParticipantsScreen(phqLocations.get(position));
            }
        });
    }

    private void setGridViewAdapter(){
        setOnClickGridItemListener();
        PHQSHGListAdapter adapter = new PHQSHGListAdapter(this, 0, phqLocations);
        phqGridView.setAdapter(adapter);
    }

    private void callGetCoordinatorSHGList(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.co_loc_list";
        PHQLocations phqCoordinatorLocations = new PHQLocations();
        phqCoordinatorLocations.setCoordinatorName(mithraUtility.getSharedPreferencesData(PHQ9SHGListScreen.this, getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setCoordinatorSHGServerEvents(this);
        requestObject.getCoordinatorLocations(PHQ9SHGListScreen.this, phqCoordinatorLocations, url);
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

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void coordinatorSHGList(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PHQLocations>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null) {
            ArrayList<PHQLocations> phqLocationsArrayList;

            try {
                phqLocationsArrayList = gson.fromJson(jsonObject.get("message"), type);
                if(phqLocationsArrayList.size()!=0){
                    phqLocations = phqLocationsArrayList.stream()
                            .filter(phqLocations -> phqLocations.getActive().equalsIgnoreCase("yes"))
                            .collect(Collectors.toCollection(ArrayList::new));
                    setGridViewAdapter();
                }
            } catch (Exception e) {
                Toast.makeText(PHQ9SHGListScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(PHQ9SHGListScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }
}
