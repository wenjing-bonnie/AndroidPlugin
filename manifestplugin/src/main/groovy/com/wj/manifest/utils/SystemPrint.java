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

            System.out.println(String.format("<= %s => :  %s ", getTag(tag), info));
        }
    }

    /**
     * \33[前背景色代号;背景色代号;数字m
     * 前背景色代号41-46
     * 背景色代号31-36
     * 数字m 1加粗 3斜体 4 下划线
     */
    private static void color(String info) {
        System.out.format("\33[32;1m%s%n", info);
    }

    public static void errorPrintln(String tag, String info) {
        if (DEBUG) {
            System.err.println(String.format("<= %s => :  %s ", getTag(tag), info));

        }
    }

    public static void outPrintln(String info) {
        outPrintln(TAG, info);
    }

    public static void errorPrintln(String info) {
        errorPrintln(TAG, info);
    }

    private static String getTag(String tag) {
        if (tag.length() <= 20) {
            return tag;
        }
        return String.format("%s...", tag.substring(0, 19));
    }
}
