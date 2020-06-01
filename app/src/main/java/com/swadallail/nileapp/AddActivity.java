package com.swadallail.nileapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swadallail.nileapp.SaveSharedPreference.*;

import com.swadallail.nileapp.Governorates.NewsListActivityMain;
import com.swadallail.nileapp.api.model.Cities;
import com.swadallail.nileapp.api.model.CreatShop;
import com.swadallail.nileapp.api.model.ResultCreateShop;
import com.swadallail.nileapp.api.model.ResultShopType;
import com.swadallail.nileapp.api.model.ShopeType;
import com.swadallail.nileapp.api.service.UserClient;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {

    ArrayList<String> myTitle = new ArrayList<String>();
    HashMap<String, Integer> iD = new HashMap<String, Integer>();
    Spinner spinner;
    int flagInternet = 0;
    ProgressDialog dialog;
    private static Animation shakeAnimation;
    int CityId = -1;
    int  GovernId = -1;
   // String NameAct, NameUser, Citis,Mobile;
   // EditText eNameAct,eNameUser,eCity,eMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = this.getIntent().getExtras();

        final double currentLat = extras.getDouble("lat");
        final double currentLong = extras.getDouble("long");
       // EditText eMobile = (EditText) findViewById(R.id.mobile);
        //eMobile.setText(" ");

        //allShoptype();


       // Toast.makeText(getApplicationContext(), "lat=" + currentLat + "laong=" + currentLong , Toast.LENGTH_SHORT).show();


        EditText city =(EditText) findViewById(R.id.city);;
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(AddActivity.this,"نجح", Toast.LENGTH_LONG).show();
               // Intent intent1 = new Intent(AddActivity.this, NewsListActivityMain.class);
                Intent intent1 = new Intent(AddActivity.this, NewsListActivityMain.class);

                startActivity(intent1);

            }
        });



         spinner = (Spinner) findViewById(R.id.TypeAct);


        final String[] select_qualification = {
                "الفئة"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select_qualification);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);








        View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Your code
                    if(myTitle.size() == 0) {
                        dialog = new ProgressDialog(com.swadallail.nileapp.AddActivity.this);
                        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);

                        allShoptype();
                    }

                }
                return false;
            }
        };



        spinner.setOnTouchListener(spinnerOnTouch);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                String item = parentView.getItemAtPosition(position).toString();


               // int CityId = Objects.requireNonNull(iD.get(item));
                          if(iD.size() !=0) {
                              GovernId = iD.get(item);
                              //Toast.makeText(parentView.getContext(),Integer.toString(GovernId) + item, Toast.LENGTH_LONG).show();
                          }
                // Showing selected spinner item



             //   String selectedItemText = (String) parentView.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
              //  if (position > 0) {
                    // Notify the selected item text
                 //   Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(getApplicationContext(), "Selected : " , Toast.LENGTH_SHORT).show();
              //  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }






    @Override
    public void onResume() {
        super.onResume();

        if (SaveSharedPreferenceCity.getUserName(AddActivity.this).length() != 0 && SaveSharedPreferenceCityId.getUserName(AddActivity.this).length() != 0){

            EditText Edcity =(EditText) findViewById(R.id.city);

            String cityy = SaveSharedPreferenceCity.getUserName(AddActivity.this);
             CityId = Integer.parseInt(SaveSharedPreferenceCityId.getUserName(AddActivity.this));
            //Toast.makeText(getApplicationContext(), Integer.toString(CityId), Toast.LENGTH_SHORT).show();
            Edcity.setText(cityy);
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("city").apply();
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("CityId").apply();



        }


    }


    public void allShoptype() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/Shop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi
        Call<ResultShopType> con = userclient.geShopType();
