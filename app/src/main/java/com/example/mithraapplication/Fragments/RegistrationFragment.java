package com.example.mithraapplication.Fragments;

import static com.example.mithraapplication.ParticipantProfileScreen.participant_primary_ID;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdatePassword;
import com.example.mithraapplication.ModelClasses.UpdateRegisterParticipant;
import com.example.mithraapplication.ModelClasses.UpdateRegisterStatus;
import com.example.mithraapplication.ModelClasses.UpdateScreeningStatus;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.example.mithraapplication.PHQParticipantsScreen;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener, HandleServerResponse {

    private EditText participantAgeET, participantPhoneNumberET, participantUserNameET, participantPasswordET, participantConfirmPasswordET;
    private Button createButton, maleButton, femaleButton, othersButton, editButton, createNewPasswordButton;
    private TextView participantAgeTV, participantPhoneNumberTV, participantUserNameTV, participantPasswordTV,
            participantConfirmPasswordTV, ManualIDTV, SHGAssociationTV, participantNameTV, PHQScreeningIDTV,
            genderTV, participantSHGNameTV;
    private AutoCompleteTextView participantNameAutoCompleteTV;
    private Spinner PHQScreeningSpinner, ManualIDSpinner, CountryCodeSpinner;
    private String participantName, participantUserName, participantPassword;
    private String participantVillageName, participantSHGAssociation, participantPanchayat, participantGender, participantPhoneNum, participantCountryCode, participantManualID, participantPHQScreeningID;
    private int participantAge;
    private List<String> PHQScreeningNamesList, ManualNamesList, ParticipantNamesList, CountryCodeList = new ArrayList<>();
    private List<PHQParticipantDetails> locationsArrayList, filteredPHQScreeningList, filteredManualIDList, temporaryList;
    private ArrayAdapter PHQScreeningSpinnerAdapter, ParticipantNameAdapter, ManualIDSpinnerAdapter;
    private ArrayAdapter countryCodeAdapter;
    private MithraUtility mithraUtility = new MithraUtility();
    public static String trackingName = "NULL";
    private TrackingParticipantStatus trackingParticipantStatus = null;
    private RegisterParticipant registerParticipantDetails = null;
    private String isEditable;
    private Dialog dialog;
    private PHQLocations phqLocations;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
//        callGetLocationsForParticipant();
        callGetAllPHQParticipantsData();
        if(trackingParticipantStatus!=null && trackingParticipantStatus.getRegistration()!=null){
            if(isEditable!= null && !isEditable.equals("true")){
                editButton.setEnabled(true);
                editButton.setVisibility(View.VISIBLE);
                callGetIndividualParticipantDetails();
            }
        }else{
            isEditable = "true";
            editButton.setEnabled(false);
            editButton.setVisibility(View.GONE);
        }
        onClickRegisterButton();
        getSelectedGender();
        setOnclickOfEditButton();
    }

    public RegistrationFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable, RegisterParticipant registerParticipant, PHQLocations phqLocations){
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.registerParticipantDetails = registerParticipant;
        this.phqLocations = phqLocations;
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews(View view){
        participantNameAutoCompleteTV = view.findViewById(R.id.participantNameAutoComplete);
        participantNameAutoCompleteTV.setDropDownBackgroundResource(R.color.white);
        participantNameAutoCompleteTV.setThreshold(0);
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
        PHQScreeningIDTV = view.findViewById(R.id.PHQScreeningId);
        SHGAssociationTV = view.findViewById(R.id.SHGNameTV);
        ManualIDTV = view.findViewById(R.id.ManualIDTV);
        participantNameTV = view.findViewById(R.id.participantName);
        genderTV = view.findViewById(R.id.genderTV);
        participantSHGNameTV = view.findViewById(R.id.SHGParticipantTV);
        if(phqLocations!=null){
            participantSHGNameTV.setText(phqLocations.getSHGName());
        }

        createButton = view.findViewById(R.id.registerButton);
        maleButton = view.findViewById(R.id.maleOption);
        femaleButton = view.findViewById(R.id.femaleOption);
        femaleButton.setBackgroundResource(R.drawable.selected_button);
        participantGender = "Female";
        othersButton = view.findViewById(R.id.othersOption);
        editButton = requireActivity().findViewById(R.id.profileEditButton);
        createNewPasswordButton = view.findViewById(R.id.createNewPasswordButton);
        createNewPasswordButton.setVisibility(View.GONE);

        PHQScreeningSpinner = view.findViewById(R.id.PHQScreeningIDSpinner);
        ManualIDSpinner = view.findViewById(R.id.spinnerManualID);

        CountryCodeSpinner = view.findViewById(R.id.spinnerCountryCode);
        CountryCodeSpinner.setOnItemSelectedListener(this);

        CountryCodeList.add("+91");
        countryCodeAdapter = new ArrayAdapter(context, R.layout.spinner_item, CountryCodeList);
        CountryCodeSpinner.setAdapter(countryCodeAdapter);
        CountryCodeSpinner.setSelection(0, true);
    }

    private void setEditable(){
        if(isEditable!=null && isEditable.equals("true")){
            participantNameAutoCompleteTV.setFocusable(true);
            participantNameAutoCompleteTV.setEnabled(true);
            participantAgeET.setFocusable(true);
            participantPhoneNumberET.setFocusable(true);
            participantUserNameET.setFocusable(true);
            participantPasswordET.setFocusable(true);
            participantConfirmPasswordET.setFocusable(true);
            participantSHGNameTV.setText(phqLocations.getSHGName());

            participantNameAutoCompleteTV.setClickable(true);
            participantAgeET.setClickable(true);
            participantPhoneNumberET.setClickable(true);
            participantUserNameET.setClickable(true);
            participantPasswordET.setClickable(true);
            participantConfirmPasswordET.setClickable(true);

            maleButton.setEnabled(true);
            femaleButton.setEnabled(true);
            othersButton.setEnabled(true);
            createButton.setVisibility(View.VISIBLE);
            createButton.setEnabled(true);
            createButton.setTextColor(getResources().getColor(R.color.white));
            createButton.setBackgroundResource(R.drawable.button_background);

            PHQScreeningSpinner.setEnabled(true);
            ManualIDSpinner.setEnabled(true);
            CountryCodeSpinner.setEnabled(true);

        }else if(isEditable!=null && isEditable.equals("false")){
            participantNameAutoCompleteTV.setFocusable(false);
            participantNameAutoCompleteTV.setClickable(false);
            participantNameAutoCompleteTV.setEnabled(false);
            participantNameAutoCompleteTV.setText(registerParticipantDetails.getParticipantName());

            participantAgeET.setFocusable(false);
            participantAgeET.setClickable(false);
            participantAgeET.setText(registerParticipantDetails.getParticipantAge());

            participantPhoneNumberET.setFocusable(false);
            participantPhoneNumberET.setClickable(false);
            Phonenumber.PhoneNumber phoneNumber = mithraUtility.getCountryCodeAndNumber(registerParticipantDetails.getParticipantPhoneNumber());
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
            participantSHGNameTV.setText(phqLocations.getSHGName());

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
            createButton.setVisibility(View.INVISIBLE);
            createNewPasswordButton.setVisibility(View.VISIBLE);
            createNewPasswordButton.setEnabled(false);
//
            PHQScreeningSpinner.setSelection(((ArrayAdapter<String>)PHQScreeningSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantVillageName()));
            PHQScreeningSpinner.setEnabled(false);
            ManualIDSpinner.setSelection(((ArrayAdapter<String>)ManualIDSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantPanchayat()));
            ManualIDSpinner.setEnabled(false);
            CountryCodeSpinner.setEnabled(false);
        }else{
            participantNameAutoCompleteTV.setText(registerParticipantDetails.getParticipantName());
            participantNameAutoCompleteTV.setFocusable(true);
            participantNameAutoCompleteTV.setClickable(true);
            participantNameAutoCompleteTV.setEnabled(true);
            participantNameAutoCompleteTV.setFocusableInTouchMode(true);

            participantAgeET.setFocusable(true);
            participantAgeET.setClickable(true);
            participantAgeET.setFocusableInTouchMode(true);
            participantAgeET.setText(registerParticipantDetails.getParticipantAge());

            participantPhoneNumberET.setFocusable(true);
            participantPhoneNumberET.setClickable(true);
            participantPhoneNumberET.setFocusableInTouchMode(true);
            Phonenumber.PhoneNumber phoneNumber = mithraUtility.getCountryCodeAndNumber(registerParticipantDetails.getParticipantPhoneNumber());
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
            createButton.setVisibility(View.VISIBLE);
            createButton.setEnabled(true);
            createButton.setText(R.string.create);
            createButton.setTextColor(getResources().getColor(R.color.white));
            createButton.setBackgroundResource(R.drawable.button_background);
            createNewPasswordButton.setVisibility(View.VISIBLE);
            createNewPasswordButton.setEnabled(true);
            onClickOfCreateNewPassword();

            PHQScreeningSpinner.setSelection(((ArrayAdapter)PHQScreeningSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantVillageName()));
            PHQScreeningSpinner.setEnabled(true);
            ManualIDSpinner.setSelection(((ArrayAdapter)ManualIDSpinner.getAdapter()).getPosition(registerParticipantDetails.getParticipantPanchayat()));
            ManualIDSpinner.setEnabled(true);
            CountryCodeSpinner.setEnabled(true);
        }
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private boolean getUserEnteredData(){
        participantName = participantNameAutoCompleteTV.getText().toString();
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
        maleButton.setOnClickListener(v -> {
            maleButton.setBackgroundResource(R.drawable.selected_button);
            participantGender = "Male";

            femaleButton.setBackgroundResource(R.drawable.yes_no_button);
            othersButton.setBackgroundResource(R.drawable.yes_no_button);
        });

        femaleButton.setOnClickListener(v -> {
            femaleButton.setBackgroundResource(R.drawable.selected_button);
            participantGender = "Female";

            othersButton.setBackgroundResource(R.drawable.yes_no_button);
            maleButton.setBackgroundResource(R.drawable.yes_no_button);
        });

        othersButton.setOnClickListener(v -> {
            othersButton.setBackgroundResource(R.drawable.selected_button);
            participantGender = "Other";

            femaleButton.setBackgroundResource(R.drawable.yes_no_button);
            maleButton.setBackgroundResource(R.drawable.yes_no_button);
        });

    }

    private void onClickOfCreateNewPassword(){
        createNewPasswordButton.setOnClickListener(v -> showDialogToCreatePassword());
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

        resetButton.setOnClickListener(v -> {
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
        });
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
        createButton.setOnClickListener(v -> {
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
        });
    }

    private void setOnclickOfEditButton(){
        editButton.setOnClickListener(v -> {
            if(isEditable!=null && isEditable.equals("false")){
                editButton.setBackgroundResource(R.drawable.status_button);
                isEditable = "reEdit";
                setEditable();
            }else if(isEditable!=null && isEditable.equals("reEdit")){
                editButton.setBackgroundResource(R.drawable.yes_no_button);
                isEditable = "false";
                setEditable();
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
        registerParticipant.setLocation(phqLocations.getName());
        registerParticipant.setPhq_scr_id(participantPHQScreeningID);
        registerParticipant.setMan_id(participantManualID);
        registerParticipant.setActive("yes");
        registerParticipant.setName("null");
        registerParticipant.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setUser_pri_id(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.participantPrimaryID)));

        return registerParticipant;
    }


    private void callGetLocationsForParticipant() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.locations";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantLocations(context, url);
    }

    private void callGetAllPHQParticipantsData(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.phq9_scr_sub.api.pre_screenings";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQParticipantDetails(context, url);
    }

    private void callServerLoginForParticipant() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.useradd";
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(participantUserName);
        userLogin.setUserPassword(participantPassword);
        userLogin.setUserRole("participant");
        userLogin.setActive("yes");
        userLogin.setCreated_user(mithraUtility.getSharedPreferencesData(requireActivity(), context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postUserLogin(context, userLogin, url);
    }

    private void callServerRegisterParticipant() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/participant";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postRegisterUser(context, getRegisterParticipantData(), url);
    }

    private void callCreateTrackingStatus(String registrationName) {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/tracking";
        TrackingParticipantStatus trackingParticipantStatus = new TrackingParticipantStatus();
        trackingParticipantStatus.setUser_pri_id(mithraUtility.getSharedPreferencesData(requireActivity(), context.getString(R.string.primaryID), context.getString(R.string.participantPrimaryID)));
        trackingParticipantStatus.setRegistration(registrationName);
        trackingParticipantStatus.setActive("yes");
        trackingParticipantStatus.setEnroll("33");
        trackingParticipantStatus.setCreated_user(mithraUtility.getSharedPreferencesData(requireActivity(), context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postTrackingStatus(context, trackingParticipantStatus, url);
    }

    private void callGetIndividualParticipantDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.participant.api.one_participant";
        GetParticipantDetails participantDetails = new GetParticipantDetails();
        participantDetails.setUser_pri_id(registerParticipantDetails.getUser_pri_id());
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantRegistrationDetails(context, participantDetails, url);
    }

    private void callServerUpdateParticipantDetails() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/participant/" +  trackingParticipantStatus.getRegistration();
        UpdateRegisterParticipant registerParticipant = new UpdateRegisterParticipant();
        registerParticipant.setParticipantUserName(participantUserName);
        registerParticipant.setParticipantName(participantName);
        registerParticipant.setParticipantGender(participantGender);
        registerParticipant.setParticipantAge(String.valueOf(participantAge));
        registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
        registerParticipant.setParticipantVillageName(participantVillageName);
        registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
        registerParticipant.setParticipantPanchayat(participantPanchayat);
        registerParticipant.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setModified_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        registerParticipant.setUser_pri_id(registerParticipantDetails.getUser_pri_id());
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putRegisterUser(context, registerParticipant, url);
    }

    private void callUpdateParticipantPassword(String password) {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.changepassword";
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setName(registerParticipantDetails.getUser_pri_id());
        updatePassword.setPassword(password);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putUpdateUserPassword(context, updatePassword, url);
    }

    private void callUpdateRegisterStatus() {
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/phq9_scr_sub/" + participantPHQScreeningID;
        UpdateRegisterStatus updateRegisterStatus = new UpdateRegisterStatus();
        updateRegisterStatus.setRegister("yes");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.putUpdateRegisterStatus(context, updateRegisterStatus, url);
    }

    /**
     * Description : This method is used to move from the Registration tab to SocioDemography tab
     */
    private void moveToSocioDemographyTab(){
        Log.i("RegistrationFragment", "moveToSocioDemographyTab");
        ((ParticipantProfileScreen) requireActivity()).setupSelectedTabFragment(1);
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
        else if(parent.getId() == R.id.spinnerManualID){

            participantManualID = ManualNamesList.get(position);
            ManualIDSpinner.setSelection(position, false);
            if(temporaryList != null && temporaryList.size()!=0){
                filteredManualIDList = temporaryList.stream().filter(PHQParticipantDetails -> PHQParticipantDetails.getManualID().contains(participantManualID)).distinct().collect(Collectors.toList());
                PHQScreeningNamesList.clear();
                PHQScreeningNamesList = filteredManualIDList.stream().map(PHQParticipantDetails::getPHQScreeningID).distinct().collect(Collectors.toList());
                PHQScreeningSpinnerAdapter = new ArrayAdapter(context, R.layout.spinner_item, PHQScreeningNamesList);
                PHQScreeningSpinner.setAdapter(PHQScreeningSpinnerAdapter);
            }else{
                temporaryList = locationsArrayList;
            }
        }else if(parent.getId() == R.id.PHQScreeningIDSpinner){
            participantPHQScreeningID = PHQScreeningNamesList.get(position);
            PHQScreeningSpinner.setSelection(position, false);

            if(temporaryList!=null && temporaryList.size()!=0) {
                filteredPHQScreeningList = temporaryList.stream().filter(PHQParticipantDetails -> PHQParticipantDetails.getPHQScreeningID().contains(participantPHQScreeningID)).distinct().collect(Collectors.toList());
                ManualNamesList.clear();
                ManualNamesList = filteredPHQScreeningList.stream().map(PHQParticipantDetails::getManualID).distinct().collect(Collectors.toList());
                ManualIDSpinnerAdapter = new ArrayAdapter(context, R.layout.spinner_item, ManualNamesList);
                ManualIDSpinner.setAdapter(ManualIDSpinnerAdapter);
            }else{
                temporaryList = locationsArrayList;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void prepareSpinnerData(){
        ManualNamesList = locationsArrayList.stream().map(PHQParticipantDetails::getManualID).distinct().collect(Collectors.toList());
        PHQScreeningNamesList = locationsArrayList.stream().map(PHQParticipantDetails::getPHQScreeningID).distinct().collect(Collectors.toList());
        ParticipantNamesList = locationsArrayList.stream().map(PHQParticipantDetails::getPHQParticipantName).distinct().collect(Collectors.toList());

        ManualIDSpinnerAdapter = new ArrayAdapter(context, R.layout.spinner_item, ManualNamesList);
        ManualIDSpinner.setAdapter(ManualIDSpinnerAdapter);
        participantManualID = ManualNamesList.get(0);
        ManualIDSpinner.setSelection(0,false);
        ManualIDSpinner.setOnItemSelectedListener(this);

        PHQScreeningSpinnerAdapter = new ArrayAdapter(context, R.layout.spinner_item, PHQScreeningNamesList);
        PHQScreeningSpinner.setAdapter(PHQScreeningSpinnerAdapter);
        participantPHQScreeningID = PHQScreeningNamesList.get(0);
        PHQScreeningSpinner.setSelection(0,false);
        PHQScreeningSpinner.setOnItemSelectedListener(this);

        ParticipantNameAdapter = new ArrayAdapter(context, R.layout.spinner_item, ParticipantNamesList);
        participantNameAutoCompleteTV.setAdapter(ParticipantNameAdapter);
        participantNameAutoCompleteTV.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                participantNameAutoCompleteTV.showDropDown();
        });
        participantNameAutoCompleteTV.setOnTouchListener((v, event) -> {
            participantNameAutoCompleteTV.showDropDown();
            return false;
        });
        participantName = PHQScreeningNamesList.get(0);
    }

    /**
     * Handle the server response
     */
    @Override
    public void responseReceivedSuccessfully(String message) {

        Gson gson = new Gson();
        Type typeLocation = new TypeToken<ArrayList<PHQParticipantDetails>>(){}.getType();
        JsonObject jsonObjectLocation = JsonParser.parseString(message).getAsJsonObject();

        if(jsonObjectLocation.get("message")!=null){
            try{
                ArrayList<PHQParticipantDetails> locationsArrayListObj = gson.fromJson(jsonObjectLocation.get("message"), typeLocation);
                if(locationsArrayListObj.size() > 1) {
                    locationsArrayList = locationsArrayListObj;
                    temporaryList = locationsArrayList;
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
                                    mithraUtility.putSharedPreferencesData(context, context.getString(R.string.userName), context.getString(R.string.user_name_participant), userLogins.get(0).getUserName());
                                    mithraUtility.putSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.participantPrimaryID), userLogins.get(0).getUser_pri_id());
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
                    Toast.makeText(context, "Your password has been updated.", Toast.LENGTH_LONG).show();
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
                        mithraUtility.putSharedPreferencesData(context, context.getString(R.string.registration), frappeResponse.getUser_pri_id(), registrationName);
//                        callUpdateRegisterStatus();
                        callCreateTrackingStatus(registrationName);
                    }else if(frappeResponse!=null && frappeResponse.getDoctype().equals("tracking")){
                        trackingName = frappeResponse.getName();
                        mithraUtility.putSharedPreferencesData(context, context.getString(R.string.tracking), frappeResponse.getUser_pri_id(), trackingName);
                        mithraUtility.removeSharedPreferencesData(context, context.getString(R.string.userScreeningName), context.getString(R.string.userScreeningID));
                        participant_primary_ID = frappeResponse.getUser_pri_id();
                        moveToSocioDemographyTab();
                    }
                }else{
                    registerParticipantDetails = getRegisterParticipantData();
                    editButton.setEnabled(true);
                    editButton.setBackgroundResource(R.drawable.yes_no_button);
                    isEditable = "false";
                    setEditable();
                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
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
        PHQScreeningIDTV.setText(R.string.phq_screening_id);
        ManualIDTV.setText(R.string.manual_id);
        SHGAssociationTV.setText(R.string.shg_association);
        participantNameTV.setText(R.string.name_small_case);
        genderTV.setText(R.string.gender);

        if(createNewPasswordButton!=null){
            createNewPasswordButton.setText(R.string.create_new_password);
        }

        maleButton.setText(R.string.male);
        femaleButton.setText(R.string.female);
        othersButton.setText(R.string.others);
        createButton.setText(R.string.create);
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
}
