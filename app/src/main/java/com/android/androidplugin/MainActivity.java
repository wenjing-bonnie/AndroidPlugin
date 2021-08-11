package com.android.androidplugin;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends Activity {
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tv_info);
        setInfoText("The channel is " + getChannelFromAndroidManifest() + " , " + BuildConfig.CHANNEL, null);
    }

    private void setInfoText(String info, int[] nums) {
        tvInfo.setText(info);
    }

    public void btnOnClick(View view) {
        tvInfo.setTextColor(Color.RED);
    }

    /**
     * 获取meta-data中的channel
     *
     * @return
     */
    private String getChannelFromAndroidManifest() {
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


    private int sum(int a, int b) {
        Log.d(this.getClass().getSimpleName(),""+a);
        return a + b;
    }

}