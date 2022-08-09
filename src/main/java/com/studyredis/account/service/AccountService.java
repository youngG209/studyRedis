package com.studyredis.account.service;

import com.studyredis.account.entity.Account;
import com.studyredis.account.entity.repository.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public Account createAccount(Account account) {
		return accountRepository.save(account);
	}

	public int nowTotalAccount(String accountNumber) {
		return accountRepository.findTotalByAccountNumber(accountNumber);
	}

	public List<Account> getAll() {
		return accountRepository.findAll();
	}

	public Account getByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Transactional
	public int inMoney(String accountNumber, int money) {
		Account account = accountRepository.findByAccountNumberForPessimisticLock(accountNumber);
		if (account != null) {
			account.inMoney(money);
			log.info("결과 잔고 : {}", account);
			return account.getTotal();
		}
		account = accountRepository.save(new Account(accountNumber, money));
		log.info("결과 잔고 : {}", account);
		return account.getTotal();
	}

	@Transactional
	public Account outMoney(String accountNumber, int money) {
		Account account = accountRepository.findByAccountNumberForPessimisticLock(accountNumber);
		account.outMoney(money);
		log.info("결과 잔고 : {}", account);
		return account;
	}

	@Transactional
	public int outMoneyByNoLock(String accountNumber, int money) {
		Account account = accountRepository.findByAccountNumber(accountNumber);
		account.outMoney(money);
		log.info("결과 잔고 : {}", account);
		return account.getTotal();
	}

	@Transactional
	public void removeAccount(String accountNumber) {
		Account account = accountRepository.findByAccountNumberForPessimisticLock(accountNumber);
		accountRepository.delete(account);
	}

}
