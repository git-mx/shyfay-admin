package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.service.SysDepartmentService;
import lombok.extern.slf4j.Slf4j;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.web.input.DepartmentAdd;
import com.shyfay.admin.web.input.DepartmentCondition;
import com.shyfay.admin.web.iobean.DepartmentIoentity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/department")
@Api(value="部门管理接口", description = "部门管理接口")
@Slf4j
public class SysDepartmentController {
    @Autowired
    private SysDepartmentService departmentService;

    @ApiOperation(value="添加部门", httpMethod="POST", notes="添加部门")
    @RequestMapping(value = "/addDepartment", method = RequestMethod.POST)
    public ResponseResult addDepartment(
        @ApiParam(required = true, name = "department", value = "POST实体")@RequestBody @Valid DepartmentAdd department,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            departmentService.add(department);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("添加部门报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="修改部门信息", httpMethod="POST", notes = "修改部门信息")
    @RequestMapping(value="/modifyDepartment", method= RequestMethod.POST)
    public ResponseResult modifyDepartment(
            @ApiParam(required=true, name="department", value="POST实体") @RequestBody @Valid DepartmentIoentity department,
            Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            departmentService.modify(department);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("修改部门报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="删除部门", httpMethod="GET", notes="删除部门")
    @RequestMapping(value="/deleteDepartment", method= RequestMethod.GET)
    public ResponseResult delete(
            @ApiParam(required=true, name="departmentId", value="部门ID") @RequestParam Long departmentId){
        if(null == departmentId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            departmentService.delete(departmentId);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("删除部门报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取部门详情", httpMethod="GET", notes="获取部门详情")
    @RequestMapping(value="/detailDepartment", method= RequestMethod.GET)
    public ResponseResult detailDepartment(
            @ApiParam(required=true, name="departmentId", value="部门ID") @RequestParam Long departmentId){
        if(null == departmentId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(departmentService.getById(departmentId));
        }catch(Exception e){
            log.error("获取部门详情报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取部门列表", httpMethod="POST", notes="获取部门列表")
    @RequestMapping(value="/listDepartment", method= RequestMethod.POST)
    public ResponseResult listDepartment(
        @ApiParam(required=true, name="condition", value="POST实体") @RequestBody @Valid DepartmentCondition condition,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(departmentService.listByCondition(condition));
        }catch(Exception e){
            log.error("获取部门列表报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="获取下拉框选择属性", httpMethod="GET", notes="获取下拉框选择属性")
    @RequestMapping(value="/listOption", method= RequestMethod.GET)
    public ResponseResult listOption(){
        try{
            return ResponseResult.success(departmentService.listOption());
        }catch(Exception e){
            log.error("获取下拉框选择属性报错:" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
}
