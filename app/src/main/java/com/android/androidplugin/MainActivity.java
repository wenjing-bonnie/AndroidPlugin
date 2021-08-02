package com.android.androidplugin;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(getClass().getSimpleName(), "channel = " + getChannelFromAndroidMainfest());
    }

    private String getChannelFromAndroidMainfest() {
        String metaChannel = "";
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (info == null || info.metaData == null) {
                return metaChannel;
            }
            metaChannel = info.metaData.getString("channel");
        } catch (PackageManager.NameNotFoundException e) {
        }
        return metaChannel;
    }
}