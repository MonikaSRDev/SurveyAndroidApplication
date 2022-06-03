package com.example.mithraapplication;

import android.content.Intent;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.DiseasesProfile;

import java.util.ArrayList;

public class DiseasesProfileActivity extends AppCompatActivity {

    private ArrayList<DiseasesProfile> diseasesProfilesArray = new ArrayList<>();
    private RecyclerView recyclerViewLeft, recyclerViewRight;
    private DiseasesProfileAdapter diseasesProfileAdapter;
    private Button nextDiseaseProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_profile_screen);
        initializeData();
        RegisterViews();
        setRecyclerView();
        onClickOfNextButton();
    }

    private void RegisterViews(){
        recyclerViewLeft = findViewById(R.id.leftRecyclerView);
        recyclerViewRight = findViewById(R.id.rightRecyclerView);
        nextDiseaseProfileButton = findViewById(R.id.diseasesNextButton);
    }

    private void initializeData(){
        DiseasesProfile profile1 = new DiseasesProfile();
        profile1.setDiseaseName("DIABETES MELLITUS");
        profile1.setDiagnosed("Have you been diagnosed with DIABETES MELLITUS");
        profile1.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile1.setReceivedTreatment("Do you receive treatment for this condition?");
        profile1.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile1);

        DiseasesProfile profile2 = new DiseasesProfile();
        profile2.setDiseaseName("Thyroid");
        profile2.setDiagnosed("Have you been diagnosed with Thyroid");
        profile2.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile2.setReceivedTreatment("Do you receive treatment for this condition?");
        profile2.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile2);

        DiseasesProfile profile3 = new DiseasesProfile();
        profile3.setDiseaseName("MALIGNANCY");
        profile3.setDiagnosed("Have you been diagnosed with MALIGNANCY");
        profile3.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile3.setReceivedTreatment("Do you receive treatment for this condition?");
        profile3.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile3);

        DiseasesProfile profile4 = new DiseasesProfile();
        profile4.setDiseaseName("HYPERTENSION");
        profile4.setDiagnosed("Have you been diagnosed with HYPERTENSION");
        profile4.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile4.setReceivedTreatment("Do you receive treatment for this condition?");
        profile4.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile4);

        DiseasesProfile profile5 = new DiseasesProfile();
        profile5.setDiseaseName("GASTRIC PROBLEM");
        profile5.setDiagnosed("Have you been diagnosed with GASTRIC PROBLEM");
        profile5.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile5.setReceivedTreatment("Do you receive treatment for this condition?");
        profile5.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile5);

        DiseasesProfile profile6 = new DiseasesProfile();
        profile6.setDiseaseName("CHRONIC RENAL DISEASE");
        profile6.setDiagnosed("Have you been diagnosed with CHRONIC RENAL DISEASE");
        profile6.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile6.setReceivedTreatment("Do you receive treatment for this condition?");
        profile6.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile6);

        DiseasesProfile profile7 = new DiseasesProfile();
        profile7.setDiseaseName("ASTHMA");
        profile7.setDiagnosed("Have you been diagnosed with ASTHMA");
        profile7.setDiagnosedAge("If yes, how old were you when this was diagnosed?");
        profile7.setReceivedTreatment("Do you receive treatment for this condition?");
        profile7.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile7);
    }

    private void setRecyclerView(){
        int size = diseasesProfilesArray.size();

        ArrayList<DiseasesProfile> firstList = new ArrayList<>(diseasesProfilesArray.subList(0, (size/2) ));
        ArrayList<DiseasesProfile> secondList = new ArrayList<>(diseasesProfilesArray.subList((size/2), size));

        diseasesProfileAdapter = new DiseasesProfileAdapter(DiseasesProfileActivity.this, firstList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewLeft.setLayoutManager(linearLayoutManager);
        recyclerViewLeft.setAdapter(diseasesProfileAdapter);

        diseasesProfileAdapter = new DiseasesProfileAdapter(DiseasesProfileActivity.this, secondList);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerViewRight.setLayoutManager(linearLayoutManager1);
        recyclerViewRight.setAdapter(diseasesProfileAdapter);
    }

    private void onClickOfNextButton(){
        nextDiseaseProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiseasesProfileActivity.this, QuestionActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }

}
