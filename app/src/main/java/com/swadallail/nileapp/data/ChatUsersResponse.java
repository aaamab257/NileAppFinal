package com.swadallail.nileapp.data;

import com.google.gson.annotations.SerializedName;

public class ChatUsersResponse {
    @SerializedName("userId")
    public String userId ;
    @SerializedName("userName")
    public String userName ;
    @SerializedName("chatId")
    public int chatId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
