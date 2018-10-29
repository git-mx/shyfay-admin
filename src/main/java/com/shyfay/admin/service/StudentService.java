package com.shyfay.admin.service;

import com.github.pagehelper.PageInfo;
import com.shyfay.admin.bean.Student;
import com.shyfay.admin.web.input.StudentAdd;
import com.shyfay.admin.web.input.StudentCondition;

import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
public interface StudentService {
    int add(Student student);
    int delete(Long studentId);
    Student get(Long studentId);
    List<Student> getRand();
    int update(Student student);
    List<Student> list();
    PageInfo<Student> listByCondition(StudentCondition condition);
    List<Student> listTwins(String nameOne, String nameTwo);
    Student getExchange(Long groupNo, Long studentId);
}
