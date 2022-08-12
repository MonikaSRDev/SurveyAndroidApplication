package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.Adapters.ParticipantScreenAdapter;
import com.example.mithraapplication.DashboardScreen;
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantsAllListServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParticipantsAllListFragment extends Fragment implements HandleServerResponse, ParticipantsAllListServerEvents {

    private TextView addNewParticipantTV, participantDetailsAllList, participantEnrollmentStatusAllList, participantSurveyStatusAllList, participantModuleStatusAllList, participantReferralStatusAllList;
    private ImageView addNewParticipantIcon;
    private FloatingActionButton floatingAddButton;
    private RecyclerView participantListRecyclerView;
    private ParticipantScreenAdapter participantScreenAdapter;
    private Context context;
    private PHQLocations phqLocations;
    private final MithraUtility mithraUtility = new MithraUtility();
    private Dialog dialog;

    public ParticipantsAllListFragment(Context context, PHQLocations phqLocations) {
        this.context = context;
        this.phqLocations = phqLocations;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_participants_all_list_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startProgressBar();
        RegisterViews(view);
        callGetAllParticipantsDetails();
        onClickAddNewParticipantIcon();
        onClickFloatingAddNewParticipantButton();
    }

    private void RegisterViews(View view) {
        addNewParticipantTV = view.findViewById(R.id.addNewParticipantTV);
        addNewParticipantIcon = view.findViewById(R.id.addParticipantIcon);
        participantListRecyclerView = view.findViewById(R.id.participantDetailsRecyclerView);
        floatingAddButton = view.findViewById(R.id.floatingActionButton);
        floatingAddButton.setVisibility(View.GONE);
        participantDetailsAllList = view.findViewById(R.id.participantDetailsAllList);
        participantEnrollmentStatusAllList = view.findViewById(R.id.participantEnrollmentStatusAllList);
        participantSurveyStatusAllList = view.findViewById(R.id.participantSurveyStatusAllList);
        participantModuleStatusAllList = view.findViewById(R.id.participantModuleStatusAllList);
        participantReferralStatusAllList = view.findViewById(R.id.participantReferralStatusAllList);
    }

    private void onClickAddNewParticipantIcon() {
        addNewParticipantIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, ParticipantProfileScreen.class);
            intent.putExtra("RegisterParticipant Array", (Serializable) new RegisterParticipant());
            intent.putExtra("PHQLocations", (Serializable) phqLocations);
            intent.putExtra("isEditable", "true");
            startActivity(intent);
        });
    }

    private void onClickFloatingAddNewParticipantButton() {
        floatingAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ParticipantProfileScreen.class);
            intent.putExtra("RegisterParticipant Array", (Serializable) new RegisterParticipant());
            intent.putExtra("PHQLocations", (Serializable) phqLocations);
            intent.putExtra("isEditable", "true");
            startActivity(intent);
        });
    }

    private void setRecyclerView(ArrayList<RegisterParticipant> registerParticipants){
        participantScreenAdapter = new ParticipantScreenAdapter(context, registerParticipants, this::moveToParticipantProfileScreen);
        participantListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false){
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                stopProgressBar();
            }
        });
        participantListRecyclerView.setAdapter(participantScreenAdapter);

    }

    private void moveToParticipantProfileScreen(RegisterParticipant registerParticipant){
        Intent participantIntent = new Intent(context, ParticipantProfileScreen.class);
        participantIntent.putExtra("RegisterParticipant Array", (Serializable) registerParticipant);
        participantIntent.putExtra("PHQLocations", (Serializable) phqLocations);
        participantIntent.putExtra("isEditable", "false");
        startActivity(participantIntent);
    }

    private void startProgressBar(){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_progress_bar, null);

        ProgressBar progressbar = customLayout.findViewById(R.id.progressBar);

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
    }

    private void stopProgressBar(){
        dialog.dismiss();
    }

    private void callGetAllParticipantsDetails(){
        String url = "http://" + getString(R.string.base_url) +"/api/method/mithra.mithra.doctype.participant.api.participants";
        ServerRequestAndResponse serverRequestAndResponse = new ServerRequestAndResponse();
        serverRequestAndResponse.setHandleServerResponse(this);
        serverRequestAndResponse.setParticipantsAllListServerEvents(this);
        serverRequestAndResponse.getAllParticipantsDetails(context, url);
    }

    private void callGetTrackingDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.tracking.api.enrollstatus";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setParticipantsAllListServerEvents(this);
        requestObject.getAllTrackingDetails(context, url);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(addNewParticipantTV!=null){
            addNewParticipantTV.setText(R.string.add_new_participant_text);
        }

        participantDetailsAllList.setText(R.string.details);
        participantEnrollmentStatusAllList.setText(R.string.enrollment_status);
        participantSurveyStatusAllList.setText(R.string.survey_status);
        participantModuleStatusAllList.setText(R.string.module_status);
        participantReferralStatusAllList.setText(R.string.referral_status);
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

    @Override
    public void getAllParticipantsDetails(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RegisterParticipant>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<RegisterParticipant> registerParticipantsArrayList;

        try{
//            callGetTrackingDetails();
            registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
            registerParticipantsArrayList = registerParticipantsArrayList.stream()
                    .filter(RegisterParticipant -> RegisterParticipant.getParticipantSHGAssociation().equalsIgnoreCase(phqLocations.getSHGName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            if(registerParticipantsArrayList.size() == 0){
                stopProgressBar();
                addNewParticipantIcon.setVisibility(View.VISIBLE);
                addNewParticipantTV.setVisibility(View.VISIBLE);
                floatingAddButton.setVisibility(View.GONE);
            }else{
                addNewParticipantIcon.setVisibility(View.GONE);
                addNewParticipantTV.setVisibility(View.GONE);
                floatingAddButton.setVisibility(View.VISIBLE);
                setRecyclerView(registerParticipantsArrayList);
            }
        }catch(Exception e){
            Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getParticipantTrackingData(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ParticipantDetails>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null){
            try{
//                registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
//                oldParticipantsArrayList = registerParticipantsArrayList;
            }catch(Exception e){
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(context, serverErrorResponse);
        }else{
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}