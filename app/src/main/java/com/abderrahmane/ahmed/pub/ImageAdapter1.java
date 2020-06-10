package com.abderrahmane.ahmed.pub;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.abderrahmane.ahmed.sfeeds.R;

/**
 * Created by Ahmed on 11/26/2017.
 */

public class ImageAdapter1 extends BaseAdapter {
    private Context mContext;

    // Constructor
    public ImageAdapter1(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          //  imageView.setPadding(2, 2, 2, 2);
            imageView.setBackgroundResource(R.color.darkred);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.pub3,
            R.drawable.pub3
    };
}
