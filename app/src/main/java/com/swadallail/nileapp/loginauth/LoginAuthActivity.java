package com.swadallail.nileapp.loginauth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.swadallail.nileapp.AuthPhone;
import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.Services.ChatService;
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
    boolean mBound = false;
    ChatService chatService;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    Profile profile;
    Intent intent;
    Intent open;
    String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatService = new ChatService();
        if (SharedHelper.getKey(this, "isLoged").equals("yes")) {
            Intent intent = new Intent(LoginAuthActivity.this, ChatService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Intent goo = new Intent(LoginAuthActivity.this, MainActivity.class);
            startActivity(goo);
            LoginAuthActivity.this.finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_auth);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);
        Log.e("Loged", SharedHelper.getKey(this, "isLoged"));


    }


    public class MyClick {
        Context context;

        public MyClick(Context con) {
            con = this.context;
        }

        public void phonelogin(View view) {
            startActivity(new Intent(LoginAuthActivity.this, AuthPhone.class));
            finish();
        }

        public void loginfun(View view) {
            Log.e("UserNameBefore", SharedHelper.getKey(LoginAuthActivity.this, "UserName"));
            open = new Intent(LoginAuthActivity.this, MainActivity.class);
            email = binding.edEmaillogin.getText().toString();
            pass = binding.edPasslogin.getText().toString();

            if (email.isEmpty()) {
                binding.edEmaillogin.setError("ادخل البريد الإلكترونى");
            } else if (pass.isEmpty()) {
                binding.edPasslogin.setError("ادخل كلمة المرور");
            } else {
                SharedHelper.clearSharedPreferences(LoginAuthActivity.this);
                dialog = new ProgressDialog(LoginAuthActivity.this);
                dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://test.nileappco.com/api/User/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiInterface userclient = retrofit.create(ApiInterface.class);
                UserLogin userLogin = new UserLogin(email, pass);
                Call<MainResponse<UserResponse<UserDataResponse>>> call = userclient.UserLoginFun(userLogin);
                call.enqueue(new Callback<MainResponse<UserResponse<UserDataResponse>>>() {
                    @Override
                    public void onResponse(Call<MainResponse<UserResponse<UserDataResponse>>> call, Response<MainResponse<UserResponse<UserDataResponse>>> response) {
                        if (response.body() != null) {
                            Log.e("StatusCode", "" + response.body().statusCode);
                            dialog.dismiss();
                            if (response.body().success) {
                                Log.e("SAVED", "YES");
                                SharedHelper.putKey(LoginAuthActivity.this, "mailConfirm", "" + response.body().data.user.getMailConfirmed());
                                SharedHelper.putKey(LoginAuthActivity.this, "phoneConfirm", "" + response.body().data.user.getPhoneConfirmed());
                                Toast.makeText(LoginAuthActivity.this, "" + response.body().data.user.getUsername(), Toast.LENGTH_SHORT).show();
                                SharedHelper.putKey(LoginAuthActivity.this, "UserName", response.body().data.user.getUsername());
                                SharedHelper.putKey(LoginAuthActivity.this, "role", response.body().data.user.getRole());
                                SharedHelper.putKey(LoginAuthActivity.this, "token", response.body().data.token);
                                chatService.getToken(LoginAuthActivity.this, response.body().data.token, response.body().data.user.getUsername());
                                Log.e("UserNameAfter", SharedHelper.getKey(LoginAuthActivity.this, "UserName"));
                                SharedHelper.putKey(LoginAuthActivity.this, "name", response.body().data.user.getFullName());
                                SharedHelper.putKey(LoginAuthActivity.this, "picUrl", response.body().data.user.getPic());
                                if (binding.chRememberme.isChecked()) {
                                    SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");
                                } else {
                                    SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "no");
                                }
                                dialog.dismiss();
                                Intent intent = new Intent(LoginAuthActivity.this, ChatService.class);
                                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                                startActivity(open);
                                LoginAuthActivity.this.finish();
                            } else {
                                Log.e("SAVED", "NO");
                                Toast.makeText(LoginAuthActivity.this, "خطأ فى البريد او كلمة المرور", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainResponse<UserResponse<UserDataResponse>>> call, Throwable t) {
                        Toast.makeText(LoginAuthActivity.this, "خطأ فى البريد او كلمة المرور", Toast.LENGTH_SHORT).show();
                        Log.e("SS", t.toString());
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
            /*SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");*/
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
                .baseUrl("https://test.nileappco.com/api/User/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        Log.e("Token", token);
        FacebookToken facebookToken = new FacebookToken(token);
        Call<MainResponse<UserResponse<UserDataResponse>>> call2 = userclient.UserLoginwithtoken(facebookToken);
        SharedHelper.putKey(LoginAuthActivity.this, "token", "");
        SharedHelper.putKey(LoginAuthActivity.this, "UserName", "");
        SharedHelper.putKey(LoginAuthActivity.this, "name", "");
        SharedHelper.putKey(LoginAuthActivity.this, "picUrl", "");
        call2.enqueue(new Callback<MainResponse<UserResponse<UserDataResponse>>>() {
            @Override
            public void onResponse(Call<MainResponse<UserResponse<UserDataResponse>>> call, Response<MainResponse<UserResponse<UserDataResponse>>> response) {
                if (response.body() != null) {
                    if (response.body().data.ssuccess) {
                        SharedHelper.putKey(LoginAuthActivity.this, "token", response.body().data.token);
                        chatService.getToken(LoginAuthActivity.this, response.body().data.token, response.body().data.user.getUsername());
                        SharedHelper.putKey(LoginAuthActivity.this, "retoken", response.body().data.refreshToken);
                        //Toast.makeText(LoginAuthActivity.this, response.body().data.token, Toast.LENGTH_SHORT).show();
                        SharedHelper.putKey(LoginAuthActivity.this, "isLoged", "yes");
                        SharedHelper.putKey(LoginAuthActivity.this, "UserName", response.body().data.user.getUsername());
                        Log.e("Name", response.body().data.user.getUsername());
                        //SharedHelper.putKey(LoginAuthActivity.this , "token" , response.body().data.token);
                        SharedHelper.putKey(LoginAuthActivity.this, "name", response.body().data.user.getFullName());
                        SharedHelper.putKey(LoginAuthActivity.this, "picUrl", response.body().data.user.getPic());
                        Intent intent = new Intent(LoginAuthActivity.this, ChatService.class);
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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

    public ServiceConnection mConnection = new ServiceConnection() {

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
}
