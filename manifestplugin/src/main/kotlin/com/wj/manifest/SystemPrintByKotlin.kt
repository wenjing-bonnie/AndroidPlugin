package com.wj.manifest

/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 *
 * 系统日志输出
 *
 * object:相当于类中的所有方法、属性都为静态方法,object类实例本身为单例,object也可以表示为一个匿名类或不存在对象
 * component object:相当于代码块内的属性、方法为静态的属性、方法.只不过Kotlin保证该类只会存在一个伴生类对象
 * @author wenjing.liu
 */
object SystemPrintByKotlin {
    val TAG: String = "ManifestProjectByKotlin"
    val DEBUG: Boolean = true

    /**
     * 定义静态方法
     * (1)companion object { }:会在该类内部创建一个伴生类,而被修饰的方法为伴生类的实例方法,
     * 只不过Kotlin保证该类只会存在一个伴生类对象
     * 确确实实的静态方法:注解和顶层方法
     * (2)@JvmStatic
     * (3)顶层方法指的是定义在一个单独的类文件中,可通过@file:JvmName("")制定在java调用时的类名称,Kotlin会将所有的顶层方法编译成静态方法
     */
    // @JvmStatic
    fun outPrintln(info: String, tag: String) {
        if (DEBUG) {
            println("<-  $tag  -> : $info")
        }
    }


    //  定义方法
    //fun  [方法名] ( [参数名] : [参数类型] ) : [返回类型]{
    //   ...
    //  return [返回值]
    //}
    //若无返回值,则可以去除: [返回类型]

    //@JvmStatic
    fun outPrintln(info: String) {
        outPrintln(info, TAG)
    }

}