package com.teleclub.rabbtel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("field")
    @Expose
    private Object field;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("type")
    @Expose
    private String type;

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
