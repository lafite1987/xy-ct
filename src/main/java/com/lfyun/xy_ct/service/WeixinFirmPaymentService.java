package com.lfyun.xy_ct.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lfyun.xy_ct.common.util.MessageDigestUtil;
import com.lfyun.xy_ct.common.util.UuidUtil;
import com.lfyun.xy_ct.common.wx.WeiXinResultXMLBean;
import com.lly835.bestpay.utils.XmlUtil;

/**
 * 微信企业付款：用于用户收益提现
 * @author honeyleo
 *
 */
@SuppressWarnings("deprecation")
@Service
public class WeixinFirmPaymentService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinFirmPaymentService.class);

	@Value("${wechat.mpAppId}")
    private String appId;

    @Value("${wechat.mpAppSecret}")
    private String appSecret;
    
    @Value("${wechat.mchId}")
    private String mchId;
    
    @Value("${wechat.mchKey}")
    private String mchKey;
    
    @Value("${wechat.keyPath}")
    private String keyPath;
    
    @Value("${wechat.firmPay.url}")
    private String url;
    
	public WeiXinResultXMLBean firmPay(String openid, Double amount, String desc,
			String trade_no) {
		try {
			StringBuilder sb = new StringBuilder();
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(
					new File(keyPath));
			try {
				keyStore.load(instream, mchId.toCharArray());
			} finally {
				instream.close();
			}

			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadKeyMaterial(keyStore, mchId.toCharArray())
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, new String[]{"TLSv1"}, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();
			HttpPost httpPost = new HttpPost(url);
			String weiXinFirmPay = getWeiXinFirmPay(openid, amount, desc, trade_no);
			HttpEntity httEntity = new StringEntity(weiXinFirmPay, "UTF-8");
			logger.info("Weixin FirmPay Request:" + weiXinFirmPay);
			httpPost.setEntity(httEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String text;
				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
			}
//			sb.append("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[]]></return_msg><nonce_str><![CDATA[9f53d83ec0691550f7d2507d57f4f5a2]]></nonce_str><result_code><![CDATA[SUCCESS]]></result_code><partner_trade_no><![CDATA[2ffc492ceb284e389384a3d0c3baf0e3]]></partner_trade_no><payment_no><![CDATA[1000018301201710250845424430]]></payment_no><payment_time><![CDATA[2017-10-25 23:28:35]]></payment_time></xml>");
//			sb.append("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[NO_AUTH]]></return_msg><result_code><![CDATA[FAIL]]></result_code><err_code><![CDATA[NO_AUTH]]></err_code><err_code_des><![CDATA[产品权限验证失败,请查看您当前是否具有该产品的权限]]></err_code_des></xml>");
			logger.info("Weixin FirmPay Response:" + sb.toString());
			WeiXinResultXMLBean weiXinResultXMLBean = (WeiXinResultXMLBean)XmlUtil.fromXML(sb.toString(), WeiXinResultXMLBean.class);
			return weiXinResultXMLBean;
		} catch (Throwable e) {
			logger.error("微信付款发生异常", e);
		}
		return null;
	}
    
	private String getWeiXinFirmPay(String openid, Double amount, String desc,
			String trade_no) {
		StringBuffer xmlData = new StringBuffer();
		StringBuffer strData = new StringBuffer();
		Double fee = amount * 100;
		xmlData.append("<xml>");
		xmlData.append("<amount>" + fee.intValue() + "</amount>");// 提现金额
		strData.append("amount=" + fee.intValue());
		xmlData.append("<check_name>" + "NO_CHECK" + "</check_name>");
		strData.append("&check_name=" + "NO_CHECK");
		xmlData.append("<desc>" + desc + "</desc>");
		strData.append("&desc=" + desc);
		xmlData.append("<mch_appid>" + appId + "</mch_appid>"); // 应用ID
		strData.append("&mch_appid=" + appId);
		xmlData.append("<mchid>" + mchId + "</mchid>"); // 商户号
		strData.append("&mchid=" + mchId);
		String nonce_str = UuidUtil.uuid();// "b957d47523c4e92a822cc38703a3bcc3";
		xmlData.append("<nonce_str>" + nonce_str + "</nonce_str>"); // 随机字符串
		strData.append("&nonce_str=" + nonce_str);
		xmlData.append("<openid>" + openid + "</openid>");
		strData.append("&openid=" + openid);
		xmlData.append("<partner_trade_no>" + trade_no + "</partner_trade_no>"); // 商户订单号
		strData.append("&partner_trade_no=" + trade_no);
		xmlData.append("<spbill_create_ip>120.77.255.241</spbill_create_ip>"); // 终端IP
		strData.append("&spbill_create_ip=120.77.255.241");
		strData.append("&key=" + mchKey);
		String sign = MessageDigestUtil.getMD5(strData.toString());
		xmlData.append("<sign>" + sign + "</sign>"); // 签名
		xmlData.append("</xml>");
		return xmlData.toString();
	}
}
