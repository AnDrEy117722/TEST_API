package com.bnet.testapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WatchOfRecord extends AppCompatActivity {

    private TextView body_of_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_of_record);

        Intent intent = getIntent();
        String body = intent.getStringExtra("body");

        body_of_record = findViewById(R.id.body_of_record);
        body_of_record.setText(body);
    }

}
