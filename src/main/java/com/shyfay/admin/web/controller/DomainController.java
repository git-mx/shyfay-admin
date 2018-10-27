package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="域名控制器", description = "域名控制器")
@RequestMapping("/domain")
@Slf4j
public class DomainController {

    @ApiOperation(value="获取动态域名", httpMethod="GET", notes = "获取动态域名")
    @RequestMapping(value="/getDomain", method= RequestMethod.GET)
    public ResponseResult getDomain(){
        try{
            return ResponseResult.success(Constant.domainUrl);
        }catch (Exception e){
            log.error("获取动态域名报错："+ e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

}
