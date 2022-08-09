package com.example.mithraapplication.MithraAppServerEvents;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.R;

import java.util.HashMap;
import java.util.Map;

public class DownloadFileVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Context context;
    private MithraUtility mithraUtility = new MithraUtility();

    //create a static map for directly accessing headers
    public Map<String, String> responseHeaders ;

    public DownloadFileVolleyRequest(int method, String mUrl ,Response.Listener<byte[]> listener,
                                     Response.ErrorListener errorListener, Context context) {
        // TODO Auto-generated constructor stub

        super(method, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
        this.context = context;
    }

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
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}

