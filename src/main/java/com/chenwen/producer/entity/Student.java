package com.chenwen.producer.entity;

import java.util.Date;

public class Student {

    private int id;
    private String stuName;
    private int age;
    private Date birthday;
    private float grade;

    public Student() {
    }

    public Student(int id, String stuName, int age, Date birthday, float grade) {
        this.id = id;
        this.stuName = stuName;
        this.age = age;
        this.birthday = birthday;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", stuName='" + stuName + '\'' +
                ", age='" + age + '\'' +
                ", birthday=" + birthday +
                ", grade=" + grade +
                '}';
    }
}
