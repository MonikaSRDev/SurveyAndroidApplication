package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.ParticipantReportAdapter;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantReportServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.ParticipantReport;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateActiveStatus;
import com.example.mithraapplication.ModelClasses.UpdateRegisterStatus;
import com.example.mithraapplication.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantReportFragment extends Fragment implements AdapterView.OnItemSelectedListener, ParticipantReportServerEvents, HandleServerResponse {

    private TrackingParticipantStatus trackingParticipantStatus = null;
    private String isEditable, currentStatusReason, currentStatus;
    private Context context;
    private ArrayList<ParticipantReport> participantReportArrayList = new ArrayList<>();
    private RecyclerView participantStatusRecyclerView;
    private ParticipantReportAdapter participantReportAdapter;
    private Button statusButton;
    private TextView noSurveyTextView;
    private Dialog dialog;
    private final List<String> statusUpdateReason = Arrays.asList("Moved to other SHG", "Preferred to Doctor", "Not interested", "Death");
    private ArrayAdapter statusUpdateAdapter;
    private Spinner statusUpdateReasonSpinner;
    private RegisterParticipant registerParticipant;
    private PHQLocations phqLocations;
    String updatedStatus = null;
    private final MithraUtility mithraUtility = new MithraUtility();

    public ParticipantReportFragment(Context context, TrackingParticipantStatus trackingParticipantStatus, String isEditable, RegisterParticipant registerParticipant, PHQLocations phqLocations) {
        this.context = context;
        this.trackingParticipantStatus = trackingParticipantStatus;
        this.isEditable = isEditable;
        this.registerParticipant = registerParticipant;
        this.phqLocations = phqLocations;
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
//        initializeData();
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
        participantStatusRecyclerView.setVisibility(View.GONE);
        statusButton = requireActivity().findViewById(R.id.profileEditButton);
        noSurveyTextView = view.findViewById(R.id.noSurveyTextView);
        noSurveyTextView.setVisibility(View.VISIBLE);
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
        TextView statusUpdateReasonTV = customLayout.findViewById(R.id.statusUpdateReason);
        EditText userNameET = customLayout.findViewById(R.id.userNameETPopup);
        Button activeStatusButton = customLayout.findViewById(R.id.activeStatusButton);
        Button inActiveStatusButton = customLayout.findViewById(R.id.inactiveStatusButton);

        currentStatus = registerParticipant.getActive();

        if(currentStatus!=null && currentStatus.equalsIgnoreCase("yes")){
            activeStatusButton.setBackgroundResource(R.drawable.selected_button);
            inActiveStatusButton.setBackgroundResource(R.drawable.yes_no_button);
        }else if(currentStatus!=null && currentStatus.equalsIgnoreCase("no")){
            activeStatusButton.setBackgroundResource(R.drawable.yes_no_button);
            inActiveStatusButton.setBackgroundResource(R.drawable.selected_button);
        }

        userNameET.setText(registerParticipant.getParticipantUserName());
        EditText userPhoneNumberET = customLayout.findViewById(R.id.userPhoneNumETPopup);
        userPhoneNumberET.setText(registerParticipant.getParticipantPhoneNumber());
        EditText userSHGET = customLayout.findViewById(R.id.userSHGETPopup);
        userSHGET.setText(registerParticipant.getParticipantSHGAssociation());

        statusUpdateReasonSpinner = customLayout.findViewById(R.id.statusUpdateReasonSpinner);
        statusUpdateAdapter = new ArrayAdapter(context, R.layout.spinner_item, statusUpdateReason);
        statusUpdateReasonSpinner.setAdapter(statusUpdateAdapter);
        statusUpdateReasonSpinner.setOnItemSelectedListener(this);

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
            if(currentStatus!=null && currentStatus.equalsIgnoreCase(updatedStatus)){
               Toast.makeText(context, context.getString(R.string.status_update), Toast.LENGTH_LONG).show();
            }else{
                callUpdateActiveStatus(updatedStatus);
            }
        });

        closeButton.setOnClickListener(v -> {
            statusButton.setBackgroundResource(R.drawable.edit_button_background);
            dialog.dismiss();
        });

        activeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedStatus = "yes";
                activeStatusButton.setBackgroundResource(R.drawable.selected_button);
                inActiveStatusButton.setBackgroundResource(R.drawable.yes_no_button);
            }});

        inActiveStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedStatus = "no";
                inActiveStatusButton.setBackgroundResource(R.drawable.selected_button);
                activeStatusButton.setBackgroundResource(R.drawable.yes_no_button);
            }});
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.statusUpdateReasonSpinner){
            currentStatusReason = statusUpdateReason.get(position);
            statusUpdateReasonSpinner.setSelection(position);
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

    private void callUpdateActiveStatus(String status){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.status";
        UpdateActiveStatus updateActiveStatus = new UpdateActiveStatus();
        updateActiveStatus.setUser_pri_id(registerParticipant.getUser_pri_id());
        updateActiveStatus.setActive(status);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setParticipantReportServerEvents(this);
        requestObject.putUpdateActiveStatus(context, updateActiveStatus, url);
    }

    @Override
    public void updateActiveStatusParticipant(String message) {
        dialog.dismiss();
        Toast.makeText(context, context.getString(R.string.update_message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(context, serverErrorResponse);
        }else{
            Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        statusButton.setText(R.string.status);
        noSurveyTextView.setText(R.string.no_survey_taken_to_display);
    }
}
