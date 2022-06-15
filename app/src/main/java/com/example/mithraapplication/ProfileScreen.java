package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ProfileScreen extends AppCompatActivity {

    private Button englishButtonProfile, kannadaButtonProfile;
    private TextView profileTitleTV, dashboardTVProfile, profileTVProfile, coordinatorNameTVProfile;
    private LinearLayout dashboardLinearLayoutProfile, participantLinearLayoutProfile;
    private ImageView mithraLogoProfile, coordinatorProfile, notificationsIconProfile, participantsIconParticipant;
    private TabLayout profileTabLayout;
    private FrameLayout profileFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        RegisterViews();
    }

    private void RegisterViews() {
        englishButtonProfile = findViewById(R.id.englishButtonProfile);
        kannadaButtonProfile = findViewById(R.id.kannadaButtonProfile);

        dashboardLinearLayoutProfile = findViewById(R.id.dashboardLinearLayoutProfile);
        participantLinearLayoutProfile = findViewById(R.id.participantLinearLayoutProfile);
        participantLinearLayoutProfile.setBackgroundResource(R.drawable.selected_page);

        profileTitleTV = findViewById(R.id.profileTitleTV);
        dashboardTVProfile = findViewById(R.id.dashboardTVProfile);
        profileTVProfile = findViewById(R.id.participantsTVProfile);
        profileTVProfile.setTextColor(getResources().getColor(R.color.text_color));
        coordinatorNameTVProfile = findViewById(R.id.coordinatorNameTVProfile);

        mithraLogoProfile = findViewById(R.id.appLogoProfile);
        coordinatorProfile = findViewById(R.id.coordinatorProfile);
        notificationsIconProfile = findViewById(R.id.notificationsLogoProfile);
        participantsIconParticipant = findViewById(R.id.participantsIconProfile);
        participantsIconParticipant.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black));


        profileTabLayout = findViewById(R.id.profileTabLayout);
        profileFrameLayout = findViewById(R.id.profileFrameLayout);
//        profileTabLayout.addTab(profileTabLayout.newTab().setText("Report"), 3);
//        profileTabLayout.getTabAt(1)
        disableTab(1);
        disableTab(2);
        setStartupTab();
        setTabSelectedListener();

        dashboardLinearLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfileScreen.this, DashboardScreen.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void disableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) profileTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(false);
    }

    private void setStartupTab(){
        fragment = new RegistrationFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profileFrameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
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
                fragment = new RegistrationFragment();
                TabLayout.Tab tabdata = profileTabLayout.getTabAt(position);
                assert tabdata != null;
                tabdata.select();
                break;
            case 1:
                fragment = new SocioDemographyFragment();
                TabLayout.Tab tabdata1 = profileTabLayout.getTabAt(position);
                assert tabdata1 != null;
                tabdata1.select();
                disableTab(0);
                break;
            case 2:
                fragment = new DiseasesProfileFragment();
                TabLayout.Tab tabdata2 = profileTabLayout.getTabAt(position);
                assert tabdata2 != null;
                tabdata2.select();
                disableTab(0);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.profileFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
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
}
