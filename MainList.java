package com.grupa2.przewodnikturystyczny;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ListView;

import org.w3c.dom.Attr;


public class MainList extends AppCompatActivity {

    private AttractionDatabase attrDatabase;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attrDatabase = new AttractionDatabase(this);
        attrDatabase.open();
        attrDatabase.deleteAllAttractions();

        Integer[] photoIDs = {
                R.drawable.photo1,
                R.drawable.photo2,
                R.drawable.photo3
        };

        /*myślałem żeby trochę inaczej realizować zdjęcia na przykładzie:
         http://stackoverflow.com/questions/5782922/android-gallery-into-grid-style-menu?noredirect=1&lq=1
         korzystając z tablicy, nie zakładając ile zdjęć ma każda atrakcja, ale tu utknąłem*/



        displayListView();
    }

    private void displayListView() {
        Cursor cursor = attrDatabase.fetchAllAttractions();

        String[] columns = new String[] {

                AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME,
                AttractionDatabase.AttrConst.COLUMN_SHORT_DESCRIPTION,
        };

        int[] to = new int[] {

                R.id.nazwa,
                R.id.opis,
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_view_element,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listaAtrakcji);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Pobierz dane z wybranej pozycji
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), AttractionInfo.class);
                Bundle b = new Bundle();
                String attr_name = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME));
                b.putString("mName",attr_name);
                String attr_adres = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_ADRES));
                b.putString("mAdress",attr_adres);
                String attr_description = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_FULL_DESCRIPTION));
                b.putString("mFullDecsription",attr_description);
                String attr_photos = cursor.getString(cursor.getColumnIndexOrThrow(AttractionDatabase.AttrConst.COLUMN_PHOTOS));
                b.putIntArray("mPhotos", photoIds[]);

                intent.putExtras(b);
                startActivity(intent);
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.szukacz);
        myFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return attrDatabase.fetchAttractionByNameAndDistance(constraint.toString());
            }
        });
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
            case R.id.order_id_asc:
                attrDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " ASC");
                displayListView();
                return true;

            case R.id.order_id_desc:
                attrDatabase.setOrderBy(AttractionDatabase.AttrConst.COLUMN_ATTRACTION_NAME + " DESC");
                displayListView();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
