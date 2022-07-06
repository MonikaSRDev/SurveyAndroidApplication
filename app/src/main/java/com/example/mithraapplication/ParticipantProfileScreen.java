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
import com.example.mithraapplication.Fragments.RegistrationFragment;
import com.example.mithraapplication.Fragments.ScreeningFragment;
import com.example.mithraapplication.Fragments.SocioDemographyFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class ParticipantProfileScreen extends AppCompatActivity {

    private Button englishButtonProfile, kannadaButtonProfile;
    private TextView profileTitleTV, dashboardTVProfile, participantTVProfile, coordinatorNameTVProfile;
    private LinearLayout dashboardLinearLayoutProfile, participantLinearLayoutProfile;
    private ImageView mithraLogoProfile, coordinatorProfile, notificationsIconProfile, participantsIconParticipant;
    private TabLayout profileTabLayout;
    private FrameLayout profileFrameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    private MithraUtility mithraUtility = new MithraUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        RegisterViews();
        onClickOfLanguageButton();
        getCurrentLocale();
        onClickOfDashboardButton();
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
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_coordinator));
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
//        profileTabLayout.addTab(profileTabLayout.newTab().setText("Report"), 3);
//        profileTabLayout.getTabAt(1)
        disableTab(1);
        disableTab(2);
        disableTab(3);
        setStartupTab();
        setTabSelectedListener();
    }

    private void disableTab(int tabNumber)
    {
        ViewGroup vg = (ViewGroup) profileTabLayout.getChildAt(0);
        ViewGroup vgTab = (ViewGroup) vg.getChildAt(tabNumber);
        vgTab.setEnabled(false);
    }

    private void setStartupTab(){
        fragment = new ScreeningFragment();
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
                fragment = new ScreeningFragment();
                TabLayout.Tab tabData = profileTabLayout.getTabAt(position);
                assert tabData != null;
                tabData.select();
                break;
            case 1:
                fragment = new RegistrationFragment();
                TabLayout.Tab tabData1 = profileTabLayout.getTabAt(position);
                assert tabData1 != null;
                tabData1.select();
                disableTab(0);
                break;
            case 2:
                fragment = new SocioDemographyFragment();
                TabLayout.Tab tabData2 = profileTabLayout.getTabAt(position);
                assert tabData2 != null;
                tabData2.select();
                disableTab(0);
                disableTab(1);
                break;
            case 3:
                fragment = new DiseasesProfileFragment();
                TabLayout.Tab tabData3 = profileTabLayout.getTabAt(position);
                assert tabData3 != null;
                tabData3.select();
                disableTab(0);
                disableTab(1);
                disableTab(2);
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
                englishButtonProfile.setBackgroundResource(R.drawable.left_selected_toggle_button);
                englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
                kannadaButtonProfile.setBackgroundResource(R.drawable.right_unselected_toggle_button);
                kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButtonProfile.setBackgroundResource(R.drawable.right_selected_toggle_button);
                kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
                englishButtonProfile.setBackgroundResource(R.drawable.left_unselected_toggle_button);
                englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
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
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_selected_toggle_button);
            kannadaButtonProfile.setTextColor(getResources().getColor(R.color.black));
            englishButtonProfile.setBackgroundResource(R.drawable.left_unselected_toggle_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonProfile.setBackgroundResource(R.drawable.left_selected_toggle_button);
            englishButtonProfile.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonProfile.setBackgroundResource(R.drawable.right_unselected_toggle_button);
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
//        participantUserNameET.setHint(R.string.user_name);
//        participantPasswordET.setHint(R.string.password);
//        participantConfirmPasswordET.setHint(R.string.confirm_password);
//        participantNameET.setHint(R.string.participant_name);
//        participantAgeET.setHint(R.string.age);
//
//        dashboardTV.setText(R.string.dashboard);
//        participantsTV.setText(R.string.participants);
//        participateAgeTV.setText(R.string.age);
//        participatePhoneNumberTV.setText(R.string.phone_number);
//        participateUserNameTV.setText(R.string.user_name);
//        participatePasswordTV.setText(R.string.password);
//        participantConfirmPasswordTV.setText(R.string.confirm_password);
//        VillageNameTV.setText(R.string.village_name);
//        SHGAssociationTV.setText(R.string.shg_association);
//        participantNameTV.setText(R.string.name_small_case);
//        genderTV.setText(R.string.gender);
//        addParticipantTV.setText(R.string.add_new_participant);
//
//        maleButton.setText(R.string.male);
//        femaleButton.setText(R.string.female);
//        othersButton.setText(R.string.others);
//        registerButton.setText(R.string.register);

        profileTitleTV.setText(R.string.profile);
        dashboardTVProfile.setText(R.string.dashboard);
        participantTVProfile.setText(R.string.participants);
        if(profileTabLayout.getTabAt(0)!= null){
            Objects.requireNonNull(profileTabLayout.getTabAt(0)).setText(R.string.screening);
        }
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setText(R.string.registration);
        Objects.requireNonNull(profileTabLayout.getTabAt(2)).setText(R.string.socio_demography);
        Objects.requireNonNull(profileTabLayout.getTabAt(3)).setText(R.string.disease_profile);
//        Objects.requireNonNull(profileTabLayout.getTabAt(4)).setText(R.string.red_tab);

        super.onConfigurationChanged(newConfig);
    }
}
