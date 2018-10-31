package com.shyfay.admin.common.util;

import com.shyfay.admin.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author 牟雪
 * @since 2018/10/29
 */
@Component
public class OrderNumberUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getOrderNumber(){
        LocalDateTime emYearMonthTemp = LocalDateTime.ofEpochSecond(System.currentTimeMillis()/1000,0, ZoneOffset.of("+8"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowStr = emYearMonthTemp.format(formatter);
        String reidsKeyPrefix = Constant.ORDER_NUMBER_REDIS_KEY + nowStr;
        Long keyvalue;
        keyvalue=  redisTemplate.opsForValue().increment(reidsKeyPrefix, 1L);
        if(1==keyvalue){
            redisTemplate.expire(reidsKeyPrefix,1L, TimeUnit.DAYS);
        }
        return Constant.ORDER_NUMBER_PREFIX + nowStr + String.format("%04d", keyvalue);
    }
}
