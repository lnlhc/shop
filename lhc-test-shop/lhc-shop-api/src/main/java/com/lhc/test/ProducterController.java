package com.lhc.test;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhc.shop.utils.ActiveStatusConstants;
import com.lhc.shop.utils.Response;

@Controller
@RequestMapping("/shop/jms")
public class ProducterController {
	private final Logger logger = Logger.getLogger(getClass());
	
	private JmsTemplate jms;
	
	/**
	 * @Description: 发送消息  生产者
	 * @param token
	 * @author liuhaichao
	 * @date 2017年4月13日
	 * @return
	 * @throws Exception
    */
	@ResponseBody
	@RequestMapping(value = { "/sendSms" }, method = {RequestMethod.GET, RequestMethod.POST })
	public String sendSms(){
		logger.info("开始发送消息。。。。。。。。。。。" );
		
		try {
			for (int i = 0; i < 5; i++) {
				jms.convertAndSend("发送的消息:" +i);
			}
			
			logger.info("已成功发送消息。。。。。。。。。。。");
			return Response.build()
					.setStatus(ActiveStatusConstants.Common.SUCCESS.status)
					.setMessage(ActiveStatusConstants.Common.SUCCESS.message)
					.toJSON();
		} catch (Exception e) {
			logger.info("发送短息失败");
			return Response.build()
					.setStatus(ActiveStatusConstants.Common.SYSTEM_ERROE.status)
					.setMessage(ActiveStatusConstants.Common.SYSTEM_ERROE.message).
					toJSON();
		}
	}
}
