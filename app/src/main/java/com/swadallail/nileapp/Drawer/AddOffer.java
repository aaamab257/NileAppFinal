package com.swadallail.nileapp.Drawer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.swadallail.nileapp.R;

public class AddOffer extends AppCompatActivity {
    TextView phone, whatsapp, email;
    String countryUser,countryU,phoneU,whatsappU,emailU;
    String tag_json_obj = "json_obj_req";
    int flagInternet=0;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 56789;
    //private String urlc = "http://192.168.43.248/Users/call.php";
    private String urlc = "http://waynaqar.com/Users/call.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addoffer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      //  Bundle extras = this.getIntent().getExtras();
       // countryUser = extras.getString("numbers1");

       // getCall();


    }


    /*

    private void getCall() {
        StringRequest strReq = new StringRequest(Request.Method.POST, urlc, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                if (flagInternet == 1) {
                    Toast.makeText(CallMe.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("infocall");
                    JSONArray jsonArray = new JSONArray(getObject);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String mobile = jsonObject.getString("phone");
                        String loc = jsonObject.getString("location");
                        String whatsa = jsonObject.getString("whatsapp");
                        String emai = jsonObject.getString("email");

                        if (loc.equals(countryUser)) {
                            phone = (TextView) findViewById(R.id.t1);
                            whatsapp = (TextView) findViewById(R.id.t2);
                            email = (TextView) findViewById(R.id.t4);
                            phone.setText(mobile);
                            whatsapp.setText(whatsa);
                            email.setText(emai);

                        }

                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                if (flagInternet == 0) {
                    Toast.makeText(CallMe.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

*/


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


}
