package com.example.mithraapplication;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.google.gson.Gson;

import java.util.Locale;

public class AddNewParticipantActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText participantNameET, participantAgeET, participantPhoneNumberET, participantUserNameET, participantPasswordET, participantConfirmPasswordET;
    private Button registerButton, maleButton, femaleButton, othersButton, englishButton, kannadaButton;
    private TextView participantsTV, participateAgeTV, participatePhoneNumberTV, participateUserNameTV, participatePasswordTV, participantConfirmPasswordTV, VillageNameTV, SHGAssociationTV, dashboardTV, participantNameTV, genderTV, addParticipantTV;
    private Spinner VillageNameSpinner, SHGSpinner;
    private String participantName, participantUserName, participantPassword;
    private String participantVillageName;
    private String participantSHGAssociation;
    private int participantAge;
    private String participantGender;
    private String participantPhoneNum;
    private String[] SHGGroups = {"SHG 1", "SHG 2", "SHG 3"};
    private String[] VillageNames = {"Village 1", "Village 2", "Village 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_participant_screen);
        RegisterViews();
        getSelectedGender();
        onClickRegisterButton();
        onClickOfLanguageButton();
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews(){
        participantNameET = findViewById(R.id.participantNameET);
        participantAgeET = findViewById(R.id.participantAgeET);
        participantPhoneNumberET = findViewById(R.id.participantPhoneNumberET);
        participantUserNameET = findViewById(R.id.participantUserNameET);
        participantPasswordET = findViewById(R.id.participantPasswordET);
        participantConfirmPasswordET = findViewById(R.id.participantConfirmPasswordET);

        participantsTV = findViewById(R.id.participantsTV);
        participateAgeTV = findViewById(R.id.ageTextView);
        participatePhoneNumberTV = findViewById(R.id.phoneNumberTV);
        participateUserNameTV = findViewById(R.id.participantUserName);
        participatePasswordTV = findViewById(R.id.passwordTV);
        participantConfirmPasswordTV = findViewById(R.id.confirmPasswordTV);
        VillageNameTV = findViewById(R.id.villageNameTV);
        SHGAssociationTV = findViewById(R.id.SHGSpinner);
        dashboardTV = findViewById(R.id.dashboardTV);
        participantNameTV = findViewById(R.id.participantName);
        genderTV = findViewById(R.id.genderTV);
        addParticipantTV = findViewById(R.id.addParticipantTV);


        registerButton = findViewById(R.id.registerButton);
        maleButton = findViewById(R.id.maleOption);
        femaleButton = findViewById(R.id.femaleOption);
        femaleButton.setBackgroundResource(R.drawable.selected_button);
        femaleButton.setTextColor(getResources().getColor(R.color.white));
        participantGender = "Female";
        othersButton = findViewById(R.id.othersOption);

        englishButton = findViewById(R.id.englishButton);
        englishButton.setBackgroundResource(R.drawable.left_toggle_selected_button);
        englishButton.setTextColor(getResources().getColor(R.color.black));

        kannadaButton = findViewById(R.id.kannadaButton);

        VillageNameSpinner = findViewById(R.id.villageNameSpinner);
        VillageNameSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterForVillageNames = new ArrayAdapter(this, android.R.layout.simple_spinner_item, VillageNames);
        adapterForVillageNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VillageNameSpinner.setAdapter(adapterForVillageNames);
        VillageNameSpinner.setSelection(0, true);
        View v = VillageNameSpinner.getSelectedView();
        ((TextView)v).setTextColor(Color.BLACK);

        SHGSpinner = findViewById(R.id.spinnerSHG);
        SHGSpinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, SHGGroups);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SHGSpinner.setAdapter(ad);
        SHGSpinner.setSelection(0, true);
        View view = SHGSpinner.getSelectedView();
        ((TextView)view).setTextColor(Color.BLACK);
    }

    /**
     * Description : This method is used to get the selected gender
     */
    private void getSelectedGender(){
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantGender = "Male";
                maleButton.setBackgroundResource(R.drawable.selected_button);
                maleButton.setTextColor(getResources().getColor(R.color.white));
                femaleButton.setTextColor(getResources().getColor(R.color.text_color));
                othersButton.setTextColor(getResources().getColor(R.color.text_color));
                femaleButton.setBackgroundResource(R.drawable.inputs_background);
                othersButton.setBackgroundResource(R.drawable.inputs_background);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantGender = "Female";
                maleButton.setBackgroundResource(R.drawable.inputs_background);
                femaleButton.setBackgroundResource(R.drawable.selected_button);
                femaleButton.setTextColor(getResources().getColor(R.color.white));
                maleButton.setTextColor(getResources().getColor(R.color.text_color));
                othersButton.setTextColor(getResources().getColor(R.color.text_color));
                othersButton.setBackgroundResource(R.drawable.inputs_background);
            }
        });

        othersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantGender = "Others";
                maleButton.setBackgroundResource(R.drawable.inputs_background);
                femaleButton.setBackgroundResource(R.drawable.inputs_background);
                othersButton.setBackgroundResource(R.drawable.selected_button);
                othersButton.setTextColor(getResources().getColor(R.color.white));
                maleButton.setTextColor(getResources().getColor(R.color.text_color));
                femaleButton.setTextColor(getResources().getColor(R.color.text_color));
            }
        });
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
                    RegisterParticipant registerParticipant = new RegisterParticipant();
                    registerParticipant.setParticipantName(participantName);
                    registerParticipant.setParticipantGender(participantGender);
                    registerParticipant.setParticipantAge(String.valueOf(participantAge));
                    registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
                    registerParticipant.setParticipantVillageName(participantVillageName);
                    registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
                    registerParticipant.setParticipantUserName(participantUserName);
                    registerParticipant.setParticipantPassword(participantPassword);
                    Gson gson = new Gson();
                    ServerRequestAndResponse object = new ServerRequestAndResponse();
                    object.getJsonRequest(AddNewParticipantActivity.this);
