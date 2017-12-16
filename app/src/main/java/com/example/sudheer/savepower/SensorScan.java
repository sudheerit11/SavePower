package com.example.sudheer.savepower;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by anirudh on 11/29/2017.
 */

public class SensorScan implements Runnable {

    private static double xaxis,yaxis,zaxis;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private Context mContext;
    private List<Sensor> sensorList=new ArrayList<>();
    private Thread t;
    private String threadName="SensorThread";
    private final int samplingPeriod=100;
    float proximitySensor,lightIntensity;
    public final int timedelay=2000;
    KeyguardManager km;


    public SensorScan(Context context){
        this.mContext=context;
    }

    @Override
    public void run() {
        mSensorManager=(SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        getSensorList(mSensorManager);
        mSensorListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType()== Sensor.TYPE_PROXIMITY){
                    Log.d(TAG, "onSensorChanged: Proximity Intensity"+event.values[0]);
                    proximitySensor=event.values[0];
                    if(proximitySensor<=3){
                        new Timer().schedule(new TimerTask(){
                            public void run() {
                                MainActivity.lockScreen();
                            }
                        }, timedelay);
                    }
                }
                // light sensor
                if(event.sensor.getType() == Sensor.TYPE_LIGHT){

                    Log.d(TAG, "light Intensity"+event.values[0]);
                    lightIntensity=event.values[0];
                    if(lightIntensity==0 && proximitySensor<=3){
                        new Timer().schedule(new TimerTask(){
                            public void run() {
                                MainActivity.lockScreen();
                            }
                        }, timedelay);
                    }
                }

                if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
                     xaxis=event.values[0];
                    yaxis=event.values[1];
                     zaxis=event.values[2];
                    Log.d("Ttest", "onSensorChanged: Xaxis: "+xaxis+" Yaxis: "+yaxis+" Zaxis: "+zaxis);
                   //***********************Write COde here X y z axis conditions write if else loops*******************
                    new Timer().schedule(new TimerTask(){

                        public void run() {
                            if(xaxis > 8 && zaxis < -1 ){ //left rotaion x>8 z< -1
                                MainActivity.lockScreen();
                            }else if (xaxis < -8 && zaxis < -1){//right rotation x< -8 z< -1
                                MainActivity.lockScreen();
                            }else if (yaxis < -8 && zaxis < 3){ //upside y < -8 z <3
                                MainActivity.lockScreen();
                            }
                        }
                    }, timedelay);
                }
                if(event.sensor.getType()== Sensor.TYPE_GAME_ROTATION_VECTOR){
                    Log.d(TAG, "onSensorChanged: theta"+event.values[0]);
                    Log.d(TAG, "onSensorChanged: phi"+event.values[1]);
                    Log.d(TAG, "onSensorChanged: tsi"+event.values[2]);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        for(Sensor sensor:sensorList){
            Log.d("Register Sensor", "Sensor reg "+sensor.getName());
            mSensorManager.registerListener(mSensorListener,sensor,samplingPeriod);
        }
    }

    public void getSensorList(SensorManager sensorManager){
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)!=null){
            sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        }
        else{
            Log.d(TAG, "getSensorList: LIgt Sensor Not found ");
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }
        else{
            Log.d(TAG, "getSensorList: Accelerometer Sensor Not found ");
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)!=null){
            sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR));
        }
        else {
            Log.d(TAG, "getSensorList: Gyroscope not found");
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!=null){
            sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        }
        else {
            Log.d(TAG, "getSensorList: Light Sensor  not found");
        }

    }

    public void start(){

        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public synchronized void stop(){

        for(Sensor sensor:sensorList){
            mSensorManager.unregisterListener(mSensorListener,sensor);
        }
        sensorList.removeAll(sensorList);
    }

    public void turnScreenOn() {
        km = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");


        if ((wakeLock != null) &&           // we have a WakeLock
                (wakeLock.isHeld() == false)) {  // but we don't hold it
            wakeLock.acquire();
        }

    }

}
