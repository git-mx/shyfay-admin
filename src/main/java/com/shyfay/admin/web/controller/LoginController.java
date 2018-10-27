package com.shyfay.admin.web.controller;


import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.common.constant.Constant;
import com.shyfay.admin.common.util.IpAddress;
import lombok.extern.slf4j.Slf4j;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.common.util.Encrypt;
import com.shyfay.admin.service.SysUserService;
import com.shyfay.admin.web.input.UserLogin;
import com.shyfay.admin.web.input.UserPassword;
import com.shyfay.admin.web.output.UserDetail;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(value="登录相关接口", description = "登录相关接口")
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    SysUserService userService;


    @ApiOperation(value = "获取验证码", httpMethod = "GET", notes = "获取验证码")
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public ResponseResult getCode(HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        String redisKey = Constant.CODE_KEY_PREFIX + IpAddress.getIPAddress(request);
        redisTemplate.opsForValue().set(redisKey, capText);
        redisTemplate.expire(redisKey, 5L, TimeUnit.MINUTES);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
            return null;
        }
    }

    @ApiOperation(value="用户登录", httpMethod = "POST", notes = "用户登录")
    @RequestMapping(value="/userLogin", method = RequestMethod.POST)
    public ResponseResult userLogin(
            @ApiParam(required=true, name="user", value="登录信息") @RequestBody @Valid UserLogin user,
            HttpServletRequest request,
            Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            String redisKey = Constant.CODE_KEY_PREFIX + IpAddress.getIPAddress(request);
            String code = redisTemplate.opsForValue().get(redisKey);
            if(StringUtils.isBlank(code)){
                return new ResponseResult(ExceptionCode.CODE_EXPIRED);
            }
            if(!code.equalsIgnoreCase(user.getLoginCode())){
                return new ResponseResult(ExceptionCode.CODE_ERROR);
            }
            UserDetail userDetail = userService.getByLogin(user);
            if(null == userDetail){
                return new ResponseResult(ExceptionCode.LOGIN_ERROR);
            }
            Map<String, String> resultMap = new HashMap<>();
            String sessionId = UUID.randomUUID().toString();
            resultMap.put("sessionId", sessionId);
            resultMap.put("userId", userDetail.getUserId().toString());
            resultMap.put("userName", userDetail.getUserName());
            String sessionKey = Constant.SESSION_KEY_PREFIX + sessionId;
            redisTemplate.opsForValue().set(sessionKey, userDetail.getUserId().toString());
            redisTemplate.expire(sessionKey, 30L, TimeUnit.MINUTES);
            return ResponseResult.success(resultMap);

        }catch (Exception e){
            log.error("用户登录报错：" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/userExit", method = RequestMethod.GET)
    @ApiOperation(value = "退出登录", httpMethod = "GET", notes = "退出登录")
    public ResponseResult userExit(HttpServletRequest request){
        try{
            String sessionId = request.getHeader("sessionId");
            String sessionKey = Constant.SESSION_KEY_PREFIX + sessionId;
            redisTemplate.delete(sessionKey);
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch (Exception e){
            log.error("用户退出报错：" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", httpMethod = "POST", notes = "修改密码")
    public ResponseResult modifyPassword(
        @ApiParam(required = true, name="userPassword", value="POST实体")
        @RequestBody @Valid UserPassword userPassword,
        Errors errors
    ){
        if(errors.hasErrors()){
            return new ResponseResult(ExceptionCode.PARAM_ILLEGAL);
        }
        try{
            UserDetail user = userService.getById(userPassword.getUserId());
            if(null == user){
                return new ResponseResult(ExceptionCode.FAIL);
            }
            String encodePassword = Encrypt.md5(userPassword.getOldPassword());
            if(user.getLoginPassword().equals(encodePassword)){
                userService.modifyPassword(user.getUserId(), Encrypt.md5(userPassword.getNewPassword()));
                return new ResponseResult(ExceptionCode.SUCCESS);
            }
            return new ResponseResult(ExceptionCode.OLD_PASSWORD_ERROE);
        }catch (Exception e){
            log.error("修改密码报错：" + e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
}
