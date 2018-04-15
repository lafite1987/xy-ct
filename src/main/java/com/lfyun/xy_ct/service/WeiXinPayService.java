package com.lfyun.xy_ct.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lfyun.xy_ct.common.util.WXPayUtil;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.constants.WxPayConstants;

@Component
public class WeiXinPayService {

	private static final Logger log = LoggerFactory.getLogger(WeiXinPayService.class);
	
	@Autowired
	private WxPayH5Config wxPayH5Config;
	
	/**
	 * 
	 * @param notifyData
	 * @return 返回的数据见微信支付异步通知结果文档：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8
	 */
	public Map<String, String> asyncNotify(String notifyData) throws Exception {
		
		Map<String, String> map = WXPayUtil.xmlToMap(notifyData);
		//签名校验
		if(!WXPayUtil.isSignatureValid(map, wxPayH5Config.getMchKey())) {
			log.error("【微信支付异步通知】签名验证失败, response={}", map);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
		}

		if(!WxPayConstants.SUCCESS.equals(map.get("return_code"))) {
			throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + map.get("return_msg"));
		}
		
		if(!WxPayConstants.SUCCESS.equals(map.get("result_code"))
				&& "ORDERPAID".equals(map.get("err_code"))) {
			return map;
		}
		
		if(!WxPayConstants.SUCCESS.equals(map.get("result_code"))) {
			throw new RuntimeException("【微信支付异步通知】发起支付, resultCode != SUCCESS, err_code = " + map.get("err_code") + " err_code_des=" + map.get("err_code_des"));
		}
		return map;
	}
	
	
}
