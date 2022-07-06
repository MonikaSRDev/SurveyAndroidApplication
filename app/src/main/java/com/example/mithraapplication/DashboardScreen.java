package com.example.mithraapplication;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.HorizontalDashboardAdapter;
import com.example.mithraapplication.ModelClasses.ParticipantStatus;

import java.util.ArrayList;
import java.util.Locale;

public class DashboardScreen extends AppCompatActivity {

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private RecyclerView horizontalRecyclerView;
    private TextView dashboardTitleTV, dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard, villageNameDashboardTV;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;
    private MithraUtility mithraUtility = new MithraUtility();
    private ArrayList<ParticipantStatus> participantStatusArrayList = new ArrayList<>();
    private HorizontalDashboardAdapter horizontalDashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);
        initializeData();
        RegisterViews();
        setRecyclerView();
        setOnClickForParticipants();
        onClickOfLanguageButton();
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

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDashboard);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDashboard);

        dashboardTitleTV = findViewById(R.id.dashboardTitleTV);
        dashboardTVDashboard = findViewById(R.id.dashboardTVDashboard);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color));
        participantTVDashboard = findViewById(R.id.participantsTVDashboard);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDashboard);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVDashboard.setText(coordinatorUserName);
        }

        villageNameDashboardTV = findViewById(R.id.villageSpinnerDashboard);

        mithraLogoDashboard = findViewById(R.id.appLogoDashboard);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDashboard);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDashboard);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDashboard);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black));

    }

    private void setRecyclerView(){
        horizontalDashboardAdapter = new HorizontalDashboardAdapter(this, participantStatusArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setAdapter(horizontalDashboardAdapter);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager);

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
                englishButtonDashboard.setBackgroundResource(R.drawable.left_selected_toggle_button);
                englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                kannadaButtonDashboard.setBackgroundResource(R.drawable.right_unselected_toggle_button);
                kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButtonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButtonDashboard.setBackgroundResource(R.drawable.right_selected_toggle_button);
                kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
                englishButtonDashboard.setBackgroundResource(R.drawable.left_unselected_toggle_button);
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
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_selected_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_unselected_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonDashboard.setBackgroundResource(R.drawable.left_selected_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_unselected_toggle_button);
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
    }
}
