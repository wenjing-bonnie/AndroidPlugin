package com.wj.manifest;

import java.io.File;

/**
 * Created by wenjing.liu on 2021/9/18 in J1.
 * 扩展属性
 * 然后再用来学习下kotlin语法
 *
 * Java的类和方法默认是open的，而Kotlin中默认都是final的。
 * 类的修饰符：
 * final:不可被继承,相关的成员不能被复写.默认为final
 * open:可以被继承
 * abstract:必须被复写
 * enum:枚举类
 * data:数据类.该类中没有函数只有属性定义,自动生成toString()
 * override:复写父类或接口中的成员
 * @author wenjing.liu
 */
open class ManifestExtensionByKotlin {
    /**
     * object:相当于类中的所有方法、属性都为静态方法,object类实例本身为单例,object也可以表示为一个匿名类或不存在对象
     * component object:相当于代码块内的属性、方法为静态的属性、方法
     */
    companion object {
        //const 关键字用来修饰常量，且只能修饰 val，不能修饰var, companion object 的名字可以省略，可以使用 Companion来指代
       const val TAG: String = "ManifestPlugin"
    }

    //lateinit 用于var 变量初始化，避免非空检查;
//lazy用于val 应用于单例模式,而且仅当变量被第一次调用的时候，委托方法才会执行，第二次调用只会返回结果
    lateinit var versionFile: File

}