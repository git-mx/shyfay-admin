package com.shyfay.admin.service;

import com.shyfay.admin.common.constant.StatusConstant;
import com.shyfay.admin.web.input.PositionAdd;
import com.shyfay.admin.bean.SysPosition;
import com.shyfay.admin.dao.SysPositionMapper;
import com.shyfay.admin.web.input.PositionCondition;
import com.shyfay.admin.web.iobean.PositionIoentity;
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
public class SysPositionServiceImpl implements SysPositionService {

    @Autowired
    SysPositionMapper positionMapper;

    @Override
    public void add(PositionAdd position){
        SysPosition sysPosition = new SysPosition();
        BeanUtils.copyProperties(position, sysPosition);
        sysPosition.setCreateTime(System.currentTimeMillis());
        sysPosition.setUpdateTime(0L);
        sysPosition.setDeleteStatus(StatusConstant.IS_DELETE_NO.getIndex());
        positionMapper.insert(sysPosition);
    }

    @Override
    public void modify(PositionIoentity position){
        SysPosition sysPosition = new SysPosition();
        BeanUtils.copyProperties(position, sysPosition);
        sysPosition.setUpdateTime(System.currentTimeMillis());
        positionMapper.update(sysPosition);
    }

    @Override
    public void delete(@Param("positionId") Long positionId){
        positionMapper.updateDeleteStatus(positionId);
    }

    @Override
    public PositionIoentity getById(@Param("positionId") Long positionId){
        return positionMapper.get(positionId);
    }

    @Override
    public PageInfo<PositionIoentity> listByCondition(PositionCondition condition){
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<PositionIoentity> positions = positionMapper.list(condition);
        if(CollectionUtils.isEmpty(positions)){
            return new PageInfo(Lists.newArrayList());
        }
        return new PageInfo(positions);
    }

    @Override
    public List<SelectOption> listOption(){
        return positionMapper.listOption();
    }
}
