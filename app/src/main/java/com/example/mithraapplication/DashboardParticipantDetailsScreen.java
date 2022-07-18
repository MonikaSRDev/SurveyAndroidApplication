package com.example.mithraapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class DashboardParticipantDetailsScreen extends AppCompatActivity {

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private TextView dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;
    private MithraUtility mithraUtility = new MithraUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_participant_details);
        RegisterViews();
    }

    private void RegisterViews(){
        englishButtonDashboard = findViewById(R.id.englishButtonDP);
        kannadaButtonDashboard = findViewById(R.id.kannadaButtonDP);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDP);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDP);

        dashboardTVDashboard = findViewById(R.id.dashboardTVDP);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color));
        participantTVDashboard = findViewById(R.id.participantsTVDP);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDP);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVDashboard.setText(coordinatorUserName);
        }

        mithraLogoDashboard = findViewById(R.id.appLogoDP);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDP);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDP);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDP);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black));

    }
}
