package com.wzl.scheduledemo;

import java.io.Serializable;

//便于intent传值
public class Schedule implements Serializable{
    private int _id;
    private String course;  //课程名称
    private String teacher;  //老师名字
    private String location;  //地点
    private int startWeek;  //开始周
    private int endWeek;  //结束周
    private int currentWeek;  //当前周
    private int startSection;  //开始节
    private int endSection;  //结束节
    private int whichWeek;  //星期几

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getWhichWeek() {
        return whichWeek;
    }

    public void setWhichWeek(int whichWeek) {
        this.whichWeek = whichWeek;
    }
}
