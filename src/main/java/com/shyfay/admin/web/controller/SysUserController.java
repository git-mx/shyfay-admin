package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.service.SysUserService;
import com.shyfay.admin.web.input.UserAdd;
import com.shyfay.admin.web.input.UserCondition;
import com.shyfay.admin.web.input.UserUpdate;
import lombok.extern.slf4j.Slf4j;
import com.shyfay.admin.common.base.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Api(value="用户管理接口", description = "用户管理接口")
@Slf4j
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @ApiOperation(value="添加用户", httpMethod="POST", notes="添加用户")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResponseResult addUser(
        @ApiParam(required = true, name = "user", value = "POST实体")@RequestBody @Valid UserAdd user,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            if(userService.add(user)){
                return new ResponseResult(ExceptionCode.SUCCESS);
            }else{
                return new ResponseResult(ExceptionCode.LOGIN_NAME_EXISTS);
            }

        }catch(Exception e){
            log.error("添加用户报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="修改用户信息", httpMethod="POST", notes = "修改用户信息")
    @RequestMapping(value="/modifyUser", method= RequestMethod.POST)
    public ResponseResult modifyUser(
            @ApiParam(required=true, name="user", value="POST实体") @RequestBody @Valid UserUpdate user,
            Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            if(userService.modify(user)){
                return new ResponseResult(ExceptionCode.SUCCESS);
            }else{
                return new ResponseResult(ExceptionCode.LOGIN_NAME_EXISTS);
            }
        }catch(Exception e){
            log.error("修改用户信息报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="删除用户", httpMethod="GET", notes="删除用户")
    @RequestMapping(value="/deleteUser", method= RequestMethod.GET)
    public ResponseResult delete(
            @ApiParam(required=true, name="userId", value="用户ID") @RequestParam Long userId){
        if(null == userId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            userService.delete(userId);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("删除用户报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取用户详情", httpMethod="GET", notes="获取用户详情")
    @RequestMapping(value="/detailUser", method= RequestMethod.GET)
    public ResponseResult detailUser(
            @ApiParam(required=true, name="userId", value="用户ID") @RequestParam Long userId){
        if(null == userId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(userService.getById(userId));
        }catch(Exception e){
            log.error("获取用户详情报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取用户列表", httpMethod="POST", notes="获取用户列表")
    @RequestMapping(value="/listUser", method= RequestMethod.POST)
    public ResponseResult listUser(
        @ApiParam(required=true, name="condition", value="POST实体") @RequestBody @Valid UserCondition condition,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(userService.listByCondition(condition));
        }catch(Exception e){
            log.error("获取用户列表报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
}
