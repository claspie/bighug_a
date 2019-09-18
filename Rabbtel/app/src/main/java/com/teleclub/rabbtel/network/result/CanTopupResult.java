package com.teleclub.rabbtel.network.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teleclub.rabbtel.model.ResponseMessage;

import java.util.List;

public class CanTopupResult {
    @SerializedName("messages")
    @Expose
    private List<ResponseMessage> messages = null;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("can")
    @Expose
    private String can;

    public List<ResponseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ResponseMessage> messages) {
        this.messages = messages;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }
}
