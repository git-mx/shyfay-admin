package com.shyfay.admin.service;

import com.shyfay.admin.web.input.DepartmentAdd;
import com.shyfay.admin.web.input.DepartmentCondition;
import com.shyfay.admin.web.iobean.DepartmentIoentity;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import com.shyfay.admin.web.output.SelectOption;

import java.util.List;

public interface SysDepartmentService {
    void add(DepartmentAdd department);
    void modify(DepartmentIoentity department);
    void delete(@Param("departmentId") Long departmentId);
    DepartmentIoentity getById(@Param("departmentId") Long departmentId);
    PageInfo<DepartmentIoentity> listByCondition(DepartmentCondition condition);
    List<SelectOption> listOption();
}
