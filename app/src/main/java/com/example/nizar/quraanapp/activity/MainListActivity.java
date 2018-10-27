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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.nizar.quraanapp.adapter.ListItemAdapter;
import com.example.nizar.quraanapp.R;
import com.example.nizar.quraanapp.adapter.SpinnerReciterAdapter;
import com.example.nizar.quraanapp.adapter.SpinnerRewayaAdapter;
import com.example.nizar.quraanapp.language.BaseActivity;
import com.example.nizar.quraanapp.adapter.ListItemAdapter;
import com.example.nizar.quraanapp.modal.JsonResponse;
import com.example.nizar.quraanapp.modal.ReciterObj;
import com.example.nizar.quraanapp.modal.Rewaya;
import com.example.nizar.quraanapp.modal.SurahDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends BaseActivity implements ListItemAdapter.OnClickListener ,AdapterView.OnItemSelectedListener {
    ArrayList<SurahDetails> surahDetailsList;
    ArrayList<Rewaya> rewayaList;
    ArrayList<String> reciterList;
    ArrayList<ReciterObj> keyArray;
    Spinner spinner_rewaya;
    Spinner spinner_reciter;
    SpinnerRewayaAdapter spinnerRewayaAdapter;
    SpinnerReciterAdapter spinnerReciterAdapter;
    RecyclerView rv;
    Button btn;
    TextView tv;
    Context mContext;
    String server;
    private static final String FILENAME = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        surahDetailsList = new ArrayList<>();
        rewayaList = new ArrayList<>();
        reciterList = new ArrayList<>();
        keyArray = new ArrayList<>();

        rv = findViewById(R.id.rv);
//        btn = findViewById(R.id.btn);
//        tv = findViewById(R.id.tv);

//        loadData();
        // where call me on messanger as voice
        // quickly

        rv.setLayoutManager( new LinearLayoutManager(this));
        final ListItemAdapter adapter = new ListItemAdapter(surahDetailsList,getApplicationContext());
        rv.setAdapter(adapter);

        Gson gson = new Gson();
        JsonResponse resp = gson.fromJson(loadJSONFromAsset(mContext,"surah.json"),JsonResponse.class);
        surahDetailsList.addAll(resp.getData());
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MainListActivity.this);

        Type listType = new TypeToken<List<Rewaya>>() {
        }.getType();
        ArrayList<Rewaya> postlist;
        postlist = gson.fromJson(loadJSONFromAsset(mContext,"reciters.json"),listType);
        rewayaList.addAll(postlist);

        spinner_rewaya = findViewById(R.id.spinner_rewaya);
        spinnerRewayaAdapter = new SpinnerRewayaAdapter(this,rewayaList);
        spinner_rewaya.setAdapter(spinnerRewayaAdapter);
        spinner_rewaya.setOnItemSelectedListener( this);

        spinner_reciter = findViewById(R.id.spinner_reciter);
        spinnerReciterAdapter = new SpinnerReciterAdapter(this,keyArray);
        spinner_reciter.setAdapter(spinnerReciterAdapter);
        spinner_reciter.setOnItemSelectedListener(this);

    }

    private void displaySelectedItem(Rewaya rewaya){
        ArrayList<ReciterObj> obj = rewaya.getVal();
         keyArray.addAll(obj);
        spinnerReciterAdapter.notifyDataSetChanged();
        Log.d("On","selected d1");

    }

    private void displaySelectedReciter(ReciterObj reciterObj,View view){
           server = reciterObj.getServer();
           String rewayaName = reciterObj.getRewaya();
           String reciterName = reciterObj.getName();
        Log.d("On","selected d2"+server);
        SharedPreferences preferences=getSharedPreferences("PhoneBook",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("server",server);
        editor.putString("rewayaName",rewayaName);
        editor.putString("reciterName",reciterName);
        editor.apply();
            }
    public String loadJSONFromAsset(Context context ,String fileName) {
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
//    private void saveArrayList(ArrayList<SurahDetails> arrayList) {
//        try {
//            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
//            out.writeObject(arrayList);
//            out.close();
//            fileOutputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ArrayList<SurahDetails> getSavedArrayList() {
//        ArrayList<SurahDetails> savedArrayList = null;
//        try {
//            FileInputStream inputStream = openFileInput(FILENAME);
//            ObjectInputStream in = new ObjectInputStream(inputStream);
//            savedArrayList = (ArrayList<SurahDetails>) in.readObject();
//            in.close();
//            inputStream.close();
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return savedArrayList;
//    }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        switch (adapterView.getId()) {
//            case R.id.spinner_rewaya:
//
//                break;
//
//            case R.id.spinner_reciter:
//
//
//                break;
//        }
        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinner_rewaya)
        {
            keyArray.clear();
            Rewaya r = (Rewaya) adapterView.getSelectedItem();
            displaySelectedItem(r);
            Log.d("On","selected 1");
        }
         if(spinner.getId() == R.id.spinner_reciter)
        {
            ReciterObj reciterObj = (ReciterObj) adapterView.getSelectedItem();
            displaySelectedReciter(reciterObj,view);
//            TextView textView = (TextView) findViewById(R.id.tv);
//            String str = (String) adapterView.getItemAtPosition(i);
//            textView.setText(str);
            Log.d("On","selected 2");
        }
        Log.d("On","onItemSelected  "+spinner.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
