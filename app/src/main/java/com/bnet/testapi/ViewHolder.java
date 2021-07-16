package com.bnet.testapi;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    protected TextView record;
    protected TextView da;
    protected TextView dm;
    protected LinearLayout linearLayout, clickLayout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        record = itemView.findViewById(R.id.record);
        da = itemView.findViewById(R.id.da);
        dm = itemView.findViewById(R.id.dm);
        linearLayout = itemView.findViewById(R.id.dm_layout);
        clickLayout = itemView.findViewById(R.id.click);

    }


}
