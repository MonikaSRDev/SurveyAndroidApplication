package com.example.mithraapplication.Adapters;

import static com.example.mithraapplication.ParticipantProfileScreen.isLanguageSelected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class DiseasesProfileAdapter extends RecyclerView.Adapter<DiseasesProfileAdapter.ViewHolder> {
    private final ArrayList<DiseasesProfile>  diseasesProfileArrayList;
    private final ArrayList<DiseasesProfile>  userEnteredDiseasesProfileArrayList = new ArrayList<>();
    private final Context context;
    private final String isEditable;

    @NonNull
    @Override
    public DiseasesProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.disease_profile_row, parent, false);

        return new ViewHolder(view);    }

    public DiseasesProfileAdapter(Context context, ArrayList<DiseasesProfile> diseasesProfileArrayList, String isEditable){
        this.diseasesProfileArrayList = diseasesProfileArrayList;
        this.context = context;
        this.isEditable = isEditable;
    }

    @Override
    public int getItemCount() {
        return diseasesProfileArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView diseaseTV, diagnosedAgeTV, receivedTreatmentTV, limitActivitiesTV, specifyDiseaseTV;
        EditText diagnosedAgeET, specifyDiseaseET;
        Button yesDiseaseButton, noDiseaseButton, yesReceivedTreatmentButton, noReceivedTreatmentButton, yesLimitActivities,  noLimitActivities;
        ConstraintLayout expandableConstraintLayout;
        LinearLayout specifyDiseaseLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            diseaseTV = itemView.findViewById(R.id.diseaseTV);
            diagnosedAgeTV = itemView.findViewById(R.id.diagnosedAgeTV);
            receivedTreatmentTV = itemView.findViewById(R.id.treatmentReceivedTV);
            limitActivitiesTV = itemView.findViewById(R.id.limitActivitiesTV);
            specifyDiseaseTV = itemView.findViewById(R.id.specifyDiseaseTV);

            diagnosedAgeET = itemView.findViewById(R.id.diagnosedAgeET);
            specifyDiseaseET = itemView.findViewById(R.id.specifyDiseaseET);

            yesDiseaseButton = itemView.findViewById(R.id.yesDiseaseButton);
            noDiseaseButton = itemView.findViewById(R.id.noDiseaseButton);
            yesReceivedTreatmentButton = itemView.findViewById(R.id.yesTreatmentButton);
            noReceivedTreatmentButton = itemView.findViewById(R.id.noTreatmentButton);
            yesLimitActivities = itemView.findViewById(R.id.yesLimitActivitiesButton);
            noLimitActivities = itemView.findViewById(R.id.noLimitActivitiesButton);

            expandableConstraintLayout = itemView.findViewById(R.id.expandableConstraintLayout);
            expandableConstraintLayout.setVisibility(View.GONE);

            specifyDiseaseLinearLayout = itemView.findViewById(R.id.specifyDiseaseLinearLayout);
            specifyDiseaseLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DiseasesProfileAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        DiseasesProfile newDiseaseProfile = new DiseasesProfile();

        userEnteredDiseasesProfileArrayList.add(newDiseaseProfile);

        DiseasesProfile diseasesProfile = diseasesProfileArrayList.get(position);

        userEnteredDiseasesProfileArrayList.get(position).setDiseaseName(diseasesProfile.getDiseaseName());
        userEnteredDiseasesProfileArrayList.get(position).setDiseaseNameKannada(diseasesProfile.getDiseaseNameKannada());
        userEnteredDiseasesProfileArrayList.get(position).setLimitActivities(diseasesProfile.getLimitActivities());
        userEnteredDiseasesProfileArrayList.get(position).setReceivedTreatment(diseasesProfile.getReceivedTreatment());
        userEnteredDiseasesProfileArrayList.get(position).setSpecifyDisease(diseasesProfile.getSpecifyDisease());
        userEnteredDiseasesProfileArrayList.get(position).setDiagnosedAge(diseasesProfile.getDiagnosedAge());

        if(isLanguageSelected.equals("kn")){
            holder.diseaseTV.setText(diseasesProfile.getDiseaseNameKannada());
        }else{
            holder.diseaseTV.setText(diseasesProfile.getDiseaseName());
        }
        holder.diagnosedAgeTV.setText(R.string.diagnosed_age);
        holder.receivedTreatmentTV.setText(R.string.received_treatment);
        holder.limitActivitiesTV.setText(R.string.limit_activities);

        if(diseasesProfile.getDiseaseName().equals("ANY OTHER DISEASES")){
            holder.specifyDiseaseLinearLayout.setVisibility(View.VISIBLE);
            holder.specifyDiseaseTV.setText(R.string.specify_disease);
        }

        enableDisableViews(isEditable == null || !isEditable.equalsIgnoreCase("false"), holder);
        displayDiseaseProfileData(holder, diseasesProfile);
        onClickOfYesDiseaseButton(holder, diseasesProfile);
        onClickOfNoDiseaseButton(holder, diseasesProfile);
        onClickOfYesReceivedTreatmentButton(holder, diseasesProfile);
        onClickOfNoReceivedTreatmentButton(holder, diseasesProfile);
        onClickOfYesLimitActivitiesButton(holder, diseasesProfile);
        onClickOfNoLimitActivitiesButton(holder, diseasesProfile);
        getDiagnosedAgeOfParticipant(holder, diseasesProfile);
        getSpecificDiseaseOfParticipant(holder, diseasesProfile);
    }

    /**
     * Description : This method is used to display the profile data entered by the user for viewing.
     */
    private void displayDiseaseProfileData(ViewHolder holder, DiseasesProfile diseasesProfile){
//        if(isEditable!=null && !isEditable.equals("true")){
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed(diseasesProfile.getDiagnosed());
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diseasesProfile.getDiagnosedAge());
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setReceivedTreatment(diseasesProfile.getReceivedTreatment());
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setLimitActivities(diseasesProfile.getLimitActivities());
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(diseasesProfile.getSpecifyDisease());

            if(diseasesProfile.getDiagnosed().equalsIgnoreCase("yes")){
                holder.yesDiseaseButton.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.expandableConstraintLayout.setVisibility(View.VISIBLE);
            }else if(diseasesProfile.getDiagnosed().equalsIgnoreCase("no")){
                holder.noDiseaseButton.setBackgroundResource(R.drawable.selected_no_button);
                holder.yesDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.expandableConstraintLayout.setVisibility(View.GONE);
            }else{
                holder.noDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.yesDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.expandableConstraintLayout.setVisibility(View.GONE);
            }

            if(!diseasesProfile.getDiagnosedAge().equalsIgnoreCase("null") && !diseasesProfile.getDiagnosedAge().equalsIgnoreCase("How old were you when this was diagnosed?")){
                holder.diagnosedAgeET.setText(diseasesProfile.getDiagnosedAge());
            }else{
                holder.diagnosedAgeET.setText("");
            }

            if(diseasesProfile.getReceivedTreatment().equalsIgnoreCase("yes")){
                holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
            }else if(diseasesProfile.getReceivedTreatment().equalsIgnoreCase("no")){
                holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_no_button);
            }else{
                holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
            }

            if(diseasesProfile.getLimitActivities().equalsIgnoreCase("yes")){
                holder.yesLimitActivities.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
            }else if(diseasesProfile.getLimitActivities().equalsIgnoreCase("no")){
                holder.yesLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
                holder.noLimitActivities.setBackgroundResource(R.drawable.selected_no_button);
            }else{
                holder.yesLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
                holder.noLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
            }

            if(!diseasesProfile.getSpecifyDisease().equalsIgnoreCase("null") && !diseasesProfile.getSpecifyDisease().equalsIgnoreCase("Specify other disease")){
                holder.specifyDiseaseET.setText(diseasesProfile.getSpecifyDisease());
            }else{
                holder.specifyDiseaseET.setText("");
            }
