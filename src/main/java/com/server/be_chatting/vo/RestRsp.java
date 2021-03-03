package com.server.be_chatting.vo;



import com.server.be_chatting.constant.ErrorCode;

import lombok.Data;


@Data
public class RestRsp<T> {

    private int status;
    private String message;
    private T data;

    public RestRsp() {
    }

    public RestRsp(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> RestRsp<T> of(T data) {
        RestRsp<T> rsp = new RestRsp<>();
        rsp.status = ErrorCode.SUCCESS;
        rsp.data = data;
        return rsp;
    }

    public static <T> RestRsp<T> success() {
        RestRsp<T> rsp = new RestRsp<>();
        rsp.status = ErrorCode.SUCCESS;
        rsp.message = "success";
        return rsp;
    }

    public static <T> RestRsp<T> success(T data) {
        RestRsp<T> rsp = new RestRsp<>();
        rsp.status = ErrorCode.SUCCESS;
        rsp.data = data;
        rsp.message = "success";
        return rsp;
    }

    public static <T> RestRsp<T> fail() {
        RestRsp<T> rsp = new RestRsp<>();
        rsp.status = ErrorCode.SERVER_ERROR;
        return rsp;
    }

    public static <T> RestRsp<T> fail(int status, String msg) {
        RestRsp<T> rsp = new RestRsp<>();
        rsp.status = status;
        rsp.message = msg;
        return rsp;
    }

    public static <T> RestRsp<T> fail(String msg) {
        return fail(ErrorCode.SERVER_ERROR, msg);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
