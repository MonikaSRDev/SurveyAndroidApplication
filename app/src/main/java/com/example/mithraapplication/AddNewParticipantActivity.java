package com.example.mithraapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.PhoneNumber;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;

public class AddNewParticipantActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText participantNameET, participantAgeET, participantPhoneNumberET;
    private Button registerButton;
    private RadioGroup genderRadioButton;
    private RadioButton selectedGender;
    private Switch autoGenerateCredSwitch;
    private Spinner VillageNameSpinner, SHGSpinner;
    private int selectedGenderValue;
    private String participantName;
    private String participantVillageName;
    private String participantSHGAssociation;
    private int participantGender, participantAge, autoGenerateCred;
    private PhoneNumber participantPhoneNum;
    private String[] SHGGroups = {"SHG 1", "SHG 2", "SHG 3"};
    private String[] VillageNames = {"Village 1", "Village 2", "Village 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_participant_screen);
        RegisterViews();
        getSelectedGender();
        onClickRegisterButton();
    }

    private void RegisterViews(){
        participantNameET = findViewById(R.id.participantNameET);
        participantAgeET = findViewById(R.id.participantAgeET);
        participantPhoneNumberET = findViewById(R.id.participantPhoneNumberET);

        VillageNameSpinner = findViewById(R.id.villageNameSpinner);
        VillageNameSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterForVillageNames = new ArrayAdapter(this, android.R.layout.simple_spinner_item, VillageNames);
        adapterForVillageNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VillageNameSpinner.setAdapter(adapterForVillageNames);

        registerButton = findViewById(R.id.registerButton);
        genderRadioButton = findViewById(R.id.genderRadioGroup);
        autoGenerateCredSwitch = findViewById(R.id.username_password_switch);
        autoGenerateCredSwitch.setChecked(true);

        SHGSpinner = findViewById(R.id.spinnerSHG);
        SHGSpinner.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, SHGGroups);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SHGSpinner.setAdapter(ad);
    }

    private void getSelectedGender(){
        genderRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedGender = group.findViewById(checkedId);
                switch (selectedGender.getId()){
                    case R.id.maleRadioButton:  selectedGenderValue = 1;
                                                break;
                    case R.id.femaleRadioButton: selectedGenderValue = 2;
                                                break;
                    case R.id.othersRadioButton: selectedGenderValue = 0;
                                                break;
                }
            }
        });
    }

    private void onClickRegisterButton(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserEnteredData();
                RegisterParticipant registerParticipant = new RegisterParticipant();
                registerParticipant.setParticipantName(participantName);
                registerParticipant.setParticipantGender(participantGender);
                registerParticipant.setParticipantAge(participantAge);
                registerParticipant.setParticipantPhoneNumber(participantPhoneNum);
                registerParticipant.setParticipantVillageName(participantVillageName);
                registerParticipant.setParticipantSHGAssociation(participantSHGAssociation);
                registerParticipant.setAutoGenerateCred(autoGenerateCred);
            }
        });
    }

    /**
     * Description : Enable and disable the button
     */
    private void enableDisableButton(boolean isEnabled){
        if(isEnabled){
            registerButton.setEnabled(true);
            registerButton.setBackgroundResource(R.drawable.linear_gradient_color);
            registerButton.setTextColor(Color.parseColor("#ffffff"));
        }else{
            registerButton.setEnabled(false);
            registerButton.setBackgroundResource(R.color.text_color);
            registerButton.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void getUserEnteredData(){
        participantName = participantNameET.getText().toString();
        participantGender = selectedGenderValue;
        participantAge = !participantAgeET.getText().toString().isEmpty() ? Integer.parseInt(participantAgeET.getText().toString()): 0;
        String participantPhoneNumber = participantPhoneNumberET.getText().toString();
        participantPhoneNum = new PhoneNumber();
        participantPhoneNum.setNumber(participantPhoneNumber);
        if(autoGenerateCredSwitch.isChecked()){
            autoGenerateCred = 1;
        }else{
            autoGenerateCred = 0;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerSHG){
            participantSHGAssociation = SHGGroups[position];
            SHGSpinner.setSelection(position);
//            ((TextView) parent.getChildAt(position)).setTextColor(0x00000000);
        }else if(parent.getId() == R.id.villageNameSpinner){
            participantVillageName = VillageNames[position];
            VillageNameSpinner.setSelection(position);
//            ((TextView) parent.getChildAt(position)).setTextColor(0x00000000);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
