package com.example.mithraapplication;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private static Context mContext;

    @Override
    public void onCreate(){
        super.onCreate();

        mInstance = this;
        this.setAppContext(getApplicationContext());
    }

    public static MyApplication getInstance(){return mInstance;}
    public static Context getAppContext(){return mContext;}
    public void setAppContext(Context appContext){
        mContext = appContext;
    }
}
