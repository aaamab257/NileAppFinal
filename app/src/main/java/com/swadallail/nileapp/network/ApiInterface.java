package com.swadallail.nileapp.network;

import com.swadallail.nileapp.api.model.RegisterUser;
import com.swadallail.nileapp.api.model.ResultRegisterUser;
import com.swadallail.nileapp.data.ChatMainResponse;
import com.swadallail.nileapp.data.ChatResponse;
import com.swadallail.nileapp.data.ChatUsersResponse;
import com.swadallail.nileapp.data.FacebookToken;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.MessageBody;
import com.swadallail.nileapp.data.MessageResponse;
import com.swadallail.nileapp.data.OrderBody;
import com.swadallail.nileapp.data.OrderResponse;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserLogin;
import com.swadallail.nileapp.data.UserRegister;
import com.swadallail.nileapp.data.UserRegisterResponse;
import com.swadallail.nileapp.data.UserResponse;
import com.swadallail.nileapp.modelviews.MessageViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("RegisterUser")
    Call<MainResponse<UserRegisterResponse>> RegisterUser(@Body UserRegister userRegister);
    @POST("Login")
    Call<MainResponse<UserResponse<UserDataResponse>>> UserLoginFun(@Body UserLogin userLogin);
    @POST("FacebookLogin")
    Call<MainResponse<UserResponse<UserDataResponse>>> UserLoginwithtoken(@Body FacebookToken facebookToken);
    @POST("Order/NewOrder")
    Call<MainResponse<OrderResponse>> UploadOrder(@Header("Authorization") String token , @Body OrderBody orderBody);
    @GET("Chat/GetChats")
    Call<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> UserChats(@Header("Authorization") String token);
    @POST("Chat/OpenChat")
    Call<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> GetChatMessages(@Header("Authorization") String token , @Body MessageBody messageBody);
}
