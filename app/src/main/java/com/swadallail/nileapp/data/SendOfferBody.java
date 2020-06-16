package com.swadallail.nileapp.data;

public class SendOfferBody {
    public int orderId ;
    public int amount ;

    public SendOfferBody(int orderId, int amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
