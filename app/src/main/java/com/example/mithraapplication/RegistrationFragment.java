package com.example.mithraapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mithraapplication.ModelClasses.Locations;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.ServerErrorResponse;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener, HandleServerResponse{

    private EditText participantNameET, participantAgeET, participantPhoneNumberET, participantUserNameET, participantPasswordET, participantConfirmPasswordET;
    private Button registerButton, maleButton, femaleButton, othersButton;
    private TextView participateAgeTV, participatePhoneNumberTV, participateUserNameTV, participatePasswordTV, participantConfirmPasswordTV, VillageNameTV, PanchayatTV, SHGAssociationTV, participantNameTV, genderTV, addParticipantTV;
    private Spinner VillageNameSpinner, SHGSpinner, PanchayatSpinner;
    private String participantName, participantUserName, participantPassword;
    private String participantVillageName, participantSHGAssociation, participantPanchayat, participantGender, participantPhoneNum;
    private int participantAge;
//    private String[] SHGNamesList = {"SHG 1", "SHG 2", "SHG 3"};
//    private String[] VillageNamesList = {"Village 1", "Village 2", "Village 3"};
//    private String[] PanchayatNamesList = {"Panchayat 1", "Panchayat 2", "Panchayat 3"};
    private List<String> PanchayatNamesList, VillageNamesList, SHGNamesList;
    private List<Locations> locationsArrayList, filteredVillageList, filteredSHGList, locationsList;
    private ArrayAdapter adapterForVillageNames, SHGSpinnerAdapter, panchayatSpinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callGetLocationsForParticipant();
        RegisterViews(view);
        onClickRegisterButton();
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

        participateAgeTV = view.findViewById(R.id.ageTextView);
        participatePhoneNumberTV = view.findViewById(R.id.phoneNumberTV);
        participateUserNameTV = view.findViewById(R.id.participantUserName);
        participatePasswordTV = view.findViewById(R.id.passwordTV);
        participantConfirmPasswordTV = view.findViewById(R.id.confirmPasswordTV);
        VillageNameTV = view.findViewById(R.id.villageNameTV);
        SHGAssociationTV = view.findViewById(R.id.SHGNameTV);
        PanchayatTV = view.findViewById(R.id.panchayatNameTV);
        participantNameTV = view.findViewById(R.id.participantName);
        genderTV = view.findViewById(R.id.genderTV);

        registerButton = view.findViewById(R.id.registerButton);
        maleButton = view.findViewById(R.id.maleOption);
        femaleButton = view.findViewById(R.id.femaleOption);
        femaleButton.setBackgroundResource(R.drawable.selected_button);
        femaleButton.setTextColor(getResources().getColor(R.color.white));
        participantGender = "Female";
        othersButton = view.findViewById(R.id.othersOption);

        VillageNameSpinner = view.findViewById(R.id.villageNameSpinner);
        VillageNameSpinner.setOnItemSelectedListener(this);

        SHGSpinner = view.findViewById(R.id.spinnerSHG);
        SHGSpinner.setOnItemSelectedListener(this);

        PanchayatSpinner = view.findViewById(R.id.spinnerPanchayat);
        PanchayatSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private boolean getUserEnteredData(){
        participantName = participantNameET.getText().toString();
        participantAge = !participantAgeET.getText().toString().isEmpty() ? Integer.parseInt(participantAgeET.getText().toString()): 0;
//        String participantPhoneNumber = participantPhoneNumberET.getText().toString();
//        participantPhoneNum = new PhoneNumber();
//        participantPhoneNum.setNumber(participantPhoneNumber);
        participantPhoneNum = participantPhoneNumberET.getText().toString();
        participantUserName = participantUserNameET.getText().toString();
        participantPassword = participantPasswordET.getText().toString();
        return checkIfPasswordMatches(participantPassword);
    }

    /**
     * Description : This method is used to check if the password entered by the user matches with the previously entered password
     */
    private boolean checkIfPasswordMatches(String participantPasswordVal) {
        String confirmPassword = participantConfirmPasswordET.getText().toString();
        if(participantPasswordVal.equals(confirmPassword)){
            participantPassword = participantPasswordVal;
            return true;
        }else{
            participantConfirmPasswordET.setError("Password does not match. Please check and re-enter the password.");
            return  false;
        }
    }

    /**
     * Description : This method is used to register the participant
     */
    private void onClickRegisterButton(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isvalid = getUserEnteredData();
                if(isvalid) {
                    callServerLoginForParticipant();
//                    callServerRegisterParticipant();
//                    moveToSocideepak46oDemographyTab();
                }
            }
        });
    }

    private void callServerRegisterParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/participant";
        RegisterParticipant registerParticipant = new RegisterParticipant();
        registerParticipant.setParticipantUserName(participantUserName);
        registerParticipant.setParticipantName(participantName);
        registerParticipant.setParticipantGender(participantGender);
        registerParticipant.setParticipantAge(String.valueOf(participantAge));
        registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
        registerParticipant.setParticipantVillageName(participantVillageName);
        registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
        registerParticipant.setParticipantPanchayat(participantPanchayat);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postRegisterUser(getActivity(), registerParticipant, url);
    }

    private void callServerLoginForParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/User Table";
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(participantUserName);
        userLogin.setUserPassword(participantPassword);
        userLogin.setUserRole("participant");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postUserLogin(getActivity(), userLogin, url);
    }

    private void callGetLocationsForParticipant() {
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.locations";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getParticipantLocations(getActivity(), url);
    }

    /**
     * Description : This method is used to move from the Registration tab to SocioDemography tab
     */
    private void moveToSocioDemographyTab(){
        ((ProfileScreen)getActivity()).setupSelectedTabFragment(1);
    }

    /**
     * Description : This method is used to get the selected item from the dropdown list
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerSHG){

            participantSHGAssociation = SHGNamesList.get(position);
            SHGSpinner.setSelection(position);
            ((TextView) view).setTextColor(Color.BLACK);

        }else if(parent.getId() == R.id.villageNameSpinner){

            participantVillageName = VillageNamesList.get(position);
            VillageNameSpinner.setSelection(position);
            ((TextView) view).setTextColor(Color.BLACK);

            if(filteredVillageList != null && filteredVillageList.size()!=0){
                filteredSHGList = filteredVillageList.stream().filter(location -> location.getVillage().contains(participantVillageName)).distinct().collect(Collectors.toList());
                SHGNamesList.clear();
                SHGNamesList = filteredSHGList.stream().map(Locations::getShg).distinct().collect(Collectors.toList());
                SHGSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, SHGNamesList);
                SHGSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SHGSpinner.setAdapter(SHGSpinnerAdapter);
                SHGSpinner.setSelection(0, true);
                View viewSpinner = SHGSpinner.getSelectedView();
                ((TextView)viewSpinner).setTextColor(Color.BLACK);
                ((TextView)viewSpinner).setTextColor(Color.BLACK);
            }
        }else if(parent.getId() == R.id.spinnerPanchayat){

            participantPanchayat = PanchayatNamesList.get(position);
            PanchayatSpinner.setSelection(position);
            ((TextView) view).setTextColor(Color.BLACK);

            if(locationsList!=null && locationsList.size()!=0) {
                filteredVillageList = locationsArrayList.stream().filter(location -> location.getPanchayat().contains(participantPanchayat)).distinct().collect(Collectors.toList());
                VillageNamesList.clear();
                VillageNamesList = filteredVillageList.stream().map(Locations::getVillage).distinct().collect(Collectors.toList());
                adapterForVillageNames = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, VillageNamesList);
                adapterForVillageNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VillageNameSpinner.setAdapter(adapterForVillageNames);
                VillageNameSpinner.setSelection(0, true);
                View v = VillageNameSpinner.getSelectedView();
                ((TextView)v).setTextColor(Color.BLACK);
            }else{
                locationsList = locationsArrayList;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Handle the server response
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void responseReceivedSuccessfully(String message) {

        Gson gson = new Gson();
        Type typeLocation = new TypeToken<ArrayList<Locations>>(){}.getType();
        JsonObject jsonObjectLocation = JsonParser.parseString(message).getAsJsonObject();
        try{
            locationsArrayList = gson.fromJson(jsonObjectLocation.get("message"), typeLocation);
            PanchayatNamesList = locationsArrayList.stream().map(Locations::getPanchayat).distinct().collect(Collectors.toList());
            VillageNamesList = locationsArrayList.stream().map(Locations::getVillage).distinct().collect(Collectors.toList());
            SHGNamesList = locationsArrayList.stream().map(Locations::getShg).distinct().collect(Collectors.toList());

            adapterForVillageNames = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, VillageNamesList);
            adapterForVillageNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VillageNameSpinner.setAdapter(adapterForVillageNames);
            VillageNameSpinner.setSelection(0, true);
            View v = VillageNameSpinner.getSelectedView();
            ((TextView)v).setTextColor(Color.BLACK);

            SHGSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, SHGNamesList);
            SHGSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SHGSpinner.setAdapter(SHGSpinnerAdapter);
            SHGSpinner.setSelection(0, true);
            View viewSpinner = SHGSpinner.getSelectedView();
            ((TextView)viewSpinner).setTextColor(Color.BLACK);

            panchayatSpinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, PanchayatNamesList);
            panchayatSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            PanchayatSpinner.setAdapter(panchayatSpinnerAdapter);
            PanchayatSpinner.setSelection(0, true);
            View viewSpinnerPanchayat = PanchayatSpinner.getSelectedView();
            ((TextView)viewSpinnerPanchayat).setTextColor(Color.BLACK);
        }catch(Exception e){
            if(jsonObjectLocation.get("message")!=null) {
                Toast.makeText(getActivity(), jsonObjectLocation.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }

        Type type = new TypeToken<ArrayList<UserLogin>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<UserLogin> userLogins;
        try{
            userLogins = gson.fromJson(jsonObject.get("data"), type);
            if(!userLogins.get(0).getUserName().equals("")){
                callServerRegisterParticipant();
            }
        }catch(Exception e){
            if(jsonObject.get("data")!=null) {
                Toast.makeText(getActivity(), jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
            }
        }

        Log.i("Message", "Success");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AuthorizationTokens", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("participant", getString(R.string.mp_token));
        editor.putString("coordinator", getString(R.string.mc_token));
        editor.putString("researcher", "");
        editor.apply();

        callServerRegisterParticipant();
    }

    @Override
    public void responseReceivedFailure(String message) {
        Log.i("Message", "Failure");
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String serverErrorResponse = jsonObject.get("exception").toString();
        if(serverErrorResponse.contains("frappe.exceptions.DuplicateEntryError")){
            participantUserNameET.setError("User Name already exits. Please give other user name.");
        }
    }
}
