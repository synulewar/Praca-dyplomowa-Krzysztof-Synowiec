package com.grupa2.przewodnikturystyczny;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;

public class MapMarker extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        AttractionDatabase database = new AttractionDatabase(getApplicationContext());
        database.open();
        Cursor cursor = database.fetchAllPlaces();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Intent intent = getIntent();
        double myLongitude = intent.getDoubleExtra(MainList.LONGITUDE,MainList.DEFAUL_LONGITUDE);
        double myLatitude = intent.getDoubleExtra(MainList.LATITUDE, MainList.DEFAULT_LATITUDE);
        LatLng myLatLng = new LatLng(myLatitude,myLongitude);
        googleMap.addMarker(new MarkerOptions().position(myLatLng).title("Twoja lokalizacja"));
        builder.include(myLatLng);
        if(cursor!=null) {
            try {
                while (cursor.moveToNext()) {
                    String nazwa = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LONGITUDE));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LATITUDE));
                    LatLng latLng = new LatLng(latitude,longitude);
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(nazwa));
                    builder.include(latLng);
                }
            } finally {
                cursor.close();
            }
        }
        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
        googleMap.moveCamera(cu);
    }
}
