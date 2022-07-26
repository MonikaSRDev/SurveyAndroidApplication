package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.ParticipantProfileScreen.participant_primary_ID;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.GetParticipantDetails;
import com.example.mithraapplication.ModelClasses.Locations;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdatePassword;
import com.example.mithraapplication.ModelClasses.UpdateRegisterParticipant;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener, HandleServerResponse {

    private EditText participantNameET, participantAgeET, participantPhoneNumberET, participantUserNameET, participantPasswordET, participantConfirmPasswordET;
    private Button createButton, maleButton, femaleButton, othersButton, editButton, createNewPasswordButton;
    private TextView participantAgeTV, participantPhoneNumberTV, participantUserNameTV, participantPasswordTV, participantConfirmPasswordTV, VillageNameTV, PanchayatTV, SHGAssociationTV, participantNameTV, genderTV;
    private Spinner VillageNameSpinner, SHGSpinner, PanchayatSpinner, CountryCodeSpinner;
    private String participantName, participantUserName, participantPassword;
    private String participantVillageName, participantSHGAssociation, participantPanchayat, participantGender, participantPhoneNum, participantCountryCode;
    private int participantAge;
    private List<String> PanchayatNamesList, VillageNamesList, SHGNamesList, CountryCodeList = new ArrayList<>();
    private List<Locations> locationsArrayList, filteredVillageList, filteredSHGList, locationsList;
    private ArrayAdapter adapterForVillageNames, SHGSpinnerAdapter, panchayatSpinnerAdapter, countryCodeAdapter;
    private MithraUtility mithraUtility = new MithraUtility();
    public static String trackingName = "NULL";
    private TrackingParticipantStatus trackingParticipantStatus = null;
    private RegisterParticipant registerParticipantDetails = null;
    private String isEditable;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        callGetLocationsForParticipant();
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getRegistration()!=null){
            if(isEditable!= null && !isEditable.equals("true")){
                callGetIndividualParticipantDetails();
            }
        }else{
            isEditable = "true";
            editButton.setEnabled(false);
        }
        onClickRegisterButton();
        getSelectedGender();
        setOnclickOfEditButton();
    }

    public RegistrationFragment(TrackingParticipantStatus trackingParticipantStatus, String isEditable, RegisterParticipant registerParticipant){
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.registerParticipantDetails = registerParticipant;
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews(View view){
        participantNameET = view.findViewById(R.id.participantNameET);
        participantAgeET = view.findViewById(R.id.participantAgeET);
        participantPhoneNumberET = view.findViewById(R.id.participantPhoneNumberET);
        participantUserNameET = view.findViewById(R.id.participantUserNameET);
        participantPasswordET = view.findViewById(R.id.participantPasswordET);
        participantConfirmPasswordET = view.findViewById(R.id.participantConfirmPasswordET);

        participantAgeTV = view.findViewById(R.id.ageTextView);
        participantPhoneNumberTV = view.findViewById(R.id.phoneNumberTV);
        participantUserNameTV = view.findViewById(R.id.participantUserName);
        participantPasswordTV = view.findViewById(R.id.passwordTV);
        participantConfirmPasswordTV = view.findViewById(R.id.confirmPasswordTV);
        VillageNameTV = view.findViewById(R.id.villageNameTV);
        SHGAssociationTV = view.findViewById(R.id.SHGNameTV);
        PanchayatTV = view.findViewById(R.id.panchayatNameTV);
        participantNameTV = view.findViewById(R.id.participantName);
        genderTV = view.findViewById(R.id.genderTV);

        createButton = view.findViewById(R.id.registerButton);
        maleButton = view.findViewById(R.id.maleOption);
        femaleButton = view.findViewById(R.id.femaleOption);
        femaleButton.setBackgroundResource(R.drawable.selected_button);
        participantGender = "Female";
        othersButton = view.findViewById(R.id.othersOption);
        editButton = requireActivity().findViewById(R.id.profileEditButton);
        createNewPasswordButton = view.findViewById(R.id.createNewPasswordButton);
        createNewPasswordButton.setVisibility(View.GONE);

        VillageNameSpinner = view.findViewById(R.id.villageNameSpinner);
        SHGSpinner = view.findViewById(R.id.spinnerSHG);
        PanchayatSpinner = view.findViewById(R.id.spinnerPanchayat);

        CountryCodeSpinner = view.findViewById(R.id.spinnerCountryCode);
        CountryCodeSpinner.setOnItemSelectedListener(this);

        CountryCodeList.add("+91");
        countryCodeAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, CountryCodeList);
        CountryCodeSpinner.setAdapter(countryCodeAdapter);
        CountryCodeSpinner.setSelection(0, true);
//        View viewSpinner = CountryCodeSpinner.getSelectedView();
//        ((TextView)viewSpinner).setTextColor(getResources().getColor(R.color.text_color));
    }

    private void setEditable(){
        if(isEditable!=null && isEditable.equals("true")){
            participantNameET.setFocusable(true);
            participantAgeET.setFocusable(true);
            participantPhoneNumberET.setFocusable(true);
            participantUserNameET.setFocusable(true);
            participantPasswordET.setFocusable(true);
            participantConfirmPasswordET.setFocusable(true);

            participantNameET.setClickable(true);
            participantAgeET.setClickable(true);
            participantPhoneNumberET.setClickable(true);
            participantUserNameET.setClickable(true);
            participantPasswordET.setClickable(true);
            participantConfirmPasswordET.setClickable(true);

            maleButton.setEnabled(true);
            femaleButton.setEnabled(true);
            othersButton.setEnabled(true);
            createButton.setEnabled(true);
            createButton.setTextColor(getResources().getColor(R.color.white));
            createButton.setBackgroundResource(R.drawable.button_background);

            VillageNameSpinner.setEnabled(true);
            SHGSpinner.setEnabled(true);
            PanchayatSpinner.setEnabled(true);
            CountryCodeSpinner.setEnabled(true);

        }else if(isEditable!=null && isEditable.equals("false")){
            participantNameET.setFocusable(false);
            participantNameET.setClickable(false);
            participantNameET.setText(registerParticipantDetails.getParticipantName());

            participantAgeET.setFocusable(false);
            participantAgeET.setClickable(false);
            participantAgeET.setText(registerParticipantDetails.getParticipantAge());

            participantPhoneNumberET.setFocusable(false);
            participantPhoneNumberET.setClickable(false);
            Phonenumber.PhoneNumber phoneNumber = getCountryCodeAndNumber(registerParticipantDetails.getParticipantPhoneNumber());
            if(phoneNumber!=null){
                participantCountryCode = String.valueOf(phoneNumber.getCountryCode());
                participantPhoneNum = String.valueOf(phoneNumber.getNationalNumber());
            }else{
                participantPhoneNum = null;
            }
            participantPhoneNumberET.setText(participantPhoneNum);

            participantUserNameET.setFocusable(false);
            participantUserNameET.setClickable(false);
            participantUserNameET.setText(registerParticipantDetails.getParticipantUserName());

            participantPasswordTV.setVisibility(View.INVISIBLE);
            participantPasswordET.setVisibility(View.GONE);
            participantConfirmPasswordTV.setVisibility(View.INVISIBLE);
            participantConfirmPasswordET.setVisibility(View.INVISIBLE);

            maleButton.setEnabled(false);
            femaleButton.setEnabled(false);
            othersButton.setEnabled(false);
            createButton.setEnabled(false);
            createButton.setTextColor(getResources().getColor(R.color.text_color));
            createButton.setText(R.string.create);
            createButton.setBackgroundResource(R.drawable.inputs_background);
            createNewPasswordButton.setVisibility(View.VISIBLE);
            createNewPasswordButton.setEnabled(false);

            VillageNameSpinner.setSelection(((ArrayAdapter<String>)VillageNameSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantVillageName()));
            VillageNameSpinner.setEnabled(false);
            SHGSpinner.setSelection(((ArrayAdapter<String>)SHGSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantSHGAssociation()));
            SHGSpinner.setEnabled(false);
            PanchayatSpinner.setSelection(((ArrayAdapter<String>)PanchayatSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantPanchayat()));
            PanchayatSpinner.setEnabled(false);
            CountryCodeSpinner.setEnabled(false);
        }else{
            participantNameET.setText(registerParticipantDetails.getParticipantName());
            participantNameET.setFocusable(true);
            participantNameET.setClickable(true);
            participantNameET.setFocusableInTouchMode(true);

            participantAgeET.setFocusable(true);
            participantAgeET.setClickable(true);
            participantAgeET.setFocusableInTouchMode(true);
            participantAgeET.setText(registerParticipantDetails.getParticipantAge());

            participantPhoneNumberET.setFocusable(true);
            participantPhoneNumberET.setClickable(true);
            participantPhoneNumberET.setFocusableInTouchMode(true);
            Phonenumber.PhoneNumber phoneNumber = getCountryCodeAndNumber(registerParticipantDetails.getParticipantPhoneNumber());
            if(phoneNumber!=null){
                participantCountryCode = String.valueOf(phoneNumber.getCountryCode());
                participantPhoneNum = String.valueOf(phoneNumber.getNationalNumber());
            }else{
                participantPhoneNum = null;
            }
            participantPhoneNumberET.setText(participantPhoneNum);

            participantUserNameET.setFocusable(true);
            participantUserNameET.setClickable(true);
            participantUserNameET.setFocusableInTouchMode(true);
            participantUserNameET.setText(registerParticipantDetails.getParticipantUserName());

            participantPasswordTV.setVisibility(View.INVISIBLE);
            participantPasswordET.setVisibility(View.GONE);
            participantPasswordET.setFocusable(false);
            participantPasswordET.setClickable(false);
            participantPasswordET.setInputType(InputType.TYPE_CLASS_TEXT);
            participantConfirmPasswordTV.setVisibility(View.INVISIBLE);
            participantConfirmPasswordET.setVisibility(View.INVISIBLE);

            maleButton.setEnabled(true);
            femaleButton.setEnabled(true);
            othersButton.setEnabled(true);
            createButton.setEnabled(true);
            createButton.setText(R.string.update);
            createButton.setTextColor(getResources().getColor(R.color.white));
            createButton.setBackgroundResource(R.drawable.button_background);
            createNewPasswordButton.setVisibility(View.VISIBLE);
            createNewPasswordButton.setEnabled(true);
            onClickOfCreateNewPassword();

            VillageNameSpinner.setSelection(((ArrayAdapter)VillageNameSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantVillageName()));
            VillageNameSpinner.setEnabled(true);
            SHGSpinner.setSelection(((ArrayAdapter)SHGSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantSHGAssociation()));
            SHGSpinner.setEnabled(true);
            PanchayatSpinner.setSelection(((ArrayAdapter)PanchayatSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantPanchayat()));
            PanchayatSpinner.setEnabled(true);
            CountryCodeSpinner.setEnabled(true);
        }
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private boolean getUserEnteredData(){
        participantName = participantNameET.getText().toString();
        participantAge = !participantAgeET.getText().toString().isEmpty() ? Integer.parseInt(participantAgeET.getText().toString()): 0;
        participantPhoneNum = participantCountryCode + participantPhoneNumberET.getText().toString();
        participantUserName = participantUserNameET.getText().toString();
        participantPassword = participantPasswordET.getText().toString();
        boolean valid = false;
        if(participantPassword.isEmpty()){
            participantPasswordET.setError("Please enter a password.");
        }else if(participantPassword.length() < 4){
            participantPasswordET.setError("Password must be minimum of 4 characters.");
        } else {
            valid = checkIfPasswordMatches(participantPassword, participantConfirmPasswordET.getText().toString());
            if(valid){
                return true;
            }else{
                participantConfirmPasswordET.setError("Password does not match. Please check and re-enter the password.");
                return false;
            }
        }
        return valid;
    }

    private void getSelectedGender(){
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setBackgroundResource(R.drawable.selected_button);
                participantGender = "Male";

                femaleButton.setBackgroundResource(R.drawable.yes_no_button);
                othersButton.setBackgroundResource(R.drawable.yes_no_button);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setBackgroundResource(R.drawable.selected_button);
                participantGender = "Female";

                othersButton.setBackgroundResource(R.drawable.yes_no_button);
                maleButton.setBackgroundResource(R.drawable.yes_no_button);
            }
        });

        othersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                othersButton.setBackgroundResource(R.drawable.selected_button);
                participantGender = "Other";

                femaleButton.setBackgroundResource(R.drawable.yes_no_button);
                maleButton.setBackgroundResource(R.drawable.yes_no_button);
            }
        });

    }

    private Phonenumber.PhoneNumber getCountryCodeAndNumber(String phoneNumber){
        PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber pn = null;
        try{
            pn = pnu.parse(phoneNumber, "IN");
        }catch(Exception e){
            e.printStackTrace();
        }
        return pn;
    }

    private void onClickOfCreateNewPassword(){
        createNewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToCreatePassword();
            }
        });
    }

    private void showDialogToCreatePassword(){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_create_password_popup, null);

        TextView createNewPasswordTV = customLayout.findViewById(R.id.createNewPasswordTV);
        TextView diffPassword = customLayout.findViewById(R.id.diffPasswordTV);
        TextView passwordTV = customLayout.findViewById(R.id.passwordTVPopup);
        TextView eightCharactersTV = customLayout.findViewById(R.id.eightCharTVPopup);
        TextView confirmPasswordTV = customLayout.findViewById(R.id.confirmPasswordTVPopup);
        TextView passwordMatchTV = customLayout.findViewById(R.id.passwordsMatchTVPopup);
        EditText passwordET = customLayout.findViewById(R.id.passwordETPopup);
        EditText confirmPasswordET = customLayout.findViewById(R.id.confirmPasswordETPopup);
        Button resetButton = customLayout.findViewById(R.id.resetPasswordButtonPopup);

        dialog  = new Dialog(getActivity());
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordET.getText().toString();
                if(password.isEmpty()){
                    passwordET.setError("Please enter the password");
                }else if(password.length() < 4){
                    passwordET.setError("Password must be minimum of 4 characters.");
                }
                boolean matches = checkIfPasswordMatches(password, confirmPasswordET.getText().toString());
                if(matches){
                    participantUserName = participantUserNameET.getText().toString();
                    callUpdateParticipantPassword(password);
                    dialog.dismiss();
                }else{
                    confirmPasswordET.setError("Password does not match. Please check and re-enter the password.");
                }
            }});
    }

    /**
     * Description : This method is used to check if the password entered by the user matches with the previously entered password
     */
    private boolean checkIfPasswordMatches(String participantPasswordVal, String confirmPassword) {
        if(participantPasswordVal.equals(confirmPassword)){
            participantPassword = participantPasswordVal;
            return true;
        }else{
            return  false;
        }
    }

    /**
     * Description : This method is used to register the participant
     */
    private void onClickRegisterButton(){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isvalid = getUserEnteredData();
                if(isEditable!=null && isEditable.equals("reEdit")){
                    callServerUpdateParticipantDetails();
                }
                if(isvalid) {
                    if(isEditable!=null && isEditable.equals("reEdit")){
                        callServerUpdateParticipantDetails();
                    }else{
                        callServerLoginForParticipant();
                    }
                }
            }
        });
    }

    private void setOnclickOfEditButton(){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditable!=null && isEditable.equals("false")){
                    editButton.setBackgroundResource(R.drawable.status_button);
                    isEditable = "reEdit";
                    setEditable();
                }else if(isEditable!=null && isEditable.equals("reEdit")){
                    editButton.setBackgroundResource(R.drawable.yes_no_button);
                    isEditable = "false";
                    setEditable();
                }
            }
        });
    }

    private RegisterParticipant getRegisterParticipantData(){
        RegisterParticipant registerParticipant = new RegisterParticipant();
        registerParticipant.setParticipantUserName(participantUserName);
        registerParticipant.setParticipantName(participantName);
        registerParticipant.setParticipantGender(participantGender);
        registerParticipant.setParticipantAge(String.valueOf(participantAge));
        registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
        registerParticipant.setParticipantVillageName(participantVillageName);
        registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
        registerParticipant.setParticipantPanchayat(participantPanchayat);
        String screeningID = mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.userScreeningName), getString(R.string.userScreeningID));
        registerParticipant.setScreeningid(screeningID);
        registerParticipant.setCreated_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setUser_pri_id(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.participantPrimaryID)));

        return registerParticipant;
    }


    private void callGetLocationsForParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.locations";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantLocations(getActivity(), url);
    }

    private void callServerLoginForParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.useradd";
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(participantUserName);
        userLogin.setUserPassword(participantPassword);
        userLogin.setUserRole("participant");
        userLogin.setActive("yes");
        userLogin.setCreated_user(mithraUtility.getSharedPreferencesData(requireActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postUserLogin(getActivity(), userLogin, url);
    }

    private void callServerRegisterParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/participant";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postRegisterUser(getActivity(), getRegisterParticipantData(), url);
    }

    private void callCreateTrackingStatus(String registrationName) {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking";
        TrackingParticipantStatus trackingParticipantStatus = new TrackingParticipantStatus();
        trackingParticipantStatus.setUser_pri_id(mithraUtility.getSharedPreferencesData(requireActivity(), getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
        trackingParticipantStatus.setRegistration(registrationName);
        trackingParticipantStatus.setActive("yes");
        trackingParticipantStatus.setEnroll("no");
        trackingParticipantStatus.setCreated_user(mithraUtility.getSharedPreferencesData(requireActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postTrackingStatus(getActivity(), trackingParticipantStatus, url);
    }

    private void callGetIndividualParticipantDetails() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.participant.api.one_participant";
        GetParticipantDetails participantDetails = new GetParticipantDetails();
        participantDetails.setUser_pri_id(registerParticipantDetails.getUser_pri_id());
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantRegistrationDetails(getActivity(), participantDetails, url);
    }

    private void callServerUpdateParticipantDetails() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/participant/" +  trackingParticipantStatus.getRegistration();
        UpdateRegisterParticipant registerParticipant = new UpdateRegisterParticipant();
        registerParticipant.setParticipantUserName(participantUserName);
        registerParticipant.setParticipantName(participantName);
        registerParticipant.setParticipantGender(participantGender);
        registerParticipant.setParticipantAge(String.valueOf(participantAge));
        registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
        registerParticipant.setParticipantVillageName(participantVillageName);
        registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
        registerParticipant.setParticipantPanchayat(participantPanchayat);
        registerParticipant.setCreated_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setModified_user(mithraUtility.getSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setUser_pri_id(registerParticipantDetails.getUser_pri_id());
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putRegisterUser(getActivity(), registerParticipant, url);
    }

    private void callUpdateParticipantPassword(String password) {
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.changepassword";
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setName(registerParticipantDetails.getUser_pri_id());
        updatePassword.setPassword(password);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putUpdateUserPassword(getActivity(), updatePassword, url);
    }

    /**
     * Description : This method is used to move from the Registration tab to SocioDemography tab
     */
    private void moveToSocioDemographyTab(String trackingName){
        Log.i("RegistrationFragment", "moveToSocioDemographyTab");
        ((ParticipantProfileScreen) requireActivity()).setupSelectedTabFragment(2);
    }

    /**
     * Description : This method is used to get the selected item from the dropdown list
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerCountryCode) {
            participantCountryCode = CountryCodeList.get(position);
            CountryCodeSpinner.setSelection(position);
        }
        else if(parent.getId() == R.id.spinnerSHG){
            participantSHGAssociation = SHGNamesList.get(position);
            SHGSpinner.setSelection(position, false);

        }else if(parent.getId() == R.id.villageNameSpinner){

            participantVillageName = VillageNamesList.get(position);
            VillageNameSpinner.setSelection(position, false);

            if(filteredVillageList != null && filteredVillageList.size()!=0){
                filteredSHGList = filteredVillageList.stream().filter(location -> location.getVillage().contains(participantVillageName)).distinct().collect(Collectors.toList());
                SHGNamesList.clear();
                SHGNamesList = filteredSHGList.stream().map(Locations::getShg).distinct().collect(Collectors.toList());
                SHGSpinnerAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, SHGNamesList);
                SHGSpinner.setAdapter(SHGSpinnerAdapter);
            }
        }else if(parent.getId() == R.id.spinnerPanchayat){
            participantPanchayat = PanchayatNamesList.get(position);
            PanchayatSpinner.setSelection(position, false);

            if(locationsList!=null && locationsList.size()!=0) {
                filteredVillageList = locationsArrayList.stream().filter(location -> location.getPanchayat().contains(participantPanchayat)).distinct().collect(Collectors.toList());
                VillageNamesList.clear();
                VillageNamesList = filteredVillageList.stream().map(Locations::getVillage).distinct().collect(Collectors.toList());
                adapterForVillageNames = new ArrayAdapter(getActivity(), R.layout.spinner_item, VillageNamesList);
                VillageNameSpinner.setAdapter(adapterForVillageNames);
            }else{
                locationsList = locationsArrayList;
            }

            if(locationsList != null && locationsList.size()!=0){
                filteredSHGList = locationsArrayList.stream().filter(location -> location.getPanchayat().contains(participantPanchayat)).distinct().collect(Collectors.toList());
                SHGNamesList.clear();
                SHGNamesList = filteredSHGList.stream().map(Locations::getShg).distinct().collect(Collectors.toList());
                SHGSpinnerAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, SHGNamesList);
                SHGSpinner.setAdapter(SHGSpinnerAdapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void prepareSpinnerData(){
        PanchayatNamesList = locationsArrayList.stream().map(Locations::getPanchayat).distinct().collect(Collectors.toList());
        VillageNamesList = locationsArrayList.stream().map(Locations::getVillage).distinct().collect(Collectors.toList());
        SHGNamesList = locationsArrayList.stream().map(Locations::getShg).distinct().collect(Collectors.toList());

        adapterForVillageNames = new ArrayAdapter(getActivity(), R.layout.spinner_item, VillageNamesList);
        VillageNameSpinner.setAdapter(adapterForVillageNames);
        participantVillageName = VillageNamesList.get(0);
        VillageNameSpinner.setSelection(0,false);
        VillageNameSpinner.setOnItemSelectedListener(this);

        SHGSpinnerAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, SHGNamesList);
        SHGSpinner.setAdapter(SHGSpinnerAdapter);
        participantSHGAssociation = SHGNamesList.get(0);
        SHGSpinner.setSelection(0,false);
        SHGSpinner.setOnItemSelectedListener(this);

        panchayatSpinnerAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, PanchayatNamesList);
        PanchayatSpinner.setAdapter(panchayatSpinnerAdapter);
        participantPanchayat = PanchayatNamesList.get(0);
        PanchayatSpinner.setSelection(0,false);
        PanchayatSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Handle the server response
     */
    @Override
    public void responseReceivedSuccessfully(String message) {

        Gson gson = new Gson();
        Type typeLocation = new TypeToken<ArrayList<Locations>>(){}.getType();
        JsonObject jsonObjectLocation = JsonParser.parseString(message).getAsJsonObject();

        if(jsonObjectLocation.get("message")!=null){
            try{
                ArrayList<Locations> locationsArrayListObj = gson.fromJson(jsonObjectLocation.get("message"), typeLocation);
                if(locationsArrayListObj.size() > 1) {
                    locationsArrayList = locationsArrayListObj;
                    prepareSpinnerData();
                }
                else{
                    if(trackingParticipantStatus!=null){
                        if(isEditable!= null && !isEditable.equals("true")){
                            Type typeParticipant = new TypeToken<ArrayList<RegisterParticipant>>(){}.getType();
                            ArrayList<RegisterParticipant> registerParticipantArrayList = gson.fromJson(jsonObjectLocation.get("message"), typeParticipant);
                            registerParticipantDetails = registerParticipantArrayList.get(0);
                            editButton.setEnabled(true);
                            setEditable();
                        }
                    }else{
                        Type type = new TypeToken<ArrayList<UserLogin>>(){}.getType();
                        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                        ArrayList<UserLogin> userLogins;
                        try{
                            userLogins = gson.fromJson(jsonObject.get("message"), type);
                            if(userLogins!=null){
                                if(!userLogins.get(0).getUserName().equals("NULL")){
                                    mithraUtility.putSharedPreferencesData(getActivity(), getString(R.string.userName), getString(R.string.user_name_participant), userLogins.get(0).getUserName());
                                    mithraUtility.putSharedPreferencesData(getActivity(), getString(R.string.primaryID), getString(R.string.participantPrimaryID), userLogins.get(0).getUser_pri_id());
                                    callServerRegisterParticipant();
                                }
                            }
                        }catch(Exception e){
                            if(jsonObject.get("message")!=null) {
//                                Toast.makeText(getActivity(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }catch(Exception e){
                if(jsonObjectLocation.get("message")!=null && jsonObjectLocation.get("message").toString().equals("\"updated\"")) {
                    Toast.makeText(getActivity(), "Your password has been updated.", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), jsonObjectLocation.get("message").toString(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
            Type type = new TypeToken<FrappeResponse>(){}.getType();
            if(jsonObjectRegistration.get("data")!=null){
                FrappeResponse frappeResponse;
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), type);
                if(isEditable!=null && isEditable.equals("true")){
                    if(frappeResponse!=null && frappeResponse.getDoctype().equals("participant")){
                        String registrationName = frappeResponse.getName();
                        mithraUtility.putSharedPreferencesData(getActivity(), getString(R.string.registration), frappeResponse.getUser_pri_id(), registrationName);
                        callCreateTrackingStatus(registrationName);
                    }else if(frappeResponse!=null && frappeResponse.getDoctype().equals("tracking")){
                        trackingName = frappeResponse.getName();
                        mithraUtility.putSharedPreferencesData(getActivity(), getString(R.string.tracking), frappeResponse.getUser_pri_id(), trackingName);
                        mithraUtility.removeSharedPreferencesData(getActivity(), getString(R.string.userScreeningName), getString(R.string.userScreeningID));
                        participant_primary_ID = frappeResponse.getUser_pri_id();
                        moveToSocioDemographyTab(trackingName);
                    }
                }else{
                    registerParticipantDetails = getRegisterParticipantData();
                    editButton.setEnabled(true);
                    editButton.setBackgroundResource(R.drawable.yes_no_button);
                    isEditable = "false";
                    setEditable();
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                }

            }else{
                Log.i("RegistrationFragment", "JsonObjectRegistration data is Empty");
            }
        }
        Log.i("Message", "Success");
    }

    @Override
    public void responseReceivedFailure(String message) {
        Log.i("Message", "Failure");
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String serverErrorResponse = jsonObject.get("exception").toString();
        if(serverErrorResponse.contains("frappe.exceptions.DuplicateEntryError")){
            participantUserNameET.setError("User Name already exits. Please give other user name.");
        }else{
            Toast.makeText(getActivity(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        participantAgeTV.setText(R.string.age);
        participantPhoneNumberTV.setText(R.string.phone_number);
        participantUserNameTV.setText(R.string.user_name);
        participantPasswordTV.setText(R.string.password);
        participantConfirmPasswordTV.setText(R.string.confirm_password);
        VillageNameTV.setText(R.string.village_name);
        PanchayatTV.setText(R.string.panchayat);
        SHGAssociationTV.setText(R.string.shg_association);
        participantNameTV.setText(R.string.name_small_case);
        genderTV.setText(R.string.gender);


        maleButton.setText(R.string.male);
        femaleButton.setText(R.string.female);
        othersButton.setText(R.string.others);
        createButton.setText(R.string.create);
    }
}
