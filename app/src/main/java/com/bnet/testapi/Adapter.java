package com.bnet.testapi;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Record> records = new ArrayList<>();
    private CallBack callBack;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Record record = records.get(position);

        long date_ = Long.parseLong(record.getDa());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(date_ * 1000);

        holder.da.setText(formatter.format(date));

        if (!record.getDa().equals(record.getDm())){
            long date_1 = Long.parseLong(record.getDm());
            Date date1 = new Date(date_1 * 1000);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.dm.setText(formatter.format(date1));
        }

        if (record.getBody().trim().length() > 200){
            String short_body = record.getBody().trim().substring(0, 199) + "...";
            holder.record.setText(short_body);
        } else holder.record.setText(record.getBody().trim());

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCallBackStartActivity(record.getBody().trim());
            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<Record> records, CallBack callBack){
        this.records = records;
        this.callBack = callBack;
        notifyDataSetChanged();
    }

    public static Adapter sInstance;
    public Adapter() {

    }
    public synchronized static Adapter getInstance(){
        if (sInstance == null) {
            sInstance = new Adapter();
        }
        return sInstance;
    }
}
