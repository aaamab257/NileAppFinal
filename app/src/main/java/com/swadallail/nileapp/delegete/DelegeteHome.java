package com.swadallail.nileapp.delegete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.adapters.HistoryAdapter;
import com.swadallail.nileapp.adapters.NewOrdersAdapter;
import com.swadallail.nileapp.data.GetNewOrdersData;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.databinding.ActivityDelegeteHomeBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.history.HistoryOreders;
import com.swadallail.nileapp.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DelegeteHome extends AppCompatActivity {
    ActivityDelegeteHomeBinding binding;
    ProgressDialog dialog;
    private LocationManager mLocationManager;
    NewOrdersAdapter adapter;
    Location myLocation ;
    double lat , lng ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delegete_home);
        getMyLocation();
        callApitoGetOrders();

    }

    private void getMyLocation() {
        myLocation = getLastKnownLocation() ;
        lat = myLocation.getLatitude() ;
        lng = myLocation.getLongitude();
        Toast.makeText(DelegeteHome.this , ""+lat+","+lng, Toast.LENGTH_SHORT).show();
    }

    private void callApitoGetOrders() {

        dialog = new ProgressDialog(DelegeteHome.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        String Token = "Bearer " + SharedHelper.getKey(DelegeteHome.this, "token");
        Call<Main<GetNewOrdersData>> call = userclient.GetNewOrders(Token , lat , lng);
        call.enqueue(new Callback<Main<GetNewOrdersData>>() {
            @Override
            public void onResponse(Call<Main<GetNewOrdersData>> call, Response<Main<GetNewOrdersData>> response) {
                if (response.body() != null) {
                    Log.e("BodyDelegete", "" + response.body().data.size());
                    adapter = new NewOrdersAdapter(DelegeteHome.this, response.body());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(DelegeteHome.this);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    binding.recOrders.setLayoutManager(mLayoutManager);
                    binding.recOrders.setAdapter(adapter);
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Main<GetNewOrdersData>> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }


        return bestLocation;

    }
}
