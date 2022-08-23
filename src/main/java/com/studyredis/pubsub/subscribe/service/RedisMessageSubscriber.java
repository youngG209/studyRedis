package com.studyredis.pubsub.subscribe.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

	public static List<String> messageList = new ArrayList<String>();

	@Override
	public void onMessage(Message message, byte[] pattern) {
		messageList.add(message.toString());

		log.info("message : {}", message.toString());
	}
}
