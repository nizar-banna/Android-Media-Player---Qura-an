package com.example.nizar.quraanapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nizar.quraanapp.R;
import com.example.nizar.quraanapp.modal.SurahDetails;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private Context mContext;
   private   List<SurahDetails> nameList;
    private OnClickListener mListener;


    public ListItemAdapter(List<SurahDetails> nameList , Context mContext) {
        this.nameList = nameList;
        this.mContext = mContext;
    }
    public interface OnClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SurahDetails name = nameList.get(position);
        holder.surah_name.setText(name.getName());

    }


    @Override
    public int getItemCount() {
        return nameList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        public TextView surah_name;

        public ViewHolder(View itemView) {
            super(itemView);
            surah_name = itemView.findViewById(R.id.surah_name);
            Typeface m_typeFace = Typeface.createFromAsset(itemView.getContext().getAssets(), "kfc_naskh-webfont.otf");
            surah_name.setTypeface(m_typeFace);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
