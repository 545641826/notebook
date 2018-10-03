package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 54564 on 2018/3/1.
 */
public class SearchView extends Activity {//继承Activity类
    //变量声明
    private MyOpenHelper moh;
    private SQLiteDatabase sd;
    private ArrayList<student_info> studentlist;
    private ListView lv;
    private Cursor cursor;

    //定义可继承的方法
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysearch);//使用R.layout.activitysearch作为活动界面。控件均在本界面找。

        Button button = (Button) findViewById(R.id.searchButtons);//获取id为searchButtons的返回按钮。fuck，我当时怎么会这样起id？
        button.setOnClickListener(new View.OnClickListener() {//若单击，跳转回主界面。
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchView.this, "return successfully", Toast.LENGTH_LONG).show();
                SearchView.this.finish();
                startActivity(new Intent(SearchView.this, student.class));

            }
        });
        //创建或打开"my_data.db"数据库
        moh = new MyOpenHelper(this, "my_data.db", null, 2);
        sd = moh.getReadableDatabase();//数据库
        studentlist = new ArrayList<>();//数据列表。
        //扫描数据库,将数据库信息放入studentlist
        if (student.k) {//若student.k=true说明主程序通过长按进入本界面，按内容搜索。
            //搜索内容（age）带有关键字（student.keyword）的信息。
            cursor = sd.rawQuery("select * from my_data where age like '%" + student.keyword +
                    "%';", null);
        } else {//否则按标题（name）搜索。
            cursor = sd.rawQuery("select * from my_data where name like '%" + student.keyword +
                    "%';", null);
        }
        while (cursor.moveToNext()) {//遍历所有搜索出来的数据。
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String age = cursor.getString(cursor.getColumnIndex("age"));
            student_info st = new student_info(number, name, age);    //student_info存一个条目的数据
            studentlist.add(st);//把数据库的每一行加入数组中
        }

        //下面这些代码和student中展示列表的代码相同，不必再看。应该把他们封装成类的。。。我当时还是太年轻了。


        //获取ListView,并通过Adapter把studentlist的信息显示到ListView
        //为ListView设置一个适配器,getCount()返回数据个数;getView()为每一行设置一个条目
        lv = (ListView) findViewById(R.id.student_lvs);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return studentlist.size();
            }

            //ListView的每一个条目都是一个view对象
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                //对ListView的优化，convertView为空时，创建一个新视图；convertView不为空时，代表它是滚出
                //屏幕，放入Recycler中的视图,若需要用到其他layout，则用inflate(),同一视图，用fiindViewBy()
                if (convertView == null) {
                    view = View.inflate(getBaseContext(), R.layout.studentlayout, null);
                } else {
                    view = convertView;
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        //点击后在标题上显示点击了第几行
                        //  Toast.makeText(student.this, "你点击了第" + arg2 + "行", Toast.LENGTH_SHORT).show();

                        // mdb.delete(String.valueOf(arg2));
                        //  HashMap item = (HashMap)arg0.getItemAtPosition(arg2);
                        // String section =String.valueOf(item.get("name").toString());//get每一行的数据的名字
                        // Toast.makeText(student.this, section, Toast.LENGTH_LONG).show();
                        //View v = lv.getChildAt(position);
                        TextView t = (TextView) arg1.findViewById(R.id.stu_number);
                        student.id = t.getText().toString();
                        SQLiteDatabase db = moh.getReadableDatabase();

                        //db.execSQL("delete from student where number='" + id + "'");//删除
                        db.close();
                        //Toast.makeText(student.this, "delete" + id, Toast.LENGTH_LONG).show();
                        final SharedPreferences sp = getSharedPreferences(student.id, MODE_PRIVATE);
                        final String context = sp.getString(student.id, null);
                        if (context == null) {
                            SearchView.this.finish();
                            Intent intent = new Intent(SearchView.this, addActivity.class);
                            startActivity(intent);
                        } else {
                            LayoutInflater factory = LayoutInflater.from(SearchView.this);
                            final View view = factory.inflate(R.layout.edittext, null);//这里必须是final的
                            final EditText textn = (EditText) view.findViewById(R.id.editText);//获得输入框对象
                            AlertDialog.Builder builder = new AlertDialog.Builder(SearchView.this);
                            builder.setView(view);
                            builder.setTitle("请输入密码");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub

                                    arg0.dismiss();
                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub

                                    final SharedPreferences sp = getSharedPreferences(student.id, MODE_PRIVATE);
                                    String edit = textn.getText().toString();
                                    if (edit.equals(context)) {
                                        SearchView.this.finish();
                                        Intent intent = new Intent(SearchView.this, addActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SearchView.this, "The keyword is error!", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });
                            builder.create().show();
                        }

                    }
                });
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int arg2, long arg3) {
                        TextView t = (TextView) arg1.findViewById(R.id.stu_number);
                        student.id = t.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchView.this);

                        builder.setMessage("确认删除吗");
                        builder.setTitle("提示");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                arg0.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub


                                SQLiteDatabase db = moh.getReadableDatabase();

                                db.execSQL("delete from my_data where number='" + student.id + "'");//删除
                                db.close();
                                arg0.dismiss();
                                refresh();
                                Toast.makeText(SearchView.this, "delete successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.create().show();
                        return true;     // 这里一定要改为true，代表长按自己消费掉了，若为false，触发长按事件的同时，还会触发点击事件</span></strong>
                    }
                });
                //从studentlist中取出一行数据，position相当于数组下标,可以实现逐行取数据
                student_info st = studentlist.get(position);
                TextView number = (TextView) view.findViewById(R.id.stu_number);
                TextView name = (TextView) view.findViewById(R.id.stu_name);
                TextView age = (TextView) view.findViewById(R.id.stu_age);
                number.setText(st.getNumber());
                name.setText(st.getName());
                age.setText(st.getAge());
                return view;


            }


            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }
        });
    }

    public void refresh() {
        onCreate(null);
    }

    public void onBackPressed() {
        SearchView.this.finish();
        Intent intents = new Intent(SearchView.this, student.class);
        startActivity(intents);
    }


}
