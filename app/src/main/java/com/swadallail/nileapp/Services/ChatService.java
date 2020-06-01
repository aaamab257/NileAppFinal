package com.swadallail.nileapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.swadallail.nileapp.api.model.User;
import com.swadallail.nileapp.chatpage.ChatActivity;
import com.swadallail.nileapp.data.CustomMessage;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserMessage;
import com.swadallail.nileapp.helpers.Globals;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.modelviews.MessageViewModel;

import org.json.JSONObject;
import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class ChatService extends Service {

    private final IBinder mBinder = new LocalBinder();
    HubConnection hubConnection;
    Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!StartHubConnection()) {
            ExitWithMessage("Chat Service failed to start!");
        } else if (StartHubConnection()) {
            ExitWithMessage("Done");
        }

        OnRecevied();
        return mBinder;
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        super.onCreate();
    }

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }

    }

    private boolean StartHubConnection() {
        hubConnection =
                HubConnectionBuilder
                        .create("http://nileapp-001-site3.itempurl.com/chatHub")
                        .withAccessTokenProvider(Single.defer(() -> {
                            return Single.just(SharedHelper.getKey(getApplicationContext(),"token"));
                            //SharedHelper.getKey(getApplicationContext(),"token");
                        })).build();
        new HubTask().execute(hubConnection);


        return true;
    }

    public static class HubTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnection.start().blockingAwait();//.andThen(()-> Log.e("",""));
            String conn = hubConnection.getConnectionId();
            Log.e("Conn", "" + conn);
            return null;
        }
    }

    private void ExitWithMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 3000);
    }

    public void Send(CustomMessage object) {
        try {
            hubConnection.send("Send", object );
            Log.e("message",""+object.Message);
            Log.e("ChatID",""+object.ChatId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnRecevied() {
        hubConnection.on("RecieveChatMessage", (message) -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("Qu",""+message.content);
                    message.isMine = message.from.equals(SharedHelper.getKey(getApplicationContext(),"UserName")) ? 1 : 0;
                    Globals.Messages.add(message);
                    sendBroadcast(new Intent().setAction("notifyAdapter"));

                }
            });
        }, MessageViewModel.class);
        /*hubConnection.on("Send", (message) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent("Send");
                    in.putExtra("message",message);
                    sendBroadcast(in);
                }
            });
        }, String.class);*/
    }



}
