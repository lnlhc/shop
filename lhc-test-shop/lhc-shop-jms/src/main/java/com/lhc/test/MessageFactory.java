package com.lhc.test;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


/**
 * 信息发送器的工厂,根据不同消息类型，创建不同的发送器
 * 
 * @author lijw
 *
 */
@Component
public class MessageFactory {
	
	
	@Resource(name="SMSMessageSenderImpl")
	private IMessageSender sms = null ;
	

	
	/**
	 * 获取单例的发送类
	 * @param tyep
	 * @return
	 */
	public IMessageSender getMessageSender(String tyep) {
		if(MessageConstant.EmailMessage.equals(tyep)){
			return null ;
		}else if(MessageConstant.SMSMessage.equals(tyep)){
			return this.sms ;
		}else if(MessageConstant.VoiceSMSMessage.equals(tyep)){
			return null ;
		}else{
			return null;
		}
	}
}
