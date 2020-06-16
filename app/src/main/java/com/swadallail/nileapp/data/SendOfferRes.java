package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class SendOfferRes {
    @SerializedName("offerId")
    public String offerId ;
    @SerializedName("amount")
    public String amount ;
    @SerializedName("commission")
    public String commission ;
    @SerializedName("total")
    public String total ;
    @SerializedName("orderId")
    public String orderId ;
    @SerializedName("ownerId")
    public String ownerId ;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
