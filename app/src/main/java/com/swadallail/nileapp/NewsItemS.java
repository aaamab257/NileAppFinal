package com.swadallail.nileapp;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;


public class NewsItemS {

    private String shopeType;
    private String shopeName;
    private String pubDate;
    private LatLng latlng;
    private  String phone;
    private double lat;
    private double lng;
    private double space;
    private String Img;
    private Context context;
    private Bitmap pi;
    private  String echeck;
    private  String echeckBox;
    private  int id;
    private String date;
    private  int counteye;
    private String details;
    private  Boolean chatenable;
    private String userId;

    public Boolean getChatenable() {
        return chatenable;
    }

    public void setChatenable(Boolean chatenable) {
        this.chatenable = chatenable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopeType() {
        return shopeType;
    }

    public void setShopeType(String shopeType) {
        this.shopeType = shopeType;
    }

    public String getShopeName() {
        return shopeName;
    }

    public void setShopeName(String shopeName) {
        this.shopeName = shopeName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public LatLng getLatLng() {
        return latlng;
    }

    public void setLatLng(LatLng latlng) {
        this.latlng = latlng;
    }


    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
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

    public String getImg()  { return Img; }
    public Bitmap getImgg()  { return pi; }

    public void setImg(String Img) {
        this.Img = Img;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public String getEcheck() {
        return echeck;
    }

    public void setEcheck(String echeck) {
        this.echeck = echeck;
    }

    public String getEcheckBox() {
        return echeckBox;
    }

    public void setEcheckBox(String echeckBox) {
        this.echeckBox = echeckBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int idd) {
        this.id = idd;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


    public int getCountEye() {
        return counteye;
    }

    public void setCountEye(int counteye) {
        this.counteye = counteye;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }












}