package com.abderrahmane.ahmed.pub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abderrahmane.ahmed.sfeeds.R;
import com.abderrahmane.ahmed.sfeeds.UpdateNews;
import db.dbHelper;
import com.abderrahmane.ahmed.sfeeds.UpdateNews;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ahmed on 11/26/2017.
 */
public class ImageAdapter2 extends BaseAdapter {
    private Context mContext;
    private static Bitmap btm;
    private String imgs[] = new String[6];
    private String titles[] =  new String[6];
    static Context c = UpdateNews.c;

    // Constructor
    public ImageAdapter2(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imgs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @SuppressLint("ResourceAsColor")
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layout;
        ImageView imageView;
        TextView text;

        if (convertView == null) {
            layout = new LinearLayout(mContext);

            dbHelper db = new dbHelper(layout.getContext());
            String[][] sites = db.getAllSite();
            int i = 0;
            while (i<6) {
                int j = (int) (Math.random() * 66) ;
                if(!sites[j][3].equals("NULL")) {
                    imgs[i] = sites[j][3];
                    titles[i] = sites[j][1];
                    i++;
                }
            }

            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setBackgroundResource(R.color.white);
            text = new TextView(mContext);
            text.setText(titles[position]);
            text.setTextColor(R.color.darkred);
            text.setMaxLines(4);

            final BitmapLoader[] bitm = new BitmapLoader[1];
            new Thread(new Runnable() {
                public void run() {
                    bitm[0] = new BitmapLoader(imgs[position]);
                    bitm[0].start();
                }
            }).start();

            btm = bitm[0].bmp;
            if(btm != null) {
                imageView.setImageBitmap(btm);
            }
            else
                imageView.setImageResource(mThumbIds[position]);

            layout.addView(imageView);
            layout.addView(text);
            layout.setOrientation(LinearLayout.VERTICAL);

        }
        else
        {
            layout = (LinearLayout) convertView;
        }

        return layout;
    }

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.pub3,
            R.drawable.pub3,
            R.drawable.pub3,
            R.drawable.pub4,
            R.drawable.pub4,
            R.drawable.pub4


    };

    }
