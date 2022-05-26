package com.example.mithraapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingletonRequestQueue {

    private static VolleySingletonRequestQueue requestQueueSingleton;

    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingletonRequestQueue(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }
    public static synchronized VolleySingletonRequestQueue getInstance(Context context) {
        if (requestQueueSingleton == null) {
            requestQueueSingleton = new VolleySingletonRequestQueue(context);
        }
        return requestQueueSingleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
