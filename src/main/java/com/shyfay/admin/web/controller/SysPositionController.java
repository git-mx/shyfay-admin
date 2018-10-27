package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.web.input.PositionAdd;
import lombok.extern.slf4j.Slf4j;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.service.SysPositionService;
import com.shyfay.admin.web.input.PositionCondition;
import com.shyfay.admin.web.iobean.PositionIoentity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/position")
@Api(value="职位管理接口", description = "职位管理接口")
@Slf4j
public class SysPositionController {
    @Autowired
    private SysPositionService positionService;

    @ApiOperation(value="添加职位", httpMethod="POST", notes="添加职位")
    @RequestMapping(value = "/addPosition", method = RequestMethod.POST)
    public ResponseResult addPosition(
        @ApiParam(required = true, name = "position", value = "POST实体")@RequestBody @Valid PositionAdd position,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            positionService.add(position);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("添加职位报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="修改职位信息", httpMethod="POST", notes = "修改职位信息")
    @RequestMapping(value="/modifyPosition", method= RequestMethod.POST)
    public ResponseResult modifyPosition(
            @ApiParam(required=true, name="position", value="POST实体") @RequestBody @Valid PositionIoentity position,
            Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            positionService.modify(position);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("修改职位信息报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="删除职位", httpMethod="GET", notes="删除职位")
    @RequestMapping(value="/deletePosition", method= RequestMethod.GET)
    public ResponseResult delete(
            @ApiParam(required=true, name="positionId", value="职位ID") @RequestParam Long positionId){
        if(null == positionId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            positionService.delete(positionId);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            log.error("删除职位报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取职位详情", httpMethod="GET", notes="获取职位详情")
    @RequestMapping(value="/detailPosition", method= RequestMethod.GET)
    public ResponseResult detailPosition(
            @ApiParam(required=true, name="positionId", value="职位ID") @RequestParam Long positionId){
        if(null == positionId){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(positionService.getById(positionId));
        }catch(Exception e){
            log.error("获取职位详情报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
    @ApiOperation(value="获取职位列表", httpMethod="POST", notes="获取职位列表")
    @RequestMapping(value="/listPosition", method= RequestMethod.POST)
    public ResponseResult listPosition(
        @ApiParam(required=true, name="condition", value="POST实体") @RequestBody @Valid PositionCondition condition,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            return ResponseResult.success(positionService.listByCondition(condition));
        }catch(Exception e){
            log.error("获取职位列表报错" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="获取下拉框选择属性", httpMethod="GET", notes="获取下拉框选择属性")
    @RequestMapping(value="/listOption", method= RequestMethod.GET)
    public ResponseResult listOption(){
        try{
            return ResponseResult.success(positionService.listOption());
        }catch(Exception e){
            log.error("获取下拉框选择属性报错:" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
}
