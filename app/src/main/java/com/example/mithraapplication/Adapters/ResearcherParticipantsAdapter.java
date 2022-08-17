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

public class ResearcherParticipantsAdapter extends RecyclerView.Adapter<ResearcherParticipantsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParticipantDetails> participantArrayList;
    private onItemClickListener itemClickListener;

    @NonNull
    @Override
    public ResearcherParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.researcher_participant_row, parent, false);
        return new ViewHolder(view);
    }

    public ResearcherParticipantsAdapter(Context context, ArrayList<ParticipantDetails> participantArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantArrayList = participantArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ResearcherParticipantsAdapter.ViewHolder holder, int position) {

        ParticipantDetails participantDetails = participantArrayList.get(position);

        holder.participantID.setText(participantDetails.getRegistration());
        holder.participantName.setText(participantDetails.getFull_name());
        holder.participantAge.setText(participantDetails.getAge());
        holder.participantVillage.setText(participantDetails.getVillage_name());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(participantDetails));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView participantID, participantName, participantAge, participantVillage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            participantID = itemView.findViewById(R.id.researcherParticipantIDTV);
            participantName = itemView.findViewById(R.id.researcherPartName);
            participantAge = itemView.findViewById(R.id.researcherPartAge);
            participantVillage = itemView.findViewById(R.id.researcherPartVillage);
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
