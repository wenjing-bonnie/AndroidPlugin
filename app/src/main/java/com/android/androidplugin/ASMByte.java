package com.android.androidplugin;

import android.util.Log;
import android.view.View;

/**
 * Created by wenjing.liu on 2021/8/6 in J1.
 * <p>
 * 用来学习字节码的测试文件
 *
 * @author wenjing.liu
 */
public class ASMByte implements View.OnClickListener {
    private int a = 10;
    private static int c = 20;
    private String name;

    private int sum(int aa, int bb) {
        long beginTime = System.currentTimeMillis();
        System.out.println("Other running code");
        long callTime = System.currentTimeMillis() - beginTime;
        // Log.d(getClass().getSimpleName(), String.format("%s cost time is [%l]ms", name, callTime));
        Log.d("AUTO", String.format("cost time is [%d]ms", callTime));

        return aa + bb;
    }

    @Override
    public void onClick(View v) {

    }
}
