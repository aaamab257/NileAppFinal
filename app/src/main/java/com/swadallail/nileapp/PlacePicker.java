package com.swadallail.nileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PlacePicker extends AppCompatActivity {

GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placepicker);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SupportMapFragment mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //  mapView.getMapAsync(MainActivity.this);


        mapView.getMapAsync(new OnMapReadyCallback() {


            @SuppressLint("WrongConstant")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;


                granted();
                buildAlertMessageNoGps(9);


               // LatLng center = new LatLng(15.5007, 32.5599);
               // CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
                //gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));









                gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        // display imageView
                        ImageView imgPinUp = (ImageView) findViewById(R.id.imgLocationPinUp);

                        imgPinUp.setVisibility(View.GONE);

                           MarkerOptions markerOptions = new MarkerOptions().position(gMap.getCameraPosition().target)
                                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.mkl));

                         gMap.addMarker(markerOptions);

                    }

                });




                gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {

                        gMap.clear();

                        ImageView imgPinUp = (ImageView) findViewById(R.id.imgLocationPinUp);

                        imgPinUp.setVisibility(View.VISIBLE);

                     //   MarkerOptions markerOptions = new MarkerOptions().position(gMap.getCameraPosition().target)
                       //         .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin_up));

                        // gMap.addMarker(markerOptions);


                    }

                });



            }
            });



    }


    private void granted() {
        // boolean isGranted = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean isGranted = ActivityCompat.checkSelfPermission(PlacePicker.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlacePicker.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (isGranted) {


            gMap.setMyLocationEnabled(true);
        } else {

            ActivityCompat.requestPermissions(PlacePicker.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                gMap.setMyLocationEnabled(true);
            }

        }

    }




    public void current(int x) {
        try {
            // buildAlertMessageNoGps(x);
            Location myLocation = getLastKnownLocation();
            double latti = myLocation.getLatitude();
            double longi = myLocation.getLongitude();
           LatLng latLng = new LatLng(latti, longi);
            getcamera(latLng, 18);
            //  addMarker(latLng, getResources().getString(R.string.my_place));
        } catch (Exception e) {
            final LatLng mapTargetLatLng = gMap.getCameraPosition().target;
            getcamera(mapTargetLatLng, 18);


            //addMarker(mapTargetLatLng, getResources().getString(R.string.my_place));
        }


    }


    private void getcamera(LatLng latlng, int zoomGet) {
       LatLng center = latlng;
       CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(zoomGet).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    @SuppressLint("WrongConstant")
    protected void buildAlertMessageNoGps(int x) {
        final LocationManager manager = (LocationManager) PlacePicker.this.getSystemService(this.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

         /*   gMap.setStyle(Style, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                    enableLocationPlugin(style, x);
                }

            });
            */

            current(18);


        } else {

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

    }


    private Location getLastKnownLocation() {
      LocationManager  mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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




    public void selectLocationButton(View v) {


        double   currentLat = gMap.getCameraPosition().target.latitude;
        double  currentLong = gMap.getCameraPosition().target.longitude;


        Intent intent1 = new Intent(PlacePicker.this, AddActivity.class);

        intent1.putExtra("lat", currentLat);
        intent1.putExtra("long", currentLong);

        startActivity(intent1);
        finish();


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


}
