package com.shyfay.admin.common.base;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private CodeMessage meta;
    private T data;

    public ResponseResult(T data) {
        this(ExceptionCode.SUCCESS.getCode(), ExceptionCode.SUCCESS.getMessage(), data);
    }

    public ResponseResult() {
        this.meta = new CodeMessage();
    }

    public void setMeta(CodeMessage meta) {
        this.meta = meta;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this(code, message, null);
    }

    public ResponseResult(Integer code, String message, T data) {
        this.meta = new CodeMessage();
        this.meta.setCode(code);
        this.meta.setMessage(message);
        this.data = data;
    }

    public ResponseResult(CodeMessage codeMessage) {
        this(codeMessage.getCode(), codeMessage.getMessage(), null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult(data);
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return new ResponseResult(code, message);
    }

    public T getData() {
        return this.data;
    }

    public CodeMessage getMeta() {
        return this.meta;
    }

    public boolean checkSuccess() {
        return null != this.meta && ExceptionCode.SUCCESS.getCode().equals(this.meta.getCode());
    }
}
