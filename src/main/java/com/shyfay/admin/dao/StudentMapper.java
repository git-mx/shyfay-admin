package com.shyfay.admin.dao;

import com.shyfay.admin.bean.Student;
import com.shyfay.admin.web.input.StudentCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
public interface StudentMapper {
    int add(Student student);
    void insertBatch(@Param("students")List<Student> students);
    int delete(@Param("studentId")Long studentId);
    Student get(@Param("studentId")Long studentId);
    List<Student> getRand();
    int update(Student student);
    List<Student> list();
    List<Student> listTwins(@Param("nameOne") String nameOne, @Param("nameTwo") String nameTwo);
    List<Student> listByCondition(StudentCondition condition);
    Student getExchange(@Param("groupNo")Long groupNo, @Param("studentId")Long studentId);
}
