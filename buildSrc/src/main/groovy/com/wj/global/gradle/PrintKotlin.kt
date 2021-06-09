package com.wj.global.gradle

/**
 * Created by wenjing.liu on 2021/6/9 in J1.
 *
 * 输出日志
 *
 * @author wenjing.liu
 */
class PrintKotlin {
    companion object {
        @JvmStatic
        fun print(info: String) {
            print("~~~~~~~~   " + info)
        }
    }
}