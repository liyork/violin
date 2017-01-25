package com.wolf.exceptionresolver;

/**
 * Description:
 * <br/> Created on 2016/12/2 9:33
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SimpleException extends RuntimeException{

	public SimpleException() {
		super();
	}

	public SimpleException(String message) {
		super(message);
	}

	public SimpleException(String message, Throwable cause) {
		super(message, cause);
	}

	public SimpleException(Throwable cause) {
		super(cause);
	}

	protected SimpleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
