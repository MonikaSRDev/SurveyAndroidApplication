package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;

public class JsonServerObject {

    public String ToJSON(){
        Gson gson = new Gson();
        String jsonData = gson.toJson(this).replace("\\\"","");
        return jsonData.replaceAll(" ", " ");
    }

    public JsonServerObject FromJson(String jsonString, String methodName){return JsonServerObject.this;}
}
