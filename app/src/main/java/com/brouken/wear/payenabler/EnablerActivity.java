package com.brouken.wear.payenabler;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class EnablerActivity extends Activity {

    //private final String PKG = "com.google.android.deskclock";
    private final String PKG = "com.google.android.apps.walletnfcrel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchOrEnable(PKG);
        finish();
    }

    private void launchOrEnable(String pkg) {
        try {
            ApplicationInfo app = getPackageManager().getApplicationInfo(pkg, 0);

            if (app.enabled)
                launchApp(pkg);
            else
                enableAppViaPlayStore(pkg);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enableApp(String pkg) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkg));
        startActivity(intent);
    }

    private void launchApp(String pkg) {
        startActivity(getPackageManager().getLaunchIntentForPackage(pkg));
    }

    private void enableAppViaPlayStore(String pkg) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg))
        startActivity(intent);
    }
}