//                object.postJsonRequest(AddNewParticipantActivity.this, gson.toJson(registerParticipant));
                }
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButton.setBackgroundResource(R.drawable.left_toggle_selected_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                kannadaButton.setBackgroundResource(R.drawable.right_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButton.setBackgroundResource(R.drawable.right_toggle_selected_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                englishButton.setBackgroundResource(R.drawable.left_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("kn");
            }
        });
    }

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
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        participantUserNameET.setHint(R.string.user_name);
        participantPasswordET.setHint(R.string.password);
        participantConfirmPasswordET.setHint(R.string.confirm_password);
        participantNameET.setHint(R.string.participant_name);

        dashboardTV.setText(R.string.dashboard);
        participantsTV.setText(R.string.participants);
        participateAgeTV.setText(R.string.age);
        participatePhoneNumberTV.setText(R.string.phone_number);
        participateUserNameTV.setText(R.string.user_name);
        participatePasswordTV.setText(R.string.password);
        participantConfirmPasswordTV.setText(R.string.confirm_password);
        VillageNameTV.setText(R.string.village_name);
        SHGAssociationTV.setText(R.string.shg_association);
        participantNameTV.setText(R.string.name_small_case);
        genderTV.setText(R.string.gender);
        addParticipantTV.setText(R.string.add_new_participant);

        maleButton.setText(R.string.male);
        femaleButton.setText(R.string.female);
        othersButton.setText(R.string.others);

        super.onConfigurationChanged(newConfig);
        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {

        } else{
        }
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
     * Description : This method is used to get the selected item from the dropdown list
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerSHG){
            participantSHGAssociation = SHGGroups[position];
            SHGSpinner.setSelection(position);
            ((TextView) view).setTextColor(Color.BLACK);
        }else if(parent.getId() == R.id.villageNameSpinner){
            participantVillageName = VillageNames[position];
            VillageNameSpinner.setSelection(position);
            ((TextView) view).setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
