package com.swadallail.nileapp.accept;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.adapters.OfferAdapter;
import com.swadallail.nileapp.adapters.OffersListAdapter;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.OfferResponse;
import com.swadallail.nileapp.databinding.ActivityAcceptOffersBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.network.ApiInterface;
import com.swadallail.nileapp.offers.OffersPage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AcceptOffers extends AppCompatActivity {
    ActivityAcceptOffersBinding binding ;
    int orderid = 0 ;
    ProgressDialog dialog;
    OffersListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_accept_offers);
        Intent id = getIntent();
        orderid = id.getIntExtra("orderId" , 0);
        //Toast.makeText(this, ""+orderid, Toast.LENGTH_SHORT).show();
        GetOffersofOrder(orderid);
    }

    private void GetOffersofOrder(int orderid) {
        dialog = new ProgressDialog(AcceptOffers.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        String Token = "Bearer "+ SharedHelper.getKey(AcceptOffers.this , "token");
        Call<Main<OfferResponse>> call = userclient.GetOffers(Token , orderid);
        call.enqueue(new Callback<Main<OfferResponse>>() {
            @Override
            public void onResponse(Call<Main<OfferResponse>> call, Response<Main<OfferResponse>> response) {
                if (response.body() != null){
                    Log.e("Body" , ""+response.body().data.size());
                    adapter = new OffersListAdapter(AcceptOffers.this , response.body());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(AcceptOffers.this);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    binding.recOff.setLayoutManager(mLayoutManager);
                    binding.recOff.setAdapter(adapter);
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Main<OfferResponse>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}