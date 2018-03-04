package com.lfyun.xy_ct.common.wx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 微信企业付款返回结果
 * <xml>
	<return_code><![CDATA[SUCCESS]]></return_code>
	<return_msg><![CDATA[]]></return_msg>
	<mch_appid><![CDATA[wxec38b8ff840bd989]]></mch_appid>
	<mchid><![CDATA[10013274]]></mchid>
	<device_info><![CDATA[]]></device_info>
	<nonce_str><![CDATA[lxuDzMnRjpcXzxLx0q]]></nonce_str>
	<result_code><![CDATA[SUCCESS]]></result_code>
	<partner_trade_no><![CDATA[10013574201505191526582441]]></partner_trade_no>
	<payment_no><![CDATA[1000018301201505190181489473]]></payment_no>
	<payment_time><![CDATA[2015-05-19 15：26：59]]></payment_time>
   </xml>
   
   <xml>
	<return_code><![CDATA[FAIL]]></return_code>
	<return_msg><![CDATA[系统繁忙,请稍后再试.]]></return_msg>
	<result_code><![CDATA[FAIL]]></result_code>
	<err_code><![CDATA[SYSTEMERROR]]></err_code>
	<err_code_des><![CDATA[系统繁忙,请稍后再试.]]></err_code_des>
   </xml>
 * @author Luo Siwei
 * 2016-6-23
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="xml")
public class WeiXinResultXMLBean {

	/**
	 * 返回状态码 SUCCESS/FAIL
	 */
	private String return_code;
	
	/**
	 * 返回信息
	 */
	private String return_msg;
	
	/**
	 * 应用ID
	 */
	private String mch_appid;
	
	/**
	 * 商户号
	 */
	private String mchid;
	
	/**
	 * 设备号[暂时没用]
	 */
	private String device_info;
	
	/**
	 * 随机字符串
	 */
	private String nonce_str;
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 业务结果  SUCCESS/FAIL
	 */
	private String result_code;
	
	/**
	 * 商户订单号
	 */
	private String partner_trade_no;
	
	/**
	 * 微信订单号
	 */
	private String payment_no;
	
	/**
	 * 支付时间
	 */
	private String payment_time;
	
	/**
	 * 错误代码
	 */
	private String err_code;
	
	/**
	 * 错误描述
	 */
	private String err_code_des;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getMch_appid() {
		return mch_appid;
	}

	public void setMch_appid(String mch_appid) {
		this.mch_appid = mch_appid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getPartner_trade_no() {
		return partner_trade_no;
	}

	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getPayment_no() {
		return payment_no;
	}

	public void setPayment_no(String payment_no) {
		this.payment_no = payment_no;
	}

	public String getPayment_time() {
		return payment_time;
	}

	public void setPayment_time(String payment_time) {
		this.payment_time = payment_time;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

}
