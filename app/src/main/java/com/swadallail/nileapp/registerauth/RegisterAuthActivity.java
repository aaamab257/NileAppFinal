package com.swadallail.nileapp.registerauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.api.model.ResultRegisterUser;
import com.swadallail.nileapp.api.service.UserClient;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.UserRegister;
import com.swadallail.nileapp.data.UserRegisterResponse;
import com.swadallail.nileapp.databinding.ActivityRegisterAuthBinding;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;
import com.swadallail.nileapp.signup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterAuthActivity extends AppCompatActivity {
    ActivityRegisterAuthBinding binding;
    MyClick handlers;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_auth);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);

    }

    public class MyClick {
        Context context;

        public MyClick(Context con) {
            con = this.context;
        }

        public void registerfun(View view) {
            dialog = new ProgressDialog(RegisterAuthActivity.this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            String email = binding.edEmail.getText().toString();
            String pass = binding.edPassword.getText().toString();
            String conpass = binding.edConfirmPass.getText().toString();
            if (email.isEmpty() || pass.isEmpty() || conpass.isEmpty()) {
                Toast.makeText(RegisterAuthActivity.this, "املأ الخانات الفارغة", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (pass.length() < 6) {
                Toast.makeText(RegisterAuthActivity.this, "كلمة المرور يجب ان تكون اكبر من 6 حروف او ارقام", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (!conpass.equals(pass)) {
                Toast.makeText(RegisterAuthActivity.this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://test.nileappco.com/api/User/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiInterface userclient = retrofit.create(ApiInterface.class);
                //Toast.makeText(RegisterAuthActivity.this, email + pass, Toast.LENGTH_SHORT).show();
                UserRegister userRegister = new UserRegister(email, pass, conpass);
                Call<MainResponse<UserRegisterResponse>> call = userclient.RegisterUser(userRegister);
                call.enqueue(new Callback<MainResponse<UserRegisterResponse>>() {
                    @Override
                    public void onResponse(Call<MainResponse<UserRegisterResponse>> call, Response<MainResponse<UserRegisterResponse>> response) {
                        if (response.body() != null) {
                            Toast.makeText(RegisterAuthActivity.this, "تم انشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterAuthActivity.this, LoginAuthActivity.class));
                            finish();
                            dialog.dismiss();
                        }


                    }

                    @Override
                    public void onFailure(Call<MainResponse<UserRegisterResponse>> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            }
        }

        public void gotologin(View view) {
            startActivity(new Intent(RegisterAuthActivity.this, LoginAuthActivity.class));
            finish();
        }

        public void facebooklogin(View view) {

        }

        public void googlelogin(View view) {

        }

    }
}
