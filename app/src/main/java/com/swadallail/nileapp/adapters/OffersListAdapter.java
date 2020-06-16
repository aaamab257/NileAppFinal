package com.swadallail.nileapp.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.accept.AcceptOffers;
import com.swadallail.nileapp.data.AcceptBody;
import com.swadallail.nileapp.data.AcceptRes;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.OfferResponse;
import com.swadallail.nileapp.data.SendOfferBody;
import com.swadallail.nileapp.data.SendOfferRes;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyViewHolder> {
    Context con ;
    Main<OfferResponse> mlist ;
    ProgressDialog dialog;
    AlertDialog alertDialog ;

    public OffersListAdapter(Context con, Main<OfferResponse> mlist) {
        this.con = con;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public OffersListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item, parent, false);
        return new OffersListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersListAdapter.MyViewHolder holder, int position) {
        holder.resname.setText(mlist.data.get(position).getReprestiveName());
        holder.ratenum.setText(mlist.data.get(position).getRate());
        holder.total.setText(mlist.data.get(position).getTotal());
        holder.ratingBar.setRating(Float.valueOf(mlist.data.get(position).getRate()));
        if(mlist.data.get(position).getImg() != null){
            Picasso.with(con).load(mlist.data.get(position).getImg()).into(holder.img);
        }else{
            holder.img.setImageResource(R.drawable.avatar_circle);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(position , v , mlist.data.get(position).getOfferId() ,mlist.data.get(position).getOrderId());
            }
        });

    }

    private void ShowDialog(int position, View v, int offerId, int orderId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con);
        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.accept_offer_dialog, null);
        dialogBuilder.setView(dialogView);
         alertDialog = dialogBuilder.create();

        Button btnverify = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btncan = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            callAcceptOffer(position , offerId , orderId);
            alertDialog.cancel();
            }
        });
        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void callAcceptOffer(int i, int offerId, int orderId) {
        dialog = new ProgressDialog(con);
        dialog.setMessage(con.getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        String Token = "Bearer "+ SharedHelper.getKey(con , "token");
        AcceptBody body = new AcceptBody(offerId , orderId);
        Call<MainResponse<AcceptRes>> call = userclient.Accept(Token , body );
        call.enqueue(new Callback<MainResponse<AcceptRes>>() {
            @Override
            public void onResponse(Call<MainResponse<AcceptRes>> call, Response<MainResponse<AcceptRes>> response) {
                if(response.body() != null){
                    Log.e("Success" , response.body().success+"");
                    if (response.body().data != null){
                        if(response.body().data.getOfferId() > 0){
                            Toast.makeText(con, "تم قبول العرض", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(con, "لقد قمت بقبول العرض من قبل", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(con, "لقد قمت بقبول العرض من قبل", Toast.LENGTH_SHORT).show();
                    }

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MainResponse<AcceptRes>> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error" ,t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView resname , total , ratenum ;
        RatingBar ratingBar ;
        ImageView img ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_repre);
            resname = itemView.findViewById(R.id.txt_repreName);
            total = itemView.findViewById(R.id.txt_price);
            ratenum = itemView.findViewById(R.id.txt_rate);
            ratingBar = itemView.findViewById(R.id.rb_repre);

        }
    }
}
