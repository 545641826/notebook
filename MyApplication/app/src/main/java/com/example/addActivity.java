package com.example;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by 54564 on 2018/2/28.
 */

public class addActivity extends Activity {
    private MyOpenHelper moh;
    private SQLiteDatabase my_database;
    private ArrayList<student_info> notelist;
    private String number;
    private String name;
    private String age;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);//使用R.layout.activity_add界面
        //创建或打开数据库
        moh = new MyOpenHelper(this, "my_data.db", null, 2);
        my_database = moh.getReadableDatabase();//moh.getReadableDatabase()方法得到打开的数据库，并赋给my_database
        notelist = new ArrayList<>();//实例化数组，放入变量中
        //查询数据库number=student.id的数据。有且只有一条。当客户端从主界面跳转到此时，student.id已经唯一确定。
        Cursor cursor = my_database.rawQuery("select * from my_data where number='" + student.id +
                "';", null);
        //感觉上下面这段可以有大改动。暂时不改。
        while (cursor.moveToNext()) {
            number = cursor.getString(cursor.getColumnIndex("number"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            age = cursor.getString(cursor.getColumnIndex("age"));
            student_info st = new student_info(number, name, age);    //student_info存一个条目的数据
            notelist.add(st);//把数据库的每一行加入数组中
        }
        final EditText text = (EditText) findViewById(R.id.contentText);//获得内容文本框
        final EditText text1 = (EditText) findViewById(R.id.titleText);//获得标题文本框
        Button button = (Button) findViewById(R.id.button);//获得保存按钮
        final TextView numtext = (TextView) findViewById(R.id.textViewm);//获得显示总字数的小文本框。
       /* /显示字数/*/
        text.addTextChangedListener(new TextWatcher() {//监听内容文本框的修改
            private CharSequence temp;//存储文本框内文字
            private int editStart;
            private int editEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//改变时
                // TODO Auto-generated method stub
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
//   mTextView.setText(s);//将输入的内容实时显示
            }

            @Override
            public void afterTextChanged(Editable s) {//改变后调用
                // TODO Auto-generated method stub
                editStart = text.getSelectionStart();//感觉可以删掉
                editEnd = text.getSelectionEnd();//同上
                numtext.setText("共" + temp.length() + "字");//将字数显示到numtext上。
            }

        });

        if (name != null) {//如果数据标题不为空。
            text1.setText(name);//展示到，text1文本框。
        }

        if (age != null) {//如果内容不为空。
            text.setText(age);//展示到内容文本框
        }

        button.setOnClickListener(new View.OnClickListener() {//监听保存按钮
            @Override
            public void onClick(View v) {

                String name1 = text1.getText().toString();//获得标题框内容
                String age1 = text.getText().toString();//获得内容框内容

                //my_database.execSQL("update notes set number='"+student.id+"',name=" + name1 + "',age='" + age1 + "'where number='" + student.id+ "';");
                SQLiteDatabase db = moh.getReadableDatabase();//打开数据库
                db.execSQL("update my_data set name=? where number=?", new Object[]{name1, student.id});//更新number=id词条的标题
                db.execSQL("update my_data set age=? where number=?", new Object[]{age1, student.id});//更新unmber=id词条的内容

                Toast.makeText(addActivity.this, "save successfully", Toast.LENGTH_LONG).show();//弹出消息框
                my_database.close();//关闭数据库

            }
        });
        Button button3 = (Button) findViewById(R.id.button3);//获取返回按钮
        button3.setOnClickListener(new View.OnClickListener() {//点击事件监听
            @Override
            public void onClick(View v) {//跳转到主界面
                addActivity.this.finish();//关闭当前界面
                //跳
                Intent intent = new Intent(addActivity.this, student.class);
                startActivity(intent);

            }
        });
        Button buttonlock = (Button) findViewById(R.id.buttonlock);//加密按钮
        buttonlock.setOnClickListener(new View.OnClickListener() {//单击监听
            @Override
            public void onClick(View v) {
                //配置对话框
                LayoutInflater factory = LayoutInflater.from(addActivity.this);
                final View view = factory.inflate(R.layout.edittext, null);//布局，存放输入框的容器
                final EditText textn = (EditText) view.findViewById(R.id.editText);//获得输入框对象
                AlertDialog.Builder builder = new AlertDialog.Builder(addActivity.this);//实例化文本框对象。
                builder.setView(view);
                builder.setTitle("请输入密码");
                builder.setNegativeButton("解锁", new DialogInterface.OnClickListener() {//解锁按钮事件监听

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        final SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);//打开“data”存储对象
                        SharedPreferences.Editor editor = sp.edit();//实例化编辑对象
                        editor.remove(student.id);//删除键=student.id的键值对。
                        editor.commit();//提交修改
                        Toast.makeText(addActivity.this, "delete keyword successfully", Toast.LENGTH_LONG).show();//消息框
                        arg0.dismiss();//关闭对话框
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//确定按钮监听
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        final SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);//打开“data”存储对象
                        String edit = textn.getText().toString();//获得文本框内容
                        SharedPreferences.Editor editor = sp.edit();//实例化编辑对象
                        editor.putString(student.id, edit);//上传{id:密码}键值对。
                        editor.commit();//提交修改
                        Toast.makeText(addActivity.this, "save successfully", Toast.LENGTH_LONG).show();//消息
                    }
                });
                builder.create().show();//对话框展示。
            }
        });
        Button sharebutton = (Button) findViewById(R.id.share);//分享按钮。//突然发现这是残次版，分享图标都没改~~呜呜。
        //@txu：你提过的问题！！
        sharebutton.setOnClickListener(new View.OnClickListener() {//监听分享按钮。
            @Override
            public void onClick(View v) {//单击触发的方法
                String name1 = text1.getText().toString();//获得标题框（text1）的内容。
                String age1 = text.getText().toString();//获得内容框的内容。
                Intent sendIntent = new Intent();//实例化一个意图。
                sendIntent.setAction(Intent.ACTION_SEND);//ACTION_SEND意图，
                //安卓系统本身可以很简便的实现分享功能，因为我们只需向startActivity传递一个ACTION_SEND的Intent，系统就为我们弹出一个应用程序列表。
                sendIntent.putExtra(Intent.EXTRA_TEXT, name1 + "\n" + age1);//设置发送数据意图，并将标题与内容作为参数发送过去。
                sendIntent.setType("text/plain");//设置发送格式为text/plain
                startActivity(sendIntent);//启动意图
                Toast.makeText(addActivity.this, "share successfully", Toast.LENGTH_LONG).show();//消息提示框
                my_database.close();//关闭数据库

            }
        });

    }

    public void onBackPressed() {//监听手机返回键。
        addActivity.this.finish();
        Intent intents = new Intent(addActivity.this, student.class);
        startActivity(intents);
    }


}