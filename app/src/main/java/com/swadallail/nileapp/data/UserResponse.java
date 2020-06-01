package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class UserResponse<T> {
    @SerializedName("user")
    public T user;
    @SerializedName("token")
    public String token;
    @SerializedName("refreshToken")
    public String refreshToken;
    @SerializedName("success")
    public Boolean ssuccess;
}
