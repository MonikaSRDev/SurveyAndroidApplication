package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.PHQScreeningServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.ParticipantScreening;
import com.example.mithraapplication.ModelClasses.UpdateScreeningStatus;
import com.example.mithraapplication.PHQParticipantsScreen;
import com.example.mithraapplication.ParticipantsScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

public class PHQScreeningFragment extends Fragment implements HandleServerResponse, PHQScreeningServerEvents {

//    public PHQScreeningFragment(String isEditable) {
//        // Required empty public constructor
//        this.isEditable = isEditable;
//    }

    public PHQScreeningFragment(Context context, PHQParticipantDetails phqParticipantDetails, PHQLocations phqLocations) {
        this.context = context;
        this.phqParticipantDetails = phqParticipantDetails;
        this.phqLocations = phqLocations;
    }

    private Button inclusionYesButton, inclusionNoButton, ageAboveEighteenYesButton, ageAboveEighteenNoButton,
            residentYesButton, residentNoButton, CBOMeetingYesButton, CBOMeetingNoButton,
            exclusionYesButton, exclusionNoButton, mentalIllnessYesButton, mentalIllnessNoButton,
            substanceAbuseYesButton, substanceAbuseNoButton, suicideAttemptYesButton, suicideAttemptNoButton,
            participationConsentYesButton, participationConsentNoButton, screeningRegisterButton, editButton;

    private TextView inclusionTV, ageAboveEighteenTV, residentTV, CBOMeetingTV, exclusionTV, mentalIllnessTV,
            substanceAbuseTV, suicideAttemptTV, participationConsentTV, SHGName, participantName, PHQScreeningID;

    private String ageAboveEighteen = "NULL", resident = "NULL", CBOMeeting = "NULL", mentalIllness = "NULL", substanceAbuse = "NULL", suicideAttempt = "NULL", participationConsent = "NULL";
    private MithraUtility mithraUtility = new MithraUtility();

