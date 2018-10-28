package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.common.util.SpringContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 牟雪
 * @since 2018/10/28
 */
@RestController
@Api(value="图片上传控制器", description = "图片上传控制器")
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Autowired
    SpringContextUtils springContextUtils;

    @ApiOperation(value="上传图片", httpMethod="POST", notes = "上传图片")
    @RequestMapping(value="/uploadImage", method= RequestMethod.POST)
    public ResponseResult uploadImage(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        try{
            if (file.isEmpty()) {
                return ResponseResult.success(null);

            }else {
                //保存时的文件名
                String originalName = file.getOriginalFilename();
                String suffix = originalName.substring(originalName.lastIndexOf("."));
                String fireName = UUID.randomUUID().toString() + suffix;

                //保存文件的绝对路径
                WebApplicationContext webApplicationContext = (WebApplicationContext)SpringContextUtils.applicationContext;
                ServletContext servletContext = webApplicationContext.getServletContext();
                String realPath = servletContext.getRealPath("/");
                String filePath = realPath + "USERRESOURCES" + File.separator + fireName;
                log.info("绝对路径：" + filePath);
                File newFile = new File(filePath);

                //MultipartFile的方法直接写文件
                try {

                    //上传文件
                    file.transferTo(newFile);

                    //数据库存储的相对路径
                    String projectPath = servletContext.getContextPath();
                    String contextpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+projectPath;
                    String url = contextpath + "/USERRESOURCES/" + fireName;
                    log.info("相对路径：" + url);
                    return ResponseResult.success(url);
                } catch (IllegalStateException | IOException e) {
                    return new ResponseResult(ExceptionCode.SERVER_ERROR);
                }
            }
        }catch (Exception e){
            log.error("获取动态域名报错："+ e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }
}
