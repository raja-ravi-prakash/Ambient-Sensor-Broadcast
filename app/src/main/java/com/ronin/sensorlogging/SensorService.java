package com.ronin.sensorlogging;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Date;

public class SensorService extends Service implements SensorEventListener {
    public SensorService() {
    }
    final private String TAG = "SensorDebugLogging";

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setSocket(intent);
        return START_STICKY;
    }

    private SensorManager sm;

    void configureSensorData(){
        Log.d(TAG,"Configuring Sensor Service");

        sm = (SensorManager)getSystemService(Service.SENSOR_SERVICE);
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor p = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Log.d(TAG,"Sensor Service Configured");

        sm.registerListener(this, s,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, p,SensorManager.SENSOR_DELAY_NORMAL);

        Log.d(TAG,"Setting Up Listener");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float val = sensorEvent.values[0];
        sendPacket(Float.toString(val),sensorEvent);
    }

    InetAddress ip;
    int port;
    DatagramSocket ds;
    void setSocket(Intent i){
        try {
            ip = InetAddress.getByName(i.getStringExtra("ip"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d(TAG,"Unknown Host");
            Toast.makeText(this,"Unknown Host",Toast.LENGTH_LONG).show();
            stopAndReturn();
        }

        port = Integer.parseInt(i.getStringExtra("port"));

        try {
            ds = new DatagramSocket();
            configureSensorData();

        } catch (SocketException e) {
            e.printStackTrace();
            Log.d(TAG,"Socket Exception");
            Toast.makeText(this,"Network Unavailable",Toast.LENGTH_LONG).show();
            stopAndReturn();
        }
    }

    void stopAndReturn(){
        stopSelf();
    }

    float lastProx=5;
    void sendPacket(String sensorValue,SensorEvent se){

        if(se.sensor.getType() == Sensor.TYPE_PROXIMITY)
            lastProx = Float.parseFloat(sensorValue);

        String type;
        String value;

        if(lastProx < 5){
            type= "Proximity";
            value = "MinValue";
        }
        else {
            type = "Ambient";
            value = sensorValue;
        }

        String data = MessageFormat.format("{0}:{1}",type,value);

        DatagramPacket d = new DatagramPacket(data.getBytes(),data.length(),ip,port);

        try {
            ds.send(d);
        } catch(Exception e){
            e.printStackTrace();
            Log.d(TAG,"Connection Failed");
            Toast.makeText(this,"Unable to send Data",Toast.LENGTH_LONG).show();
            stopAndReturn();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
        Intent intent = new Intent ("Service Status");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}