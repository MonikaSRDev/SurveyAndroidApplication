package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Date;

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
     * Description : Remove the data stored in the shared preferences
     */
    public void removeSharedPreferencesData(Context context, String className, String variableName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(className, MODE_PRIVATE);
        sharedPreferences.edit().remove(variableName).apply();
    }

    /**
     * Description : Get the current time of the user device for calculation of duration taken to complete the survey
     */
    public String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String strDate = sdf.format(c.getTime());
        Log.d("MithraUtility","getCurrentTime : " + strDate);
        return strDate;
    }

    /**
     * Description : Get the current time of the user device for calculation of duration taken to complete the survey
     */
    public String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(c.getTime());
        Log.d("MithraUtility","getCurrentTime : " + strDate);
        return strDate;
    }

    /**
     * Description : Get the difference between two time values in hours
     */
    public String getTimeDifferenceHours(String startTime, String endTime){
        String hours = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        try{
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            long milliseconds = endDate.getTime() - startDate.getTime();
            hours = String.valueOf((int) (milliseconds/(1000 * 60 * 60)));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return hours;
    }

    /**
     * Description : Get the difference between two time values in minutes
     */
    public String getTimeDifferenceMinutes(String startTime, String endTime){
        String minutes = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        try{
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            long milliseconds = endDate.getTime() - startDate.getTime();
            minutes = String.valueOf((int) (milliseconds/(1000*60)) % 60);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return minutes;
    }

    /**
     * Description : Get the difference between two time values in seconds
     */
    public String getTimeDifferenceSeconds(String startTime, String endTime){
        String seconds = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        try{
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            long milliseconds = endDate.getTime() - startDate.getTime();
            seconds = String.valueOf((int) (milliseconds/1000));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return seconds;
    }

    public void hideKeyboard(Context context, View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public Phonenumber.PhoneNumber getCountryCodeAndNumber(String phoneNumber){
        PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber pn = null;
        try{
            pn = pnu.parse(phoneNumber, "IN");
        }catch(Exception e){
            e.printStackTrace();
        }
        return pn;
    }

    public void showAppropriateMessages(Context context, String serverResponse){
        if(serverResponse!=null && serverResponse.contains("frappe.exceptions.DuplicateEntryError")){
            Toast.makeText(context, "Name already exists. Please enter other name.", Toast.LENGTH_LONG).show();
        }else if(serverResponse!=null && serverResponse.contains("frappe.exceptions.LinkValidationError")){
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }else if(serverResponse!=null && serverResponse.contains("frappe.exceptions.MandatoryError")){
            Toast.makeText(context, "Please fill all the details.", Toast.LENGTH_LONG).show();
        }else if(serverResponse!=null && serverResponse.contains("frappe.exceptions.ValidationError")){
            Toast.makeText(context, "Data with the same ID already exists.", Toast.LENGTH_LONG).show();
        }else if(serverResponse!=null && serverResponse.contains("frappe.exceptions.PermissionError")){
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }else if(serverResponse!=null && serverResponse.contains("frappe.exceptions.IntegrityError")){
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
