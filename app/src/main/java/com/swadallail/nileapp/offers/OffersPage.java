package com.swadallail.nileapp.offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.adapters.HistoryAdapter;
import com.swadallail.nileapp.adapters.OfferAdapter;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.databinding.ActivityOffersPageBinding;
import com.swadallail.nileapp.delegete.DelegeteHome;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.history.HistoryOreders;
import com.swadallail.nileapp.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffersPage extends AppCompatActivity {
    ActivityOffersPageBinding binding ;
    ProgressDialog dialog;
    OfferAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_offers_page);
        callApitoGetOrders();
    }
    private void callApitoGetOrders() {

        dialog = new ProgressDialog(OffersPage.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        String Token = "Bearer "+ SharedHelper.getKey(OffersPage.this , "token");
        Call<Main<GetOrdersRes>> call = userclient.GetOrders(Token);
        call.enqueue(new Callback<Main<GetOrdersRes>>() {
            @Override
            public void onResponse(Call<Main<GetOrdersRes>> call, Response<Main<GetOrdersRes>> response) {
                if (response.body() != null){
                    //Log.e("Body" , ""+response.body().data.size());
                    adapter = new OfferAdapter(OffersPage.this , response.body());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(OffersPage.this);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    binding.recOffers.setLayoutManager(mLayoutManager);
                    binding.recOffers.setAdapter(adapter);
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Main<GetOrdersRes>> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }
}