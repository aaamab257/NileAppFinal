package com.swadallail.nileapp;

import android.Manifest;
import android.animation.FloatEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Credentials;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.signalr.HubConnectionBuilder;
import com.smartarmenia.dotnetcoresignalrclientjava.HubConnection;
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnection;
import com.smartarmenia.dotnetcoresignalrclientjava.WebSocketHubConnectionP2;
import com.swadallail.nileapp.Drawer.CallMe;
import com.swadallail.nileapp.Drawer.ReportProblem;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceCity;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceCityId;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferenceName;
import com.swadallail.nileapp.SaveSharedPreference.SaveSharedPreferencePhone;
import com.swadallail.nileapp.Services.ChatService;
import com.swadallail.nileapp.api.model.Cities;
import com.swadallail.nileapp.api.model.Data;
import com.swadallail.nileapp.api.model.GetShopByTypeAndCityId;
import com.swadallail.nileapp.api.model.Login;
import com.swadallail.nileapp.api.model.ResultCities;
import com.swadallail.nileapp.api.model.ResultGetShopByTypeAndCityId;
import com.swadallail.nileapp.api.model.ResultShopType;
import com.swadallail.nileapp.api.model.ShopeType;
import com.swadallail.nileapp.api.service.UserClient;
import com.swadallail.nileapp.chatpage.ChatActivity;
import com.swadallail.nileapp.chatroomspage.ChatRooms;
import com.swadallail.nileapp.data.GetNewOrdersData;
import com.swadallail.nileapp.data.LocationBody;
import com.swadallail.nileapp.data.Main;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.delegete.DelegeteHome;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.history.HistoryOreders;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;
import com.swadallail.nileapp.offers.OffersPage;
import com.swadallail.nileapp.orderpage.MakeOrder;
import com.swadallail.nileapp.uploaddoc.UploadData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.Request;
import okhttp3.internal.platform.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.defaultValue;
import static android.R.attr.publicKey;
import static android.R.attr.readPermission;
import static android.R.attr.start;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    GoogleMap gMap;
    CameraPosition cameraPosition;
    LatLng center, latLng, movelatlng;
    Marker mCurrLocation, locationMarker;
    private Circle mCircle;
    Button call, move, chat;
    boolean mBound = false;
    int flagInfo = 0; //فلاق عرض إظهار النافذه التي بالأسف كامل أو إخفائها كامله أثناء كتابة البحث
    int flagMarkerSearch = 0;
    int flagMarkerShow = 0;  //فلاق عرض دبوس البحث لوحده فقط
    int flagContentInfo = 0;  //فلاق عرض او إخفاء محتويات النافذة التي بالأسف
    Marker lastMarker;


    LinearLayout linearSearch, info;
    EditText edittext;
    Button cross;

    NewsListAdapterSearch adapter;

    LocationManager mLocationManager;
    MarkerOptions markerOptions = new MarkerOptions();

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mdrawerToggle;
    private NavigationView mnavigationView;


    private int lastIdMarker;
    private String lastPhoneMarker;
    Boolean chatEnabled;
    String shopID;

    private GroundOverlay mRedPoint = null;

    Spinner spinner, spinnerCity;
    RecyclerView list;
    int flagInternet = 0;
    ProgressDialog dialog;
    LinearLayout prog;
    private static Animation shakeAnimation;
    int CityId = -1;
    int GovernId = -1;
    String GovernName = " ";
    String CityName = " ";
    double CityLat, CityLng;


    ArrayList<String> myGovern = new ArrayList<String>();
    HashMap<String, Integer> myGovernId = new HashMap<String, Integer>();


    ArrayList<String> myCity = new ArrayList<String>();
    // HashMap<String, Integer> myCityId = new HashMap<String, Integer>();
    HashMap<String, LatLng> myCityLatLng = new HashMap<String, LatLng>();


    HashMap<LatLng, Integer> MarkerIdRemove = new HashMap<LatLng, Integer>();
    HashMap<Integer, String> MarkerIdName = new HashMap<Integer, String>();
    HashMap<Integer, String> MarkerIdPhone = new HashMap<Integer, String>();
    HashMap<Integer, String> MarkerIdshopTypeName = new HashMap<Integer, String>();


    HashMap<Integer, Integer> myShopId = new HashMap<Integer, Integer>();
    // HashMap<LatLng, Integer> myShopId = new HashMap<LatLng, Integer>();
    // HashMap<Integer, String> myShopeType = new HashMap<Integer, String>();
    //HashMap<Integer, String> myShopeName = new HashMap<Integer, String>();
    //HashMap<Integer, String> myShopPhone = new HashMap<Integer, String>();


    ArrayList<Integer> myShopeId = new ArrayList<Integer>();
    ArrayList<String> myShopeType = new ArrayList<String>();
    ArrayList<String> myShopeName = new ArrayList<String>();
    ArrayList<String> myShopePhone = new ArrayList<String>();
    ArrayList<LatLng> myShopeLatLng = new ArrayList<LatLng>();
    ArrayList<Boolean> chats = new ArrayList<>();
    ArrayList<String> UserID = new ArrayList<>();

    double mlat, mlong;
    HubConnection connection;
    Button order;
    Location myLocation;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //  mapView.getMapAsync(MainActivity.this);


        call = (Button) findViewById(R.id.btn_call);
        order = findViewById(R.id.btn_order);
        move = (Button) findViewById(R.id.btn_move);
        chat = findViewById(R.id.btn_chat);
        prog = (LinearLayout) this.findViewById(R.id.prog);
        edittext = (EditText) findViewById(R.id.search);
        cross = (Button) findViewById(R.id.cross);
        linearSearch = (LinearLayout) this.findViewById(R.id.linearSearch);
        info = (LinearLayout) this.findViewById(R.id.infow);
        list = (RecyclerView) findViewById(R.id.listViewSearch);



        String role = SharedHelper.getKey(MainActivity.this, "role");
        if (role.equals("WebClient")) {
            order.setVisibility(View.VISIBLE);
        } else {
            order.setVisibility(View.GONE);
        }
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = SharedHelper.getKey(MainActivity.this, "role");
                if (role.equals("WebClient")) {
                    String pconfirm = SharedHelper.getKey(MainActivity.this, "phoneConfirm");
                    if (pconfirm.equals("true")) {
                        Intent makeOrder = new Intent(MainActivity.this, MakeOrder.class);
                        startActivity(makeOrder);
                    } else {
                        Intent gotoPhone = new Intent(MainActivity.this, AuthPhone.class);
                        startActivity(gotoPhone);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "هذه الخدمة غير متاحة لحسابكم", Toast.LENGTH_LONG).show();
                }

            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("WrongConstant")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                if (granted()) {
                    try {
                        Location myLocation = getLastKnownLocation();
                        mlat = myLocation.getLatitude();
                        mlong = myLocation.getLongitude();
                        sendLocation(mlat, mlong);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "قم بقبول الصلاحية لتحديد موقعك", Toast.LENGTH_SHORT).show();
                    }

                }


                new AppUpdater(MainActivity.this)
                        .setUpdateFrom(UpdateFrom.JSON)
                        .setUpdateJSON("https://www.nileappco.com/api/Version/CurrentVersion")
                        .setTitleOnUpdateAvailable(getResources().getString(R.string.update))
                        .setContentOnUpdateAvailable(getResources().getString(R.string.new_version))
                        .setButtonUpdate(getResources().getString(R.string.update))
                        .setButtonDismiss(getResources().getString(R.string.cancel))
                        .setButtonDoNotShowAgain(getResources().getString(R.string.dont_show))
                        .setCancelable(false)
                        .start();


                Intent intent = getIntent();
                if (null != intent) {
                    try {
                        int flag = intent.getIntExtra("numbers3", defaultValue);
                        double lat = intent.getDoubleExtra("numbers1", defaultValue);
                        double lng = intent.getDoubleExtra("numbers2", defaultValue);

                        if (flag == 111)
                            getcamera(new LatLng(lat, lng), 16);

                    } catch (Exception e) {

                    }


                }


                gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        // if overlay already exists - remove it
                        // if (mRedPoint != null) {
                        //   mRedPoint.remove();
                        //}

                        // if(mCircle !=null)
                        // mCircle.remove();

                        try {

                            double currentLat = gMap.getCameraPosition().target.latitude;
                            double currentLong = gMap.getCameraPosition().target.longitude;


                            LatLng latLng = new LatLng(mlat, mlong);

                            String keyEnter = edittext.getText().toString();
                            if (keyEnter.length() <= 0)
                                edittext.setCursorVisible(false);

                            if (info.getVisibility() == View.GONE)
                                info.setVisibility(View.VISIBLE);


                            // if(mCircle == null ){
                            clearSearch();
                            if (flagMarkerShow != 1) {
                                //   gMap.clear();
                                //////
                                drawMarkerWithCircle(latLng);
                                prog.setVisibility(View.VISIBLE);
                                getMarker(GovernId, mlat, mlong);
                            }

                        } catch (Exception e) {

                        }


                    }
                });


                gMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {

                        try {

                            if (flagMarkerShow != 1) {

                                gMap.clear();
                                deleteAraayMarker();
                            }

                        } catch (Exception e) {


                        }

                    }


                });


                gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {


                        try {


                        } catch (Exception e) {

                        }

                        //  if (mRedPoint != null) {
                        //     mRedPoint.remove();
                        //}

                        //        double   currentLat = gMap.getCameraPosition().target.latitude;
                        //      double  currentLong = gMap.getCameraPosition().target.longitude;

//                        LatLng latLng  = new LatLng(currentLat, currentLong);


                        // if(mCircle == null ){
                        //                          drawMarkerWithCircle(latLng);

                        //  getMarker(GovernId,currentLat,currentLong);
                        //}else{
                        //   updateMarkerWithCircle(latLng);
                        // }


                        /*
                        double   currentLat = gMap.getCameraPosition().target.latitude;
                        double  currentLong = gMap.getCameraPosition().target.longitude;

                        mRedPoint = showRipples(new LatLng(currentLat, currentLong), Color.RED);
*/
                        //   MarkerOptions markerOptions = new MarkerOptions().position(gMap.getCameraPosition().target)
                        //         .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_up));

                        // gMap.addMarker(markerOptions);


                    }

                });


                //double   currentLat = gMap.getCameraPosition().target.latitude;
                //double  currentLong = gMap.getCameraPosition().target.longitude;
                // gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLong), 16));


                mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mdrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mdrawerLayout, R.string.open, R.string.close);
                mdrawerLayout.addDrawerListener(mdrawerToggle);
                mdrawerToggle.syncState();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mnavigationView = (NavigationView) findViewById(R.id.nav_view);


                try {
                    if (SaveSharedPreferencePhone.getUserName(MainActivity.this).length() != 0) {
                        // get menu from navigationView
                        Menu menu = mnavigationView.getMenu();

                        // find MenuItem you want to change
                        //MenuItem sign = menu.findItem(R.id.nav_item_eight);

                        // set new title to the MenuItem
                        //sign.setTitle("تسجيل الخروج");

                        View header = mnavigationView.getHeaderView(0);
                        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
                        TextView nameU = (TextView) header.findViewById(R.id.nav_header_name);
                        TextView phoneU = (TextView) header.findViewById(R.id.nav_header_phone);

                        String name = SaveSharedPreferenceName.getUserName(MainActivity.this);
                        String phone = SaveSharedPreferencePhone.getUserName(MainActivity.this);

                        nameU.setText(name);
                        phoneU.setText(phone);


                    }
                } catch (Exception e) {
                }


                mnavigationView.setNavigationItemSelectedListener(MainActivity.this);


            }


        });


        // Login();
        spinner = (Spinner) findViewById(R.id.govern);

        final String[] select_qualification = {getApplicationContext().getResources().getString(R.string.categories)};
        // Creating adapter for spinner
        ArrayAdapter<String> GovernAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select_qualification);

        // Drop down layout style - list view with radio button
        GovernAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(GovernAdapter);


       /*
        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }

        MyAdapter myAdapter = new MyAdapter(MainActivity.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);
*/

        // Creating adapter for spinner


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                String item = parentView.getItemAtPosition(position).toString();
                if (myGovernId.size() != 0) {
                    try {

                        double currentLat = gMap.getCameraPosition().target.latitude;
                        double currentLong = gMap.getCameraPosition().target.longitude;

                        prog.setVisibility(View.VISIBLE);
                        gMap.clear();

                        if (item.equals("الفئات") || item.equals("Categories")) {

                            GovernName = item;
                            GovernId = -1;

                            getMarker(GovernId, mlat, mlong);

                        } else {

                            GovernId = myGovernId.get(item);
                            GovernName = item;

                            getMarker(GovernId, mlat, mlong);
                        }

                    } catch (Exception e) {

                    }
                }
                // Toast.makeText(parentView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();                //  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Your code
                    try {
                        if (myGovern.size() == 0) {
                            //     dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
                            //   View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.remove_border, null);
                            //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            // dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            //dialog.setContentView(view);

                            dialog = new ProgressDialog(com.swadallail.nileapp.MainActivity.this);
                            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);


                            allShoptype();
                        }

                    } catch (Exception e) {

                    }

                }

                return false;
            }
        };


        spinner.setOnTouchListener(spinnerOnTouch);


