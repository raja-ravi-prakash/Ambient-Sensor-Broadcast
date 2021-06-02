package com.ronin.sensorlogging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;

public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        setUpUI();
        startService();
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("Service Status"));
    }

    protected void onPause (){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }

    void setUpUI(){
        String url = "https://media.giphy.com/media/dsWccpBcLVYNHm0TR8/giphy.gif";
        ImageView imageView = findViewById(R.id.wv);
        Glide.with(this).load(url).into(imageView);
    }

    void startService(){
        Intent i = new Intent(getApplicationContext(),SensorService.class);
        i.putExtra("ip",getIntent().getStringExtra("ip"));
        i.putExtra("port",getIntent().getStringExtra("port"));
        this.startService(i);
    }

    public void stopService(View v){
        Intent i = new Intent(getApplicationContext(),SensorService.class);
        stopService(i);
        this.finish();
    }

}