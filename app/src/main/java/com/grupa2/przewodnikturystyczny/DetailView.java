package com.grupa2.przewodnikturystyczny;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {

    Attraction mAttraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.detail);
        Bundle data = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAttraction = data.getParcelable(AttractionDatabase.AttrConst.KEY);

        TextView nazwa = (TextView) findViewById(R.id.nazwa);
        TextView adres = (TextView) findViewById(R.id.adres);
        TextView opis = (TextView) findViewById(R.id.szczegolowy_opis);
        ImageView photo1 = (ImageView) findViewById(R.id.photo1);
        ImageView photo2 = (ImageView) findViewById(R.id.photo2);
        ImageView photo3 = (ImageView) findViewById(R.id.photo3);

        int idPhoto1 = context.getResources().getIdentifier(mAttraction.getPhoto1(), "drawable", context.getPackageName());
        int idPhoto2 = context.getResources().getIdentifier(mAttraction.getPhoto2(), "drawable", context.getPackageName());
        int idPhoto3 = context.getResources().getIdentifier(mAttraction.getPhoto3(), "drawable", context.getPackageName());

        nazwa.setText(mAttraction.getName());
        adres.setText(mAttraction.getAdress());
        opis.setText(mAttraction.getFullDescription());
        photo1.setImageResource(idPhoto1);
        photo2.setImageResource(idPhoto2);
        photo3.setImageResource(idPhoto3);

        ImageButton localizeButton = (ImageButton) findViewById(R.id.localizeButton);
        localizeButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                findMe(mAttraction.getAdress());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findMe(String adres) {
        String url = "http://maps.google.co.in/maps?q=" + adres;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
