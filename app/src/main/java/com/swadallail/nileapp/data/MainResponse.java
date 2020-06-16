package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class MainResponse<T> {
    @SerializedName("data")
    public T data;
    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("statusCode")
    public int statusCode;
    @SerializedName("errors")
    public String errors;
}
