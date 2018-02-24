package com.lfyun.xy_ct.ctrl;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lfyun.xy_ct.common.User;
import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;
import com.lfyun.xy_ct.configure.wx.ProjectUrlConfig;
import com.lfyun.xy_ct.entity.UserEntity;
import com.lfyun.xy_ct.exception.SellException;
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
            throw new SellException(ExceptionCodeEnums.WECHAT_MP_ERROR , e.getError().getErrorMsg());
        }
        UserEntity userEntity = userService.getByOpenid(wxMpOAuth2AccessToken.getOpenId());
        if(userEntity == null) {
        	try {
				WxMpUser oauth2getUserInfo = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
				userEntity = new UserEntity();
				userEntity.setOpenid(oauth2getUserInfo.getOpenId());
				userEntity.setNickname(oauth2getUserInfo.getNickname());
				userEntity.setUserType("user");
				userEntity.setAvatar(oauth2getUserInfo.getHeadImgUrl());
				userService.insert(userEntity);
			} catch (WxErrorException e) {
				log.error("【微信网页授权】获取用户信息失败{}", e);
			}
        	
        }
        User user = new User();
    	user.setId(userEntity.getId());
        user.setOpenid(wxMpOAuth2AccessToken.getOpenId());
        user.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
        user.setNickname(userEntity.getNickname());
        user.setAvatar(userEntity.getAvatar());
        sessionManager.save(user, response);
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid="+openId;

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
            throw new SellException(ExceptionCodeEnums.WECHAT_MP_ERROR , e.getError().getErrorMsg());
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid="+openId;

    }
}
