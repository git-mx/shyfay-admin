package com.shyfay.admin.service;

import com.shyfay.admin.common.constant.StatusConstant;
import com.shyfay.admin.dao.SysDepartmentMapper;
import com.shyfay.admin.bean.SysDepartment;
import com.shyfay.admin.web.input.DepartmentAdd;
import com.shyfay.admin.web.input.DepartmentCondition;
import com.shyfay.admin.web.iobean.DepartmentIoentity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.ibatis.annotations.Param;
import com.shyfay.admin.web.output.SelectOption;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {
    @Autowired
    SysDepartmentMapper departmentMapper;

    @Override
    public void add(DepartmentAdd department){
        SysDepartment sysDepartment = new SysDepartment();
        BeanUtils.copyProperties(department, sysDepartment);
        sysDepartment.setCreateTime(System.currentTimeMillis());
        sysDepartment.setUpdateTime(0L);
        sysDepartment.setDeleteStatus(StatusConstant.IS_DELETE_NO.getIndex());
        departmentMapper.insert(sysDepartment);
    }

    @Override
    public void modify(DepartmentIoentity department){
        SysDepartment sysDepartment = new SysDepartment();
        BeanUtils.copyProperties(department, sysDepartment);
        sysDepartment.setUpdateTime(System.currentTimeMillis());
        departmentMapper.update(sysDepartment);
    }

    @Override
    public void delete(@Param("departmentId") Long departmentId){
        departmentMapper.updateDeleteStatus(departmentId);
    }

    @Override
    public DepartmentIoentity getById(@Param("departmentId") Long departmentId){
        return departmentMapper.get(departmentId);
    }

    @Override
    public PageInfo<DepartmentIoentity> listByCondition(DepartmentCondition condition){
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<DepartmentIoentity> departments = departmentMapper.list(condition);
        if(CollectionUtils.isEmpty(departments)){
            return new PageInfo(Lists.newArrayList());
        }
        return new PageInfo(departments);
    }

    @Override
    public List<SelectOption> listOption(){
        return departmentMapper.listOption();
    }
}
