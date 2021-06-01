package com.ronin.sensorlogging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor pressure;

    public MainActivity() throws UnknownHostException, SocketException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }
    int port = 8000;
    InetAddress ip = InetAddress.getByName("192.168.0.4");
    DatagramSocket client_socket = new DatagramSocket(port);
    @Override
    public void onSensorChanged(SensorEvent event) {
        Float blaa = event.values[0];
        String data = "Sensor Data at (" + new Date().toString() + ") - " + blaa.toString() + "\n";
        byte[] send_data = data.getBytes();
        DatagramPacket send_packet = new DatagramPacket(send_data,data.length(), ip, port);
        try {
            client_socket.send(send_packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}