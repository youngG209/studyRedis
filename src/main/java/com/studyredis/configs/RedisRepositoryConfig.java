package com.studyredis.configs;

import com.studyredis.pubsub.publisher.service.MessagePublisher;
import com.studyredis.pubsub.publisher.service.impl.RedisMessagePublisher;
import com.studyredis.pubsub.subscribe.service.RedisMessageSubscriber;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {

	private final RedisProperties redisProperties;

	private static final int DEFAULT_EXPIRE_SECONDS = 10;

	private static final String LIST = "list";
	private static final int LIST_EXPIRE_SECONDS = 20;

//	RedisConnectionFactory 인터페이스를 통해 LettuceConnectionFactory를 생성하여 반환
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(
			redisProperties.getHost(),
			redisProperties.getPort()
		);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());
//		setKeySerializer, setValueSerializer 설정해주는 이유는 RedisTemplate를 사용할 때 Spring - Redis 간 데이터 직렬화, 역직렬화 시 사용하는 방식이 Jdk 직렬화 방식이기 때문
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean(name = "cacheManager")
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.disableCachingNullValues()
			.entryTtl(Duration.ofSeconds(DEFAULT_EXPIRE_SECONDS))
			.computePrefixWith(CacheKeyPrefix.simple())
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		// List
		cacheConfigurations.put(LIST, RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofSeconds(LIST_EXPIRE_SECONDS)));

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
			.cacheDefaults(configuration)
			.withInitialCacheConfigurations(cacheConfigurations).build();
	}

	@Bean
	MessageListenerAdapter messageListenerAdapter() {
		return new MessageListenerAdapter(new RedisMessageSubscriber());
	}

	@Bean
	RedisMessageListenerContainer redisContainer() {
	    RedisMessageListenerContainer container
	      = new RedisMessageListenerContainer();
	    container.setConnectionFactory(redisConnectionFactory());
	    container.addMessageListener(messageListenerAdapter(), topic());
	    return container;
	}

	@Bean
	MessagePublisher redisPublisher() {
	    return new RedisMessagePublisher(redisTemplate(), topic());
	}

	@Bean
	ChannelTopic topic() {
	    return new ChannelTopic("messageQueue");
	}

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.setCodec(new StringCodec());
		String redisUrl = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
		config.useSingleServer()
			.setAddress(
				redisUrl);
		System.out.println("create redisUrl : " + redisUrl);
		return Redisson.create(config);
	}
}
