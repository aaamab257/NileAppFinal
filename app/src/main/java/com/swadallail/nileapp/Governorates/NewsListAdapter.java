package com.swadallail.nileapp.Governorates;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.R;

import java.util.ArrayList;
import java.util.Locale;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private ArrayList<NewsItem> listData;
    private LayoutInflater layoutInflater;
    private Context context;
   private Bitmap pi;
    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public NewsListAdapter(Context context, ArrayList<NewsItem> listData) {
        this.listData = listData;
        this.context = context;
      //  imageLoader = CustomVolleyRequest.getInstance(getApplicationContext())
        //        .getImageLoader();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(!Locale.getDefault().getLanguage().toString().equals("ar"))
             view = LayoutInflater.from(context).inflate(R.layout.snippet_categ_list_row_eng,parent,false);
        else
             view = LayoutInflater.from(context).inflate(R.layout.snippet_categ_list_row,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city.setText(listData.get(position).getHeadline());

       // if(!Locale.getDefault().getLanguage().toString().equals("ar"))
          //  holder.img.setImageResource(R.drawable.ic_keyboard_arrow_right);

        //holder.img.setImageDrawable(getDrawable(R.drawable.android_image3));

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
      public   TextView city;
     // public ImageView img;


        public ViewHolder(View convertView) {
            super(convertView);

            city =  convertView.findViewById(R.id.city);
           // img = convertView.findViewById(R.id.imageView);




        }
    }







}