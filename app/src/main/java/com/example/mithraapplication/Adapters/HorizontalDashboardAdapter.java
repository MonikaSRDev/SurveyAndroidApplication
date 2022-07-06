package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.ParticipantStatus;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class HorizontalDashboardAdapter extends RecyclerView.Adapter<HorizontalDashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParticipantStatus> participantStatusArrayList = new ArrayList<>();

    @NonNull
    @Override
    public HorizontalDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_horizontal_row, parent, false);
        return new ViewHolder(view);
    }

    public HorizontalDashboardAdapter(Context context, ArrayList<ParticipantStatus> participantStatusArrayList){
        this.context = context;
        this.participantStatusArrayList = participantStatusArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalDashboardAdapter.ViewHolder holder, int position) {

        ParticipantStatus participantStatus = participantStatusArrayList.get(position);

        holder.statusNameTV.setText(participantStatus.getStatusName());
        holder.completeValTV.setText(participantStatus.getCompleted());
        holder.pendingValTV.setText(participantStatus.getPending());
        holder.totalValTV.setText(participantStatus.getTotal());

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView statusNameTV, completeTV, pendingTV, totalTV, completeValTV, pendingValTV, totalValTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusNameTV = itemView.findViewById(R.id.statusNameTV);
            completeTV = itemView.findViewById(R.id.completeTV);
            pendingTV = itemView.findViewById(R.id.pendingTV);
            totalTV = itemView.findViewById(R.id.totalTV);
            completeValTV = itemView.findViewById(R.id.completeValueTV);
            pendingValTV = itemView.findViewById(R.id.pendingValueTV);
            totalValTV = itemView.findViewById(R.id.totalValueTV);
        }
    }

    @Override
    public int getItemCount() {
        return participantStatusArrayList.size();
    }
}
