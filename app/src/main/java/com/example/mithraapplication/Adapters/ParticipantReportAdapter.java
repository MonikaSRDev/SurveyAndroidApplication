package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.ParticipantReport;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class ParticipantReportAdapter extends RecyclerView.Adapter<ParticipantReportAdapter.ViewHolder> {

    private ArrayList<ParticipantReport> participantReportArrayList = null;
    private Context context;

    @NonNull
    @Override
    public ParticipantReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_report_status_row, parent, false);

        return new ParticipantReportAdapter.ViewHolder(view);
    }

    public ParticipantReportAdapter(Context context, ArrayList<ParticipantReport> participantReportArrayList){
        this.context = context;
        this.participantReportArrayList = participantReportArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView surveyName, startTimeTV, startTimeVal, endTimeTV, endTimeVal, scoreTV, scoreVal, modulesAssignedTV,
            modulesAssignedVal, activityCompletedTV, activityCompletedVal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            surveyName = itemView.findViewById(R.id.cardNameTV);
            startTimeTV = itemView.findViewById(R.id.startTimeTV);
            startTimeVal = itemView.findViewById(R.id.startTimeValTV);
            endTimeTV = itemView.findViewById(R.id.endTimeTV);
            endTimeVal = itemView.findViewById(R.id.endTimeValTV);
            scoreTV = itemView.findViewById(R.id.scoreStatusTV);
            scoreVal = itemView.findViewById(R.id.scoreStatusValTV);
            modulesAssignedTV = itemView.findViewById(R.id.modulesAssignedTV);
            modulesAssignedVal = itemView.findViewById(R.id.modulesAssignedValTV);
            activityCompletedTV = itemView.findViewById(R.id.activityCompletedTV);
            activityCompletedVal = itemView.findViewById(R.id.activityCompletedValTV);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantReportAdapter.ViewHolder holder, int position) {
        ParticipantReport participantReport = participantReportArrayList.get(position);

        holder.surveyName.setText(participantReport.getSurveyName());
        holder.startTimeVal.setText(participantReport.getSurveyStart());
        holder.endTimeVal.setText(participantReport.getSurveyStop());
        holder.scoreVal.setText(participantReport.getSurveyScore());
        holder.modulesAssignedVal.setText(participantReport.getModulesAssigned());
        holder.activityCompletedVal.setText(participantReport.getActivityCompleted());

    }

    @Override
    public int getItemCount() {
        return participantReportArrayList.size();
    }
}
