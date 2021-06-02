package com.ronin.sensorlogging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    final private String TAG = "SensorDebugLogging";
    EditText ipField, portField;


    public MainActivity()  {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ipField = findViewById(R.id.ip);
        portField = findViewById(R.id.port);
    }

    public void connect(View view) {
        String ip = ipField.getText().toString();
        String port = portField.getText().toString();

        Log.d(TAG, MessageFormat.format("IP = {0} PORT = {1}",ip,port));

        Intent i = new Intent(getApplicationContext(),SensorActivity.class);
        i.putExtra("ip",ip);
        i.putExtra("port",port);
        this.startActivity(i);
    }

}