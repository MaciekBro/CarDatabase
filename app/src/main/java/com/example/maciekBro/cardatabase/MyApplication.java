package com.example.maciekBro.cardatabase;

import android.app.Application;
import android.os.Build;

import com.facebook.stetho.*;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isInDebag = BuildConfig.DEBUG;
        Stetho.initializeWithDefaults(this);        //biblioteka ktora pozwala przejrzeć pliki wewnatrz aplikacji, || niebezpieczna
    }
}
