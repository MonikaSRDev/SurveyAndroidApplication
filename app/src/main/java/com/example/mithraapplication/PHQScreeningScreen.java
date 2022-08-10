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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mithraapplication.Fragments.PHQQuestionnaireFragment;
import com.example.mithraapplication.Fragments.PHQScreeningFragment;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PHQScreeningScreen extends AppCompatActivity {

    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV, phqScreenTitle;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();
    private TabLayout phqScreeningTabLayout;
    private FrameLayout phqScreeningFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    private PHQLocations phqLocations;
    private PHQParticipantDetails phqParticipantDetails;
    private Button englishButton, kannadaButton;
    public static String isLanguageSelected = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_screening_screen);
        RegisterViews();
        getIntentData();
        moveToDashboardScreen();
        moveToParticipantsScreen();
        onClickOfLanguageButton();
        getCurrentLocale();
    }

    private void RegisterViews() {
        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLLScreening);
        phqScreeningTV = findViewById(R.id.phqScreeningTVScreening);
        phqScreeningIcon = findViewById(R.id.phqScreeningIconScreening);

        phqScreeningTV.setTextColor(getResources().getColor(R.color.text_color));
        phqScreeningLinearlayout.setBackgroundResource(R.drawable.selected_page);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQScreening);
        dashboardTV = findViewById(R.id.dashboardTVPHQScreening);
        dashboardIcon = findViewById(R.id.dashboardIconPHQScreening);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQScreening);
        participantTV = findViewById(R.id.participantsTVPHQScreening);
        participantIcon = findViewById(R.id.participantsIconPHQScreening);

        phqScreeningTabLayout = findViewById(R.id.tabLayoutPHQScreening);
        phqScreeningFrameLayout = findViewById(R.id.phqScreeningFrameLayout);
        setTabSelectedListener();

        englishButton = findViewById(R.id.englishButtonPHQScreening);
        kannadaButton = findViewById(R.id.kannadaButtonPHQScreening);

        phqScreenTitle = findViewById(R.id.dashboardTitleTVPHQScreening);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            String fromActivity = intent.getStringExtra("FromActivity");
            if(fromActivity.equals("PHQParticipantScreen")){
                phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
                phqParticipantDetails = (PHQParticipantDetails) intent.getSerializableExtra("PHQParticipantDetails");
                setupSelectedTabFragment(1);
            }else{
                phqLocations = (PHQLocations) intent.getSerializableExtra("PHQLocations");
                setupSelectedTabFragment(0);
            }

        }
    }

    private void setTabSelectedListener(){
        phqScreeningTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                fragment = new PHQQuestionnaireFragment(PHQScreeningScreen.this, phqLocations);
                TabLayout.Tab tabData = phqScreeningTabLayout.getTabAt(position);
                assert tabData != null;
                tabData.select();
                disableTab(1);
                break;
            case 1:
                fragment = new PHQScreeningFragment(PHQScreeningScreen.this, phqParticipantDetails, phqLocations);
                TabLayout.Tab tabData1 = phqScreeningTabLayout.getTabAt(position);
                assert tabData1 != null;
                tabData1.select();
                disableTab(0);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.phqScreeningFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void disableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) phqScreeningTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(false);
    }

    private void enableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) phqScreeningTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(true);
    }

    private void moveToParticipantsScreen(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQScreeningScreen.this, CoordinatorSHGList.class);
            startActivity(participantIntent);
        });
    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQScreeningScreen.this, DashboardScreen.class);
            startActivity(participantIntent);
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(v -> {
            isLanguageSelected = "en";
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("en");
        });

        kannadaButton.setOnClickListener(v -> {
            isLanguageSelected = "kn";
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
            isLanguageSelected = "kn";
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            isLanguageSelected = "en";
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
        Objects.requireNonNull(phqScreeningTabLayout.getTabAt(0)).setText(R.string.phq_screen);
        Objects.requireNonNull(phqScreeningTabLayout.getTabAt(1)).setText(R.string.screening);

        super.onConfigurationChanged(newConfig);
    }

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
}
