package com.studyredis.distributed_lock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.studyredis.account.entity.repository.AccountRepository;
import com.studyredis.account.service.AccountService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedissonLockAccountServiceTest {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private RedissonLockAccountService redissonLockAccountService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	private final static String ACCOUNT_NUMBER = "12345";
	private final static int TOTAL = 100000;

	private static final String SEAT_LOCK = "seat_lock";

	@BeforeEach
	void setUp() {
		redissonClient.getBucket(SEAT_LOCK).set(TOTAL);
//		accountService.inMoney(ACCOUNT_NUMBER, TOTAL);
	}

//	@AfterEach
//	void tearDown() {
//		accountRepository.deleteAll();
//	}


	@Test
	void 잔금_확인() {
		RLock rLock = redissonClient.getLock(SEAT_LOCK+":lock");
		String thread = Thread.currentThread().getName();

		try {
			boolean available = rLock.tryLock(1, 3, TimeUnit.SECONDS);

			if (!available) {
				System.out.println("Lock 획득 실패 : " + thread);
				throw new InterruptedException();
			}
			int currentMoney = Integer.parseInt((String) redissonClient.getBucket(SEAT_LOCK).get());
			if (currentMoney < 1) {
				System.out.println("[{" + thread + "}] 현재 잔액가 없습니다. : " + currentMoney);
				throw new InterruptedException();
			}

//			int remainder = accountService.outMoneyByNoLock(accountNumber, money);
			redissonClient.getBucket(SEAT_LOCK).set(currentMoney - 1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rLock.unlock();
		}
	}

	@Test
	@DisplayName("CompletableFuture로_100개_동시에_요청")
	public void outMoneyByRedissonLock() {
		int out = 10000;
		int executeLoopCount = 100;
		CompletableFuture<Integer> integerCompletableFuture = new CompletableFuture<>();
		try {
			ExecutorService executor = Executors.newFixedThreadPool(100);

			for (int i = 0; i < executeLoopCount; i++) {
				integerCompletableFuture = CompletableFuture.supplyAsync(
					() -> redissonLockAccountService.outMoneyByRedissonLock(ACCOUNT_NUMBER, out),
					executor);
//					.completeExceptionally(new RuntimeException())
					;
			}
			int getResult = integerCompletableFuture.get();
			int currentMoney = Integer.parseInt((String) redissonClient.getBucket(SEAT_LOCK).get());
			System.out.println("결과값 : " + getResult);
			System.out.println("현재 잔금 : " + currentMoney);
			assertEquals(getResult, currentMoney);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}