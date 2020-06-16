package com.swadallail.nileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.swadallail.nileapp.Cities.NewsListActivityMain;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceCity;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceName;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferencePhone;
import com.swadallail.nileapp.api.model.CheckUser;
import com.swadallail.nileapp.api.model.Cities;
import com.swadallail.nileapp.api.model.ResultCheckUser;
import com.swadallail.nileapp.api.model.ResultCities;
import com.swadallail.nileapp.api.service.UserClient;


import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthPhone extends AppCompatActivity {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    int flagInternet = 0;

    ProgressDialog dialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    FirebaseAuth auth;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View view = inflater.inflate(R.layout.login_layout, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            LinearLayout li = findViewById(R.id.li);
            li.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        }


        findViews();
        StartFirebaseLogin();


        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    dialog = new ProgressDialog(AuthPhone.this);
                    dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    phoneNumber = "+2" + etPhoneNumber.getText().toString();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            60,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            AuthPhone.this,        // Activity (for callback binding)
                            mCallback);                      // OnVerificationStateChangedCallbacks

                } catch (Exception e) {

                }

            }
        });


    }


    private void findViews() {
        btnGenerateOTP = findViewById(R.id.btn_generate_otp);
        etPhoneNumber = findViewById(R.id.et_phone_number);
    }


    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                try {
                    dialog.dismiss();
                    Toast.makeText(AuthPhone.this, "هذا الرقم تم التسجيل به من قبل", Toast.LENGTH_SHORT).show();
                   //getCheckUser(phoneNumber);

                } catch (Exception e) {

                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                dialog.dismiss();
                Toast.makeText(AuthPhone.this, "فشل التحقق", Toast.LENGTH_SHORT).show();
                Log.e("AuthPhone" , e.toString());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    //Log.d(TAG, "Invalid credential: "
                    //  + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Toast.makeText(AuthPhone.this, "لقد تجاوزة عدد الرسائل اليوم", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;

                dialog.dismiss();

                try {

                    Toast.makeText(AuthPhone.this, "تم إرسال الكود", Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(AuthPhone.this, AuthCode.class);
                    intent2.putExtra("code", verificationCode);
                    intent2.putExtra("phone", phoneNumber);
                    // intent2.putExtra("cb", (Parcelable) mCallback);
                    //intent2.putExtra("token", forceResendingToken);
                    // callbackphone ob = new callbackphone();
                    //ob.setForceResendingToken(forceResendingToken);
                    //ob.setmCallBacks(mCallback);

                    startActivity(intent2);

                } catch (Exception e) {
                }

            }
        };
    }


    private void getCheckUser(String mobileNo) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/User/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


        Call<ResultCheckUser> con = userclient.getCheckUser(mobileNo.substring(1));

//للتنفيذ
        con.enqueue(new Callback<ResultCheckUser>() {
            @Override
            public void onResponse(Call<ResultCheckUser> call, retrofit2.Response<ResultCheckUser> response) {
                if (flagInternet == 1) {
                    Toast.makeText(AuthPhone.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                try {

                    CheckUser checkUser = response.body().getCheckUser();


                    if (null == checkUser) {

                        dialog.dismiss();
                        Toast.makeText(AuthPhone.this, "رقمك غير متوفر حاليا,الرجاء مراجعة خدمة العملاء", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(AuthPhone.this, MainActivity.class);
                        startActivity(intent2);
                        finish();

                    } else {

                        String id = checkUser.getid();
                        String Name = checkUser.getfullName();
                        String phone = checkUser.getmobileNo();

                        dialog.dismiss();

                        SaveSharedPreferencePhone.setUserName(AuthPhone.this, phone);
                        SaveSharedPreferenceName.setUserName(AuthPhone.this, Name);

                        Toast.makeText(AuthPhone.this, "نجح التحقق", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(AuthPhone.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                } catch (Exception e) {

                    Toast.makeText(AuthPhone.this, "error internet", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultCheckUser> call, Throwable t) {
                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(AuthPhone.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
