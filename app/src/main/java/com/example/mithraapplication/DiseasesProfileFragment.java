package com.example.mithraapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mithraapplication.ModelClasses.DiseasesProfile;

import java.util.ArrayList;

public class DiseasesProfileFragment extends Fragment {

    private ArrayList<DiseasesProfile> diseasesProfilesArray = new ArrayList<>();
    private RecyclerView recyclerViewLeft;
    private DiseasesProfileAdapter diseasesProfileAdapter;
    private Button nextDiseaseProfileButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disease_profile_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        RegisterViews(view);
        setRecyclerView();
        onClickOfNextButton();

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("DiseasesProfileData"));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = intent.getBundleExtra("ParticipantDiseaseData");
            ArrayList<DiseasesProfile> diseasesProfile = (ArrayList<DiseasesProfile>) args.getSerializable("ARRAYLIST");
        }
    };

    private void RegisterViews(View view){
        recyclerViewLeft = view.findViewById(R.id.diseasesRecyclerView);
        nextDiseaseProfileButton = view.findViewById(R.id.diseasesNextButton);
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
        diseasesProfileAdapter = new DiseasesProfileAdapter(getActivity(), diseasesProfilesArray);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerViewLeft.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewLeft.setAdapter(diseasesProfileAdapter);
    }

    private void onClickOfNextButton(){
        nextDiseaseProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diseasesProfileAdapter != null){
                    diseasesProfileAdapter.sendDataToActivity();
                }else{
                    Log.d("TESTING", "Adapter is Empty");
                }
                moveToParticipantsScreen();
            }
        });
    }

    private void moveToParticipantsScreen(){
        Intent intent = new Intent(getActivity(), ParticipantsScreen.class);
        startActivity(intent);
    }

}
