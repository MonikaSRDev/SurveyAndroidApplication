package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PHQScreeningScreen extends AppCompatActivity {

    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();
    private TabLayout phqScreeningTabLayout;
    private FrameLayout phqScreeningFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    private PHQLocations phqLocations;
    private PHQParticipantDetails phqParticipantDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_screening_screen);
        RegisterViews();
        getIntentData();
        moveToDashboardScreen();
        moveToParticipantsScreen();
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
}
