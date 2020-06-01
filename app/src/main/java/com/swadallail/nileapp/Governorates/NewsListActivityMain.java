package com.swadallail.nileapp.Governorates;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.RecyclerItemClickListener;
import com.swadallail.nileapp.api.model.Governorates;
import com.swadallail.nileapp.api.model.ResultGovernorates;
import com.swadallail.nileapp.api.service.UserClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsListActivityMain extends AppCompatActivity {


    private NewsListAdapter adapter;

    public static final String KEY_HEADLINE="news_headline";
    public static final String KEY_DETAILS="news_details";
    public static final String KEY_PUBDATE = "news_pub_date";
    public static final String  KEY_IMGE = "news_imge";
    String currency;
    int j;
    int flag = 0;
    ProgressDialog dialog;
    private int GovernorateId;

    public static final String ID = "id";
    public static final String IDUSER = "iduser";
    public static final String NAME = "name";
    public static final String PHONE = "password";
    public static final String TITLE = "nama";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String SPACE = "space";
    public static final String PRICE = "price";
    public static final String ECHECKED = "echecked";

    ArrayList<String> myTitle = new ArrayList<String>();
    HashMap<String, Integer> iD = new HashMap<String, Integer>();





    private String url = "http://waynaqar.com/Earth/markers.php";

    public int flagInternet = 0;
    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categ);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // Bundle extras = this.getIntent().getExtras();
        // currency = extras.getString("numbers1");






        dialog = new ProgressDialog(NewsListActivityMain.this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        getGovernorates();



}




    void getAdapter()
    {

  //    if(flag == 1) {

            adapter = new NewsListAdapter(this, getData(myTitle));

            RecyclerView list = (RecyclerView) findViewById(R.id.newsList);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);


            list.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click


                            NewsItem item = (NewsItem) adapter.getItem(position);

                           String city = item.getHeadline();

                            GovernorateId = iD.get(city);

                            Intent intent1 = new Intent(NewsListActivityMain.this, com.swadallail.nileapp.Cities.NewsListActivityMain.class);
                            intent1.putExtra("GovernorateId", GovernorateId);

                            startActivityForResult(intent1,5);

                           // Toast.makeText(NewsListActivityMain.this, city, Toast.LENGTH_LONG).show();




                        }
                    })
            );





/*
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(NewsListActivity.this, "Last", Toast.LENGTH_LONG).show();


                   adapter = new NewsListAdapter(NewsListActivity.this, getData(myTitle,myName,myPhone,mySpace,myPrice,myEchecked,myLat,myLng,myUrl,myEcheckBox,myId,myDate,mycounteye,myDetails));
                    //  ListView list = (ListView) findViewById(R.id.newsList);
                    // list.setAdapter(adapter);
                    //list.setLayoutManager(new LinearLayoutManager(this));
                    list.setAdapter(adapter);

                }
            }
        });

        */





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // finish();

        if(resultCode == 5)
            finish();

    }

    private ArrayList<NewsItem> getData(ArrayList<String> A) {
        ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();



        for (int i = 0; i < A.size(); i++) {


            NewsItem item = new NewsItem();
            item.setHeadline(A.get(i));

            newsList.add(item);
        }
        return newsList;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }





    private void getGovernorates(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/Region/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi
        Call<ResultGovernorates> con = userclient.getGovernorates();
//للتنفيذ
        con.enqueue(new Callback<ResultGovernorates>() {
            @Override
            public void onResponse(Call<ResultGovernorates> call, retrofit2.Response<ResultGovernorates> response) {
                if (flagInternet == 1) {
                    Toast.makeText(NewsListActivityMain.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                     try {

                List<Governorates> governorates = response.body().getGovernorates();
                // for (int i = 0; i < governorates.size(); i++) {
                         if (governorates.size() == 0) {
                             dialog.dismiss();}
                         else {
                             for (int i = (governorates.size() - 1); i >= 0; i--) {
                                 int id = governorates.get(i).getGovernorateId();
                                 String Name = governorates.get(i).getGovernorateName();
                                 myTitle.add(Name);
                                 iD.put(Name, id);


                                 if (i == 0) {

                                     getAdapter();
                                     dialog.dismiss();
                                 }


                             }

                         }
               }
                 catch (Exception e)
                {

                    Toast.makeText(NewsListActivityMain.this, "error internet", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultGovernorates> call, Throwable t) {

                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(NewsListActivityMain.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }


            }
        });


    }



}