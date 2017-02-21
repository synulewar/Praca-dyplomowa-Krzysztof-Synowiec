package com.grupa2.przewodnikturystyczny;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dargielen on 21.02.2017.
 */

public class AttractionInfo extends AppCompatActivity{

    String mName, mAdress, mFullDescription;
    Integer[]mPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attraction_info);

        Intent intent = getIntent();
        mName = intent.getStringExtra("mName");
        mAdress = intent.getStringExtra("mAdress");
        mFullDescription = intent.getStringExtra("mFullDescription");
        mPhotos = intent.getIntArrayExtra("mPhotos");
        TextView textView21 = (TextView) findViewById(R.id.textView21);
        textView21.setText(mName);
        TextView textView22 = (TextView) findViewById(R.id.textView22);
        textView22.setText(mAdress);
        TextView textView23 = (TextView) findViewById(R.id.textView23);
        textView23.setText(mFullDescription);


        GridView photosGrid = (GridView) findViewById(R.id.photos_grid);


        Button btn_mapa = (Button) findViewById(R.id.buttonMapa);
        btn_mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showOnMap();
            }
        });
    }// koniec onCreate

    private void showOnMap() {

        String geoUri = "http://maps.google.com/maps?q=loc:" + " (" + mAdress + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);
    }
}
