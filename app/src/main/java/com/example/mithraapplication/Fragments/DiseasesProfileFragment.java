package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.Fragments.RegistrationFragment.trackingName;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateDiseaseProfileTracking;
import com.example.mithraapplication.ModelClasses.UpdateSocioDemographyTracking;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.ParticipantsScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DiseasesProfileFragment extends Fragment implements HandleServerResponse {

    private ArrayList<DiseasesProfile> diseasesProfilesArray = new ArrayList<>();
    private RecyclerView recyclerViewLeft;
    private DiseasesProfileAdapter diseasesProfileAdapter;
    private Button nextDiseaseProfileButton;
    private ArrayList<DiseasesProfile> diseasesProfile = new ArrayList<>();
    private MithraUtility mithraUtility = new MithraUtility();
    private TextView diseaseTV, diagnosedAgeTV, receivedTreatmentTV, limitActivitiesTV, specifyDiseaseTV;
    private EditText diagnosedAgeET, specifyDiseaseET;
    private Button yesDiseaseButton, noDiseaseButton, yesReceivedTreatmentButton, noReceivedTreatmentButton, yesLimitActivities,  noLimitActivities, editButton;
    private ParticipantProfileScreen participantsProfileScreen;
    private TrackingParticipantStatus trackingParticipantStatus = null;
    private String isEditable;
    private Context context;
    private DiseasesProfilePostRequest diseasesProfileDetails = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        participantsProfileScreen = (ParticipantProfileScreen) getActivity();
        return inflater.inflate(R.layout.fragment_disease_profile_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        RegisterViews(view);
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getDisease_profile()!=null) {
            if(isEditable!= null && !isEditable.equals("true")){
                callGetIndividualDiseaseProfileDetails();
            }
        }else{
            isEditable = "true";
            editButton.setEnabled(false);
            setRecyclerView(isEditable);
        }
        onClickOfNextButton();
        setOnclickOfEditButton();

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("DiseasesProfileData"));
    }

    public DiseasesProfileFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable){
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = intent.getBundleExtra("ParticipantDiseaseData");
            diseasesProfile = (ArrayList<DiseasesProfile>) args.getSerializable("ARRAYLIST");
            if(isEditable!=null && !isEditable.equals("true")){
                callServerUpdateDiseaseProfileDetails();
            }else{
                callServerPostDiseasesProfile();
            }
        }
    };

    @Override
    public void onResume() {
        participantsProfileScreen.registerReceiver(mMessageReceiver, new IntentFilter("ParticipantDiseaseData"));
        super.onResume();
    }

    @Override
    public void onPause() {
        participantsProfileScreen.unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void RegisterViews(View view){
        recyclerViewLeft = view.findViewById(R.id.diseasesRecyclerView);
        nextDiseaseProfileButton = view.findViewById(R.id.diseasesNextButton);
        if(isEditable!=null && isEditable.equalsIgnoreCase("false")){
           nextDiseaseProfileButton.setEnabled(false);
        }else{
            nextDiseaseProfileButton.setEnabled(true);
        }

        diseaseTV = view.findViewById(R.id.diseaseTV);
        diagnosedAgeTV = view.findViewById(R.id.diagnosedAgeTV);
        receivedTreatmentTV = view.findViewById(R.id.treatmentReceivedTV);
        limitActivitiesTV = view.findViewById(R.id.limitActivitiesTV);
        specifyDiseaseTV = view.findViewById(R.id.specifyDiseaseTV);

        diagnosedAgeET = view.findViewById(R.id.diagnosedAgeET);
        specifyDiseaseET = view.findViewById(R.id.specifyDiseaseET);

        yesDiseaseButton = view.findViewById(R.id.yesDiseaseButton);
        noDiseaseButton = view.findViewById(R.id.noDiseaseButton);
        yesReceivedTreatmentButton = view.findViewById(R.id.yesTreatmentButton);
        noReceivedTreatmentButton = view.findViewById(R.id.noTreatmentButton);
        yesLimitActivities = view.findViewById(R.id.yesLimitActivitiesButton);
        noLimitActivities = view.findViewById(R.id.noLimitActivitiesButton);
        editButton = requireActivity().findViewById(R.id.profileEditButton);
    }

    private void initializeData(){
        DiseasesProfile profile1 = new DiseasesProfile();
        profile1.setDiseaseName("DIABETES MELLITUS");
        profile1.setDiseaseNameKannada("ಮಧುಮೇಹ");
        profile1.setDiagnosedAge("How old were you when this was diagnosed?");
        profile1.setReceivedTreatment("Do you receive treatment for this condition?");
        profile1.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile1);

        DiseasesProfile profile2 = new DiseasesProfile();
        profile2.setDiseaseName("HYPERTENSION");
        profile2.setDiseaseNameKannada("ಅಧಿಕ ರಕ್ತದೊತ್ತಡ");
        profile2.setDiagnosedAge("How old were you when this was diagnosed?");
        profile2.setReceivedTreatment("Do you receive treatment for this condition?");
        profile2.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile2);

        DiseasesProfile profile3 = new DiseasesProfile();
        profile3.setDiseaseName("HEART DISEASE");
        profile3.setDiseaseNameKannada("ಹೃದಯರೋಗ");
        profile3.setDiagnosedAge("How old were you when this was diagnosed?");
        profile3.setReceivedTreatment("Do you receive treatment for this condition?");
        profile3.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile3);

        DiseasesProfile profile4 = new DiseasesProfile();
        profile4.setDiseaseName("Thyroid");
        profile4.setDiseaseNameKannada("ಥೈರಾಯ್ಡ್");
        profile4.setDiagnosedAge("How old were you when this was diagnosed?");
        profile4.setReceivedTreatment("Do you receive treatment for this condition?");
        profile4.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile4);

        DiseasesProfile profile5 = new DiseasesProfile();
        profile5.setDiseaseName("CHRONIC LIVER DISEASE");
        profile5.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಯಕೃತ್ತಿನ ರೋಗ");
        profile5.setDiagnosedAge("How old were you when this was diagnosed?");
        profile5.setReceivedTreatment("Do you receive treatment for this condition?");
        profile5.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile5);

        DiseasesProfile profile6 = new DiseasesProfile();
        profile6.setDiseaseName("CHRONIC RENAL DISEASE");
        profile6.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಮೂತ್ರಪಿಂಡ ಕಾಯಿಲೆ");
        profile6.setDiagnosedAge("How old were you when this was diagnosed?");
        profile6.setReceivedTreatment("Do you receive treatment for this condition?");
        profile6.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile6);

        DiseasesProfile profile7 = new DiseasesProfile();
        profile7.setDiseaseName("MALIGNANCY");
        profile7.setDiseaseNameKannada("ಕ್ಯಾನ್ಸರ್");
        profile7.setDiagnosedAge("How old were you when this was diagnosed?");
        profile7.setReceivedTreatment("Do you receive treatment for this condition?");
        profile7.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile7);

        DiseasesProfile profile8 = new DiseasesProfile();
        profile8.setDiseaseName("DISABILITIES");
        profile8.setDiseaseNameKannada("ವಿಕಲಾಂಗತೆಗಳು");
        profile8.setDiagnosedAge("How old were you when this was diagnosed?");
        profile8.setReceivedTreatment("Do you receive treatment for this condition?");
        profile8.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile8);

        DiseasesProfile profile9 = new DiseasesProfile();
        profile9.setDiseaseName("GASTRIC PROBLEM");
        profile9.setDiseaseNameKannada("ಗ್ಯಾಸ್ಟ್ರಿಕ್ ಸಮಸ್ಯೆ");
        profile9.setDiagnosedAge("How old were you when this was diagnosed?");
        profile9.setReceivedTreatment("Do you receive treatment for this condition?");
        profile9.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile9);

        DiseasesProfile profile10 = new DiseasesProfile();
        profile10.setDiseaseName("MENTAL ILLNESS");
        profile10.setDiseaseNameKannada("ಮಾನಸಿಕ ಅನಾರೋಗ್ಯ");
        profile10.setDiagnosedAge("How old were you when this was diagnosed?");
        profile10.setReceivedTreatment("Do you receive treatment for this condition?");
        profile10.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile10);

        DiseasesProfile profile11 = new DiseasesProfile();
        profile11.setDiseaseName("EPILEPSY");
        profile11.setDiseaseNameKannada("ಮೂರ್ಛೆರೋಗ");
        profile11.setDiagnosedAge("How old were you when this was diagnosed?");
        profile11.setReceivedTreatment("Do you receive treatment for this condition?");
        profile11.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile11);

        DiseasesProfile profile12 = new DiseasesProfile();
        profile12.setDiseaseName("ASTHMA");
        profile12.setDiseaseNameKannada("ಉಬ್ಬಸ");
        profile12.setDiagnosedAge("How old were you when this was diagnosed?");
        profile12.setReceivedTreatment("Do you receive treatment for this condition?");
        profile12.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile12);

        DiseasesProfile profile13 = new DiseasesProfile();
        profile13.setDiseaseName("SKIN DISEASE");
        profile13.setDiseaseNameKannada("ಚರ್ಮದ ಕಾಯಿಲೆ");
        profile13.setDiagnosedAge("How old were you when this was diagnosed?");
        profile13.setReceivedTreatment("Do you receive treatment for this condition?");
        profile13.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile13);

        DiseasesProfile profile14 = new DiseasesProfile();
        profile14.setDiseaseName("ANY OTHER DISEASES");
        profile14.setDiseaseNameKannada("ಇತರ ರೋಗ");
        profile14.setSpecifyDisease("Specify other disease");
        profile14.setDiagnosedAge("How old were you when this was diagnosed?");
        profile14.setReceivedTreatment("Do you receive treatment for this condition?");
        profile14.setLimitActivities("Does this condition limit your activities?");
        diseasesProfilesArray.add(profile14);
    }

    private void setRecyclerView(String isEditable){
        diseasesProfileAdapter = new DiseasesProfileAdapter(context, diseasesProfilesArray, isEditable);
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

    private void setOnclickOfEditButton(){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setBackgroundResource(R.drawable.status_button);
                isEditable = "reEdit";
                setRecyclerView(isEditable);
            }
        });
    }

    private void moveToParticipantsScreen(){
        Intent intent = new Intent(getActivity(), ParticipantsScreen.class);
        startActivity(intent);
    }

    private void getDiseasesProfileArrayList(){
        diseasesProfilesArray.clear();

        String diseaseName = diseasesProfileDetails.getDiabetes_mellitus();
        DiseasesProfile profile1 = new DiseasesProfile();
        profile1.setDiseaseName("DIABETES MELLITUS");
        profile1.setDiseaseNameKannada("ಮಧುಮೇಹ");
        String[] arr;
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile1.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile1.setDiagnosed(arr[0]);
            profile1.setDiagnosedAge(arr[1]);
            profile1.setReceivedTreatment(arr[2]);
            profile1.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile1);

        diseaseName = diseasesProfileDetails.getHypertension();
        DiseasesProfile profile2 = new DiseasesProfile();
        profile2.setDiseaseName("HYPERTENSION");
        profile2.setDiseaseNameKannada("ಅಧಿಕ ರಕ್ತದೊತ್ತಡ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null" ) || diseaseName.equals("")){
            profile2.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile2.setDiagnosed(arr[0]);
            profile2.setDiagnosedAge(arr[1]);
            profile2.setReceivedTreatment(arr[2]);
            profile2.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile2);

        diseaseName = diseasesProfileDetails.getHeart_disease();
        DiseasesProfile profile3 = new DiseasesProfile();
        profile3.setDiseaseName("HEART DISEASE");
        profile3.setDiseaseNameKannada("ಹೃದಯರೋಗ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile3.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile3.setDiagnosed(arr[0]);
            profile3.setDiagnosedAge(arr[1]);
            profile3.setReceivedTreatment(arr[2]);
            profile3.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile3);

        diseaseName = diseasesProfileDetails.getThyroid();
        DiseasesProfile profile4 = new DiseasesProfile();
        profile4.setDiseaseName("Thyroid");
        profile4.setDiseaseNameKannada("ಥೈರಾಯ್ಡ್");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile4.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile4.setDiagnosed(arr[0]);
            profile4.setDiagnosedAge(arr[1]);
            profile4.setReceivedTreatment(arr[2]);
            profile4.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile4);

        diseaseName = diseasesProfileDetails.getChronic_liver_disease();
        DiseasesProfile profile5 = new DiseasesProfile();
        profile5.setDiseaseName("CHRONIC LIVER DISEASE");
        profile5.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಯಕೃತ್ತಿನ ರೋಗ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile5.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile5.setDiagnosed(arr[0]);
            profile5.setDiagnosedAge(arr[1]);
            profile5.setReceivedTreatment(arr[2]);
            profile5.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile5);

        diseaseName = diseasesProfileDetails.getChronic_renal_disease();
        DiseasesProfile profile6 = new DiseasesProfile();
        profile6.setDiseaseName("CHRONIC RENAL DISEASE");
        profile6.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಮೂತ್ರಪಿಂಡ ಕಾಯಿಲೆ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile6.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile6.setDiagnosed(arr[0]);
            profile6.setDiagnosedAge(arr[1]);
            profile6.setReceivedTreatment(arr[2]);
            profile6.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile6);

        diseaseName = diseasesProfileDetails.getMalignancy();
        DiseasesProfile profile7 = new DiseasesProfile();
        profile7.setDiseaseName("MALIGNANCY");
        profile7.setDiseaseNameKannada("ಕ್ಯಾನ್ಸರ್");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile7.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile7.setDiagnosed(arr[0]);
            profile7.setDiagnosedAge(arr[1]);
            profile7.setReceivedTreatment(arr[2]);
            profile7.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile7);

        diseaseName = diseasesProfileDetails.getDisabilities();
        DiseasesProfile profile8 = new DiseasesProfile();
        profile8.setDiseaseName("DISABILITIES");
        profile8.setDiseaseNameKannada("ವಿಕಲಾಂಗತೆಗಳು");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile8.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile8.setDiagnosed(arr[0]);
            profile8.setDiagnosedAge(arr[1]);
            profile8.setReceivedTreatment(arr[2]);
            profile8.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile8);

        diseaseName = diseasesProfileDetails.getGastric_problem();
        DiseasesProfile profile9 = new DiseasesProfile();
        profile9.setDiseaseName("GASTRIC PROBLEM");
        profile9.setDiseaseNameKannada("ಗ್ಯಾಸ್ಟ್ರಿಕ್ ಸಮಸ್ಯೆ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile9.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile9.setDiagnosed(arr[0]);
            profile9.setDiagnosedAge(arr[1]);
            profile9.setReceivedTreatment(arr[2]);
            profile9.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile9);

        diseaseName = diseasesProfileDetails.getMental_illness();
        DiseasesProfile profile10 = new DiseasesProfile();
        profile10.setDiseaseName("MENTAL ILLNESS");
        profile10.setDiseaseNameKannada("ಮಾನಸಿಕ ಅನಾರೋಗ್ಯ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile10.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile10.setDiagnosed(arr[0]);
            profile10.setDiagnosedAge(arr[1]);
            profile10.setReceivedTreatment(arr[2]);
            profile10.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile10);

        diseaseName = diseasesProfileDetails.getEpilepsy();
        DiseasesProfile profile11 = new DiseasesProfile();
        profile11.setDiseaseName("EPILEPSY");
        profile11.setDiseaseNameKannada("ಮೂರ್ಛೆರೋಗ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile11.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile11.setDiagnosed(arr[0]);
            profile11.setDiagnosedAge(arr[1]);
            profile11.setReceivedTreatment(arr[2]);
            profile11.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile11);

        diseaseName = diseasesProfileDetails.getAsthma();
        DiseasesProfile profile12 = new DiseasesProfile();
        profile12.setDiseaseName("ASTHMA");
        profile12.setDiseaseNameKannada("ಉಬ್ಬಸ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile12.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile12.setDiagnosed(arr[0]);
            profile12.setDiagnosedAge(arr[1]);
            profile12.setReceivedTreatment(arr[2]);
            profile12.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile12);

        diseaseName = diseasesProfileDetails.getSkin_disease();
        DiseasesProfile profile13 = new DiseasesProfile();
        profile13.setDiseaseName("SKIN DISEASE");
        profile13.setDiseaseNameKannada("ಚರ್ಮದ ಕಾಯಿಲೆ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile13.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile13.setDiagnosed(arr[0]);
            profile13.setDiagnosedAge(arr[1]);
            profile13.setReceivedTreatment(arr[2]);
            profile13.setLimitActivities(arr[3]);
        }
        diseasesProfilesArray.add(profile13);

        diseaseName = diseasesProfileDetails.getOther_diseases();
        DiseasesProfile profile14 = new DiseasesProfile();
        profile14.setDiseaseName("ANY OTHER DISEASES");
        profile14.setDiseaseNameKannada("ಇತರ ರೋಗ");
        if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
            profile14.setDiagnosed(diseaseName);
        }else{
            diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
            arr = diseaseName.split(",");
            profile14.setDiagnosed(arr[0]);
            profile14.setDiagnosedAge(arr[1]);
            profile14.setReceivedTreatment(arr[2]);
            profile14.setLimitActivities(arr[3]);
        }
