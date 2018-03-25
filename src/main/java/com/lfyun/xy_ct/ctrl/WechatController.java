package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.common.enums.UserType;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.exception.AppException;
import com.lfyun.xy_ct.service.SessionManager;
import com.lfyun.xy_ct.service.UserService;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * Created by Administrator on 2017/10/18 0018.
 */
@Controller
@Slf4j
@RequestMapping("/wechat")
public class WechatController {

	static Logger LOGGER = LoggerFactory.getLogger(WechatController.class);
	
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;
    
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
    	LOGGER.info("returnUrl:{}", returnUrl);
        //1.配置微信公众号信息
        String url = projectUrlConfig.getMpAuthorizeUrl()+"/wxp/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));
        //2.获取网页code
        return "redirect:" + redirectUrl;
    }


    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code , @RequestParam("state") String returnUrl, HttpServletResponse response) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】授权失败{}" , e);
            throw new AppException(ExceptionCodeEnums.WECHAT_MP_ERROR , e.getError().getErrorMsg());
        }
        WxMpUser oauth2getUserInfo = null;
        try {
        	oauth2getUserInfo = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
        } catch (WxErrorException e) {
			log.error("【微信网页授权】获取用户信息失败{}", e);
		}
        UserEntity userEntity = userService.getByOpenid(wxMpOAuth2AccessToken.getOpenId());
        if(userEntity == null) {
        	userEntity = new UserEntity();
			userEntity.setOpenid(oauth2getUserInfo.getOpenId());
			userEntity.setNickname(oauth2getUserInfo.getNickname());
			userEntity.setUserType(UserType.USER.getCode());
			userEntity.setAvatar(oauth2getUserInfo.getHeadImgUrl());
			userService.insert(userEntity);
        } else {
        	boolean isUpdate = false;
        	UserEntity tmpUserEntity = new UserEntity();
        	tmpUserEntity.setId(userEntity.getId());
        	if(!userEntity.getNickname().equals(oauth2getUserInfo.getNickname())) {
        		isUpdate = true;
        		tmpUserEntity.setNickname(oauth2getUserInfo.getNickname());
        	}
        	if(!userEntity.getAvatar().equals(oauth2getUserInfo.getHeadImgUrl())) {
        		isUpdate = true;
        		tmpUserEntity.setAvatar(oauth2getUserInfo.getHeadImgUrl());
        	}
        	if(isUpdate) {
        		userService.updateById(tmpUserEntity);
        	}
        }
        User user = new User();
    	user.setId(userEntity.getId());
        user.setOpenid(wxMpOAuth2AccessToken.getOpenId());
        user.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
        user.setNickname(userEntity.getNickname());
        user.setAvatar(userEntity.getAvatar());
        sessionManager.save(user, response);
        Map<String, String> urlParam = parseUrlParam(returnUrl);
        if(urlParam.containsKey("fromUserId") && StringUtils.isBlank(urlParam.get("fromUserId"))) {
        	urlParam.put("fromUserId", String.valueOf(user.getId()));
        	returnUrl = TruncateUrl(returnUrl)  + concat(urlParam);
        }
        return "redirect:" + returnUrl;

    }

    public static String concat(Map<String, String> urlParam) {
    	if(urlParam == null || urlParam.isEmpty()) {
    		return "";
    	}
    	StringBuilder sb = new StringBuilder();
    	int i = 0;
    	for(Entry<String, String> entry : urlParam.entrySet()) {
    		if(i == 0) {
    			sb.append("?");
    		} else {
    			sb.append("&");
    		}
    		sb.append(entry.getKey()).append("=").append(entry.getValue());
    		i++;
    	}
    	return sb.toString();
    }
    public static Map<String, String> parseUrlParam(String URL) {  
        Map<String, String> mapRequest = new HashMap<String, String>();  
      
        String[] arrSplit = null;  
      
        String strUrlParam = TruncateUrlPage(URL);  
        if (strUrlParam == null) {  
            return mapRequest;  
        }  
        arrSplit = strUrlParam.split("[&]");  
        for (String strSplit : arrSplit) {  
            String[] arrSplitEqual = null;  
            arrSplitEqual = strSplit.split("[=]");  
      
            //解析出键值  
            if (arrSplitEqual.length > 1) {  
                //正确解析  
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);  
      
            } else {  
                if (arrSplitEqual[0] != "") {  
                    //只有参数没有值，不加入  
                    mapRequest.put(arrSplitEqual[0], "");  
                }  
            }  
        }  
        return mapRequest;  
    }  
    
    /** 
     * 去掉url中的路径，留下请求参数部分 
     * 
     * @param strURL url地址 
     * @return url请求参数部分 
     */  
    private static String TruncateUrlPage(String strURL) {  
        String strAllParam = null;  
        String[] arrSplit = null;  
  
        strURL = strURL.trim();  
  
        arrSplit = strURL.split("[?]");  
        if (strURL.length() > 1) {  
            if (arrSplit.length > 1) {  
                if (arrSplit[1] != null) {  
                    strAllParam = arrSplit[1];  
                }  
            }  
        }  
  
        return strAllParam;  
    }  
    
    /** 
     * 去掉url中请求参数，留下URL
     * 
     * @param strURL url地址 
     * @return url
     */  
    private static String TruncateUrl(String strURL) {  
        String url = null;  
        String[] arrSplit = null;  
  
        strURL = strURL.trim();  
  
        arrSplit = strURL.split("[?]");  
        if (strURL.length() >= 1) {  
            url = arrSplit[0];
        }  
  
        return url;  
    }  
    
    public static void main(String[] args) {
    	Map<String, String> parseUrlParam = parseUrlParam("http://api.mcwh123.com/wxp/recharge.htm?fromUserId=&productId=1001001");
    	System.out.println(parseUrlParam);
	}
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) {
        //1.配置微信公众号信息
        String url = projectUrlConfig.getOpenAuthorizeUrl()+"/wxp/wechat/qrUserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url , WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN , URLEncoder.encode(returnUrl));
        //2.获取网页code
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code , @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】授权失败{}" , e);
            throw new AppException(ExceptionCodeEnums.WECHAT_MP_ERROR , e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid="+openId;

    }
}
