package com.example.nizar.quraanapp.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nizar.quraanapp.R;
import com.example.nizar.quraanapp.download.DirectoryHelper;
import com.example.nizar.quraanapp.download.DownloadSongService;

import java.io.File;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private Utilities utils;
    MediaPlayer mediaPlayer;
    AudioManager am;
    TextView num;
    ImageView playMedia, download;
    TextView currentPosition, totalPosition;
    SeekBar sb;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    int no, length = 0, seekBarValue = 0;
    private DownloadManager downloadManager;
    private long Music_DownloadId;
    String path = "https://server11.mp3quran.net/hazza/001.mp3", name, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);
        //code to text style
        Typeface m_typeFace = Typeface.createFromAsset(getAssets(), "kfc_naskh-webfont.otf");
        utils = new Utilities();
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        num = findViewById(R.id.no);
        playMedia = findViewById(R.id.btn_play);
        download = findViewById(R.id.download);
        currentPosition = findViewById(R.id.currentPosition);
        totalPosition = findViewById(R.id.totalPosition);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        no = i.getIntExtra("no", 0);

        String with3digits = String.format("%03d", no);
        number = arabicToDecimal(with3digits); // number = 42;
        path = "https://server8.mp3quran.net/frs_a/" + number + ".mp3";

        num.setText(name);
        num.setTypeface(m_typeFace);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);
        if (!playPause) {
            playMedia.setImageResource(R.drawable.pause_btn);

            if (initialStage) {
                new Player().execute(path);
            } else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }

            playPause = true;

        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playMedia.setImageResource(R.drawable.play_btn);

            }

            playPause = false;
        }
        // Request audio focus for playback
        playMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playMedia.setImageResource(R.drawable.play_btn);
                    Toast.makeText(getApplicationContext(), "ffff" + length, Toast.LENGTH_SHORT).show();
                } else {
                    if (mediaPlayer != null) {
//                        mediaPlayer.seekTo(seekBarValue);
                        mediaPlayer.start();
                        playMedia.setImageResource(R.drawable.pause_btn);
                        Toast.makeText(getApplicationContext(), "dddddddd", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        sb = findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });



        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MediaPlayerActivity.this, "Downloading", Toast.LENGTH_SHORT).show();
//                if(downloadManager.ERROR_FILE_ALREADY_EXISTS ==1009){
//                    Toast.makeText(MediaPlayerActivity.this, "Exsist", Toast.LENGTH_SHORT).show();
//                }

                startDownload();
            }
        });

    }


    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);

    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            totalPosition.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            currentPosition.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            sb.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    private String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_SHORT).show();
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        Boolean prepared = false;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        playMedia.setImageResource(R.drawable.play_btn);
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;

        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            mediaPlayer.pause();
            Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // other app had stopped playing song now , so u can do u stuff now .
                updateProgressBar();
                mediaPlayer.start();

            }
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("جاري التحميل ...");
            progressDialog.show();
        }
    }
//    public void  playSurah(String path) {
//        try {
//            mediaPlayer.reset();
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//            // Changing Button Image to pause image
//            playMedia.setImageResource(R.drawable.pause_btn);
//            // set Progress bar values
//            sb.setProgress(0);
//            sb.setMax(100);
//
//            // Updating progress bar
//            updateProgressBar();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("error", "error:" + e.getMessage());
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==100){
//            name = data.getStringExtra("name");
//            no = data.getIntExtra("no",0);
//            playSurah(path);
//
//
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
    }

    private OnAudioFocusChangeListener focusChangeListener =
            new OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            // Lower the volume while ducking.
                            mediaPlayer.setVolume(0.2f, 0.2f);
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            mediaPlayer.pause();
                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS):
                            mediaPlayer.stop();
                            ComponentName component = new ComponentName(MediaPlayerActivity.this, MainListActivity.class);
                            am.unregisterMediaButtonEventReceiver(component);
                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN):
                            // Return the volume to normal and resume if paused.
                            mediaPlayer.setVolume(1f, 1f);
//                            mediaPlayer.seekTo(seekBarValue);
                            mediaPlayer.start();
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "pause" + seekBarValue, Toast.LENGTH_SHORT).show();
    }


    private void startDownload() {
        File applictionFile = new File(Environment.
                getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS) + number + ".mp3");

        if (applictionFile.exists()) {
            Toast.makeText(getApplicationContext(), "File Already Exists",
                    Toast.LENGTH_LONG).show();
        }else {
            Uri music_uri = Uri.parse(path);
            Music_DownloadId = DownloadData(music_uri);
            //set filter to only when download is complete and register broadcast receiver
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            registerReceiver(downloadReceiver, filter);
        }
    }

    private long DownloadData(Uri uri) {

        long downloadReference;
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("جاري تحميل "+ name +"...");

        //Setting description of request
        request.setDescription("القران الكريم");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(MediaPlayerActivity.this, Environment.DIRECTORY_DOWNLOADS, number + ".mp3");

        //Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

//    private void Check_Music_Status(long Music_DownloadId) {
//
//        DownloadManager.Query MusicDownloadQuery = new DownloadManager.Query();
//        //set the query filter to our previously Enqueued download
//        MusicDownloadQuery.setFilterById(Music_DownloadId);
//
//        //Query the download manager about downloads that have been requested.
//        Cursor cursor = downloadManager.query(MusicDownloadQuery);
//        if (cursor.moveToFirst()) {
//            DownloadStatus(cursor, Music_DownloadId);
//        }
//
//    }

    private void DownloadStatus(Cursor cursor, long DownloadId) {

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = "Filename:\n" + filename;
                break;
        }

        if (DownloadId == Music_DownloadId) {

            Toast toast = Toast.makeText(MediaPlayerActivity.this,
                    "تم تحميل  "+name + "\n" + statusText + "\n" +
                            reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 800);
            toast.show();

        } else {

            Toast toast = Toast.makeText(MediaPlayerActivity.this,
                    "Image Download Status:" + "\n" + statusText + "\n" +
                            reasonText,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

            // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 3000);

        }

    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == Music_DownloadId) {

                Toast toast = Toast.makeText(MediaPlayerActivity.this,
                        "Music Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }

        }
    };

//    public void DownloadChecker() {
//        File applictionFile = new File(Environment.
//                getExternalStoragePublicDirectory(Environment
//                        .DIRECTORY_DOWNLOADS).getAbsolutePath() + number + ".mp3");
//
//        if (applictionFile.exists()) {
//            Toast.makeText(getApplicationContext(), "File Already Exists",
//                    Toast.LENGTH_LONG).show();
//        } else {
//            String servicestring = Context.DOWNLOAD_SERVICE;
//            DownloadManager downloadmanager;
//            downloadmanager = (DownloadManager) getSystemService(servicestring);
//            Uri uri = Uri.parse(path);
//            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setDestinationInExternalFilesDir(MediaPlayerActivity.this,
//                    Environment.DIRECTORY_DOWNLOADS, number + ".mp3");
//
//            Long reference = downloadmanager.enqueue(request);
//        }
//
//    }
}
