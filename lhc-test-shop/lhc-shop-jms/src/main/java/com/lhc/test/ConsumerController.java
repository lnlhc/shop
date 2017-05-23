package com.lhc.test;

import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/shop/jms")
public class ConsumerController{
	private final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private  JmsTemplate jms;
	
	 

	/* public  String receiveMotorist() throws Exception{  
		    while(true){
		    	TextMessage message  = (TextMessage)jms.receive();  
		    	if (message != null) {
		    		 logger.info("接收的消息为:"+ message.getText());
				}else{
					break;
				}
		    }
		    logger.info("已成功接收消息。。。。。。。。。。。");
		    return "成功";
	    }  
	    @ResponseBody
		@RequestMapping(value = { "/test" }, method = {RequestMethod.GET, RequestMethod.POST })
		public String test(){
		 try {
			 this.receiveMotorist();
			 return "成功";
			 
			 
		} catch (Exception e) {
			return"失敗";
		}
	 }*/
}
