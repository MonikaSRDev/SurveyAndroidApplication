package com.example.mithraapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mithraapplication.Fragments.ParticipantsAllListFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class ParticipantsScreen extends AppCompatActivity{

    private Button englishButtonParticipant, kannadaButtonParticipant;
    private TextView participantTitleTV, dashboardTVParticipant, participantTVParticipant, coordinatorNameTVParticipant;
    private LinearLayout dashboardLinearLayoutParticipant, participantLinearLayoutParticipant;
    private ImageView mithraLogoParticipant, coordinatorParticipant, notificationsIconParticipant, participantIcon;
    private TabLayout participantTabLayout;
    private FrameLayout participantFrameLayout;
    private FragmentManager fragmentManagerParticipant;
    private FragmentTransaction fragmentTransactionParticipant;
    private Fragment fragmentParticipant = null;
    private MithraUtility mithraUtility = new MithraUtility();
    private TabItem participantTabItem1, participantTabItem2, participantTabItem3, participantTabItem4, participantTabItem5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_screen);
        RegisterViews();
        onClickOfLanguageButton();
        getCurrentLocale();
        onClickOfDashboardButton();
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

        participantTitleTV = findViewById(R.id.participantsTitleTV);
        dashboardTVParticipant = findViewById(R.id.dashboardTVParticipant);
        participantTVParticipant = findViewById(R.id.participantsTVParticipant);
        participantTVParticipant.setTextColor(getResources().getColor(R.color.text_color));
        coordinatorNameTVParticipant = findViewById(R.id.coordinatorNameTVParticipant);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVParticipant.setText(coordinatorUserName);
        }

        mithraLogoParticipant = findViewById(R.id.appLogoParticipants);
        coordinatorParticipant = findViewById(R.id.coordinatorProfileParticipant);
        notificationsIconParticipant = findViewById(R.id.notificationsLogoParticipant);
        participantIcon = findViewById(R.id.participantsIconParticipant);
        participantIcon.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black));

        participantTabLayout = findViewById(R.id.tabLayoutParticipant);
        participantFrameLayout = findViewById(R.id.frameLayoutParticipant);
        setStartupTab();
        setTabSelectedListener();

        participantTabItem1 = findViewById(R.id.participantTabItem1);
        participantTabItem2 = findViewById(R.id.participantTabItem2);
        participantTabItem3 = findViewById(R.id.participantTabItem3);
        participantTabItem4 = findViewById(R.id.participantTabItem4);
        participantTabItem5 = findViewById(R.id.participantTabItem5);
    }

    private void setStartupTab(){
        fragmentParticipant = new ParticipantsAllListFragment();
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
                fragmentParticipant = new ParticipantsAllListFragment();
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

    private void onClickOfDashboardButton(){
        dashboardLinearLayoutParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantsScreen.this, DashboardScreen.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButtonParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButtonParticipant.setBackgroundResource(R.drawable.left_selected_toggle_button);
                englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
                kannadaButtonParticipant.setBackgroundResource(R.drawable.right_unselected_toggle_button);
                kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButtonParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButtonParticipant.setBackgroundResource(R.drawable.right_selected_toggle_button);
                kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
                englishButtonParticipant.setBackgroundResource(R.drawable.left_unselected_toggle_button);
                englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
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
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_selected_toggle_button);
            kannadaButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            englishButtonParticipant.setBackgroundResource(R.drawable.left_unselected_toggle_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButtonParticipant.setBackgroundResource(R.drawable.left_selected_toggle_button);
            englishButtonParticipant.setTextColor(getResources().getColor(R.color.black));
            kannadaButtonParticipant.setBackgroundResource(R.drawable.right_unselected_toggle_button);
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

        participantTitleTV.setText(R.string.participants);
        dashboardTVParticipant.setText(R.string.dashboard);
        participantTVParticipant.setText(R.string.participants);
        Objects.requireNonNull(participantTabLayout.getTabAt(0)).setText(R.string.all_list);
        Objects.requireNonNull(participantTabLayout.getTabAt(1)).setText(R.string.new_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(2)).setText(R.string.green_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(3)).setText(R.string.yellow_tab);
        Objects.requireNonNull(participantTabLayout.getTabAt(4)).setText(R.string.red_tab);

        super.onConfigurationChanged(newConfig);
    }
}
