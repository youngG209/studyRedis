package com.studyredis.account.exception;

public class NotEnoughMoneyException extends RuntimeException {

	public NotEnoughMoneyException(String message) {
		super(message);
	}
}
