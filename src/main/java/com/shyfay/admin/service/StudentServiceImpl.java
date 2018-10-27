package com.shyfay.admin.service;

import com.shyfay.admin.bean.Student;
import com.shyfay.admin.dao.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mx on 2018/8/18 0018.
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentMapper studentMapper;

    @Override
    public int add(Student student){
        student.setGroupNo(0);
        return studentMapper.add(student);
    }
    @Override
    public int delete(Integer studentId){
        return studentMapper.delete(studentId);
    }
    @Override
    public Student get(Integer studentId){
        return studentMapper.get(studentId);
    }
    @Override
    public List<Student> getRand(){
        return studentMapper.getRand();
    }
    @Override
    public int update(Student student){
        return studentMapper.update(student);
    }
    @Override
    public List<Student> list(){
        return studentMapper.list();
    }
    @Override
    public List<Student> listTwins(String nameOne, String nameTwo){
        return studentMapper.listTwins(nameOne, nameTwo);
    }
    @Override
    public Student getExchange(int groupNo, int studentId){
        return studentMapper.getExchange(groupNo, studentId);
    }
}
