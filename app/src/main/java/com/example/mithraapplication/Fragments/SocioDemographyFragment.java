package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.Fragments.RegistrationFragment.trackingName;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateRegisterParticipant;
import com.example.mithraapplication.ModelClasses.UpdateSocioDemographyTracking;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private ArrayList<TrackingParticipantStatus> trackingParticipantStatus = null;
    private String isEditable = "true";
    private SocioDemography socioDemographyDetails = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_socio_demography_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isEditable!= null && !isEditable.equals("true")){
            callGetIndividualSocioDemographyDetails();
        }
        RegisterViews(view);
        getYearsOfEducation();
        getOccupation();
        getReligion();
        getCaste();
        getSelectedMaritalStatus();
        getSelectedFamilyType();
        onClickOfNextButton();
    }

    public SocioDemographyFragment(ArrayList<TrackingParticipantStatus> trackingParticipantStatus, String isEditable){
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
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

    private void setEditable(){
        if(isEditable!=null && isEditable.equals("true")){
            setEditableViews(true);
        }else {
            setEditableViews(isEditable == null || !isEditable.equals("false"));
            totalFamilyIncomeET.setText(socioDemographyDetails.getFamilyIncome());
            totalEarningFamilyMembersET.setText(socioDemographyDetails.getNumEarningFamMembers());
            numAdultFamilyET.setText(socioDemographyDetails.getNumFamilyAdultMembers());
            numChildFamilyET.setText(socioDemographyDetails.getNumFamilyChildMembers());
            associationDurationET.setText(socioDemographyDetails.getAssociationDuration());
            SHGMeetingsET.setText(socioDemographyDetails.getCBOMeetings());
            nearestPHCET.setText(socioDemographyDetails.getNearestPHC());

            if(isEditable!=null && !isEditable.equals("true") && socioDemographyDetails!=null ){
                participantSchooling = socioDemographyDetails.getYearsOfEducation();
                participantOccupation = socioDemographyDetails.getOccupation();
                participantReligion = socioDemographyDetails.getReligion();
                participantCaste = socioDemographyDetails.getCaste();
                participantMaritalStatus = socioDemographyDetails.getMaritalStatus();
                participantFamilyType = socioDemographyDetails.getTypeOfFamily();
            }

            if(participantSchooling!=null && participantSchooling.equals(noSchoolingButton.getText().toString())){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equals(lessThanFiveButton.getText().toString())){
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equals(fiveToSevenButton.getText().toString())){
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equals(eightToNineButton.getText().toString())){
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equals(tenToElevenButton.getText().toString())){
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equals(twelveOrMoreButton.getText().toString())){
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantOccupation!=null && participantOccupation.equals(professionalButton.getText().toString())){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equals(clericalButton.getText().toString())){
                clericalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equals(salesAndServicesButton.getText().toString())){
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equals(skilledManualButton.getText().toString())){
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equals(unskilledManualButton.getText().toString())){
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equals(agricultureButton.getText().toString())){
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantReligion!=null && participantReligion.equals(hinduButton.getText().toString())){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(muslimButton.getText().toString())){
                muslimButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(christianButton.getText().toString())){
                christianButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(sikhButton.getText().toString())){
                sikhButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(buddhistButton.getText().toString())){
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(jainButton.getText().toString())){
                jainButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(otherReligionButton.getText().toString())){
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantReligion!=null && participantReligion.equals(pNAReligionButton.getText().toString())){
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantCaste!=null && participantCaste.equals(scheduledCasteButton.getText().toString())){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantCaste!=null && participantCaste.equals(scheduledTribeButton.getText().toString())){
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantCaste!=null && participantCaste.equals(backwardClassesButton.getText().toString())){
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantCaste!=null && participantCaste.equals(doNotKnowCasteButton.getText().toString())){
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantCaste!=null && participantCaste.equals(otherCasteButton.getText().toString())){
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantCaste!=null && participantCaste.equals(pNACasteButton.getText().toString())){
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantMaritalStatus!=null && participantMaritalStatus.equals(neverMarriedButton.getText().toString())){
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equals(marriedButton.getText().toString())){
                marriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equals(divorcedButton.getText().toString())){
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equals(separatedButton.getText().toString())){
                separatedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equals(widowButton.getText().toString())) {
                widowButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantFamilyType != null && participantFamilyType.equals(nuclearFamilyButton.getText().toString())){
                nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }else if(participantFamilyType != null && participantFamilyType.equals(jointFamilyButton.getText().toString())){
                jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }
        }
    }

    private void setEditableViews(boolean editable){
        totalFamilyIncomeET.setFocusable(editable);
        totalEarningFamilyMembersET.setFocusable(editable);
        numAdultFamilyET.setFocusable(editable);
        numChildFamilyET.setFocusable(editable);
        associationDurationET.setFocusable(editable);
        SHGMeetingsET.setFocusable(editable);
        nearestPHCET.setFocusable(editable);

        totalFamilyIncomeET.setClickable(editable);
        totalEarningFamilyMembersET.setClickable(editable);
        numAdultFamilyET.setClickable(editable);
        numChildFamilyET.setClickable(editable);
        associationDurationET.setClickable(editable);
        SHGMeetingsET.setClickable(editable);
        nearestPHCET.setClickable(editable);

        nextButton.setEnabled(editable);
        nuclearFamilyButton.setEnabled(editable);
        jointFamilyButton.setEnabled(editable);
        neverMarriedButton.setEnabled(editable);
        marriedButton.setEnabled(editable);
        divorcedButton.setEnabled(editable);
        separatedButton.setEnabled(editable);
        widowButton.setEnabled(editable);
        scheduledCasteButton.setEnabled(editable);
        scheduledTribeButton.setEnabled(editable);
        backwardClassesButton.setEnabled(editable);
        doNotKnowCasteButton.setEnabled(editable);
        otherCasteButton.setEnabled(editable);
        pNACasteButton.setEnabled(editable);
        hinduButton.setEnabled(editable);
        muslimButton.setEnabled(editable);
        christianButton.setEnabled(editable);
        sikhButton.setEnabled(editable);
        buddhistButton.setEnabled(editable);
        jainButton.setEnabled(editable);
        otherReligionButton.setEnabled(editable);
        pNAReligionButton.setEnabled(editable);
        professionalButton.setEnabled(editable);
        clericalButton.setEnabled(editable);
        salesAndServicesButton.setEnabled(editable);
        skilledManualButton.setEnabled(editable);
        unskilledManualButton.setEnabled(editable);
        agricultureButton.setEnabled(editable);
        noSchoolingButton.setEnabled(editable);
        lessThanFiveButton.setEnabled(editable);
        fiveToSevenButton.setEnabled(editable);
        eightToNineButton.setEnabled(editable);
        tenToElevenButton.setEnabled(editable);
        twelveOrMoreButton.setEnabled(editable);
    }

    private void getYearsOfEducation(){
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

    private void getOccupation(){
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

    private void getReligion(){
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

    private void getCaste(){
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
        socioDemographyObject.setUser_pri_id(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
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
        socioDemographyObject.setCBOMeetings(SHGMeetings);
        socioDemographyObject.setNearestPHC(nearestPHC);
        socioDemographyObject.setActive("yes");
        socioDemographyObject.setCreated_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));

        return socioDemographyObject;
    }

    /**
     * Description : This method is called when user clicks on the next button
     */
    private void onClickOfNextButton(){
        nextButton.setOnClickListener(v -> {
            if(isEditable!=null && isEditable.equals("true")){
                callServerPostSocioDemography();
//            moveToDiseaseProfileTab();
            }else{
                callServerUpdateSocioDemographyDetails();
            }
        });
    }

    private void callServerPostSocioDemography(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/demography";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postSocioDemographyDetails(getActivity(), getUserEnteredData(), url);
    }

    private void callUpdateTrackingDetails(String socioDemographyName){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking/" + trackingName;
        UpdateSocioDemographyTracking trackingParticipantStatus = new UpdateSocioDemographyTracking();
        trackingParticipantStatus.setSocio_demography(socioDemographyName);
        trackingParticipantStatus.setModified_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putTrackingStatusSocioDemography(getActivity(), trackingParticipantStatus, url);
    }

    private void callGetIndividualSocioDemographyDetails() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/demography/" + trackingParticipantStatus.get(0).getSocio_demography();
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantSocioDemographyDetails(getActivity(), url);
    }

    private void callServerUpdateSocioDemographyDetails() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/demography/" +  trackingParticipantStatus.get(0).getSocio_demography();
        SocioDemography socioDemographyObject = getUserEnteredData();
        socioDemographyObject.setUser_pri_id(trackingParticipantStatus.get(0).getUser_pri_id());
        socioDemographyObject.setModified_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putSocioDemographyDetails(getActivity(), socioDemographyObject, url);
    }

    private void moveToDiseaseProfileTab(){
        ((ParticipantProfileScreen)getActivity()).setupSelectedTabFragment(3);
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
                if (frappeResponse != null && frappeResponse.getDoctype().equals("demography")) {
                    String registrationName = frappeResponse.getName();
                    callUpdateTrackingDetails(registrationName);
                } else if (frappeResponse != null && frappeResponse.getDoctype().equals("tracking")) {
                    trackingName = frappeResponse.getName();
                    moveToDiseaseProfileTab();
                }
            }else{
                Type typeSocioDemography = new TypeToken<SocioDemography>(){}.getType();
                socioDemographyDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeSocioDemography);
                trackingParticipantStatus.get(0).setUser_pri_id(socioDemographyDetails.getUser_pri_id());
                setEditable();
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
            }
        }else{
            //Do nothing
            Log.i("SocioDemographyFragment", "JsonObjectRegistration data is Empty");
        }
    }

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
        familyChildMembersTV.setText(R.string.child_members);
        SHGMeetingsTV.setText(R.string.SHGMeetings6Months);
        nearestPHCTV.setText(R.string.nearest_phc);

        nextButton.setText(R.string.next);
        nuclearFamilyButton.setText(R.string.nuclear_family);
        jointFamilyButton.setText(R.string.joint_family);
        neverMarriedButton.setText(R.string.never_married);
        marriedButton.setText(R.string.married);
        divorcedButton.setText(R.string.divorced);
        separatedButton.setText(R.string.separated);
        widowButton.setText(R.string.widow);
        scheduledCasteButton.setText(R.string.scheduled_caste);
        scheduledTribeButton.setText(R.string.scheduled_tribe);
        backwardClassesButton.setText(R.string.other_backward_class);
        doNotKnowCasteButton.setText(R.string.do_not_know);
        otherCasteButton.setText(R.string.other);
        pNACasteButton.setText(R.string.preferred_not_to_answer);
        hinduButton.setText(R.string.hindu);
        muslimButton.setText(R.string.muslim);
        christianButton.setText(R.string.christian);
        sikhButton.setText(R.string.sikh);
        buddhistButton.setText(R.string.buddhist_neo_buddhist);
        jainButton.setText(R.string.jain);
        otherReligionButton.setText(R.string.other);
        pNAReligionButton.setText(R.string.preferred_not_to_answer);
        professionalButton.setText(R.string.professional);
        clericalButton.setText(R.string.clerical);
        salesAndServicesButton.setText(R.string.sales_and_services);
        skilledManualButton.setText(R.string.skilled_manual);
        unskilledManualButton.setText(R.string.unskilled_manual);
        agricultureButton.setText(R.string.agriculture);
        noSchoolingButton.setText(R.string.no_schooling);
        lessThanFiveButton.setText(R.string.less_than_five);
        fiveToSevenButton.setText(R.string.five_to_seven);
        eightToNineButton.setText(R.string.eight_to_nine);
        tenToElevenButton.setText(R.string.ten_to_eleven);
        twelveOrMoreButton.setText(R.string.twelve_or_more);
    }
}
