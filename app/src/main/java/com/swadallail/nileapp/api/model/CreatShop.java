package com.swadallail.nileapp.api.model;

public class CreatShop {
    private String  shopName;
    private String ownerName;
    private String mobileNo;
    private Double lat;
    private  Double lng;
    private  int shopTypeId;
    private  int cityId;


public CreatShop(String shopName, String ownerName,String mobileNo,double lat,double lng,int shopTypeId,int CityId){
    this.shopName = shopName;
    this.ownerName = ownerName;
    this.mobileNo = mobileNo;
    this.lat = lat;
    this.lng = lng;
    this.shopTypeId = shopTypeId;
    this.cityId = CityId;


}
}