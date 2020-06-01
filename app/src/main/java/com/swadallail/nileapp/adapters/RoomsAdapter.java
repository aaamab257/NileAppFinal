package com.swadallail.nileapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.chatpage.ChatActivity;
import com.swadallail.nileapp.data.ChatMainResponse;
import com.swadallail.nileapp.data.ChatResponse;
import com.swadallail.nileapp.data.ChatUsersResponse;
import com.swadallail.nileapp.data.MainResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {
    Context con ;
    ArrayList<ChatResponse<ChatUsersResponse>> list ;

    public RoomsAdapter(Context con, ArrayList<ChatResponse<ChatUsersResponse>> list) {
        this.con = con;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.username.setText(list.get(position).data.get(position).users.get(position).getUserName());
        holder.username.setText(list.get(position).users.get(0).getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goChat = new Intent(con , ChatActivity.class);
                //goChat.putExtra("chatId",list.get(position).data.get(position).users.get(position).getChatId());
                goChat.putExtra("chatId",list.get(position).chatId);
                con.startActivity(goChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.txtMessageOwner);
        }
    }
}
