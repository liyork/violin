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
public class UpdateResponseStatusException extends RuntimeException{

	public UpdateResponseStatusException() {
		super();
	}

	public UpdateResponseStatusException(String message) {
		super(message);
	}

	public UpdateResponseStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateResponseStatusException(Throwable cause) {
		super(cause);
	}

	protected UpdateResponseStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
