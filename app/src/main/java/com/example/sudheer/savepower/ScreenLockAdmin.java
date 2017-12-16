package com.example.sudheer.savepower;
/**
 * Created by anirudh on 11/29/2017.
 */

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class ScreenLockAdmin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context,"Admin Permissions Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Toast.makeText(context,"Admin Permission Disabled", Toast.LENGTH_SHORT).show();
        return super.onDisableRequested(context, intent);

    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context,"Admin Permission Disabled Enable it to lock the screen", Toast.LENGTH_SHORT).show();
    }
}

