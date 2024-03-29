package com.example.mithraapplication;

import static com.example.mithraapplication.Fragments.RegistrationFragment.trackingName;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mithraapplication.Fragments.DiseasesProfileFragment;
import com.example.mithraapplication.Fragments.ParticipantReportFragment;
import com.example.mithraapplication.Fragments.RegistrationFragment;
import com.example.mithraapplication.Fragments.SocioDemographyFragment;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantProfileServerEvents;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ParticipantProfileScreen extends AppCompatActivity implements HandleServerResponse, ParticipantProfileServerEvents {

    public Button englishButtonProfile, kannadaButtonProfile, profileEditButton;
    private TextView profileTitleTV, dashboardTVProfile, participantTVProfile, coordinatorNameTVProfile, profileParticipantName, phqTVProfile;
    private LinearLayout dashboardLinearLayoutProfile, participantLinearLayoutProfile, socioDemographyLinearLayout, PHQLinearLayout;
    private ImageView mithraLogoProfile, coordinatorProfile, notificationsIconProfile, participantsIconParticipant;
    private TabLayout profileTabLayout;
    private FrameLayout profileFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    private ConstraintLayout constraintlayout;
    private MithraUtility mithraUtility = new MithraUtility();
    public static String isLanguageSelected = "en";
    private RegisterParticipant registerParticipant = new RegisterParticipant();
    private String isEditable = "true";
    public static String participant_primary_ID = "NULL";
    private TrackingParticipantStatus trackingParticipantStatus = null;
    private int currentTab, previousTab;
    private PHQLocations phqLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        RegisterViews();
        getIntentData();
        onClickOfLanguageButton();
        getCurrentLocale();
        onClickOfDashboardButton();
        onClickOfPHQScreening();
        if(isEditable!=null && isEditable.equals("true")){
            disableTab(1);
            disableTab(2);
        }
        setTabSelectedListener();
    }

    private void RegisterViews() {
        englishButtonProfile = findViewById(R.id.englishButtonProfile);
        kannadaButtonProfile = findViewById(R.id.kannadaButtonProfile);

        dashboardLinearLayoutProfile = findViewById(R.id.dashboardLinearLayoutProfile);
        participantLinearLayoutProfile = findViewById(R.id.participantLinearLayoutProfile);
        participantLinearLayoutProfile.setBackgroundResource(R.drawable.selected_page);

        PHQLinearLayout = findViewById(R.id.PHQScreeningLinearLayoutProfile);

        profileTitleTV = findViewById(R.id.profileTitleTV);
        phqTVProfile = findViewById(R.id.PHQScreeningTVProfile);
        dashboardTVProfile = findViewById(R.id.dashboardTVProfile);
        participantTVProfile = findViewById(R.id.participantsTVProfile);
        participantTVProfile.setTextColor(getResources().getColor(R.color.text_color));
        coordinatorNameTVProfile = findViewById(R.id.coordinatorNameTVProfile);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVProfile.setText(coordinatorUserName);
        }

        mithraLogoProfile = findViewById(R.id.appLogoProfile);
        coordinatorProfile = findViewById(R.id.coordinatorProfile);
        notificationsIconProfile = findViewById(R.id.notificationsLogoProfile);
        participantsIconParticipant = findViewById(R.id.participantsIconProfile);
        participantsIconParticipant.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black));

        profileTabLayout = findViewById(R.id.profileTabLayout);
        profileFrameLayout = findViewById(R.id.profileFrameLayout);

        profileParticipantName = findViewById(R.id.profileParticipantName);
        profileEditButton = findViewById(R.id.profileEditButton);
        setVisibilityForEdit(false);

