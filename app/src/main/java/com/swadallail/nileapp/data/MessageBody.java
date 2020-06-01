package com.swadallail.nileapp.data;

public class MessageBody {
    public int chatId ;
    public String userId ;

    public MessageBody(int chatId, String userId) {
        this.chatId = chatId;
        this.userId = userId;
    }
}
