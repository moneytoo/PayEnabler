package com.brouken.wear.payenabler;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

public class ExecutionService extends Service {

    private int timeout = 0;

    final Handler mHandler = new Handler();

    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (checkAndLaunch() || timeout <= 0) {
                timeout = 0;
                stopSelf();
            } else {
                timeout--;
                mHandler.postDelayed(this, 200);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean runningAlready = false;
        if (timeout > 0)
            runningAlready = true;

        // Poll app state every second for 30 seconds until giving up
        timeout = 5 * 30;

        if (!runningAlready)
            mHandler.post(mRunnable);

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkAndLaunch() {
        try {
            ApplicationInfo app = getPackageManager().getApplicationInfo(EnablerActivity.PKG, 0);

            if (app.enabled) {
                EnablerActivity.launchApp(getApplicationContext());
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
