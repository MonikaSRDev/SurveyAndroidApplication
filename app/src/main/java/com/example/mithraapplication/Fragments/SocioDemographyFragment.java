package com.example.mithraapplication.Fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;

import java.util.Locale;

public class SocioDemographyFragment extends Fragment implements HandleServerResponse {

    private Button nextButton, nuclearFamilyButton, jointFamilyButton,
            neverMarriedButton, marriedButton, divorcedButton, separatedButton, widowButton,
            scheduledCasteButton, scheduledTribeButton, backwardClassesButton, doNotKnowCasteButton, otherCasteButton, pNACasteButton,
            hinduButton, muslimButton, christianButton, sikhButton, buddhistButton, jainButton, otherReligionButton, pNAReligionButton,
            professionalButton, clericalButton, salesAndServicesButton, skilledManualButton, unskilledManualButton, agricultureButton,
            noSchoolingButton, lessThanFiveButton, fiveToSevenButton, eightToNineButton, tenToElevenButton, twelveOrMoreButton;
    private TextView educationYearsTV, maritalStatusTV, occupationTV, religionTV, familyIncomeTV, SHGMeetingsTV, nearestPHCTV,
            casteTV, familyAdultMembersTV, familyChildMembersTV, earningMembersTV, familyTypeTV, associationDurationTV;
    private EditText numAdultFamilyET, numChildFamilyET, totalFamilyIncomeET, totalEarningFamilyMembersET, associationDurationET, SHGMeetingsET, nearestPHCET;
    private String  numAdultFamily = "NULL", numChildFamily = "NULL", totalFamilyIncome = "NULL", totalEarningFamilyMembers = "NULL", associationDuration = "NULL",
            participantMaritalStatus = "NULL", participantFamilyType = "NULL", participantCaste = "NULL", participantReligion = "NULL",
            participantOccupation = "NULL", participantSchooling = "NULL", nearestPHC = "NULL", SHGMeetings = "NULL";
    private MithraUtility mithraUtility = new MithraUtility();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_socio_demography_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        getYearsOfEducation();
        getOccupation();
        getReligion();
        getCaste();
        getSelectedMaritalStatus();
        getSelectedFamilyType();
        onClickOfNextButton();
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews(View view){
        educationYearsTV = view.findViewById(R.id.educationYearsTV);
        maritalStatusTV = view.findViewById(R.id.maritalStatusTV);
        occupationTV = view.findViewById(R.id.occupationTV);
        religionTV = view.findViewById(R.id.religionTV);
        familyIncomeTV = view.findViewById(R.id.familyIncomeTV);
        casteTV = view.findViewById(R.id.casteTV);
        earningMembersTV = view.findViewById(R.id.earningMemberTV);
        familyAdultMembersTV = view.findViewById(R.id.familyAdultMembersTV);
        familyChildMembersTV = view.findViewById(R.id.familyChildMembersTV);
        familyTypeTV = view.findViewById(R.id.familyTypeTV);
        associationDurationTV = view.findViewById(R.id.associationDurationTV);
        SHGMeetingsTV = view.findViewById(R.id.SHGMeetingsTV);
        nearestPHCTV = view.findViewById(R.id.nearestPHCTV);

        nextButton =  view.findViewById(R.id.socioNextButton);
        nuclearFamilyButton = view.findViewById(R.id.nuclearFamilyButton);
        jointFamilyButton = view.findViewById(R.id.jointFamilyButton);

        neverMarriedButton = view.findViewById(R.id.neverMarriedButton);
        marriedButton = view.findViewById(R.id.marriedButton);
        divorcedButton  = view.findViewById(R.id.divorcedButton);
        separatedButton = view.findViewById(R.id.separatedButton);
        widowButton = view.findViewById(R.id.widowButton);

        scheduledCasteButton = view.findViewById(R.id.scheduledCasteButton);
        scheduledTribeButton = view.findViewById(R.id.scheduledTribeButton);
        backwardClassesButton = view.findViewById(R.id.otherBackwardClassButton);
        doNotKnowCasteButton = view.findViewById(R.id.dontKnowButton);
        otherCasteButton = view.findViewById(R.id.otherCasteButton);
        pNACasteButton = view.findViewById(R.id.pnACasteButton);

        hinduButton = view.findViewById(R.id.hinduButton);
        muslimButton = view.findViewById(R.id.muslimButton);
        christianButton = view.findViewById(R.id.christianButton);
        sikhButton = view.findViewById(R.id.sikhButton);
        buddhistButton = view.findViewById(R.id.buddhistButton);
        jainButton = view.findViewById(R.id.jainButton);
        otherReligionButton = view.findViewById(R.id.otherButton);
        pNAReligionButton = view.findViewById(R.id.pnAReligionButton);

        professionalButton = view.findViewById(R.id.professionalButton);
        clericalButton = view.findViewById(R.id.clericalButton);
        salesAndServicesButton = view.findViewById(R.id.salesAndServicesButton);
        skilledManualButton = view.findViewById(R.id.skilledButton);
        unskilledManualButton = view.findViewById(R.id.unskilledButton);
        agricultureButton = view.findViewById(R.id.agricultureButton);

        noSchoolingButton = view.findViewById(R.id.noSchoolingButton);
        lessThanFiveButton = view.findViewById(R.id.less_than_five_button);
        fiveToSevenButton = view.findViewById(R.id.five_seven_Button);
        eightToNineButton = view.findViewById(R.id.eight_nine_Button);
        tenToElevenButton = view.findViewById(R.id.ten_eleven_button);
        twelveOrMoreButton = view.findViewById(R.id.twelve_more_Button);

        totalFamilyIncomeET = view.findViewById(R.id.familyIncomeET);
        totalEarningFamilyMembersET = view.findViewById(R.id.earningMemberET);
        numAdultFamilyET = view.findViewById(R.id.familyAdultMembersET);
        numChildFamilyET = view.findViewById(R.id.familyChildMembersET);
        associationDurationET = view.findViewById(R.id.associationDurationET);
        SHGMeetingsET = view.findViewById(R.id.SHGMeetingsET);
        nearestPHCET = view.findViewById(R.id.nearestPHCET);
    }

