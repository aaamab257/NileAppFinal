package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Main<T> {
    @SerializedName("data")
    public ArrayList<T> data;
}
