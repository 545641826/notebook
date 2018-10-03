package com.example;
//声明包名

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
import java.util.LinkedList;
import java.util.List;
//导入各种包后面会用

/**
 * Created by 54564 on 2018/2/28.
 */
//当年自动生成的注释，往事如烟~~
public class student extends Activity {
    //带private的只能在本类中访问，也就是说其他类不能通过 类对象.属性 的方法获得数据
    //声明的变量可以作为实例化对象的名字，也可以通过“变量=对象”直接作为已经存在对象的名字。
    private MyOpenHelper moh;//声明MyOpenHelper对象，名为moh
    private SQLiteDatabase sd;//声明数据库对象，名为my_database
    private ArrayList<student_info> studentlist;//声明一个数组，名为notelist，<student_info> 表示里面可以放student_info类型的信息
    private List<Activity> activityList = new LinkedList();//声明一个数组，名为activityList，并将实例化的LinkedList链接列表赋值给它。ps：估计为了实现跳转
    private ListView lv;//声明列表视图对象
    static String id;//静态变量，可以在外部访问，用来记录所存信息条数
    static int n = 1;//
    static String keyword;//
    static boolean k;//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_open_helper);//设置活动界面所用xml文件，R代表res文件夹。
        //实例化一个 数据库管理助手（自己起的名字），实例化时会创建或打开一个名为my_data的数据库
        moh = new MyOpenHelper(this, "my_data.db", null,2);
        sd = moh.getReadableDatabase();//moh.getReadableDatabase()方法得到打开的数据库，并赋给my_database
        studentlist = new ArrayList<>();//
        //取出my_data数据库内的所有数据，赋给cursor
        Cursor cursor = sd.rawQuery("select * from my_data", null);
        if (cursor.getCount() == 0) {//如果cursor为空，打开数据库后插入数据
            SQLiteDatabase db = moh.getReadableDatabase();
//            cursor = sd.rawQuery("select * from my_data where number ='"+String.valueOf(n)+"';", null);
            db.execSQL("insert into my_data values(?,?,?)", new Object[]{"000", "使用指南", "单击条目编辑；长按条目删除；单击搜索按钮按标题搜索，长按按内容。"});
            db.close();
            refresh();
        }
        while (cursor.moveToNext()) {//游标后移
            //获得游标当前位置的信息：
            String number = cursor.getString(cursor.getColumnIndex("number"));//获得number
            String name = cursor.getString(cursor.getColumnIndex("name"));//获得name
            String age = cursor.getString(cursor.getColumnIndex("age"));//获得age
            student_info st = new student_info(number, name, age);    //将信息存入student_info对象
            studentlist.add(st);//将st对象存入studentlist数组
        }
        //获取ListView,并通过Adapter把studentlist的信息显示到ListView：
        //为ListView设置一个适配器,getCount()返回数据个数;getView()为每一行设置一个条目
        lv = (ListView) findViewById(R.id.student_lv);//通过id在xml里找到列表对象，并赋值给lv（获得列表控件）
        lv.setAdapter(new BaseAdapter() {
//  adapter是view和数据的桥梁。在一个ListView或者GridView中，你不可能手动给每一个格子都新建一个view，
// 所以这时候就需要Adapter的帮忙，它会帮你自动绘制view并且填充数据。
            //较难理解的一个模块，此处只重写getview方法。通过重写一个条目数据的展示与操作规则，adapter会自动应用规则，并显示所有条目。
            @Override
            public int getCount() {//获得studentlist大小的方法
                return studentlist.size();
            }

            //ListView的每一个条目都是一个view对象
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view;
                //这段注释没看懂
                //对ListView的优化，convertView为空时，创建一个新视图；convertView不为空时，代表它是滚出
                //屏幕，放入Recycler中的视图,若需要用到其他layout，则用inflate()；同一视图，用fiindViewBy()
                if (convertView == null) {
                    view = View.inflate(getBaseContext(), R.layout.studentlayout, null);
                } else {
                    view = convertView;
                }
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击事件监听
                    //点击列表后会调用下列函数
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        //  arg2表示点击的行数
                        //下面是以前的注释
                        //  Toast.makeText(student.this, "你点击了第" + arg2 + "行", Toast.LENGTH_SHORT).show();
                        // mdb.delete(String.valueOf(arg2));
                        //  HashMap item = (HashMap)arg0.getItemAtPosition(arg2);
                        // String section =String.valueOf(item.get("name").toString());//get每一行的数据的名字
                        // Toast.makeText(student.this, section, Toast.LENGTH_LONG).show();
                        //View v = lv.getChildAt(position);
                        TextView t = (TextView) arg1.findViewById(R.id.stu_number);
//                        arg1表示点击的控件。
                        //从点击的控件，寻找id为stu_number的文本框控件（id为stu_number的文本框是隐藏的）
                        id = t.getText().toString();//将文本框内容放入id。
// 对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。
                        final SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);//实例化一个名为“data"的轻量级存储对象。
                        final String content = sp.getString(id, null);//获得存储对象键=id的条目，存储的值。其实就是密码
                        if (content == null) {//如果不存在密码
                            student.this.finish();//结束这个活动，关闭这个界面
                            Intent intent = new Intent(student.this, addActivity.class);//定义跳转到addActivity.class界面的对象
                            startActivity(intent);//开始跳转。我一般连同上一句写成一句
                        } else {//如果存在密码
                            //实例化布局
                            LayoutInflater factory = LayoutInflater.from(student.this);
                            //实例化带文本框布局
                            final View view = factory.inflate(R.layout.edittext, null);
                            final EditText textn = (EditText) view.findViewById(R.id.editText);//获得输入框内容，textn
                            //实例化对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(student.this);
                            //文本框视图放入对话框
                            builder.setView(view);
                            //对话框标题
                            builder.setTitle("请输入密码");
                            //对话框按钮
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {//按钮监听函数，“取消”可以改成任意字符串
                                    // TODO Auto-generated method stub
                                    arg0.dismiss();//点取消时，不提及文本框内容，关闭对话框
                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    String edit = textn.getText().toString();//textn的内容转化为字符串赋给edit
                                    if (edit.equals(content)) {//content是存储对象里存的密码，前面定义过的。这里将它和edit比较
                                        student.this.finish();//如果相同。跳转。这里换种写法。
                                        startActivity(new Intent(student.this, addActivity.class));
                                    } else {
                                        //如果不同，弹个警告框。
                                        Toast.makeText(student.this, "The keyword is error!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            //显示对话框，前面都是配置，没有这一步的话什么都不会显示。
                            builder.create().show();
                        }

                    }
                });
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int arg2, long arg3) {//长按事件监听
                        TextView t = (TextView) arg1.findViewById(R.id.stu_number);//通过控件的id属性找到控件
                        id = t.getText().toString();//控件内容转化为字符串赋给id
                        AlertDialog.Builder builder = new AlertDialog.Builder(student.this);//实例化对话框
                        builder.setMessage("确认删除吗");//设置对话框内容
                        builder.setTitle("提示");//设置对话框标题
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {//取消按钮的监听
                                // TODO Auto-generated method stub
                                arg0.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {//确定按钮的监听
                                // TODO Auto-generated method stub
                                if (id.equals("000")) {//删除的对话框id是“000”，弹出警告，不能删除。
                                    Toast.makeText(student.this, "this content cannot be delete.", Toast.LENGTH_LONG).show();
                                } else {//如果不是“000”

                                    //打开存储对象
                                    final SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();//实例化编辑对象
                                    editor.remove(id);//用编辑对象移除键=id的条目（键值对）
                                    editor.commit();//提交修改
                                    //打开数据库
                                    SQLiteDatabase db = moh.getReadableDatabase();
                                    db.execSQL("delete from my_data where number='" + id + "'");//删除数据库里number=id的条目（元组）
                                    db.close();//关闭数据库
                                    arg0.dismiss();//关闭对话框
                                    refresh();//刷新活动界面，不然你删除的条目还会显示
                                    Toast.makeText(student.this, "delete successfully", Toast.LENGTH_SHORT).show();//弹出消息框， 持续时间：Toast.LENGTH_SHORT
                                }
                            }
                        });
                        builder.create().show();//显示对话框
                        return true;     // 这里一定要改为true，代表长按自己消费掉了，若为false，触发长按事件的同时，还会触发点击事件
                    }
                });
                Button button2 = (Button) findViewById(R.id.button2);//打开activity_my_open_helper.xml可发现，id为button2的按钮是添加按钮
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//添加按钮的单击监听
                        SQLiteDatabase db = moh.getReadableDatabase();//打开数据
                        //活动数据库里所有number的数据。
                        Cursor cursor = sd.rawQuery("select * from my_data where number ='" + String.valueOf(n) + "';", null);
                        while (cursor.getCount() > 0) {//如果存在，n++再找，直到找到一个数据库没有的number
                            n++;
                            cursor = sd.rawQuery("select * from my_data where number ='" + String.valueOf(n) + "';", null);
                        }
                        //插入数据：标题为”new“，内容为空。number为n转成字符串
                        db.execSQL("insert into my_data values(?,?,?)", new Object[]{String.valueOf(n), "new", null});
                        //可以考虑让n=1;重置n。下次添加时再找一遍。
                        Toast.makeText(student.this, "add successfully", Toast.LENGTH_SHORT).show();//弹出消息框
                        db.close();//关闭数据库
                        refresh();//刷新界面
                    }
                });
                final EditText texts = (EditText) findViewById(R.id.searchText);//搜索文本框
                Button buttons = (Button) findViewById(R.id.searchButton);//搜索按钮
                buttons.setOnClickListener(new View.OnClickListener() {//单击搜索按钮
                    @Override
                    public void onClick(View v) {
                        keyword = texts.getText().toString();//获取搜索框文本
                        k = false;//我怕是明白了，SearchView.class会通过检查k的值判断按标题搜索，还是按内容搜索。这是一大败笔。暂时不改
                        Toast.makeText(student.this, "shearch successfully", Toast.LENGTH_SHORT).show();//弹出消息
                        student.this.finish();//结束当前活动
                        startActivity(new Intent(student.this, SearchView.class));//跳转到搜索结果显示界面
                    }
                });
                buttons.setOnLongClickListener(new View.OnLongClickListener() {//长按搜索按钮
                    @Override
                    public boolean onLongClick(View v) {//同上，应该封装成函数。
                        keyword = texts.getText().toString();
                        k = true;
                        Toast.makeText(student.this, "shearch successfully", Toast.LENGTH_SHORT).show();
                        student.this.finish();
                        startActivity(new Intent(student.this, SearchView.class));
                        return true;
                    }
                });
                //从studentlist中取出一行数据，position相当于数组下标,可以实现逐行取数据
                student_info st = studentlist.get(position);
                //获得列表内一行的控件（3个）
                TextView number = (TextView) view.findViewById(R.id.stu_number);
                TextView name = (TextView) view.findViewById(R.id.stu_name);
                TextView age = (TextView) view.findViewById(R.id.stu_age);
                //让控件显示数据（1，3控件宽度为0，用户看不到，但可以访问其中数据）
                number.setText(st.getNumber());
                name.setText(st.getName());
                age.setText(st.getAge());
                return view;//返回view对象
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
    }//刷新界面方法

    public void onBackPressed() {//返回键监听
        // TODO Auto-generated method stub
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
//<!--下转java\com\example\SearchView.java与java\com\example\addActivity.java代码页-->