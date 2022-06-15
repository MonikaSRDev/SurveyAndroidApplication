package com.example.mithraapplication;

import android.content.Intent;
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

import com.example.mithraapplication.ModelClasses.SocioDemography;

import java.util.Locale;

public class SocioDemographyFragment extends Fragment {

    private Button nextButton, unmarriedButton, marriedButton, nuclearButton, jointButton;
    private TextView educationYearsTV, maritalStatusTV, occupationTV, nearestPHCTV, religionTV, familyIncomeTV,
            casteTV, familyMembersTV, earningMembersTV, CBOMeetingsTV, familyTypeTV, associationDurationTV;
    private EditText educationYearsET, occupationET, nearestPHCET, religionET, familyIncomeET,
            casteET, familyMembersET, earningMembersET, CBOMeetingsET, associationDurationET;
    private String  participantMaritalStatus = "NULL", participantFamilyType = "NULL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_socio_demography_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
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
        nearestPHCTV = view.findViewById(R.id.nearestPHCTV);
        religionTV = view.findViewById(R.id.religionTV);
        familyIncomeTV = view.findViewById(R.id.familyIncomeTV);
        casteTV = view.findViewById(R.id.casteTV);
        familyMembersTV = view.findViewById(R.id.familyMembersTV);
        earningMembersTV = view.findViewById(R.id.earningMembersTV);
        CBOMeetingsTV = view.findViewById(R.id.CBOMeetingsTV);
        familyTypeTV = view.findViewById(R.id.familyTypeTV);
        associationDurationTV = view.findViewById(R.id.associationDurationTV);

        educationYearsET = view.findViewById(R.id.educationYearsET);
        occupationET = view.findViewById(R.id.occupationET);
        nearestPHCET = view.findViewById(R.id.nearestPHCET);
        religionET = view.findViewById(R.id.religionET);
        familyIncomeET = view.findViewById(R.id.familyIncomeET);
        casteET = view.findViewById(R.id.casteET);
        familyMembersET = view.findViewById(R.id.familyMembersET);
        earningMembersET = view.findViewById(R.id.earningMembersET);
        CBOMeetingsET = view.findViewById(R.id.CBOMeetingsET);
        associationDurationET = view.findViewById(R.id.associationDurationET);

        nextButton = view.findViewById(R.id.socioNextButton);
        unmarriedButton = view.findViewById(R.id.unmarriedButton);
        marriedButton = view.findViewById(R.id.marriedButton);
        nuclearButton = view.findViewById(R.id.nuclearButton);
        jointButton = view.findViewById(R.id.jointButton);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        educationYearsTV.setText(R.string.years_of_education);
        maritalStatusTV.setText(R.string.marital_status);
        occupationTV.setText(R.string.occupation);
        nearestPHCTV.setText(R.string.nearest_phc);
        religionTV.setText(R.string.religion);
        familyIncomeTV.setText(R.string.total_family_income_per_month);
        casteTV.setText(R.string.caste);
        familyMembersTV.setText(R.string.number_of_family_members);
        earningMembersTV.setText(R.string.earning_members);
        CBOMeetingsTV.setText(R.string.CBOMeetings);
        familyTypeTV.setText(R.string.type_of_family);
        associationDurationTV.setText(R.string.association_duration);

        nextButton.setText(R.string.next);
        unmarriedButton.setText(R.string.unmarried);
        marriedButton.setText(R.string.married);
        nuclearButton.setText(R.string.nuclear);
        jointButton.setText(R.string.joint);
    }


    /**
     * Description : This method is used to get the selected marital status
     */
    private void getSelectedMaritalStatus(){
        unmarriedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantMaritalStatus = "Unmarried";
                unmarriedButton.setBackgroundResource(R.drawable.selected_button);
                unmarriedButton.setTextColor(getResources().getColor(R.color.white));
                marriedButton.setTextColor(getResources().getColor(R.color.text_color));
                marriedButton.setBackgroundResource(R.drawable.inputs_background);
            }
        });

        marriedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantMaritalStatus = "Married";
                unmarriedButton.setBackgroundResource(R.drawable.inputs_background);
                marriedButton.setBackgroundResource(R.drawable.selected_button);
                marriedButton.setTextColor(getResources().getColor(R.color.white));
                unmarriedButton.setTextColor(getResources().getColor(R.color.text_color));
            }
        });
    }

    /**
     * Description : This method is used to get the selected family type
     */
    private void getSelectedFamilyType(){
        nuclearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantFamilyType = "Nuclear";
                nuclearButton.setBackgroundResource(R.drawable.selected_button);
                nuclearButton.setTextColor(getResources().getColor(R.color.white));
                jointButton.setTextColor(getResources().getColor(R.color.text_color));
                jointButton.setBackgroundResource(R.drawable.inputs_background);
            }
        });

        jointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantFamilyType = "Joint";
                nuclearButton.setBackgroundResource(R.drawable.inputs_background);
                jointButton.setBackgroundResource(R.drawable.selected_button);
                jointButton.setTextColor(getResources().getColor(R.color.white));
                nuclearButton.setTextColor(getResources().getColor(R.color.text_color));
            }
        });
    }

    /**
     * Description : This method is used to get the data entered by the user
     */
    private void getUserEnteredData(){
        int yearsOfEducation = !educationYearsET.getText().toString().isEmpty() ? Integer.parseInt(educationYearsET.getText().toString()): 0;
        int numFamilyMembers = !familyMembersET.getText().toString().isEmpty() ? Integer.parseInt(familyMembersET.getText().toString()): 0;
        int totalFamilyIncome = !familyIncomeET.getText().toString().isEmpty() ? Integer.parseInt(familyIncomeET.getText().toString()): 0;
        int totalEarningMembers = !earningMembersET.getText().toString().isEmpty() ? Integer.parseInt(earningMembersET.getText().toString()): 0;

        String religion = !religionET.getText().toString().isEmpty() ? religionET.getText().toString(): "NULL";
        String caste = !casteET.getText().toString().isEmpty() ? casteTV.getText().toString(): "NULL";
        String occupation = !occupationET.getText().toString().isEmpty() ? occupationET.getText().toString(): "NULL";
        String nearestPHC = !nearestPHCET.getText().toString().isEmpty() ? nearestPHCET.getText().toString(): "NULL";
        String associationDuration = !associationDurationET.getText().toString().isEmpty() ? associationDurationET.getText().toString(): "NULL";
        String CBOMeetings = !CBOMeetingsET.getText().toString().isEmpty() ? CBOMeetingsET.getText().toString(): "NULL";

        SocioDemography socioDemographyObject = new SocioDemography();
        socioDemographyObject.setYearsOfEducation(yearsOfEducation);
        socioDemographyObject.setNumFamilyMembers(numFamilyMembers);
        socioDemographyObject.setFamilyIncome(totalFamilyIncome);
        socioDemographyObject.setNumEarningFamMembers(totalEarningMembers);
        socioDemographyObject.setReligion(religion);
        socioDemographyObject.setCaste(caste);
        socioDemographyObject.setOccupation(occupation);
        socioDemographyObject.setNearestPHC(nearestPHC);
        socioDemographyObject.setAssociationDuration(associationDuration);
        socioDemographyObject.setCBOMeetings(CBOMeetings);
    }

    /**
     * Description : This method is called when user clicks on the next button
     */
    private void onClickOfNextButton(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserEnteredData();
                moveToDiseaseProfileTab();
            }
        });
    }

    private void moveToDiseaseProfileTab(){
        ((ProfileScreen)getActivity()).setupSelectedTabFragment(2);
    }


}
