package com.shyfay.admin.common.base;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CodeMessage {
    private Integer code;
    private String message;

    public CodeMessage() {
    }

    public CodeMessage(Integer code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(String paramValue) {
        return StringUtils.replacePattern(this.message, "\\$\\{.*\\}", paramValue);
    }

    public String getMessage(Map<String, String> paramValues) {
        String msg = this.message;

        Entry entry;
        for(Iterator var4 = paramValues.entrySet().iterator(); var4.hasNext(); msg = StringUtils.replacePattern(msg, "\\$\\{" + (String)entry.getKey() + "\\}", (String)entry.getValue())) {
            entry = (Entry)var4.next();
        }

        return msg;
    }

    public String toString() {
        return "code = " + this.code + "，message = " + this.message;
    }
}
