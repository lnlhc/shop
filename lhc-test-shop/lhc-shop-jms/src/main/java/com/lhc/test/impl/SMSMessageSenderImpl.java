package com.lhc.test.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.lhc.test.IMessageSender;

import javax.jms.MapMessage;

@Component("SMSMessageSenderImpl")
public class SMSMessageSenderImpl implements IMessageSender {

    private  Logger log = Logger.getLogger(SMSMessageSenderImpl.class);
    @Autowired
    private JmsTemplate jms;


    /**
     * 发送短信
     *
     * @param message
     * @return void
     */
    public void sendMessage(MapMessage message) throws Exception {
    	log.info("开始消费短信");
    	while(true){
    		if (message != null) {
        		jms.receive();
        		log.info("接收的消息为:"+ message.getInt("i"));
    		}else{
    			log.info("消息已全部消费完毕");
    		}
    	}
    }
}
