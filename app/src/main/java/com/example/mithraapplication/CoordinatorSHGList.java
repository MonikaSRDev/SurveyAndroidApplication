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
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CoordinatorSHGList extends AppCompatActivity implements HandleServerResponse{
    private GridView phqGridView;
    private LinearLayout phqScreeningLinearlayout, dashboardLinearLayout, participantLinearLayout;
    private TextView phqScreeningTV, dashboardTV, participantTV, coordinatorSHGTV;
    private ImageView phqScreeningIcon, dashboardIcon, participantIcon;
    private final MithraUtility mithraUtility = new MithraUtility();
    ArrayList<PHQLocations> phqLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phq_shg_list);
        RegisterViews();
        moveToPHQScreening();
        moveToDashboardScreen();
        callGetCoordinatorSHGList();
    }

    private void RegisterViews() {
        phqGridView = findViewById(R.id.SHGListPHQScreening);

        phqScreeningLinearlayout = findViewById(R.id.phqScreeningLL);
        phqScreeningTV = findViewById(R.id.phqScreeningTV);
        phqScreeningIcon = findViewById(R.id.phqScreeningIcon);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutPHQ);
        dashboardTV = findViewById(R.id.dashboardTVPHQ);
        dashboardIcon = findViewById(R.id.dashboardIconPHQ);

        participantLinearLayout = findViewById(R.id.participantLinearLayoutPHQ);
        participantTV = findViewById(R.id.participantsTVPHQ);
        participantIcon = findViewById(R.id.participantsIconPHQ);

        participantTV.setTextColor(getResources().getColor(R.color.text_color));
        participantLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantIcon.setImageDrawable(getResources().getDrawable(R.drawable.participants_icon_black, CoordinatorSHGList.this.getTheme()));

        coordinatorSHGTV = findViewById(R.id.dashboardTitleTVPHQ);
        coordinatorSHGTV.setText("Coordinator SHG List");

    }

//    private void moveToParticipantsScreen(){
//        participantLinearLayout.setOnClickListener(v -> {
//            Intent participantIntent = new Intent(CoordinatorSHGList.this, ParticipantsScreen.class);
//            startActivity(participantIntent);
//        });
//    }

    private void moveToDashboardScreen(){
        dashboardLinearLayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(CoordinatorSHGList.this, DashboardScreen.class);
            startActivity(participantIntent);
        });
    }

    private void moveToPHQScreening(){
        phqScreeningLinearlayout.setOnClickListener(v -> {
            Intent participantIntent = new Intent(CoordinatorSHGList.this, PHQ9SHGListScreen.class);
            startActivity(participantIntent);
        });
    }

    private void moveToParticipantsScreen(PHQLocations phqLocations){
        Intent participantIntent = new Intent(CoordinatorSHGList.this, ParticipantsScreen.class);
        participantIntent.putExtra("PHQLocations", (Serializable) phqLocations);
        startActivity(participantIntent);
    }

    private void setOnClickGridItemListener(){
        phqGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToParticipantsScreen(phqLocations.get(position));
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
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.location.api.co_eli_loc_list";
        PHQLocations phqCoordinatorLocations = new PHQLocations();
        phqCoordinatorLocations.setCoordinatorName(mithraUtility.getSharedPreferencesData(CoordinatorSHGList.this, getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID)));
        phqCoordinatorLocations.setEligible("yes");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getCoordinatorLocations(CoordinatorSHGList.this, phqCoordinatorLocations, url);
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
                Toast.makeText(CoordinatorSHGList.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CoordinatorSHGList.this, DashboardScreen.class);
        startActivity(intent);
        finish();
    }
}