//        profile14.setSpecifyDisease(arr[4]);
        diseasesProfilesArray.add(profile14);

        setRecyclerView(isEditable);

    }

    private String getDiseaseList(int position){
        List<String> disease = new ArrayList<>();
        disease.add(diseasesProfile.get(position).getDiagnosed());
        disease.add(diseasesProfile.get(position).getDiagnosedAge());
        disease.add(diseasesProfile.get(position).getReceivedTreatment());
        disease.add(diseasesProfile.get(position).getLimitActivities());
        disease.add(diseasesProfile.get(position).getSpecifyDisease());
        String diseaseStr = String.join(",", disease );
        diseaseStr = "[" + diseaseStr + "]";

        Log.i("ARRAY LIST", "Diseases Data - list" + disease);
        Log.i("ARRAY LIST", "Diseases Data - String" + diseaseStr);

        return diseaseStr;
    }

    private DiseasesProfilePostRequest getDiseaseProfileData(){
        DiseasesProfilePostRequest diseasesProfilePostRequest = new DiseasesProfilePostRequest();
        diseasesProfilePostRequest.setUser_pri_id(mithraUtility.getSharedPreferencesData(participantsProfileScreen, participantsProfileScreen.getString(R.string.primaryID), participantsProfileScreen.getString(R.string.participantPrimaryID)));
        if(diseasesProfile.get(0).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setDiabetes_mellitus(getDiseaseList(0));
        }else{
            diseasesProfilePostRequest.setDiabetes_mellitus(diseasesProfile.get(0).getDiagnosed());
        }

        if(diseasesProfile.get(1).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setHypertension(getDiseaseList(1));
        }else{
            diseasesProfilePostRequest.setHypertension(diseasesProfile.get(1).getDiagnosed());
        }

        if(diseasesProfile.get(2).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setHeart_disease(getDiseaseList(2));
        }else{
            diseasesProfilePostRequest.setHeart_disease(diseasesProfile.get(2).getDiagnosed());
        }

        if(diseasesProfile.get(3).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setThyroid(getDiseaseList(3));
        }else{
            diseasesProfilePostRequest.setThyroid(diseasesProfile.get(3).getDiagnosed());
        }

        if(diseasesProfile.get(4).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setChronic_liver_disease(getDiseaseList(4));
        }else{
            diseasesProfilePostRequest.setChronic_liver_disease(diseasesProfile.get(4).getDiagnosed());
        }

        if(diseasesProfile.get(5).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setChronic_renal_disease(getDiseaseList(5));
        }else{
            diseasesProfilePostRequest.setChronic_renal_disease(diseasesProfile.get(5).getDiagnosed());
        }

        if(diseasesProfile.get(6).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setMalignancy(getDiseaseList(6));
        }else{
            diseasesProfilePostRequest.setMalignancy(diseasesProfile.get(6).getDiagnosed());
        }

        if(diseasesProfile.get(7).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setDisabilities(getDiseaseList(7));
        }else{
            diseasesProfilePostRequest.setDisabilities(diseasesProfile.get(7).getDiagnosed());
        }

        if(diseasesProfile.get(8).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setGastric_problem(getDiseaseList(8));
        }else{
            diseasesProfilePostRequest.setGastric_problem(diseasesProfile.get(8).getDiagnosed());
        }

        if(diseasesProfile.get(9).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setMental_illness(getDiseaseList(9));
        }else{
            diseasesProfilePostRequest.setMental_illness(diseasesProfile.get(9).getDiagnosed());
        }

        if(diseasesProfile.get(10).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setEpilepsy(getDiseaseList(10));
        }else{
            diseasesProfilePostRequest.setEpilepsy(diseasesProfile.get(10).getDiagnosed());
        }

        if(diseasesProfile.get(11).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setAsthma(getDiseaseList(11));
        }else{
            diseasesProfilePostRequest.setAsthma(diseasesProfile.get(11).getDiagnosed());
        }

        if(diseasesProfile.get(12).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setSkin_disease(getDiseaseList(12));
        }else{
            diseasesProfilePostRequest.setSkin_disease(diseasesProfile.get(12).getDiagnosed());
        }

        if(diseasesProfile.get(13).getDiagnosed().equals("yes")){
            diseasesProfilePostRequest.setOther_diseases(getDiseaseList(13));
        }else{
            diseasesProfilePostRequest.setOther_diseases(diseasesProfile.get(13).getDiagnosed());
        }

        diseasesProfilePostRequest.setActive("yes");
        diseasesProfilePostRequest.setCreated_user(mithraUtility.getSharedPreferencesData(participantsProfileScreen, participantsProfileScreen.getString(R.string.primaryID), participantsProfileScreen.getString(R.string.coordinatorPrimaryID)));

        return diseasesProfilePostRequest;
    }

    private void callServerPostDiseasesProfile(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/disease_profile";
        DiseasesProfilePostRequest diseasesProfilePostRequest = getDiseaseProfileData();
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postDiseaseProfileDetails(getActivity(), diseasesProfilePostRequest, url);
    }

    private void callUpdateTrackingDetails(String diseaseProfileName){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/tracking/" + trackingName;
        UpdateDiseaseProfileTracking trackingParticipantStatus = new UpdateDiseaseProfileTracking();
        trackingParticipantStatus.setDisease_profile(diseaseProfileName);
        trackingParticipantStatus.setModified_user(mithraUtility.getSharedPreferencesData(context, getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        trackingParticipantStatus.setEnroll("yes");
        String participantBaseTime = mithraUtility.getCurrentTime();
        trackingParticipantStatus.setRegistrationCompleteTime(participantBaseTime);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putTrackingStatusDiseaseProfile(getActivity(), trackingParticipantStatus, url);
    }

    private void callGetIndividualDiseaseProfileDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/disease_profile/" + trackingParticipantStatus.getDisease_profile();
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantDiseaseProfileDetails(getActivity(), url);
    }

    private void callServerUpdateDiseaseProfileDetails() {
        String url = "http://"+ participantsProfileScreen.getString(R.string.base_url)+ "/api/resource/disease_profile/" +  trackingParticipantStatus.getDisease_profile();
        DiseasesProfilePostRequest diseasesProfilePostRequest = getDiseaseProfileData();
        diseasesProfilePostRequest.setUser_pri_id(trackingParticipantStatus.getUser_pri_id());
        diseasesProfilePostRequest.setModified_user(mithraUtility.getSharedPreferencesData(participantsProfileScreen, participantsProfileScreen.getString(R.string.primaryID), participantsProfileScreen.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putDiseaseProfileDetails(participantsProfileScreen, diseasesProfilePostRequest, url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type type = new TypeToken<FrappeResponse>(){}.getType();
        if(jsonObjectRegistration.get("data")!=null){
            FrappeResponse frappeResponse;
            frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
            if(isEditable!=null && isEditable.equals("true")){
                if (frappeResponse != null && frappeResponse.getDoctype().equals("disease_profile")) {
                    String registrationName = frappeResponse.getName();
                    callUpdateTrackingDetails(registrationName);
                } else if (frappeResponse != null && frappeResponse.getDoctype().equals("tracking")) {
                    trackingName = frappeResponse.getName();
                    moveToParticipantsScreen();
                }
            }else{
                Type typeDiseaseProfile = new TypeToken<DiseasesProfilePostRequest>(){}.getType();
                diseasesProfileDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeDiseaseProfile);
                trackingParticipantStatus.setUser_pri_id(diseasesProfileDetails.getUser_pri_id());
                editButton.setEnabled(true);
                getDiseasesProfileArrayList();
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
            }
        }else{
            //Do nothing
            Log.i("RegistrationFragment", "JsonObjectRegistration data is Empty");
        }    }

    @Override
    public void responseReceivedFailure(String message) {

    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            participantsProfileScreen.isLanguageSelected = "kn";
        }else{
            participantsProfileScreen.isLanguageSelected = "en";
        }

        if(diagnosedAgeTV!=null){
            diagnosedAgeTV.setText(R.string.diagnosed_age);
        }

        if(receivedTreatmentTV!=null){
            receivedTreatmentTV.setText(R.string.received_treatment);
        }

        if(limitActivitiesTV!=null){
            limitActivitiesTV.setText(R.string.limit_activities);
        }

        if(specifyDiseaseTV!=null){
            specifyDiseaseTV.setText(R.string.specify_disease);
        }

        if(yesDiseaseButton!=null){
            yesDiseaseButton.setText(R.string.yes);
        }

        if(noDiseaseButton!=null){
            noDiseaseButton.setText(R.string.no);
        }

        if(yesReceivedTreatmentButton!=null){
            yesReceivedTreatmentButton.setText(R.string.yes);
        }

        if(noReceivedTreatmentButton!=null){
            noReceivedTreatmentButton.setText(R.string.no);
        }

        if(yesLimitActivities!=null){
            yesLimitActivities.setText(R.string.yes);
        }

        if(noLimitActivities!=null){
            noLimitActivities.setText(R.string.no);
        }

        if(nextDiseaseProfileButton!=null){
            nextDiseaseProfileButton.setText(R.string.save);
        }

        if(diseasesProfileAdapter!=null){
            diseasesProfileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}
