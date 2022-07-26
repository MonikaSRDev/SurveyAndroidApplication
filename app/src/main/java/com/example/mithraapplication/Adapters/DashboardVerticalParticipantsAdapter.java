package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class DashboardVerticalParticipantsAdapter extends RecyclerView.Adapter<DashboardVerticalParticipantsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RegisterParticipant> participantArrayList;
    private onItemClickListener itemClickListener;

    @NonNull
    @Override
    public DashboardVerticalParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_vertical_row, parent, false);
        return new ViewHolder(view);
    }

    public DashboardVerticalParticipantsAdapter(Context context, ArrayList<RegisterParticipant> participantArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantArrayList = participantArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardVerticalParticipantsAdapter.ViewHolder holder, int position) {

        RegisterParticipant participantDetails = participantArrayList.get(position);

        holder.participantID.setText(participantDetails.getParticipantUserName()); //Need ID
        holder.participantName.setText(participantDetails.getParticipantName());
        holder.participantAge.setText(participantDetails.getParticipantAge());
        holder.participantVillage.setText(participantDetails.getParticipantVillageName());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(participantDetails));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView participantID, participantName, participantAge, participantVillage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            participantID = itemView.findViewById(R.id.participantIDValTV);
            participantName = itemView.findViewById(R.id.partDetailsName);
            participantAge = itemView.findViewById(R.id.partDetailsAge);
            participantVillage = itemView.findViewById(R.id.partDetailsVillage);
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
