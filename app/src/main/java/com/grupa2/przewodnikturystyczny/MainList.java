package com.grupa2.przewodnikturystyczny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;

public class MainList extends AppCompatActivity {

    Activity mActivity;
    Context mContext;
    AttractionDatabase mDatabase;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();
        mActivity = this;

        mDatabase = new AttractionDatabase(mContext);
        mDatabase.open();

        //nie mamy zewnetrznego serwera wiec w ten sposob realizujemy fikcyjne uzupelnianie bazy
        mDatabase.deleteAllAttractions();
        mDatabase.addAttractions();



        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            //Nie mamy jeszcze obliczania dystansu wiec poki co sortujemy po adresie
            case R.id.order_distance_asc:
                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ADRES + " ASC");
                displayListView();
                return true;

            case R.id.order_distance_desc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ADRES + " DESC");
                displayListView();
                return true;

            case  R.id.order_name_asc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " ASC");
                displayListView();
                return true;

            case  R.id.order_name_desc:

                mDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " DESC");
                displayListView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayListView() {
        Cursor cursor = mDatabase.fetchAllAttractions();

        // Kolumny do podpięcia
        String[] columns = new String[]{
                AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME,
                AttractionDatabase.AttrConst.COLUMN_ADRES,
                AttractionDatabase.AttrConst.COLUMN_SHORT_DESCRIPTION,
                AttractionDatabase.AttrConst.COLUMN_PHOTO_1
        };

        int[] to = new int[]{
                R.id.nazwa,
                R.id.place,
                R.id.opis,
                R.id.main_photo
        };


        // Tworzymy adapter z kursorem wskazującym na nasze dane
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_view_element, cursor, columns, to, 0);


        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.main_photo) {
                    ImageView mainPhoto = (ImageView) view;
                    int resId = mContext.getResources().getIdentifier(cursor.getString(columnIndex), "drawable", mContext.getPackageName());
                    mainPhoto.setImageResource(resId);
                    return true;
                }
                return false;
            }
            // Assign adapter to ListView

        });

        EditText mFilter = (EditText) findViewById(R.id.szukacz);
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

        // Podpinamy adapter do listy
        ListView listView = (ListView) findViewById(R.id.listaAtrakcji);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Pobierz dane z wybranej pozycji
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Pobieramy numer telefonu z bazy dla wybranego klienta i wyświetlamy w Toast

                String nazwa = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME));
                String adres = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ADRES));
                String opis = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_FULL_DESCRIPTION));
                String photo1 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_1));
                String photo2 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_2));
                String photo3 = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTO_3));
                Attraction attraction = new Attraction(nazwa, adres, null, opis, photo1, photo2, photo3);

                Intent intent = new Intent(mActivity, DetailView.class);
                intent.putExtra(AttractionDatabase.AttrConst.KEY, attraction);
                startActivity(intent);
            }
        });
    }
}

