package com.android.wj.debug.utils;

/**
 * Created by wenjing.liu on 2021/7/19 in J1.
 * <p>
 * 输出日志信息
 *
 * @author wenjing.liu
 */
public class SystemOutPrintln {
    private static final boolean DEBUG = false;
    private static String TAG = " ~*~*~*~*~*~* DebugPlugin ~*~*~*~*~*~* ";

    /**
     * 前面自动添加tag，输出日志信息
     *
     * @param info
     */
    public static void println(String info) {
        if (DEBUG) {
            System.out.println(String.format("%s %s", TAG, info));
        }
    }
}
