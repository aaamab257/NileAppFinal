package com.swadallail.nileapp.api.model;

public class Customers {
private  String userId;
private  String email;
private  String userName;
private  String token;


public String getuserId(){
    return  userId;
}

public void setuserId(String userId){
    this.userId = userId;
}



    public String getUserName(){
        return  userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }


    public String getEmail(){
        return  email;
    }

    public void setEmail(String email){
        this.email = email;
    }


    public String getToken(){
        return  token;
    }

    public void setToken(String token){
        this.token = token;
    }


}
