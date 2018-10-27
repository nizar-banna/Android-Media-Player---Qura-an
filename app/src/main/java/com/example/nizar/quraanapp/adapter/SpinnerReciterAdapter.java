package com.example.nizar.quraanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nizar.quraanapp.R;
import com.example.nizar.quraanapp.modal.ReciterObj;
import com.example.nizar.quraanapp.modal.Rewaya;

import java.util.ArrayList;

public class SpinnerReciterAdapter extends ArrayAdapter<ReciterObj> {
    public SpinnerReciterAdapter(Context context, ArrayList<ReciterObj> list){
        super(context,0,list);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner,parent,false);
        }

        TextView textViewSpinner = convertView.findViewById(R.id.textViewSpinner);
        ReciterObj currentItem = getItem(position);

        if(currentItem != null ){
//            for (Reciter e: currentItem) {
//
//            }
            textViewSpinner.setText(currentItem.getName());
            Log.d("Nizar","obj:"+currentItem.getName());


        }
        return convertView;
    }
}
