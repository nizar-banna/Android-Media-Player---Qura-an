package com.example.nizar.quraanapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class SurahImages extends AppCompatActivity {
    TextView t;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surah_images);

    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
//            InputStream is = context.getAssets().open("surah.json");
            AssetManager mngr = getAssets();
            InputStream is = mngr.open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}