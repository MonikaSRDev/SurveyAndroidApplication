package com.example.mithraapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.Adapters.ParticipantScreenAdapter;
import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ParticipantProfileScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParticipantsAllListFragment extends Fragment implements HandleServerResponse {

    private TextView addNewParticipantTV;
    private ImageView addNewParticipantIcon;
    private FloatingActionButton floatingAddButton;
    private RecyclerView participantListRecyclerView;
    private ParticipantScreenAdapter participantScreenAdapter;
    private Context context;
    private PHQLocations phqLocations;

    public ParticipantsAllListFragment(Context context, PHQLocations phqLocations) {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participants_all_list_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    private void onClickAddNewParticipantIcon() {
        addNewParticipantIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ParticipantProfileScreen.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void onClickFloatingAddNewParticipantButton() {
        floatingAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ParticipantProfileScreen.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void setRecyclerView(ArrayList<RegisterParticipant> registerParticipants){
        participantScreenAdapter = new ParticipantScreenAdapter(getActivity(), registerParticipants, this::moveToParticipantProfileScreen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        participantListRecyclerView.setLayoutManager(linearLayoutManager);
        participantListRecyclerView.setAdapter(participantScreenAdapter);

    }

    private void moveToParticipantProfileScreen(RegisterParticipant registerParticipant){
        Intent participantIntent = new Intent(getActivity(), ParticipantProfileScreen.class);
        participantIntent.putExtra("RegisterParticipant Array", (Serializable) registerParticipant);
        participantIntent.putExtra("isEditable", "false");
        startActivity(participantIntent);
        requireActivity().finish();
    }

    private void callGetAllParticipantsDetails(){
        String url = "http://" + getString(R.string.base_url) +"/api/method/mithra.mithra.doctype.participant.api.participants";
        ServerRequestAndResponse serverRequestAndResponse = new ServerRequestAndResponse();
        serverRequestAndResponse.setHandleServerResponse(this);
        serverRequestAndResponse.getAllParticipantsDetails(getActivity(), url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RegisterParticipant>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<RegisterParticipant> registerParticipantsArrayList;

        try{
            registerParticipantsArrayList = gson.fromJson(jsonObject.get("message"), type);
            if(registerParticipantsArrayList.size() == 0){
                addNewParticipantIcon.setVisibility(View.VISIBLE);
                addNewParticipantTV.setVisibility(View.VISIBLE);
                floatingAddButton.setVisibility(View.GONE);
            }else{
                addNewParticipantIcon.setVisibility(View.GONE);
                addNewParticipantTV.setVisibility(View.GONE);
                floatingAddButton.setVisibility(View.VISIBLE);
                ArrayList<RegisterParticipant> registerParticipantsList = registerParticipantsArrayList;
//                ArrayList<RegisterParticipant> registerParticipantsList = registerParticipantsArrayList.stream()
//                        .filter(RegisterParticipant -> RegisterParticipant.getParticipantSHGAssociation().equalsIgnoreCase(phqLocations.getSHGName()))
//                        .collect(Collectors.toCollection(ArrayList::new));
                setRecyclerView(registerParticipantsList);
            }
        }catch(Exception e){
            Toast.makeText(getActivity(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

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
    }
}