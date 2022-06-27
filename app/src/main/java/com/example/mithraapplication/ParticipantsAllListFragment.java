package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ParticipantsAllListFragment extends Fragment implements HandleServerResponse{

    private TextView addNewParticipantTV;
    private ImageView addNewParticipantIcon;
//    private FloatingActionButton floatingAddButton;

    public ParticipantsAllListFragment() {
        // Required empty public constructor
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
//        callGetAllParticipantsDetails();
        onClickAddNewParticipantIcon();
//        onClickFloatingAddNewParticipantButton();
    }

    private void RegisterViews(View view) {
        addNewParticipantTV = view.findViewById(R.id.addNewParticipantTV);
        addNewParticipantIcon = view.findViewById(R.id.addParticipantIcon);
//        floatingAddButton = view.findViewById(R.id.floating_add_button);
    }

    private void onClickAddNewParticipantIcon() {
        addNewParticipantIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileScreen.class);
                startActivity(intent);
            }
        });
    }

//    private void onClickFloatingAddNewParticipantButton() {
//        addNewParticipantIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileScreen.class);
//                startActivity(intent);
//            }
//        });
//    }

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
//            if(registerParticipantsArrayList.size() == 0){
//                addNewParticipantIcon.setVisibility(View.VISIBLE);
//                addNewParticipantTV.setVisibility(View.VISIBLE);
////                floatingAddButton.setVisibility(View.GONE);
//            }else{
//                addNewParticipantIcon.setVisibility(View.GONE);
//                addNewParticipantTV.setVisibility(View.GONE);
////                floatingAddButton.setVisibility(View.VISIBLE);
//            }

        }catch(Exception e){
            Toast.makeText(getActivity(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}