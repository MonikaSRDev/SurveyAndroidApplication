package com.example.mithraapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mithraapplication.Fragments.DiseasesProfileFragment;
import com.example.mithraapplication.Fragments.ParticipantReportFragment;
import com.example.mithraapplication.Fragments.RegistrationFragment;
import com.example.mithraapplication.Fragments.ScreeningFragment;
import com.example.mithraapplication.Fragments.SocioDemographyFragment;
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

public class ParticipantProfileScreen extends AppCompatActivity implements HandleServerResponse{

    public Button englishButtonProfile, kannadaButtonProfile, profileEditButton;
    private TextView profileTitleTV, dashboardTVProfile, participantTVProfile, coordinatorNameTVProfile, profileParticipantName;
    private LinearLayout dashboardLinearLayoutProfile, participantLinearLayoutProfile;
    private ImageView mithraLogoProfile, coordinatorProfile, notificationsIconProfile, participantsIconParticipant;
    private TabLayout profileTabLayout;
    private FrameLayout profileFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    private MithraUtility mithraUtility = new MithraUtility();
    public static String isLanguageSelected = "en";
    private RegisterParticipant registerParticipant = new RegisterParticipant();
    private String isEditable = "true";
    private ArrayList<TrackingParticipantStatus> trackingParticipantStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        RegisterViews();
        onClickOfLanguageButton();
        getCurrentLocale();
        onClickOfDashboardButton();
        onClickOfEditButton();
        getIntentData();
    }

    private void RegisterViews() {
        englishButtonProfile = findViewById(R.id.englishButtonProfile);
        kannadaButtonProfile = findViewById(R.id.kannadaButtonProfile);

        dashboardLinearLayoutProfile = findViewById(R.id.dashboardLinearLayoutProfile);
        participantLinearLayoutProfile = findViewById(R.id.participantLinearLayoutProfile);
        participantLinearLayoutProfile.setBackgroundResource(R.drawable.selected_page);

        profileTitleTV = findViewById(R.id.profileTitleTV);
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
        disableTab(1);
        disableTab(2);
        disableTab(3);
        setTabSelectedListener();

        profileParticipantName = findViewById(R.id.profileParticipantName);
        profileEditButton = findViewById(R.id.profileEditButton);
        setVisibilityForEdit(false);
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

    private void setStartupTab(){
        fragment = new ScreeningFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profileFrameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            registerParticipant = (RegisterParticipant) intent.getSerializableExtra("RegisterParticipant Array");
            isEditable = intent.getStringExtra("isEditable");
        }

        if(isEditable!=null && !isEditable.equals("true")){
            ((LinearLayout)profileTabLayout.getTabAt(0).view).setVisibility(View.GONE);
            profileTabLayout.getTabAt(1).setText("Profile");
            enableTab(2);
            enableTab(3);
            if(registerParticipant!=null){
                profileParticipantName.setText(registerParticipant.getParticipantName());
            }
            profileTabLayout.addTab(profileTabLayout.newTab().setText("Report"), 4);
            setVisibilityForEdit(true);
            callGetParticipantTrackingDetails();
        }else{
            setStartupTab();
        }
    }

    private void setTabSelectedListener(){
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setupSelectedTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setupSelectedTabFragment(int position){
        switch (position) {
            case 0:
                fragment = new ScreeningFragment();
                TabLayout.Tab tabData = profileTabLayout.getTabAt(position);
                assert tabData != null;
                tabData.select();
                break;
            case 1:
                fragment = new RegistrationFragment(trackingParticipantStatus, isEditable, registerParticipant);
                TabLayout.Tab tabData1 = profileTabLayout.getTabAt(position);
                assert tabData1 != null;
                tabData1.select();
                ((LinearLayout)profileTabLayout.getTabAt(0).view).setVisibility(View.GONE);
                break;
            case 2:
                fragment = new SocioDemographyFragment(trackingParticipantStatus, isEditable);
                TabLayout.Tab tabData2 = profileTabLayout.getTabAt(position);
                assert tabData2 != null;
                tabData2.select();
                enableTab(1);
                break;
            case 3:
                fragment = new DiseasesProfileFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable);
                TabLayout.Tab tabData3 = profileTabLayout.getTabAt(position);
                assert tabData3 != null;
                tabData3.select();
                enableTab(1);
                enableTab(2);
                break;
            case 4:
                fragment = new ParticipantReportFragment(ParticipantProfileScreen.this, trackingParticipantStatus, isEditable);
                profileEditButton.setText(R.string.status);
                TabLayout.Tab tabData4 = profileTabLayout.getTabAt(position);
                assert tabData4 != null;
                tabData4.select();
                enableTab(1);
                enableTab(2);
                enableTab(3);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.profileFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void onClickOfEditButton(){
        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable = "reEdit";
                setupSelectedTabFragment(profileTabLayout.getSelectedTabPosition());
            }
        });
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_popup_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 0);
        popupWindow.setOutsideTouchable(true);

        //Initialize the elements of our window, install the handler

        TextView logoutTextView = popupView.findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                Toast.makeText(view.getContext(), "Logout User", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void onClickOfDashboardButton(){
        dashboardLinearLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ParticipantProfileScreen.this, DashboardScreen.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLanguageSelected = "en";
                englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
                englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
                kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_button);
                kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLanguageSelected = "kn";
                kannadaButtonProfile.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
                kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
                englishButtonProfile.setBackgroundResource(R.drawable.left_english_toggle_button);
                englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("kn");
            }
        });
    }

    private void callGetParticipantTrackingDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking?fields=[\"name\",\"registration\",\"socio_demography\",\"disease_profile\"]&or_filters=[[\"user_pri_id\", \"=\", \"" + registerParticipant.getUser_pri_id() + "\"]]";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
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
    public void onConfigurationChanged(Configuration newConfig) {

        profileTitleTV.setText(R.string.profile);
        dashboardTVProfile.setText(R.string.dashboard);
        participantTVProfile.setText(R.string.participants);
        if(profileTabLayout.getTabAt(0)!= null && ((LinearLayout)profileTabLayout.getTabAt(0).view).getVisibility()!=View.GONE){
            Objects.requireNonNull(profileTabLayout.getTabAt(0)).setText(R.string.screening);
        }
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setText(R.string.registration);
        Objects.requireNonNull(profileTabLayout.getTabAt(2)).setText(R.string.socio_demography);
        Objects.requireNonNull(profileTabLayout.getTabAt(3)).setText(R.string.disease_profile);
//        Objects.requireNonNull(profileTabLayout.getTabAt(4)).setText(R.string.red_tab);

        super.onConfigurationChanged(newConfig);
    }

    public String getIsLanguageSelected(){
        return isLanguageSelected;
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<TrackingParticipantStatus>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        try{
            trackingParticipantStatus = gson.fromJson(jsonObject.get("data"), type);
            setupSelectedTabFragment(1);
        }catch(Exception e){
            Toast.makeText(ParticipantProfileScreen.this, jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
