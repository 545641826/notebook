

// @txu:别看这页代码，我会在调用的地方说明。


package com.example;

/**
 * Created by 54564 on 2018/2/28.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//继承SQLiteOpenHelper类
public class MyOpenHelper extends SQLiteOpenHelper {

    //构造方法，new时会调用，cursor：封装数据库查询时返回的数据，初始为-1.依次向下读取下一行数据，null使用默认游标
    //楼上注释我页看不懂
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);//调用父类构造方法
    }
    @Override
    public void onCreate(SQLiteDatabase db) {//传入一个数据库对象
        //用sql语句在数据库建立一个表
        String sql="create table my_data(number char(4) primary key,name char(4),age char(1))";//这是语句
        db.execSQL(sql);//执行语句，建表
    }

    //更新数据时，此方法会调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //感觉我当初压根没理解这页代码
        System.out.println("数据库被更新了");//只输出个语句，什么都没干
    }
}