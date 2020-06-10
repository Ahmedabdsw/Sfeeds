package com.abderrahmane.ahmed.sfeeds;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import db.dbHelper;

public class searchActivity extends AppCompatActivity {
    private String[][] rss1;
    private ArrayList<ItemDetails> image_details;
    private ListView lv;
    private TextView txt;
    private dbHelper db;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar tb = findViewById(R.id.actionb3);
        tb.setTitle("");
        setSupportActionBar(tb);

        db = new dbHelper(searchActivity. this);
        final SearchView search = (SearchView) findViewById(R.id.srview);
        search.onActionViewExpanded();

        AdView mAdView = findViewById(R.id.adViews);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        lv = findViewById(R.id.slistview);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                ItemDetails obj = (ItemDetails) o;
                Intent intent = new Intent(searchActivity.this, WebActivity.class);
                intent.putExtra("yourkey", rss1[position][2]);
                startActivityForResult(intent, 10);

                db.insertData(obj.getImageNumber());
            }
        });

        ImageButton b = findViewById(R.id.close2);
        if(ViewCompat.getLayoutDirection(b.getRootView()) == ViewCompat.LAYOUT_DIRECTION_RTL)
            b.setRotation(b.getRotation()+180f);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchActivity.this, MainActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(!query.isEmpty() && !query.matches("\\d+")){
                    image_details = GetSearchResults1(query);
                        lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
                        lv.invalidate();
                        lv.invalidateViews();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

       lv.invalidate();
        lv.invalidateViews();

    }

    private ArrayList<ItemDetails> GetSearchResults1(String query) {
        ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
        ItemDetails item_details;
//        dbHelper db = new dbHelper(this);
        if (Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]").matcher(query).matches() )
        //    if(query.contains("http://") || query.contains("https://"))
        rss1 = db.SearchLink(query);
        else
        rss1 = db.SearchTitle(query);
        if(rss1.length > 0){
        for (String[] rs : rss1) {
                    item_details = new ItemDetails();
                    item_details.setTitre1(rs[1]);
                    item_details.setImageNumber(Integer.parseInt(rs[0]));
                    results.add(item_details);
          }
        }
          else
        Toast.makeText(searchActivity.this,"لا توجد نتائج"  , Toast.LENGTH_LONG).show();

        return results;
    }

}