package com.abderrahmane.ahmed.sfeeds;

 import android.annotation.SuppressLint;
 import android.content.Intent;
 import android.net.Uri;
 import android.support.v4.view.ViewCompat;
 import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 import android.support.v7.widget.Toolbar;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.webkit.SafeBrowsingResponse;
 import android.webkit.WebChromeClient;
 import android.webkit.WebResourceRequest;
 import android.webkit.WebView;
 import android.webkit.WebViewClient;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.TextView;

 import com.google.android.gms.ads.AdRequest;
 import com.google.android.gms.ads.AdView;

public class WebActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 10;
    private String url = null;
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar tb = findViewById(R.id.actionb1);
        tb.setTitle("");
        setSupportActionBar(tb);

        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        web  = (WebView) findViewById(R.id.web1);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);

        WebChromeClient wc = new WebChromeClient();
     //   web.setWebChromeClient(wc);
        web.pageUp(true);
        Bundle extras = getIntent().getExtras();
        web.setWebViewClient(new myWebClient());
        url = extras.getString("yourkey");
      //  String html = "<html><head><meta charset=\"utf-8\"/></head><body><h1>test</h1>"+inputString+"</body></html>";
       // web.loadData(html, "text/html", "utf-8");vnb
        if(url == null)
            web.loadUrl("file:///android_asset/index.html");
          else {
            web.loadUrl(url);
        }

        ImageButton b = findViewById(R.id.close1);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.loop:
                web.reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class myWebClient extends WebViewClient {
        @SuppressLint("NewApi")
        @Override
        public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback){
            // true argument indicates that your app reports incidents like this one to safe browsing.
            callback.backToSafety(true);
        }


    }

}
