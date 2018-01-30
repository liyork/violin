package com.wolf.test.base.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 错误示范！！！
 */
public class ErrorExceptionExample extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;

    private String showMessage;

    private Throwable cause;

    public ErrorExceptionExample() {
    }

    public ErrorExceptionExample(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorExceptionExample(Throwable cause) {
        super(cause);
    }

    public ErrorExceptionExample(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ErrorExceptionExample(String errorCode, String errorMessage, String showMessage) {
        super(showMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.showMessage = showMessage;
    }


    public ErrorExceptionExample(String showMessage, Throwable cause) {
        super(showMessage, cause);
        this.showMessage = showMessage;
        this.cause = cause;
    }

    public ErrorExceptionExample(String errorCode, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.cause = cause;
        this.errorMessage = errorMessage;
    }

    public ErrorExceptionExample(String errorCode, String errorMessage, String showMessage, Throwable cause) {
        super(showMessage, cause);
        this.cause = cause;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.showMessage = showMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getShowMessage() {
        if (this.showMessage == null) {
            return super.getMessage();
        }
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        String msg = super.getMessage();
        String causeMsg = null;
        if (this.cause != null) {
            causeMsg = this.cause.getMessage();
        }
        if (msg != null) {
            if (causeMsg != null) {
                return msg + " caused by: " + causeMsg;
            }
            return msg;
        }
        return causeMsg;
    }

    public String getErrorStack() {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bo);
        printStackTrace(ps);
        String errorStack = new String(bo.toByteArray());
        return errorStack;
    }

    //原来这里是最大的坑!!!人家父类是 fillInStackTrace(0);，所以StackTraceDepth深度是0!!!
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        if (this.cause == null) {
            return super.toString();
        }
        return super.toString() + " caused by: " + this.cause.toString();
    }
}
