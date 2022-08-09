package com.example.mithraapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.DashboardVerticalParticipantsAdapter;
import com.example.mithraapplication.Adapters.HorizontalDashboardAdapter;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.DashboardServerEvents;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.ModelClasses.ParticipantStatus;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class DashboardScreen extends AppCompatActivity implements HandleServerResponse, DashboardServerEvents {

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    private TextView dashboardTitleTV, dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard, villageNameDashboardTV,
            participantIDDashboard, participantDetailsDashboard, participantEnrollmentStatusDashboard, participantSurveyStatusDashboard,
            participantModuleStatusDashboard, participantReferralStatusDashboard, addParticipantTV, PHQTextView;
    private LinearLayout dashboardLinearLayout, participantLinearLayout, PHQLinearLayout;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard, addParticipantIcon, PHQScreeningIcon;
    private final MithraUtility mithraUtility = new MithraUtility();
    private ArrayList<ParticipantStatus> participantStatusArrayList = new ArrayList<>();
    private HorizontalDashboardAdapter horizontalDashboardAdapter;
    private DashboardVerticalParticipantsAdapter dashboardVerticalParticipantsAdapter;
//    private FloatingActionButton floatingActionButton;
    private ArrayList<TrackingParticipantStatus> trackingParticipantStatusArrayList = new ArrayList<>();
    private ArrayList<ParticipantDetails> registerParticipantsArrayList = new ArrayList<>();
    private ArrayList<ParticipantDetails> oldParticipantsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);
        RegisterViews();
        callGetCardDetails();
//        callGetAllParticipantsDetails();
        callGetParticipantDetails();
        setOnClickForParticipants();
        onClickOfLanguageButton();
        onClickFloatingAddNewParticipantButton();
        getCurrentLocale();
        onClickOfPHQScreening();

        mithraLogoDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardScreen.this, QuestionnaireSingleScreen.class);
            startActivity(intent);
        });
    }

    private void initializeData(ParticipantStatus participantStatus){
        if(participantStatus!=null){
            participantStatus.setStatusName("Enrollment Status");
        }

        ParticipantStatus participantStatus1 = new ParticipantStatus();
        participantStatus1.setStatusName("Survey Status");
        participantStatus1.setEnroll_completed("90");
        participantStatus1.setEnroll_remaining("70");
        participantStatus1.setEnroll_total("160");

        ParticipantStatus participantStatus2 = new ParticipantStatus();
        participantStatus2.setStatusName("Priority Status");
        participantStatus2.setEnroll_completed("90");
        participantStatus2.setEnroll_remaining("70");
        participantStatus2.setEnroll_total("160");

        ParticipantStatus participantStatus3 = new ParticipantStatus();
        participantStatus3.setStatusName("Module Status");
        participantStatus3.setEnroll_completed("90");
        participantStatus3.setEnroll_remaining("70");
        participantStatus3.setEnroll_total("160");

        ParticipantStatus participantStatus4 = new ParticipantStatus();
        participantStatus4.setStatusName("Referral Status");
        participantStatus4.setEnroll_completed("90");
        participantStatus4.setEnroll_remaining("70");
        participantStatus4.setEnroll_total("160");

        participantStatusArrayList.add(participantStatus);
        participantStatusArrayList.add(participantStatus1);
        participantStatusArrayList.add(participantStatus2);
        participantStatusArrayList.add(participantStatus3);
        participantStatusArrayList.add(participantStatus4);
    }

    private void RegisterViews() {

        englishButtonDashboard = findViewById(R.id.englishButtonDashboard);
        kannadaButtonDashboard = findViewById(R.id.kannadaButtonDashboard);

        horizontalRecyclerView = findViewById(R.id.dashboardHorizontalRV);
        verticalRecyclerView = findViewById(R.id.dashboardVerticalRV);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDashboard);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDashboard);

        dashboardTitleTV = findViewById(R.id.dashboardTitleTV);
        dashboardTVDashboard = findViewById(R.id.dashboardTVDashboard);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color, DashboardScreen.this.getTheme()));
        participantTVDashboard = findViewById(R.id.participantsTVDashboard);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDashboard);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVDashboard.setText(coordinatorUserName);
        }

        villageNameDashboardTV = findViewById(R.id.villageSpinnerDashboard);

        participantIDDashboard = findViewById(R.id.participantIDDashboard);
        participantDetailsDashboard = findViewById(R.id.participantDetailsDashboard);
        participantEnrollmentStatusDashboard = findViewById(R.id.participantEnrollmentStatusDashboard);
        participantModuleStatusDashboard = findViewById(R.id.participantModuleStatusDashboard);
        participantSurveyStatusDashboard = findViewById(R.id.participantSurveyStatusDashboard);
        participantReferralStatusDashboard = findViewById(R.id.participantReferralStatusDashboard);

        mithraLogoDashboard = findViewById(R.id.appLogoDashboard);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDashboard);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDashboard);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDashboard);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black, DashboardScreen.this.getTheme()));

        addParticipantTV = findViewById(R.id.addNewParticipantTVDashboard);
        addParticipantTV.setVisibility(View.GONE);
        addParticipantIcon = findViewById(R.id.addParticipantIconDashboard);
        addParticipantIcon.setVisibility(View.GONE);

        PHQLinearLayout = findViewById(R.id.phqScreeningLLDashboard);
        PHQTextView = findViewById(R.id.phqScreeningTVDashboard);
        PHQScreeningIcon = findViewById(R.id.phqScreeningIconDashboard);

