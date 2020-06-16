package com.swadallail.nileapp.orderpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.Services.ChatService;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.PickedBody;
import com.swadallail.nileapp.data.RateBody;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserLogin;
import com.swadallail.nileapp.data.UserResponse;
import com.swadallail.nileapp.databinding.ActivityOrderProgressBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderProgress extends AppCompatActivity {
    Intent getDetails;
    ActivityOrderProgressBinding binding;
    MyClick handlers;
    String otolat, otolng, ofromlat, ofromlng, oname, oid, ownerId;
    ProgressDialog dialog;
    int orderID = 0;
    String rule = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_progress);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);
        rule = SharedHelper.getKey(this, "role");
        if(rule.equals("WebClient")){
            binding.btnRec.setVisibility(View.GONE);
            binding.btnDone.setVisibility(View.GONE);
            binding.nname.setText("المندوب");
            binding.goo.setVisibility(View.GONE);
            binding.lin.setVisibility(View.GONE);
        }else {
            binding.btnRec.setVisibility(View.VISIBLE);
            binding.btnDone.setVisibility(View.VISIBLE);
        }
        setData();

    }

    private void setData() {
        getDetails = getIntent();//image
        String img = getDetails.getStringExtra("image");
        String otxt = getDetails.getStringExtra("orderText");
        int oh = getDetails.getIntExtra("orderH", 0);
        String addfrom = getDetails.getStringExtra("orderFrom");
        String addto = getDetails.getStringExtra("orderTo");
        otolat = getDetails.getStringExtra("orderTolat");
        otolng = getDetails.getStringExtra("orderTolng");
        ofromlat = getDetails.getStringExtra("orderFromlat");
        ofromlng = getDetails.getStringExtra("orderFromlng");
        oname = getDetails.getStringExtra("orderOwner");
        oid = getDetails.getStringExtra("orderID");
        orderID = Integer.valueOf(oid);
        ownerId = getDetails.getStringExtra("orderOwnerID");
        binding.orderText.setText(otxt);
        binding.orderAfrom.setText(addfrom);
        binding.orderAto.setText(addto);
        binding.orderOwner.setText(oname);
        binding.orderH.setText(oh + "ساعة");
        if (img.isEmpty()) {
            binding.imgOrder.setImageResource(R.drawable.nileapp);
        } else {
            Picasso.with(this).load(img).into(binding.imgOrder);
        }


    }

    public class MyClick {
        Context context;

        public MyClick(Context con) {
            con = this.context;
        }

        public void picked(View view) {
            showPickedAlert();
        }

        public void sent(View view) {
            showSentAlert();
        }

        public void goTo(View view) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + otolat + "," + otolng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }

        public void goFrom(View view) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + ofromlat + "," + ofromlng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void showSentAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderProgress.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sendalert, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button ok = dialogView.findViewById(R.id.btn_ok);
        Button no = dialogView.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitsentApi();
                alertDialog.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void showPickedAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderProgress.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pickedalert, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button ok = dialogView.findViewById(R.id.btn_ok);
        Button no = dialogView.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitPickedApi();
                alertDialog.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }

    private void hitsentApi() {
        dialog = new ProgressDialog(OrderProgress.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        PickedBody body = new PickedBody(orderID);
        String token = "Bearer " + SharedHelper.getKey(OrderProgress.this, "token");
        Call<MainResponse> call = userclient.orderDelivered(token, body);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().success) {
                            Toast.makeText(OrderProgress.this, "تم تسليم الطلب", Toast.LENGTH_LONG).show();
                            showRatingAlert();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(OrderProgress.this, "لقد قمت بتسليم الطلب بالفعل", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(OrderProgress.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void showRatingAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderProgress.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ratealert, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        RatingBar rateuser = dialogView.findViewById(R.id.rateUser);
        Button ok = dialogView.findViewById(R.id.btn_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rate = (int) rateuser.getRating();
                if(rate != 0){
                    hitRateApi(rate);
                    alertDialog.cancel();
                }else {
                    Toast.makeText(OrderProgress.this, "رجاء قم بتقيم العميل", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void hitRateApi(int rate) {
        dialog = new ProgressDialog(OrderProgress.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        RateBody body = new RateBody(rate ,ownerId);
        String token = "Bearer " + SharedHelper.getKey(OrderProgress.this, "token");
        Call<MainResponse> call = userclient.rateUser(token, body);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().success) {
                            Toast.makeText(OrderProgress.this, "تم تقيم العميل", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(OrderProgress.this, "لقد قمت بتقيم العميل من قبل", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(OrderProgress.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void hitPickedApi() {
        dialog = new ProgressDialog(OrderProgress.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        PickedBody body = new PickedBody(orderID);
        String token = "Bearer " + SharedHelper.getKey(OrderProgress.this, "token");
        Call<MainResponse> call = userclient.orderPicked(token, body);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().success) {
                            Toast.makeText(OrderProgress.this, "تم استلام الطلب", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(OrderProgress.this, "لقد قمت بأستلام الطلب بالفعل", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Toast.makeText(OrderProgress.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}