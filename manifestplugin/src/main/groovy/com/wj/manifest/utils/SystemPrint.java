package com.wj.manifest.utils;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 日志输出
 *
 * @author wenjing.liu
 */
public class SystemPrint {

    private static final boolean DEBUG = true;
    private static final String TAG = "ManifestProject";

    public static void outPrintln(String tag, String info) {
        if (DEBUG) {
            System.out.println(String.format("#@@#@@# %s #@@#@@#  %s ", tag, info));
        }
    }

    public static void errorPrintln(String tag, String info) {
        if (DEBUG) {
            System.err.println(String.format("#@@#@@# %s #@@#@@#  %s ", tag, info));

        }
    }

    public static void outPrintln(String info) {
        outPrintln(TAG, info);
    }

    public static void errorPrintln(String info) {
        errorPrintln(TAG, info);
    }
}
