package com.android.androidplugin;

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

    private int sum(int aa, int bb) {
        return aa + bb;
    }

    @Override
    public void onClick(View v) {

    }
}
