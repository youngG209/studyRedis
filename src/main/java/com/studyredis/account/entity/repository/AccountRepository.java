package com.studyredis.account.entity.repository;

import com.studyredis.account.entity.Account;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select h.total from Account h where h.accountNumber = :accountNumber")
	int findTotalByAccountNumber(String accountNumber);

	Account findByAccountNumber(String accountNumber);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select h from Account h where h.accountNumber = :accountNumber")
	Account findByAccountNumberForPessimisticLock(String accountNumber);

}
