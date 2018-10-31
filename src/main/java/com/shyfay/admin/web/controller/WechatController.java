package com.shyfay.admin.web.controller;

import com.shyfay.admin.common.util.IpAddress;
import com.shyfay.admin.common.util.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 牟雪
 * @since 2018/10/31
 */
@RestController
@Api(value="域名控制器", description = "域名控制器")
@RequestMapping("/wx")
@Slf4j
public class WechatController {
    @ApiOperation(value="微信H5支付下单接口", httpMethod="GET", notes = "微信H5支付下单接口")
    @RequestMapping(value="/pay", method= RequestMethod.GET)
    public void pay(HttpServletRequest request, HttpServletResponse response){
        try{
            HashMap<String, String> data = new HashMap<String, String>();
            //nonce_str 随机字符串，不长于32位。
            data.put("nonce_str", WXPayUtil.generateUUID());
            //公众账号ID 微信分配的公众账号ID(应该是注册商户的时候微信给的一个AppID)
            data.put("appid", "wx2c091889a72d528a");
            //商户号 微信支付分配的商户号(应该是注册商户的时候微信给的一个MchID)
            data.put("mch_id", "1444986802");
            //签名类型HMACSHA256
            data.put("sign_type", "HMAC-SHA256");
            //body 商品描述
            data.put("body", "h5支付微信测试");
            //out_trade_no 商户系统内部订单号，商户支付的订单号由商户自定义生成
            data.put("out_trade_no", WXPayUtil.genTimeUUID());
            //fee_type 货币类型 CNY代表人民币
            data.put("fee_type", "CNY");
            //total_fee 订单总金额
            data.put("total_fee", "1");
            //spbill_create_ip APP和网页支付提交用户端ip
            data.put("spbill_create_ip", IpAddress.getIPAddress(request));
            //notify_url 通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
            data.put("notify_url", "http://lzwx.mrwu.xin/shyfay-admin/wx/notify");
            //交易类型--MWEB表示H5支付
            data.put("trade_type", "MWEB");
            //场景信息 这是一个自定义的JSON数据
            String scene_info="{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}}";
            data.put("scene_info", scene_info);
            //签名
            data.put("sign", WXPayUtil.generateSignature(data, "a1f3c33c33bb49c7d9e4a8de1af85c6a"));
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
            httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + data.get("mch_id"));
            //TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            log.info(httpResponse.toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            String xmlResult = EntityUtils.toString(httpEntity, "UTF-8");
            log.info(httpResponse.toString());
            Map<String, String> resultMap = WXPayUtil.processResponseXml(xmlResult);
            log.info(resultMap.toString());
            String mweb_url=resultMap.get("mweb_url");
            response.sendRedirect(mweb_url);
        }catch (Exception e){
            log.error("微信H5支付下单接口："+ e.getMessage());
        }
    }

    @ApiOperation(value="微信H5统一下单回调接口", httpMethod="GET", notes = "微信H5统一下单回调接口")
    @RequestMapping(value="/notify", method= RequestMethod.GET)
    public void notify(HttpServletRequest request){
        log.info("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        log.info(request.toString());
    }
}
