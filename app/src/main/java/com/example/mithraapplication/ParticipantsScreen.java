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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mithraapplication.Fragments.ParticipantsAllListFragment;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class ParticipantsScreen extends AppCompatActivity{

    private Button englishButtonParticipant, kannadaButtonParticipant;
    private TextView participantTitleTV, dashboardTVParticipant, participantTVParticipant, coordinatorNameTVParticipant, phqScreeningTV;
    private LinearLayout dashboardLinearLayoutParticipant, participantLinearLayoutParticipant, PHQLinearLayout;
    private ImageView mithraLogoParticipant, coordinatorParticipant, notificationsIconParticipant, participantIcon;
    private TabLayout participantTabLayout;
    private FrameLayout participantFrameLayout;
    private FragmentManager fragmentManagerParticipant;
    private FragmentTransaction fragmentTransactionParticipant;
    private Fragment fragmentParticipant = null;
    private MithraUtility mithraUtility = new MithraUtility();
    private TabItem participantTabItem1, participantTabItem2, participantTabItem3, participantTabItem4, participantTabItem5;
    private PHQLocations phqLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_screen);
        RegisterViews();
        onClickOfLanguageButton();
        getCurrentLocale();
        getIntentData();
        onClickOfDashboardButton();
        onClickOfPHQScreening();
        setStartupTab();
        setTabSelectedListener();
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews(){
        englishButtonParticipant = findViewById(R.id.englishButtonParticipant);
        kannadaButtonParticipant = findViewById(R.id.kannadaButtonParticipant);

        dashboardLinearLayoutParticipant = findViewById(R.id.dashboardLinearLayoutParticipant);
        participantLinearLayoutParticipant = findViewById(R.id.participantLinearLayoutParticipant);
        participantLinearLayoutParticipant.setBackgroundResource(R.drawable.selected_page);

        PHQLinearLayout = findViewById(R.id.PHQScreeningLinearLayoutParticipant);

        participantTitleTV = findViewById(R.id.participantsTitleTV);
        dashboardTVParticipant = findViewById(R.id.dashboardTVParticipant);
        participantTVParticipant = findViewById(R.id.participantsTVParticipant);
        participantTVParticipant.setTextColor(getResources().getColor(R.color.text_color));
        coordinatorNameTVParticipant = findViewById(R.id.coordinatorNameTVParticipant);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVParticipant.setText(coordinatorUserName);
        }
        phqScreeningTV = findViewById(R.id.PHQScreeningTVParticipant);

        mithraLogoParticipant = findViewById(R.id.appLogoParticipants);
        coordinatorParticipant = findViewById(R.id.coordinatorProfileParticipant);
        notificationsIconParticipant = findViewById(R.id.notificationsLogoParticipant);
        participantIcon = findViewById(R.id.participantsIconParticipant);
        participantIcon.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black));

        participantTabLayout = findViewById(R.id.tabLayoutParticipant);
        participantFrameLayout = findViewById(R.id.frameLayoutParticipant);

        participantTabItem1 = findViewById(R.id.participantTabItem1);
        participantTabItem2 = findViewById(R.id.participantTabItem2);
        participantTabItem3 = findViewById(R.id.participantTabItem3);
        participantTabItem4 = findViewById(R.id.participantTabItem4);
        participantTabItem5 = findViewById(R.id.participantTabItem5);
    }

    private void setStartupTab(){
        fragmentParticipant = new ParticipantsAllListFragment(ParticipantsScreen.this, phqLocations);
        fragmentManagerParticipant = getSupportFragmentManager();
        fragmentTransactionParticipant = fragmentManagerParticipant.beginTransaction();
        fragmentTransactionParticipant.replace(R.id.frameLayoutParticipant, fragmentParticipant);
        fragmentTransactionParticipant.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransactionParticipant.commit();
    }

    private void setTabSelectedListener(){
        participantTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            default:
                fragmentParticipant = new ParticipantsAllListFragment(ParticipantsScreen.this, phqLocations);
                TabLayout.Tab tabdata2 = participantTabLayout.getTabAt(position);
                assert tabdata2 != null;
                tabdata2.select();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayoutParticipant, fragmentParticipant);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
        }
    }

    private void onClickOfDashboardButton(){
        dashboardLinearLayoutParticipant.setOnClickListener(v -> {
            Intent intent = new Intent(ParticipantsScreen.this, DashboardScreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void moveToPHQScreeningPage(){
        Intent participantIntent = new Intent(ParticipantsScreen.this, PHQ9SHGListScreen.class);
        startActivity(participantIntent);
    }

    private void onClickOfPHQScreening(){
        PHQLinearLayout.setOnClickListener(v -> moveToPHQScreeningPage());
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonParticipant.setOnClickListener(v -> {
            englishButtonParticipant.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("en");
        });

        kannadaButtonParticipant.setOnClickListener(v -> {
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            englishButtonParticipant.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
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
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            englishButtonParticipant.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonParticipant.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
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

        participantTitleTV.setText(R.string.participants);
        dashboardTVParticipant.setText(R.string.dashboard);
        participantTVParticipant.setText(R.string.participants);
        phqScreeningTV.setText(R.string.phq_screening);
        Objects.requireNonNull(participantTabLayout.getTabAt(0)).setText(R.string.all_list);
        Objects.requireNonNull(participantTabLayout.getTabAt(1)).setText(R.string.new_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(2)).setText(R.string.green_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(3)).setText(R.string.yellow_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(4)).setText(R.string.red_tab);

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ParticipantsScreen.this, CoordinatorSHGList.class);
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
