package com.example.mithraapplication.Fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.ParticipantReportAdapter;
import com.example.mithraapplication.FullScreenVideoView;
import com.example.mithraapplication.ModelClasses.ParticipantReport;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.R;
import com.example.mithraapplication.VideoScreen;

import java.util.ArrayList;

public class ParticipantReportFragment extends Fragment {

    private ArrayList<TrackingParticipantStatus> trackingParticipantStatus = null;
    private String isEditable;
    private Context context;
    private ArrayList<ParticipantReport> participantReportArrayList = new ArrayList<>();
    private RecyclerView participantStatusRecyclerView;
    private ParticipantReportAdapter participantReportAdapter;
    private Button statusButton;
    private Dialog dialog;

    public ParticipantReportFragment(Context context, ArrayList<TrackingParticipantStatus> trackingParticipantStatus, String isEditable) {
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participant_report_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        initializeData();
        setRecyclerView();
        setOnclickOfStatusButton();
    }

    private void initializeData(){
        ParticipantReport participantReport = new ParticipantReport();
        participantReport.setSurveyName("survey -3");
        participantReport.setSurveyStart("12/03/2022");
        participantReport.setSurveyStop("18/07/2022");
        participantReport.setSurveyScore("3");
        participantReport.setModulesAssigned("1");
        participantReport.setActivityCompleted("70%");

        participantReportArrayList.add(participantReport);
        participantReportArrayList.add(participantReport);
        participantReportArrayList.add(participantReport);

    }

    private void RegisterViews(View view) {
        participantStatusRecyclerView = view.findViewById(R.id.participantStatusRecyclerView);
        statusButton = requireActivity().findViewById(R.id.profileEditButton);
    }

    private void setRecyclerView(){
        participantReportAdapter = new ParticipantReportAdapter(getActivity(), participantReportArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        participantStatusRecyclerView.setLayoutManager(linearLayoutManager);
        participantStatusRecyclerView.setAdapter(participantReportAdapter);
    }

    private void setOnclickOfStatusButton(){
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusButton.setBackgroundResource(R.drawable.status_button);
                showPopupToUpdateStatus();
            }
        });
    }

    /**
     * Description : Used to show alert to the participant after completing the survey
     */
    private void showPopupToUpdateStatus(){
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View customLayout = getLayoutInflater().inflate(R.layout.activity_status_update_popup, null);

        TextView userNameTV = customLayout.findViewById(R.id.userNameTVPopup);
        TextView userPhoneNumberTV = customLayout.findViewById(R.id.userPhoneNumTVPopup);
        TextView userSHGTV = customLayout.findViewById(R.id.userSHGTVPopup);
        TextView userCurrentStatusTV = customLayout.findViewById(R.id.userCurrentStatusTVPopup);
        EditText userNameET = customLayout.findViewById(R.id.userNameETPopup);
        EditText userPhoneNumberET = customLayout.findViewById(R.id.userPhoneNumETPopup);
        EditText userSHGET = customLayout.findViewById(R.id.userSHGETPopup);
        Spinner userCurrentStatusSpinner = customLayout.findViewById(R.id.userCurrentStatusSpinnerPopup);
        Button saveButton = customLayout.findViewById(R.id.saveCurrentStatusButton);

        dialog  = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().setLayout(500, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something when clicked
                Toast.makeText(getActivity(), "Button Pressed", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }});
    }

}
