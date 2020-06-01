package com.swadallail.nileapp.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResultGetShopByTypeAndCityId {

    private List<GetShopByTypeAndCityId> data;



    public void setGetShopByTypeAndCityId(List<GetShopByTypeAndCityId> data){

        this.data = data;
    }


    public  List<GetShopByTypeAndCityId> getGetShopByTypeAndCityId(){

        return data;
    }



}
