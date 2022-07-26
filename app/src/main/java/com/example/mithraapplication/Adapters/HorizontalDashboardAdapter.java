package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.ParticipantStatus;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class HorizontalDashboardAdapter extends RecyclerView.Adapter<HorizontalDashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ParticipantStatus> participantStatusArrayList;
    private final onItemClickListener itemClickListener;

    @NonNull
    @Override
    public HorizontalDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_horizontal_row, parent, false);
        return new ViewHolder(view);
    }

    public HorizontalDashboardAdapter(Context context, ArrayList<ParticipantStatus> participantStatusArrayList, onItemClickListener itemClickListener){
        this.context = context;
        this.participantStatusArrayList = participantStatusArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalDashboardAdapter.ViewHolder holder, int position) {

        ParticipantStatus participantStatus = participantStatusArrayList.get(position);

        holder.statusNameTV.setText(participantStatus.getStatusName());
        holder.completeValTV.setText(participantStatus.getCompleted());
        holder.pendingValTV.setText(participantStatus.getPending());
        holder.totalValTV.setText(participantStatus.getTotal());

        holder.completedLinearLayout.setOnClickListener(v -> itemClickListener.onItemClick(holder.completeTV.getText().toString()));

        holder.pendingLinearLayout.setOnClickListener(v -> itemClickListener.onItemClick(holder.pendingTV.getText().toString()));

        holder.totalLinearLayout.setOnClickListener(v -> itemClickListener.onItemClick(holder.totalTV.getText().toString()));

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView statusNameTV, completeTV, pendingTV, totalTV, completeValTV, pendingValTV, totalValTV;
        LinearLayout completedLinearLayout, pendingLinearLayout, totalLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusNameTV = itemView.findViewById(R.id.statusNameTV);
            completeTV = itemView.findViewById(R.id.completeTV);
            pendingTV = itemView.findViewById(R.id.pendingTV);
            totalTV = itemView.findViewById(R.id.totalTV);
            completeValTV = itemView.findViewById(R.id.completeValueTV);
            pendingValTV = itemView.findViewById(R.id.pendingValueTV);
            totalValTV = itemView.findViewById(R.id.totalValueTV);
            completedLinearLayout = itemView.findViewById(R.id.completedLinearLayout);
            pendingLinearLayout = itemView.findViewById(R.id.pendingLinearLayout);
            totalLinearLayout = itemView.findViewById(R.id.totalLinearLayout);
        }
    }

    public interface onItemClickListener{
        void onItemClick(String participantStatus);
    }

    @Override
    public int getItemCount() {
        return participantStatusArrayList.size();
    }
}
