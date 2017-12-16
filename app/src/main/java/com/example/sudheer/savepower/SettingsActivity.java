package com.example.sudheer.savepower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.sudheer.savepower.MainActivity.settingsMap;

public class SettingsActivity extends AppCompatActivity {


    Button btnEnableMode;
    Switch lockScreenLockSwitch;
    SeekBar timeSeekBar;
    TextView timeValue;
    private int modeSelected= Integer.MAX_VALUE;
    private boolean isLockEnabled=true,ismodeEnabled=false;
    private float time=0f;
    private final String TAG="SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViewGroup();
        onClick();
        timeValue.setText(String.valueOf(time));
    }
    public void initViewGroup(){
        Intent mIntent= getIntent();
        modeSelected=mIntent.getIntExtra("Positions",0);
        btnEnableMode=(Button)findViewById(R.id.btnSettingOn);
        lockScreenLockSwitch=(Switch)findViewById(R.id.btnSwitchLock);
        timeSeekBar=(SeekBar)findViewById(R.id.timeSeekBar);
        timeValue=(TextView)findViewById(R.id.txtTimeVal);
    }

    public void onClick(){
        btnEnableMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"Settings Enabled", Toast.LENGTH_SHORT).show();
                ismodeEnabled=true;
                SetSettings settings= new SetSettings(modeSelected,ismodeEnabled,isLockEnabled,time);
                settingsMap.put(modeSelected,settings);

                    new Timer().schedule(new TimerTask(){
                    public void run() {
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    }
                }, 2000);
            }
        });
        lockScreenLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isLockEnabled=false;
                }
                else{
                    isLockEnabled=true;
                }
            }
        });
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time=(float)progress/5;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}

class SetSettings{
    private int selectedMode;
    private boolean modeEnabled;
    private boolean lock;
    private float timeValue;

    public SetSettings(int selectedMode,boolean modeEnabled,boolean lock,float timeValue){
        this.selectedMode=selectedMode;
        this.modeEnabled=modeEnabled;
        this.lock=lock;
        this.timeValue=timeValue;
    }

    public int getSelectedMode() {
        return selectedMode;
    }

    public boolean isModeEnabled() {
        return modeEnabled;
    }

    public boolean isLock() {
        return lock;
    }

    public float getTimeValue() {
        return timeValue;
    }
}
