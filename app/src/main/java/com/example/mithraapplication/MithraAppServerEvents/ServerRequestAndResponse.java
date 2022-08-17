package com.example.mithraapplication.MithraAppServerEvents;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.mithraapplication.MithraAppServerEventsListeners.CoordinatorSHGServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.DashboardServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.DiseaseProfileServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.LoginScreenServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.PHQParticipantServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.PHQQuestionnaireServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.PHQScreeningServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantProfileServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantReportServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.ParticipantsAllListServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.RegistrationServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.ResearcherParticipantsServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.SocioDemographyServerEvents;
import com.example.mithraapplication.MithraAppServerEventsListeners.SurveyServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.DiseasesProfilePostRequest;
import com.example.mithraapplication.ModelClasses.GetParticipantDetails;
import com.example.mithraapplication.ModelClasses.OptionsRequest;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQSurveyPostAnswers;
import com.example.mithraapplication.ModelClasses.ParticipantScreening;
import com.example.mithraapplication.ModelClasses.PostSurveyQuestions;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateActiveStatus;
import com.example.mithraapplication.ModelClasses.UpdateDiseaseProfileTracking;
import com.example.mithraapplication.ModelClasses.UpdatePassword;
import com.example.mithraapplication.ModelClasses.UpdateRegisterParticipant;
import com.example.mithraapplication.ModelClasses.UpdateRegisterStatus;
import com.example.mithraapplication.ModelClasses.UpdateScreeningStatus;
import com.example.mithraapplication.ModelClasses.UpdateSocioDemographyTracking;
import com.example.mithraapplication.ModelClasses.UserLogin;
import com.example.mithraapplication.R;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerRequestAndResponse extends Application {

    private static HandleServerResponse handleServerResponse = null;
    private static LoginScreenServerEvents loginScreenServerEvents = null;
    private static CoordinatorSHGServerEvents coordinatorSHGServerEvents = null;
    private static DashboardServerEvents dashboardServerEvents = null;
    private static DiseaseProfileServerEvents diseaseProfileServerEvents = null;
    private static ParticipantProfileServerEvents participantProfileServerEvents = null;
    private static ParticipantsAllListServerEvents participantsAllListServerEvents = null;
    private static PHQParticipantServerEvents phqParticipantServerEvents = null;
    private static PHQQuestionnaireServerEvents phqQuestionnaireServerEvents = null;
    private static PHQScreeningServerEvents phqScreeningServerEvents = null;
    private static RegistrationServerEvents registrationServerEvents = null;
    private static ParticipantReportServerEvents participantReportServerEvents = null;
    private static SocioDemographyServerEvents socioDemographyServerEvents = null;
    private static ResearcherParticipantsServerEvents researcherParticipantsServerEvents = null;
    private static SurveyServerEvents surveyServerEvents = null;
    private static HandleFileDownloadResponse handleFileDownloadResponse = null;
    private final MithraUtility mithraUtility = new MithraUtility();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public enum ServerMethodNames{
        LoginUser, CoordinatorSHG, GetCardDetails, GetParticipantDetails, GetTrackingDetails, GetPHQParticipants,
        GetAllTrackingDetails, PostSurveyAnswers, GetPHQ9Questions, GetPHQ9Options, PostPHQAnswers, PostScreeningDetails,
        LoginForParticipant, RegisterParticipant, CreateTrackingDetails, IndividualParticipantDetails, GetAllParticipantDetailsResearcher,
        UpdateParticipantDetails, UpdateParticipantPassword, UpdateRegisterStatus, PostSocioDemography, UpdateSocioTrackingDetails,
        IndividualSocioDemographyDetails, UpdateSocioDemographyDetails, PostDiseasesProfile, UpdateDiseaseTrackingDetails, IndividualDiseaseProfileDetails,
        UpdateDiseaseProfileDetails, UpdateScreeningStatus, GetAllParticipantDetails, GetSurveyQuestions, GetSurveyOptions, GetParticipantScreening,
        UpdateActiveStatus
    }

    /**
     * Description : This method is used to make getRequest to the server and handle the response
     */
    public void getJsonRequest(Context context, ServerMethodNames serverMethodNames, String url){

        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, response -> {
                    handleServerResponseResult(serverMethodNames, response);
                    Log.i("JSONGETREQUEST","Success");
                }, error -> {
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json = new String(response.data);
                    }
                    handleServerResponse.responseReceivedFailure(json);
                    Log.i("JSONGETREQUEST","Failure");
                }){

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                String userRole = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.user_role), context.getString(R.string.user_role));
                if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.participant))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.participant));
                    params.put("Authorization", userToken);
                }else if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.coordinator))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.coordinator));
                    params.put("Authorization", userToken);
                }else{
                    params.put("Authorization", context.getString(R.string.mc_token));
                }
                return params;
            }
        };


        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Description : This method is used to make getRequest to the server and handle the response
     */
    public void getJsonRequestWithParameters(Context context, ServerMethodNames serverMethodNames, String url, String fields, String filter){

        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, response -> {
                    handleServerResponseResult(serverMethodNames, response);
                    Log.i("JSONGETREQUEST","Success");
                }, error -> {
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json = new String(response.data);
                    }
                    handleServerResponse.responseReceivedFailure(json);
                    Log.i("JSONGETREQUEST","Failure");
                }){

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                String userRole = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.user_role), context.getString(R.string.user_role));
                if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.participant))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.participant));
                    params.put("Authorization", userToken);
                }else if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.coordinator))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.coordinator));
                    params.put("Authorization", userToken);
                }else{
                    params.put("Authorization", context.getString(R.string.mc_token));
                }
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fields", fields);
                params.put("or_filters", filter);
                return params;
            }
        };


        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Description : This method is used to make postRequest to the server and handle the response
     */
    public void postJsonRequest(Context context, ServerMethodNames serverMethodNames, String json, String url){

        StringRequest newRequest = new StringRequest(Request.Method.POST,
                url, response -> {
                    handleServerResponseResult(serverMethodNames, response);
                    Log.i("JSONPOSTREQUEST","Success");
                }, error -> {
                    String json1 = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json1 = new String(response.data);
                    }
                    handleServerResponse.responseReceivedFailure(json1);
                    Log.i("JSONPOSTREQUEST","Failure");
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                String userRole = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.user_role), context.getString(R.string.user_role));
                if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.participant))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.participant));
                    params.put("Authorization", userToken);
                }else if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.coordinator))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.coordinator));
                    params.put("Authorization", userToken);
                }else{
                    params.put("Authorization", context.getString(R.string.mc_token));
                }
                return params;
            }

            @Override
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(newRequest);
    }

    /**
     * Description : This method is used to make postRequest to the server and handle the response
     */
    public void putJsonRequest(Context context, ServerMethodNames serverMethodNames, String json, String url){

        StringRequest newRequest = new StringRequest(Request.Method.PUT,
                url, response -> {
                    handleServerResponseResult(serverMethodNames, response);
                    Log.i("JSONPOSTREQUEST","Success");
                }, error -> {
                    String json1 = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json1 = new String(response.data);
                    }
                    handleServerResponse.responseReceivedFailure(json1);
                    Log.i("JSONPOSTREQUEST","Failure");
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                String userRole = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.user_role), context.getString(R.string.user_role));
                if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.participant))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.participant));
                    params.put("Authorization", userToken);
                }else if(!userRole.equals("NULL") && userRole.equals(context.getString(R.string.coordinator))){
                    String userToken = mithraUtility.getSharedPreferencesData(context, context.getString(R.string.authorization_tokens), context.getString(R.string.coordinator));
                    params.put("Authorization", userToken);
                }else{
                    params.put("Authorization", context.getString(R.string.mc_token));
                }
                return params;
            }

            @Override
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(newRequest);
    }

    /**
     * Description : This method is used to make postRequest to the server and handle the response
     */
    public void downloadFileRequest(Context context, String url){
        DownloadFileVolleyRequest request = new DownloadFileVolleyRequest(Request.Method.GET, url,
                response -> {
                    // TODO handle the response
                    try {
                        if (response!=null) {
                            handleFileDownloadResponse.fileDownloadedSuccessfully(response);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                        e.printStackTrace();
                    }
                }, error -> {
                    // TODO handle the error
                    error.printStackTrace();
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        json = new String(response.data);
                    }
                    handleFileDownloadResponse.fileDownloadFailure(json);
                }, context);
        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(request);
        Log.i("VIDEODownload", "started time :" + mithraUtility.getCurrentTime());
    }

    public void handleServerResponseResult(ServerMethodNames serverMethodNames, String message){
        switch (serverMethodNames){
            case LoginUser:
                if(loginScreenServerEvents!=null){
                    loginScreenServerEvents.loginResponse(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetCardDetails:
                if(dashboardServerEvents!=null){
                    dashboardServerEvents.getCardDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetParticipantDetails:
                if(dashboardServerEvents!=null){
                    dashboardServerEvents.getParticipantDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case CoordinatorSHG:
                if(coordinatorSHGServerEvents!=null){
                    coordinatorSHGServerEvents.coordinatorSHGList(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetTrackingDetails:
                if(participantProfileServerEvents!=null){
                    participantProfileServerEvents.getParticipantTrackingDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetPHQParticipants:
                if(phqParticipantServerEvents!=null){
                    phqParticipantServerEvents.getPHQParticipantsDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetSurveyQuestions:
                if(surveyServerEvents!=null){
                    surveyServerEvents.getSurveyQuestions(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetSurveyOptions:
                if(surveyServerEvents!=null){
                    surveyServerEvents.getSurveyOptions(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case PostSurveyAnswers:
                if(surveyServerEvents!=null){
                    surveyServerEvents.postSurveyAnswers(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case PostDiseasesProfile:
                if(diseaseProfileServerEvents!=null){
                    diseaseProfileServerEvents.postDiseasesProfile(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateDiseaseProfileDetails:
                if(diseaseProfileServerEvents!=null){
                    diseaseProfileServerEvents.updateDiseaseProfileDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateDiseaseTrackingDetails:
                if(diseaseProfileServerEvents!=null){
                    diseaseProfileServerEvents.updateDiseaseTrackingDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case IndividualDiseaseProfileDetails:
                if(diseaseProfileServerEvents!=null){
                    diseaseProfileServerEvents.individualDiseaseProfileDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetAllParticipantDetails:
                if(participantsAllListServerEvents!=null){
                    participantsAllListServerEvents.getAllParticipantsDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetPHQ9Options:
                if(phqQuestionnaireServerEvents!=null){
                    phqQuestionnaireServerEvents.getPHQ9Options(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetPHQ9Questions:
                if(phqQuestionnaireServerEvents!=null){
                    phqQuestionnaireServerEvents.getPHQ9Questions(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case PostPHQAnswers:
                if(phqQuestionnaireServerEvents!=null){
                    phqQuestionnaireServerEvents.postPHQAnswers(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case PostScreeningDetails:
                if(phqScreeningServerEvents!=null){
                    phqScreeningServerEvents.postScreeningDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateScreeningStatus:
                if(phqScreeningServerEvents!=null){
                    phqScreeningServerEvents.updateScreeningStatus(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetParticipantScreening:
                if(registrationServerEvents!=null){
                    registrationServerEvents.getAllPHQParticipants(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case LoginForParticipant:
                if(registrationServerEvents!=null){
                    registrationServerEvents.loginForParticipant(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case RegisterParticipant:
                if(registrationServerEvents!=null){
                    registrationServerEvents.registerParticipant(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case CreateTrackingDetails:
                if(registrationServerEvents!=null){
                    registrationServerEvents.createTrackingStatus(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case IndividualParticipantDetails:
                if(registrationServerEvents!=null){
                    registrationServerEvents.individualParticipantDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateParticipantDetails:
                if(registrationServerEvents!=null){
                    registrationServerEvents.updateParticipantDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateParticipantPassword:
                if(registrationServerEvents!=null){
                    registrationServerEvents.updateParticipantPassword(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateRegisterStatus:
                if(registrationServerEvents!=null){
                    registrationServerEvents.updateRegisterStatus(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case IndividualSocioDemographyDetails:
                if(socioDemographyServerEvents!=null){
                    socioDemographyServerEvents.individualSocioDemography(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case PostSocioDemography:
                if(socioDemographyServerEvents!=null){
                    socioDemographyServerEvents.postSocioDemography(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateSocioDemographyDetails:
                if(socioDemographyServerEvents!=null){
                    socioDemographyServerEvents.updateSocioDemographyDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case UpdateSocioTrackingDetails:
                if(socioDemographyServerEvents!=null){
                    socioDemographyServerEvents.updateSocioTrackingDetails(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetAllTrackingDetails:
                if(participantsAllListServerEvents!=null){
                    participantsAllListServerEvents.getParticipantTrackingData(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

            case GetAllParticipantDetailsResearcher:
                if(researcherParticipantsServerEvents!=null){
                    researcherParticipantsServerEvents.getAllParticipantsForResearcher(message);
                }else{
                    handleServerResponse.responseReceivedFailure(message);
                }
                break;

        }
    }

    public void setLoginServerEvents(LoginScreenServerEvents loginScreenServerEvents){
        ServerRequestAndResponse.loginScreenServerEvents = loginScreenServerEvents;
    }

    public void setCoordinatorSHGServerEvents(CoordinatorSHGServerEvents coordinatorSHGServerEvents){
        ServerRequestAndResponse.coordinatorSHGServerEvents = coordinatorSHGServerEvents;
    }

    public void setDashboardServerEvents(DashboardServerEvents dashboardServerEvents){
        ServerRequestAndResponse.dashboardServerEvents = dashboardServerEvents;
    }

    public void setDiseaseProfileServerEvents(DiseaseProfileServerEvents diseaseProfileServerEvents){
        ServerRequestAndResponse.diseaseProfileServerEvents = diseaseProfileServerEvents;
    }

    public void setParticipantProfileServerEvents(ParticipantProfileServerEvents participantProfileServerEvents){
        ServerRequestAndResponse.participantProfileServerEvents = participantProfileServerEvents;
    }

    public void setParticipantsAllListServerEvents(ParticipantsAllListServerEvents participantsAllListServerEvents){
        ServerRequestAndResponse.participantsAllListServerEvents = participantsAllListServerEvents;
    }

    public void setPhqParticipantServerEvents(PHQParticipantServerEvents phqParticipantServerEvents){
        ServerRequestAndResponse.phqParticipantServerEvents = phqParticipantServerEvents;
    }

    public void setPhqQuestionnaireServerEvents(PHQQuestionnaireServerEvents phqQuestionnaireServerEvents){
        ServerRequestAndResponse.phqQuestionnaireServerEvents = phqQuestionnaireServerEvents;
    }

    public void setPhqScreeningServerEvents(PHQScreeningServerEvents phqScreeningServerEvents){
        ServerRequestAndResponse.phqScreeningServerEvents = phqScreeningServerEvents;
    }

    public void setRegistrationServerEvents(RegistrationServerEvents registrationServerEvents){
        ServerRequestAndResponse.registrationServerEvents = registrationServerEvents;
    }

    public void setSocioDemographyServerEvents(SocioDemographyServerEvents socioDemographyServerEvents){
        ServerRequestAndResponse.socioDemographyServerEvents = socioDemographyServerEvents;
    }

    public void setSurveyServerEvents(SurveyServerEvents surveyServerEvents){
        ServerRequestAndResponse.surveyServerEvents = surveyServerEvents;
    }

    public void setResearcherParticipantsServerEvents(ResearcherParticipantsServerEvents researcherParticipantsServerEvents){
        ServerRequestAndResponse.researcherParticipantsServerEvents = researcherParticipantsServerEvents;
    }

    public void setParticipantReportServerEvents(ParticipantReportServerEvents participantReportServerEvents){
        ServerRequestAndResponse.participantReportServerEvents = participantReportServerEvents;
    }

    public void setHandleServerResponse(HandleServerResponse context){
        handleServerResponse = context;
    }

    public void setHandleFileDownloadResponse(HandleFileDownloadResponse context){
        handleFileDownloadResponse = context;
    }

    public void postUserLogin(Context context, UserLogin userLoginObject, String url){
        postJsonRequest(context, ServerMethodNames.LoginUser, userLoginObject.ToJSON(), url);
    }

    public void getCardDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetCardDetails, url);
    }

    public void getParticipantDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetParticipantDetails, url);
    }

    public void getCoordinatorLocations(Context context, PHQLocations phqLocations, String url){
        postJsonRequest(context, ServerMethodNames.CoordinatorSHG, phqLocations.ToJSON(), url);
    }

    public void getTrackingDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetTrackingDetails, url);
    }

    public void getPHQParticipantDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetPHQParticipants, url);
    }

    public void getSurveyQuestions(Context context, SurveyQuestions surveyQuestions, String url){
        postJsonRequest(context, ServerMethodNames.GetSurveyQuestions, surveyQuestions.ToJSON(), url);
    }

    public void getSurveyOptions(Context context, OptionsRequest optionsRequest, String url){
        postJsonRequest(context, ServerMethodNames.GetSurveyOptions, optionsRequest.ToJSON(), url);
    }

    public void postSurveyAnswers(Context context, PostSurveyQuestions surveyPostRequest, String url){
        postJsonRequest(context, ServerMethodNames.PostSurveyAnswers, surveyPostRequest.ToJSON(), url);
    }

    public void postDiseaseProfileDetails(Context context, DiseasesProfilePostRequest diseasesProfilePostRequest, String url){
        postJsonRequest(context, ServerMethodNames.PostDiseasesProfile, diseasesProfilePostRequest.ToJSON(), url);
    }

    public void putTrackingStatusDiseaseProfile(Context context, UpdateDiseaseProfileTracking trackingParticipantStatus, String url){
        putJsonRequest(context, ServerMethodNames.UpdateDiseaseTrackingDetails, trackingParticipantStatus.ToJSON(), url);
    }

    public void getParticipantDiseaseProfileDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.IndividualDiseaseProfileDetails, url);
    }

    public void putDiseaseProfileDetails(Context context, DiseasesProfilePostRequest diseasesProfilePostRequest, String url){
        putJsonRequest(context, ServerMethodNames.UpdateDiseaseProfileDetails, diseasesProfilePostRequest.ToJSON(), url);
    }

    public void getAllParticipantsDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetAllParticipantDetails, url);
    }

    public void getAllParticipantsDetailsResearcher(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetAllParticipantDetailsResearcher, url);
    }

    public void getPHQ9Questions(Context context, SurveyQuestions surveyQuestions, String url){
        postJsonRequest(context, ServerMethodNames.GetPHQ9Questions, surveyQuestions.ToJSON(), url);
    }

    public void getPHQ9Options(Context context, OptionsRequest optionsRequest, String url){
        postJsonRequest(context, ServerMethodNames.GetPHQ9Options, optionsRequest.ToJSON(), url);
    }

    public void postPHQScreeningAnswers(Context context, PHQSurveyPostAnswers phqSurveyPostAnswers, String url){
        postJsonRequest(context, ServerMethodNames.PostPHQAnswers, phqSurveyPostAnswers.ToJSON(), url);
    }

    public void postScreeningDetails(Context context, ParticipantScreening participantScreening, String url){
        postJsonRequest(context, ServerMethodNames.PostScreeningDetails, participantScreening.ToJSON(), url);
    }

    public void putUpdateScreeningStatus(Context context, UpdateScreeningStatus updateScreeningStatus, String url){
        putJsonRequest(context, ServerMethodNames.UpdateScreeningStatus, updateScreeningStatus.ToJSON(), url);
    }

    public void getParticipantsScreening(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetParticipantScreening, url);
    }

    public void createUserLogin(Context context, UserLogin userLoginObject, String url){
        postJsonRequest(context, ServerMethodNames.LoginForParticipant, userLoginObject.ToJSON(), url);
    }

    public void postRegisterUser(Context context, RegisterParticipant registerParticipant, String url){
        postJsonRequest(context, ServerMethodNames.RegisterParticipant,  registerParticipant.ToJSON(), url);
    }

    public void postTrackingStatus(Context context, TrackingParticipantStatus trackingParticipantStatus, String url){
        postJsonRequest(context, ServerMethodNames.CreateTrackingDetails, trackingParticipantStatus.ToJSON(), url);
    }

    public void getParticipantRegistrationDetails(Context context, GetParticipantDetails participantDetails, String url){
        postJsonRequest(context, ServerMethodNames.IndividualParticipantDetails, participantDetails.ToJSON(), url);
    }

    public void putRegisterUser(Context context, UpdateRegisterParticipant registerParticipant, String url){
        putJsonRequest(context, ServerMethodNames.UpdateParticipantDetails, registerParticipant.ToJSON(), url);
    }

    public void putUpdateUserPassword(Context context, UpdatePassword updatePassword, String url){
        putJsonRequest(context, ServerMethodNames.UpdateParticipantPassword, updatePassword.ToJSON(), url);
    }

    public void putUpdateRegisterStatus(Context context, UpdateRegisterStatus updateRegisterStatus, String url){
        putJsonRequest(context, ServerMethodNames.UpdateRegisterStatus, updateRegisterStatus.ToJSON(), url);
    }

    public void getParticipantSocioDemographyDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.IndividualSocioDemographyDetails, url);
    }

    public void postSocioDemographyDetails(Context context, SocioDemography socioDemography, String url){
        postJsonRequest(context, ServerMethodNames.PostSocioDemography, socioDemography.ToJSON(), url);
    }

    public void putTrackingStatusSocioDemography(Context context, UpdateSocioDemographyTracking trackingParticipantStatus, String url){
        putJsonRequest(context, ServerMethodNames.UpdateSocioTrackingDetails, trackingParticipantStatus.ToJSON(), url);
    }

    public void putSocioDemographyDetails(Context context, SocioDemography socioDemography, String url){
        putJsonRequest(context, ServerMethodNames.UpdateSocioDemographyDetails, socioDemography.ToJSON(), url);
    }

    public void getAllTrackingDetails(Context context, String url){
        getJsonRequest(context, ServerMethodNames.GetAllTrackingDetails, url);
    }

    public void putUpdateActiveStatus(Context context, UpdateActiveStatus updateActiveStatus, String url){
        putJsonRequest(context, ServerMethodNames.UpdateActiveStatus, updateActiveStatus.ToJSON(), url);
    }



}
