package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class MessageResponse<T> {
    @SerializedName("content")
    public String content;
    @SerializedName("from")
    public String from;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("isMine")
    public int isMine;
    @SerializedName("chatId")
    public int chatId ;
    @SerializedName("senderId")
    public String senderId ;
}
