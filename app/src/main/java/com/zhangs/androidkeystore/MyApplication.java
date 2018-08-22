package com.zhangs.androidkeystore;

import android.app.Application;

import com.zhangs.library.PreferencesHelper;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesHelper.instance(this);
    }
}
