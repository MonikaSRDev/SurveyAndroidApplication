package com.example.mithraapplication.Adapters;

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

import java.io.Serializable;
import java.util.ArrayList;

public class DiseasesProfileAdapter extends RecyclerView.Adapter<DiseasesProfileAdapter.ViewHolder> {
    private ArrayList<DiseasesProfile>  diseasesProfileArrayList = new ArrayList<>();
    private ArrayList<DiseasesProfile>  userEnteredDiseasesProfileArrayList = new ArrayList<>();
    private DiseasesProfile newDiseaseProfile;
    private Context context;

    @NonNull
    @Override
    public DiseasesProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.disease_profile_row, parent, false);

        return new ViewHolder(view);    }

    public DiseasesProfileAdapter(Context context, ArrayList<DiseasesProfile> diseasesProfileArrayList){
        this.diseasesProfileArrayList = diseasesProfileArrayList;
        this.context = context;
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

        newDiseaseProfile = new DiseasesProfile();

        userEnteredDiseasesProfileArrayList.add(newDiseaseProfile);

        DiseasesProfile diseasesProfile = diseasesProfileArrayList.get(position);

        holder.diseaseTV.setText(diseasesProfile.getDiseaseName());
        holder.diagnosedAgeTV.setText(diseasesProfile.getDiagnosedAge());
        holder.receivedTreatmentTV.setText(diseasesProfile.getReceivedTreatment());
        holder.limitActivitiesTV.setText(diseasesProfile.getLimitActivities());

        if(!diseasesProfile.getSpecifyDisease().equals("NULL")){
            holder.specifyDiseaseLinearLayout.setVisibility(View.VISIBLE);
            holder.specifyDiseaseTV.setText(diseasesProfile.getSpecifyDisease());
        }

        onClickOfYesDiseaseButton(holder);
        onClickOfNoDiseaseButton(holder);
//        onClickOfYesDiagnosedButton(holder);
//        onClickOfNoDiagnosedButton(holder);
        onClickOfYesReceivedTreatmentButton(holder);
        onClickOfNoReceivedTreatmentButton(holder);
        onClickOfYesLimitActivitiesButton(holder);
        onClickOfNoLimitActivitiesButton(holder);
        getDiagnosedAgeOfParticipant(holder);
        getSpecificDiseaseOfParticipant(holder);
    }

    private void onClickOfYesDiseaseButton(ViewHolder holder) {
        holder.yesDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("Yes");
                holder.yesDiseaseButton.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.expandableConstraintLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onClickOfNoDiseaseButton(ViewHolder holder) {
        holder.noDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("No");
                holder.noDiseaseButton.setBackgroundResource(R.drawable.selected_no_button);
                holder.yesDiseaseButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.expandableConstraintLayout.setVisibility(View.GONE);
            }
        });
    }

//    private void onClickOfYesDiagnosedButton(ViewHolder holder) {
//        holder.yesDiagnosedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("Yes");
//                holder.yesDiagnosedButton.setBackgroundResource(R.drawable.selected_yes_button);
//                holder.noDiagnosedButton.setBackgroundResource(R.drawable.yes_no_button);
//            }
//        });
//    }
//
//    private void onClickOfNoDiagnosedButton(ViewHolder holder) {
//        holder.noDiagnosedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosed("No");
//                holder.yesDiagnosedButton.setBackgroundResource(R.drawable.yes_no_button);
//                holder.noDiagnosedButton.setBackgroundResource(R.drawable.selected_no_button);
//            }
//        });
//    }

    private void onClickOfYesReceivedTreatmentButton(ViewHolder holder) {
        holder.yesReceivedTreatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setReceivedTreatment("Yes");
                holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
            }
        });
    }

    private void onClickOfNoReceivedTreatmentButton(ViewHolder holder) {
        holder.noReceivedTreatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setReceivedTreatment("No");
                holder.yesReceivedTreatmentButton.setBackgroundResource(R.drawable.yes_no_button);
                holder.noReceivedTreatmentButton.setBackgroundResource(R.drawable.selected_no_button);
            }
        });
    }

    private void onClickOfYesLimitActivitiesButton(ViewHolder holder) {
        holder.yesLimitActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setLimitActivities("Yes");
                holder.yesLimitActivities.setBackgroundResource(R.drawable.selected_yes_button);
                holder.noLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
            }
        });
    }

    private void onClickOfNoLimitActivitiesButton(ViewHolder holder) {
        holder.noLimitActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setLimitActivities("No");
                holder.yesLimitActivities.setBackgroundResource(R.drawable.yes_no_button);
                holder.noLimitActivities.setBackgroundResource(R.drawable.selected_no_button);
            }
        });
    }

    private void getDiagnosedAgeOfParticipant(ViewHolder holder){
        holder.diagnosedAgeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String diagnosedAge = holder.diagnosedAgeET != null ? holder.diagnosedAgeET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setDiagnosedAge(diagnosedAge);
            }
        });
    }

    private void getSpecificDiseaseOfParticipant(ViewHolder holder){
        holder.specifyDiseaseET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String specificDisease = holder.specifyDiseaseET != null ? holder.specifyDiseaseET.getText().toString() : "NULL";
                userEnteredDiseasesProfileArrayList.get(holder.getAbsoluteAdapterPosition()).setSpecifyDisease(specificDisease);
            }
        });
    }

    public void sendDataToActivity(){
        Intent intent = new Intent("DiseasesProfileData");
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)userEnteredDiseasesProfileArrayList);
        intent.putExtra("ParticipantDiseaseData",args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
