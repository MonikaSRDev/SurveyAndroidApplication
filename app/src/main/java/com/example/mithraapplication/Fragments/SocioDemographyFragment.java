package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.Fragments.RegistrationFragment.trackingName;
import static com.example.mithraapplication.ParticipantProfileScreen.participant_primary_ID;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.SocioDemographyServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateSocioDemographyTracking;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SocioDemographyFragment extends Fragment implements HandleServerResponse, SocioDemographyServerEvents {

    private Button nextButton, nuclearFamilyButton, jointFamilyButton,
            neverMarriedButton, marriedButton, divorcedButton, separatedButton, widowButton,
            scheduledCasteButton, scheduledTribeButton, backwardClassesButton, doNotKnowCasteButton, otherCasteButton, pNACasteButton,
            hinduButton, muslimButton, christianButton, sikhButton, buddhistButton, jainButton, otherReligionButton, pNAReligionButton,
            professionalButton, clericalButton, salesAndServicesButton, skilledManualButton, unskilledManualButton, agricultureButton,
            noSchoolingButton, lessThanFiveButton, fiveToSevenButton, eightToNineButton, tenToElevenButton, twelveOrMoreButton, editButton;
    private TextView educationYearsTV, maritalStatusTV, occupationTV, religionTV, familyIncomeTV, SHGMeetingsTV, nearestPHCTV,
            casteTV, familyAdultMembersTV, familyChildMembersTV, earningMembersTV, familyTypeTV, associationDurationTV;
    private EditText numAdultFamilyET, numChildFamilyET, totalFamilyIncomeET, totalEarningFamilyMembersET, associationDurationET, SHGMeetingsET, nearestPHCET;
    private String  numAdultFamily = "NULL", numChildFamily = "NULL", totalFamilyIncome = "NULL", totalEarningFamilyMembers = "NULL", associationDuration = "NULL",
            participantMaritalStatus = "NULL", participantFamilyType = "NULL", participantCaste = "NULL", participantReligion = "NULL",
            participantOccupation = "NULL", participantSchooling = "NULL", nearestPHC = "NULL", SHGMeetings = "NULL";
    private MithraUtility mithraUtility = new MithraUtility();
    private TrackingParticipantStatus trackingParticipantStatus = null;
    private String isEditable = "true";
    private SocioDemography socioDemographyDetails = null;
    private Context context;
    private PHQLocations phqLocations;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_socio_demography_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getSocio_demography()!=null) {
            if(isEditable!= null && !isEditable.equals("true")){
                editButton.setEnabled(true);
                editButton.setVisibility(View.VISIBLE);
                callGetIndividualSocioDemographyDetails();
            }
        }else{
            isEditable = "true";
            editButton.setEnabled(false);
            editButton.setVisibility(View.GONE);
        }
        if(isEditable!= null && isEditable.equals("false")){
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.INVISIBLE);
            nextButton.setText(R.string.next);
            nextButton.setBackgroundResource(R.drawable.inputs_background);
            nextButton.setTextColor(getResources().getColor(R.color.text_color));
        }
        getYearsOfEducation();
        getOccupation();
        getReligion();
        getCaste();
        getSelectedMaritalStatus();
        getSelectedFamilyType();
        onClickOfNextButton();
        setOnclickOfEditButton();
    }

    public SocioDemographyFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable, PHQLocations phqLocations){
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.phqLocations = phqLocations;
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
        nextButton.setEnabled(true);
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
        editButton = requireActivity().findViewById(R.id.profileEditButton);
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

            if(participantSchooling!=null && participantSchooling.equalsIgnoreCase("No Schooling")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.contains("<5")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.contains("5-7")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.contains("8-9")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.contains("10-11")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantSchooling!=null && participantSchooling.equalsIgnoreCase("12ormore")){
                noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Professional")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Clerical")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Sales and Services")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Skilled Manual")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Unskilled Manual")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantOccupation!=null && participantOccupation.equalsIgnoreCase("Agriculture")){
                professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                agricultureButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantReligion!=null && participantReligion.equalsIgnoreCase("Hindu")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Muslim")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Christian")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Sikh")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Buddhist/Neo buddhist")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Jain")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Other")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantReligion!=null && participantReligion.equalsIgnoreCase("Preferred Not to Answer")){
                hinduButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                muslimButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                christianButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                sikhButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                buddhistButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                jainButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherReligionButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNAReligionButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantCaste!=null && participantCaste.equalsIgnoreCase("Scheduled Caste")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantCaste!=null && participantCaste.equalsIgnoreCase("Scheduled Tribe")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantCaste!=null && participantCaste.equalsIgnoreCase("Other Backward Class")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantCaste!=null && participantCaste.equalsIgnoreCase("Don't Know")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantCaste!=null && participantCaste.equalsIgnoreCase("Other")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantCaste!=null && participantCaste.equalsIgnoreCase("Preferred Not to Answer")){
                scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                pNACasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantMaritalStatus!=null && participantMaritalStatus.equalsIgnoreCase("Never Married")){
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equalsIgnoreCase("Married")){
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                marriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equalsIgnoreCase("Divorced")){
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equalsIgnoreCase("Separated")){
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                separatedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantMaritalStatus!=null && participantMaritalStatus.equalsIgnoreCase("Widow")) {
                neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
                widowButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            }

            if(participantFamilyType != null && participantFamilyType.equalsIgnoreCase("Nuclear Family")){
                nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
                jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            }else if(participantFamilyType != null && participantFamilyType.equalsIgnoreCase("Joint Family")){
                nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
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

        totalFamilyIncomeET.setFocusableInTouchMode(editable);
        totalEarningFamilyMembersET.setFocusableInTouchMode(editable);
        numAdultFamilyET.setFocusableInTouchMode(editable);
        numChildFamilyET.setFocusableInTouchMode(editable);
        associationDurationET.setFocusableInTouchMode(editable);
        SHGMeetingsET.setFocusableInTouchMode(editable);
        nearestPHCET.setFocusableInTouchMode(editable);

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
            participantSchooling = "No Schooling";
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        lessThanFiveButton.setOnClickListener(v -> {
            participantSchooling = "<5";
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        fiveToSevenButton.setOnClickListener(v -> {
            participantSchooling = "5-7";
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        eightToNineButton.setOnClickListener(v -> {
            participantSchooling = "8-9";
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        tenToElevenButton.setOnClickListener(v -> {
            participantSchooling = "10-11";
            noSchoolingButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            lessThanFiveButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            fiveToSevenButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            eightToNineButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            tenToElevenButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            twelveOrMoreButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        twelveOrMoreButton.setOnClickListener(v -> {
            participantSchooling = "12ormore";
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
            participantOccupation = "Professional";
            professionalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        clericalButton.setOnClickListener(v -> {
            participantOccupation = "Clerical";
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        salesAndServicesButton.setOnClickListener(v -> {
            participantOccupation = "Sales and Services";
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        skilledManualButton.setOnClickListener(v -> {
            participantOccupation = "Skilled Manual";
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        unskilledManualButton.setOnClickListener(v -> {
            participantOccupation = "Unskilled Manual";
            professionalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            clericalButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            salesAndServicesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            skilledManualButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            unskilledManualButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            agricultureButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        agricultureButton.setOnClickListener(v -> {
            participantOccupation = "Agriculture";
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
            participantReligion = "Hindu";
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
            participantReligion = "Muslim";
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
            participantReligion = "Christian";
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
            participantReligion = "Sikh";
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
            participantReligion = "Buddhist/Neo buddhist";
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
            participantReligion = "Jain";
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
            participantReligion = "Other";
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
            participantReligion = "Preferred Not to Answer";
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
            participantCaste = "Scheduled Caste";
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        scheduledTribeButton.setOnClickListener(v -> {
            participantCaste = "Scheduled Tribe";
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        backwardClassesButton.setOnClickListener(v -> {
            participantCaste = "Other Backward Classes" ;
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        doNotKnowCasteButton.setOnClickListener(v -> {
            participantCaste = "Don't Know";
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        otherCasteButton.setOnClickListener(v -> {
            participantCaste = "Other";
            scheduledCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            scheduledTribeButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            backwardClassesButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            doNotKnowCasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            otherCasteButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            pNACasteButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        pNACasteButton.setOnClickListener(v -> {
            participantCaste = "Preferred Not to Answer";
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
            participantMaritalStatus = "Never Married";
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        marriedButton.setOnClickListener(v -> {
            participantMaritalStatus = "Married";
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        divorcedButton.setOnClickListener(v -> {
            participantMaritalStatus = "Divorced";
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);

        });

        separatedButton.setOnClickListener(v -> {
            participantMaritalStatus = "Separated";
            neverMarriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            marriedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            divorcedButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            separatedButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            widowButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        widowButton.setOnClickListener(v -> {
            participantMaritalStatus = "Widow";
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
            participantFamilyType = "Nuclear Family";
            nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
            jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
        });

        jointFamilyButton.setOnClickListener(v -> {
            participantFamilyType = "Joint Family";
            nuclearFamilyButton.setBackgroundResource(R.drawable.socio_demo_inputs_background);
            jointFamilyButton.setBackgroundResource(R.drawable.socio_demo_selected_inputs_background);
        });
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private SocioDemography getUserEnteredData(){
        SocioDemography socioDemographyObject = new SocioDemography();
        socioDemographyObject.setUser_pri_id(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.participantPrimaryID)));
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
        socioDemographyObject.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));

        return socioDemographyObject;
    }

    private boolean isUserEnteredAllDetails(){
        numAdultFamily = !numAdultFamilyET.getText().toString().isEmpty() ? numAdultFamilyET.getText().toString() : "NULL";
        totalFamilyIncome = !totalFamilyIncomeET.getText().toString().isEmpty() ? totalFamilyIncomeET.getText().toString(): "NULL";
        totalEarningFamilyMembers = !totalEarningFamilyMembersET.getText().toString().isEmpty() ? totalEarningFamilyMembersET.getText().toString(): "NULL";
        numChildFamily = !numChildFamilyET.getText().toString().isEmpty() ? numChildFamilyET.getText().toString(): "NULL";
        associationDuration = !associationDurationET.getText().toString().isEmpty() ? associationDurationET.getText().toString(): "NULL";
        SHGMeetings = !SHGMeetingsET.getText().toString().isEmpty() ? SHGMeetingsET.getText().toString(): "NULL";
        nearestPHC = !nearestPHCET.getText().toString().isEmpty() ? nearestPHCET.getText().toString(): "NULL";

        if(numAdultFamily.equalsIgnoreCase("null") && totalFamilyIncome.equalsIgnoreCase("null") && totalEarningFamilyMembers.equalsIgnoreCase("null") && numChildFamily.equalsIgnoreCase("null") &&
                associationDuration.equalsIgnoreCase("null") && SHGMeetings.equalsIgnoreCase("null") && nearestPHC.equalsIgnoreCase("null") && participantSchooling.equalsIgnoreCase("null") && participantCaste.equalsIgnoreCase("null") &&
                participantOccupation.equalsIgnoreCase("null") && participantReligion.equalsIgnoreCase("null") && participantFamilyType.equalsIgnoreCase("null") && participantMaritalStatus.equalsIgnoreCase("null") ){
            Toast.makeText(context, "Please enter all the details.", Toast.LENGTH_LONG).show();
            return false;
        }else if(numAdultFamily.equalsIgnoreCase("null") || numAdultFamily.isEmpty() || numAdultFamily.equalsIgnoreCase(" ")){
            numAdultFamilyET.setError("Please enter the number of adult family members");
            return false;
        }else if(totalFamilyIncome.equalsIgnoreCase("null") || totalFamilyIncome.isEmpty() || totalFamilyIncome.equalsIgnoreCase(" ")){
            totalFamilyIncomeET.setError("Please enter total family income.");
            return false;
        }else if(totalEarningFamilyMembers.equalsIgnoreCase("null") || totalEarningFamilyMembers.isEmpty() || totalEarningFamilyMembers.equalsIgnoreCase(" ")){
            totalEarningFamilyMembersET.setError("Please enter the number of total earning family members");
            return false;
        }else if(numChildFamily.equalsIgnoreCase("null") || numChildFamily.isEmpty() || numChildFamily.equalsIgnoreCase(" ")){
            numChildFamilyET.setError("Please enter the number of children in the family.");
            return false;
        }else if(associationDuration.equalsIgnoreCase("null") || associationDuration.isEmpty() || associationDuration.equalsIgnoreCase(" ")){
            associationDurationET.setError("Please enter the duration of association with CBO/SHG.");
            return false;
        }else if(SHGMeetings.equalsIgnoreCase("null") || SHGMeetings.isEmpty() || SHGMeetings.equalsIgnoreCase(" ")){
            SHGMeetingsET.setError("Please enter the number of SHG meetings attended.");
            return false;
        }else if(nearestPHC.equalsIgnoreCase("null") || nearestPHC.isEmpty() || nearestPHC.equalsIgnoreCase(" ")){
            nearestPHCET.setError("Please enter the nearest PHC.");
            return false;
        }else if(participantFamilyType.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's Family Type.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(participantReligion.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's religion.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(participantMaritalStatus.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's marital status.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(participantOccupation.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's occupation.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(participantCaste.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's caste.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(participantSchooling.equalsIgnoreCase("null")){
            Toast.makeText(context, "Please select participant's schooling.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    /**
     * Description : This method is called when user clicks on the next button
     */
    private void onClickOfNextButton(){
        nextButton.setOnClickListener(v -> {
            if(isEditable!=null && isEditable.equals("true")){
                boolean isValid = isUserEnteredAllDetails();
                if(isValid){
                    callServerPostSocioDemography();
                }
//            moveToDiseaseProfileTab();
            }else{

                boolean isValid = isUserEnteredAllDetails();
                if(isValid){
                    if(trackingParticipantStatus!=null){
                        callServerUpdateSocioDemographyDetails();
                    }else{
                        callServerPostSocioDemography();
                    }
                }
            }
        });
    }

    private void setOnclickOfEditButton(){
        editButton.setOnClickListener(v -> {
            if(isEditable!=null && isEditable.equals("false")){
                editButton.setBackgroundResource(R.drawable.status_button);
                nextButton.setVisibility(View.VISIBLE);
                nextButton.setText(R.string.update);
                nextButton.setBackgroundResource(R.drawable.button_background);
                nextButton.setTextColor(getResources().getColor(R.color.white));
                isEditable = "reEdit";
                setEditable();
            }else if(isEditable!=null && isEditable.equals("reEdit")){
                editButton.setBackgroundResource(R.drawable.yes_no_button);
                nextButton.setVisibility(View.INVISIBLE);
                nextButton.setText(R.string.next);
                nextButton.setBackgroundResource(R.drawable.inputs_background);
                nextButton.setTextColor(getResources().getColor(R.color.text_color));
                isEditable = "false";
                setEditable();
            }
        });
    }

    private void callServerPostSocioDemography(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/demography";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setSocioDemographyServerEvents(this);
        requestObject.postSocioDemographyDetails(context, getUserEnteredData(), url);
    }

    private void callUpdateTrackingDetails(String socioDemographyName){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/tracking/" + trackingName;
        UpdateSocioDemographyTracking trackingParticipantStatus = new UpdateSocioDemographyTracking();
        trackingParticipantStatus.setSocio_demography(socioDemographyName);
        trackingParticipantStatus.setEnroll("66");
        trackingParticipantStatus.setModified_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setSocioDemographyServerEvents(this);
        requestObject.putTrackingStatusSocioDemography(context, trackingParticipantStatus, url);
    }

    private void callGetIndividualSocioDemographyDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/demography/" + trackingParticipantStatus.getSocio_demography();
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setSocioDemographyServerEvents(this);
        requestObject.getParticipantSocioDemographyDetails(context, url);
    }

    private void callServerUpdateSocioDemographyDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/demography/" +  trackingParticipantStatus.getSocio_demography();
        SocioDemography socioDemographyObject = getUserEnteredData();
        socioDemographyObject.setUser_pri_id(trackingParticipantStatus.getUser_pri_id());
        socioDemographyObject.setModified_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setSocioDemographyServerEvents(this);
        requestObject.putSocioDemographyDetails(context, socioDemographyObject, url);
    }

    private void moveToDiseaseProfileTab(){
        ((ParticipantProfileScreen)context).setupSelectedTabFragment(2);
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
    public void postSocioDemography(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type type = new TypeToken<FrappeResponse>() {
        }.getType();
        if (jsonObjectRegistration.get("data") != null) {
            FrappeResponse frappeResponse;
            frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
            if (frappeResponse != null && frappeResponse.getDoctype().equals("demography")) {
                String registrationName = frappeResponse.getName();
                mithraUtility.putSharedPreferencesData(context, context.getString(R.string.socio_demography_sp), frappeResponse.getUser_pri_id(), registrationName);
                callUpdateTrackingDetails(registrationName);
            }
            else {
                Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void updateSocioTrackingDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type type = new TypeToken<FrappeResponse>(){}.getType();
        if(jsonObjectRegistration.get("data")!=null){
            FrappeResponse frappeResponse;
            frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
            trackingName = frappeResponse.getName();
            participant_primary_ID = frappeResponse.getUser_pri_id();
            moveToDiseaseProfileTab();
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void individualSocioDemography(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObjectRegistration.get("data")!=null){
            Type typeSocioDemography = new TypeToken<SocioDemography>(){}.getType();
            socioDemographyDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeSocioDemography);
            trackingParticipantStatus.setUser_pri_id(socioDemographyDetails.getUser_pri_id());
            editButton.setEnabled(true);
            nextButton.setVisibility(View.INVISIBLE);
            isEditable = "false";
            setEditable();
        }else{
            Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateSocioDemographyDetails(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObjectRegistration.get("data")!=null){
            if(isEditable!=null && isEditable.equals("reEdit")){
                editButton.setBackgroundResource(R.drawable.yes_no_button);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
            }
            Type typeSocioDemography = new TypeToken<SocioDemography>(){}.getType();
            socioDemographyDetails = gson.fromJson(jsonObjectRegistration.get("data"), typeSocioDemography);
            trackingParticipantStatus.setUser_pri_id(socioDemographyDetails.getUser_pri_id());
            editButton.setEnabled(true);
            nextButton.setVisibility(View.INVISIBLE);
            isEditable = "false";
            setEditable();
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
