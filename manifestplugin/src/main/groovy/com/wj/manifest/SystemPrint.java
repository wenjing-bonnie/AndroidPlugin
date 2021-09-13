package com.wj.manifest;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 日志输出
 *
 * @author wenjing.liu
 */
public class SystemPrint {

    private static final boolean DEBUG = true;

    public static void outPrintln(String info) {
        if (DEBUG) {
            System.out.println("#@@#@@#@@#@@#@@#@@# ManifestProject #@@#@@#@@#@@#@@#@@# " + info);
        }
    }

    public static void errorPrintln(String info){
        if (DEBUG) {
            System.err.println("#@@#@@#@@#@@#@@#@@# ManifestProject #@@#@@#@@#@@#@@#@@# " + info);
        }
    }
}
