package com.swadallail.nileapp.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.swadallail.nileapp.Services.ChatService;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;

public class SingletonHelper {
    private static SingletonHelper INSTANCE = null;

    // other instance variables can be here
    private SingletonHelper() {
    }



    public static synchronized SingletonHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonHelper();
        }
        return (INSTANCE);
    }
    // other instance methods can follow

}