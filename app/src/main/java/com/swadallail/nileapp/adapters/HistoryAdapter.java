package com.swadallail.nileapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.Conditions;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.orderpro.OrderProgress;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context con ;
    Main<GetOrdersRes> mlist ;
    String rule ;
    public HistoryAdapter(Context con, Main<GetOrdersRes> mlist) {
        this.con = con;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.order.setText(mlist.data.get(position).getDescription());
        holder.hours.setText(""+mlist.data.get(position).getHours());
        holder.id.setText(mlist.data.get(position).getOrderId()+"");
        if (mlist.data.get(position).getStatus().equals("InProgress")){
            if(mlist.data.get(position).getState().equals("Picked")){
                holder.status.setText("تم استلام الطلب");
            }else {
                holder.status.setText("جارى استلام الطلب");
            }
        }else {
            holder.status.setText("انتهى");
        }
        Log.e("Image" , mlist.data.get(position).getImg());

        rule = SharedHelper.getKey(con, "role");
        if(rule.equals("WebClient")){
            holder.name.setText(mlist.data.get(position).getReprestiveName());
            holder.ratingBar.setRating(mlist.data.get(position).getReprestiveRate());
            holder.ratNum.setText(""+mlist.data.get(position).getReprestiveRate());
        }else{
            holder.name.setText(mlist.data.get(position).getOwnerName());
            holder.ratingBar.setRating(mlist.data.get(position).getOwnerRate());
            holder.ratNum.setText(""+mlist.data.get(position).getOwnerRate());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDe = new Intent(con , OrderProgress.class);
                goToDe.putExtra("image" , mlist.data.get(position).getImg());
                goToDe.putExtra("orderText" , mlist.data.get(position).getDescription());
                goToDe.putExtra("orderH" , mlist.data.get(position).getHours());
                if(rule.equals("WebClient")){
                    goToDe.putExtra("orderOwner" , mlist.data.get(position).getReprestiveName());
                }else{
                    goToDe.putExtra("orderOwner" , mlist.data.get(position).getOwnerName());
                }

                goToDe.putExtra("orderOwnerID" , mlist.data.get(position).getOwnerId());
                goToDe.putExtra("orderID" , mlist.data.get(position).getOrderId());
                goToDe.putExtra("orderTo" , mlist.data.get(position).getToAddress());
                goToDe.putExtra("orderFrom" , mlist.data.get(position).getFromAddress());
                goToDe.putExtra("orderTolat" , mlist.data.get(position).getToLat());
                goToDe.putExtra("orderTolng" , mlist.data.get(position).getToLng());
                goToDe.putExtra("orderFromlat" , mlist.data.get(position).getFromLat());
                goToDe.putExtra("orderFromlng" , mlist.data.get(position).getFromLng());
                con.startActivity(goToDe);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order , loc1 , loc2 , hours , name , ratNum , id , status;
        RatingBar ratingBar ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            id = itemView.findViewById(R.id.txt_orderid);
            ratingBar = itemView.findViewById(R.id.rate);
            order = itemView.findViewById(R.id.ordertext);
            loc1 = itemView.findViewById(R.id.loc_one);
            loc2 = itemView.findViewById(R.id.loc_two);
            hours = itemView.findViewById(R.id.hours);
            name = itemView.findViewById(R.id.txt_name);
            ratNum = itemView.findViewById(R.id.rate_num);
        }
    }
}
