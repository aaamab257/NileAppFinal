package com.swadallail.nileapp.api.service;

import com.swadallail.nileapp.api.model.CreatShop;
import com.swadallail.nileapp.api.model.Data;
import com.swadallail.nileapp.api.model.RegisterUser;
import com.swadallail.nileapp.api.model.ResultCheckUser;
import com.swadallail.nileapp.api.model.ResultCities;
import com.swadallail.nileapp.api.model.ResultCreateShop;
import com.swadallail.nileapp.api.model.ResultGetShopByTypeAndCityId;
import com.swadallail.nileapp.api.model.ResultGovernorates;
import com.swadallail.nileapp.api.model.ResultModels;
import com.swadallail.nileapp.api.model.ResultRegisterUser;
import com.swadallail.nileapp.api.model.ResultShopType;
import com.swadallail.nileapp.api.model.User;
import com.swadallail.nileapp.api.model.Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserClient {

    @GET("GetUsers?Page=0&PageSize=10")
    Call<ResultModels> getCustomers();

    @POST("Login")
    Call<Data> Login(@Body Login login);

    @GET("Login")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
    //Call<User> gettToken();

    // Call<ResponseBody> getSecret(@Header("Authorization") String authToken);

    @GET("GetGovernoratesByCounrtyId?CountryId=1")
    Call<ResultGovernorates> getGovernorates();


//    @GET("GetCitiesByGovernroateId?GovernorateId=2")

    @GET("GetCitiesByGovernroateId")
    Call<ResultCities> getCities(@Query("GovernorateId") Integer currentPage);
    // Call<ResultCities> getCities();

    //Call<Track> getTrack(@Path("url") String url,@Path("clientId") String clientId);


    @GET("GetAllShopTypes")
    Call<ResultShopType> geShopType();


    @POST("CreateShopRequest")
    Call<ResultCreateShop> CreatGeovernorate(@Body CreatShop CreateShop);

    @GET("GetShopByTypeAndCityId")
    Call<ResultGetShopByTypeAndCityId> getShopByTypeAndCityId(@Query("ShopTypeId") Integer ShopTypeId, @Query("Lat") double Lat, @Query("Lng") double Lng);
    //GetShopByTypeAndCityId?ShopTypeId=2&Lat=15.0000&Lng=13.44444


    @GET("GetShopByTypeAndCityId")
    Call<ResultGetShopByTypeAndCityId> getShopByTypeAndCityIdFilter(@Query("Lat") double Lat, @Query("Lng") double Lng, @Query("Filter") String ShopTypeId);


    @GET("CheckUser")
    Call<ResultCheckUser> getCheckUser(@Query("MobileNo") String MobileNo);
    //Call<ResultCheckUser> getCheckUser(@Query("mobileNo") String  mobileNo);


    @POST("RegisterUser")
    Call<ResultRegisterUser> RegisterUser(@Body RegisterUser RegisterUser);


}
