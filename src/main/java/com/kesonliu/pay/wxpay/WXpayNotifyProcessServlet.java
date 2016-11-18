package com.kesonliu.pay.wxpay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.kesonliu.pay.wxpay.utils.ParseXMLUtils;

/**
 * Servlet implementation class TenpayNotifyProcessServlet
 */
@WebServlet("/WXpayNotifyProcessServlet")
public class WXpayNotifyProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(WXpayNotifyProcessServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WXpayNotifyProcessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger logger = Logger.getLogger(WXpayNotifyProcessServlet.class);
		logger.info("enter this page notify url");
		
		//获取service层的类来处理数据
		//OrderServiceImpl orderService = SpringContextUtils.springContextUtils.getBean(OrderServiceImpl.class);
		
		//获取微信支付POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		//获取微信回调的请求的流
		InputStream in = request.getInputStream();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		
		while((len = in.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		
		String result = new String(out.toByteArray(),"UTF-8");		
		//关闭流
		out.close();
		in.close();	
		Map<String,String> wxResultMap = ParseXMLUtils.jdomParseXml(result);
		
//		//打印微信回调的参数值
//		Set<String> keySet = wxResultMap.keySet();
//		for(String name : keySet){
//			String value = wxResultMap.get(name);
//			logger.info("key: "+name+"============ value:"+value);
//		}
		
		String return_code = wxResultMap.get("return_code");
		String result_code = wxResultMap.get("result_code");
		
		
//		Map requestParams = request.getParameterMap();
//		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//			String name = (String) iter.next();
//			String[] values = (String[]) requestParams.get(name);
//			String valueStr = "";
//			for (int i = 0; i < values.length; i++) {
//				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//			}
//			logger.info("key:" + name + "============= value:" + valueStr);
//			params.put(name, valueStr);
//		}
		
		//检查对应业务数据的状态，判断是否已处理(微信提示：在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱)
		
		
//		String return_code = request.getParameter("return_code");//微信返回的状态码
		if("SUCCESS".equals(return_code)){
			//为"SUCCESS"时才去取result_code值
//			String result_code = request.getParameter("result_code");
			logger.info("validate the return_code success!");
			if("SUCCESS".equals(result_code)){
				//获取我们商户的订单号
//				String out_trade_no = request.getParameter("out_trade_no");
				logger.info("validate the result_code success!");
				String out_trade_no = wxResultMap.get("out_trade_no");
				String trade_no = wxResultMap.get("transaction_id");
//				//应对重复通知
//				BookOrder bookOrder = orderDao.getBookOrderById(Long.parseLong(out_trade_no));
//				int status = bookOrder.getStatus();
//				if(status != 3){//未成功处理,则去处理
//					//我们自己的业务代码
//					orderService.completeWXOrder(out_trade_no, trade_no);			
//				}				
				//我们自己的业务代码
				//orderService.completeWXOrder(out_trade_no, trade_no);			
				
				//返回通知微信，我们成功接收到微信回调了
				response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
				logger.info("have send success to WX !!");
			}else{
				//没成功则,获取失败的信息打印
				String err_code_des = wxResultMap.get("err_code_des");
				logger.error(err_code_des);
			}
			
			
		}
		
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
