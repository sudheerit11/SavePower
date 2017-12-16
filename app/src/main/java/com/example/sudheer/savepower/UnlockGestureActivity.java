package com.example.sudheer.savepower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class UnlockGestureActivity extends AppCompatActivity {

    Button btnRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_gesture);
        btnRecord=(Button)findViewById(R.id.btnRecordReadings);
        onClick();
    }
    public void onClick(){
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UnlockGestureActivity.this,
                        "Sensors Started Create Gesture for next 4 seconds", Toast.LENGTH_SHORT).show();

                new Timer().schedule(new TimerTask(){
                    public void run() {
                        storeReading();
                    }
                }, 4000);
                Toast.makeText(UnlockGestureActivity.this,"Recording Completed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void storeReading(){

    }

}
