package com.studyredis.distributed_lock.service;

import com.studyredis.account.exception.NotEnoughMoneyException;
import com.studyredis.account.service.AccountService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockAccountService {

	private final RedissonClient redissonClient;

	private final AccountService accountService;

	private static final String SEAT_LOCK = "seat_lock";

	public int outMoneyByRedissonLock(String accountNumber, int money) {
		System.out.println("시작");
		RLock rLock = redissonClient.getLock(SEAT_LOCK+":lock");
		String thread = Thread.currentThread().getName();

		try {
			boolean available = rLock.tryLock(1, 3, TimeUnit.SECONDS);

			if (!available) {
				log.info("[{}] Lock 획득 실패", thread);
				throw new InterruptedException();
			}
			int currentMoney = Integer.parseInt((String) redissonClient.getBucket(SEAT_LOCK).get());
			if (currentMoney < 1) {
				log.info("[{}] 현재 잔액가 없습니다. ({}개)", thread, currentMoney);
				throw new NotEnoughMoneyException("현재 잔액가 없습니다.");
			}

			log.info("[{}] 현재 잔액 : {}개", thread, currentMoney);
//			int remainder = accountService.outMoneyByNoLock(accountNumber, money);
			redissonClient.getBucket(SEAT_LOCK).set(currentMoney - money);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rLock.unlock();
		}
		return Integer.parseInt((String) redissonClient.getBucket(SEAT_LOCK).get());
	}

}
