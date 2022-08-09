package com.example.mithraapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class DashboardParticipantDetailsScreen extends AppCompatActivity implements HandleServerResponse {

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private LinearLayout dashboardLinearLayout, participantLinearLayout, PHQLinearLayout;
    private TextView dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard, participantName,
            participantAge, participantPhoneNum, participantPanchayat, participantSHG, participantVillage, phqScreeningDashboardTV;
    private TextView preScreeningTV, registrationTV, socioDemographyTV, diseaseProfileTV, enrolmentStatusTV, surveyStatusTV, moduleStatusTV, referralStatusTV,
            PHQ9TV, badsTV, quidsTV, whodasTV, module0TV, module1TV, module2TV, module3TV, module4TV, module5TV, module6TV, module7TV;
    private ImageView preScreeningIV, registrationIV, socioDemographyIV, diseaseProfileIV;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;
    private final MithraUtility mithraUtility = new MithraUtility();
    private ParticipantDetails participantDetails;
//    private ArrayList<TrackingParticipantStatus> trackingParticipantStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_participant_details);
        RegisterViews();
        getIntentData();
//        callGetParticipantTrackingDetails();
        onClickOfDashboard();
        setOnClickForParticipants();
        onClickOfPHQScreening();
        getCurrentLocale();
        onClickOfLanguageButton();
    }

    private void RegisterViews(){
        englishButtonDashboard = findViewById(R.id.englishButtonDP);
        kannadaButtonDashboard = findViewById(R.id.kannadaButtonDP);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDP);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDP);

        PHQLinearLayout = findViewById(R.id.phqScreeningLLDP);

        dashboardTVDashboard = findViewById(R.id.dashboardTVDP);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color, this.getTheme()));
        participantTVDashboard = findViewById(R.id.participantsTVDP);
        phqScreeningDashboardTV = findViewById(R.id.phqScreeningTVDP);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDP);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVDashboard.setText(coordinatorUserName);
        }

        mithraLogoDashboard = findViewById(R.id.appLogoDP);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDP);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDP);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDP);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black, this.getTheme()));

        participantName = findViewById(R.id.participantNameDPTV);
        participantAge = findViewById(R.id.participantAgeDPTV);
        participantPhoneNum = findViewById(R.id.participantMobileDPTV);
        participantPanchayat = findViewById(R.id.participantAddress1DPTV);
        participantSHG = findViewById(R.id.participantAddress2DPTV);
        participantVillage = findViewById(R.id.participantVillageDPTV);

        preScreeningTV = findViewById(R.id.preScreeningTVDP);
        registrationTV = findViewById(R.id.registrationTVDP);
        socioDemographyTV = findViewById(R.id.socioDemographyTVDP);
        diseaseProfileTV = findViewById(R.id.diseaseProfileTVDP);
        preScreeningIV = findViewById(R.id.preScreeningIconDP);
        registrationIV = findViewById(R.id.registrationIconDP);
        socioDemographyIV = findViewById(R.id.socioDemographyIconDP);
        diseaseProfileIV = findViewById(R.id.diseaseProfileIconDP);

        enrolmentStatusTV = findViewById(R.id.enrollmentStatusDP);
        surveyStatusTV = findViewById(R.id.surveyStatusDP);
        moduleStatusTV = findViewById(R.id.moduleStatusDP);
        referralStatusTV = findViewById(R.id.referralStatusTVDP);
        PHQ9TV = findViewById(R.id.phq9TVDP);
        badsTV = findViewById(R.id.badsTVDP);
        quidsTV = findViewById(R.id.quidsTVDP);
        whodasTV = findViewById(R.id.whodasTVDP);
        module0TV = findViewById(R.id.module0TVDP);
        module1TV = findViewById(R.id.module1TVDP);
        module2TV = findViewById(R.id.module2TVDP);
        module3TV = findViewById(R.id.module3TVDP);
        module4TV = findViewById(R.id.module4TVDP);
        module5TV = findViewById(R.id.module5TVDP);
        module6TV = findViewById(R.id.module6TVDP);
        module7TV = findViewById(R.id.module7TVDP);


    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            participantDetails = (ParticipantDetails) intent.getSerializableExtra("RegisterParticipant Array");
            setParticipantDetails();
        }
    }

    private void setParticipantDetails(){
        participantName.setText(participantDetails.getFull_name());
        participantAge.setText(participantDetails.getAge());
        String phoneNum = null;
        Phonenumber.PhoneNumber phoneNumber = mithraUtility.getCountryCodeAndNumber(participantDetails.getMobile_number());
        if(phoneNumber!=null){
            phoneNum = phoneNumber.getCountryCode() + String.valueOf(phoneNumber.getNationalNumber());
            participantPhoneNum.setText(phoneNum);
        }else{
            participantPhoneNum.setText(participantDetails.getMobile_number());
        }

        participantPanchayat.setText(participantDetails.getPanchayat());
        participantSHG.setText(participantDetails.getShg_associate());
        participantVillage.setText(participantDetails.getVillage_name());

        if(participantDetails!=null){
            if(participantDetails.getRegistration()!=null && !participantDetails.getRegistration().equalsIgnoreCase("null")){
                preScreeningIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
                registrationIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                preScreeningIV.setImageDrawable(getDrawable(R.drawable.error_icon));
                registrationIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
            if(participantDetails.getSocio_demography()!=null && !participantDetails.getSocio_demography().equalsIgnoreCase("null")){
                socioDemographyIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                socioDemographyIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
            if(participantDetails.getDisease_profile()!=null && !participantDetails.getDisease_profile().equalsIgnoreCase("null")){
                diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
        }else{
            preScreeningIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            registrationIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            socioDemographyIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.error_icon));
        }
    }

    private void onClickOfDashboard() {
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardParticipantDetailsScreen.this, DashboardScreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void moveToPHQScreeningPage(){
        Intent participantIntent = new Intent(DashboardParticipantDetailsScreen.this, PHQ9SHGListScreen.class);
        startActivity(participantIntent);
        finish();
    }

    private void onClickOfPHQScreening(){
        PHQLinearLayout.setOnClickListener(v -> moveToPHQScreeningPage());
    }

    private void setOnClickForParticipants(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent loginIntent = new Intent(DashboardParticipantDetailsScreen.this, CoordinatorSHGList.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void callGetParticipantTrackingDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking?fields=[\"name\",\"registration\",\"socio_demography\",\"disease_profile\"]&or_filters=[[\"user_pri_id\", \"=\", \"" + participantDetails.getUser_pri_id() + "\"]]";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getTrackingDetails(DashboardParticipantDetailsScreen.this, url);
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
        englishButtonDashboard.setOnClickListener(v -> {
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
            changeLocalLanguage("en");
        });

        kannadaButtonDashboard.setOnClickListener(v -> {
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
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
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
        }else{
            englishButtonDashboard.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
            kannadaButtonDashboard.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonDashboard.setTextColor(getResources().getColor(R.color.black, DashboardParticipantDetailsScreen.this.getTheme()));
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
        participantTVDashboard.setText(R.string.participants);
        dashboardTVDashboard.setText(R.string.dashboard);
        phqScreeningDashboardTV.setText(R.string.phq_screening);

        preScreeningTV.setText(R.string.pre_screening);
        registrationTV.setText(R.string.registration);
        socioDemographyTV.setText(R.string.socio_demography);
        diseaseProfileTV.setText(R.string.disease_profile);

        enrolmentStatusTV.setText(R.string.enrollment_status);
        surveyStatusTV.setText(R.string.survey_status);
        moduleStatusTV.setText(R.string.module_status);
        referralStatusTV.setText(R.string.referral_status);
        PHQ9TV.setText(R.string.phq9);
        badsTV.setText(R.string.bads);
        quidsTV.setText(R.string.quids_sr);
        whodasTV.setText(R.string.whodas);
        module0TV.setText(R.string.module_0);
        module1TV.setText(R.string.module_1);
        module2TV.setText(R.string.module_2);
        module3TV.setText(R.string.module_3);
        module4TV.setText(R.string.module_4);
        module5TV.setText(R.string.module_5);
        module6TV.setText(R.string.module_6);
        module7TV.setText(R.string.module_7);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DashboardParticipantDetailsScreen.this, DashboardScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