    private PHQParticipantDetails phqParticipantDetails;
    private PHQLocations phqLocations;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant_screening_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        getUserEnteredData();
        onClickRegisterButton();
    }

    private void RegisterViews(View view){
        inclusionTV = view.findViewById(R.id.inclusionTV);
        ageAboveEighteenTV = view.findViewById(R.id.aboveEighteenTV);
        residentTV = view.findViewById(R.id.residentTV);
        CBOMeetingTV = view.findViewById(R.id.CBOMeetingTV);
        exclusionTV = view.findViewById(R.id.exclusionTV);
        mentalIllnessTV = view.findViewById(R.id.mentalIllnessTV);
        substanceAbuseTV = view.findViewById(R.id.substanceAbuseTV);
        suicideAttemptTV = view.findViewById(R.id.suicideAttemptTV);
        participationConsentTV = view.findViewById(R.id.participationConsentTV);

        inclusionYesButton = view.findViewById(R.id.inclusionYesButton);
        inclusionNoButton = view.findViewById(R.id.inclusionNoButton);

        ageAboveEighteenYesButton = view.findViewById(R.id.aboveEighteenYesButton);
        ageAboveEighteenNoButton = view.findViewById(R.id.aboveEighteenNoButton);

        residentYesButton = view.findViewById(R.id.residentYesButton);
        residentNoButton = view.findViewById(R.id.residentNoButton);

        CBOMeetingYesButton = view.findViewById(R.id.CBOMeetingYesButton);
        CBOMeetingNoButton = view.findViewById(R.id.CBOMeetingNoButton);

        exclusionYesButton = view.findViewById(R.id.exclusionYesButton);
        exclusionNoButton = view.findViewById(R.id.exclusionNoButton);

        mentalIllnessYesButton = view.findViewById(R.id.mentalIllnessYesButton);
        mentalIllnessNoButton = view.findViewById(R.id.mentalIllnessNoButton);

        substanceAbuseYesButton = view.findViewById(R.id.substanceAbuseYesButton);
        substanceAbuseNoButton = view.findViewById(R.id.substanceAbuseNoButton);

        suicideAttemptYesButton = view.findViewById(R.id.suicideAttemptedYesButton);
        suicideAttemptNoButton = view.findViewById(R.id.suicideAttemptedNoButton);

        participationConsentYesButton = view.findViewById(R.id.participationConsentYesButton);
        participationConsentNoButton = view.findViewById(R.id.participationConsentNoButton);

        screeningRegisterButton = view.findViewById(R.id.screeningRegisterButton);
        editButton = requireActivity().findViewById(R.id.profileEditButton);

        SHGName = view.findViewById(R.id.SHGNamePHQScreening);
        participantName = view.findViewById(R.id.PHQScreeningNameTV);
        PHQScreeningID = view.findViewById(R.id.PHQScreeningID);

        if(phqLocations!=null){
            SHGName.setText(phqLocations.getSHGName());
        }
        participantName.setText(phqParticipantDetails.getPHQParticipantName());
        PHQScreeningID.setText(phqParticipantDetails.getPHQScreeningID());

    }

    private void onClickRegisterButton(){
        screeningRegisterButton.setOnClickListener(v -> {
            int score = 0;

            if(ageAboveEighteen.equalsIgnoreCase("NULL") || resident.equalsIgnoreCase("NULL") || CBOMeeting.equalsIgnoreCase("NULL") || mentalIllness.equalsIgnoreCase("NULL")
                    || substanceAbuse.equalsIgnoreCase("NULL") || suicideAttempt.equalsIgnoreCase("NULL") || participationConsent.equalsIgnoreCase("NULL")){
                Toast.makeText(context, "Please enter all the details.", Toast.LENGTH_LONG).show();
            }

            if(!ageAboveEighteen.equals("NULL") && ageAboveEighteen.equals("Yes")){
                score = score + 1;
            }
            if(!resident.equals("NULL") && resident.equals("Yes")){
                score = score + 1;
            }
            if(!CBOMeeting.equals("NULL") && CBOMeeting.equals("Yes")){
                score = score + 1;
            }
            if(!mentalIllness.equals("NULL") && mentalIllness.equals("No")){
                score = score + 1;
            }
            if(!substanceAbuse.equals("NULL") && substanceAbuse.equals("No")){
                score = score + 1;
            }
            if(!suicideAttempt.equals("NULL") && suicideAttempt.equals("No")){
                score = score + 1;
            }
            if(!participationConsent.equals("NULL") && participationConsent.equals("No")){
                score = score + 1;
            }

            ParticipantScreening participantScreening = new ParticipantScreening();
            participantScreening.setYears(ageAboveEighteen);
            participantScreening.setResident(resident);
            participantScreening.setCbo(CBOMeeting);
            participantScreening.setMental_illness(mentalIllness);
            participantScreening.setSubstance(substanceAbuse);
            participantScreening.setSuicide(suicideAttempt);
            participantScreening.setAgree(participationConsent);
            participantScreening.setScore(score);
            participantScreening.setCreated_user(mithraUtility.getSharedPreferencesData(requireActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));

            callServerPostScreeningDetails(participantScreening);
        });
    }

