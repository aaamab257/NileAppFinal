package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class OfferResponse {
    @SerializedName("offerId")
    public int offerId ;
    @SerializedName("total")
    public String total ;
    @SerializedName("orderId")
    public int orderId ;
    @SerializedName("represtiveId")
    public String represtiveId ;
    @SerializedName("represtiveName")
    public String represtiveName ;
    @SerializedName("represtivePhone")
    public String represtivePhone ;
    @SerializedName("rate")
    public String rate ;
    @SerializedName("img")
    public String img ;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getReprestiveId() {
        return represtiveId;
    }

    public void setReprestiveId(String represtiveId) {
        this.represtiveId = represtiveId;
    }

    public String getReprestiveName() {
        return represtiveName;
    }

    public void setReprestiveName(String represtiveName) {
        this.represtiveName = represtiveName;
    }

    public String getReprestivePhone() {
        return represtivePhone;
    }

    public void setReprestivePhone(String represtivePhone) {
        this.represtivePhone = represtivePhone;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
