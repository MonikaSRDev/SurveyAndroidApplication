package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class DashboardVerticalParticipantsAdapter extends RecyclerView.Adapter<DashboardVerticalParticipantsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParticipantDetails> participantArrayList;
    private onItemClickListener itemClickListener;

    @NonNull
    @Override
    public DashboardVerticalParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_vertical_row, parent, false);
        return new ViewHolder(view);
    }

    public DashboardVerticalParticipantsAdapter(Context context, ArrayList<ParticipantDetails> participantArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantArrayList = participantArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardVerticalParticipantsAdapter.ViewHolder holder, int position) {

        ParticipantDetails participantDetails = participantArrayList.get(position);

        holder.participantID.setText(participantDetails.getRegistration());
        holder.participantName.setText(participantDetails.getFull_name());
        holder.participantAge.setText(participantDetails.getAge());
        holder.participantVillage.setText(participantDetails.getVillage_name());

        String enrollPercentage = participantDetails.getEnroll();
        if(enrollPercentage!=null && enrollPercentage.equalsIgnoreCase("yes")){
            holder.enrollmentProgressBar.setProgress(100);
            holder.enrollmentPercentage.setText("100%");
        }else if(enrollPercentage!=null && enrollPercentage.equalsIgnoreCase("66")){
            holder.enrollmentProgressBar.setProgress(66);
            holder.enrollmentPercentage.setText("66%");
        }else if(enrollPercentage!=null && enrollPercentage.equalsIgnoreCase("33")){
            holder.enrollmentProgressBar.setProgress(33);
            holder.enrollmentPercentage.setText("33%");
        }else{
            holder.enrollmentProgressBar.setProgress(0);
            holder.enrollmentPercentage.setText("0%");
        }

        holder.surveyProgressBar.setProgress(10);
        holder.surveyPercentage.setText("10%");

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(participantDetails));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView participantID, participantName, participantAge, participantVillage, enrollmentPercentage, surveyPercentage;
        ProgressBar enrollmentProgressBar, surveyProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            participantID = itemView.findViewById(R.id.participantIDValTV);
            participantName = itemView.findViewById(R.id.partDetailsName);
            participantAge = itemView.findViewById(R.id.partDetailsAge);
            participantVillage = itemView.findViewById(R.id.partDetailsVillage);
            enrollmentPercentage = itemView.findViewById(R.id.enrollmentStatusPercentage);
            enrollmentProgressBar = itemView.findViewById(R.id.enrollmentProgressBar);
            surveyPercentage = itemView.findViewById(R.id.surveyStatusPercentage);
            surveyProgressBar = itemView.findViewById(R.id.surveyProgressBar);
        }
    }

    public interface onItemClickListener{
        void onItemClick(ParticipantDetails participantDetails);
    }

    @Override
    public int getItemCount() {
        return participantArrayList.size();
    }
}
