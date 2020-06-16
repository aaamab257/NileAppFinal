package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class GetNewOrdersData {
    @SerializedName("img")
    public String img ;
    @SerializedName("description")
    public String description ;
    @SerializedName("hours")
    public int hours ;
    @SerializedName("fromLat")
    public String fromLat ;
    @SerializedName("fromLng")
    public String fromLng ;
    @SerializedName("toLat")
    public String toLat ;
    @SerializedName("toLng")
    public String toLng ;
    @SerializedName("fromAddress")
    public String fromAddress ;
    @SerializedName("toAddress")
    public String toAddress ;
    @SerializedName("editable")
    public Boolean editable ;
    @SerializedName("orderId")
    public int orderId ;
    @SerializedName("ownerName")
    public String ownerName ;
    @SerializedName("ownerId")
    public String ownerId ;
    @SerializedName("represtiveId")
    public String represtiveId ;
    @SerializedName("represtiveName")
    public String represtiveName ;
    @SerializedName("fromDistance")
    public String fromdis ;
    @SerializedName("toDistance")
    public String todis ;

    public String getFromdis() {
        return fromdis;
    }

    public void setFromdis(String fromdis) {
        this.fromdis = fromdis;
    }

    public String getTodis() {
        return todis;
    }

    public void setTodis(String todis) {
        this.todis = todis;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getFromLat() {
        return fromLat;
    }

    public void setFromLat(String fromLat) {
        this.fromLat = fromLat;
    }

    public String getFromLng() {
        return fromLng;
    }

    public void setFromLng(String fromLng) {
        this.fromLng = fromLng;
    }

    public String getToLat() {
        return toLat;
    }

    public void setToLat(String toLat) {
        this.toLat = toLat;
    }

    public String getToLng() {
        return toLng;
    }

    public void setToLng(String toLng) {
        this.toLng = toLng;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
