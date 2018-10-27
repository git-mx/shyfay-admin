package com.shyfay.admin.service;

import com.shyfay.admin.bean.Student;

import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
public interface StudentService {
    int add(Student student);
    int delete(Integer studentId);
    Student get(Integer studentId);
    List<Student> getRand();
    int update(Student student);
    List<Student> list();
    List<Student> listTwins(String nameOne, String nameTwo);
    Student getExchange(int groupNo, int studentId);
}
