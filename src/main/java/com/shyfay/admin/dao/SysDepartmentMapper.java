package com.shyfay.admin.dao;

import com.shyfay.admin.bean.SysDepartment;
import com.shyfay.admin.web.input.DepartmentCondition;
import com.shyfay.admin.web.iobean.DepartmentIoentity;
import org.apache.ibatis.annotations.Param;
import com.shyfay.admin.web.output.SelectOption;

import java.util.List;

public interface SysDepartmentMapper {
    void insert(SysDepartment department);
    void update(SysDepartment department);
    void updateDeleteStatus(@Param("departmentId") Long departmentId);
    DepartmentIoentity get(@Param("departmentId") Long departmentId);
    List<DepartmentIoentity> list(DepartmentCondition condition);
    List<SelectOption> listOption();
}
