package com.example.nizar.quraanapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainListActivity extends BaseActivity implements ListItemAdapter.OnClickListener  {
    AsyncHttpClient asyncHttpClient;
    List<SurahDetails> surahDetailsList;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        rv = findViewById(R.id.rv);
        loadData();
        rv.setLayoutManager( new LinearLayoutManager(this));
        final ListItemAdapter adapter = new ListItemAdapter(surahDetailsList,getApplicationContext());
        rv.setAdapter(adapter);
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get("http://api.alquran.cloud/surah",null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                JsonResponse resp = gson.fromJson(response.toString(),JsonResponse.class);
                surahDetailsList.addAll(resp.getData());
                adapter.notifyDataSetChanged();
                adapter.setOnItemClickListener(MainListActivity.this);
                Log.e("TAG", "on Success" + resp.getData());
                saveData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("TAG", "on failure" + errorResponse);

            }
        });


    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenced",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(surahDetailsList);
        editor.putString("list",json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferenced",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list",null);
        Type type = new TypeToken<ArrayList<SurahDetails>>(){}.getType();
        surahDetailsList = gson.fromJson(json,type);
        if (surahDetailsList == null){
            surahDetailsList = new ArrayList<>();
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(),MediaPlayerActivity.class);
        SurahDetails clicked = surahDetailsList.get(position);
        intent.putExtra("no",clicked.getNumber());
        intent.putExtra("name",clicked.getName());
        setResult(100,intent);
        Log.d("success","fffffff");
        startActivity(intent);
    }
}
