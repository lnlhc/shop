package com.lhc.test;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 消息接收器
 * 
 * @author lijw
 *
 */
public class MessageReceiver implements MessageListener {
	
	private static Logger log = Logger.getLogger(MessageReceiver.class);
	
	@Autowired
	private MessageFactory factory;
	
	
	// 接收消息
	@SuppressWarnings("all")
	public void onMessage(final Message message) {
		System.out.println(message);;
		if (message instanceof MapMessage) {
			MapMessage map = (MapMessage) message;
			try {
				processMessage(map);
			} catch (Exception e) {
				log.error("jsm消息监听异常!",e);
			}finally {
				try {
					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 消息监听处理流程
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void processMessage(MapMessage message) throws Exception {
		log.info(new Date() + "收到的内容"+ message);
		IMessageSender sender = this.factory.getMessageSender(message.getString(MessageConstant.MessageType));
		
		if(null == sender){
			throw new RuntimeException("Not MessageType Support");
		}
		
		try{
			sender.sendMessage(message);
		}catch(Exception ex){
			log.error("消息监听流程异常----com.xinhe99.jms.MessageReceiver!",ex);
			throw new RuntimeException("Message SEND ERROR !");
		}
	}
}
