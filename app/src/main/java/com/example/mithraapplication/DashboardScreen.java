package com.example.mithraapplication;

import android.app.admin.DeviceAdminService;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.DashboardVerticalParticipantsAdapter;
import com.example.mithraapplication.Adapters.HorizontalDashboardAdapter;
import com.example.mithraapplication.Adapters.VerticalVideoModulesAdapter;
import com.example.mithraapplication.ModelClasses.ParticipantStatus;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class DashboardScreen extends AppCompatActivity implements HandleServerResponse{

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    private TextView dashboardTitleTV, dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard, villageNameDashboardTV,
            participantIDDashboard, participantDetailsDashboard, participantEnrollmentStatusDashboard, participantSurveyStatusDashboard, participantModuleStatusDashboard, participantReferralStatusDashboard;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;
    private MithraUtility mithraUtility = new MithraUtility();
    private ArrayList<ParticipantStatus> participantStatusArrayList = new ArrayList<>();
    private HorizontalDashboardAdapter horizontalDashboardAdapter;
    private DashboardVerticalParticipantsAdapter dashboardVerticalParticipantsAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);
        initializeData();
        RegisterViews();
        setRecyclerView();
        callGetAllParticipantsDetails();
        setOnClickForParticipants();
        onClickOfLanguageButton();
        onClickFloatingAddNewParticipantButton();
        getCurrentLocale();
    }

    private void initializeData(){
        ParticipantStatus participantStatus = new ParticipantStatus();
        participantStatus.setStatusName("Enrollment Status");
        participantStatus.setCompleted("90");
        participantStatus.setPending("70");
        participantStatus.setTotal("160");

        ParticipantStatus participantStatus1 = new ParticipantStatus();
        participantStatus1.setStatusName("Survey Status");
        participantStatus1.setCompleted("90");
        participantStatus1.setPending("70");
        participantStatus1.setTotal("160");

        ParticipantStatus participantStatus2 = new ParticipantStatus();
        participantStatus2.setStatusName("Priority Status");
        participantStatus2.setCompleted("90");
        participantStatus2.setPending("70");
        participantStatus2.setTotal("160");

        ParticipantStatus participantStatus3 = new ParticipantStatus();
        participantStatus3.setStatusName("Module Status");
        participantStatus3.setCompleted("90");
        participantStatus3.setPending("70");
        participantStatus3.setTotal("160");

        ParticipantStatus participantStatus4 = new ParticipantStatus();
        participantStatus4.setStatusName("Referral Status");
        participantStatus4.setCompleted("90");
        participantStatus4.setPending("70");
        participantStatus4.setTotal("160");

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
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color));
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
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black));

        floatingActionButton = findViewById(R.id.floatingActionButtonDashboard);

    }

    private void setRecyclerView(){
        horizontalDashboardAdapter = new HorizontalDashboardAdapter(this, participantStatusArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setAdapter(horizontalDashboardAdapter);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

    }

    private void setVerticalRecyclerView(ArrayList<RegisterParticipant> participantArrayList){
        dashboardVerticalParticipantsAdapter = new DashboardVerticalParticipantsAdapter(this, participantArrayList, new DashboardVerticalParticipantsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(RegisterParticipant registerParticipant) {
                Intent intent = new Intent(DashboardScreen.this, DashboardParticipantDetailsScreen.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        verticalRecyclerView.setAdapter(dashboardVerticalParticipantsAdapter);
        verticalRecyclerView.setLayoutManager(linearLayoutManager);

    }


    private void onClickFloatingAddNewParticipantButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardScreen.this, ParticipantProfileScreen.class);
                startActivity(intent);
            }
        });
    }

    private void setOnClickForParticipants(){
        participantLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(DashboardScreen.this, ParticipantsScreen.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
                englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
                kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButtonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
                kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
                englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
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
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
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
        dashboardTitleTV.setText(R.string.dashboard);
        participantTVDashboard.setText(R.string.participants);
        dashboardTVDashboard.setText(R.string.dashboard);
        villageNameDashboardTV.setText(R.string.village);
        participantIDDashboard.setText(R.string.part_id);
        participantEnrollmentStatusDashboard.setText(R.string.enrollment_status);
        participantDetailsDashboard.setText(R.string.details);
        participantModuleStatusDashboard.setText(R.string.module_status);
        participantSurveyStatusDashboard.setText(R.string.survey_status);
        participantReferralStatusDashboard.setText(R.string.referral_status);

    }

    private void callGetAllParticipantsDetails(){
        String url = "http://" + getString(R.string.base_url) +"/api/method/mithra.mithra.doctype.participant.api.participants";
        ServerRequestAndResponse serverRequestAndResponse = new ServerRequestAndResponse();
        serverRequestAndResponse.setHandleServerResponse(this);
        serverRequestAndResponse.getAllParticipantsDetails(DashboardScreen.this, url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RegisterParticipant>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<RegisterParticipant> registerParticipantsArrayList;

        try{
            registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
            if(registerParticipantsArrayList.size() == 0){
                floatingActionButton.setVisibility(View.GONE);
            }else{
                floatingActionButton.setVisibility(View.VISIBLE);
                setVerticalRecyclerView(registerParticipantsArrayList);
            }
        }catch(Exception e){
            Toast.makeText(DashboardScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
