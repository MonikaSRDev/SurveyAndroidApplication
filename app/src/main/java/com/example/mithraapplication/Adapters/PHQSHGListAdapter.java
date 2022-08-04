package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class PHQSHGListAdapter extends ArrayAdapter<PHQLocations> {

    public PHQSHGListAdapter(@NonNull Context context, int resource, ArrayList<PHQLocations> phqLocationsArrayList) {
        super(context, resource, phqLocationsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shg_grid_view_row, parent, false);
        }
        PHQLocations phqLocations = getItem(position);
        TextView SHGTV = listItemView.findViewById(R.id.SHGTextView);
        TextView VillageTV = listItemView.findViewById(R.id.VillageTextView);
        TextView PanchayatTV = listItemView.findViewById(R.id.PanchayatTextView);
        SHGTV.setText(phqLocations.getSHGName());
        VillageTV.setText(phqLocations.getVillageName());
        PanchayatTV.setText(phqLocations.getPanchayatName());

        return listItemView;
    }
}
