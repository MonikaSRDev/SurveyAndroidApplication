package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class PHQParticipantsAdapter extends RecyclerView.Adapter<PHQParticipantsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<PHQParticipantDetails> phqParticipantDetailsArrayList;
    private PHQParticipantsAdapter.onItemClickListener itemClickListener;

    @NonNull
    @Override
    public PHQParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phq_participants_row, parent, false);
        return new PHQParticipantsAdapter.ViewHolder(view);
    }

    public PHQParticipantsAdapter(Context context, ArrayList<PHQParticipantDetails> phqParticipantArrayList, PHQParticipantsAdapter.onItemClickListener itemClickListener){
        this.context = context;
        this.phqParticipantDetailsArrayList = phqParticipantArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull PHQParticipantsAdapter.ViewHolder holder, int position) {

        PHQParticipantDetails participantDetails = phqParticipantDetailsArrayList.get(position);

        holder.phqParticipantID.setText(participantDetails.getPHQScreeningID());
        holder.phqScreeningID.setText(participantDetails.getManualID());
        holder.phqParticipantName.setText(participantDetails.getPHQParticipantName());
        holder.phqScoreVal.setText(String.valueOf(participantDetails.getPHQScreeningScore()));

        if(participantDetails.getScreeningConsentScore().equals("pending")){
            holder.phqEligibility.setText("Pending");
            holder.eligibilityStatus.setBackgroundColor(context.getResources().getColor(R.color.notEligibleColor, context.getTheme()));
        }else{
            holder.phqEligibility.setText(participantDetails.getScreening_ID());
            holder.eligibilityStatus.setBackgroundColor(context.getResources().getColor(R.color.completed_color, context.getTheme()));
        }

        if(!participantDetails.getScreening_ID().equalsIgnoreCase("null") && participantDetails.getScreening_ID().equalsIgnoreCase("S0068")){
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(participantDetails));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView phqParticipantID, phqScreeningID, phqParticipantName, phqScoreVal, phqEligibility;
        View eligibilityStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            phqParticipantID = itemView.findViewById(R.id.PHQParticipantIDVal);
            phqScreeningID = itemView.findViewById(R.id.PHQScreeningIDVal);
            phqParticipantName = itemView.findViewById(R.id.PHQParticipantNameVal);
            phqScoreVal = itemView.findViewById(R.id.PHQScoreVal);
            phqEligibility = itemView.findViewById(R.id.PHQEligibilityVal);
            eligibilityStatus = itemView.findViewById(R.id.PHQEligibilityView);
        }
    }

    public interface onItemClickListener{
        void onItemClick(PHQParticipantDetails participantDetails);
    }

    @Override
    public int getItemCount() {
        return phqParticipantDetailsArrayList.size();
    }
}
