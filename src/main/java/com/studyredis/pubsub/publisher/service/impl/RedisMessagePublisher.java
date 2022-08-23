package com.studyredis.pubsub.publisher.service.impl;

import com.studyredis.pubsub.publisher.service.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


@Service
public class RedisMessagePublisher implements MessagePublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	private final ChannelTopic topic;

	public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	@Override
	public void publisher(String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}

	@Override
	public void publisher(ChannelTopic channelTopic, String message) {
		redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}


}
