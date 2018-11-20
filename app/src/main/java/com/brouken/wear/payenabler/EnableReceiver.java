package com.brouken.wear.payenabler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class EnableReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String pkg = intent.getData().getSchemeSpecificPart();
        if (isAppEnabled(context, pkg))
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(pkg));
    }

    public static boolean isAppEnabled(final Context context, final String pkg) {
        try {
            final ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(pkg, 0);
            return applicationInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
