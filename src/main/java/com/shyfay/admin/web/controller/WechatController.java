package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.base.ExceptionCode;
import com.shyfay.admin.common.base.ResponseResult;
import com.shyfay.admin.common.constant.Constant;
import com.shyfay.admin.common.util.IpAddress;
import com.shyfay.admin.common.util.WXPayUtil;
import com.shyfay.admin.web.input.PreOrderInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 *
 *
 *@author 牟雪
 *@since 2018/10/31
 */
// TODO H5支付会遇到的几种情况：
// TODO 1.在使用泸州通APP支付的时候调起微信之后还没有支付，就把微信退出了，回到泸州通APP可以正常展示支付失败页面
// TODO 2.在使用泸州通APP支付的时候调起微信之后一直停留在微信APP上，一直不支付，如果回到使用泸州通APP可以正常展示支付失败页面，
// TODO   在支付失败页面展示完之后，用户再回到微信进行支付，支付完成之后后台正常处理逻辑，这时候微信会调起手机浏览器
// TODO   并且定位到获取支付结果页面，再展示支付成功页面，这就会导致展示的不友好性，不过这并不影响业务处理，因为只要支付成功
// TODO   我们就处理后台逻辑，用户下一次再进入使用泸州通APP查看自己的考试报名信息会发现是已支付状态。
// TODO 3.在支付的时候调起微信之后一直停留在微信APP上，然后把我们的泸州通关掉，又回到微信进行支付，支付完整之后可以正常处理
// TODO   逻辑，只是支付完之后微信APP又会调起手机浏览器访问支付结果页面
// TODO 4.由于微信原因导致的支付失败还没遇到过
@RestController
@Api(value="微信H5支付控制器", description = "微信H5支付控制器")
@RequestMapping("/wx")
@Slf4j
public class WechatController {

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    //发起支付所在服务的域名一定要在微信商户系统里设置白名单，一个微信商户最多可以设置5个域名白名单
    //设置的回调接口的域名必须与发起支付所在服务的域名保持一致
    //设置的支付成功后的跳转页面的域名必须与发起支付所在服务的域名保持一致，且必须是发起支付所在服务的页面

    @ApiOperation(value="微信H5支付下单接口", httpMethod="GET", notes = "微信H5支付下单接口")
    @RequestMapping(value="/pay", method= RequestMethod.GET)
    public void pay(HttpServletRequest request, HttpServletResponse response){
        try{
            String totalFee = redisTemplate.opsForValue().get(Constant.SELF_ORDER_PREFIX + IpAddress.getIPAddress(request));
            if(StringUtils.isBlank(totalFee) || Integer.valueOf(totalFee) <= 0){
                response.sendRedirect("http://lzwx.mrwu.xin/shyfay-admin/wx/order.html?expire=1");
            }
            String ipAddress = IpAddress.getIPAddress(request);
            HashMap<String, String> data = new HashMap<String, String>();
            //nonce_str 随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateUUID());
            //公众账号ID 微信分配的公众账号ID(应该是注册商户的时候微信给的一个AppID)
            data.put("appid", Constant.WX_PAY_APP_ID);
            //商户号 微信支付分配的商户号(应该是注册商户的时候微信给的一个MchID)
            data.put("mch_id", Constant.WX_PAY_MCH_ID);
            //签名类型HMACSHA256
            data.put("sign_type", "HMAC-SHA256");
            //body 商品描述
            data.put("body", "h5支付微信测试");
            //out_trade_no 商户系统内部订单号，商户支付的订单号由商户自定义生成
            data.put("out_trade_no", WXPayUtil.genTimeUUID());
            //fee_type 货币类型 CNY代表人民币
            data.put("fee_type", "CNY");
            //total_fee 订单总金额
            data.put("total_fee", totalFee);
            //spbill_create_ip APP和网页支付提交用户端ip
            data.put("spbill_create_ip", ipAddress);
            //notify_url 通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
            //该地址的域名必须与发起支付所在服务的域名一致
            data.put("notify_url", "http://lzwx.mrwu.xin/shyfay-admin/wx/notify");
            //交易类型--MWEB表示H5支付
            data.put("trade_type", "MWEB");
            //场景信息 这是一个自定义的JSON数据
            String scene_info="{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}}";
            data.put("scene_info", scene_info);
            //签名
            data.put("sign", WXPayUtil.generateSignature(data, Constant.WX_PAY_SING_KEY));
            log.info(data.toString());
            //请求体
            String reqBody = WXPayUtil.mapToXml(data);
            BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connManager)
                    .build();
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            StringEntity postEntity = new StringEntity(reqBody, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            //TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
            httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + data.get("mch_id"));
            httpPost.setEntity(postEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String xmlResult = EntityUtils.toString(httpEntity, "UTF-8");
            Map<String, String> resultMap = WXPayUtil.processResponseXml(xmlResult);
            //下单成功之后吧openId存到redis里面，这个openId是另一个redis的key
            String mwebUrl = resultMap.get("mweb_url");
            if(StringUtils.isNotBlank(mwebUrl)){
                //重定向到mwebUrl发起支付，拼接自己的redirect_url到后面，支付完成之后会重定向到这个页面，这个url需要urlencode;
                mwebUrl += "&redirect_url=" + URLEncoder.encode("http://lzwx.mrwu.xin/shyfay-admin/wx/result.html");
                log.info("拼接后的URL：" + mwebUrl);
                response.sendRedirect(mwebUrl);
            }else{
                response.sendRedirect("http://lzwx.mrwu.xin/shyfay-admin/wx/fail.html");
            }
        }catch (Exception e){
            log.error("微信H5支付下单接口："+ e.getMessage());
        }
    }

