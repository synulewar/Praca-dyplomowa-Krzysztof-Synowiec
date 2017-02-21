package com.grupa2.przewodnikturystyczny;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dargielen on 21.02.2017.
 */

public class ImageAdapter {

    private Context context;
    Integer[]Photos2;

    public ImageAdapter(Context c, Integer[] mPhotos)
    {
        context = c;
        Photos2 = mPhotos;
    }

    //---returns the number of images---
    public int getCount() {
        return Photos2.length;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(Photos2[position]);
        return imageView;
    }
}


