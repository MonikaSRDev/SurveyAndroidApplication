package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MithraUtility extends Application {

    /**
     * Description : Put the data to be stored in the shared preferences
     */
    public void putSharedPreferencesData(Context context, String className, String variableName, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(className, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(variableName, value);
        editor.apply();
    }

    /**
     * Description : Get the data stored in the shared preferences
     */
    public String getSharedPreferencesData(Context context, String className, String variableName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(className, MODE_PRIVATE);
        return sharedPreferences.getString(variableName, "NULL");
    }
}
