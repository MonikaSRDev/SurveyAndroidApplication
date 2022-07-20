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

public class ParticipantScreenAdapter extends RecyclerView.Adapter<ParticipantScreenAdapter.ViewHolder> {

    private ArrayList<RegisterParticipant> participantArrayList = new ArrayList<>();
    private Context context;
    private onItemClickListener itemClickListener;

    @NonNull
    @Override
    public ParticipantScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_vertical_row, parent, false);
        return new ParticipantScreenAdapter.ViewHolder(view);
    }

    public ParticipantScreenAdapter(Context context, ArrayList<RegisterParticipant> participantArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantArrayList = participantArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantScreenAdapter.ViewHolder holder, int position) {

        RegisterParticipant participantDetails = participantArrayList.get(position);

        holder.participantDetailsName.setText(participantDetails.getParticipantName());
        holder.participantDetailsAge.setText(participantDetails.getParticipantAge());
        holder.participantDetailsVillage.setText(participantDetails.getParticipantVillageName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(participantDetails);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View statusView;
        TextView participantDetailsName, participantDetailsAge, participantDetailsVillage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            statusView = itemView.findViewById(R.id.participantStatusView);
            participantDetailsName = itemView.findViewById(R.id.participantDetailsName);
            participantDetailsAge = itemView.findViewById(R.id.participantDetailsAge);
            participantDetailsVillage = itemView.findViewById(R.id.participantDetailsVillage);
        }
    }

    public interface onItemClickListener{
        void onItemClick(RegisterParticipant registerparticipant);
    }

    @Override
    public int getItemCount() {
        return participantArrayList.size();
    }
}