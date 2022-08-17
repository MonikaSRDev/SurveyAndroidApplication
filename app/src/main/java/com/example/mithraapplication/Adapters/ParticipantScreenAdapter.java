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
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParticipantScreenAdapter extends RecyclerView.Adapter<ParticipantScreenAdapter.ViewHolder> {

    private ArrayList<RegisterParticipant> participantArrayList;
    private Context context;
    private final onItemClickListener itemClickListener;
    private ArrayList<ParticipantDetails> registerParticipantsArrayList;

    @NonNull
    @Override
    public ParticipantScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_vertical_row, parent, false);
        return new ParticipantScreenAdapter.ViewHolder(view);
    }

    public ParticipantScreenAdapter(Context context, ArrayList<RegisterParticipant> participantArrayList, ArrayList<ParticipantDetails> registerParticipantsArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantArrayList = participantArrayList;
        this.itemClickListener = itemClickListener;
        this.registerParticipantsArrayList = registerParticipantsArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantScreenAdapter.ViewHolder holder, int position) {

        RegisterParticipant registerParticipant = participantArrayList.get(position);
        ParticipantDetails participantDetails = registerParticipantsArrayList.stream().filter(ParticipantDetails -> ParticipantDetails.getRegistration().equals(registerParticipant.getName())).findFirst().orElse(null);

        holder.participantDetailsName.setText(participantDetails.getFull_name());
        holder.participantDetailsAge.setText(participantDetails.getAge());
        holder.participantDetailsVillage.setText(participantDetails.getVillage_name());

        if(registerParticipant.getActive()!=null && participantDetails.getActive().equalsIgnoreCase("yes")){
            holder.statusView.setBackgroundColor(context.getResources().getColor(R.color.completed_color, context.getTheme()));
        }else{
            holder.statusView.setBackgroundColor(context.getResources().getColor(R.color.notEligibleColor, context.getTheme()));
        }

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

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(registerParticipant));

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View statusView;
        TextView participantDetailsName, participantDetailsAge, participantDetailsVillage, enrollmentPercentage, surveyPercentage;
        ProgressBar enrollmentProgressBar, surveyProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusView = itemView.findViewById(R.id.participantStatusView);
            participantDetailsName = itemView.findViewById(R.id.participantDetailsName);
            participantDetailsAge = itemView.findViewById(R.id.participantDetailsAge);
            participantDetailsVillage = itemView.findViewById(R.id.participantDetailsVillage);
            enrollmentPercentage = itemView.findViewById(R.id.enrollmentStatusPercentageParticipants);
            enrollmentProgressBar = itemView.findViewById(R.id.enrollmentStatusPBParticipants);
            surveyPercentage = itemView.findViewById(R.id.surveyStatusPercentageParticipants);
            surveyProgressBar = itemView.findViewById(R.id.surveyProgressBarParticipants);
        }
    }

    public interface onItemClickListener{
        void onItemClick(RegisterParticipant registerParticipant);
    }

    @Override
    public int getItemCount() {
        return participantArrayList.size();
    }
}
