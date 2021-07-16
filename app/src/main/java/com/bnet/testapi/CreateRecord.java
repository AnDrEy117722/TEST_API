package com.bnet.testapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CreateRecord extends AppCompatActivity {

    private Button save, cancel;
    private CallBack callBack;
    private EditText record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_record);

        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        save.setOnClickListener(Save);
        cancel.setOnClickListener(Cancel);

        record = findViewById(R.id.record_edit);

        callBack = ApplicationM.callBack;
    }

    private final View.OnClickListener Save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callBack.onCallBackAdd(String.valueOf(record.getText()));
            finish();
        }
    };

    private final View.OnClickListener Cancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("test", String.valueOf(record.getText()));
            finish();
        }
    };

}
