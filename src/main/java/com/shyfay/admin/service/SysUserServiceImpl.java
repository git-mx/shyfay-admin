package com.shyfay.admin.service;

import com.shyfay.admin.common.constant.StatusConstant;
import com.shyfay.admin.common.util.Encrypt;
import com.shyfay.admin.web.input.UserAdd;
import com.shyfay.admin.web.input.UserCondition;
import com.shyfay.admin.web.input.UserUpdate;
import com.shyfay.admin.bean.SysUser;
import com.shyfay.admin.dao.SysDepartmentMapper;
import com.shyfay.admin.dao.SysPositionMapper;
import com.shyfay.admin.dao.SysUserMapper;
import com.shyfay.admin.web.input.UserLogin;
import com.shyfay.admin.web.iobean.DepartmentIoentity;
import com.shyfay.admin.web.iobean.PositionIoentity;
import com.shyfay.admin.web.output.UserDetail;
import com.shyfay.admin.web.output.UserList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    SysUserMapper userMapper;
    @Autowired
    SysDepartmentMapper departmentMapper;
    @Autowired
    SysPositionMapper positionMapper;


    @Override
    public Boolean add(UserAdd user) throws Exception{
        if(userMapper.findExist(user.getLoginName(), 0L)){
            return false;
        }
        try{
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(user, sysUser);
            sysUser.setLoginPassword(Encrypt.md5(sysUser.getLoginPassword()));
            DepartmentIoentity department = departmentMapper.get(user.getDepartmentId());
            sysUser.setDepartmentName(department.getDepartmentName());
            PositionIoentity position = positionMapper.get(user.getPositionId());
            sysUser.setPositionName(position.getPositionName());
            sysUser.setCreateTime(System.currentTimeMillis());
            sysUser.setUpdateTime(System.currentTimeMillis());
            userMapper.insert(sysUser);
        }catch (Exception e){
            throw new Exception(e);
        }
        return true;
    }

    @Override
    public Boolean modify(UserUpdate user) throws Exception {
        if (userMapper.findExist(user.getLoginName(), user.getUserId())) {
            return false;
        }
        try {
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(user, sysUser);
            if(StringUtils.isNotBlank(sysUser.getLoginPassword())){
                sysUser.setLoginPassword(Encrypt.md5(sysUser.getLoginPassword()));
            }
            DepartmentIoentity department = departmentMapper.get(user.getDepartmentId());
            sysUser.setDepartmentName(department.getDepartmentName());
            PositionIoentity position = positionMapper.get(user.getPositionId());
            sysUser.setPositionName(position.getPositionName());
            sysUser.setUpdateTime(System.currentTimeMillis());
            sysUser.setUpdateTime(0L);
            sysUser.setDeleteStatus(StatusConstant.IS_DELETE_NO.getIndex());
            userMapper.update(sysUser);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        return true;
    }

    @Override
    public void modifyPassword(Long userId, String loginPasword){
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginPassword(loginPasword);
        sysUser.setUpdateId(userId);
        sysUser.setUpdateTime(System.currentTimeMillis());
        userMapper.update(sysUser);
    }

    @Override
    public void delete(Long userId){
        userMapper.updateDeleteStatus(userId);
    }

    @Override
    public UserDetail getById(Long userId){
        return userMapper.get(userId);
    }

    @Override
    public UserDetail getByLogin(UserLogin userLogin) throws Exception {
        try{
            String encodePassword = Encrypt.md5(userLogin.getLoginPassword());
            return userMapper.getLogin(userLogin.getLoginName(), encodePassword);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public PageInfo<UserList> listByCondition(UserCondition condition){
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<UserList> users = userMapper.list(condition);
        if(CollectionUtils.isEmpty(users)){
            return new PageInfo(Lists.newArrayList());
        }
        return new PageInfo(users);
    }
}
