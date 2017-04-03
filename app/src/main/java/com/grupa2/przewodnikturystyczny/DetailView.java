package com.grupa2.przewodnikturystyczny;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailView extends AppCompatActivity {

    Attraction mAttraction;
    int[] mResources = new int[3] ;

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

        int idPhoto1 = context.getResources().getIdentifier(mAttraction.getPhoto1(), "drawable", context.getPackageName());
        int idPhoto2 = context.getResources().getIdentifier(mAttraction.getPhoto2(), "drawable", context.getPackageName());
        int idPhoto3 = context.getResources().getIdentifier(mAttraction.getPhoto3(), "drawable", context.getPackageName());

        mResources[0] = idPhoto1;
        mResources[1] = idPhoto2;
        mResources[2] = idPhoto3;


        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(customPagerAdapter);

        nazwa.setText(mAttraction.getName());
        adres.setText(mAttraction.getAdress());
        opis.setText(mAttraction.getFullDescription());

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

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
