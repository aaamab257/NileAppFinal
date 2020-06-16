package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class AcceptRes {
    @SerializedName("offerId")
    public int offerId ;
    @SerializedName("amount")
    public int amount ;
    @SerializedName("commission")
    public int commission ;
    @SerializedName("total")
    public int total ;
    @SerializedName("orderId")
    public int orderId ;
    @SerializedName("ownerId")
    public String ownerId ;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
