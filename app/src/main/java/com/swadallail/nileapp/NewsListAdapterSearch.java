package com.swadallail.nileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.Cities.NewsItem;

import java.util.ArrayList;


public class NewsListAdapterSearch extends RecyclerView.Adapter<NewsListAdapterSearch.ViewHolder> {

    private ArrayList<NewsItemS> listData;
    private LayoutInflater layoutInflater;
    private Context context;
   private Bitmap pi;
    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public NewsListAdapterSearch(Context context, ArrayList<NewsItemS> listData) {
        this.listData = listData;
        this.context = context;
      //  imageLoader = CustomVolleyRequest.getInstance(getApplicationContext())
        //        .getImageLoader();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
            view = LayoutInflater.from(context).inflate(R.layout.list_search,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.shopetype.setText(listData.get(position).getShopeType());
        holder.shopeName.setText(listData.get(position).getShopeName());
      /*  holder.pubDate.setText(listData.get(position).getPubDate());
        holder.date.setText(listData.get(position).getDate());
        String Url = listData.get(position).getImg();
        Picasso.with(context).load(Url).into(holder.img);
*/

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }



    public Object getItem(int position) {
        return listData.get(position);
    }





    static class ViewHolder extends RecyclerView.ViewHolder {
      public   TextView shopetype;
        public   TextView shopeName;


        public ViewHolder(View convertView) {
            super(convertView);

            shopetype =  convertView.findViewById(R.id.shopetype);
            shopeName =  convertView.findViewById(R.id.shopename);

        }
    }







}