//        }
    }

    /**
     * Description : This method is used to enable and disable the views based on the isEditable.
     */
    private void enableDisableViews(boolean isEdit, ViewHolder holder){
        if(isEdit){
            holder.diagnosedAgeET.setFocusable(true);
            holder.diagnosedAgeET.setClickable(true);
            holder.specifyDiseaseET.setFocusable(true);
            holder.specifyDiseaseET.setClickable(true);
            holder.diagnosedAgeET.setFocusableInTouchMode(true);
            holder.specifyDiseaseET.setFocusableInTouchMode(true);

            holder.yesDiseaseButton.setEnabled(true);
            holder.noDiseaseButton.setEnabled(true);
            holder.yesLimitActivities.setEnabled(true);
            holder.noLimitActivities.setEnabled(true);
            holder.yesReceivedTreatmentButton.setEnabled(true);
            holder.noReceivedTreatmentButton.setEnabled(true);
        }else{
            holder.diagnosedAgeET.setFocusable(false);
            holder.diagnosedAgeET.setClickable(false);
            holder.specifyDiseaseET.setFocusable(false);
            holder.specifyDiseaseET.setClickable(false);

            holder.yesDiseaseButton.setEnabled(false);
            holder.noDiseaseButton.setEnabled(false);
            holder.yesLimitActivities.setEnabled(false);
            holder.noLimitActivities.setEnabled(false);
            holder.yesReceivedTreatmentButton.setEnabled(false);
            holder.noReceivedTreatmentButton.setEnabled(false);
        }
    }

    /**
     * Description : This method is handles the onclick action on "YesDiseaseButton"
     */
    private void onClickOfYesDiseaseButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.yesDiseaseButton.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("yes");

            holder.yesDiseaseButton.setBackgroundResource(R.drawable.selected_yes_button);
            holder.noDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
            holder.expandableConstraintLayout.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Description : This method is handles the onclick action on "NoDiseaseButton"
     */
    private void onClickOfNoDiseaseButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.noDiseaseButton.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("no");

            holder.noDiseaseButton.setBackgroundResource(R.drawable.selected_no_button);
            holder.yesDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
            holder.expandableConstraintLayout.setVisibility(View.GONE);
        });
    }

    /**
     * Description : This method is handles the onclick action on "YesReceivedTreatmentButton"
     */
    private void onClickOfYesReceivedTreatmentButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.yesReceivedTreatmentButton.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setReceivedTreatment("yes");

            holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_yes_button);
            holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
        });
    }

    /**
     * Description : This method is handles the onclick action on "NoReceivedTreatmentButton"
     */
    private void onClickOfNoReceivedTreatmentButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.noReceivedTreatmentButton.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setReceivedTreatment("no");

            holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
            holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_no_button);
        });
    }

    /**
     * Description : This method is handles the onclick action on "YesLimitActivitiesButton"
     */
    private void onClickOfYesLimitActivitiesButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.yesLimitActivities.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setLimitActivities("yes");

            holder.yesLimitActivities.setBackgroundResource(R.drawable.selected_yes_button);
            holder.noLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
        });
    }

    /**
     * Description : This method is handles the onclick action on "NoLimitActivitiesButton"
     */
    private void onClickOfNoLimitActivitiesButton(ViewHolder holder, DiseasesProfile diseasesProfile) {
        holder.noLimitActivities.setOnClickListener(v -> {
            userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setLimitActivities("no");

            holder.yesLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
            holder.noLimitActivities.setBackgroundResource(R.drawable.selected_no_button);
        });
    }

    /**
     * Description : This method is used to get the disease diagnosed age of the participant
     */
    private void getDiagnosedAgeOfParticipant(ViewHolder holder, DiseasesProfile diseasesProfile){
        holder.diagnosedAgeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }
        });
    }

    /**
     * Description : This method is used to get the specific disease name from the participant if they have any disease other than the listed
     */
    private void getSpecificDiseaseOfParticipant(ViewHolder holder, DiseasesProfile diseasesProfile){
        holder.specifyDiseaseET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "null";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }
        });
    }

    /**
     * Description : This method is used to get the data entered by the user and send it to main activity on click of the save button
     */
    public void sendDataToActivity(){
        Intent intent = new Intent("DiseasesProfileData");
        Bundle args = new Bundle();
        args.putSerializable("ParticipantDiseaseList", userEnteredDiseasesProfileArrayList);
        intent.putExtra("ParticipantDiseaseData",args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