//        constraintlayout = findViewById(R.id.mainConstrainLayoutProfile);
//        constraintlayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(getCurrentFocus()!=null){
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
//                return false;
//            }
//        });
//
//        profileFrameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(getCurrentFocus()!=null){
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
//            }
//        });
    }

    private void disableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) profileTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(false);
    }

    private void enableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) profileTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(true);
    }

    private void setVisibilityForEdit(boolean isVisible){
        if(isVisible){
            profileParticipantName.setVisibility(View.VISIBLE);
            profileEditButton.setVisibility(View.VISIBLE);
        }else{
            profileParticipantName.setVisibility(View.GONE);
            profileEditButton.setVisibility(View.GONE);
        }
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            registerParticipant = (RegisterParticipant) intent.getSerializableExtra("RegisterParticipant Array");
            isEditable = intent.getStringExtra("isEditable");
            phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
        }
        if(registerParticipant!=null && registerParticipant.getUser_pri_id()!=null){
            mithraUtility.putSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.primaryID), getString(R.string.participantPrimaryID), registerParticipant.getUser_pri_id());
            participant_primary_ID = registerParticipant.getUser_pri_id();
        }
        if(isEditable!=null && !isEditable.equals("true")){
            callGetParticipantTrackingDetails();
//            ((LinearLayout) Objects.requireNonNull(profileTabLayout.getTabAt(0)).view).setVisibility(View.GONE);
        }else{
            trackingParticipantStatus = null;
            setupSelectedTabFragment(0);
        }
    }

    private void setTabSelectedListener(){
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                if((previousTab > currentTab) && trackingParticipantStatus == null){
                    isEditable = "false";
                    checkIfEditable(currentTab);
                    setupSelectedTabFragment(tab.getPosition());
//                    trackingParticipantStatus = null;
                }else if (previousTab > currentTab){
                    checkIfEditable(currentTab);
                    setupSelectedTabFragment(tab.getPosition());
                }else if ((currentTab > previousTab)){
                    checkIfEditable(currentTab);
                    setupSelectedTabFragment(tab.getPosition());
                }else{
                    isEditable = "true";
                    setupSelectedTabFragment(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                previousTab = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void checkIfEditable(int position){
        trackingParticipantStatus = new TrackingParticipantStatus();
        String tracking = mithraUtility.getSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.tracking), participant_primary_ID);
        String screeningName = mithraUtility.getSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.userScreeningName), getString(R.string.userScreeningID));
        String registrationName = mithraUtility.getSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.registration_sp), participant_primary_ID);
        String socioDemographyName = mithraUtility.getSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.socio_demography_sp), participant_primary_ID);
        String diseaseProfileName = mithraUtility.getSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.disease_profile_sp), participant_primary_ID);

        switch(position){
            case 0: if(registrationName!=null && !registrationName.equals("NULL")){
                        trackingParticipantStatus.setName(tracking);
                        trackingParticipantStatus.setRegistration(registrationName);
                        registerParticipant.setUser_pri_id(participant_primary_ID);
                        enableTab(0);
                        enableTab(1);
                        disableTab(2);
                        isEditable = "false";
                    }else{
                        trackingParticipantStatus = null;
                        isEditable = "true";
                        enableTab(0);
                        disableTab(1);
                        disableTab(2);
                    }

                if(socioDemographyName!=null && !socioDemographyName.equals("NULL")){
                    trackingParticipantStatus.setName(tracking);
                    trackingParticipantStatus.setRegistration(registrationName);
                    trackingParticipantStatus.setSocio_demography(socioDemographyName);
                    registerParticipant.setUser_pri_id(participant_primary_ID);
                    enableTab(0);
                    enableTab(1);
                    enableTab(2);
                    isEditable = "false";
                }

                    break;
            case 1: if(socioDemographyName!=null && !socioDemographyName.equals("NULL")){
                        trackingParticipantStatus.setName(tracking);
                        trackingParticipantStatus.setRegistration(registrationName);
                        trackingParticipantStatus.setSocio_demography(socioDemographyName);
                        isEditable = "false";
                        enableTab(0);
                        enableTab(1);
                        enableTab(2);
                    }else{
                        trackingParticipantStatus = null;
                        isEditable = "true";
                        enableTab(0);
                        enableTab(1);
                        disableTab(2);
                    }
                    break;
            case 2: if(diseaseProfileName!=null && !diseaseProfileName.equals("NULL")){
                        trackingParticipantStatus.setName(tracking);
                        trackingParticipantStatus.setRegistration(registrationName);
                        trackingParticipantStatus.setSocio_demography(socioDemographyName);
                        trackingParticipantStatus.setDisease_profile(diseaseProfileName);
                        isEditable = "false";
                    }else{
                        trackingParticipantStatus = null;
                        isEditable = "true";
                    }
                    enableTab(0);
                    enableTab(1);
                    enableTab(2);
//                    enableTab(3);
                    break;
        }
    }

    private void redirectParticipantToTab(){
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getName()!=null){
            if(trackingParticipantStatus.getRegistration()==null){
                setupSelectedTabFragment(0);
            }else if(trackingParticipantStatus.getSocio_demography()==null){
                setupSelectedTabFragment(1);
            }else if(trackingParticipantStatus.getDisease_profile()==null){
                setupSelectedTabFragment(2);
            }else{
                setupSelectedTabFragment(0);
            }
        }else{
            setupSelectedTabFragment(0);
        }
    }

    public void setupSelectedTabFragment(int position){
        switch (position) {
            case 0:
                fragment = new RegistrationFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable, registerParticipant, phqLocations);
                profileEditButton.setText(R.string.edit);
                profileEditButton.setBackgroundResource(R.drawable.edit_button_background);
                TabLayout.Tab tabData1 = profileTabLayout.getTabAt(position);
                assert tabData1 != null;
                tabData1.select();
                break;
            case 1:
                fragment = new SocioDemographyFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable, phqLocations);
                profileEditButton.setText(R.string.edit);
                profileEditButton.setBackgroundResource(R.drawable.edit_button_background);
                TabLayout.Tab tabData2 = profileTabLayout.getTabAt(position);
                assert tabData2 != null;
                tabData2.select();
                enableTab(0);
                break;
            case 2:
                fragment = new DiseasesProfileFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable, phqLocations);
                profileEditButton.setText(R.string.edit);
                profileEditButton.setBackgroundResource(R.drawable.edit_button_background);
                TabLayout.Tab tabData3 = profileTabLayout.getTabAt(position);
                assert tabData3 != null;
                tabData3.select();
                enableTab(0);
                enableTab(1);
                break;
            case 3:
                fragment = new ParticipantReportFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable, registerParticipant, phqLocations);
                profileEditButton.setText(R.string.status);
                profileEditButton.setBackgroundResource(R.drawable.edit_button_background);
                TabLayout.Tab tabData4 = profileTabLayout.getTabAt(position);
                assert tabData4 != null;
                tabData4.select();
                enableTab(0);
                enableTab(1);
                enableTab(2);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.profileFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_popup_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = false;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 0);
        popupWindow.setOutsideTouchable(true);

        TextView logoutTextView = popupView.findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(v -> Toast.makeText(view.getContext(), "Logout User", Toast.LENGTH_SHORT).show());
    }

    private void onClickOfDashboardButton(){
        dashboardLinearLayoutProfile.setOnClickListener(v -> {
            Intent loginIntent = new Intent(ParticipantProfileScreen.this, DashboardScreen.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void moveToPHQScreeningPage(){
        Intent participantIntent = new Intent(ParticipantProfileScreen.this, PHQ9SHGListScreen.class);
        startActivity(participantIntent);
    }

    private void onClickOfPHQScreening(){
        PHQLinearLayout.setOnClickListener(v -> moveToPHQScreeningPage());
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonProfile.setOnClickListener(v -> {
            isLanguageSelected = "en";
            englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("en");
        });

        kannadaButtonProfile.setOnClickListener(v -> {
            isLanguageSelected = "kn";
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
            englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("kn");
        });
    }

    private void callGetParticipantTrackingDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking?fields=[\"name\",\"registration\",\"socio_demography\",\"disease_profile\",\"enroll\"]&or_filters=[[\"user_pri_id\", \"=\", \"" + registerParticipant.getUser_pri_id() + "\"]]";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setParticipantProfileServerEvents(this);
        requestObject.getTrackingDetails(ParticipantProfileScreen.this, url);
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
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
            englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
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

        profileTitleTV.setText(R.string.profile);
        dashboardTVProfile.setText(R.string.dashboard);
        participantTVProfile.setText(R.string.participants);
        phqTVProfile.setText(R.string.phq_screening);
        profileEditButton.setText(R.string.edit);
        if(isEditable!=null && !isEditable.equals("true")){
            Objects.requireNonNull(profileTabLayout.getTabAt(0)).setText(R.string.profile);
        }else{
            Objects.requireNonNull(profileTabLayout.getTabAt(0)).setText(R.string.registration);
        }
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setText(R.string.socio_demography);
        Objects.requireNonNull(profileTabLayout.getTabAt(2)).setText(R.string.disease_profile);
        if(profileTabLayout.getTabAt(3)!=null){
            Objects.requireNonNull(profileTabLayout.getTabAt(3)).setText(R.string.report);
        }
        super.onConfigurationChanged(newConfig);
    }

    public String getIsLanguageSelected(){
        return isLanguageSelected;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(ParticipantProfileScreen.this, ParticipantsScreen.class);
        loginIntent.putExtra("PHQLocations", phqLocations);
        startActivity(loginIntent);
        finish();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int[] source_coordinates = new int[2];
            w.getLocationOnScreen(source_coordinates);
            float x = event.getRawX() + w.getLeft() - source_coordinates[0];
            float y = event.getRawY() + w.getTop() - source_coordinates[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    public void getParticipantTrackingDetails(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<TrackingParticipantStatus>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        try{
            ArrayList<TrackingParticipantStatus> trackingParticipantStatusArrayList = gson.fromJson(jsonObject.get("data"), type);
            if(trackingParticipantStatusArrayList!=null && trackingParticipantStatusArrayList.size()!=0){
                trackingParticipantStatus = trackingParticipantStatusArrayList.get(0);
                trackingName = trackingParticipantStatus.getName();
                participant_primary_ID = registerParticipant.getUser_pri_id();
                mithraUtility.putSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.tracking), participant_primary_ID, trackingParticipantStatus.getName());
                mithraUtility.putSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.registration_sp), participant_primary_ID, trackingParticipantStatus.getRegistration());
                mithraUtility.putSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.socio_demography_sp), participant_primary_ID, trackingParticipantStatus.getSocio_demography());
                mithraUtility.putSharedPreferencesData(ParticipantProfileScreen.this, getString(R.string.disease_profile_sp), participant_primary_ID, trackingParticipantStatus.getDisease_profile());

                TabLayout.Tab tab = profileTabLayout.getTabAt(0);
                assert tab != null;
                tab.setText(R.string.profile);
                enableTab(1);
                enableTab(2);
                if(registerParticipant!=null){
                    profileParticipantName.setText(registerParticipant.getParticipantName());
                }
                if(trackingParticipantStatus.getEnroll().equalsIgnoreCase("yes")){
                    profileTabLayout.addTab(profileTabLayout.newTab().setText(getString(R.string.report)), 3);
                    enableTab(3);
                }
                setVisibilityForEdit(true);
            }else{
                trackingParticipantStatus = null;
            }
            redirectParticipantToTab();
        }catch(Exception e){
            Toast.makeText(ParticipantProfileScreen.this, jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
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
