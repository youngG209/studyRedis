package com.studyredis.pubsub.publisher.service;

import org.springframework.data.redis.listener.ChannelTopic;

public interface MessagePublisher {

	void publisher(String message);

	void publisher(ChannelTopic channel, String message);
}
