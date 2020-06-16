package com.swadallail.nileapp.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.accept.AcceptOffers;
import com.swadallail.nileapp.adapters.HistoryAdapter;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.databinding.ActivityHistoryOredersBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryOreders extends AppCompatActivity {
    ActivityHistoryOredersBinding binding ;
    ProgressDialog dialog;
    HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_history_oreders);
        callApitoGetOrders();

    }

    private void callApitoGetOrders() {

        dialog = new ProgressDialog(HistoryOreders.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        String Token = "Bearer "+SharedHelper.getKey(HistoryOreders.this , "token");
        Call<Main<GetOrdersRes>> call = userclient.GetOrders(Token);
        call.enqueue(new Callback<Main<GetOrdersRes>>() {
            @Override
            public void onResponse(Call<Main<GetOrdersRes>> call, Response<Main<GetOrdersRes>> response) {
                if (response.body() != null){
                    Log.e("Body" , ""+response.body().data.size());
                    adapter = new HistoryAdapter(HistoryOreders.this , response.body());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(HistoryOreders.this);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    binding.recHistory.setLayoutManager(mLayoutManager);
                    binding.recHistory.setAdapter(adapter);
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