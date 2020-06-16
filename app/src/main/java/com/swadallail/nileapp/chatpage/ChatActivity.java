package com.swadallail.nileapp.chatpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.Services.ChatService;
import com.swadallail.nileapp.adapters.MessageAdapter;
import com.swadallail.nileapp.data.ChatResponse;
import com.swadallail.nileapp.data.CustomMessage;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.MessageBody;
import com.swadallail.nileapp.data.MessageResponse;
import com.swadallail.nileapp.helpers.Globals;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.modelviews.MessageViewModel;
import com.swadallail.nileapp.network.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    ChatService chatService;
    boolean mBound = false;
    MyReceiver myReceiver;
    MessageAdapter adapter;
    ProgressDialog dialog;
    int finalchatid;
    String chatname;
    String user_id;
    GridView gridView;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Intent inGet = getIntent();
        user_id = inGet.getStringExtra("shopId");
        finalchatid = inGet.getIntExtra("chatId", 0);
        SharedHelper.putKey(ChatActivity.this, "opened", "true");
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        //intentFilter.addAction("Send");
        intentFilter.addAction("notifyAdapter");
        registerReceiver(myReceiver, intentFilter);
        Log.e("userid", user_id + "");
        Log.e("chatid", finalchatid + "");
        getChatMessages(user_id, finalchatid);
        gridView = (GridView) findViewById(R.id.gvMessages);
        gridView.setTranscriptMode(GridView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        ImageButton sendButton = findViewById(R.id.bSend);
        final EditText editText = (EditText) findViewById(R.id.etMessageText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "رجاء قم بكتابة الرسالة", Toast.LENGTH_SHORT).show();
                } else {
                    editText.setText("");
                    CustomMessage object = new CustomMessage();
                    object.Message = message;
                    object.ChatId = finalchatid;
                    chatService.Send(object);
                }


                /*arrayAdapter.add(message);
                arrayAdapter.notifyDataSetChanged();*/

            }
        });

//

    }

    private void getChatMessages(String user_id, int chatId) {
        dialog = new ProgressDialog(ChatActivity.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        MessageBody body = new MessageBody(chatId, user_id);
        Log.e("Body", "" + chatId);
        //String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYUBhLmNvbSIsInN1YiI6ImFAYS5jb20iLCJqdGkiOiJhZjFkMTNkMC00NzViLTQ2OWMtOWEwYi01YmU5NzE5YWJjMTciLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjAyN2Y2MWJiLWFlNzUtNGE2Yy04YWY4LWE5MDcyNmFlM2IyOCIsIlVzZXJJZCI6IjAyN2Y2MWJiLWFlNzUtNGE2Yy04YWY4LWE5MDcyNmFlM2IyOCIsImV4cCI6MTU5MjMyOTcxMywiaXNzIjoiUmF6TmV0LmNvbSIsImF1ZCI6IlJhek5ldC5jb20ifQ.ZoImLshfPppUM7qrURMeIBV-61YNeWAqF9Ge1lQ4N0w";
        String token = "Bearer " + SharedHelper.getKey(ChatActivity.this, "token");
        Call<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> call = userclient.GetChatMessages(token, body);
        call.enqueue(new Callback<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>>() {
            @Override
            public void onResponse(Call<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> call, Response<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> response) {
                if (response.body() != null) {

                    finalchatid = response.body().data.chatId;
                    //chatname = response.body().data.messages.get(0).from;
                    if (response.body().data.messages.size() > 0) {
                        Globals.Messages = response.body().data.messages;
                    } else {
                        Toast.makeText(ChatActivity.this, "لا يوجد محادثة من قبل", Toast.LENGTH_SHORT).show();
                        adapter = new MessageAdapter(ChatActivity.this, Globals.Messages);
                        gridView.setAdapter(adapter);
                    }
                }


                for (int i = 0; i < response.body().data.messages.size(); i++) {
                    if (response.body().data.messages.get(i).isMine == 1) {
                        adapter = new MessageAdapter(ChatActivity.this, Globals.Messages, 1);
                        gridView.setAdapter(adapter);
                    } else {
                        adapter = new MessageAdapter(ChatActivity.this, Globals.Messages, 0);
                        gridView.setAdapter(adapter);
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> call, Throwable t) {
                Log.e("BodyE", "" + t);
            }
        });
    }

    @Override
    protected void onStop() {

        if (mBound) {
            unbindService(mConnection);
            unregisterReceiver(myReceiver);
            mBound = false;
        }
        super.onStop();

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "notifyAdapter":
                    adapter.notifyDataSetChanged();
                    break;
            }

        }

    }


    @Override
    protected void onDestroy() {
        SharedHelper.putKey(ChatActivity.this, "opened", "no");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedHelper.putKey(ChatActivity.this, "opened", "no");
        super.onPause();
    }
}
