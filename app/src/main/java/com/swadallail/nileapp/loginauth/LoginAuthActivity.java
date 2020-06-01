package com.swadallail.nileapp.loginauth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.data.FacebookToken;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserLogin;
import com.swadallail.nileapp.data.UserResponse;
import com.swadallail.nileapp.databinding.ActivityLoginAuthBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.network.ApiInterface;
import com.swadallail.nileapp.registerauth.RegisterAuthActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAuthActivity extends AppCompatActivity {
    ActivityLoginAuthBinding binding;
    ProgressDialog dialog;
    MyClick handlers;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "isLoged") == "yes") {
            startActivity(new Intent(LoginAuthActivity.this, MainActivity.class));
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_auth);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);




    }


    public class MyClick {
        Context context;

        public MyClick(Context con) {
            con = this.context;
        }

        public void loginfun(View view) {
            String email = binding.edEmaillogin.getText().toString();
            String pass = binding.edPasslogin.getText().toString();

           if(email.isEmpty()){
                binding.edEmaillogin.setError("ادخل البريد الإلكترونى");
            }else if(pass.isEmpty()){
                binding.edPasslogin.setError("ادخل كلمة المرور");
            }else {
                dialog = new ProgressDialog(LoginAuthActivity.this);
                dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://nileapp-001-site3.itempurl.com/api/User/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiInterface userclient = retrofit.create(ApiInterface.class);
                UserLogin userLogin = new UserLogin(email, pass);
                Call<MainResponse<UserResponse<UserDataResponse>>> call = userclient.UserLoginFun(userLogin);
               SharedHelper.putKey(LoginAuthActivity.this, "token", "");
               SharedHelper.putKey(LoginAuthActivity.this,"UserName","");
               SharedHelper.putKey(LoginAuthActivity.this , "name" , "");
               SharedHelper.putKey(LoginAuthActivity.this , "picUrl" , "");
                call.enqueue(new Callback<MainResponse<UserResponse<UserDataResponse>>>() {
                    @Override
                    public void onResponse(Call<MainResponse<UserResponse<UserDataResponse>>> call, Response<MainResponse<UserResponse<UserDataResponse>>> response) {
                        if (response.body() != null){
                            Toast.makeText(LoginAuthActivity.this,""+ response.body().data.user.getUsername(), Toast.LENGTH_SHORT).show();
                            SharedHelper.putKey(LoginAuthActivity.this,"UserName",response.body().data.user.getUsername());
                            SharedHelper.putKey(LoginAuthActivity.this , "token" , response.body().data.token);
                            Log.e("TOKEN" , SharedHelper.getKey(LoginAuthActivity.this , "token"));
                            SharedHelper.putKey(LoginAuthActivity.this , "name" , response.body().data.user.getFullName());
                            SharedHelper.putKey(LoginAuthActivity.this , "picUrl" , response.body().data.user.getPic());
                            SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");
                            startActivity(new Intent(LoginAuthActivity.this , MainActivity.class));
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainResponse<UserResponse<UserDataResponse>>> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        }

        public void gotoregister(View view) {
            startActivity(new Intent(LoginAuthActivity.this, RegisterAuthActivity.class));
            finish();
        }

        public void facebooklogin(View view) {
            callbackManager = CallbackManager.Factory.create();
            binding.btnFaceLogin.setPermissions("email");
            binding.btnFaceLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    getUserData(loginResult.getAccessToken());
                    loginWithToken(loginResult.getAccessToken().getToken());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

        }

        public void googlelogin(View view) {

        }

        public void rememberme(View view) {
            SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");
        }

        public void forgetpassword(View view) {

        }

    }

    private void loginWithToken(String token) {
        dialog = new ProgressDialog(LoginAuthActivity.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nileapp-001-site3.itempurl.com/api/User/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        Log.e("Token" , token);
        FacebookToken facebookToken = new FacebookToken(token);
        Call<MainResponse<UserResponse<UserDataResponse>>> call2 = userclient.UserLoginwithtoken(facebookToken);
        SharedHelper.putKey(LoginAuthActivity.this, "token", "");
        SharedHelper.putKey(LoginAuthActivity.this,"UserName","");
        SharedHelper.putKey(LoginAuthActivity.this , "name" , "");
        SharedHelper.putKey(LoginAuthActivity.this , "picUrl" , "");
        call2.enqueue(new Callback<MainResponse<UserResponse<UserDataResponse>>>() {
            @Override
            public void onResponse(Call<MainResponse<UserResponse<UserDataResponse>>> call, Response<MainResponse<UserResponse<UserDataResponse>>> response) {
                if (response.body() != null) {
                    if (response.body().data.ssuccess) {
                        SharedHelper.putKey(LoginAuthActivity.this, "token", response.body().data.token);
                        SharedHelper.putKey(LoginAuthActivity.this, "retoken", response.body().data.refreshToken);
                        //Toast.makeText(LoginAuthActivity.this, response.body().data.token, Toast.LENGTH_SHORT).show();
                        SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");
                        SharedHelper.putKey(LoginAuthActivity.this,"UserName",response.body().data.user.getUsername());
                        Log.e("Name", response.body().data.user.getUsername());
                        //SharedHelper.putKey(LoginAuthActivity.this , "token" , response.body().data.token);
                        SharedHelper.putKey(LoginAuthActivity.this , "name" , response.body().data.user.getFullName());
                        SharedHelper.putKey(LoginAuthActivity.this , "picUrl" , response.body().data.user.getPic());


                        startActivity(new Intent(LoginAuthActivity.this, MainActivity.class));
                    }
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<MainResponse<UserResponse<UserDataResponse>>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void getUserData(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            //Toast.makeText(LoginAuthActivity.this, email, Toast.LENGTH_SHORT).show();
                            SharedHelper.putKey(LoginAuthActivity.this, "fname", first_name);
                            SharedHelper.putKey(LoginAuthActivity.this, "lname", last_name);
                            SharedHelper.putKey(LoginAuthActivity.this, "picUrl", image_url);
                            SharedHelper.putKey(LoginAuthActivity.this, "email", email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
