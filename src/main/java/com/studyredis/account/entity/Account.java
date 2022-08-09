package com.studyredis.account.entity;

import com.studyredis.account.exception.NotEnoughMoneyException;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Account implements Serializable {
//	redis에 직렬화하여 저장할 수 있도록 Serializable 을 상속했음

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	private String accountNumber;

	private int total;

	public Account(String accountNumber, int total) {
		this.accountNumber = accountNumber;
		this.total = total;
	}
	public void inMoney(int in) {
		this.total += in;
	}

	public void outMoney(int out) {
		int remaining = this.total - out;

		if (remaining < 0) {
			throw new NotEnoughMoneyException("잔고가 부족합니다.");
		}
		this.total = remaining;
	}
}
