package com.shyfay.admin.dao;

import com.shyfay.admin.web.input.UserCondition;
import com.shyfay.admin.bean.SysUser;
import com.shyfay.admin.web.output.UserDetail;
import com.shyfay.admin.web.output.UserList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    void insert(SysUser sysUser);
    void update(SysUser sysUser);
    void updateDeleteStatus(@Param("userId") Long userId);
    UserDetail get(@Param("userId") Long userId);
    UserDetail getLogin(@Param("loginName") String loginName, @Param("loginPassword") String loginPassword);
    List<UserList> list(UserCondition condition);
    Boolean findExist(@Param("loginName") String loginName, @Param("userId") Long userId);
}
