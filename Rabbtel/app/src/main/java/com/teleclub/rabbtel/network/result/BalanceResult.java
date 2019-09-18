package com.teleclub.rabbtel.network.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teleclub.rabbtel.model.ResponseMessage;

import java.util.List;

public class BalanceResult {
    @SerializedName("messages")
    @Expose
    private List<ResponseMessage> messages = null;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("balance")
    @Expose
    private double balance;

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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
