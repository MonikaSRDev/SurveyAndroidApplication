package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;

public class JsonServerObject {

    public String ToJSON(){
        Gson gson = new Gson();
        String jsonData = gson.toJson(this).replace("\\\"","");
        String json = jsonData.replaceAll(" ", "%20");
//        int maxLogSize = 1000;
//        for(int i = 0; i<= json.length()/maxLogSize; i++ ){
//            int start = i * maxLogSize;
//            int end = (i+1) * maxLogSize;
//            end = Math.min(end, json.length());
//        }
        return json;
    }

    public JsonServerObject FromJson(String jsonString, String methodName){return JsonServerObject.this;}
}
