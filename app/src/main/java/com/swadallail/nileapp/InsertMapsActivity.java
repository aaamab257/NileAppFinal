package com.swadallail.nileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InsertMapsActivity extends FragmentActivity implements LocationListener {

     GoogleMap googleMap;
    private LatLng garageLocation;
    Button btnSetGarageLocation;
    LatLng  center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_maps);
        btnSetGarageLocation = (Button) findViewById(R.id.btn_set_garage_location);
       // int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

     /*   if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available
*/
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapp);

            // Getting GoogleMap object from the fragment
            //googleMap = fm.getMap();

            fm.getMapAsync(new OnMapReadyCallback() {


                @SuppressLint("WrongConstant")
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap = googleMap;

                  //  googleMap.setMyLocationEnabled(true);

                    center = new LatLng(15.5007, 32.5599);
                CameraPosition    cameraPosition = new CameraPosition.Builder().target(center).zoom(10).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }


            });


            // Enabling MyLocation Layer of Google Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                ActivityCompat.requestPermissions(InsertMapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }
            //googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling

                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
               // onLocationChanged(location);
            }
        //    locationManager.requestLocationUpdates(provider, 100, 0, (android.location.LocationListener) this);





            btnSetGarageLocation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = getIntent();
                    intent.putExtra("location_lat",garageLocation.latitude);
                    intent.putExtra("location_lng",garageLocation.longitude);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        //}





        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                garageLocation = latLng;
            }
        });





    }






    @Override
    public void onLocationChanged(Location location)
    {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.getMaxZoomLevel();

        // Setting latitude and longitude in the TextView tv_location

    }


}