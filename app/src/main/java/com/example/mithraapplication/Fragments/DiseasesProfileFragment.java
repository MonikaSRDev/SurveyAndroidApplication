package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.Fragments.RegistrationFragment.trackingName;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.DiseaseProfileServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.DiseasesProfilePostRequest;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateDiseaseProfileTracking;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.ParticipantsScreen;
import com.example.mithraapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DiseasesProfileFragment extends Fragment implements HandleServerResponse, DiseaseProfileServerEvents {

    private ArrayList<DiseasesProfile> diseasesProfilesArray = new ArrayList<>();
    private RecyclerView recyclerViewLeft;
    private DiseasesProfileAdapter diseasesProfileAdapter;
    private Button nextDiseaseProfileButton;
    private ArrayList<DiseasesProfile> diseasesProfile = new ArrayList<>();
    private final MithraUtility mithraUtility = new MithraUtility();
    private TextView diseaseTV, diagnosedAgeTV, receivedTreatmentTV, limitActivitiesTV, specifyDiseaseTV;
    private Button yesDiseaseButton, noDiseaseButton, yesReceivedTreatmentButton, noReceivedTreatmentButton, yesLimitActivities,  noLimitActivities, editButton;
    private TrackingParticipantStatus trackingParticipantStatus;
    private String isEditable;
    private Context context;
    private DiseasesProfilePostRequest diseasesProfileDetails = null;
    private DiseasesProfilePostRequest diseasesProfilePostRequest = new DiseasesProfilePostRequest();
    private Dialog dialog;
    private PHQLocations phqLocations;
    private boolean isConfigChanged = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disease_profile_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        startProgressBar();
        RegisterViews(view);
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getDisease_profile()!=null) {
            if(isEditable!= null && !isEditable.equals("true")){
                editButton.setEnabled(true);
                editButton.setVisibility(View.VISIBLE);
                callGetIndividualDiseaseProfileDetails();
            }
        }else{
            isEditable = "true";
            editButton.setEnabled(false);
            editButton.setVisibility(View.GONE);
            setRecyclerView(isEditable, diseasesProfilesArray);
        }
        if(isEditable!= null && isEditable.equals("false")){
            nextDiseaseProfileButton.setVisibility(View.INVISIBLE);
        }
        onClickOfNextButton();
        setOnclickOfEditButton();
    }

    public DiseasesProfileFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable, PHQLocations phqLocations){
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.phqLocations = phqLocations;
    }

    /**
     * Description : This broadcast receiver is used get the data from the adapter
     */
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = intent.getBundleExtra("ParticipantDiseaseData");
//            diseasesProfile.clear();
            diseasesProfile = (ArrayList<DiseasesProfile>) args.getSerializable("ParticipantDiseaseList");
            if(isConfigChanged){
                if(diseasesProfileAdapter!=null){
                    setRecyclerView(isEditable, diseasesProfile);
                    isConfigChanged = false;
                }
            }else{
                boolean isValid = getDiseaseProfileData();
                if(isValid){
                    if(isEditable!=null && !isEditable.equals("true")){
                        callServerUpdateDiseaseProfileDetails();
                    }else{
                        callServerPostDiseasesProfile();
                    }
                }else{
                    Toast.makeText(context, "Please give all the details.", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("DiseasesProfileData"));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    /**
     * Description : This method is used to register the views to the Fragment
     */
    private void RegisterViews(View view){
        recyclerViewLeft = view.findViewById(R.id.diseasesRecyclerView);
        nextDiseaseProfileButton = view.findViewById(R.id.diseasesNextButton);
        nextDiseaseProfileButton.setVisibility(View.INVISIBLE);

        diseaseTV = view.findViewById(R.id.diseaseTV);
        diagnosedAgeTV = view.findViewById(R.id.diagnosedAgeTV);
        receivedTreatmentTV = view.findViewById(R.id.treatmentReceivedTV);
        limitActivitiesTV = view.findViewById(R.id.limitActivitiesTV);
        specifyDiseaseTV = view.findViewById(R.id.specifyDiseaseTV);

        yesDiseaseButton = view.findViewById(R.id.yesDiseaseButton);
        noDiseaseButton = view.findViewById(R.id.noDiseaseButton);
        yesReceivedTreatmentButton = view.findViewById(R.id.yesTreatmentButton);
        noReceivedTreatmentButton = view.findViewById(R.id.noTreatmentButton);
        yesLimitActivities = view.findViewById(R.id.yesLimitActivitiesButton);
        noLimitActivities = view.findViewById(R.id.noLimitActivitiesButton);
        editButton = requireActivity().findViewById(R.id.profileEditButton);
    }

    /**
     * Description : This method is used to prepare the data to be shown to the user.
     */
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

    /**
     * Description : Sets the recycler view with the data disease profile array list and the editable parameter
     */
    private void setRecyclerView(String isEditable, ArrayList<DiseasesProfile> diseasesProfileArrayList){
        diseasesProfileAdapter = new DiseasesProfileAdapter(context, diseasesProfileArrayList, isEditable);
        recyclerViewLeft.setAdapter(diseasesProfileAdapter);
        recyclerViewLeft.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                stopProgressBar();
                if(isEditable!=null && isEditable.equals("true")){
                    nextDiseaseProfileButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Description : On clicking the button, method in the adapter is triggered and the data from the adapter is fetched and returned to fragment.
     */
    private void onClickOfNextButton(){
        nextDiseaseProfileButton.setOnClickListener(v -> {
            if(diseasesProfileAdapter != null) {
                diseasesProfileAdapter.sendDataToActivity();
            }
        });
    }

    /**
     * Description : Makes the views editable for user to update the disease profile data on clicking the edit button.
     */
    private void setOnclickOfEditButton(){
        editButton.setOnClickListener(v -> {
            if(isEditable!=null && isEditable.equals("false")){
                editButton.setBackgroundResource(R.drawable.status_button);
                isEditable = "reEdit";
                nextDiseaseProfileButton.setVisibility(View.VISIBLE);
                nextDiseaseProfileButton.setEnabled(true);
                nextDiseaseProfileButton.setText(R.string.save);
                nextDiseaseProfileButton.setBackgroundResource(R.drawable.button_background);
                nextDiseaseProfileButton.setTextColor(getResources().getColor(R.color.white, context.getTheme()));
                setRecyclerView(isEditable, diseasesProfilesArray);
            }else if(isEditable!=null && isEditable.equals("reEdit")){
                editButton.setBackgroundResource(R.drawable.yes_no_button);
                isEditable = "false";
                nextDiseaseProfileButton.setVisibility(View.INVISIBLE);
                nextDiseaseProfileButton.setEnabled(false);
                nextDiseaseProfileButton.setText(R.string.save);
                nextDiseaseProfileButton.setBackgroundResource(R.drawable.inputs_background);
                nextDiseaseProfileButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
                setRecyclerView(isEditable, diseasesProfilesArray);
            }
        });
    }

    /**
     * Description : Move from profile screen activity to the ParticipantScreen activity.
     */
    private void moveToParticipantsScreen(){
        Intent intent = new Intent(context, ParticipantsScreen.class);
        intent.putExtra("PHQLocations", (Serializable) phqLocations);
        startActivity(intent);
    }

    private void startProgressBar(){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_progress_bar, null);

        ProgressBar progressbar = customLayout.findViewById(R.id.progressBar);

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    private void stopProgressBar(){
        dialog.dismiss();
    }

    /**
     * Description : Converts the data received from the server to DiseaseProfile type.
     */
    private void getDiseasesProfileArrayList(){
        diseasesProfilesArray.clear();

        String diseaseName = diseasesProfileDetails.getDiabetes_mellitus();
        DiseasesProfile profile1 = new DiseasesProfile();
        profile1.setDiseaseName("DIABETES MELLITUS");
        profile1.setDiseaseNameKannada("ಮಧುಮೇಹ");
        String[] arr;
        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile1.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile1.setDiagnosed(arr[0]);
                profile1.setDiagnosedAge(arr[1]);
                profile1.setReceivedTreatment(arr[2]);
                profile1.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile1.setSpecifyDisease(arr[4]);
                }else{
                    profile1.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile1);

        diseaseName = diseasesProfileDetails.getHypertension();
        DiseasesProfile profile2 = new DiseasesProfile();
        profile2.setDiseaseName("HYPERTENSION");
        profile2.setDiseaseNameKannada("ಅಧಿಕ ರಕ್ತದೊತ್ತಡ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null" ) || diseaseName.equals("")){
                profile2.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile2.setDiagnosed(arr[0]);
                profile2.setDiagnosedAge(arr[1]);
                profile2.setReceivedTreatment(arr[2]);
                profile2.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile2.setSpecifyDisease(arr[4]);
                }else{
                    profile2.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile2);

        diseaseName = diseasesProfileDetails.getHeart_disease();
        DiseasesProfile profile3 = new DiseasesProfile();
        profile3.setDiseaseName("HEART DISEASE");
        profile3.setDiseaseNameKannada("ಹೃದಯರೋಗ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile3.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile3.setDiagnosed(arr[0]);
                profile3.setDiagnosedAge(arr[1]);
                profile3.setReceivedTreatment(arr[2]);
                profile3.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile3.setSpecifyDisease(arr[4]);
                }else{
                    profile3.setSpecifyDisease("null");
                }
            }
            diseasesProfilesArray.add(profile3);
        }

        diseaseName = diseasesProfileDetails.getThyroid();
        DiseasesProfile profile4 = new DiseasesProfile();
        profile4.setDiseaseName("Thyroid");
        profile4.setDiseaseNameKannada("ಥೈರಾಯ್ಡ್");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile4.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile4.setDiagnosed(arr[0]);
                profile4.setDiagnosedAge(arr[1]);
                profile4.setReceivedTreatment(arr[2]);
                profile4.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile4.setSpecifyDisease(arr[4]);
                }else{
                    profile4.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile4);

        diseaseName = diseasesProfileDetails.getChronic_liver_disease();
        DiseasesProfile profile5 = new DiseasesProfile();
        profile5.setDiseaseName("CHRONIC LIVER DISEASE");
        profile5.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಯಕೃತ್ತಿನ ರೋಗ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile5.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile5.setDiagnosed(arr[0]);
                profile5.setDiagnosedAge(arr[1]);
                profile5.setReceivedTreatment(arr[2]);
                profile5.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile5.setSpecifyDisease(arr[4]);
                }else{
                    profile5.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile5);

        diseaseName = diseasesProfileDetails.getChronic_renal_disease();
        DiseasesProfile profile6 = new DiseasesProfile();
        profile6.setDiseaseName("CHRONIC RENAL DISEASE");
        profile6.setDiseaseNameKannada("ದೀರ್ಘಕಾಲದ ಮೂತ್ರಪಿಂಡ ಕಾಯಿಲೆ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile6.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile6.setDiagnosed(arr[0]);
                profile6.setDiagnosedAge(arr[1]);
                profile6.setReceivedTreatment(arr[2]);
                profile6.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile6.setSpecifyDisease(arr[4]);
                }else{
                    profile6.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile6);

        diseaseName = diseasesProfileDetails.getMalignancy();
        DiseasesProfile profile7 = new DiseasesProfile();
        profile7.setDiseaseName("MALIGNANCY");
        profile7.setDiseaseNameKannada("ಕ್ಯಾನ್ಸರ್");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile7.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile7.setDiagnosed(arr[0]);
                profile7.setDiagnosedAge(arr[1]);
                profile7.setReceivedTreatment(arr[2]);
                profile7.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile7.setSpecifyDisease(arr[4]);
                }else{
                    profile7.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile7);

        diseaseName = diseasesProfileDetails.getDisabilities();
        DiseasesProfile profile8 = new DiseasesProfile();
        profile8.setDiseaseName("DISABILITIES");
        profile8.setDiseaseNameKannada("ವಿಕಲಾಂಗತೆಗಳು");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile8.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile8.setDiagnosed(arr[0]);
                profile8.setDiagnosedAge(arr[1]);
                profile8.setReceivedTreatment(arr[2]);
                profile8.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile8.setSpecifyDisease(arr[4]);
                }else{
                    profile8.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile8);

        diseaseName = diseasesProfileDetails.getGastric_problem();
        DiseasesProfile profile9 = new DiseasesProfile();
        profile9.setDiseaseName("GASTRIC PROBLEM");
        profile9.setDiseaseNameKannada("ಗ್ಯಾಸ್ಟ್ರಿಕ್ ಸಮಸ್ಯೆ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile9.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile9.setDiagnosed(arr[0]);
                profile9.setDiagnosedAge(arr[1]);
                profile9.setReceivedTreatment(arr[2]);
                profile9.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile9.setSpecifyDisease(arr[4]);
                }else{
                    profile9.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile9);

        diseaseName = diseasesProfileDetails.getMental_illness();
        DiseasesProfile profile10 = new DiseasesProfile();
        profile10.setDiseaseName("MENTAL ILLNESS");
        profile10.setDiseaseNameKannada("ಮಾನಸಿಕ ಅನಾರೋಗ್ಯ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile10.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile10.setDiagnosed(arr[0]);
                profile10.setDiagnosedAge(arr[1]);
                profile10.setReceivedTreatment(arr[2]);
                profile10.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile10.setSpecifyDisease(arr[4]);
                }else{
                    profile10.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile10);

        diseaseName = diseasesProfileDetails.getEpilepsy();
        DiseasesProfile profile11 = new DiseasesProfile();
        profile11.setDiseaseName("EPILEPSY");
        profile11.setDiseaseNameKannada("ಮೂರ್ಛೆರೋಗ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile11.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile11.setDiagnosed(arr[0]);
                profile11.setDiagnosedAge(arr[1]);
                profile11.setReceivedTreatment(arr[2]);
                profile11.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile11.setSpecifyDisease(arr[4]);
                }else{
                    profile11.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile11);

        diseaseName = diseasesProfileDetails.getAsthma();
        DiseasesProfile profile12 = new DiseasesProfile();
        profile12.setDiseaseName("ASTHMA");
        profile12.setDiseaseNameKannada("ಉಬ್ಬಸ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile12.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile12.setDiagnosed(arr[0]);
                profile12.setDiagnosedAge(arr[1]);
                profile12.setReceivedTreatment(arr[2]);
                profile12.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile12.setSpecifyDisease(arr[4]);
                }else{
                    profile12.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile12);

        diseaseName = diseasesProfileDetails.getSkin_disease();
        DiseasesProfile profile13 = new DiseasesProfile();
        profile13.setDiseaseName("SKIN DISEASE");
        profile13.setDiseaseNameKannada("ಚರ್ಮದ ಕಾಯಿಲೆ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile13.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile13.setDiagnosed(arr[0]);
                profile13.setDiagnosedAge(arr[1]);
                profile13.setReceivedTreatment(arr[2]);
                profile13.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile13.setSpecifyDisease(arr[4]);
                }else{
                    profile13.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile13);

        diseaseName = diseasesProfileDetails.getOther_diseases();
        DiseasesProfile profile14 = new DiseasesProfile();
        profile14.setDiseaseName("ANY OTHER DISEASES");
        profile14.setDiseaseNameKannada("ಇತರ ರೋಗ");

        if(diseaseName!=null){
            if(diseaseName.equalsIgnoreCase("no") || diseaseName.equalsIgnoreCase("null") || diseaseName.equals("")){
                profile14.setDiagnosed(diseaseName);
            }else{
                diseaseName = diseaseName.substring(1, (diseaseName.length() - 1));
                arr = diseaseName.split(",");
                profile14.setDiagnosed(arr[0]);
                profile14.setDiagnosedAge(arr[1]);
                profile14.setReceivedTreatment(arr[2]);
                profile14.setLimitActivities(arr[3]);
                if(diseaseName.length() > 4){
                    profile14.setSpecifyDisease(arr[4]);
                }else{
                    profile14.setSpecifyDisease("null");
                }
            }
        }
        diseasesProfilesArray.add(profile14);

        setRecyclerView(isEditable, diseasesProfilesArray);

    }

    /**
     * Description : Converts the Disease Profile object into a single list
     */
    private String getDiseaseList(int position){
        List<String> disease = new ArrayList<>();
        if(diseasesProfile.get(position).getDiagnosed()!=null && !diseasesProfile.get(position).getDiagnosed().equalsIgnoreCase("null")){
            disease.add(diseasesProfile.get(position).getDiagnosed());
        }else{
            return "false";
        }

        if(diseasesProfile.get(position).getDiagnosedAge()!=null && !diseasesProfile.get(position).getDiagnosedAge().equalsIgnoreCase("null")){
            disease.add(diseasesProfile.get(position).getDiagnosedAge());
        }else{
            return "false";
        }

        if(diseasesProfile.get(position).getReceivedTreatment()!=null && !diseasesProfile.get(position).getReceivedTreatment().equalsIgnoreCase("null")){
            disease.add(diseasesProfile.get(position).getReceivedTreatment());
        }else{
            return "false";
        }

        if(diseasesProfile.get(position).getLimitActivities()!=null && !diseasesProfile.get(position).getLimitActivities().equalsIgnoreCase("null")){
            disease.add(diseasesProfile.get(position).getLimitActivities());
        }else{
            return "false";
        }

        disease.add(diseasesProfile.get(position).getSpecifyDisease());

        String diseaseStr = String.join(",", disease );
        diseaseStr = "[" + diseaseStr + "]";

        Log.i("ARRAY LIST", "Diseases Data - list" + disease);
        Log.i("ARRAY LIST", "Diseases Data - String" + diseaseStr);

        return diseaseStr;
    }

    /**
     * Description : Prepare the data in the format to send it to the server.
     */
    private boolean getDiseaseProfileData(){
        diseasesProfilePostRequest = new DiseasesProfilePostRequest();
        diseasesProfilePostRequest.setUser_pri_id(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.participantPrimaryID)));
        if(diseasesProfile!=null && diseasesProfile.size() > 0){
            if(diseasesProfile.get(0).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(0);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setDiabetes_mellitus(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(0).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setDiabetes_mellitus(diseasesProfile.get(0).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(1).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(1);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setHypertension(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(1).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setHypertension(diseasesProfile.get(1).getDiagnosed());
            }else {
                return false;
            }

            if(diseasesProfile.get(2).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(2);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setHeart_disease(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(2).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setHeart_disease(diseasesProfile.get(2).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(3).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(3);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setThyroid(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(3).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setThyroid(diseasesProfile.get(3).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(4).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(4);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setChronic_liver_disease(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(4).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setChronic_liver_disease(diseasesProfile.get(4).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(5).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(5);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setChronic_renal_disease(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(5).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setChronic_renal_disease(diseasesProfile.get(5).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(6).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(6);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setMalignancy(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(6).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setMalignancy(diseasesProfile.get(6).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(7).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(7);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setDisabilities(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(7).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setDisabilities(diseasesProfile.get(7).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(8).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(8);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setGastric_problem(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(8).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setGastric_problem(diseasesProfile.get(8).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(9).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(9);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setMental_illness(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(9).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setMental_illness(diseasesProfile.get(9).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(10).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(10);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setEpilepsy(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(10).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setEpilepsy(diseasesProfile.get(10).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(11).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(11);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setAsthma(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(11).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setAsthma(diseasesProfile.get(11).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.get(12).getDiagnosed().equals("yes")){
                String valid = getDiseaseList(12);
                if(valid!=null && !valid.equalsIgnoreCase("false")){
                    diseasesProfilePostRequest.setSkin_disease(valid);
                }else{
                    return false;
                }
            }else if(diseasesProfile.get(12).getDiagnosed().equals("no")){
                diseasesProfilePostRequest.setSkin_disease(diseasesProfile.get(12).getDiagnosed());
            }else{
                return false;
            }

            if(diseasesProfile.size() > 13){
                if(diseasesProfile.get(13).getDiagnosed().equals("yes")){
                    String valid = getDiseaseList(13);
                    if(valid!=null && !valid.equalsIgnoreCase("false")){
                        diseasesProfilePostRequest.setOther_diseases(valid);
                    }else{
                        return false;
                    }
                }else if(diseasesProfile.get(13).getDiagnosed().equals("no")){
                    diseasesProfilePostRequest.setOther_diseases(diseasesProfile.get(13).getDiagnosed());
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }

        diseasesProfilePostRequest.setActive("yes");
        diseasesProfilePostRequest.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));

        return true;
    }

    /**
     * Description : Server call to post the disease profile data entered by the user.
     */
    private void callServerPostDiseasesProfile(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/disease_profile";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setDiseaseProfileServerEvents(this);
        requestObject.postDiseaseProfileDetails(context, diseasesProfilePostRequest, url);
    }

    /**
     * Description : Server call to update the tracking details table with the disease profile ID received from server after successfully storing the disease profile data on the database.
     */
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
        requestObject.setDiseaseProfileServerEvents(this);
        requestObject.putTrackingStatusDiseaseProfile(context, trackingParticipantStatus, url);
    }

    /**
     * Description : Server call to get the disease profile data of one participant with Disease Profile ID.
     */
    private void callGetIndividualDiseaseProfileDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/disease_profile/" + trackingParticipantStatus.getDisease_profile();
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setDiseaseProfileServerEvents(this);
        requestObject.getParticipantDiseaseProfileDetails(context, url);
    }

    /**
     * Description : Server call to update the disease profile data after the user edits.
     */
    private void callServerUpdateDiseaseProfileDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/disease_profile/" +  trackingParticipantStatus.getDisease_profile();
        diseasesProfilePostRequest.setUser_pri_id(trackingParticipantStatus.getUser_pri_id());
        diseasesProfilePostRequest.setModified_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setDiseaseProfileServerEvents(this);
        requestObject.putDiseaseProfileDetails(context, diseasesProfilePostRequest, url);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            ParticipantProfileScreen.isLanguageSelected = "kn";
        }else{
            ParticipantProfileScreen.isLanguageSelected = "en";
        }

        isConfigChanged = true;
        if(diseasesProfileAdapter!=null){
            diseasesProfileAdapter.sendDataToActivity();
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

//        if(diseasesProfileAdapter!=null){
//            setRecyclerView(isEditable);
//        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void postDiseasesProfile(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type type = new TypeToken<FrappeResponse>(){}.getType();
        if(jsonObjectRegistration.get("data")!=null){
            FrappeResponse frappeResponse;
            frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
            if (frappeResponse != null && frappeResponse.getDoctype().equals("disease_profile")) {
                String registrationName = frappeResponse.getName();
                mithraUtility.putSharedPreferencesData(context, context.getString(R.string.disease_profile_sp), frappeResponse.getUser_pri_id(), registrationName);
                callUpdateTrackingDetails(registrationName);
            }
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateDiseaseTrackingDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type type = new TypeToken<FrappeResponse>(){}.getType();
        if(jsonObjectRegistration.get("data")!=null){
            FrappeResponse frappeResponse;
            frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
            trackingName = frappeResponse.getName();
            moveToParticipantsScreen();
            mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.userScreeningName), context.getString(R.string.userScreeningID));
            mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.tracking), frappeResponse.getUser_pri_id());
            mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.registration_sp), frappeResponse.getUser_pri_id());
            mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.socio_demography_sp), frappeResponse.getUser_pri_id());
            mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.disease_profile_sp), frappeResponse.getUser_pri_id());
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void individualDiseaseProfileDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObjectRegistration.get("data")!=null){
            Type typeDiseaseProfile = new TypeToken<DiseasesProfilePostRequest>(){}.getType();
            diseasesProfileDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeDiseaseProfile);
            trackingParticipantStatus.setUser_pri_id(diseasesProfileDetails.getUser_pri_id());
            isEditable = "false";
            nextDiseaseProfileButton.setVisibility(View.INVISIBLE);
            editButton.setEnabled(true);
            getDiseasesProfileArrayList();
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateDiseaseProfileDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObjectRegistration.get("data")!=null){
            if(isEditable!=null && isEditable.equals("reEdit")){
                editButton.setBackgroundResource(R.drawable.yes_no_button);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
            }
            Type typeDiseaseProfile = new TypeToken<DiseasesProfilePostRequest>(){}.getType();
            diseasesProfileDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeDiseaseProfile);
            trackingParticipantStatus.setUser_pri_id(diseasesProfileDetails.getUser_pri_id());
            isEditable = "false";
            nextDiseaseProfileButton.setVisibility(View.INVISIBLE);
            editButton.setEnabled(true);
            getDiseasesProfileArrayList();
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(context, serverErrorResponse);
        }else{
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
