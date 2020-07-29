package com.example.abhikalpana;

public class FirebaseModel {

    String name, branch, school, class_no, lastattended, lastlog, lastcheckup;
    int age,nest;

    public int getNest() {
        return nest;
    }

    public void setNest(int nest) {
        this.nest = nest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLastattended() {
        return lastattended;
    }

    public void setLastattended(String lastattended) {
        this.lastattended = lastattended;
    }

    public String getLastlog() {
        return lastlog;
    }

    public void setLastlog(String lastlog) {
        this.lastlog = lastlog;
    }

    public String getLastcheckup() {
        return lastcheckup;
    }

    public void setLastcheckup(String lastcheckup) {
        this.lastcheckup = lastcheckup;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) { this.age = age;  }

    public FirebaseModel() {


    }
}
