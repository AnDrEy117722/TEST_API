package com.bnet.testapi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

@SuppressLint("ParcelCreator")
public class MainActivity extends AppCompatActivity implements CallBack{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addBtn;
    private Response response = null;
    public String postUrl= "http://bnet.i-partner.ru/testAPI/";
    public static String token = "tgNFq4D-3Y-19RD5YA";
    private List<Record> records = new ArrayList<>();
    public String session = "";
    private CallBack callBack;
    private LinearLayout linearLayout;
    private Button update_button;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.user_records);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Adapter.getInstance());

        linearLayout = findViewById(R.id.internet_connection);
        update_button = findViewById(R.id.update);
        update_button.setOnClickListener(UPDATE);

        scrollView = findViewById(R.id.scroll);

        addBtn = findViewById(R.id.add);
        addBtn.setOnClickListener(AddBtn);

        this.callBack = (CallBack) this;

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("test", String.valueOf(hasConnection(this)));

        if (hasConnection(this)) {
            start();
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }

    }

    private final View.OnClickListener UPDATE = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasConnection(MainActivity.this)) {
                start();
            }
        }
    };

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo= cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private void start(){

        addBtn.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String html_sessions = String.valueOf(getSessions());
                Document document = Jsoup.parse(html_sessions);
                Elements elements = document.select("body > pre");
                String string = StringUtils.substringBetween(elements.text(), "sessions:", "data:");
                String string1 = StringUtils.substringBetween(string, "Array\n(", ")");
                if (string1.equals("\n")){
                    try {
                        newSession(postUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String string2 = StringUtils.substringBetween(string, "Array\n(", ")\n\n)");
                    session = StringUtils.substringBetween(string2, "[session] => ", "[date_created]").trim();
                    callBack.onCallBackGet();
                }
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        callBack.onCallBackStartAdapter();
    }

    private Object getToken(){

        final MediaType mediaType
                = MediaType.parse("application/x-www-form-urlencoded");

        OkHttpClient httpClient = new OkHttpClient();
        String url = postUrl;

        String data ="a=get_token&name=Andrey%20Gluhov&email=gluhov01%40inbox.ru&=";
        byte[] out = data.getBytes();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, out))
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e(TAG, "error in getting response for json post request okhttp");
        }
        return null;
    }

    private Object getSessions(){

        OkHttpClient httpClient = new OkHttpClient();
        String url = "http://bnet.i-partner.ru/testAPI/?a=test&token=tgNFq4D-3Y-19RD5YA";

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e(TAG, "error in getting response for json post request okhttp");
        }
        return null;
    }

    private void newSession(String postUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("a", "new_session")
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .addHeader("token", token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("test", String.valueOf(e));
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().toString() != null) {
                    parseJSON(response.peekBody(2048).string(), "new session");
                }
            }
        });
    }

    private Object getEntries(String postUrl, String session) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("a", "get_entries")
                .addFormDataPart("session", session)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .addHeader("token", token)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e(TAG, "error in getting response for json post request okhttp");
        }
        return null;
    }

    private void addEntry(String postUrl, String session, String text) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("a", "add_entry")
                .addFormDataPart("session", session)
                .addFormDataPart("body", text)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .addHeader("token", token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("test", String.valueOf(e));
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.e("test", response.body().string());
            }
        });
    }

    private void parseJSON(String json, String ACTION){
        try {
            switch (ACTION) {
                case "new session": {
                    JSONObject jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    session = jsonObject1.getString("session");
                    callBack.onCallBackGet();
                }
                case "get entries": {

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray Array = jsonObject.getJSONArray("data");
                    JSONArray Array1 = Array.getJSONArray(0);
                    records.clear();

                    for (int i = 0; i < Array1.length(); i++){
                        JSONObject finish = (JSONObject) Array1.get(i);
                        String id = finish.getString("id");
                        String body = finish.getString("body");
                        String da = finish.getString("da");
                        String dm = finish.getString("dm");
                        Record record = new Record(id, body, da, dm);
                        records.add(record);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private final View.OnClickListener AddBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CreateRecord.class);
            ApplicationM.callBack = callBack;
            startActivity(intent);
        }
    };

    @Override
    public void onCallBackGet() {
        try {
            String get_entries = String.valueOf(getEntries(postUrl, session));
            parseJSON(get_entries, "get entries");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallBackAdd(String text) {
        try {
            addEntry(postUrl, session, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallBackStartAdapter() {
        Adapter.getInstance().setRecords(records, callBack);
    }

    @Override
    public void onCallBackStartActivity(String text) {
        Intent intent = new Intent(MainActivity.this, WatchOfRecord.class);
        intent.putExtra("body", text);
        startActivity(intent);
    }
}