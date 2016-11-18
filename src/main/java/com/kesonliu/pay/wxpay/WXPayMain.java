package com.kesonliu.pay.wxpay;

import java.util.Map;
import java.util.SortedMap;


import com.kesonliu.pay.wxpay.utils.HttpXmlUtils;
import com.kesonliu.pay.wxpay.utils.ParseXMLUtils;
import com.kesonliu.pay.wxpay.utils.SignHelper;
import com.kesonliu.pay.wxpay.Unifiedorder;
import com.kesonliu.pay.wxpay.utils.WXSignUtils;

public class WXPayMain {
	
	private static String price;//你商品的价格

	public static void main(String[] args) {
		
		// 获取参数，生产预付订单package包，加签名,提交订单，得到prepay id
		String prepayId = "";
		long totalFeeNum = (long) (Double.parseDouble(price) * 100);// 先将费用单位转化为分
		String totalFee = String.valueOf(totalFeeNum);
		
		SortedMap<Object, Object> firstSignMap = SignHelper.getFirstSignMap("your product'name ");
		// 商户订单号
		firstSignMap.put("out_trade_no", Long.toString(1111));//1111换成你的商户的id
		// 总费用
		firstSignMap.put("total_fee", totalFee); // 费用单位为分
		// 用户终端ip
		firstSignMap.put("spbill_create_ip", "127.0.0.1");//当前用户所在的设备的ip

		// 开始签名
		String firstSign = WXSignUtils.createSign("UTF-8", firstSignMap);
		// 准备用于转化的Unifiedorder的类
		Unifiedorder unifiedorder = SignHelper.getUnifiedorder();
		unifiedorder.setNonce_str(firstSignMap.get("nonce_str").toString());
		unifiedorder.setSign(firstSign);
		unifiedorder.setOut_trade_no(firstSignMap.get("out_trade_no").toString());
		unifiedorder.setTotal_fee(totalFee);// 费用单位为分
		unifiedorder.setSpbill_create_ip("127.0.0.1");
		unifiedorder.setBody("description of your product");

		String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
		String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String method = "POST";
		// 按照微信要求的格式访问，返回相应的xml串的返回值
		String weixinResult = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();
		// 解析微信返回的参数，获取微信返回的prepayid
		// 解析返回的xml文件获取其中的prepayid
		Map<String, String> wxResultMap = ParseXMLUtils.jdomParseXml(weixinResult);
		// Set<String> keySet = wxResultMap.keySet();
		// for(String name : keySet){
		// String value = wxResultMap.get(name);
		// logger.info("key: "+name+"============ value:"+value);
		// }

		// 返回状态码为"SUCCESS"才去获取prepay_id值
		String return_code = wxResultMap.get("return_code");
		if ("SUCCESS".equals(return_code)) {
			String result_code = wxResultMap.get("result_code");
			if ("SUCCESS".equals(result_code)) {
				prepayId = wxResultMap.get("prepay_id");
			}
		}
		if (prepayId!=null&&(!prepayId.trim().equals(""))) {
			// 若未获取到prepayId，则返回告诉前端
			return ;//Response.noContent().build();
		}
		// 返回给app端，prepay id 及一些参数
		// 获取prepay_id后的新一次签名
		SortedMap<Object, Object> secondSignMap = SignHelper.getSecondSignMap();
		secondSignMap.put("prepayid", prepayId);
		String secondSign = WXSignUtils.createSign("UTF-8", secondSignMap);
		secondSignMap.put("sign", secondSign);

		//然后将前端调起微信支付所需的参数返回回去
		
	}
}
