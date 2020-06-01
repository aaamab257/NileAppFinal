package com.swadallail.nileapp.api.model;

import java.util.List;

public class ResultModels {

    private List<Customers> data;

    public  List<Customers> getCustomers(){

        return data;
    }


    public void setCustomers(List<Customers> data){

        this.data = data;
    }

}
