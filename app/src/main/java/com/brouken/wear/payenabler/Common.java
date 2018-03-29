package com.brouken.wear.payenabler;

import android.content.Context;

public class Common {

    //public static final String PKG = "com.runtastic.android";
    public static final String PKG = "com.google.android.apps.walletnfcrel";

    public static void launchApp(Context context) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(PKG));
    }
}