//        floatingActionButton = findViewById(R.id.floatingActionButtonDashboard);
//        floatingActionButton.setEnabled(false);
//        floatingActionButton.hide();

    }

    private void setRecyclerView(){
        horizontalDashboardAdapter = new HorizontalDashboardAdapter(this, participantStatusArrayList, participantStatus -> {
            if(participantStatus.equalsIgnoreCase("complete")){
                ArrayList<ParticipantDetails> participantDetails = getCompletedParticipantList(registerParticipantsArrayList);
                setVerticalRecyclerView(participantDetails);
            }else if(participantStatus.equalsIgnoreCase("pending")){
                ArrayList<ParticipantDetails> participantDetails = getPendingParticipantList(registerParticipantsArrayList);
                setVerticalRecyclerView(participantDetails);
            }else{
                setVerticalRecyclerView(registerParticipantsArrayList);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setAdapter(horizontalDashboardAdapter);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

    }

    private void setVerticalRecyclerView(ArrayList<ParticipantDetails> participantArrayList){
        dashboardVerticalParticipantsAdapter = new DashboardVerticalParticipantsAdapter(this, participantArrayList, this::moveToParticipantDetailsScreen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        verticalRecyclerView.setAdapter(dashboardVerticalParticipantsAdapter);
        verticalRecyclerView.setLayoutManager(linearLayoutManager);

    }

    @NonNull
    public static ArrayList<ParticipantDetails> getCompletedParticipantList(ArrayList<ParticipantDetails> participantDetailsArrayList)
    {
        return participantDetailsArrayList.stream()
                .filter(participantDetails -> participantDetails.getEnroll().equalsIgnoreCase("yes")).collect(Collectors.toCollection(ArrayList::new));
    }

    @NonNull
    public static ArrayList<ParticipantDetails> getPendingParticipantList(ArrayList<ParticipantDetails> participantDetailsArrayList)
    {
        return participantDetailsArrayList.stream()
                .filter(participantDetails -> !participantDetails.getEnroll().equalsIgnoreCase("yes")).collect(Collectors.toCollection(ArrayList::new));
    }

    private void moveToParticipantDetailsScreen(ParticipantDetails participantDetails){
        Intent participantIntent = new Intent(DashboardScreen.this, DashboardParticipantDetailsScreen.class);
        participantIntent.putExtra("RegisterParticipant Array", (Serializable) participantDetails);
        startActivity(participantIntent);
        finish();
    }

    private void moveToPHQScreeningPage(){
        Intent participantIntent = new Intent(DashboardScreen.this, PHQ9SHGListScreen.class);
        startActivity(participantIntent);
    }

    private void onClickOfPHQScreening(){
        PHQLinearLayout.setOnClickListener(v -> moveToPHQScreeningPage());
    }

    private void setOnClickForParticipants(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent loginIntent = new Intent(DashboardScreen.this, CoordinatorSHGList.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void onClickFloatingAddNewParticipantButton() {
        addParticipantIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardScreen.this, ParticipantProfileScreen.class);
            startActivity(intent);
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonDashboard.setOnClickListener(v -> {
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
            changeLocalLanguage("en");
        });

        kannadaButtonDashboard.setOnClickListener(v -> {
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
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
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
        }else{
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardScreen.this.getTheme()));
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
        dashboardTitleTV.setText(R.string.dashboard);
        participantTVDashboard.setText(R.string.participants);
        dashboardTVDashboard.setText(R.string.dashboard);
        PHQTextView.setText(R.string.phq_screening);
        villageNameDashboardTV.setText(R.string.village);
        participantIDDashboard.setText(R.string.part_id);
        participantEnrollmentStatusDashboard.setText(R.string.enrollment_status);
        participantDetailsDashboard.setText(R.string.details);
        participantModuleStatusDashboard.setText(R.string.module_status);
        participantSurveyStatusDashboard.setText(R.string.survey_status);
        participantReferralStatusDashboard.setText(R.string.referral_status);

    }

    private void callGetCardDetails(){
        String url = "http://" + getString(R.string.base_url) +"/api/method/mithra.mithra.doctype.tracking.api.card_data";
        ServerRequestAndResponse serverRequestAndResponse = new ServerRequestAndResponse();
        serverRequestAndResponse.setHandleServerResponse(this);
        serverRequestAndResponse.setDashboardServerEvents(this);
        serverRequestAndResponse.getCardDetails(DashboardScreen.this, url);
    }

    private void callGetParticipantDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.tracking.api.enrollstatus";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setDashboardServerEvents(this);
        requestObject.getParticipantDetails(DashboardScreen.this, url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void getCardDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null){
            Type typeCardDetails = new TypeToken<ParticipantStatus>(){}.getType();
            ParticipantStatus participantStatus = gson.fromJson(jsonObject.get("message"), typeCardDetails);
            initializeData(participantStatus);
            setRecyclerView();
        }else{
            Toast.makeText(DashboardScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getParticipantDetails(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ParticipantDetails>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null){
            try{
                registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
                oldParticipantsArrayList = registerParticipantsArrayList;
                if(registerParticipantsArrayList.size() == 0){
//                    floatingActionButton.setVisibility(View.GONE);
                    addParticipantTV.setVisibility(View.GONE);
                    addParticipantIcon.setVisibility(View.GONE);
                }else{
//                    floatingActionButton.setVisibility(View.VISIBLE);
                    addParticipantTV.setVisibility(View.GONE);
                    addParticipantIcon.setVisibility(View.GONE);
                    setVerticalRecyclerView(registerParticipantsArrayList);
                }
            }catch(Exception e){
                Toast.makeText(DashboardScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(DashboardScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
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
