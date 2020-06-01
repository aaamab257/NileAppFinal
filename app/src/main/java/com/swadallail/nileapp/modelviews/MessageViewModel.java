package com.swadallail.nileapp.modelviews;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageViewModel{
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
