package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.ParticipantReportAdapter;
import com.example.mithraapplication.ModelClasses.ParticipantReport;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantReportFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private TrackingParticipantStatus trackingParticipantStatus = null;
    private String isEditable, userCurrentStatus;
    private Context context;
    private ArrayList<ParticipantReport> participantReportArrayList = new ArrayList<>();
    private RecyclerView participantStatusRecyclerView;
    private ParticipantReportAdapter participantReportAdapter;
    private Button statusButton;
    private Dialog dialog;
    private final List<String> userStatus = Arrays.asList("Active", "Inactive");
    private ArrayAdapter userStatusAdapter;
    private Spinner userCurrentStatusSpinner;
    private RegisterParticipant registerParticipant;

    public ParticipantReportFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable, RegisterParticipant registerParticipant) {
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.registerParticipant = registerParticipant;
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
        participantReportAdapter = new ParticipantReportAdapter(context, participantReportArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        participantStatusRecyclerView.setLayoutManager(linearLayoutManager);
        participantStatusRecyclerView.setAdapter(participantReportAdapter);
    }

    private void setOnclickOfStatusButton(){
        statusButton.setOnClickListener(v -> {
            statusButton.setBackgroundResource(R.drawable.status_button);
            showPopupToUpdateStatus();
        });
    }

    /**
     * Description : Used to show alert to the participant after completing the survey
     */
    private void showPopupToUpdateStatus(){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_status_update_popup, null);

        TextView userNameTV = customLayout.findViewById(R.id.userNameTVPopup);
        TextView userPhoneNumberTV = customLayout.findViewById(R.id.userPhoneNumTVPopup);
        TextView userSHGTV = customLayout.findViewById(R.id.userSHGTVPopup);
        TextView userCurrentStatusTV = customLayout.findViewById(R.id.userCurrentStatusTVPopup);
        EditText userNameET = customLayout.findViewById(R.id.userNameETPopup);
        userNameET.setText(registerParticipant.getParticipantUserName());
        EditText userPhoneNumberET = customLayout.findViewById(R.id.userPhoneNumETPopup);
        userPhoneNumberET.setText(registerParticipant.getParticipantPhoneNumber());
        EditText userSHGET = customLayout.findViewById(R.id.userSHGETPopup);
        userSHGET.setText(registerParticipant.getParticipantSHGAssociation());
        userCurrentStatusSpinner = customLayout.findViewById(R.id.userCurrentStatusSpinnerPopup);
        userStatusAdapter = new ArrayAdapter(context, R.layout.spinner_item, userStatus);
//        userStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userCurrentStatusSpinner.setAdapter(userStatusAdapter);
        userCurrentStatusSpinner.setOnItemSelectedListener(this);
        Button saveButton = customLayout.findViewById(R.id.saveCurrentStatusButton);
        ImageView closeButton = customLayout.findViewById(R.id.closeAlertButtonPopup);

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        saveButton.setOnClickListener(v -> {
            statusButton.setBackgroundResource(R.drawable.edit_button_background);
            dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {
            statusButton.setBackgroundResource(R.drawable.edit_button_background);
            dialog.dismiss();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.userCurrentStatusSpinnerPopup){
            userCurrentStatus = userStatus.get(position);
            userCurrentStatusSpinner.setSelection(position);
            ((TextView) view).setTextColor(getResources().getColor(R.color.text_color));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
