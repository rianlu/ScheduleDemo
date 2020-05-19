package com.wzl.scheduledemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int TODAY_ID = 1;  //今日布局
    private static int WEEK_ID = 2;  //一周布局
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private TodayScheduleFragment todayScheduleFragment;
    private WeekScheduleFragment weekScheduleFragment;
    private SharedPreferences sp;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Tag", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  设置 ToolBar
        initToolBar();

        // 初始化工具类
        new ScheduleUtils(this);

        //  初始化布局
        initFragment();

        //  初始化切换布按钮
        initFloatButton();

        if (sp.getBoolean("FIRST_LAUNCH", true)){
            // 显示提示，并申请权限
            showTip();
        }
    }

    //  初始化ToolBar
    public void  initToolBar(){
        final TextView tvWeek = findViewById(R.id.tv_week);
        //  获取保存的周数并显示
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // 获得存储的总周数
        int totalWeek = sp.getInt("TOTAL_WEEK_NUMBER", 1);
        // 当前设置的周数
        int week = sp.getInt("WEEK_NUMBER", 1);
        // 判断是否是第一次存储总周数
        if (totalWeek != 1){
            int addWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - totalWeek;
            if (addWeek > 0){
                ScheduleUtils.WEEK_NUMBER = week + addWeek;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("WEEK_NUMBER", ScheduleUtils.WEEK_NUMBER);
                editor.putInt("TOTAL_WEEK_NUMBER", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                editor.apply();
            }
        }
        ScheduleUtils.WEEK_NUMBER = sp.getInt("WEEK_NUMBER", 1);
        tvWeek.setText("第 " + ScheduleUtils.WEEK_NUMBER + " 周");
        Toolbar toolbar = findViewById(R.id.tb);
        //去除标题
        toolbar.setTitle("");
        //长按修改周数
        tvWeek.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_current_week, null, false);
                final EditText et = dialogView.findViewById(R.id.et_current_week);
                dialog.setTitle("设置当前周")
                        .setView(dialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ScheduleUtils.WEEK_NUMBER = Integer.parseInt(et.getText().toString());
                                tvWeek.setText("第" + ScheduleUtils.WEEK_NUMBER + "周");
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("WEEK_NUMBER", ScheduleUtils.WEEK_NUMBER);
                                editor.putInt("TOTAL_WEEK_NUMBER", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                                editor.apply();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });
        setSupportActionBar(toolbar);
    }

    //  初始化布局
    public void initFragment(){
        //  获取 Fragment 管理者
        fragmentManager = getSupportFragmentManager();
        //  开启事务
        transaction = fragmentManager.beginTransaction();

        todayScheduleFragment = new TodayScheduleFragment();
        weekScheduleFragment = new WeekScheduleFragment();
        //  获取保存的 ID
        if (sp.getInt("LAYOUT_ID", 1) == TODAY_ID){
            transaction.replace(R.id.change_layout, todayScheduleFragment);
            transaction.add(R.id.change_layout, weekScheduleFragment);

        }else if (sp.getInt("LAYOUT_ID", 1) == WEEK_ID){
            transaction.replace(R.id.change_layout, weekScheduleFragment);
            transaction.add(R.id.change_layout, todayScheduleFragment);
        }
        // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        // 在finish()之后会报异常
        //transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    //  改变布局
    public void changeFragment(){
        //开启事务
        transaction = fragmentManager.beginTransaction();
        SharedPreferences.Editor editor = sp.edit();
        //  获取保存的 ID
        int switchId = sp.getInt("LAYOUT_ID", 1);
        if (switchId == WEEK_ID){
            //transaction.replace(R.id.change_layout, todayScheduleFragment);
            //避免每次切换重复加载 不适用replace
            transaction.hide(weekScheduleFragment).show(todayScheduleFragment);
            editor.putInt("LAYOUT_ID", TODAY_ID);
        }else {
            transaction.hide(todayScheduleFragment).show(weekScheduleFragment);
            editor.putInt("LAYOUT_ID", WEEK_ID);
        }
        editor.apply();
        //一定要记得commit!!!
        //一定要记得commit!!!
        //一定要记得commit!!!
        transaction.commit();
    }

    //  初始化悬浮按钮
    public void initFloatButton(){
        FloatingActionButton fab = findViewById(R.id.float_change_btn);
        fab.setOnClickListener(             new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_course:
                Intent intent = new Intent(this, AddScheduleActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.import_data:
                importData();
                break;
            case R.id.export_data:
                exportData();
                break;
            case R.id.use_info:
                new AlertDialog.Builder(this)
                        .setTitle("关于")
                        .setMessage("Design By Lu")
                        .setPositiveButton("确定", null)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // 添加
        if (requestCode == REQUEST_CODE && resultCode == 1){
            initFragment();
        }
        // 详情页面返回
        if (requestCode == REQUEST_CODE && resultCode == 2){
            initFragment();
        }
    }

    //  导入数据
    public void importData(){
        //在导入数据之前清空数据，防止重复添加
        ScheduleUtils.deleteData();
        //开始导入数据
        XmlPullParser parser = Xml.newPullParser();
        Schedule schedule = null;
        int success = 0;
        int fail = 0;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "backup.xml");
            InputStream inputStream = new FileInputStream(file);
            parser.setInput(inputStream, "utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if ("course".equals(parser.getName())){
                            schedule = new Schedule();
                            schedule.setCourse(parser.nextText());
                        } else if ("teacher".equals(parser.getName())){
                            schedule.setTeacher(parser.nextText());
                        } else if ("location".equals(parser.getName())){
                            schedule.setLocation(parser.nextText());
                        } else if ("startweek".equals(parser.getName())){
                            schedule.setStartWeek(Integer.parseInt(parser.nextText()));
                        } else if ("endweek".equals(parser.getName())){
                            schedule.setEndWeek(Integer.parseInt(parser.nextText()));
                        } else if ("currentweek".equals(parser.getName())){
                            schedule.setCurrentWeek(Integer.parseInt(parser.nextText()));
                        } else if ("startsection".equals(parser.getName())){
                            schedule.setStartSection(Integer.parseInt(parser.nextText()));
                        } else if ("endsection".equals(parser.getName())){
                            schedule.setEndSection(Integer.parseInt(parser.nextText()));
                        } else if ("whichweek".equals(parser.getName())){
                            schedule.setWhichWeek(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //  代表一个读取完成
                        if ("schedule".equals(parser.getName())){
                            if (schedule != null){
                                ScheduleUtils.addSchedule(schedule);
                                success ++;
                            } else{
                                fail ++;
                            }
                        }
                        break;
                }
                //  确保继续读取下一个
                eventType = parser.next();
            }
            inputStream.close();
            Toast.makeText(this, "导入完成，成功 " + success + " 个," + "失败 " + fail + " 个，请重启应用", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  导出数据
    public void exportData() {
        XmlSerializer serializer = Xml.newSerializer();
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "backup.xml");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            serializer.setOutput(fos, "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "schedules");

            List<Schedule> list = ScheduleUtils.getScheduleList();
            for (Schedule schedule : list){
                serializer.startTag(null, "schedule");

                serializer.startTag(null, "course");
                serializer.text(schedule.getCourse());
                serializer.endTag(null, "course");

                serializer.startTag(null, "teacher");
                serializer.text(schedule.getTeacher());
                serializer.endTag(null, "teacher");

                serializer.startTag(null, "location");
                serializer.text(schedule.getLocation());
                serializer.endTag(null, "location");

                serializer.startTag(null, "startweek");
                serializer.text(String.valueOf(schedule.getStartWeek()));
                serializer.endTag(null, "startweek");

                serializer.startTag(null, "endweek");
                serializer.text(String.valueOf(schedule.getEndWeek()));
                serializer.endTag(null, "endweek");

                serializer.startTag(null, "currentweek");
                serializer.text(String.valueOf(schedule.getCurrentWeek()));
                serializer.endTag(null, "currentweek");

                serializer.startTag(null, "startsection");
                serializer.text(String.valueOf(schedule.getStartSection()));
                serializer.endTag(null, "startsection");

                serializer.startTag(null, "endsection");
                serializer.text(String.valueOf(schedule.getEndSection()));
                serializer.endTag(null, "endsection");

                serializer.startTag(null, "whichweek");
                serializer.text(String.valueOf(schedule.getWhichWeek()));
                serializer.endTag(null, "whichweek");

                serializer.endTag(null, "schedule");
            }
            serializer.endTag(null, "schedules");
            serializer.endDocument();
            fos.close();
            Toast.makeText(this, "导出成功，文件在根目录下，文件名为backup.xml", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 申请权限
    public void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    //提示
    public void showTip(){
        new AlertDialog.Builder(this)
                .setTitle("关于")
                .setMessage(R.string.tip_dialog)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("FIRST_LAUNCH", false);
                        editor.apply();
                        // 申请权限
                        requestPermissions();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
    }
}
