package com.lhc.test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


public class ConsumerController implements MessageListener{
	
	
	


	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
			    System.out.println(((TextMessage) message).getText());
			    message.acknowledge(); //手动确认消息
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
