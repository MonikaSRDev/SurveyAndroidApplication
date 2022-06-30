package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

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

    /**
     * Description : Get the current time of the user device for calculation of duration taken to complete the survey
     */
    public String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        Log.d("SurveyScreen","getCurrentTime : " + strDate);
        return strDate;
    }
}
