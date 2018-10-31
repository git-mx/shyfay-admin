package com.shyfay.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shyfay.admin.bean.Student;
import com.shyfay.admin.common.util.OrderNumberUtil;
import com.shyfay.admin.dao.StudentMapper;
import com.shyfay.admin.web.input.StudentAdd;
import com.shyfay.admin.web.input.StudentCondition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        return studentMapper.add(student);
    }
    @Override
    public void addBatch(List<Student> students){
        studentMapper.insertBatch(students);
    }
    @Override
    public int delete(Long studentId){
        return studentMapper.delete(studentId);
    }
    @Override
    public Student get(Long studentId){
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
    public PageInfo<Student> listByCondition(StudentCondition condition){
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<Student> students = studentMapper.listByCondition(condition);
        if(CollectionUtils.isEmpty(students)){
            return new PageInfo(Lists.newArrayList());
        }
        return new PageInfo(students);
    }
    @Override
    public Student getExchange(Long groupNo, Long studentId){
        return studentMapper.getExchange(groupNo, studentId);
    }
}
