package com.kesonliu.pay.alipay;

import com.kesonliu.pay.alipay.utils.RSA;

public class AliPayMain {

	
	public static void main(String[] args) throws Exception {
		
		//生成准备签名的字串
		String orderInfo = OrderGenrate.getOrderInfo("名称", "商品详情", "价格", "我们服务器的订单号");
		
		String sign = RSA.sign(orderInfo, AlipayConfig.private_key, "utf-8");
		
		
		//sign连同orderInfo传给客户端,发起支付
	}
	
}
