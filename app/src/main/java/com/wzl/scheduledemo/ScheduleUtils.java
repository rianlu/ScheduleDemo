package com.wzl.scheduledemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 不需要申明public ，即默认是package-private，仅对自己包内的类可见
public class ScheduleUtils {

    private static DBHelper dbHelper;
    private static List<Schedule> scheduleList = null;
    static int WEEK_NUMBER = 0;  //设置周数

    public ScheduleUtils(Context context){
        scheduleList = new ArrayList<>();
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    //添加课表
    static long addSchedule(Schedule schedule){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course", schedule.getCourse());
        values.put("teacher", schedule.getTeacher());
        values.put("location", schedule.getLocation());
        values.put("startweek", schedule.getStartWeek());
        values.put("endweek", schedule.getEndWeek());
        values.put("currentweek", schedule.getCurrentWeek());
        values.put("startsection", schedule.getStartSection());
        values.put("endsection", schedule.getEndSection());
        values.put("whichweek", schedule.getWhichWeek());
        long insert = db.insert("schedule", null, values);
        db.close();
        return insert;
    }

    //修改课表
    static int updateSchedule(Schedule schedule){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course", schedule.getCourse());
        values.put("teacher", schedule.getTeacher());
        values.put("location", schedule.getLocation());
        values.put("startweek", schedule.getStartWeek());
        values.put("endweek", schedule.getEndWeek());
        values.put("currentweek", schedule.getCurrentWeek());
        values.put("startsection", schedule.getStartSection());
        values.put("endsection", schedule.getEndSection());
        values.put("whichweek", schedule.getWhichWeek());
        int update = db.update("schedule", values, "_id=?", new String[]{String.valueOf(schedule.get_id())});
        return update;
    }

    //删除课表
    static int delSchedule(Schedule schedule){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delete = db.delete("schedule", "_id=?", new String[]{String.valueOf(schedule.get_id())});
        db.close();
        return delete;
    }

    //获取列表
    static List<Schedule> getScheduleList(){
        scheduleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("schedule", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Schedule schedule = new Schedule();
                schedule.set_id(cursor.getInt(0));
                schedule.setCourse(cursor.getString(1));
                schedule.setTeacher(cursor.getString(2));
                schedule.setLocation(cursor.getString(3));
                schedule.setStartWeek(cursor.getInt(4));
                schedule.setEndWeek(cursor.getInt(5));
                schedule.setCurrentWeek(cursor.getInt(6));
                schedule.setStartSection(cursor.getInt(7));
                schedule.setEndSection(cursor.getInt(8));
                schedule.setWhichWeek(cursor.getInt(9));
                scheduleList.add(schedule);
            }
            cursor.close();
        }
        db.close();
        return scheduleList;
    }

    //根据星期获取列表
    static List<Schedule> getScheduleListByWeek(int whichWeek){
        scheduleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("schedule", null, "whichweek=?", new String[]{String.valueOf(whichWeek)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Schedule schedule = new Schedule();
                schedule.set_id(cursor.getInt(0));
                schedule.setCourse(cursor.getString(1));
                schedule.setTeacher(cursor.getString(2));
                schedule.setLocation(cursor.getString(3));
                schedule.setStartWeek(cursor.getInt(4));
                schedule.setEndWeek(cursor.getInt(5));
                schedule.setCurrentWeek(cursor.getInt(6));
                schedule.setStartSection(cursor.getInt(7));
                schedule.setEndSection(cursor.getInt(8));
                schedule.setWhichWeek(cursor.getInt(9));
                scheduleList.add(schedule);
            }
            cursor.close();
        }
        db.close();
        return scheduleList;
    }

    //获取全部的课程名
    static List<String> getAllCourse(){
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("schedule", new String[]{"course"}, null, null, "course", null, null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    //根据课程名查询单个课表
    static Schedule getScheduleByCourse(String course){
        // 该死
        // 居然写成Schedule schedule = null;
        // 没有实例化，调试了半天，就是空值
        Schedule schedule = new Schedule();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select distinct * from schedule where course=?", new String[]{course});
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                schedule.setCourse(cursor.getString(1));
                schedule.setTeacher(cursor.getString(2));
                schedule.setLocation(cursor.getString(3));
                schedule.setStartWeek(cursor.getInt(4));
                schedule.setEndWeek(cursor.getInt(5));
                schedule.setCurrentWeek(cursor.getInt(6));
                schedule.setStartSection(cursor.getInt(7));
                schedule.setEndSection(cursor.getInt(8));
                schedule.setWhichWeek(cursor.getInt(9));
            }
            cursor.close();
        }
        db.close();
        return schedule;
    }

    //清空表
    static void deleteData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from schedule");
        db.close();
    }

    static String numberToWeek(int number){
        List<String> weekList = new ArrayList<>();
        weekList.add("星期一");
        weekList.add("星期二");
        weekList.add("星期三");
        weekList.add("星期四");
        weekList.add("星期五");
        weekList.add("星期六");
        weekList.add("星期日");
        return weekList.get(number - 1);
    }
}
