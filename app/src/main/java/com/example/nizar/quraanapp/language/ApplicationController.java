package com.example.nizar.quraanapp.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class ApplicationController extends Application{
    Context m;

    @Override

    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.d("======","attachBaseContext");
        Locale languageType = new Locale("ar");
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    //Setting a Language
    public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf,getBaseContext().getResources().getDisplayMetrics());
    }

}
