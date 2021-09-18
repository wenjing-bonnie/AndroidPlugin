package com.wj.manifest

/**
 * Created by wenjing.liu on 2021/9/17 in J1.
 * 读取配置文件(含有对每个版本的信息描述)中的versionCode与versionName
 * 然后再用来学习下kotlin语法
 * @author wenjing.liu
 */
class KotlinSetLastVersionInfoTask {
    /**
     * (1)var声明变量/常量 var/val name:Type = xxx
     * (2)类、函数、字段的声明默认为public类型.
     * (3)当行代码可以不加; 一行有多个语句需要用;
     * (4)变量的声明和初始化分为:top level(顶层)/class level(类成员)/function level(函数局部变量)
     * */
    var versionCode: String = ""
    var versionName: String = ""
    val TAG: String = "SetLastVersionInfoTask"
}