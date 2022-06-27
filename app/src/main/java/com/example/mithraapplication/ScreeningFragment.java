package com.example.mithraapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.ModelClasses.ParticipantScreening;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScreeningFragment extends Fragment implements HandleServerResponse{

    public ScreeningFragment() {
        // Required empty public constructor
    }

    private Button inclusionYesButton, inclusionNoButton, ageAboveEighteenYesButton, ageAboveEighteenNoButton,
            residentYesButton, residentNoButton, CBOMeetingYesButton, CBOMeetingNoButton,
            exclusionYesButton, exclusionNoButton, mentalIllnessYesButton, mentalIllnessNoButton,
            substanceAbuseYesButton, substanceAbuseNoButton, suicideAttemptYesButton, suicideAttemptNoButton,
            participationConsentYesButton, participationConsentNoButton, screeningRegisterButton;

    private TextView inclusionTV, ageAboveEighteenTV, residentTV, CBOMeetingTV, exclusionTV, mentalIllnessTV,
            substanceAbuseTV, suicideAttemptTV, participationConsentTV;

    private String ageAboveEighteen = "NULL", resident = "NULL", CBOMeeting = "NULL", mentalIllness = "NULL", substanceAbuse = "NULL", suicideAttempt = "NULL", participationConsent = "NULL";
    private MithraUtility mithraUtility = new MithraUtility();

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

    }

    private void onClickRegisterButton(){

        screeningRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParticipantScreening participantScreening = new ParticipantScreening();
                participantScreening.setYears(ageAboveEighteen);
                participantScreening.setResident(resident);
                participantScreening.setCbo(CBOMeeting);
                participantScreening.setMental_illness(mentalIllness);
                participantScreening.setSubstance(substanceAbuse);
                participantScreening.setSuicide(suicideAttempt);
                participantScreening.setAgree(participationConsent);

                int score = 0;

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

                participantScreening.setScore(score);

                Log.i("SCREENING SCORE", "Score : "+score);

                callServerPostScreeningDetails(participantScreening);
            }
        });
    }

    private void callServerPostScreeningDetails(ParticipantScreening participantScreening) {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/screening";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postScreeningDetails(getActivity(), participantScreening, url);
    }

    private void moveToRegistrationFragment() {
        ((ProfileScreen) requireActivity()).setupSelectedTabFragment(1);
    }

    private void getUserEnteredData(){
        inclusionYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        inclusionNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        ageAboveEighteenYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageAboveEighteenYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                ageAboveEighteenNoButton.setBackgroundResource(R.drawable.yes_no_button);
                ageAboveEighteen = "Yes";
            }
        });

        ageAboveEighteenNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageAboveEighteenYesButton.setBackgroundResource(R.drawable.yes_no_button);
                ageAboveEighteenNoButton.setBackgroundResource(R.drawable.selected_no_button);
                ageAboveEighteen = "No";
            }
        });

        residentYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                residentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                residentNoButton.setBackgroundResource(R.drawable.yes_no_button);
                resident = "Yes";
            }
        });

        residentNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                residentYesButton.setBackgroundResource(R.drawable.yes_no_button);
                residentNoButton.setBackgroundResource(R.drawable.selected_no_button);
                resident = "No";
            }
        });

        CBOMeetingYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CBOMeetingYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                CBOMeetingNoButton.setBackgroundResource(R.drawable.yes_no_button);
                CBOMeeting = "Yes";
            }
        });

        CBOMeetingNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CBOMeetingYesButton.setBackgroundResource(R.drawable.yes_no_button);
                CBOMeetingNoButton.setBackgroundResource(R.drawable.selected_no_button);
                CBOMeeting = "No";
            }
        });

        exclusionYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        exclusionNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        mentalIllnessYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentalIllnessYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                mentalIllnessNoButton.setBackgroundResource(R.drawable.yes_no_button);
                mentalIllness = "Yes";
            }
        });

        mentalIllnessNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentalIllnessYesButton.setBackgroundResource(R.drawable.yes_no_button);
                mentalIllnessNoButton.setBackgroundResource(R.drawable.selected_no_button);
                mentalIllness = "No";
            }
        });

        substanceAbuseYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substanceAbuseYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                substanceAbuseNoButton.setBackgroundResource(R.drawable.yes_no_button);
                substanceAbuse = "Yes";
            }
        });

        substanceAbuseNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substanceAbuseYesButton.setBackgroundResource(R.drawable.yes_no_button);
                substanceAbuseNoButton.setBackgroundResource(R.drawable.selected_no_button);
                substanceAbuse = "No";
            }
        });

        suicideAttemptYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suicideAttemptYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                suicideAttemptNoButton.setBackgroundResource(R.drawable.yes_no_button);
                suicideAttempt = "Yes";
            }
        });

        suicideAttemptNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suicideAttemptYesButton.setBackgroundResource(R.drawable.yes_no_button);
                suicideAttemptNoButton.setBackgroundResource(R.drawable.selected_no_button);
                suicideAttempt = "No";
            }
        });

        participationConsentYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participationConsentYesButton.setBackgroundResource(R.drawable.selected_yes_button);
                participationConsentNoButton.setBackgroundResource(R.drawable.yes_no_button);
                participationConsent = "Yes";
            }
        });

        participationConsentNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participationConsentYesButton.setBackgroundResource(R.drawable.yes_no_button);
                participationConsentNoButton.setBackgroundResource(R.drawable.selected_no_button);
                participationConsent = "No";
            }
        });
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ParticipantScreening>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ParticipantScreening participantScreening;
        try{
            participantScreening = gson.fromJson(jsonObject.get("data"), type);
            if(participantScreening!=null){
                if(!participantScreening.getName().equals("NULL")){
                    mithraUtility.putSharedPreferencesData(getActivity(), "ScreeningName", "screeningName", participantScreening.getName());
                    moveToRegistrationFragment();
                }
            }
        }catch(Exception e){
            if(jsonObject.get("data")!=null) {
                Toast.makeText(getActivity(), jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}