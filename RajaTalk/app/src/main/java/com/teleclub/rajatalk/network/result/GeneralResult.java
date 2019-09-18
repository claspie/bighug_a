package com.teleclub.rajatalk.network.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teleclub.rajatalk.model.ResponseMessage;

import java.util.List;

public class GeneralResult {
    @SerializedName("messages")
    @Expose
    private List<ResponseMessage> messages = null;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("result")
    @Expose
    private String result;

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
}
