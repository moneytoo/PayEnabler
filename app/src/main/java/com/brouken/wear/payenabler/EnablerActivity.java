package com.brouken.wear.payenabler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class EnablerActivity extends Activity {

    //private static final String PKG = "com.runtastic.android";
    public static final String PKG = "com.google.android.apps.walletnfcrel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchOrEnable(this);
        finish();
    }

    private void launchOrEnable(final Context context) {
        try {
            final ApplicationInfo app = getPackageManager().getApplicationInfo(PKG, 0);

            if (app.enabled)
                launchApp(context);
            else
                enableAppViaPlayStore(PKG);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enableAppViaPlayStore(final String pkg) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg))
        startActivity(intent);
        startService(new Intent(this, ExecutionService.class));
    }

    public static void launchApp(final Context context) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(PKG));
    }
}
