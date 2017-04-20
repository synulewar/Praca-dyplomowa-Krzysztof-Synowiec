package com.grupa2.przewodnikturystyczny;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainList extends AppCompatActivity {

    private static final String TAG = "kkk";
    private static final String ATTRACTION_URL = "https://drive.google.com/uc?export=download&id=0B6RSK6h2F7l8TkdFN3paNEpuQmM";
    Activity mActivity;
    Context mContext;
    LocationManager mLocationManager;
    Location mMyLocation = new Location("");
    AttractionDatabase mDatabase;
    private SimpleCursorAdapter dataAdapter;
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final double DEFAUL_LONGITUDE = 17.060520699999984;
    public static final double DEFAULT_LATITUDE = 51.1082569;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private static final long INTERVAL_TIME = 50;
    private static final float MINIMAL_DISTANCE = 50;
    private ListView mListView;
    private EditText mFilter;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mActivity = this;
        if(isNetworkAvailable()) {
            setStandardView();
        } else {
            setNoInternetView();
        }
    }

    private void setNoInternetView() {
        setContentView(R.layout.internet_missing);
        Snackbar.make(getWindow().getDecorView().getRootView(), mContext.getString(R.string.internet_connectio_error), Snackbar.LENGTH_LONG).show();
        Button refers = (Button) findViewById(R.id.refresh_button);
        refers.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mContext, MainList.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  );
                startActivity(mainIntent);
                mActivity.finish();
            }
        });
    }

    private void setStandardView() {
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);
        mMyLocation.setLatitude(DEFAULT_LATITUDE);
        mMyLocation.setLongitude(DEFAUL_LONGITUDE);

        mListView = (ListView) findViewById(R.id.listaAtrakcji);
        mFilter = (EditText) findViewById(R.id.szukacz);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mListView.setVisibility(View.INVISIBLE);
        mFilter.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        FloatingActionButton mapa = (FloatingActionButton) findViewById(R.id.map);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MapMarker.class);
                intent.putExtra(LONGITUDE, mMyLocation.getLongitude());
                intent.putExtra(LATITUDE, mMyLocation.getLatitude());
                startActivity(intent);
            }
        });
        mDatabase = new AttractionDatabase(mContext);
        mDatabase.open();
        mDatabase.deleteAllAttractions();
        useOkHttp();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService
                (Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null;
    }

    private void getViewReady() {
        getLocation();
        calculateDistanceFromAttracions();
        displayListView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void calculateDistanceFromAttracions() {

        double result;
        Cursor cursor = mDatabase.fetchAllLocations();
        if(cursor!=null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst._ID));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LONGITUDE));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LATITUDE));
                    Location location = new Location("");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    result =(double) mMyLocation.distanceTo(location)/1000;
                    mDatabase.insertDistance(id, result);
                    Log.d(TAG, "calculateDistanceFromAttracions: " + result);
                }
            } finally {
                cursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.order_distance_asc:
                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_DISTANCE + " ASC");
                displayListView();
                return true;

            case R.id.order_distance_desc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_DISTANCE + " DESC");
                displayListView();
                return true;

            case R.id.order_name_asc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " ASC");
                displayListView();
                return true;

            case R.id.order_name_desc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " DESC");
                displayListView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayListView() {
        Cursor cursor = mDatabase.fetchAllAttractions();

        String[] columns = new String[]{
                AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME,
                AttractionDatabase.AttrConst.COLUMN_ADRES,
                AttractionDatabase.AttrConst.COLUMN_SHORT_DESCRIPTION,
                AttractionDatabase.AttrConst.COLUMN_PHOTO_1,
                AttractionDatabase.AttrConst.COLUMN_DISTANCE
        };

        int[] to = new int[]{
                R.id.nazwa,
                R.id.place,
                R.id.opis,
                R.id.main_photo,
                R.id.dystans
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_view_element, cursor, columns, to, 0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.main_photo) {
                    ImageView mainPhoto = (ImageView) view;
                    mainPhoto.setBackgroundColor(Color.WHITE);
                    Picasso.with(mContext).load(cursor.getString(columnIndex)).placeholder
                            (R.drawable.animation_progress).error(R.drawable.nophoto).into(mainPhoto);
                    return true;
                } else if(view.getId() == R.id.dystans) {
                    TextView dystans = (TextView) view;
                    double dyst = cursor.getDouble(columnIndex);
                    String tekst = String.format("%.2f", dyst) + " km";
                    dystans.setText(tekst);
                    return true;
                }
                return false;
            }
        });

        mListView.setAdapter(dataAdapter);

        mListView.setVisibility(View.VISIBLE);
        mFilter.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String nazwa = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME));
                String adres = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ADRES));
                String opis = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_FULL_DESCRIPTION));
                String photo1 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_1));
                String photo2 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_2));
                String photo3 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_3));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LONGITUDE));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_LATITUDE));
                double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_DISTANCE));

                Attraction attraction = new Attraction(nazwa, adres, null, opis, photo1, photo2, photo3, longitude, latitude, distance);

                Intent intent = new Intent(mActivity, DetailView.class);
                intent.putExtra(AttractionDatabase.AttrConst.KEY, attraction);
                startActivity(intent);
            }
        });

        mFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return mDatabase.getAttractionByNameOrFragment(constraint.toString());
            }
        });
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                getPermission();
            } else {
                startLocationListener();
            }
        } else {
            startLocationListener();
        }
    }


    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationListener();
                } else {
                    Toast.makeText(getApplicationContext(), "Nie przyznano uprawnien. Domyslna lokalizacja to budynek C6", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void startLocationListener() {

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) mActivity.getSystemService
                    (Context.LOCATION_SERVICE);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null) {
                Log.d(TAG, "lastKnownLocation:  " + lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());
                mMyLocation.setLatitude(lastKnownLocation.getLatitude());
                mMyLocation.setLongitude(lastKnownLocation.getLongitude());
            }

            Log.d(TAG, "getLocation: ");

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "onLocationChanged:  ");
                    mMyLocation.setLatitude(location.getLatitude());
                    mMyLocation.setLongitude(location.getLongitude());
                    calculateDistanceFromAttracions();
                    dataAdapter.notifyDataSetChanged();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.d(TAG, "onStatusChanged: ");
                }

                public void onProviderEnabled(String provider) {
                    Log.d(TAG, "onProviderEnabled: ");
                }

                public void onProviderDisabled(String provider) {
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_TIME, MINIMAL_DISTANCE, locationListener);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AttractionLoadedEvent event) {
        Attraction[] attracions = event.getAttractions();
        for (Attraction attraction:attracions) {
            mDatabase.insertAttraction(attraction);
        }
        EventBus.getDefault().unregister(this);
        getViewReady();
    }

    private void useOkHttp() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(ATTRACTION_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: fail !");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    Log.d(TAG, "onResponse: " + json);
                    Attraction[] attractions = new Gson().fromJson(json, Attraction[].class);
                    EventBus.getDefault().post(new AttractionLoadedEvent(attractions));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



