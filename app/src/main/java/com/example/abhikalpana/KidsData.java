package com.example.abhikalpana;

import android.content.Context;

public class KidsData {

    String name, class_no, dpurl, school, last_attended, last_studied, last_checkup;
    int age, nest;
    Context context;

    public KidsData(String name, String class_no, String dpurl, String school, String last_attended, String last_studied, String last_checkup, int age, int nest) {
        this.name = name;
        this.class_no = class_no;
        this.dpurl = dpurl;
        this.school = school;
        this.last_attended = last_attended;
        this.last_studied = last_studied;
        this.last_checkup = last_checkup;
        this.age = age;
        this.nest = nest;
    }

    public KidsData(String name, String class_no, String dpurl, int age) {
        this.name = name;
        this.class_no = class_no;
        this.dpurl = dpurl;
        this.age = age;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }

    public String getDpurl() {
        return dpurl;
    }

    public void setDpurl(String dpurl) {
        this.dpurl = dpurl;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLast_attended() {
        return last_attended;
    }

    public void setLast_attended(String last_attended) {
        this.last_attended = last_attended;
    }

    public String getLast_studied() {
        return last_studied;
    }

    public void setLast_studied(String last_studied) {
        this.last_studied = last_studied;
    }

    public String getLast_checkup() {
        return last_checkup;
    }

    public void setLast_checkup(String last_checkup) {
        this.last_checkup = last_checkup;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNest() {
        return nest;
    }

    public void setNest(int nest) {
        this.nest = nest;
    }


}
