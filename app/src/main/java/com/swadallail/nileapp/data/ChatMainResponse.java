package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatMainResponse<T> {
    @SerializedName("data")
    public List data;
}
