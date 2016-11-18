package com.kesonliu.pay.wxpay.utils;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.kesonliu.pay.wxpay.Unifiedorder;
import com.kesonliu.pay.wxpay.WXpayConfig;

public class SignHelper {

	private static Logger logger = Logger.getLogger(SignHelper.class);
	
	public static SortedMap<Object,Object> getFirstSignMap(String body){
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		//用户appid
		parameters.put("appid", WXpayConfig.appid);
		//商户号
		parameters.put("mch_id", WXpayConfig.mch_id);
		//随机字符串(16位)
		parameters.put("nonce_str", RandCharsUtils.getRandomString(16));
		//通知地址
		String notifyUrl = "";
		notifyUrl = "your server's notifyServletUrl";//PropertiesUtil.getValue("WXPAY_NOTIFY_URL");
		parameters.put("notify_url", notifyUrl);
		//商品描述
		parameters.put("body", body);
		//交易类型
		parameters.put("trade_type", "APP");
//		//用户终端的ip
//		parameters.put("spbill_create_ip", "127.0.0.1");

		return parameters;
	}
	
	public static Unifiedorder getUnifiedorder(){
		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(WXpayConfig.appid);
		unifiedorder.setMch_id(WXpayConfig.mch_id);
//		unifiedorder.setSpbill_create_ip("127.0.0.1");
		String notifyUrl = "";
		notifyUrl = "your server's notifyServletUrl";//PropertiesUtil.getValue("WXPAY_NOTIFY_URL");
		unifiedorder.setNotify_url(notifyUrl);
		unifiedorder.setTrade_type("APP");
		return unifiedorder;
	}
	
	public static SortedMap<Object,Object> getSecondSignMap(){
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appid", WXpayConfig.appid);
		parameters.put("partnerid",WXpayConfig.mch_id);
		parameters.put("noncestr",RandCharsUtils.getRandomString(16));
		parameters.put("timestamp",(System.currentTimeMillis()/1000));//取自1970年1月1日 0点0分0秒以来的秒数(10位数字)
		parameters.put("package","Sign=WXPay");

		return parameters;
	}
}
