package com.shyfay.admin.service;

import com.shyfay.admin.web.input.PositionAdd;
import com.shyfay.admin.web.input.PositionCondition;
import com.shyfay.admin.web.iobean.PositionIoentity;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import com.shyfay.admin.web.output.SelectOption;

import java.util.List;


public interface SysPositionService {
    void add(PositionAdd position);
    void modify(PositionIoentity position);
    void delete(@Param("positionId") Long positionId);
    PositionIoentity getById(@Param("positionId") Long positionId);
    PageInfo<PositionIoentity> listByCondition(PositionCondition condition);
    List<SelectOption> listOption();
}
