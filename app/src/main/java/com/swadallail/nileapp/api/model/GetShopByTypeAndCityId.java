package com.swadallail.nileapp.api.model;

import com.google.gson.annotations.SerializedName;

public class GetShopByTypeAndCityId {


    private int shopId;
    private String shopName;
    private String mobileNo;
    private double lat;
    private double lng;
    private String shopTypeName;
    private String userId;
    private Boolean chatEnable;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getChatEnable() {
        return chatEnable;
    }

    public void setChatEnable(Boolean chatEnable) {
        this.chatEnable = chatEnable;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }





    }