package com.server.be_chatting.exception;


public class ServiceException extends RuntimeException {
    private final int code;
    private final String message;
    private final Object data;

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        if (this.message == null) {
            return super.getMessage();
        } else {
            return message;
        }
    }

    public Object getData() {
        return data;
    }

    private ServiceException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ServiceException of(int code, String message) {
        return new ServiceException(code, message, null);
    }

    public static ServiceException of(int code, String message, Object data) {
        return new ServiceException(code, message, data);
    }

    public static ServiceException ofCode(int code) {
        return of(code, null);
    }
}
