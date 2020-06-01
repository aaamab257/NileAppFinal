package com.swadallail.nileapp.Drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallMe extends AppCompatActivity {
    TextView phone, whatsapp, email,facebook;
    String countryUser,countryU,phoneU,whatsappU,emailU;
    String tag_json_obj = "json_obj_req";
    int flagInternet=0;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 56789;

    String whatsappNumber = "+201150454254";
    String emailMe = "info@nileappco.com";
    String facebookMe = "https://www.facebook.com/Nile-app-%D9%86%D8%A7%D9%8A%D9%84-%D8%A2%D8%A8-105425790961563/?__tn__=kC-R&eid=ARAlFpbuIMHxnDY-Mf5UHQDO6vxkDms9oCzkX986m-4OOuYAjhTC3bxslVUYETLtfseEP2Z041bfCDz8&hc_ref=ARQVW3ByigAFrHkBZ8k32AbfIB75SQvyKastNjWvVG_OMUJo0UJZz-n-GQk4QAnJspE&fref=nf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callme);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        whatsapp = (TextView) findViewById(R.id.t2);
        email = (TextView) findViewById(R.id.t3);
        facebook = (TextView) findViewById(R.id.t4);


        ImageView  whatsappImg = (ImageView) findViewById(R.id.imageView1);
        ImageView  emailImg = (ImageView) findViewById(R.id.imageView2);
        ImageView   facebookImg = (ImageView) findViewById(R.id.imageView3);


        whatsapp.setText(whatsappNumber);
        email.setText(emailMe);
        //facebook.setText(facebookMe);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openWhatsappContact(whatsappNumber);
                }catch (Exception e){}


            }
        });


        whatsappImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openWhatsappContact(whatsappNumber);
                }catch (Exception e){}


            }
        });



        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent2 = new Intent(CallMe.this, ReportProblem.class);
                    startActivity(intent2);
                }catch (Exception e){}


            }
        });


        emailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent2 = new Intent(CallMe.this, ReportProblem.class);
                    startActivity(intent2);
                }catch (Exception e){}


            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(facebookMe));
                    startActivity(intent);
                }catch (Exception e){}


            }
        });


        facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(facebookMe));
                    startActivity(intent);

                }catch (Exception e){}


            }
        });


    }


    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
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
