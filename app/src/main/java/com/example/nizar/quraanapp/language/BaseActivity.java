package com.example.nizar.quraanapp.language;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.example.nizar.quraanapp.language.MyContextWrapper;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.d("======","attachBaseContext");
        Locale languageType = new Locale("ar");
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }



}
