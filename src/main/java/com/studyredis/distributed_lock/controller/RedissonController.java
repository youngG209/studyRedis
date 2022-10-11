package com.studyredis.distributed_lock.controller;

import com.studyredis.account.entity.Account;
import com.studyredis.distributed_lock.service.RedissonLockAccountService;
import com.studyredis.pubsub.publisher.service.impl.RedisMessagePublisher;
import com.studyredis.pubsub.subscribe.service.RedisMessageSubscriber;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/redisson")
@RestController
public class RedissonController {

	private final RedissonLockAccountService redissonLockAccountService;
	// 토픽 목록

	@GetMapping("/out")
	public void outMoney(String accountNumber, int money) {

		redissonLockAccountService.outMoneyByRedissonLock(accountNumber, money);

	}
}
