package com.example.nizar.quraanapp.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nizar.quraanapp.R;

import java.io.IOException;

public final class MyPlayer  {
    private ImageView playMedia;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        playMedia = findViewById(R.id.btn_play);
//
//    }

    public static MediaPlayer mp;
    //create an object of SingleObject
    private static MyPlayer instance = new MyPlayer();

    //make the constructor private so that this class cannot be
    //instantiated
    private MyPlayer(){}

    //Get the only object available
    public static MyPlayer getInstance(){
        return instance;
    }

    public void  playSurah(String path) {
            try {
                mp.reset();
                mp.setDataSource(path);
                mp.prepare();
                mp.start();
//                 mp.setOnCompletionListener(onCompletionListener);
                // Changing Button Image to pause image
                playMedia.setImageResource(R.drawable.pause_btn);
//            Toast.makeText(getApplicationContext(),"play:"+path,Toast.LENGTH_SHORT).show();
                Log.d("hhhhh", "j" + path);

                // set Progress bar values
//            sb.setProgress(0);
//            sb.setMax(mp.getDuration());

                // Updating progress bar
//            updateProgressBar();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", "error:" + e.getMessage());
            }



    }

    public MediaPlayer getMp() {
        mp = new MediaPlayer();
        return mp;
    }




}