    public void getYearsOfEducation(){

        noSchoolingButton.setOnClickListener(v -> {
            participantSchooling = noSchoolingButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        lessThanFiveButton.setOnClickListener(v -> {
            participantSchooling = lessThanFiveButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        fiveToSevenButton.setOnClickListener(v -> {
            participantSchooling = fiveToSevenButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        eightToNineButton.setOnClickListener(v -> {
            participantSchooling = eightToNineButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        tenToElevenButton.setOnClickListener(v -> {
            participantSchooling = tenToElevenButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        twelveOrMoreButton.setOnClickListener(v -> {
            participantSchooling = twelveOrMoreButton.getText().toString();
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    public void getOccupation(){
        professionalButton.setOnClickListener(v -> {
            participantOccupation = professionalButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        clericalButton.setOnClickListener(v -> {
            participantOccupation = clericalButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        salesAndServicesButton.setOnClickListener(v -> {
            participantOccupation = salesAndServicesButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        skilledManualButton.setOnClickListener(v -> {
            participantOccupation = skilledManualButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        unskilledManualButton.setOnClickListener(v -> {
            participantOccupation = unskilledManualButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        agricultureButton.setOnClickListener(v -> {
            participantOccupation = agricultureButton.getText().toString();
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    public void getReligion(){
        hinduButton.setOnClickListener(v -> {
            participantReligion = hinduButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        muslimButton.setOnClickListener(v -> {
            participantReligion = muslimButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        christianButton.setOnClickListener(v -> {
            participantReligion = christianButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        sikhButton.setOnClickListener(v -> {
            participantReligion = sikhButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        buddhistButton.setOnClickListener(v -> {
            participantReligion = buddhistButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        jainButton.setOnClickListener(v -> {
            participantReligion = jainButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        otherReligionButton.setOnClickListener(v -> {
            participantReligion = otherReligionButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        pNAReligionButton.setOnClickListener(v -> {
            participantReligion = pNAReligionButton.getText().toString();
            hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    public void getCaste(){
        scheduledCasteButton.setOnClickListener(v -> {
            participantCaste = scheduledCasteButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        scheduledTribeButton.setOnClickListener(v -> {
            participantCaste = scheduledTribeButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        backwardClassesButton.setOnClickListener(v -> {
            participantCaste = backwardClassesButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        doNotKnowCasteButton.setOnClickListener(v -> {
            participantCaste = doNotKnowCasteButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        otherCasteButton.setOnClickListener(v -> {
            participantCaste = otherCasteButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        pNACasteButton.setOnClickListener(v -> {
            participantCaste = pNACasteButton.getText().toString();
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

//    /**
//     * Description : This method is used to change the language of the screen based on the button clicked
//     */
//    private void onClickOfLanguageButton(){
//        englishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                englishButton.setBackgroundResource(R.drawable.left_toggle_selected_button);
//                englishButton.setTextColor(getResources().getColor(R.color.black));
//                kannadaButton.setBackgroundResource(R.drawable.right_toggle_button);
//                kannadaButton.setTextColor(getResources().getColor(R.color.black));
//                changeLocalLanguage("en");
//            }
//        });
//
//        kannadaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                kannadaButton.setBackgroundResource(R.drawable.right_toggle_selected_button);
//                kannadaButton.setTextColor(getResources().getColor(R.color.black));
//                englishButton.setBackgroundResource(R.drawable.left_toggle_button);
//                englishButton.setTextColor(getResources().getColor(R.color.black));
//                changeLocalLanguage("kn");
//            }
//        });
//    }

    /**
     * @param selectedLanguage
     * Description : This method is used to change the content of the screen to user selected language
     */
    private void changeLocalLanguage(String selectedLanguage){
        Locale myLocale = new Locale(selectedLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        educationYearsTV.setText(R.string.years_of_education);
        maritalStatusTV.setText(R.string.marital_status);
        occupationTV.setText(R.string.occupation);
        religionTV.setText(R.string.religion);
        familyIncomeTV.setText(R.string.total_family_income_per_month);
        casteTV.setText(R.string.caste);
        familyAdultMembersTV.setText(R.string.number_of_family_members);
        earningMembersTV.setText(R.string.earning_members);
        familyTypeTV.setText(R.string.type_of_family);
        associationDurationTV.setText(R.string.association_duration);

        nextButton.setText(R.string.next);
        neverMarriedButton.setText(R.string.unmarried);
        marriedButton.setText(R.string.married);
        nuclearFamilyButton.setText(R.string.nuclear);
        jointFamilyButton.setText(R.string.joint);
    }


    /**
     * Description : This method is used to get the selected marital status
     */
    private void getSelectedMaritalStatus(){
        neverMarriedButton.setOnClickListener(v -> {
            participantMaritalStatus = neverMarriedButton.getText().toString();
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        marriedButton.setOnClickListener(v -> {
            participantMaritalStatus = marriedButton.getText().toString();
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        divorcedButton.setOnClickListener(v -> {
            participantMaritalStatus = divorcedButton.getText().toString();
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);

        });

        separatedButton.setOnClickListener(v -> {
            participantMaritalStatus = separatedButton.getText().toString();
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        widowButton.setOnClickListener(v -> {
            participantMaritalStatus = widowButton.getText().toString();
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    /**
     * Description : This method is used to get the selected family type
     */
    private void getSelectedFamilyType(){
        nuclearFamilyButton.setOnClickListener(v -> {
            participantFamilyType = nuclearFamilyButton.getText().toString();
            nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        jointFamilyButton.setOnClickListener(v -> {
            participantFamilyType = jointFamilyButton.getText().toString();
            nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private SocioDemography getUserEnteredData(){
        numAdultFamily = !numAdultFamilyET.getText().toString().isEmpty() ? numAdultFamilyET.getText().toString() : "NULL";
        totalFamilyIncome = !totalFamilyIncomeET.getText().toString().isEmpty() ? totalFamilyIncomeET.getText().toString(): "NULL";
        totalEarningFamilyMembers = !totalEarningFamilyMembersET.getText().toString().isEmpty() ? totalEarningFamilyMembersET.getText().toString(): "NULL";
        numChildFamily = !numChildFamilyET.getText().toString().isEmpty() ? numChildFamilyET.getText().toString(): "NULL";
        associationDuration = !associationDurationET.getText().toString().isEmpty() ? associationDurationET.getText().toString(): "NULL";
        SHGMeetings = !SHGMeetingsET.getText().toString().isEmpty() ? SHGMeetingsET.getText().toString(): "NULL";
        nearestPHC = !nearestPHCET.getText().toString().isEmpty() ? nearestPHCET.getText().toString(): "NULL";

        SocioDemography socioDemographyObject = new SocioDemography();
        socioDemographyObject.setUserName(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.user_name), getString(R.string.user_name_participant)));
        socioDemographyObject.setYearsOfEducation(participantSchooling);
        socioDemographyObject.setMaritalStatus(participantMaritalStatus);
        socioDemographyObject.setNumFamilyAdultMembers(numAdultFamily);
        socioDemographyObject.setNumFamilyChildMembers(numChildFamily);
        socioDemographyObject.setFamilyIncome(totalFamilyIncome);
        socioDemographyObject.setNumEarningFamMembers(totalEarningFamilyMembers);
        socioDemographyObject.setReligion(participantReligion);
        socioDemographyObject.setCaste(participantCaste);
        socioDemographyObject.setOccupation(participantOccupation);
        socioDemographyObject.setTypeOfFamily(participantFamilyType);
        socioDemographyObject.setAssociationDuration(associationDuration);
        socioDemographyObject.setSHGMeetings(SHGMeetings);
        socioDemographyObject.setNearestPHC(nearestPHC);

        return socioDemographyObject;
    }

    /**
     * Description : This method is called when user clicks on the next button
     */
    private void onClickOfNextButton(){
        nextButton.setOnClickListener(v -> {
//                callServerPostSocioDemography();
            moveToDiseaseProfileTab();
        });
    }

    private void callServerPostSocioDemography(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/demography";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postSocioDemographyDetails(getActivity(), getUserEnteredData(), url);
    }

    private void moveToDiseaseProfileTab(){
        ((ParticipantProfileScreen)getActivity()).setupSelectedTabFragment(3);
    }


    @Override
    public void responseReceivedSuccessfully(String message) {
        moveToDiseaseProfileTab();
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
