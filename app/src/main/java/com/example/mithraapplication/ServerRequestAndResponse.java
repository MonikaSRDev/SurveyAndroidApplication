package com.example.mithraapplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ServerRequestAndResponse {

    String url = "";
    RequestQueue requestQueue;

//    requestQueue = VolleySingletonRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
//    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                }
//            }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//
//        }
//    });
////        jsonObjectRequest.setTag(REQ_TAG);
//        requestQueue.add(jsonObjectRequest);
}
