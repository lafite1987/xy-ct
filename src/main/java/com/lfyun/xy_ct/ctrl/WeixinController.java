package com.lfyun.xy_ct.ctrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.lfyun.xy_ct.common.util.MessageDigestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信请求
 * Created by Administrator on 2017/10/18 0018.
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

	private static final String TOKEN = "honeyleo_lafite";
	
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入回调函数");
        log.info("code={}" , code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa6344813f106bc7a&secret=0561d7c09b9cc71b0083e9133e9a903d&code="+code+"&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url , String.class);
        log.info("result={}" , result);
    }
    
    @RequestMapping("/auth2")
	public void auth2(HttpServletRequest request, HttpServletResponse response) {
		
    	log.info("Weixin auth param={}", request.getParameterMap());
		
		try {
			
			String signature = request.getParameter("signature");		//微信加密签名
			String timestamp = request.getParameter("timestamp");		//时间戳
			String nonce	 = request.getParameter("nonce");			//随机数
			String echostr 	 = request.getParameter("echostr");			//字符串
			
			if(null != signature && null != timestamp && null != nonce && null != echostr){/* 接口验证  */
			    List<String> list = new ArrayList<String>(3) { 
				    private static final long serialVersionUID = 2621444383666420433L; 
				    public String toString() {
				               return this.get(0) + this.get(1) + this.get(2); 
				           } 
				   }; 
				   list.add(TOKEN);
				   list.add(timestamp); 
				   list.add(nonce); 
				   Collections.sort(list);
				   String tmpStr = MessageDigestUtil.getSHA1(list.toString());
				   
				    if (signature.equals(tmpStr)) { 
				           response.getWriter().write(echostr);
				     }else { 
				    	 response.getWriter().write(echostr);
			       } 
				    response.getWriter().flush();
				    response.getWriter().close(); 
			}else{/* 消息处理  */
				log.info("进入消息处理...");
				response.reset();
			}
		} catch(Exception e){
			log.error(e.toString(), e);
		}
	}
}
