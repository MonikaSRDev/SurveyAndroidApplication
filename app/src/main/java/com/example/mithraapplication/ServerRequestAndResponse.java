package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerRequestAndResponse extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void getJsonRequest(Context context){
        String url = "http://192.168.1.4/api/method/mithra.mithra.doctype.participant.api.participants";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSONGETREQUEST","Success");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("JSONGETREQUEST","Failure");
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "token b44e3c21afb2a71:341788502cf2766");
                return params;
            }
        };


        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void postJsonRequest(Context context, JSONObject json){
        String url = "https://jsonplaceholder.typicode.com/todos/1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("JSONGETREQUEST","Success");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("JSONGETREQUEST","Failure");
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "token b44e3c21afb2a71:341788502cf2766");
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleySingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
