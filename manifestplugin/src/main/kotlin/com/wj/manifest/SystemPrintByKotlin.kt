package com.wj.manifest

/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 *
 * 系统日志输出
 *
 * @author wenjing.liu
 */
object SystemPrintByKotlin {
    val TAG: String = "#@@#@@# ManifestProjectByKotlin #@@#@@# "
    val DEBUG: Boolean = true

    /**
     * 定义静态方法
     * (1)companion object { }:会在该类内部创建一个伴生类,而被修饰的方法为伴生类的实例方法,
     * 只不过Kotlin保证该类只会存在一个伴生类对象
     * 确确实实的静态方法:注解和顶层方法
     * (2)@JvmStatic
     * (3)顶层方法指的是定义在一个单独的类文件中,可通过@file:JvmName("")制定在java调用时的类名称,Kotlin会将所有的顶层方法编译成静态方法
     */
    @JvmStatic
    fun outPrintln(info: String) {
        if (DEBUG) {
            println(TAG + info)
        }
    }

}