    @ApiOperation(value="微信H5统一下单回调接口", notes = "微信H5统一下单回调接口")
    @RequestMapping(value = "/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            //sb为微信返回的xml
            String notityXml = sb.toString();
            String resXml="";
            log.info("接收到的报文：" + notityXml);

            Map<String, String> map = WXPayUtil.xmlToMap(notityXml);
            log.info("得到的MAP：" + map.toString());

            String returnCode = map.get("return_code");
            if("SUCCESS".equals(returnCode)){
                String redisKey = "redis-key-temp";
                redisTemplate.opsForValue().set(redisKey, "SUCCESS");
                redisTemplate.expire(redisKey, 2L, TimeUnit.MINUTES);
                //验证签名是否正确
                //Map<String, String> validParams = WXPayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
                //String validStr = WXPayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
                String sign = WXPayUtil.generateSignature(map, Constant.WX_PAY_SING_KEY).toUpperCase();//拼装生成服务器端验证的签名
                log.info("我们生成的签名：" + sign);
                log.info("微信返回来的签名：" + map.get("sign"));
                // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

                //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
                if(sign.equals(map.get("sign"))){
                    //在此处处理自己的业务逻辑，例如标记数据库的支付结果字段，微信订单号字段等等。
                    //处理业务逻辑的时候，系统使用out_trade_no来判断当前回调的是哪一个支付信息
                    //即在统一下单的时候传送给微信一个out_trade_no，在微信回调的时候会把out_trade_no这个参数返回回来，
                    //我们就可以通过out_trade_no把统一下单和回调一一对应起来了
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    log.info("SUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESS");
                } else {
                    log.info("微信支付回调失败!签名不一致");
                }
            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                String redisKey = "redis-key-temp";
                redisTemplate.opsForValue().set(redisKey, "FAIL");
                redisTemplate.expire(redisKey, 2L, TimeUnit.MINUTES);
            }
            log.info("返回的XML：" + resXml);

            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        }catch (Exception e){
            log.info("回调异常：" + e.getMessage());
        }
    }

    @ApiOperation(value="微信H5统一下单提交订单接口", httpMethod="POST", notes = "微信H5统一下单提交订单接口")
    @RequestMapping(value="/preOrder", method= RequestMethod.POST)
    public ResponseResult preOrder(HttpServletRequest request, @RequestBody PreOrderInput input) {
        try{
            String redisKey = Constant.SELF_ORDER_PREFIX + IpAddress.getIPAddress(request);
            redisTemplate.opsForValue().set(redisKey, input.getTotalAmount());
            //确认下单页面一分钟后失效
            redisTemplate.expire(redisKey, 1L, TimeUnit.MINUTES);
            log.info("下单成功", input.toString());
            return new ResponseResult(ExceptionCode.SUCCESS);
        }catch(Exception e){
            redisTemplate.opsForValue().set(Constant.SELF_ORDER_PREFIX + IpAddress.getIPAddress(request), input.getTotalAmount());
            log.info("下单错误：", e.getMessage());
            return new ResponseResult(ExceptionCode.SERVER_ERROR);
        }
    }

    @ApiOperation(value="获取支付结果接口", httpMethod="GET", notes = "获取支付结果接口")
    @RequestMapping(value="/getResult", method= RequestMethod.GET)
    public ResponseResult getResult(HttpServletRequest request) {
        log.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        try{
            String key = "redis-key-temp";
            if(StringUtils.isNotBlank(key)){
                String result = redisTemplate.opsForValue().get(key);
                if(StringUtils.isNotBlank(result)){
                    if(result.equals("SUCCESS")){
                        return new ResponseResult(ExceptionCode.SUCCESS);
                    }else{
                        return new ResponseResult(ExceptionCode.FAIL);
                    }
                }else{
                    return new ResponseResult(ExceptionCode.KEEP_WAITING);
                }
            }else{
                return new ResponseResult(ExceptionCode.FAIL);
            }
        }catch(Exception e){
            log.info("获取结果失败：", e.getMessage());
            return new ResponseResult(ExceptionCode.FAIL);
        }
    }

}

