package com.example.sudheer.savepower;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnSensorScan,btnLockScreen,btnEnablePermission;
    static DevicePolicyManager devicePolicyManager;
    ActivityManager activityManager;
    static ComponentName componentName;
    private static  final int RESULT_ENABLE=1;
    boolean disable=false;
    public static Map<Integer,SetSettings> settingsMap=new HashMap<>();
    String[] listViewElements={"Screen Rotations","Swipe Screen ","Pocket Sensor"};
    ListView listView;
    private final String TAG="MainActivity";
    Map<Integer,SetSettings> map;
    SensorScan sensorScan;
    private PowerManager powerManager;


    public MainActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAdminPermission();
        initView();
        populateListView();
        onClick();
    }
    public void initView(){
        btnSensorScan=(Button)findViewById(R.id.btnSensorScan);
        listView=(ListView)findViewById(R.id.listView);
    }

    public void populateListView(){
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.items,listViewElements);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
                settingsIntent.putExtra("Positions",position);
                startActivity(settingsIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.unlockGesture:
                Intent intent= new Intent(MainActivity.this,UnlockGestureActivity.class);
                startActivity(intent);
                break;

            case R.id.disable:
                sensorScan=new SensorScan(this);
                sensorScan.stop();
                Toast.makeText(MainActivity.this,"Sensors Disabled", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public void onClick(){
        btnSensorScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Sensors Started", Toast.LENGTH_SHORT).show();
                sensorScan=new SensorScan(MainActivity.this);
                sensorScan.start();
                getSettings();
            }
        });

    }

    public void disblePermissions(){
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    public void  initAdminPermission(){
        devicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        componentName=new ComponentName(this,ScreenLockAdmin.class);
        getAdminPermission();

    }

    public void getAdminPermission(){
        Intent intent= new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added.");
        startActivityForResult(intent,RESULT_ENABLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getSettings(){
        try{
            Log.d(TAG, "onClick: "+settingsMap.size());
            for(Map.Entry<Integer,SetSettings>entry:settingsMap.entrySet()){
                SetSettings setting=entry.getValue();
                Log.d(TAG, "onClick: "+setting.getSelectedMode()+" "+setting.getTimeValue()+"  "
                        +setting.isLock()+" "+setting.isModeEnabled());
            }

        }catch (NullPointerException e){
            Log.e(TAG, "onClick: "+e);
        }
    }


    public static void lockScreen(){
        boolean active=devicePolicyManager.isAdminActive(componentName);
        if(active){
            devicePolicyManager.lockNow();

        }
        else{
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            sensorScan.stop();
        }catch (NullPointerException exp){
        }
    }
}
