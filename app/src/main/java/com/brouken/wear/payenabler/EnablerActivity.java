package com.brouken.wear.payenabler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class EnablerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchOrEnable(this);
        finish();
    }

    private void launchOrEnable(Context context) {
        try {
            ApplicationInfo app = getPackageManager().getApplicationInfo(Common.PKG, 0);

            if (app.enabled)
                Common.launchApp(context);
            else
                enableAppViaPlayStore(Common.PKG);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    private void enableApp(String pkg) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkg));
        startActivity(intent);
    }
    */

    private void enableAppViaPlayStore(String pkg) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg))
        startActivity(intent);
        startService(new Intent(this, ExecutionService.class));
    }
}