//للتنفيذ
        con.enqueue(new Callback<ResultShopType>() {
            @Override
            public void onResponse(Call<ResultShopType> call, Response<ResultShopType> response) {
                try {

                   // myTitle.add("الفئة");
                    List<ShopeType> customers = response.body().getShopeType();
                    //for (int i = 0; i < customers.size(); i++) {
                    if (customers.size() == 0) {
                        dialog.dismiss();
                    } else {

                       // for (int i = (customers.size() - 1); i >= 0; i--) {
                        for (int i = 0; i < customers.size(); i++) {
                            int id = customers.get(i).getShopTypeId();
                            String Name = customers.get(i).getShopTypeName();
                            myTitle.add(Name);
                            iD.put(Name, id);



                          //  if (i == 0) {
                            if (i == (customers.size() - 1)) {
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, myTitle);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinner.setAdapter(dataAdapter);
                                dialog.dismiss();
                            }
                        }
                    }

                }
                catch (Exception e)
                {

                    Toast.makeText(AddActivity.this, "error internet", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultShopType> call, Throwable t) {
                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(com.swadallail.nileapp.AddActivity.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }

            }
        });

    }



    public void done(View v) {


       EditText eNameAct = (EditText) findViewById(R.id.NameAct);
      EditText  eNameUser = (EditText) findViewById(R.id.NameUser);
       EditText eCity = (EditText) findViewById(R.id.city);
       EditText eMobile = (EditText) findViewById(R.id.mobile);


      String  NameAct = eNameAct.getText().toString();
       String NameUser = eNameUser.getText().toString();
       String Citis = eCity.getText().toString();
        String Mobile = eMobile.getText().toString();


        if (NameAct.length() <= 0  || Citis.length() <= 0  || CityId < 0 || GovernId < 0 ) {

            try {
                new CustomToastt().Show_Toast(this, getResources().getString(R.string.all_fields_are_required));
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                shakeAnimation = AnimationUtils.loadAnimation(AddActivity.this, R.anim.shake);
                LinearLayout loginLayout = (LinearLayout) findViewById(R.id.activity_register);
                loginLayout.startAnimation(shakeAnimation);
            }
            catch (Exception E)
            {


                Toast.makeText(com.swadallail.nileapp.AddActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();

            }
        }

        else if(Mobile.length()!=0 && Mobile.length()!=5 && Mobile.length()!=10 && Mobile.length()!=11)
        {

            try {
                new CustomToastt().Show_Toast(this, getResources().getString(R.string.Make_sure_the_phone_number_is_correct));
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                shakeAnimation = AnimationUtils.loadAnimation(AddActivity.this, R.anim.shake);
                LinearLayout loginLayout = (LinearLayout) findViewById(R.id.activity_register);
                loginLayout.startAnimation(shakeAnimation);
            }
            catch (Exception E)
            {


                Toast.makeText(com.swadallail.nileapp.AddActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();

            }


        } else
        {


            dialog = new ProgressDialog(com.swadallail.nileapp.AddActivity.this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);


            Bundle extras = this.getIntent().getExtras();

            final double currentLat = extras.getDouble("lat");
            final double currentLong = extras.getDouble("long");
            int flag =0;


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.nileappco.com/api/Shop/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


            CreatShop CreatGeovernorate = new CreatShop(NameAct,NameUser,Mobile,currentLat,currentLong,GovernId,CityId);
            Call<ResultCreateShop> con = userclient.CreatGeovernorate(CreatGeovernorate);

            // Call<ResultModels> con = userclient.getCustomers();
//للتنفيذ
            con.enqueue(new Callback<ResultCreateShop>() {
                @Override
                public void onResponse(Call<ResultCreateShop> call, Response<ResultCreateShop> response) {
                    try {



                        if(response.isSuccessful()) {

                            Intent intent1 = new Intent(AddActivity.this, MainActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent1.putExtra("numbers1", currentLat); //lat
                            intent1.putExtra("numbers2", currentLong); //lng
                            intent1.putExtra("numbers3", 111);
                            startActivity(intent1);
                            finish();

                            Toast.makeText(AddActivity.this,"تم إستلام طلبك"  , Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            dialog.dismiss();
                            Toast.makeText(AddActivity.this,  getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e)
                    {
                        dialog.dismiss();
                        Toast.makeText(AddActivity.this, "error internet", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultCreateShop> call, Throwable t) {

                    dialog.dismiss();
                    Toast.makeText(AddActivity.this,  getResources().getString(R.string.error), Toast.LENGTH_LONG).show();


                }


            });






        }


    }




    public void back(View v) {

        finish();
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
