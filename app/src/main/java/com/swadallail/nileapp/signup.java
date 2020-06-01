package com.swadallail.nileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferencePhone;
import com.swadallail.nileapp.api.model.CreatShop;
import com.swadallail.nileapp.api.model.RegisterUser;
import com.swadallail.nileapp.api.model.ResultCreateShop;
import com.swadallail.nileapp.api.model.ResultRegisterUser;
import com.swadallail.nileapp.api.service.UserClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signup extends AppCompatActivity implements View.OnClickListener {

    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static TextView conditions;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);


        initViews();
        setListeners();





    }

    private void initViews() {

        Bundle extras = this.getIntent().getExtras();
       String phoneNumber = extras.getString("phone");

        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        mobileNumber.setText(phoneNumber);

        fullName = (EditText) findViewById(R.id.fullName);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
       // login = (TextView) findViewById(R.id.already_user);
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        loginLayout = (LinearLayout) findViewById(R.id.signup_layout);
        shakeAnimation = AnimationUtils.loadAnimation(signup.this, R.anim.shake);
        conditions = (TextView) findViewById(R.id.condition);

        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(signup.this, Conditions.class);
                startActivity(intent1);


            }
        });
    }




    private void setListeners() {
        signUpButton.setOnClickListener(this);
        //login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:


                // Call checkValidation method
                checkValidation();

                break;

          // case R.id.already_user:
                // Replace login fragment
          //      new Main2Activity().replaceLoginFragment();
            //    break;
        }

    }



    private void checkValidation() {



        // Get all edittext texts
        //SaveSharedPreference.setUserName(getActivity(),mobileNumber.getText().toString());
        String getFullName = fullName.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();


        // Pattern match for email id
       // Pattern p = Pattern.compile(Utils.regEx);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0)
        {
            //vibe.vibrate(100);
            loginLayout.startAnimation(shakeAnimation);
            new CustomToastt().Show_Toast(this, getResources().getString(R.string.all_fields_are_required));
        }

        // Make sure user should check Terms and Conditions checkbox
        else if(!terms_conditions.isChecked()) {
            //vibe.vibrate(100);
            loginLayout.startAnimation(shakeAnimation);
            new CustomToastt().Show_Toast(this, getResources().getString(R.string.please_accept_the_terms_and_conditions));
        }
        // Else do signup or do your stuff
        else {
            Toast.makeText(signup.this, getResources().getString(R.string.successfully_registered), Toast.LENGTH_SHORT)
                    .show();

            String name = fullName.getText().toString().trim();
            String phone = mobileNumber.getText().toString().trim();
//////////////////////
            dialog = new ProgressDialog(signup.this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);





            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.nileappco.com/api/User/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


            RegisterUser ru = new RegisterUser(name,phone);
            Call<ResultRegisterUser> con = userclient.RegisterUser(ru);

            // Call<ResultModels> con = userclient.getCustomers();
//للتنفيذ
            con.enqueue(new Callback<ResultRegisterUser>() {
                @Override
                public void onResponse(Call<ResultRegisterUser> call, Response<ResultRegisterUser> response) {
                    try {



                        if(response.isSuccessful()) {

                            dialog.dismiss();

                            SaveSharedPreferencePhone.setUserName(signup.this,mobileNumber.getText().toString());
                            Intent intent1 = new Intent(signup.this,MainActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent1);
                            finish();


                            Toast.makeText(signup.this,"نجح"  , Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            dialog.dismiss();
                            Toast.makeText(signup.this,  getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e)
                    {
                        dialog.dismiss();
                        Toast.makeText(signup.this, "error internet", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultRegisterUser> call, Throwable t) {

                    dialog.dismiss();
                    Toast.makeText(signup.this,  getResources().getString(R.string.error), Toast.LENGTH_LONG).show();


                }


            });







        }

    }





}
