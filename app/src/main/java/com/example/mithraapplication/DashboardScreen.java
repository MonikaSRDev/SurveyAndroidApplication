package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardScreen extends AppCompatActivity {

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private TextView dashboardTitleTV, dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);
        RegisterViews();
    }

    private void RegisterViews() {

        englishButtonDashboard = findViewById(R.id.englishButtonDashboard);
        kannadaButtonDashboard = findViewById(R.id.kannadaButtonDashboard);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDashboard);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDashboard);

        dashboardTitleTV = findViewById(R.id.dashboardTitleTV);
        dashboardTVDashboard = findViewById(R.id.dashboardTVDashboard);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color));
        participantTVDashboard = findViewById(R.id.participantsTVDashboard);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDashboard);

        mithraLogoDashboard = findViewById(R.id.appLogoDashboard);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDashboard);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDashboard);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDashboard);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black));

        participantLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(DashboardScreen.this, ParticipantsScreen.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}
