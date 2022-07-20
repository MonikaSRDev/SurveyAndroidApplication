package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DashboardParticipantDetailsScreen extends AppCompatActivity implements HandleServerResponse{

    private Button englishButtonDashboard, kannadaButtonDashboard;
    private LinearLayout dashboardLinearLayout, participantLinearLayout;
    private TextView dashboardTVDashboard, participantTVDashboard, coordinatorNameTVDashboard, participantName,
            participantAge, participantPhoneNum, participantPanchayat, participantSHG, participantVillage;
    private TextView preScreeningTV, registrationTV, socioDemographyTV, diseaseProfileTV;
    private ImageView preScreeningIV, registrationIV, socioDemographyIV, diseaseProfileIV;
    private ImageView mithraLogoDashboard, coordinatorProfileDashboard, notificationsIconDashboard, dashboardIconDashboard;
    private MithraUtility mithraUtility = new MithraUtility();
    private RegisterParticipant registerParticipant;
    private ArrayList<TrackingParticipantStatus> trackingParticipantStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_participant_details);
        RegisterViews();
        getIntentData();
        callGetParticipantTrackingDetails();
    }

    private void RegisterViews(){
        englishButtonDashboard = findViewById(R.id.englishButtonDP);
        kannadaButtonDashboard = findViewById(R.id.kannadaButtonDP);

        dashboardLinearLayout = findViewById(R.id.dashboardLinearLayoutDP);
        dashboardLinearLayout.setBackgroundResource(R.drawable.selected_page);
        participantLinearLayout = findViewById(R.id.participantLinearLayoutDP);

        dashboardTVDashboard = findViewById(R.id.dashboardTVDP);
        dashboardTVDashboard.setTextColor(getResources().getColor(R.color.text_color));
        participantTVDashboard = findViewById(R.id.participantsTVDP);
        coordinatorNameTVDashboard = findViewById(R.id.coordinatorNameTVDP);
        String coordinatorUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator));
        if(!coordinatorUserName.equals("NULL")){
            coordinatorNameTVDashboard.setText(coordinatorUserName);
        }

        mithraLogoDashboard = findViewById(R.id.appLogoDP);
        coordinatorProfileDashboard = findViewById(R.id.coordinatorProfileDP);
        notificationsIconDashboard = findViewById(R.id.notificationsLogoDP);
        dashboardIconDashboard = findViewById(R.id.dashboardIconDP);
        dashboardIconDashboard.setImageDrawable(getResources().getDrawable(R.drawable.dashboard_icon_black));

        participantName = findViewById(R.id.participantNameDPTV);
        participantAge = findViewById(R.id.participantAgeDPTV);
        participantPhoneNum = findViewById(R.id.participantMobileDPTV);
        participantPanchayat = findViewById(R.id.participantAddress1DPTV);
        participantSHG = findViewById(R.id.participantAddress2DPTV);
        participantVillage = findViewById(R.id.participantVillageDPTV);

        preScreeningTV = findViewById(R.id.preScreeningTVDP);
        registrationTV = findViewById(R.id.preScreeningTVDP);
        socioDemographyTV = findViewById(R.id.preScreeningTVDP);
        diseaseProfileTV = findViewById(R.id.preScreeningTVDP);
        preScreeningIV = findViewById(R.id.preScreeningIconDP);
        registrationIV = findViewById(R.id.registrationIconDP);
        socioDemographyIV = findViewById(R.id.socioDemographyIconDP);
        diseaseProfileIV = findViewById(R.id.diseaseProfileIconDP);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            registerParticipant = (RegisterParticipant) intent.getSerializableExtra("RegisterParticipant Array");
        }
    }

    private void setParticipantDetails(){
        participantName.setText(registerParticipant.getParticipantName());
        participantAge.setText(registerParticipant.getParticipantAge());
        participantPhoneNum.setText(registerParticipant.getParticipantPhoneNumber());
        participantPanchayat.setText(registerParticipant.getParticipantPanchayat());
        participantSHG.setText(registerParticipant.getParticipantSHGAssociation());
        participantVillage.setText(registerParticipant.getParticipantVillageName());

        if(trackingParticipantStatus!=null && trackingParticipantStatus.size()!=0){
            if(trackingParticipantStatus.get(0).getRegistration()!=null && !trackingParticipantStatus.get(0).getRegistration().equalsIgnoreCase("null")){
                preScreeningIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
                registrationIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                preScreeningIV.setImageDrawable(getDrawable(R.drawable.error_icon));
                registrationIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
            if(trackingParticipantStatus.get(0).getSocio_demography()!=null && !trackingParticipantStatus.get(0).getSocio_demography().equalsIgnoreCase("null")){
                socioDemographyIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                socioDemographyIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
            if(trackingParticipantStatus.get(0).getDisease_profile()!=null && !trackingParticipantStatus.get(0).getDisease_profile().equalsIgnoreCase("null")){
                diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.completed_icon));
            }else{
                diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            }
        }else{
            preScreeningIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            registrationIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            socioDemographyIV.setImageDrawable(getDrawable(R.drawable.error_icon));
            diseaseProfileIV.setImageDrawable(getDrawable(R.drawable.error_icon));
        }
    }

    private void callGetParticipantTrackingDetails(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/resource/tracking?fields=[\"name\",\"registration\",\"socio_demography\",\"disease_profile\"]&or_filters=[[\"user_pri_id\", \"=\", \"" + registerParticipant.getUser_pri_id() + "\"]]";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getTrackingDetails(DashboardParticipantDetailsScreen.this, url);
    }

    @Override
    public void responseReceivedSuccessfully(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<TrackingParticipantStatus>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        try{
            trackingParticipantStatus = gson.fromJson(jsonObject.get("data"), type);
            setParticipantDetails();
        }catch(Exception e){
            Toast.makeText(DashboardParticipantDetailsScreen.this, jsonObject.get("data").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
