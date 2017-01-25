package com.wolf.exceptionresolver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Description:
 * <br/> Created on 2016/12/2 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
public class MyException extends RuntimeException{

	public MyException() {
		super();
	}

	public MyException(String message) {
		super(message);
	}

	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyException(Throwable cause) {
		super(cause);
	}

	protected MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
