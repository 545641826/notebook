package com.example;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testInsert() {
        MyOpenHelper moh = new MyOpenHelper(getContext(), "student.db", null, 2);
        SQLiteDatabase db = moh.getReadableDatabase();
        db.execSQL("insert into student values(?,?,?)", new Object[]{"001", "Lily", "19"});
        db.close();
    }

    public void testDelete() {
        MyOpenHelper moh = new MyOpenHelper(getContext(), "student.db", null, 2);
        SQLiteDatabase db = moh.getReadableDatabase();
        db.execSQL("delete from student where number=?", new Object[]{1});
        db.close();
    }

    public void testUpdate() {
        MyOpenHelper moh = new MyOpenHelper(getContext(), "student.db", null, 2);
        SQLiteDatabase db = moh.getReadableDatabase();
        db.execSQL("update student set number=? where name=?", new Object[]{"1314", "Soada"});
        db.close();
    }

    public void testSelect() {
        MyOpenHelper moh = new MyOpenHelper(getContext(), "student.db", null, 2);
        SQLiteDatabase db = moh.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from student", null);
        while (cursor.moveToNext()) {
            //获取下一行数据
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String age = cursor.getString(cursor.getColumnIndex("age"));
            System.out.println(number + ";" + name + ";" + age);
            db.close();
        }
    }
}