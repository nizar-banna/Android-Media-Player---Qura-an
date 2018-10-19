package com.example.nizar.quraanapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.example.nizar.quraanapp.adapter.ListItemAdapter;
import com.example.nizar.quraanapp.R;
import com.example.nizar.quraanapp.language.BaseActivity;
import com.example.nizar.quraanapp.adapter.ListItemAdapter;
import com.example.nizar.quraanapp.modal.JsonResponse;
import com.example.nizar.quraanapp.modal.SurahDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainListActivity extends BaseActivity implements ListItemAdapter.OnClickListener  {
    AsyncHttpClient asyncHttpClient;
    ArrayList<SurahDetails> surahDetailsList;
    ArrayList<SurahDetails> savedList;
    RecyclerView rv;
    Button btn;
    Context mContext;
    private static final String FILENAME = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        surahDetailsList = new ArrayList<>();
        rv = findViewById(R.id.rv);
        btn = findViewById(R.id.btn);
//        loadData();
        rv.setLayoutManager( new LinearLayoutManager(this));
        final ListItemAdapter adapter = new ListItemAdapter(surahDetailsList,getApplicationContext());
        rv.setAdapter(adapter);
        Gson gson = new Gson();
        JsonResponse resp = gson.fromJson(loadJSONFromAsset(mContext),JsonResponse.class);
        surahDetailsList.addAll(resp.getData());
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MainListActivity.this);

      //  Log.d("ddddddd","dddd"+loadJSONFromAsset(mContext));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainListActivity.this, "saved"+savedList, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
//            InputStream is = context.getAssets().open("surah.json");
            AssetManager mngr = getAssets();
            InputStream is = mngr.open("surah.json");

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
    private void saveArrayList(ArrayList<SurahDetails> arrayList) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arrayList);
            out.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SurahDetails> getSavedArrayList() {
        ArrayList<SurahDetails> savedArrayList = null;
        try {
            FileInputStream inputStream = openFileInput(FILENAME);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            savedArrayList = (ArrayList<SurahDetails>) in.readObject();
            in.close();
            inputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return savedArrayList;
    }

//    private void saveData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenced",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(surahDetailsList);
//        editor.putString("list",json);
//        editor.apply();
//    }
//    private void loadData(){
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenced",MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("list",null);
//        Type type = new TypeToken<ArrayList<SurahDetails>>(){}.getType();
//        surahDetailsList = gson.fromJson(json,type);
//        if (surahDetailsList == null){
//            surahDetailsList = new ArrayList<>();
//        }
//    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(),MediaPlayerActivity.class);
        SurahDetails clicked = surahDetailsList.get(position);
        intent.putExtra("no",clicked.getNumber());
        intent.putExtra("name",clicked.getName());
        Log.d("success","fffffff");
        startActivity(intent);

    }
}
