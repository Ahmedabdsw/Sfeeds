package com.abderrahmane.ahmed.sfeeds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

import db.dbHelper;
import com.abderrahmane.ahmed.adapter.ItemDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class notificationsActivity extends AppCompatActivity {
    private String[][] notification;
    private  ArrayList<ItemDetails> image_details;
    private ListView lv;
    private dbHelper db;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificatons);
        Toolbar tb = findViewById(R.id.actionb4);
        tb.setTitle("");
        setSupportActionBar(tb);

        AdView mAdView = findViewById(R.id.adViewn);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        db = new dbHelper(notificationsActivity. this);
        lv = findViewById(R.id.slistview1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                ItemDetails obj = (ItemDetails) o;
                Intent intent = new Intent(notificationsActivity.this, WebActivity.class);
                intent.putExtra("yourkey", notification[position][2]);
                startActivityForResult(intent, 10);

                db.insertData(obj.getImageNumber());
            }
        });

        ImageButton b = findViewById(R.id.close3);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 100); //  time to display
            }
        });

                 image_details = GetSearchResults();
                        lv.setAdapter(new com.abderrahmane.ahmed.adapter.ItemListBaseAdapter(getApplicationContext(), image_details));
                        lv.invalidate();
                        lv.invalidateViews();
                        db.resetNotifiycations();
    }

    private ArrayList<ItemDetails> GetSearchResults() {
        ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
        ItemDetails item_details;
        notification = db.getNotifiycations1();

          if( notification.length > 0){
        for (String[] rs :  notification) {

                    item_details = new ItemDetails();
                    item_details.setTitre1(rs[1]);
                    item_details.setImageNumber(Integer.parseInt(rs[0]));
                    results.add(item_details);

          }
        }
               return results;
    }

}