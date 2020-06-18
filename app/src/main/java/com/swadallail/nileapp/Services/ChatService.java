package com.swadallail.nileapp.Services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.OnClosedCallback;
import com.swadallail.nileapp.AuthPhone;
import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.api.model.User;
import com.swadallail.nileapp.chatpage.ChatActivity;
import com.swadallail.nileapp.data.CustomMessage;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserMessage;
import com.swadallail.nileapp.data.UserResponse;
import com.swadallail.nileapp.delegete.DelegeteHome;
import com.swadallail.nileapp.helpers.Globals;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.modelviews.MessageViewModel;

import org.json.JSONObject;
import org.reactivestreams.Subscription;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class ChatService extends Service {

    private final IBinder mBinder = new LocalBinder();
    static HubConnection hubConnection;
    Handler handler;
    String token, username;
    static boolean hubStarted = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        onTaskRemoved(intent);
        hubStarted = StartHubConnection();
        if (!hubStarted) {
            ExitWithMessage("Chat Service failed to start!");
        } else if (hubStarted) {
            ExitWithMessage("Done");
        }


        return mBinder;
    }

    public void getToken(Context context, String tokens, String usernames) {
        SharedHelper.putKey(context, "tokens", tokens);
        SharedHelper.putKey(context, "usernames", usernames);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        try {
            Log.e("restart Service", ": restarted");
            startService(restartServiceIntent);
        } catch (Exception e) {
            Log.e("Service", ": Stoped");
        }

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        onTaskRemoved(intent);

        return START_STICKY;
    }

    private void OnNotificationRecevied() {
        hubConnection.on("RecieveNotification", (message) -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    String role = SharedHelper.getKey(getApplicationContext(), "role");
//                    if (role.equals("Representive") && message.equals("new order added")) {
                        biludNotification(message);
//                    } else if (role.equals("WebClient") && message.equals("new offer")) {
//                        biludNotification(message);
//                    }

                }
            });
        }, String.class);
    }

    private void biludNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


// Creating a pending intent and wrapping our intent

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long pattern[] = {0, 800, 200, 300, 400};

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.nileapp)
                .setContentTitle("Nile App")
                .setContentText(message)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(pattern);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
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

        if(hubStarted){
            return true;
        }

        hubConnection =
                HubConnectionBuilder
                        .create("https://test.nileappco.com/chatHub")
                        .withAccessTokenProvider(Single.defer(() -> {
                            return Single.just(SharedHelper.getKey(getApplicationContext(), "token"));
                            //SharedHelper.getKey(getApplicationContext(),"token");
                        })).build();
        new HubTask().execute(hubConnection);
        OnNotificationRecevied();
        OnRecevied();
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
            hubConnection.send("Send", object);
            Log.e("message", "" + object.Message);
            Log.e("ChatID", "" + object.ChatId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnRecevied() {

        hubConnection.on("RecieveChatMessage", (message) -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("Qu", "" + message.content);
//                    message.isMine = message.from.equals(SharedHelper.getKey(getApplicationContext(), "UserName")) ? 1 : 0;
//                    int mine = message.isMine;
                    String op = SharedHelper.getKey(getApplicationContext(), "opened");

                    Globals.Messages.add(message);
                    if (message.isMine == 0 && op.equals("no")) {
                        biludNotification("New Message");
                    }
                    sendBroadcast(new Intent().setAction("notifyAdapter"));
                }
            });
        }, MessageViewModel.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
    }

}