//    private void moveToRegistrationFragment() {
//        ((ParticipantProfileScreen) requireActivity()).setupSelectedTabFragment(1);
//    }

    private void getUserEnteredData(){
        inclusionYesButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            ageAboveEighteenYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            ageAboveEighteenNoButton.setBackgroundResource(R.drawable.yes_no_button);
            ageAboveEighteen = "Yes";

            residentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            residentNoButton.setBackgroundResource(R.drawable.yes_no_button);
            resident = "Yes";

            CBOMeetingYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            CBOMeetingNoButton.setBackgroundResource(R.drawable.yes_no_button);
            CBOMeeting = "Yes";

        });

        inclusionNoButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.selected_no_button);

            ageAboveEighteenYesButton.setBackgroundResource(R.drawable.yes_no_button);
            ageAboveEighteenNoButton.setBackgroundResource(R.drawable.selected_no_button);
            ageAboveEighteen = "No";

            residentYesButton.setBackgroundResource(R.drawable.yes_no_button);
            residentNoButton.setBackgroundResource(R.drawable.selected_no_button);
            resident = "No";

            CBOMeetingYesButton.setBackgroundResource(R.drawable.yes_no_button);
            CBOMeetingNoButton.setBackgroundResource(R.drawable.selected_no_button);
            CBOMeeting = "No";
        });

        ageAboveEighteenYesButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            ageAboveEighteenYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            ageAboveEighteenNoButton.setBackgroundResource(R.drawable.yes_no_button);
            ageAboveEighteen = "Yes";
        });

        ageAboveEighteenNoButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            ageAboveEighteenYesButton.setBackgroundResource(R.drawable.yes_no_button);
            ageAboveEighteenNoButton.setBackgroundResource(R.drawable.selected_no_button);
            ageAboveEighteen = "No";
        });

        residentYesButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            residentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            residentNoButton.setBackgroundResource(R.drawable.yes_no_button);
            resident = "Yes";
        });

        residentNoButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            residentYesButton.setBackgroundResource(R.drawable.yes_no_button);
            residentNoButton.setBackgroundResource(R.drawable.selected_no_button);
            resident = "No";
        });

        CBOMeetingYesButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            CBOMeetingYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            CBOMeetingNoButton.setBackgroundResource(R.drawable.yes_no_button);
            CBOMeeting = "Yes";
        });

        CBOMeetingNoButton.setOnClickListener(v -> {
            inclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            inclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            CBOMeetingYesButton.setBackgroundResource(R.drawable.yes_no_button);
            CBOMeetingNoButton.setBackgroundResource(R.drawable.selected_no_button);
            CBOMeeting = "No";
        });

        exclusionYesButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            mentalIllnessYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            mentalIllnessNoButton.setBackgroundResource(R.drawable.yes_no_button);
            mentalIllness = "Yes";

            substanceAbuseYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            substanceAbuseNoButton.setBackgroundResource(R.drawable.yes_no_button);
            substanceAbuse = "Yes";

            suicideAttemptYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            suicideAttemptNoButton.setBackgroundResource(R.drawable.yes_no_button);
            suicideAttempt = "Yes";

            participationConsentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            participationConsentNoButton.setBackgroundResource(R.drawable.yes_no_button);
            participationConsent = "Yes";
        });

        exclusionNoButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.selected_no_button);

            mentalIllnessYesButton.setBackgroundResource(R.drawable.yes_no_button);
            mentalIllnessNoButton.setBackgroundResource(R.drawable.selected_no_button);
            mentalIllness = "No";

            substanceAbuseYesButton.setBackgroundResource(R.drawable.yes_no_button);
            substanceAbuseNoButton.setBackgroundResource(R.drawable.selected_no_button);
            substanceAbuse = "No";

            suicideAttemptYesButton.setBackgroundResource(R.drawable.yes_no_button);
            suicideAttemptNoButton.setBackgroundResource(R.drawable.selected_no_button);
            suicideAttempt = "No";

            participationConsentYesButton.setBackgroundResource(R.drawable.yes_no_button);
            participationConsentNoButton.setBackgroundResource(R.drawable.selected_no_button);
            participationConsent = "No";

        });

        mentalIllnessYesButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            mentalIllnessYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            mentalIllnessNoButton.setBackgroundResource(R.drawable.yes_no_button);
            mentalIllness = "Yes";
        });

        mentalIllnessNoButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            mentalIllnessYesButton.setBackgroundResource(R.drawable.yes_no_button);
            mentalIllnessNoButton.setBackgroundResource(R.drawable.selected_no_button);
            mentalIllness = "No";
        });

        substanceAbuseYesButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            substanceAbuseYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            substanceAbuseNoButton.setBackgroundResource(R.drawable.yes_no_button);
            substanceAbuse = "Yes";
        });

        substanceAbuseNoButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            substanceAbuseYesButton.setBackgroundResource(R.drawable.yes_no_button);
            substanceAbuseNoButton.setBackgroundResource(R.drawable.selected_no_button);
            substanceAbuse = "No";
        });

        suicideAttemptYesButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            suicideAttemptYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            suicideAttemptNoButton.setBackgroundResource(R.drawable.yes_no_button);
            suicideAttempt = "Yes";
        });

        suicideAttemptNoButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            suicideAttemptYesButton.setBackgroundResource(R.drawable.yes_no_button);
            suicideAttemptNoButton.setBackgroundResource(R.drawable.selected_no_button);
            suicideAttempt = "No";
        });

        participationConsentYesButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            participationConsentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
            participationConsentNoButton.setBackgroundResource(R.drawable.yes_no_button);
            participationConsent = "Yes";
        });

        participationConsentNoButton.setOnClickListener(v -> {
            exclusionYesButton.setBackgroundResource(R.drawable.yes_no_button);
            exclusionNoButton.setBackgroundResource(R.drawable.yes_no_button);

            participationConsentYesButton.setBackgroundResource(R.drawable.yes_no_button);
            participationConsentNoButton.setBackgroundResource(R.drawable.selected_no_button);
            participationConsent = "No";
        });
    }

    private void moveToPHQParticipantsScreen(){
        Intent intent = new Intent(context, PHQParticipantsScreen.class);
        intent.putExtra("PHQLocations", (Serializable) phqLocations);
        startActivity(intent);
    }

    private void showDialogForParticipantNotEligible(){
        Dialog dialog;
        View customLayout = getLayoutInflater().inflate(R.layout.activity_participant_not_eligible_popup, null);

        TextView notEligibleTV = customLayout.findViewById(R.id.participantNotEligiblePopup);
        Button okButton = customLayout.findViewById(R.id.okButtonPopup);

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

        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(context, ParticipantsScreen.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void callServerPostScreeningDetails(ParticipantScreening participantScreening) {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/screening";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setPhqScreeningServerEvents(this);
        requestObject.postScreeningDetails(context, participantScreening, url);
    }
    private void callUpdatePHQEligibilityStatus(String ScreeningID) {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/phq9_scr_sub/" + phqParticipantDetails.getPHQScreeningID();
        UpdateScreeningStatus updateScreeningStatus = new UpdateScreeningStatus();
        updateScreeningStatus.setScreening_id(ScreeningID);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setPhqScreeningServerEvents(this);
        requestObject.putUpdateScreeningStatus(context, updateScreeningStatus, url);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        inclusionTV.setText(R.string.inclusion);
        inclusionYesButton.setText(R.string.yes);
        inclusionNoButton.setText(R.string.no);

        ageAboveEighteenTV.setText(R.string.age_above_18_years);
        ageAboveEighteenYesButton.setText(R.string.yes);
        ageAboveEighteenNoButton.setText(R.string.no);

        residentTV.setText(R.string.resident);
        residentYesButton.setText(R.string.yes);
        residentNoButton.setText(R.string.no);

        CBOMeetingTV.setText(R.string.CBOMeetingMonths);
        CBOMeetingYesButton.setText(R.string.yes);
        CBOMeetingNoButton.setText(R.string.no);

        exclusionTV.setText(R.string.exclusion);
        exclusionYesButton.setText(R.string.yes);
        exclusionNoButton.setText(R.string.no);

        mentalIllnessTV.setText(R.string.diagnosed_with_mental_illness);
        mentalIllnessYesButton.setText(R.string.yes);
        mentalIllnessNoButton.setText(R.string.no);

        substanceAbuseTV.setText(R.string.substance_abuse);
        substanceAbuseYesButton.setText(R.string.yes);
        substanceAbuseNoButton.setText(R.string.no);

        suicideAttemptTV.setText(R.string.attempted_suicide);
        suicideAttemptYesButton.setText(R.string.yes);
        suicideAttemptNoButton.setText(R.string.no);

        participationConsentTV.setText(R.string.user_consent);
        participationConsentYesButton.setText(R.string.yes);
        participationConsentNoButton.setText(R.string.no);

        screeningRegisterButton.setText(R.string.register);
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
    public void postScreeningDetails(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ParticipantScreening>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ParticipantScreening participantScreening;
        try{
            participantScreening = gson.fromJson(jsonObject.get("data"), type);
            if(participantScreening!=null){
                mithraUtility.putSharedPreferencesData(context, context.getString(R.string.userScreeningName), context.getString(R.string.userScreeningID), participantScreening.getName());
                callUpdatePHQEligibilityStatus(participantScreening.getName());
            }
        }catch(Exception e){
            Toast.makeText(context, jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateScreeningStatus(String message) {
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        try{
            moveToPHQParticipantsScreen();
        }catch(Exception e){
            Toast.makeText(context, jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
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