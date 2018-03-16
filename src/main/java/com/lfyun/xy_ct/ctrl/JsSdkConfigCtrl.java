package com.lfyun.xy_ct.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lfyun.xy_ct.common.util.DateUtil;
import com.lfyun.xy_ct.common.util.HttpUtil;
import com.lfyun.xy_ct.common.util.SignUtil;
import com.lfyun.xy_ct.dto.JsSdkParam;
import com.lfyun.xy_ct.dto.Token;

@RestController
public class JsSdkConfigCtrl {

	private static final Logger logger = LoggerFactory.getLogger(JsSdkConfigCtrl.class);
	
	@Value("${wechat.mpAppId}")
    private String appId;

    @Value("${wechat.mpAppSecret}")
    private String appSecret;
    
	private Token token;
	
    @RequestMapping("/jssdk_config")
	public JsSdkParam getJsSdkConfig(@RequestParam("url")String jsapi_url) {
        JsSdkParam jsapiParam = new JsSdkParam();
        jsapiParam.setAppId(this.appId);

        // 1.get token
        Token token = getToken();

        // 2.用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）：
        // https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
//        {
//
//            "errcode":0,
//
//                "errmsg":"ok",
//
//                "ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
//
//                "expires_in":7200
//
//        }

//        成功返回如下JSON：
        //去获取新token
        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
        params.add(new BasicNameValuePair("type", "jsapi"));

        String jsonStr = HttpUtil.getUrl(url, params);
        logger.debug(jsonStr);

        //判断返回结果
        JSONObject param = (JSONObject) JSON.parse(jsonStr);

        String jsapi_ticket =  (String) param.get("ticket");


        // 3. 签名
        Map<String, String> ret = SignUtil.sign(jsapi_ticket, jsapi_url);
        jsapiParam.setTimestamp(ret.get("timestamp"));
        jsapiParam.setNonceStr(ret.get("nonceStr"));
        jsapiParam.setSignature(ret.get("signature"));

        jsapiParam.setUrl(jsapi_url);

        return jsapiParam;
    }
	
	/**
     * 获取 access_token
     * @return
     */
    public Token getToken() {

        long pastSeconds = 0;
        if (token != null) {
            pastSeconds = DateUtil.getPastSeconds(token.getCreateTime());
        }

        if (token == null || pastSeconds >= token.getExpires_in()) {
            //去获取新token
            //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            String url = "https://api.weixin.qq.com/cgi-bin/token?";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("grant_type", "client_credential"));
            params.add(new BasicNameValuePair("appid", appId));
            params.add(new BasicNameValuePair("secret", appSecret));

            String jsonStr = HttpUtil.getUrl(url, params);

            //System.out.println(url);
            logger.debug(jsonStr);

            //判断返回结果
            JSONObject param = (JSONObject) JSON.parse(jsonStr);

            token = new Token();
            token.setAccess_token((String) param.get("access_token"));
            token.setExpires_in((Integer) param.get("expires_in"));
            token.setCreateTime(DateUtil.getCurDateTime());
        }
        return token;
    }
}
