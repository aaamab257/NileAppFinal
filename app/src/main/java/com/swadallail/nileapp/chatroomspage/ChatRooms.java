package com.swadallail.nileapp.chatroomspage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.adapters.RoomsAdapter;
import com.swadallail.nileapp.chatpage.ChatActivity;
import com.swadallail.nileapp.data.ChatMainResponse;
import com.swadallail.nileapp.data.ChatResponse;
import com.swadallail.nileapp.data.ChatUsersResponse;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.databinding.ActivityChatRoomsBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatRooms extends AppCompatActivity {
    ActivityChatRoomsBinding binding ;
    MyHandlers handlers ;
    ProgressDialog dialog;
    RoomsAdapter adapter ;
    ArrayList<ChatUsersResponse> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_chat_rooms);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("المحادثات");
        handlers = new MyHandlers(this);
        binding.setHandlers(handlers);
        list = new ArrayList<>();
        getChats();
    }

    private void getChats() {
        String url = "https://test.nileappco.com/api/";
        dialog = new ProgressDialog(ChatRooms.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        String token = "Bearer "+ SharedHelper.getKey(ChatRooms.this , "token");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userChats = retrofit.create(ApiInterface.class);
        Call<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> call = userChats.UserChats(token);
        call.enqueue(new Callback<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>>() {
            @Override
            public void onResponse(Call<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> call, Response<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> response) {
                Log.e("Response" , ""+response.body().data.size());
                /*for (int i = 0 ; i < response.body().data.size() ; i++){
                    //Log.e("INTID" , ""+response.body().data.get(i).users.get(i).chatId);
                }*/
                binding.recChats.setLayoutManager(new LinearLayoutManager(ChatRooms.this));
                adapter = new RoomsAdapter(ChatRooms.this ,  response.body().data);
                binding.recChats.setAdapter(adapter);
                /*for (int i = 0 ; i < response.body().data.size() ; i++){

                }*/



                //adapter = new RoomsAdapter(ChatRooms.this ,  response.body().data);

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> call, Throwable t) {
                Log.e("ResponseError" , ""+t);
                dialog.dismiss();
            }
        });
        //
    }

    public class MyHandlers{
        Context co ;
        public MyHandlers(Context context){
            context = this.co;
        }

    }
}
