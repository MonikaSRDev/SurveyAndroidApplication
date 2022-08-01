package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.Adapters.PHQScreeningAdapter;
import com.example.mithraapplication.ModelClasses.PHQLocations;

import java.util.ArrayList;

public class PHQ9ScreeningScreen extends AppCompatActivity {

    private GridView phqGridView;
    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_screening);
        RegisterViews();
        moveToParticipantsScreen();
        moveToDashboardScreen();

        ArrayList<PHQLocations> phqLocations = new ArrayList<>();
        phqLocations.add(new PHQLocations("SHG1", "Village1", "Panchayat1"));
        phqLocations.add(new PHQLocations("SHG2", "Village2", "Panchayat2"));
        phqLocations.add(new PHQLocations("SHG3", "Village3", "Panchayat3"));
        phqLocations.add(new PHQLocations("SHG4", "Village4", "Panchayat4"));
        phqLocations.add(new PHQLocations("SHG5", "Village5", "Panchayat5"));
        phqLocations.add(new PHQLocations("SHG6", "Village6", "Panchayat6"));
//        phqLocations.add(new PHQLocations("SHG7", "Village7", "Panchayat7"));
//        phqLocations.add(new PHQLocations("SHG8", "Village8", "Panchayat8"));
//        phqLocations.add(new PHQLocations("SHG9", "Village9", "Panchayat9"));

        PHQScreeningAdapter adapter = new PHQScreeningAdapter(this, 0, phqLocations);
        phqGridView.setAdapter(adapter);

    }

    private void RegisterViews() {
        phqGridView = findViewById(R.id.SHGListPHQScreening);

        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLL);
        phqScreeningTV = findViewById(R.id.phqScreeningTV);
        phqScreeningIcon = findViewById(R.id.phqScreeningIcon);

        phqScreeningTV.setTextColor(getResources().getColor(R.color.text_color));
        phqScreeningLinearlayout.setBackgroundResource(R.drawable.selected_page);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQ);
        dashboardTV = findViewById(R.id.dashboardTVPHQ);
        dashboardIcon = findViewById(R.id.dashboardIconPHQ);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQ);
        participantTV = findViewById(R.id.participantsTVPHQ);
        participantIcon = findViewById(R.id.participantsIconPHQ);

    }

    private void moveToParticipantsScreen(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9ScreeningScreen.this, ParticipantsScreen.class);
            startActivity(participantIntent);
        });
    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9ScreeningScreen.this, DashboardScreen.class);
            startActivity(participantIntent);
        });
    }
}
