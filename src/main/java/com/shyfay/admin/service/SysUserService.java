package com.shyfay.admin.service;

import com.shyfay.admin.web.input.UserAdd;
import com.shyfay.admin.web.input.UserCondition;
import com.shyfay.admin.web.input.UserUpdate;
import com.shyfay.admin.web.input.UserLogin;
import com.shyfay.admin.web.output.UserDetail;
import com.shyfay.admin.web.output.UserList;
import com.github.pagehelper.PageInfo;


public interface SysUserService {
    Boolean add(UserAdd user) throws Exception;
    Boolean modify(UserUpdate user) throws Exception;
    void modifyPassword(Long userId, String loginPasword);
    void delete(Long userId);
    UserDetail getById(Long userId);
    UserDetail getByLogin(UserLogin userLogin) throws Exception;
    PageInfo<UserList> listByCondition(UserCondition condition);
}
