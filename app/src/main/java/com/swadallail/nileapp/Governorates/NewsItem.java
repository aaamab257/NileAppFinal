package com.swadallail.nileapp.Governorates;

import android.content.Context;
import android.graphics.Bitmap;


public class NewsItem {

    private String headline;
    private String name;
    private String pubDate;
    private double lat;
    private double lng;
    private double space;
    private String Img;
    private Context context;
    private Bitmap pi;
    private  String echeck;
    private  String echeckBox;
    private  String ephone;
    private  int id;
    private String date;
    private  int counteye;
    private String details;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getphone() {
        return ephone;
    }

    public void setphone(String phone) {
        this.ephone = phone;
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