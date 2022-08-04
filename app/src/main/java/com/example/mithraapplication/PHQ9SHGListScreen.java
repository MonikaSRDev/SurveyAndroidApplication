package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.Adapters.PHQSHGListAdapter;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.Locations;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PHQ9SHGListScreen extends AppCompatActivity implements HandleServerResponse{

    private GridView phqGridView;
    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private final MithraUtility mithraUtility = new MithraUtility();
    ArrayList<PHQLocations> phqLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_shg_list);
        RegisterViews();
        moveToParticipantsScreen();
        moveToDashboardScreen();
//        setGridViewAdapter();
        callGetCoordinatorSHGList();
    }

    private void RegisterViews() {
        phqGridView = findViewById(R.id.SHGListPHQScreening);

        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLL);
        phqScreeningTV = findViewById(R.id.phqScreeningTV);
        phqScreeningIcon = findViewById(R.id.phqScreeningIcon);

        phqScreeningTV.setTextColor(getResources().getColor(R.color.text_color));
        phqScreeningLinearlayout.setBackgroundResource(R.drawable.selected_page);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQ);
        dashboardTV = findViewById(R.id.dashboardTVPHQ);
        dashboardIcon = findViewById(R.id.dashboardIconPHQ);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQ);
        participantTV = findViewById(R.id.participantsTVPHQ);
        participantIcon = findViewById(R.id.participantsIconPHQ);

    }

    private void moveToParticipantsScreen(){
        participantLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, CoordinatorSHGList.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, DashboardScreen.class);
            startActivity(participantIntent);
            finish();
        });
    }

    private void moveToPHQParticipantsScreen(PHQLocations phqLocations){
            Intent participantIntent = new Intent(PHQ9SHGListScreen.this, PHQParticipantsScreen.class);
            participantIntent.putExtra("PHQLocations", (Serializable) phqLocations);
            startActivity(participantIntent);
    }

    private void setOnClickGridItemListener(){
        phqGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToPHQParticipantsScreen(phqLocations.get(position));
            }
        });
    }

    private void setGridViewAdapter(){
//        phqLocations.add(new PHQLocations("SHG1", "Village1", "Panchayat1"));
//        phqLocations.add(new PHQLocations("SHG2", "Village2", "Panchayat2"));
        setOnClickGridItemListener();
        PHQSHGListAdapter adapter = new PHQSHGListAdapter(this, 0, phqLocations);
        phqGridView.setAdapter(adapter);
    }

    private void callGetCoordinatorSHGList(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.co_loc_list";
        PHQLocations phqCoordinatorLocations = new PHQLocations();
        phqCoordinatorLocations.setCoordinatorName(mithraUtility.getSharedPreferencesData(PHQ9SHGListScreen.this, getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getCoordinatorLocations(PHQ9SHGListScreen.this, phqCoordinatorLocations, url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Log.i("SurveyScreen", "responseReceivedSuccessfully");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PHQLocations>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null) {
            ArrayList<PHQLocations> phqLocationsArrayList = new ArrayList<>();

            try {
                phqLocationsArrayList = gson.fromJson(jsonObject.get("message"), type);
                if(phqLocationsArrayList.size()!=0){
                    phqLocations = phqLocationsArrayList.stream()
                            .filter(phqLocations -> phqLocations.getActive().equalsIgnoreCase("yes"))
                            .collect(Collectors.toCollection(ArrayList::new));
                    setGridViewAdapter();
                }
            } catch (Exception e) {
                Toast.makeText(PHQ9SHGListScreen.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
            Type typeFrappe = new TypeToken<FrappeResponse>(){}.getType();
            if(jsonObjectRegistration.get("data")!=null) {
                FrappeResponse frappeResponse;
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), typeFrappe);
            }
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
