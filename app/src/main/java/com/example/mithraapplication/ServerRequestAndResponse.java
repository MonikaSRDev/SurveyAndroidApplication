package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.DiseasesProfilePostRequest;
import com.example.mithraapplication.ModelClasses.GetParticipantDetails;
import com.example.mithraapplication.ModelClasses.ParticipantScreening;
import com.example.mithraapplication.ModelClasses.PostSurveyQuestions;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.SocioDemography;
import com.example.mithraapplication.ModelClasses.SurveyPostRequest;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.example.mithraapplication.ModelClasses.TrackingParticipantStatus;
import com.example.mithraapplication.ModelClasses.UpdateDiseaseProfileTracking;
import com.example.mithraapplication.ModelClasses.UpdateRegisterParticipant;
import com.example.mithraapplication.ModelClasses.UpdateSocioDemographyTracking;
import com.example.mithraapplication.ModelClasses.UserLogin;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ServerRequestAndResponse extends Application {

    private static HandleServerResponse handleServerResponse = null;
    private static HandleFileDownloadResponse handleFileDownloadResponse = null;
    private MithraUtility mithraUtility = new MithraUtility();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Description : This method is used to make getRequest to the server and handle the response
     */
    public void getJsonRequest(Context context, String url){

        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        handleServerResponse.responseReceivedSuccessfully(response);
                        Log.i("JSONGETREQUEST","Success");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            json = new String(response.data);
                        }
                        handleServerResponse.responseReceivedFailure(json);
                        Log.i("JSONGETREQUEST","Failure");
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
    public void getJsonRequestWithParameters(Context context, String url, String fields, String filter){

        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        handleServerResponse.responseReceivedSuccessfully(response);
                        Log.i("JSONGETREQUEST","Success");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            json = new String(response.data);
                        }
                        handleServerResponse.responseReceivedFailure(json);                        Log.i("JSONGETREQUEST","Failure");
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
    public void postJsonRequest(Context context, String json, String url){

        StringRequest newRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        handleServerResponse.responseReceivedSuccessfully(response);
                        Log.i("JSONPOSTREQUEST","Success");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            json = new String(response.data);
                        }
                        handleServerResponse.responseReceivedFailure(json);
                        Log.i("JSONPOSTREQUEST","Failure");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.i("utf-8","Unsupported Encoding while trying to get the bytes of %s using %s" + json );
                    return null;
                }
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(newRequest);
    }

    /**
     * Description : This method is used to make postRequest to the server and handle the response
     */
    public void putJsonRequest(Context context, String json, String url){

        StringRequest newRequest = new StringRequest(Request.Method.PUT,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                handleServerResponse.responseReceivedSuccessfully(response);
                Log.i("JSONPOSTREQUEST","Success");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    json = new String(response.data);
                }
                handleServerResponse.responseReceivedFailure(json);
                Log.i("JSONPOSTREQUEST","Failure");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.i("utf-8","Unsupported Encoding while trying to get the bytes of %s using %s" + json );
                    return null;
                }
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
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
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
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
                String json = null;
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    json = new String(response.data);
                }
                handleFileDownloadResponse.fileDownloadFailure(json);
            }
        }, context);
        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(request);
        Log.i("VIDEODownload", "started time :" + mithraUtility.getCurrentTime());
    }

    public void setHandleServerResponse(HandleServerResponse context){
        handleServerResponse = context;
    }

    public void setHandleFileDownloadResponse(HandleFileDownloadResponse context){
        handleFileDownloadResponse = context;
    }

    public void getParticipantLocations(Context context, String url){
        getJsonRequest(context, url);
    }

    public void getAllParticipantsDetails(Context context, String url){
        getJsonRequest(context, url);
    }

    public void getCardDetails(Context context, String url){
        getJsonRequest(context, url);
    }

    public void getPHQ9Questions(Context context, SurveyQuestions surveyQuestions, String url){
        postJsonRequest(context, surveyQuestions.ToJSON(), url);
    }

    public void getTrackingDetails(Context context, String url){
        getJsonRequest(context, url);
    }

    public void getParticipantRegistrationDetails(Context context, GetParticipantDetails participantDetails, String url){
        postJsonRequest(context, participantDetails.ToJSON(), url);
    }

    public void getParticipantSocioDemographyDetails(Context context, String url){
        getJsonRequest(context, url);
    }

    public void getParticipantDiseaseProfileDetails(Context context, String url){
        getJsonRequest(context, url);
    }

    public void postUserLogin(Context context, UserLogin userLoginObject, String url){
        postJsonRequest(context, userLoginObject.ToJSON(), url);
    }

    public void postScreeningDetails(Context context, ParticipantScreening participantScreening, String url){
        postJsonRequest(context, participantScreening.ToJSON(), url);
    }

    public void postRegisterUser(Context context, RegisterParticipant registerParticipant, String url){
        postJsonRequest(context, registerParticipant.ToJSON(), url);
    }

    public void postSocioDemographyDetails(Context context, SocioDemography socioDemography, String url){
        postJsonRequest(context, socioDemography.ToJSON(), url);
    }

    public void postDiseaseProfileDetails(Context context, DiseasesProfilePostRequest diseasesProfilePostRequest, String url){
        postJsonRequest(context, diseasesProfilePostRequest.ToJSON(), url);
    }

    public void postSurveyAnswers(Context context, PostSurveyQuestions surveyPostRequest, String url){
        postJsonRequest(context, surveyPostRequest.ToJSON(), url);
    }

    public void postTrackingStatus(Context context, TrackingParticipantStatus trackingParticipantStatus, String url){
        postJsonRequest(context, trackingParticipantStatus.ToJSON(), url);
    }

    public void putTrackingStatusSocioDemography(Context context, UpdateSocioDemographyTracking trackingParticipantStatus, String url){
        putJsonRequest(context, trackingParticipantStatus.ToJSON(), url);
    }

    public void putTrackingStatusDiseaseProfile(Context context, UpdateDiseaseProfileTracking trackingParticipantStatus, String url){
        putJsonRequest(context, trackingParticipantStatus.ToJSON(), url);
    }

    public void putRegisterUser(Context context, UpdateRegisterParticipant registerParticipant, String url){
        putJsonRequest(context, registerParticipant.ToJSON(), url);
    }

    public void putSocioDemographyDetails(Context context, SocioDemography socioDemography, String url){
        putJsonRequest(context, socioDemography.ToJSON(), url);
    }

    public void putDiseaseProfileDetails(Context context, DiseasesProfilePostRequest diseasesProfilePostRequest, String url){
        putJsonRequest(context, diseasesProfilePostRequest.ToJSON(), url);
    }

}
