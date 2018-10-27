package com.shyfay.admin.dao;

import com.shyfay.admin.bean.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
public interface StudentMapper {
    int add(Student student);
    int delete(Integer studentId);
    Student get(Integer studentId);
    List<Student> getRand();
    int update(Student student);
    List<Student> list();
    List<Student> listTwins(@Param("nameOne") String nameOne, @Param("nameTwo") String nameTwo);
    Student getExchange(@Param("groupNo") int groupNo, @Param("studentId") int studentId);
}
