package com.kesonliu.pay.wxpay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * nonce_str 生成？位随机字符串
 * @author 
 */
public class RandCharsUtils {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String getRandomString(int length) { //执行随机数的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";   
		Random random = new Random();   
		StringBuffer sb = new StringBuffer();
		int number = 0;
		for (int i = 0; i < length; i++) {   
			number = random.nextInt(base.length());   
			sb.append(base.charAt(number));   
		}   
		return sb.toString();   
	}   
	
	/*
	 * 当前的时间
	 */
	public static String timeStart(){
		return df.format(new Date());
	}
	
	/*
	 * 
	 */
	public static String timeExpire(){
		Calendar now=Calendar.getInstance();
		now.add(Calendar.MINUTE,30);
		return df.format(now.getTimeInMillis());
	}


	public static void main(String[] args) {
		/*for (int i = 0; i < 10; i++) {
			System.out.println(""+i+"："+getRandomString(32));
		}*/
		

		System.out.println(timeStart());
		System.out.println(timeExpire());
	}

}
