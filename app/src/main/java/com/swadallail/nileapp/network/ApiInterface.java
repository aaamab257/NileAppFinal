package com.swadallail.nileapp.network;

import androidx.media.VolumeProviderCompat;

import com.swadallail.nileapp.api.model.RegisterUser;
import com.swadallail.nileapp.api.model.ResultRegisterUser;
import com.swadallail.nileapp.data.AcceptBody;
import com.swadallail.nileapp.data.AcceptRes;
import com.swadallail.nileapp.data.ChatMainResponse;
import com.swadallail.nileapp.data.ChatResponse;
import com.swadallail.nileapp.data.ChatUsersResponse;
import com.swadallail.nileapp.data.FacebookToken;
import com.swadallail.nileapp.data.GetNewOrdersData;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.LocationBody;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.MessageBody;
import com.swadallail.nileapp.data.MessageResponse;
import com.swadallail.nileapp.data.OfferResponse;
import com.swadallail.nileapp.data.OrderBody;
import com.swadallail.nileapp.data.OrderResponse;
import com.swadallail.nileapp.data.PickedBody;
import com.swadallail.nileapp.data.RateBody;
import com.swadallail.nileapp.data.RequestRepreBody;
import com.swadallail.nileapp.data.SendOfferBody;
import com.swadallail.nileapp.data.SendOfferRes;
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
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("RegisterUser")
    Call<MainResponse<UserRegisterResponse>> RegisterUser(@Body UserRegister userRegister);
    @POST("Login")
    Call<MainResponse<UserResponse<UserDataResponse>>> UserLoginFun(@Body UserLogin userLogin );
    @POST("FacebookLogin")
    Call<MainResponse<UserResponse<UserDataResponse>>> UserLoginwithtoken(@Body FacebookToken facebookToken);
    @POST("Order/NewOrder")
    Call<MainResponse<OrderResponse>> UploadOrder(@Header("Authorization") String token , @Body OrderBody orderBody);
    @GET("Chat/GetChats")
    Call<MainResponse<ArrayList<ChatResponse<ChatUsersResponse>>>> UserChats(@Header("Authorization") String token);
    @POST("Chat/OpenChat")
    Call<MainResponse<ChatResponse<ArrayList<MessageViewModel>>>> GetChatMessages(@Header("Authorization") String token , @Body MessageBody messageBody);
    @GET("Order/GetOrders")
    Call<Main<GetOrdersRes>> GetOrders(@Header("Authorization") String token );
    @GET("Order/GetNewOrders")
    Call<Main<GetNewOrdersData>> GetNewOrders(@Header("Authorization") String token , @Query("lat") double lat , @Query("lng") double lng  );
    @POST("Order/SendOffer")
    Call<MainResponse<SendOfferRes>> SendOffer(@Header("Authorization") String token , @Body SendOfferBody body);
    @GET("Order/GetOrderOffers")
    Call<Main<OfferResponse>> GetOffers(@Header("Authorization") String token , @Query("OrderId") int order);
    @POST("Order/AcceptOffer")
    Call<MainResponse<AcceptRes>> Accept(@Header("Authorization") String token , @Body AcceptBody body);
    @POST("Order/OrderPicked")
    Call<MainResponse> orderPicked(@Header("Authorization") String token , @Body PickedBody body);
    @POST("Order/OrderDelivered")
    Call<MainResponse> orderDelivered(@Header("Authorization") String token , @Body PickedBody body);
    @POST("Order/RateUser")
    Call<MainResponse> rateUser(@Header("Authorization") String token , @Body RateBody body);
    @POST("Representive/RequestRepresentive")
    Call<MainResponse> requestRepre(@Header("Authorization") String token , @Body RequestRepreBody body);
    @POST("Representive/CurrentLocation")
    Call<MainResponse> CurrentLocation(@Header("Authorization") String token , @Body LocationBody body);
    @GET("User/ConfirmPhone")
    Call<MainResponse> ConfirmPhone(@Header("Authorization") String token );

}
