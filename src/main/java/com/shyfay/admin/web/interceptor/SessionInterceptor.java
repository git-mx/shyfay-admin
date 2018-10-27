package com.shyfay.admin.web.interceptor;


import com.alibaba.fastjson.JSON;
import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.common.constant.Constant;
import com.shyfay.admin.common.base.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Description： 登录拦截器
 * @author: SHENZL
 * @since: 2017/11/10 18:20
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER= LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        response.setHeader("Content-type", "text/json;charset=UTF-8");
        String sessionId = "";
        sessionId = request.getHeader("sessionId");
        if(!StringUtils.isNotBlank(sessionId)){
            sessionId = request.getParameter("sessionId");
            if(StringUtils.isBlank(sessionId)){
                response.getWriter().print(JSON.toJSON(new ResponseResult(ExceptionCode.SESSION_EXPIRE)));
                return false;
            }
        }
        String redisKey = Constant.SESSION_KEY_PREFIX + sessionId;
        Long expireTime = redisTemplate.getExpire(redisKey);
        if(null == expireTime || expireTime < 0){
            response.getWriter().print(JSON.toJSON(new ResponseResult(ExceptionCode.SESSION_EXPIRE)));
            return false;
        }
        redisTemplate.expire(redisKey, 30L, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}
