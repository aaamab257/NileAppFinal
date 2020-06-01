package com.swadallail.nileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceCity;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceName;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferencePhone;
import com.swadallail.nileapp.api.model.CheckUser;
import com.swadallail.nileapp.api.model.ResultCheckUser;
import com.swadallail.nileapp.api.service.UserClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthCode extends AppCompatActivity {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback,mCallbacks;
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    TextView code;
    String phoneNumber, otp;
    int flagInternet = 0;
    ProgressDialog dialog;
    FirebaseAuth auth;
    private String verificationCode;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_code);

      //

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            Bundle extras = this.getIntent().getExtras();

            verificationCode = extras.getString("code");
            phoneNumber = extras.getString("phone");
           // callbackphone ob = new callbackphone();
            //token = ob.getForceResendingToken();
           // mCallbacks = ob.getmCallBacks();
            //mCallBacks = (PhoneAuthProvider.OnVerificationStateChangedCallbacks) extras.get("cb");
            // token =  extras.get("token");
        }
        catch (Exception e){}



        findViews();
        StartFirebaseLogin();





        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    dialog = new ProgressDialog(AuthCode.this);
                    dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    otp = etOTP.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    SigninWithPhone(credential);
                }
                catch (Exception e){

                }

            }
        });



        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                 /*
                try{


                    dialog = new ProgressDialog(AuthCode.this);
                    dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                   // private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthCode.this,           //a reference to an activity if this method is in a custom service
                            mCallback,
                            token);        // resending with token got at previous call's `callbacks` method `onCodeSent`
                            */
                    // [END start_phone_auth]
                   // }

/*
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            60,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            AuthCode.this,        // Activity (for callback binding)
                            mCallbacks);


                }catch (Exception e){}

*/

            }
        });




    }


    private void findViews() {

        btnSignIn=findViewById(R.id.btn_sign_in);
        etOTP=findViewById(R.id.et_otp);
        code = findViewById(R.id.code);
    }






    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(AuthCode.this,"verification completed",Toast.LENGTH_SHORT).show();
                getCheckUser(phoneNumber);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(AuthCode.this,"فشل التحقق",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(AuthCode.this,"تم إرسال الكود",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
    }



    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // startActivity(new Intent(AuthPhone.this,SignedIn.class));
                            //finish();
                            try {

                                getCheckUser(phoneNumber);

                            }
                            catch (Exception e){}


                        } else {
                            Toast.makeText(AuthCode.this,"رمز التحقق غير صحيح",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }



    private void getCheckUser(String mobileNo){




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
                    Toast.makeText(AuthCode.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                try {

                CheckUser checkUser = response.body().getCheckUser();


                if(null == checkUser) {

                                dialog.dismiss();
                                Intent intent2 = new Intent(AuthCode.this, signup.class);
                                intent2.putExtra("phone", mobileNo); //lat

                                    startActivity(intent2);
                                                finish();
                            }else {
                                    String id = checkUser.getid();
                                    String Name = checkUser.getfullName();
                                    String phone = checkUser.getmobileNo();
                                dialog.dismiss();
                                SaveSharedPreferencePhone.setUserName(AuthCode.this, phone);
                                SaveSharedPreferenceName.setUserName(AuthCode.this, Name);

                                Toast.makeText(AuthCode.this, "نجح التحقق", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(AuthCode.this, MainActivity.class);
                                startActivity(intent2);

               }
                }catch (Exception e)
                {

                  //  Toast.makeText(AuthCode.this, "error internet", Toast.LENGTH_LONG).show();
                   // dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResultCheckUser> call, Throwable t) {
                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(AuthCode.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                   // Toast.makeText(AuthCode.this,t.getMessage(), Toast.LENGTH_LONG).show();
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
