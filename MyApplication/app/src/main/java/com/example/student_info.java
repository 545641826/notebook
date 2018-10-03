package com.example;
//本页为定义student_info类的代码
/*
student_info类有三个private数据：student_info，name，age；及初始化构造函数和各种存取数据的方法。
*/
/**
 * Created by 54564 on 2018/2/28.
 */
public class student_info {
    private String number;
    private String name;
    private String age;
    //构造方法
    public student_info(String number, String name, String age) {
        this.number = number;
        this.name = name;
        this.age = age;
    }
    //存取数据方法
    public String getNumber(){
        return number;
    }
    public void  setNumber(String num){
        this.number=num;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age=age;
    }
}
//<!--下转java\com\example\student.java代码页-->