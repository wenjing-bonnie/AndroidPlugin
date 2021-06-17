package com.wj.global.log;

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 * <p>
 * 日志输出
 *
 * @author wenjing.liu
 */
public class SystemOutPrint {
    private final static boolean DEBUG = true;

    public static void print(String info) {
        if (DEBUG) {
            System.out.println("~~~~~~GlobalGradleProject~~~~~~~~   " + info);
        }
    }
}
