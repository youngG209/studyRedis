package com.studyredis.account.controller;

import com.studyredis.account.entity.Account;
import com.studyredis.account.service.AccountService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/create")
	@Cacheable(value = "account", key = "#account.accountNumber")
	public Account createAccount(@RequestBody Account account) {
		log.info("create method call : {}", account);
		return accountService.createAccount(account);
	}

	@GetMapping("/get")
	@Cacheable(value = "account", key = "#accountNumber")
	public Account getAccount(String accountNumber) {
		log.info("getAccount method call");
		return accountService.getByAccountNumber(accountNumber);
	}

	@GetMapping("/getAll")
	@Cacheable(value = "list")
	public List<Account> getAllAccount() {
		log.info("getAllAccount method call");
		return accountService.getAll();
	}

	@PutMapping("/in")
	@CacheEvict(value = "account", key = "#accountNumber")
	public String inMoney(String accountNumber, int money) {
		log.info("inMoney method call");
		int i = accountService.inMoney(accountNumber, money);

		return "잔금 : " + i;
	}

	@PutMapping("/out")
	@CacheEvict(value = "account", key = "#accountNumber")
	public String outMoney(String accountNumber, int money) {
		log.info("outMoney method call");

		Account i = accountService.outMoney(accountNumber, money);

		return "잔금 : " + i;
	}

	@DeleteMapping("/delete")
	@CacheEvict(value = "account", key = "#accountNumber")
	public void removeMoney(String accountNumber) {
		log.info("removeMoney method call");
		accountService.removeAccount(accountNumber);
	}
}
