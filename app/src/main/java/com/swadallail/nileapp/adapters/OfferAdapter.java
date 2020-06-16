package com.swadallail.nileapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.accept.AcceptOffers;
import com.swadallail.nileapp.data.GetOrdersRes;
import com.swadallail.nileapp.data.Main;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {
    Context con ;
    Main<GetOrdersRes> mlist ;

    public OfferAdapter(Context con, Main<GetOrdersRes> mlist) {
        this.con = con;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_offers_item, parent, false);
        return new OfferAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.MyViewHolder holder, int position) {
        holder.order.setText(mlist.data.get(position).getDescription());
        /*if(mlist.data.get(position).getImg() != null){
            Log.e("Image" , mlist.data.get(position).getImg());
            //Picasso.with(con).load(mlist.data.get(position).getImg()).into(holder.img);
        }else{
            holder.img.setImageResource(R.drawable.avatar_circle);
        }*/
        holder.ratingBar.setRating(mlist.data.get(position).getReprestiveRate());
        holder.rate.setText(mlist.data.get(position).getReprestiveRate()+"");
        holder.id.setText(mlist.data.get(position).getOrderId()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acceptoffer= new Intent( con , AcceptOffers.class);
                int oid = Integer.valueOf( mlist.data.get(position).getOrderId());
                acceptoffer.putExtra("orderId" , oid);
                con.startActivity(acceptoffer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order , rate , id ;
        RatingBar ratingBar ;
        ImageView img ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_repre);
            id = itemView.findViewById(R.id.txt_orderid);
            ratingBar = itemView.findViewById(R.id.rb_repre);
            order = itemView.findViewById(R.id.ordertext);
            rate = itemView.findViewById(R.id.txt_rate);
        }
    }
}
