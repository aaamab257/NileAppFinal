package com.swadallail.nileapp.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.data.GetNewOrdersData;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.SendOfferBody;
import com.swadallail.nileapp.data.SendOfferRes;
import com.swadallail.nileapp.delegete.DelegeteHome;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.MyViewHolder> {
    Context con;
    Main<GetNewOrdersData> mlist;
    ProgressDialog dialog;
    String pricea;
    char first;

    public NewOrdersAdapter(Context con, Main<GetNewOrdersData> mlist) {
        this.con = con;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.order.setText(mlist.data.get(position).getDescription());
        holder.hours.setText("" + mlist.data.get(position).getHours());
        holder.name.setText("" + mlist.data.get(position).getOwnerName());
        holder.hours.setText("" + mlist.data.get(position).getHours());
        holder.hours.setText("" + mlist.data.get(position).getHours());
        holder.id.setText(mlist.data.get(position).getOrderId() + "");
        holder.loc1.setText(mlist.data.get(position).getFromdis());
        holder.loc2.setText(mlist.data.get(position).getTodis());
        holder.sendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricea = holder.amount.getText().toString();
                if (pricea.isEmpty()) {
                    Toast.makeText(con, "قم بأدخال رسوم التوصيل", Toast.LENGTH_SHORT).show();
                } else {
                    first = pricea.charAt(0);
                    if (first == '0') {
                        Toast.makeText(con, "اول رقم لا يجب ان يكون 0", Toast.LENGTH_SHORT).show();
                    } else if (pricea.equals("0") || pricea.equals("00") || pricea.equals("000")) {
                        Toast.makeText(con, "السعر لا يجب ان يكون 0", Toast.LENGTH_SHORT).show();
                    } else {
                        int priceaint = Integer.valueOf(pricea);
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
                        String Token = "Bearer " + SharedHelper.getKey(con, "token");
                        SendOfferBody body = new SendOfferBody(mlist.data.get(position).getOrderId(), priceaint);
                        Call<MainResponse<SendOfferRes>> call = userclient.SendOffer(Token, body);
                        call.enqueue(new Callback<MainResponse<SendOfferRes>>() {
                            @Override
                            public void onResponse(Call<MainResponse<SendOfferRes>> call, Response<MainResponse<SendOfferRes>> response) {
                                if (response.body() != null) {
                                    if (response.body().success) {
                                        ShowAlertDilaog(v, response.body().data.getTotal());
                                    } else {
                                        Toast.makeText(con, "لقد قمت بارسال طلب من قبل", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                dialog.dismiss();
                            }


                            @Override
                            public void onFailure(Call<MainResponse<SendOfferRes>> call, Throwable t) {
                                dialog.dismiss();
                            }
                        });
                    }
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return mlist.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order, loc1, loc2, hours, name, ratNum, id;
        RatingBar ratingBar;
        EditText amount;
        Button sendOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txt_orderid);
            sendOffer = itemView.findViewById(R.id.send_offer);
            amount = itemView.findViewById(R.id.ed_price);
            order = itemView.findViewById(R.id.ordertext);
            loc1 = itemView.findViewById(R.id.loc_from);
            loc2 = itemView.findViewById(R.id.loc_to);
            hours = itemView.findViewById(R.id.hours);
            name = itemView.findViewById(R.id.txt_name);
            ratNum = itemView.findViewById(R.id.rate_num);
        }

    }

    private void ShowAlertDilaog(View v, String total) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con);
        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_done, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        TextView txt = dialogView.findViewById(R.id.total);
        txt.setText("اجمالى المبلغ" + " " + total);
        Button btnverify = (Button) dialogView.findViewById(R.id.btn_ok);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
}
