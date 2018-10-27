package com.shyfay.admin.dao;

import com.shyfay.admin.web.output.SelectOption;
import com.shyfay.admin.bean.SysPosition;
import com.shyfay.admin.web.input.PositionCondition;
import com.shyfay.admin.web.iobean.PositionIoentity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPositionMapper {
    void insert(SysPosition sysPosition);
    void update(SysPosition sysPosition);
    void updateDeleteStatus(@Param("positionId") Long positionId);
    PositionIoentity get(@Param("positionId") Long positionId);
    List<PositionIoentity> list(PositionCondition condition);
    List<SelectOption> listOption();
}
