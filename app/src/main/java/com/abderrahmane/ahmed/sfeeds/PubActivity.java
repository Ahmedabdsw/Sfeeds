package com.abderrahmane.ahmed.sfeeds;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;

import com.abderrahmane.ahmed.pub.ImageAdapter1;
import com.abderrahmane.ahmed.pub.ImageAdapter2;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class PubActivity extends AppCompatActivity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
     //   adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
// TODO: Add adView to your view hierarchy.
        /*adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
*/
        GridView gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview1.setAdapter(new ImageAdapter1(getApplicationContext()));
        GridView gridview2 = (GridView) findViewById(R.id.gridview2);
        gridview2.setAdapter(new ImageAdapter2(getApplicationContext()));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                    finish();
            }
        }, 30000); //  time to display
    }

}
