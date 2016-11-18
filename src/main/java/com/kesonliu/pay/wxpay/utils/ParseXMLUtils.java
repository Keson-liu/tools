package com.kesonliu.pay.wxpay.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;



public class ParseXMLUtils {

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static void beginXMLParse(String xml){
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); //

			Element rootElt = doc.getRootElement(); // 

			System.out.println("根元素的名称："+rootElt.getName());

			Iterator iters = rootElt.elementIterator("sendResp"); // 

			while (iters.hasNext()) {
				Element recordEle1 = (Element) iters.next();
				Iterator iter = recordEle1.elementIterator("sms");

				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					String phone = recordEle.elementTextTrim("phone"); // 

					String smsID = recordEle.elementTextTrim("smsID"); // 

					System.out.println(phone+":"+smsID);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析某个子元素的text内容
	 * @param xml
	 */
	public static void xpathParseXml(String xml){
		try { 
			StringReader read = new StringReader(xml);
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(read);
			String xpath ="/xml/appid";
			System.out.print(doc.selectSingleNode(xpath).getText());  
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
	}

	/**
	 * 将一个只有一子级的xml字符串解析到一个map中
	 * @param xml
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> jdomParseXml(String xml){
		try { 
			StringReader read = new StringReader(xml);
			// SAX  InputSource 
			InputSource source = new InputSource(read);
			// SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			
			Map<String,String> resultMap = new HashMap<String,String>();
			// Document
			org.jdom.Document doc;
			doc = (org.jdom.Document) sb.build(source);

			org.jdom.Element root = doc.getRootElement();//
			List<org.jdom.Element> list = root.getChildren();

			if(list!=null&&list.size()>0){
				for (org.jdom.Element element : list) {
					
					resultMap.put(element.getName(), element.getText());
					System.out.println(element.getName()+":"+element.getText());

				}
			}
			
			return resultMap;

		} catch (JDOMException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean parseInt(String key){
		if(!StringUtils.isEmpty(key)){
			if(key.equals("total_fee")||key.equals("cash_fee")||key.equals("coupon_fee")||key.equals("coupon_count")||key.equals("coupon_fee_0")){
				return true;
			}
		}

		return false;
	}



	


}
