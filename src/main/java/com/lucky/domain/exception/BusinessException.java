package com.lucky.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

	private  Integer code;



	private BusinessException() {
		super();
	}

	private BusinessException(String message) {
		super(message);
	}

	private BusinessException(String message, Integer code) {
		super(message);
		this.code = code;
	}



	public static BusinessException newInstance() {
		return new BusinessException();
	}

	public static BusinessException newInstance(String message) {
		return new BusinessException(message);
	}

	public static BusinessException newInstance(String message, Integer code) {
		return new BusinessException(message, code);
	}


}