/////////////////


        final String[] select_city = {getApplicationContext().getResources().getString(R.string.city)};

        spinnerCity = (Spinner) findViewById(R.id.city);


        // Creating adapter for spinner
        ArrayAdapter<String> CityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, select_city);

        // Drop down layout style - list view with radio button
        CityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCity.setAdapter(CityAdapter);


        View.OnTouchListener spinnerOnTouchCity = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Your code
                    try {
                        if (myCity.size() == 0) {
                            //     dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black);
                            //   View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.remove_border, null);
                            //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            // dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            //dialog.setContentView(view);

                            dialog = new ProgressDialog(com.swadallail.nileapp.MainActivity.this);
                            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);


                            allCities();
                        } else {
                            spinnerCity.setAdapter(null);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, myCity);
                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            spinnerCity.setAdapter(dataAdapter);
                        }
                    } catch (Exception e) {

                    }

                }
                return false;
            }
        };


        spinnerCity.setOnTouchListener(spinnerOnTouchCity);


        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                try {

                    String item = parentView.getItemAtPosition(position).toString();

                    if (myCityLatLng.size() != 0 && (!item.equals("المدينة") && !item.equals("City"))) {

                        LatLng latLng = myCityLatLng.get(item);
                        flagMarkerShow = 0;
                        getcamera(latLng, 14);
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittext.setCursorVisible(true);
            }
        });


        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                try {
                    if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                            keyCode == EditorInfo.IME_ACTION_DONE ||
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        // event.getAction() == KeyEvent.ACTION_DOWN ||
                        // Perform action on key press
                        String keyEnter = edittext.getText().toString();

                        //double currentLat = gMap.getCameraPosition().target.latitude;
                        //double currentLong = gMap.getCameraPosition().target.longitude;
                        double currentLat = gMap.getMyLocation().getLatitude();
                        double currentLong = gMap.getMyLocation().getLongitude();
                        if (!keyEnter.equals("")) {

                            prog.setVisibility(View.VISIBLE);
                            linearSearch.setVisibility(View.VISIBLE);
                            // clearSearch();
                            getMarkerSearch(mlat, mlong, keyEnter);
                            edittext.setCursorVisible(false);
                            hideKeyboard(MainActivity.this);

                            if (info.getVisibility() == View.GONE)
                                info.setVisibility(View.VISIBLE);
                        }

                        return false;
                    }
                } catch (Exception e) {

                }

                return false;
            }
        });


        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(MainActivity.this, edittext.getText(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {


                try {

                    String keyEnter = edittext.getText().toString();
                    double currentLat = gMap.getCameraPosition().target.latitude;
                    double currentLong = gMap.getCameraPosition().target.longitude;

                    if (keyEnter.equals("")) {
                        clearSearch();
                        linearSearch.setVisibility(View.INVISIBLE);
                        cross.setVisibility(View.INVISIBLE);

                        if (info.getVisibility() == View.GONE)
                            info.setVisibility(View.VISIBLE);
                    } else {

                        if (info.getVisibility() == View.VISIBLE)
                            info.setVisibility(View.GONE);

                        prog.setVisibility(View.VISIBLE);
                        linearSearch.setVisibility(View.VISIBLE);
                        cross.setVisibility(View.VISIBLE);
                        clearSearch();
                        getMarkerSearch(mlat, mlong, keyEnter);
                    }

                } catch (Exception e) {

                }


            }
        });
    }

    private void sendLocation(double lat, double lng) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.nileappco.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface userclient = retrofit.create(ApiInterface.class);
        LocationBody bo = new LocationBody(lat, lng);
        String Token = "Bearer " + SharedHelper.getKey(MainActivity.this, "token");
        Call<MainResponse> call = userclient.CurrentLocation(Token, bo);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                //Toast.makeText(MainActivity.this, "تم تسجيل الموقع بنجاح", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

            }
        });
    }


    private Boolean granted() {
        try {
            boolean isGranted = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (isGranted) {

                buildAlertMessageNoGps(14);
                gMap.setMyLocationEnabled(true);
            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  getcamera(new LatLng(26.55656997, 31.70065759), 14);

                gMap.setMyLocationEnabled(true);
                buildAlertMessageNoGps(14);
            }

        }


    }

    /*private void Login() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/User/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi

        Login login = new Login("Admin", "Admin");
        Call<Data> con = userclient.Login(login);

        // Call<ResultModels> con = userclient.getCustomers();
//للتنفيذ
        con.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                try {


                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "نجح" + response.body().toString(), Toast.LENGTH_LONG).show();
                        //  token = response.body().gettToken();

                    } else {

                        Toast.makeText(MainActivity.this, "no", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(MainActivity.this, "error internet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }


        });


    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mdrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.add) {
            try {
                if (SharedHelper.getKey(MainActivity.this, "token") != null) {
                    Intent intent1 = new Intent(MainActivity.this, PlacePicker.class);
                    startActivity(intent1);
                }

            } catch (Exception e) {
            }

        } else if (id == R.id.chatRooms) {
            try {

                Intent intent2 = new Intent(MainActivity.this, ChatRooms.class);
                startActivity(intent2);


            } catch (Exception e) {
            }

        }//noinspection SimplifiableIfStatement
        // else if (id == R.id.menuSearch) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.offers) {
            String role = SharedHelper.getKey(MainActivity.this, "role");
            if (role.equals("WebClient")) {
                String pconfirm = SharedHelper.getKey(MainActivity.this, "phoneConfirm");
                if (pconfirm.equals("true")) {
                    Intent offerpage = new Intent(MainActivity.this, OffersPage.class);
                    startActivity(offerpage);
                } else {
                    Intent gotoPhone = new Intent(MainActivity.this, AuthPhone.class);
                    startActivity(gotoPhone);
                }

            } else {
                Toast.makeText(this, "لا يمكنك زيارة العروض", Toast.LENGTH_LONG).show();
            }


            //  Toast.makeText(this,"لايمكنك مشاركة النسخة التجريبية",Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.history) {

            String pconfirm = SharedHelper.getKey(MainActivity.this, "phoneConfirm");
            if (pconfirm.equals("true")) {
                Intent offerpage = new Intent(MainActivity.this, HistoryOreders.class);
                startActivity(offerpage);
            } else {
                Intent gotoPhone = new Intent(MainActivity.this, AuthPhone.class);
                startActivity(gotoPhone);
            }


            //  Toast.makeText(this,"لايمكنك مشاركة النسخة التجريبية",Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.newOrders) {
            String role = SharedHelper.getKey(MainActivity.this, "role");
            if (role.equals("Representive")) {
                String pconfirm = SharedHelper.getKey(MainActivity.this, "phoneConfirm");
                if (pconfirm.equals("true")) {
                    Intent goToOrderPage = new Intent(MainActivity.this, DelegeteHome.class);
                    startActivity(goToOrderPage);
                } else {
                    Intent gotoPhone = new Intent(MainActivity.this, AuthPhone.class);
                    startActivity(gotoPhone);
                }
            } else {
                Toast.makeText(this, "لا يمكنك زيارة الطلبات الجديدة", Toast.LENGTH_LONG).show();
            }


            //  Toast.makeText(this,"لايمكنك مشاركة النسخة التجريبية",Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.join_asDelivery) {
            String role = SharedHelper.getKey(MainActivity.this, "role");
            if (role.equals("WebClient")) {
                Intent goToRegis = new Intent(MainActivity.this, UploadData.class);
                startActivity(goToRegis);
            } else {
                Toast.makeText(this, "تم تسجيل حسابكم كمندوب بالفعل", Toast.LENGTH_LONG).show();
            }


            //  Toast.makeText(this,"لايمكنك مشاركة النسخة التجريبية",Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.nav_item_four) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String message = "\nنايل آب ليست مجرد خريطة  *https://play.google.com/store/apps/details?id=com.swadallail.nileapp* \n\n";

            i.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(i, "choose one"));

            //  Toast.makeText(this,"لايمكنك مشاركة النسخة التجريبية",Toast.LENGTH_SHORT).show();


        } else if (item.getItemId() == R.id.nav_item_five) {
            Intent intent1 = new Intent(MainActivity.this, CallMe.class);
            startActivity(intent1);
        } else if (item.getItemId() == R.id.nav_item_six) {
            Intent intent2 = new Intent(MainActivity.this, ReportProblem.class);
            startActivity(intent2);
            // }else if (item.getItemId() == R.id.nav_item_seven) {
            //////////////////////////
        } else if (item.getItemId() == R.id.logout) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_logout, null);
            dialogBuilder.setView(dialogView);
            AlertDialog alertDialog = dialogBuilder.create();

            Button logout = dialogView.findViewById(R.id.btn_ok);
            Button no = dialogView.findViewById(R.id.btn_cancel);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SharedHelper.putKey(MainActivity.this, "token", "");
                    SharedHelper.putKey(MainActivity.this, "UserName", "NONE");
                    SharedHelper.putKey(MainActivity.this, "name", "NONE");
                    SharedHelper.putKey(MainActivity.this, "picUrl", "NONE");//isLoged
                    SharedHelper.putKey(MainActivity.this, "isLoged", "no");
                    SharedHelper.putKey(MainActivity.this, "token", "NONE");
//                    chatService = new ChatService() ;
                    //chatService.getToken(MainActivity.this, "none" , "none");

                    alertDialog.dismiss();
                    SharedHelper.clearSharedPreferences(MainActivity.this);
                    MainActivity.this.finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();
            // }else if (item.getItemId() == R.id.nav_item_seven) {
            //////////////////////////
        }


        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    public void current(int x) {
        try {
            // buildAlertMessageNoGps(x);
            Location myLocation = getLastKnownLocation();
            double latti = myLocation.getLatitude();
            double longi = myLocation.getLongitude();
            latLng = new LatLng(latti, longi);
            getcamera(latLng, x);
            //  addMarker(latLng, getResources().getString(R.string.my_place));
        } catch (Exception e) {
            final LatLng mapTargetLatLng = gMap.getCameraPosition().target;
            getcamera(mapTargetLatLng, x);


            //addMarker(mapTargetLatLng, getResources().getString(R.string.my_place));
        }


    }


    private void getcamera(LatLng latlng, int zoomGet) {
        try {
            center = latlng;
            cameraPosition = new CameraPosition.Builder().target(center).zoom(zoomGet).build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } catch (Exception e) {
        }

    }


    @SuppressLint("WrongConstant")
    protected void buildAlertMessageNoGps(int x) {
        try {
            final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(this.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                         /*   gMap.setStyle(Style, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                                    enableLocationPlugin(style, x);
                                }

                            });
                            */

                current(x);


            } else {
                getcamera(new LatLng(26.55656997, 31.70065759), 14);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // builder.setMessage("Please Turn ON your GPS Connection")
                builder.setMessage(getResources().getString(R.string.gps))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {

                                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {

        }

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


    @Override
    public void onLocationChanged(Location location) {

        //remove previous current location marker and add new one at current

        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocation = gMap.addMarker(markerOptions);

        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void hitLocationApi(Location latLng) {
        //Toast.makeText(MainActivity.this, latLng.getLatitude() + "", Toast.LENGTH_LONG).show();
    }


    private GroundOverlay showRipples(LatLng latLng, int color) {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setSize(500, 500);
        d.setColor(color);
        d.setStroke(0, Color.TRANSPARENT);

        final Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
                , d.getIntrinsicHeight()
                , Bitmap.Config.ARGB_8888);

        // Convert the drawable to bitmap
        final Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        // Radius of the circle for current zoom level and latitude (because Earth is sphere at first approach)
        double meters_to_pixels = (Math.cos(gMap.getCameraPosition().target.latitude * Math.PI / 180) * 2 * Math.PI * 6378137) / (256 * Math.pow(2, gMap.getCameraPosition().zoom));
        final int radius = (int) (meters_to_pixels * getResources().getDimensionPixelSize(R.dimen.ripple_radius));

        // Add the circle to the map
        final GroundOverlay circle = gMap.addGroundOverlay(new GroundOverlayOptions()
                .position(latLng, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(bitmap)));

        // Prep the animator
        PropertyValuesHolder radiusHolder = PropertyValuesHolder.ofFloat("radius", 1, radius);
        PropertyValuesHolder transparencyHolder = PropertyValuesHolder.ofFloat("transparency", 0, 1);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setValues(radiusHolder, transparencyHolder);
        valueAnimator.setDuration(1000);
        valueAnimator.setEvaluator(new FloatEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedRadius = (float) valueAnimator.getAnimatedValue("radius");
                float animatedAlpha = (float) valueAnimator.getAnimatedValue("transparency");
                circle.setDimensions(animatedRadius * 2);
                circle.setTransparency(animatedAlpha);

            }
        });

        // start the animation
        valueAnimator.start();

        return circle;
    }


    public void move(View v) {

        try {

            if (movelatlng.latitude != 0 && movelatlng.longitude != 0) {
                double latitudee = movelatlng.latitude;
                double longitudee = movelatlng.longitude;
                //  String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                //   MainActivity.this.startActivity(intent);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitudee + "," + longitudee);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        } catch (Exception e) {

            Toast.makeText(MainActivity.this, "الرجاء قم بتفعيل قوقل ماب على الموبايل", Toast.LENGTH_LONG).show();

        }


    }


    public void call(View v) {

        try {

            //   String phone = MarkerIdPhone.get(lastIdMarker);

            String phone = lastPhoneMarker;

            if (phone.length() <= 0) {
                //call.setEnabled(false);
            } else {
                call.setEnabled(true);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:" + phone;
                callIntent.setData(Uri.parse(temp));
                startActivity(callIntent);
            }

        } catch (Exception e) {

        }


        // call.setEnabled(false);
    }


    public void cross(View v) {

        try {

            clearSearch();
            edittext.setText("");
            linearSearch.setVisibility(View.INVISIBLE);
            cross.setVisibility(View.INVISIBLE);
            if (info.getVisibility() == View.GONE)
                info.setVisibility(View.VISIBLE);

            if (flagMarkerShow == 1) {
                createMarkerAfterSearch();

            }
        } catch (Exception e) {


        }


    }


    public void allShoptype() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/Shop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi
        Call<ResultShopType> con = userclient.geShopType();
//للتنفيذ
        con.enqueue(new Callback<ResultShopType>() {
            @Override
            public void onResponse(Call<ResultShopType> call, Response<ResultShopType> response) {
                try {

                    // myTitle.add("الفئة");
                    List<ShopeType> shoptype = response.body().getShopeType();
                    //for (int i = 0; i < customers.size(); i++) {
                    if (shoptype.size() == 0) {
                        dialog.dismiss();
                    } else {
                        // for (int i = (shoptype.size() - 1); i >= 0; i--) {

                        String nameOne = getApplicationContext().getResources().getString(R.string.categories);
                        myGovern.add(nameOne);

                        for (int i = 0; i < shoptype.size(); i++) {
                            int id = shoptype.get(i).getShopTypeId();
                            String Name = shoptype.get(i).getShopTypeName();


                            myGovern.add(Name);
                            myGovernId.put(Name, id);


                            if (i == (shoptype.size() - 1)) {

                                shoptype.clear();
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, myGovern);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinner.setAdapter(dataAdapter);
                                dialog.dismiss();
                            }
                        }
                    }

                } catch (Exception e) {

                    Toast.makeText(MainActivity.this, "error internet", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultShopType> call, Throwable t) {
                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(com.swadallail.nileapp.MainActivity.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }

            }
        });

    }


    public void allCities() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/Region/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


        Call<ResultCities> con = userclient.getCities(null);
//للتنفيذ
        con.enqueue(new Callback<ResultCities>() {
            @Override
            public void onResponse(Call<ResultCities> call, Response<ResultCities> response) {
                try {

                    // myTitle.add("الفئة");
                    List<Cities> city = response.body().getCities();
                    //for (int i = 0; i < customers.size(); i++) {
                    if (city.size() == 0) {
                        dialog.dismiss();
                    } else {

                        String nameOne = getApplicationContext().getResources().getString(R.string.city);
                        myCity.add(nameOne);


                        for (int i = (city.size() - 1); i >= 0; i--) {
                            int id = city.get(i).getCityId();
                            String Name = city.get(i).getCityName();
                            double lat = city.get(i).getLat();
                            double lng = city.get(i).getLng();

                            myCity.add(Name);
                            myCityLatLng.put(Name, new LatLng(lat, lng));


                            if (i == 0) {

                                city.clear();
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, myCity);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spinnerCity.setAdapter(dataAdapter);
                                dialog.dismiss();
                            }
                        }
                    }

                } catch (Exception e) {

                    Toast.makeText(MainActivity.this, "error internet", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultCities> call, Throwable t) {
                if (flagInternet == 0) {
                    dialog.dismiss();
                    Toast.makeText(com.swadallail.nileapp.MainActivity.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }

            }
        });

    }


    int getMarkerId(LatLng l) {
        int i = MarkerIdRemove.get(l);
        return i;
    }


    private void addMarker(final int id, final String name, final String phone, LatLng latlng, final String TypeName) {
        markerOptions.position(latlng);
        markerOptions.title(name);
        MarkerIdRemove.put(latlng, id);
        MarkerIdName.put(id, name);
        MarkerIdPhone.put(id, phone);
        MarkerIdshopTypeName.put(id, TypeName);


        //  DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        //df.setMaximumFractionDigits(340);
        // markerOptions.snippet(name);

        //   markerOptions.snippet(Currency + price);
        //  IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        // BitmapDescriptorFactory.fromResource(R.drawable.organemarker);
        //Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this,R.drawable.organemarker);
        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.organemarker);
        //Icon icon = iconFactory.fromResource(R.drawable.organemarker);
        // markerOptions.icon(icon);
        gMap.addMarker(markerOptions);
        locationMarker = gMap.addMarker(markerOptions);
        // locationMarker.setIcon(icon);
        // IconGenerator iconFactory = new IconGenerator(this);
        //locationMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(Currency + df.format(price) + "\n" + echd)));
        //locationMarker.setZIndex(idF);
        // gMap.selectMarker(locationMarker);
        // locationMarker.showInfoWindow();


        // Adding and showing marker while touching the GoogleMap

        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                try {

                    lastMarker = marker;

                    int id = getMarkerId(marker.getPosition());
                    lastIdMarker = id;
                    movelatlng = marker.getPosition();
                    String nameshop = MarkerIdName.get(id);
                    String phone = MarkerIdPhone.get(id);
                    String shopTypeName = MarkerIdshopTypeName.get(id);

                    lastPhoneMarker = phone;

                    TextView egovername = (TextView) findViewById(R.id.govername);
                    TextView eshopname = (TextView) findViewById(R.id.shopname);

                    egovername.setText(shopTypeName);
                    eshopname.setText(nameshop);
                    /*chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (eshopname.getText().equals("")) {
                                Toast.makeText(MainActivity.this, "رجاء قم باختيار اين تذهب اولا", Toast.LENGTH_SHORT).show();
                            } else {
                                //startActivity(new Intent(MainActivity.this, ChatActivity.class));
                            }
                        }
                    });*/

                    if (flagMarkerShow != 1)  // لم يكن فلاق البحث شغال اخفي محتويات النافذه التي بالأسفل
                        flagContentInfo = 1;

                    /////
                    // flagMarkerShow = 1;
                    ////////


                    //if (flagInfo == 0) {
                    //  call.setEnabled(true);
                    move.setEnabled(true);
                    flagInfo = 1;
                    //}


                    if (phone.length() <= 0 || phone.equals("0")) {
                        call.setEnabled(false);
                    } else {
                        call.setEnabled(true);
                    }


                    //eMobile.setText(" ");
                } catch (Exception e) {
                }


            }

        });


        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                try {

                    lastMarker = marker;

                    int id = getMarkerId(marker.getPosition());
                    lastIdMarker = id;
                    movelatlng = marker.getPosition();
                    String nameshop = MarkerIdName.get(id);
                    String phone = MarkerIdPhone.get(id);
                    String shopTypeName = MarkerIdshopTypeName.get(id);

                    lastPhoneMarker = phone;

                    marker.showInfoWindow();

                    TextView egovername = (TextView) findViewById(R.id.govername);
                    TextView eshopname = (TextView) findViewById(R.id.shopname);

                    egovername.setText(shopTypeName);
                    eshopname.setText(nameshop);
                    /*chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (eshopname.getText().equals("")) {
                                Toast.makeText(MainActivity.this, "رجاء قم باختيار اين تذهب اولا", Toast.LENGTH_SHORT).show();
                            } else {
                                //startActivity(new Intent(MainActivity.this, ChatActivity.class));
                            }
                        }
                    });*/
                    if (flagMarkerShow != 1)  //إذا لم يكن فلاق البحث شغال اخفي محتويات النافذه التي بالأسفل
                        flagContentInfo = 1;

                    /////
                    //flagMarkerShow = 1;
                    ////////

                    // if (flagInfo == 0) {
                    //  call.setEnabled(true);
                    move.setEnabled(true);
                    flagInfo = 1;
                    //}


                    if (phone.length() <= 0 || phone.equals("0")) {
                        call.setEnabled(false);
                    } else {
                        call.setEnabled(true);
                    }


                } catch (Exception e) {
                }

                return true;
            }
        });
    }

    public void chat(View view) {
        String id = shopID;
        Boolean en = chatEnabled;
        if (en) {
            Intent openChat = new Intent(MainActivity.this, ChatActivity.class);
            openChat.putExtra("shopId", id);
            startActivity(openChat);
        } else {
            Toast.makeText(this, "هذا المستخدم غير متاح للمحادثة", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMarker(int shopeType, double lat, double lng) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nileappco.com/api/Shop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


        Call<ResultGetShopByTypeAndCityId> con = userclient.getShopByTypeAndCityId(shopeType, lat, lng);

//للتنفيذ
        con.enqueue(new Callback<ResultGetShopByTypeAndCityId>() {
            @Override
            public void onResponse(Call<ResultGetShopByTypeAndCityId> call, retrofit2.Response<ResultGetShopByTypeAndCityId> response) {
                if (flagInternet == 1) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                try {

                    if (response.isSuccessful()) {

                        List<GetShopByTypeAndCityId> shop = response.body().getGetShopByTypeAndCityId();

                        if (shop.size() == 0) {
                            prog.setVisibility(View.INVISIBLE);
                            if (mCircle != null)
                                mCircle.remove();
                        } else {
                            // for (int i = 0; i < governorates.size(); i++) {
                            for (int i = (shop.size() - 1); i >= 0; i--) {
                                int shopId = shop.get(i).getShopId();
                                String shopName = shop.get(i).getShopName();
                                String mobileNo = shop.get(i).getMobileNo();
                                LatLng latLng = new LatLng(shop.get(i).getLat(), shop.get(i).getLng());
                                String shopTypeName = shop.get(i).getShopTypeName();
                                /*Boolean chatbo = shop.get(i).getChatEnable();
                                String userId = shop.get(i).getUserId();

                                */
                                addMarker(shopId, shopName, mobileNo, latLng, shopTypeName);


                                if (i == 0) {


                                    prog.setVisibility(View.INVISIBLE);
                                    if (mCircle != null)
                                        mCircle.remove();
                                    shop.clear();
                                }


                            }

                        }
                    } else {

                        if (mCircle != null)
                            mCircle.remove();
                        prog.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "لايوجد إستجابة", Toast.LENGTH_LONG).show();


                    }
                } catch (Exception e) {

                    if (mCircle != null)
                        mCircle.remove();
                    prog.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResultGetShopByTypeAndCityId> call, Throwable t) {
                if (flagInternet == 0) {
                    if (mCircle != null)
                        mCircle.remove();
                    prog.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }


            }
        });


    }


    private void getMarkerSearch(double lat, double lng, String shopeName) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nileapp-001-site3.itempurl.com/api/Shop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        UserClient userclient = retrofit.create(UserClient.class);//لربط الكائن ref وربطه بالapi


        Call<ResultGetShopByTypeAndCityId> con = userclient.getShopByTypeAndCityIdFilter(lat, lng, shopeName);

//للتنفيذ
        con.enqueue(new Callback<ResultGetShopByTypeAndCityId>() {
            @Override
            public void onResponse(Call<ResultGetShopByTypeAndCityId> call, retrofit2.Response<ResultGetShopByTypeAndCityId> response) {
                if (flagInternet == 1) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_connection_restored), Toast.LENGTH_LONG).show();
                    flagInternet = 0;
                }

                try {

                    if (response.isSuccessful()) {

                        List<GetShopByTypeAndCityId> shop = response.body().getGetShopByTypeAndCityId();

                        if (shop.size() == 0) {
                            prog.setVisibility(View.INVISIBLE);
                            linearSearch.setVisibility(View.INVISIBLE);
                            // if(mCircle !=null)
                            //   mCircle.remove();
                            Toast.makeText(MainActivity.this, "لا يوجد نتيجة", Toast.LENGTH_LONG).show();

                        } else {
                            // for (int i = 0; i < governorates.size(); i++) {
                            clearSearch();
                            for (int i = (shop.size() - 1); i >= 0; i--) {
                                int shopId = shop.get(i).getShopId();
                                String shopName = shop.get(i).getShopName();
                                String mobileNo = shop.get(i).getMobileNo();
                                LatLng latLng = new LatLng(shop.get(i).getLat(), shop.get(i).getLng());
                                String shopTypeName = shop.get(i).getShopTypeName();
                                Boolean enabled = shop.get(i).getChatEnable();
                                String user_id = shop.get(i).getUserId();
                                Log.e("user_id", "" + user_id);
                                /*chat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!enabled){
                                            Toast.makeText(MainActivity.this, "المستخدم الذى قمت باختياره ليس لديه محادثة", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Intent openChat = new Intent(MainActivity.this , ChatActivity.class);
                                            openChat.putExtra("shopId",user_id);
                                            startActivity(openChat);
                                        }
                                    }
                                });*/
                                myShopeId.add(shopId);
                                myShopeName.add(shopName);
                                myShopeType.add(shopTypeName);
                                myShopePhone.add(mobileNo);
                                myShopeLatLng.add(latLng);
                                UserID.add(user_id);
                                chats.add(enabled);
                                //addMarker(shopId, shopName, mobileNo, latLng, shopTypeName);


                                if (i == 0) {
                                    prog.setVisibility(View.INVISIBLE);
                                    // if(mCircle !=null)
                                    //   mCircle.remove();
                                    //shop.clear();
                                    getAdapter();

                                }


                            }

                        }
                    } else {

                        if (mCircle != null)
                            mCircle.remove();
                        prog.setVisibility(View.INVISIBLE);
                        linearSearch.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "لايوجد إستجابة", Toast.LENGTH_LONG).show();


                    }
                } catch (Exception e) {

                    if (mCircle != null)
                        mCircle.remove();
                    prog.setVisibility(View.INVISIBLE);
                    linearSearch.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResultGetShopByTypeAndCityId> call, Throwable t) {
                if (flagInternet == 0) {
                    if (mCircle != null)
                        mCircle.remove();
                    prog.setVisibility(View.INVISIBLE);
                    linearSearch.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
                    flagInternet = 1;
                }


            }
        });


    }


    public void deleteAraayMarker() {
        try {
            MarkerIdRemove.clear();
            MarkerIdName.clear();
            MarkerIdPhone.clear();
            MarkerIdshopTypeName.clear();
        } catch (Exception e) {
        }

    }


    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);
    }

    private void drawMarkerWithCircle(LatLng position) {
        try {
            double radiusInMeters = 1500.0;   //نصف القطر
            int strokeColor = 0x44ff0000; //red trans outline
            //int strokeColor = 0xffff0000; //red outline
            //int shadeColor = 0x44ff0000; //opaque red fill


            // CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(3);
            CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).strokeColor(strokeColor).strokeWidth(3);

            mCircle = gMap.addCircle(circleOptions);
        } catch (Exception e) {
        }

    }


    private ArrayList<NewsItemS> getData(ArrayList<Integer> id, ArrayList<String> A, ArrayList<String> B, ArrayList<String> phone, ArrayList<LatLng> LatLng, ArrayList<Boolean> chatenabled, ArrayList<String> userID) {

        ArrayList<NewsItemS> newsList = new ArrayList<NewsItemS>();
        try {

            for (int i = 0; i < A.size(); i++) {
                NewsItemS item = new NewsItemS();
                item.setId(id.get(i));
                item.setShopeName(A.get(i));
                item.setShopeType(B.get(i));
                item.setPhone(phone.get(i));
                item.setLatLng(LatLng.get(i));
                item.setChatenable(chatenabled.get(i));
                item.setUserId(userID.get(i));
                newsList.add(item);
            }

        } catch (Exception e) {
        }


        return newsList;
    }


    void getAdapter() {

        //    if(flag == 1) {

        adapter = new NewsListAdapterSearch(this, getData(myShopeId, myShopeName, myShopeType, myShopePhone, myShopeLatLng, chats, UserID));

        //  RecyclerView list = (RecyclerView) findViewById(R.id.listViewSearch);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);


        list.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        try {

                            NewsItemS item = (NewsItemS) adapter.getItem(position);

                            linearSearch.setVisibility(View.INVISIBLE);
                            //  edittext.setText("");
                            // edittext.setCursorVisible(false);
                            hideKeyboard(MainActivity.this);

                            if (info.getVisibility() == View.GONE)
                                info.setVisibility(View.VISIBLE);


                            gMap.clear();
                            deleteAraayMarker();
                            clearSearch();

                            int shopId = item.getId();
                            String shopeType = item.getShopeType();
                            String shopeName = item.getShopeName();
                            String shopePhone = item.getPhone();
                            LatLng shopeLatLng = item.getLatLng();
                            Boolean schatEnabled = item.getChatenable();
                            String sshopID = item.getUserId();
                            Log.e("ALLITEMS", "" + item.getUserId());
                            Log.e("StringID", sshopID);
                            Log.e("Boolean", schatEnabled + "");
                            flagMarkerShow = 1;


                            if (mCircle != null)
                                mCircle.remove();


                            TextView egovername = (TextView) findViewById(R.id.govername);
                            TextView eshopname = (TextView) findViewById(R.id.shopname);


                            egovername.setText(shopeType);
                            eshopname.setText(shopeName);


                            lastPhoneMarker = shopePhone;
                            LatLng mo = new LatLng(mlat, mlong);
                            movelatlng = mo;
                            chatEnabled = schatEnabled;
                            shopID = sshopID;
                            chat.setEnabled(true);
                            //if (flagInfo == 0) {
                            //   call.setEnabled(true);
                            move.setEnabled(true);
                            flagInfo = 1;
                            // }


                            if (shopePhone.length() <= 0 || shopePhone.equals("0")) {
                                call.setEnabled(false);
                            } else {
                                call.setEnabled(true);
                            }


                            getcamera(shopeLatLng, 18);
                            addMarker(shopId, shopeName, shopePhone, shopeLatLng, shopeType);


                            // GovernorateId = iD.get(city);

                            //  Intent intent1 = new Intent(MainActivity.this, com.swadallail.nileapp.Cities.NewsListActivityMain.class);
                            //intent1.putExtra("GovernorateId", GovernorateId);

                            //startActivityForResult(intent1,5);

                            // Toast.makeText(MainActivity.this, shopeName, Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                        }


                    }
                })
        );


    }


    private void clearSearch() {

        myShopeId.clear();
        myShopeName.clear();
        myShopeType.clear();
        myShopePhone.clear();
        myShopeLatLng.clear();
        chats.clear();
        UserID.clear();
        list.setAdapter(null);
    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } catch (Exception e) {

        }
    }


    private void createMarkerAfterSearch() {

        try {

            gMap.clear();
            deleteAraayMarker();

            double currentLat = gMap.getCameraPosition().target.latitude;
            double currentLong = gMap.getCameraPosition().target.longitude;


            // LatLng latLng = new LatLng(currentLat, currentLong);

            String keyEnter = edittext.getText().toString();
            if (keyEnter.length() <= 0)
                edittext.setCursorVisible(false);

            if (info.getVisibility() == View.GONE)
                info.setVisibility(View.VISIBLE);


            if (mCircle == null) {
                mCircle.remove();
            }
            //drawMarkerWithCircle(latLng);


            if (flagMarkerShow == 1) {
                flagMarkerShow = 0;
                prog.setVisibility(View.VISIBLE);
                getcamera(new LatLng(currentLat, currentLong), 14);
                getMarker(GovernId, mlat, mlong);
            }

            /////
            infoSleep();
            //////


        } catch (Exception e) {

        }
    }


    private void infoSleep() {


        try {
            TextView egovername = (TextView) findViewById(R.id.govername);
            TextView eshopname = (TextView) findViewById(R.id.shopname);

            egovername.setText("");
            eshopname.setText("");


            flagContentInfo = 0;

            /////
            //  flagMarkerShow = 0;
            ////

            if (flagInfo == 1) {
                call.setEnabled(false);
                move.setEnabled(false);
                chat.setEnabled(false);
                lastMarker.hideInfoWindow();
                flagInfo = 0;
            }

        } catch (Exception e) {

        }


    }


    @Override
    public void onBackPressed() {


        clearSearch();
        edittext.setText("");
        linearSearch.setVisibility(View.INVISIBLE);
        cross.setVisibility(View.INVISIBLE);
        if (flagMarkerShow == 1) {
            createMarkerAfterSearch();
            //Toast.makeText(MainActivity.this, "Here4", Toast.LENGTH_SHORT).show();
        }

        if (info.getVisibility() == View.GONE) {
            info.setVisibility(View.VISIBLE);
            //Toast.makeText(MainActivity.this, "Here3", Toast.LENGTH_SHORT).show();
        }


        if (flagContentInfo == 1) {
            //Toast.makeText(MainActivity.this, "Here2", Toast.LENGTH_SHORT).show();
            infoSleep();
            gMap.clear();
        } else if (mdrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            getDialog();
        }

        //Toast.makeText(MainActivity.this, "Non", Toast.LENGTH_SHORT).show();

    }


    private void getDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_out, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        Button logout = dialogView.findViewById(R.id.btn_ok);
        Button no = dialogView.findViewById(R.id.btn_cancel);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedHelper.putKey(MainActivity.this, "token", "");
                MainActivity.this.finish();
                //stopService(new Intent(MainActivity.this, ChatService.class));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        if (granted()) {
            try {
                Location myLocation = getLastKnownLocation();
                mlat = myLocation.getLatitude();
                mlong = myLocation.getLongitude();
                sendLocation(mlat, mlong);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "قم بقبول الصلاحية لتحديد موقعك", Toast.LENGTH_SHORT).show();
            }

        }
        super.onResume();
    }

    @Override
    protected void onRestart() {

        Location myLocation = getLastKnownLocation();
        mlat = myLocation.getLatitude();
        mlong = myLocation.getLongitude();
        sendLocation(mlat, mlong);
        super.onRestart();
    }

    @Override
    protected void onStart() {
        /*Intent intent2 = new Intent(MainActivity.this, ChatService.class);
        bindService(intent2, mConnection, Context.BIND_AUTO_CREATE);*/
        super.onStart();
    }
    /**/


}
