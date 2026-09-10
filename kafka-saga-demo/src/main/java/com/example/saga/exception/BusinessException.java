package com.example.saga.exception;

public class BusinessException extends RuntimeException {

	public BusinessException(String message) {

		super(message);
	}
}