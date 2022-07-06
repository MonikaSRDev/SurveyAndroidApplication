package com.example.mithraapplication.Fragments;

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

import com.example.mithraapplication.Adapters.DiseasesProfileAdapter;
import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.DiseasesProfilePostRequest;
import com.example.mithraapplication.ParticipantsScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;

import java.util.ArrayList;
import java.util.List;

public class DiseasesProfileFragment extends Fragment implements HandleServerResponse {

    private ArrayList<DiseasesProfile> diseasesProfilesArray = new ArrayList<>();
    private RecyclerView recyclerViewLeft;
    private DiseasesProfileAdapter diseasesProfileAdapter;
    private Button nextDiseaseProfileButton;
    private ArrayList<DiseasesProfile> diseasesProfile = new ArrayList<>();
    private MithraUtility mithraUtility = new MithraUtility();

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
            diseasesProfile = (ArrayList<DiseasesProfile>) args.getSerializable("ARRAYLIST");
//            callServerPostDiseasesProfile();
            moveToParticipantsScreen();
        }
    };

    @Override
    public void onResume() {
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter("ParticipantDiseaseData"));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void RegisterViews(View view){
        recyclerViewLeft = view.findViewById(R.id.diseasesRecyclerView);
        nextDiseaseProfileButton = view.findViewById(R.id.diseasesNextButton);
    }

    private void initializeData(){
        DiseasesProfile profile1 = new DiseasesProfile();
        profile1.setDiseaseName("DIABETES MELLITUS");
        profile1.setDiagnosedAge("How old were you when this was diagnosed?");
        profile1.setReceivedTreatment("Do you receive treatment for this condition?");
        profile1.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile1);

        DiseasesProfile profile2 = new DiseasesProfile();
        profile2.setDiseaseName("HYPERTENSION");
        profile2.setDiagnosedAge("How old were you when this was diagnosed?");
        profile2.setReceivedTreatment("Do you receive treatment for this condition?");
        profile2.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile2);

        DiseasesProfile profile3 = new DiseasesProfile();
        profile3.setDiseaseName("HEART DISEASE");
        profile3.setDiagnosedAge("How old were you when this was diagnosed?");
        profile3.setReceivedTreatment("Do you receive treatment for this condition?");
        profile3.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile3);

        DiseasesProfile profile4 = new DiseasesProfile();
        profile4.setDiseaseName("Thyroid");
        profile4.setDiagnosedAge("How old were you when this was diagnosed?");
        profile4.setReceivedTreatment("Do you receive treatment for this condition?");
        profile4.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile4);

        DiseasesProfile profile5 = new DiseasesProfile();
        profile5.setDiseaseName("CHRONIC LIVER DISEASE");
        profile5.setDiagnosedAge("How old were you when this was diagnosed?");
        profile5.setReceivedTreatment("Do you receive treatment for this condition?");
        profile5.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile5);

        DiseasesProfile profile6 = new DiseasesProfile();
        profile6.setDiseaseName("CHRONIC RENAL DISEASE");
        profile6.setDiagnosedAge("How old were you when this was diagnosed?");
        profile6.setReceivedTreatment("Do you receive treatment for this condition?");
        profile6.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile6);

        DiseasesProfile profile7 = new DiseasesProfile();
        profile7.setDiseaseName("MALIGNANCY");
        profile7.setDiagnosedAge("How old were you when this was diagnosed?");
        profile7.setReceivedTreatment("Do you receive treatment for this condition?");
        profile7.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile7);

        DiseasesProfile profile8 = new DiseasesProfile();
        profile8.setDiseaseName("DISABILITIES");
        profile8.setDiagnosedAge("How old were you when this was diagnosed?");
        profile8.setReceivedTreatment("Do you receive treatment for this condition?");
        profile8.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile8);

        DiseasesProfile profile9 = new DiseasesProfile();
        profile9.setDiseaseName("GASTRIC PROBLEM");
        profile9.setDiagnosedAge("How old were you when this was diagnosed?");
        profile9.setReceivedTreatment("Do you receive treatment for this condition?");
        profile9.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile9);

        DiseasesProfile profile10 = new DiseasesProfile();
        profile10.setDiseaseName("MENTAL ILLNESS");
        profile10.setDiagnosedAge("How old were you when this was diagnosed?");
        profile10.setReceivedTreatment("Do you receive treatment for this condition?");
        profile10.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile10);

        DiseasesProfile profile11 = new DiseasesProfile();
        profile11.setDiseaseName("EPILEPSY");
        profile11.setDiagnosedAge("How old were you when this was diagnosed?");
        profile11.setReceivedTreatment("Do you receive treatment for this condition?");
        profile11.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile11);

        DiseasesProfile profile12 = new DiseasesProfile();
        profile12.setDiseaseName("ASTHMA");
        profile12.setDiagnosedAge("How old were you when this was diagnosed?");
        profile12.setReceivedTreatment("Do you receive treatment for this condition?");
        profile12.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile12);

        DiseasesProfile profile13 = new DiseasesProfile();
        profile13.setDiseaseName("SKIN DISEASE");
        profile13.setDiagnosedAge("How old were you when this was diagnosed?");
        profile13.setReceivedTreatment("Do you receive treatment for this condition?");
        profile13.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile13);

        DiseasesProfile profile14 = new DiseasesProfile();
        profile14.setDiseaseName("ANY OTHER DISEASES");
        profile14.setSpecifyDisease("Specify other disease");
        profile14.setDiagnosedAge("How old were you when this was diagnosed?");
        profile14.setReceivedTreatment("Do you receive treatment for this condition?");
        profile14.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile14);
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
            }
        });
    }

    private void moveToParticipantsScreen(){
        Intent intent = new Intent(getActivity(), ParticipantsScreen.class);
        startActivity(intent);
    }

    private String getDiseaseList(int position){
        List<String> disease = new ArrayList<>();
        disease.add(diseasesProfile.get(position).getDiagnosed());
        disease.add(diseasesProfile.get(position).getDiagnosedAge());
        disease.add(diseasesProfile.get(position).getReceivedTreatment());
        disease.add(diseasesProfile.get(position).getLimitActivities());
        String diseaseStr = String.join(",", disease );
        diseaseStr = "[" + diseaseStr + "]";

        Log.i("ARRAY LIST", "Diseases Data - list" + disease);
        Log.i("ARRAY LIST", "Diseases Data - String" + diseaseStr);

        return diseaseStr;
    }

    private void callServerPostDiseasesProfile(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/disease_profile";
        DiseasesProfilePostRequest diseasesProfilePostRequest = new DiseasesProfilePostRequest();
        diseasesProfilePostRequest.setUser_name(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.user_name), getString(R.string.user_name_participant)));
        diseasesProfilePostRequest.setDiabetes_mellitus(getDiseaseList(0));
        diseasesProfilePostRequest.setHypertension(getDiseaseList(1));
        diseasesProfilePostRequest.setHeart_disease(getDiseaseList(2));
        diseasesProfilePostRequest.setThyroid(getDiseaseList(3));
        diseasesProfilePostRequest.setChronic_liver_disease(getDiseaseList(4));
        diseasesProfilePostRequest.setChronic_renal_disease(getDiseaseList(5));
        diseasesProfilePostRequest.setMalignancy(getDiseaseList(6));
        diseasesProfilePostRequest.setDisabilities(getDiseaseList(7));
        diseasesProfilePostRequest.setGastric_problem(getDiseaseList(8));
        diseasesProfilePostRequest.setMental_illness(getDiseaseList(9));
        diseasesProfilePostRequest.setEpilepsy(getDiseaseList(10));
        diseasesProfilePostRequest.setAsthma(getDiseaseList(11));
        diseasesProfilePostRequest.setSkin_disease(getDiseaseList(12));
        diseasesProfilePostRequest.setOther_diseases(getDiseaseList(13));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postDiseaseProfileDetails(getActivity(), diseasesProfilePostRequest, url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        moveToParticipantsScreen();
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
