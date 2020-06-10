package com.abderrahmane.ahmed.sfeeds;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import db.dbHelper;


public class MainActivity extends AppCompatActivity {

//    String[] sites1 = {"http://www.france24.com/ar/top-stories/rss","http://feeds.bbci.co.uk/arabic/rss.xml","http://www.aljazeera.net/aljazeerarss/be46a341-fe26-41f1-acab-b6ed9c198b19/e6aef64d-084c-42f0-8269-abe48e0cd154","https://arabic.rt.com/rss/"
   //                     ,"https://www.skynewsarabia.com/web/rss/world.xml"};


    // constant to determine which sub-activity returns
 private static final int REQUEST_CODE = 10;
// public static RssReader[] str = new RssReader[147];
 private ListView lv;
 UpdateService serv;
    dbHelper db ;
 private ArrayList<ItemDetails> image_details;
 static final int ct =147;
    static int cc = 0;
  private String[][] rss =null;
    MenuItem mitem = null;
    TextView not;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        try {
            Process p = Runtime.getRuntime().exec("su");
        } catch (IOException e) {}
*/

        UpdateNews up = new UpdateNews();


        Intent service = new Intent(this, UpdateService.class);
        startService(service);

        Toolbar tb = findViewById(R.id.action1);
        tb.setTitle("");
        setSupportActionBar(tb);

        db = new dbHelper(MainActivity.this);

        FrameLayout fr = (FrameLayout) findViewById(R.id.frame);

        not = (TextView) findViewById(R.id.notify);
        loadData();
        rss = db.getAllSite();
        lv = findViewById(R.id.list1);
//  lv.addFooterView(footer);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                   ItemDetails obj = (ItemDetails) o;
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("yourkey", rss[position][2]);
                startActivityForResult(intent, REQUEST_CODE);
                db.insertData(obj.getImageNumber());
            }
        });

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_ENTERED:
                                Toast.makeText(MainActivity.this, "DRAG_STARTED", Toast.LENGTH_LONG).show();

                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                Toast.makeText(MainActivity.this, "DRAG_EXITID", Toast.LENGTH_LONG).show();
                                break;

                        }
                        return true;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    lv.setOnScrollListener(new AbsListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }
        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                       if(++i+i1 > i2 && (ct<147) ) {
   //                     image_details = GetSearchResults(ct+12);
     //                   absListView.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
                     //   j = i1+2;
//                           cc=absListView.getFirstVisiblePosition();
       //                 absListView.setSelection(i2-j);
                            }
       // lv.addFooterView(footer) ;
        }
    });

        lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), GetSearchResults()));

        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


 /*       AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-6781955195365912/9822911832");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        lv.addHeaderView(mAdView);
*/
        AdView mAdView1 = new AdView(this);
        mAdView1.setAdSize(AdSize.BANNER);
        mAdView1.setAdUnitId("ca-app-pub-6781955195365912/8743494728");
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);
        lv.addFooterView(mAdView1);

        lv.invalidate();
        lv.invalidateViews();


        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboard;
                ClipData clipData = null;
                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = new ClipData.Item(rss[position][1]);
                if (clipData == null) {
                    clipData = new ClipData(new ClipDescription("description", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}), item);
                    clipboard.setPrimaryClip(clipData);
                }
                clipData.addItem(item);
                Toast.makeText(MainActivity.this,"لقد تم نسخ النص"  , Toast.LENGTH_LONG).show();

                return true;
            }
        });

        Timer autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        int n = Integer.parseInt(db.getNotifiycations());
                        Log.v("n = ",""+n);
                        if(n != 0) {
                            if(n < 100)
                            not.setText(n+"");
                            else {
                                n = 99;
                                not.setText(n+"+");
                            }
                            not.setBackgroundResource(R.drawable.shape2);

                        }
                        cc = lv.getFirstVisiblePosition();
                        image_details = GetSearchResults();
                        lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
                        lv.setSelection(cc);
                        lv.invalidate();
                        lv.invalidateViews();
                    }
                });
            }
        }, 0, 120000); // updates each 120 secs

        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(MainActivity.this, notificationsActivity.class);
                startActivity(k);
                not.setText("");
                not.setBackgroundResource(R.drawable.shape);

            }
        });

        Intent j = new Intent(MainActivity.this, PubActivity.class);
        startActivity(j);

    }



    @Override
    protected void onStart() {
        super.onStart();
// Bind to LocalService
        Intent it1 = new Intent(getApplicationContext(), UpdateService.class);
        bindService(it1, mConnection,Context.BIND_AUTO_CREATE);
        mBound = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;

    }

    public void onPause() {
     super.onPause();

        rss = db.getAllSite();
        image_details = GetSearchResults();
        lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
        lv.invalidate();
        lv.invalidateViews();
        // Toast.makeText(MainActivity.this,"ct="+str[146].error  , Toast.LENGTH_LONG).show();
    }


        @Override
 public void onResume() {
  super.onResume();
   //              image_details = GetSearchResults();
  //          lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
    //        int k = ct - j;
   //         lv.setSelection(k);
  //          lv.invalidate();
 //           lv.invalidateViews();
// 8640000 = 24 hours
/*     Timer autoUpdate = new Timer();
  autoUpdate.schedule(new TimerTask() {
   @Override
   public void run() {
    runOnUiThread(new Runnable() {
        public void run() {
      cc = lv.getLastVisiblePosition();
         image_details = GetSearchResults();
      lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
            //  int k = ct - j;
          //    lv.setSelection(cc);
            lv.setSelection(cc);
            lv.invalidate();
      lv.invalidateViews();
            Toast.makeText(MainActivity.this,"cc = "+cc  , Toast.LENGTH_LONG).show();
        }
    });
   }
  +""}, 0, 80000); // updates each 120 secs  */
        }

 private ArrayList<ItemDetails> GetSearchResults() {
     ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
     ItemDetails item_details;
     if(rss.length>0){
         for (String[] rs : rss) {
             item_details = new ItemDetails();
             item_details.setTitre1(rs[1]);
             item_details.setImageNumber(Integer.parseInt(rs[0]));
             item_details.setImageUrl(rs[3]);
             results.add(item_details);
         }
     }
         return results;
 }

    boolean mBound = false;
    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            UpdateService.LocalBinder binder = (UpdateService.LocalBinder) service;
            serv = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false; serv = null;
        }
    };

    @Override
    public void onDestroy()
    {
        if (serv != null)
        {
            // do some finalization with mService
        }

        if (mBound)
        {
            mBound = false;
            unbindService(mConnection);
        }
//        db.truncateAllData();
 //       db.close();
        super.onDestroy();
    }


    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu2, menu);
        mitem = menu.findItem(R.id.refrech);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refrech:
                rss = db.getAllSite();
                image_details = GetSearchResults();
                lv.setAdapter(new ItemListBaseAdapter(getApplicationContext(), image_details));
                lv.invalidate();
                lv.invalidateViews();

                return true;
              case R.id.search:
                  Intent intent = new Intent(MainActivity.this, searchActivity.class);
                  //intent.putExtra("query", newText);
                  startActivityForResult(intent, REQUEST_CODE);
                  return true;
           default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData(){
        int[] data1 = {0,1,1,2,3,4,5,6,7,8,9,10,11,11,12,12,13,13,13,13,14,14,14,15,16,17,18,19,20,20,21,22,23,24,25,25,26,26,27,28,28,29,30,31,32,33,34,35,36,37,38,39,40,40,41,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,0,0,0,0,92,14,0,60,23,25,50,25,11,11,3,8,1,38,80,46,5,4,6,2,2,10,10,18,9,77,1,1,10,9,5,3,19,24,14,3,94,3,3,1,24,0,1,0,25,24,10,0,0,11,3,11,98,45,60,14,25,8,24,1,3,44,43,42,41,37,36,33,32,28,26,12,7,44,25,25,10,10,2,9,6,2,5,44,11,11,18,43,20,29,48,22,21,49,52,53,55,57,59,50,51,63,71,72,68,70,72,35,5,6,49,11,41,4,19,33,43,85,82,90,86,91,93,89,84,95,100,99,101,87,78,105,102,81,117,113,121,111,76,125,126,75,73,23,6,9,14,4,36,78,92,7,46,75,73,121,89,37,2,76,44,85,45,81,101,20,65,33,23,18,78,7,81,90,2,5,9,105,4,124,127,10,4,6,5,2,11,10,26,7,43,28,18,11,56,10,144,3,57,5,6,18,12,52,0,20,0,50,56,0,0,25,4,4,4,20,73,11,9,9,6,0,7,106,49,0,9,3,9,24,3,3,0,0,0};
        String[][] data2 = {
                                                      {""+0,"عاجل: مواجهات قبلية في مدينة لعيون وسقوط جرحى","http://www.alwiam.info/node/9990","http://www.alwiam.info/sites/default/files/styles/large/public/field/image/6141085-9172552_0.jpg?itok=0T1e5hRj",27473060+""},
                                                                        {""+47,"رحم الله هذا الشاب العجيب ما أحوجنا لأمثاله اليوم(صورته وقصته)","http://eljewahir.com/node/10010","http://eljewahir.com/sites/default/files/styles/large/public/field/image/t%C3%A9l%C3%A9chargement%D8%A7%D9%84%D8%A8%D9%8A4.jpg?itok=wFD1Y0FN",27473060+""},
                                                                        {""+3,"وفاة سيدة وجرح آخرين في حادث سير قرب بوتلميت ","http://essahraa.net/node/2295","http://essahraa.net/sites/default/files/IMG_4889-7b4fc.jpg",27473060+""},
                                                                        {""+5,"حادث سير مروع على طريق الامل قرب  بتليميت ","http://taqadoum.mr/node/3952","http://taqadoum.mr/sites/default/files/36882455_949297971944631_4920880692591591424_n_0.jpg",27473060+""},
                                                                        {""+50,"التشكلة النهائية للائحة نواب تواصل فى نواكشوط (اللائحة)","http://essirage.net/node/14452","http://essirage.net/sites/files/003_0.jpg",27473060+""},
                                                                        {""+6,"حصيلة ثقيلة لحوادث السير خلال يوم واحد","http://www.aqlame.com/article38333.html","NULL",""+27473060+""},
                                                                        {""+7,"حزب تواصل يعلن جميع مرشحيه للوائح الوطنة والنساء ونواكشوط ","http://rimtoday.net/?q=node/18186","http://rimtoday.net/?q=sites/default/files/styles/large/public/field/image/%D8%AA%D9%86%D8%B2%D9%8A%D9%84_0.png&amp;itok=f0qFwLp0",27473060+""},
                                                                        {""+8,"اسرارتكشف لاول مرة عن الحياة الخاصة للرئيس الاسبق معاوية ولد سيداحمد ولد  الطايع ","http://www.taqadoumi.net/index.php/2535-2018-08-04-10-59-04","http://www.taqadoumi.net/images/img2018/arton27028-300x200.jpg",27473060+""},
                                                                        {""+55,"حصيلة حوادث السير التى وقعت خلال الأيام الثلاثة الأخيرة (بيان)","http://28novembre.info/node/11846","http://28novembre.info/sites/default/files/%D9%85%D8%B9%D8%A7%20%D9%84%D9%84%D8%AD%D8%AF%20%D9%85%D9%86%20%D8%AD%D9%88%D8%A7%D8%AF%D8%AB%20%D8%A7%D9%84%D8%B3%D9%8A%D8%B1.jpg",27473060+""},
                                                                        {""+11,"شباب ولاية إنشيري يعتذر عن الحضور لاجتماع دعا له مرشح الحزب الحاكم للمجلس الجهوي ولد بابته","http://www.akhbarwatan.net/%d8%b4%d8%a8%d8%a7%d8%a8-%d9%88%d9%84%d8%a7%d9%8a%d8%a9-%d8%a5%d9%86%d8%b4%d9%8a%d8%b1%d9%8a-%d9%8a%d8%b1%d9%81%d8%b6-%d8%a7%d9%84%d8%ad%d8%b6%d9%88%d8%b1-%d9%84%d8%a7%d8%ac%d8%aa%d9%85%d8%a7%d8%b9/","NULL",""+27473060+""},
                                                                            {""+12,"طفل عبقري يدخل الجامعة لدراسة فيزياء الطب الحيوي","http://atlanticmedia.info/?q=node/28910","http://atlanticmedia.info/sites/default/files/styles/medium/public/field/image/4942732e-7d0c-43da-bc49-9a59d2b0654b_16x9_1200x676.jpeg?itok=7QGpstpx",27473060+""},
                                                                            {""+14,"مجموعة شمس الدين تعبر عن رفضها سحب ترشيح خداد","http://elmourageb.com/25370/%d9%85%d8%ac%d9%85%d9%88%d8%b9%d8%a9-%d8%b4%d9%85%d8%b3-%d8%a7%d9%84%d8%af%d9%8a%d9%86-%d8%aa%d8%b9%d8%a8%d8%b1-%d8%b9%d9%86-%d8%b1%d9%81%d8%b6%d9%87%d8%a7-%d8%b3%d8%ad%d8%a8-%d8%aa%d8%b1%d8%b4%d9%8a","NULL",""+27473060+""},
                                                                            {""+18,"والدة أسامة بن لادن تتحدث للصحافة عنه (قصص مؤثرة )","http://nawafedh.com/?q=node/8428","NULL",""+27473060+""},
                                                                            {""+19,"الأسماء الكاملة للوائح الوطنية لحزب تواصل","http://mourassiloun.com/node/4901","http://mourassiloun.com/sites/default/files/field/image/%D8%AA%D9%88%D8%A7%D8%B5%D9%84_0.jpg",27473060+""},
                                                                            {""+23,"وزراء ومسؤولون سامون يواجهون الحزب الحاكم في نيابيات ألاك (تفاصيل ) ","http://www.eljemhourriya.info/node/3751","http://www.eljemhourriya.info/sites/default/files/11_4.jpeg",27473060+""},
                                                                            {""+25,"خسارة المنتخب الموريتاني ببطولة كوتيف أمام نظيره الأرجنتيني ","http://alakhbar.info/?q=node/12531","http://alakhbar.info/sites/default/files/38284375_1905147932911634_7650150500154736640_n.jpg",27473060+""},
                                                                            {""+65,"فدرالي ولاية أنواكشوط الغربية  يصرح بعد سلسلة إجتماعات  مع الفاعلين السياسين والوجهاء في ولايته","http://www.nouakchottnow.com/node/1613","http://www.nouakchottnow.com/sites/default/files/styles/large/public/field/image/Screenshot_20180728-143936.png?itok=A2ME1cj9",27473060+""},
                                                                            {""+66,"مرشحة الإصلاح على اللائحة الوطنية للنساء خديجة منت محمد الصغير (فى سطور)","http://elmiraat.info/%d9%85%d8%b1%d8%b4%d8%ad%d8%a9-%d8%a7%d9%84%d8%a5%d8%b5%d9%84%d8%a7%d8%ad-%d8%b9%d9%84%d9%89-%d8%a7%d9%84%d9%84%d8%a7%d8%a6%d8%ad%d8%a9-%d8%a7%d9%84%d9%88%d8%b7%d9%86%d9%8a%d8%a9-%d9%84%d9%84%d9%86/","NULL",""+27473060+""},
                                                                                {""+67,"انواكشوط : حادث سير مروع يخلف قتيلا صباح اليوم السبت (صورة من الحادث)","http://elvetach.info/node/9721","http://elvetach.info/sites/default/files/001_2.jpeg",27473060+""},
                                                                                {""+32,"الحزب الحاكم يخسر أبرز رموزه في “بتلميت” وتوتر في “تمبدغه”","http://www.elhourriya.net/77500.html","NULL",""+27473060+""},
                                                                                {""+33,"وفاة شخص وجرح آخرين في حادث سير بقرية بئر الرحمة","http://tawary.com/spip.php?article34890","NULL",""+27473060+""},
                                                                                {""+75,"دعوة لتأجيل الإنتخابات.. ","http://essevir.mr/node/8488","http://essevir.mr/sites/default/files/sites/default/files/img/%D8%B3%D9%8A%D9%86%D9%8A_0.jpg",27473060+""},
                                                                                {""+81,"شركة المياه تزيد فترة الانقطاعات بالتزامن مع عطلة الرئيس","http://nouadibou.info/news/14447-2018-08-04-11-21-45.html","http://nouadibou.info/media/images/mmm_2(1).jpg",27473060+""},
                                                                                {""+83," أحمد طالب ولد سيد ابراهيم يعلن ترشحه للنيابيات في كوبني","http://www.alhakika.info/node/8945","http://www.alhakika.info/sites/default/files/t%C3%A9l%C3%A9chargement%D8%A7%D8%A7%D8%A7%D9%84%D9%84%D8%A8%D9%8A%D9%8A%D9%82.jpg",27473060+""},
                                                                                {""+50,"تواصل يدفع بقيادات نسائية على رأس لائحتهن الوطنية (اللائحة)","http://essirage.net/node/14453","http://essirage.net/sites/files/003_0.jpg",27473060+""},
                                                                                {""+91,"عصابات النهب تهز مفوضية حقوق الانسان والمفوض الجديد خارج الحسبان","http://www.essebil.com/2018/08/04/%d8%b9%d8%b5%d8%a7%d8%a8%d8%a7%d8%aa-%d8%a7%d9%84%d9%86%d9%87%d8%a8-%d8%aa%d9%87%d8%b2-%d9%85%d9%81%d9%88%d8%b6%d9%8a%d8%a9-%d8%ad%d9%82%d9%88%d9%82-%d8%a7%d9%84%d8%a7%d9%86%d8%b3%d8%a7%d9%86-%d9%88/","NULL",""+27473060+""},
                                                                                    {""+92,"نقابة الصحفيين تطالب بالكشف عن ملابسات توقيف ولد الشيخ","http://mauritania13.com/node/9789","http://mauritania13.com/sites/default/files/10494626_445499428967282_6911765519553277218_n.jpg",27473060+""},
                                                                                    {""+95,"18 حزبا يرشح لنيابيات كيفه (أسماء)","http://www.kankossatoday.net/?p=20680","NULL",""+27473060+""},
                                                                                    {""+110,"تعليق حول إشاعة عيد الرئيس فى أطار/بقلم عبد الفتاح ولد اعبيدن","http://alaqssa.org/?q=node/6256","http://alaqssa.org/sites/default/files/styles/medium/public/field/image/60D24544-DAA0-46EC-A2B8-5C269AE7A177.jpeg?itok=ulY4A8MA",27473060+""},
                                                                                    {""+113,"نقابة الصحفيين تطالب السلطات باحترام القانون و نحديد مكان احتجاز الأستاذ الجامعي","https://maurinews.info/news-reports/23306/","NULL",""+27473060+""},
                                                                                        {""+114,"الدكتور محمد فال الحسن أمين الداهي في رثاء فقيد الأمة الدكتور محمد ولد محمد صالح رحمه الله","http://niefrar.org/wordpress/?p=4287","NULL",""+27473060+""},
                                                                                        {""+117,"استشهاد فلسطيني وجرح 120 آخرين برصاص جيش الاحتلال ","http://elmohit.net/node/2288","http://elmohit.net/sites/default/files/32190850003.JPG",27473061+""},
                                                                                        {""+118,"‎عصابة تعتدي على شخص وتسلبه سيارته وما كان بحوزته","http://elbeyan.info/node/4701","http://elbeyan.info/sites/default/files/2D0E1CCA-5A0E-4294-A7E9-1ABEA2758F0B.jpeg",27473061+""},
                                                                                        {""+128,"حصيلة حوادث السير خلال ثلاثة أيام الأخيرة (بيان)","http://sawtchargh.net/archives/25983","NULL",""+27473061+""},
                                                                                        {""+131,"وفاة شخص وعدة جروح في حادث سير(تفاصيل  )","http://al-raya.info/2018/08/04/%d9%88%d9%81%d8%a7%d8%a9-%d8%b4%d8%ae%d8%b5-%d9%88%d8%b9%d8%af%d8%a9-%d8%ac%d8%b1%d9%88%d8%ad-%d9%81%d9%8a-%d8%ad%d8%a7%d8%af%d8%ab-%d8%b3%d9%8a%d8%b1%d8%aa%d9%81%d8%a7%d8%b5%d9%8a%d9%84/","NULL",""+27473061+""},
                                                                                            {""+18,"حزب تواصل يعلن مرشحيه للوائح الوطنية والنساء ونواكشوط (أسماء )","http://nawafedh.com/?q=node/8429","http://nawafedh.com/sites/default/files/%D8%B4%D8%B9%D8%A7%D8%B1%20%D8%AA%D9%88%D8%A7%D8%B5%D9%84.jpg",27822706+""},
                                                                                            {""+32,"موريتانيا تخسر أمام الأرجنتين في بطولة “كوتيف”","http://www.elhourriya.net/77508.html","NULL",""+27822706+""},
                                                                                            {""+66,"موريتانيا تخسر أمام الأرجنتين بهدفين دون رد","http://elmiraat.info/%d9%85%d9%88%d8%b1%d9%8a%d8%aa%d8%a7%d9%86%d9%8a%d8%a7-%d8%aa%d8%ae%d8%b3%d8%b1-%d8%a3%d9%85%d8%a7%d9%85-%d8%a7%d9%84%d8%a3%d8%b1%d8%ac%d9%86%d8%aa%d9%8a%d9%86-%d8%a8%d9%87%d8%af%d9%81%d9%8a%d9%86/","NULL",""+27822707+""},
                                                                                                {""+0,"المتنفذون في الحزب الحاكم: من مصادرة آراء منتسبيهم إلى فرض قراراتهم على الآخرين","http://www.alwiam.info/node/9984","http://www.alwiam.info/sites/default/files/styles/large/public/field/image/LOGO-DE-lUPR-450x300-450x300_0.jpg?itok=XPHbLeyY",28201197+""},
                                                                                                {""+5,"تقرير سري: كوريا الشمالية تحاول بيع أسلحة للحوثيين","http://taqadoum.mr/node/3955","http://taqadoum.mr/sites/default/files/1-1170392.jpg",28201197+""},
                                                                                                {""+13,"استمرار اعتقال مرشح حركة ” نستطيع “للانتخابات التشريعية د. محمد ولد الشيخ – قناة المرابطون","https://www.bellewarmedia.com/%d8%a7%d8%b3%d8%aa%d9%85%d8%b1%d8%a7%d8%b1-%d8%a7%d8%b9%d8%aa%d9%82%d8%a7%d9%84-%d9%85%d8%b1%d8%b4%d8%ad-%d8%ad%d8%b1%d9%83%d8%a9-%d9%86%d8%b3%d8%aa%d8%b7%d9%8a%d8%b9-%d9%84%d9%84%d8%a7%d9%86%d8%aa/","NULL",""+28201197+""},
                                                                                                    {""+14,"تعليق حول إشاعة عيد الرئيس فى أطار….","http://elmourageb.com/25372/%d8%aa%d8%b9%d9%84%d9%8a%d9%82-%d8%ad%d9%88%d9%84-%d8%a5%d8%b4%d8%a7%d8%b9%d8%a9-%d8%b9%d9%8a%d8%af-%d8%a7%d9%84%d8%b1%d8%a6%d9%8a%d8%b3-%d9%81%d9%89-%d8%a3%d8%b7%d8%a7%d8%b1","NULL",""+28201197+""},
                                                                                                    {""+15,"نتيجة تفاقم الموت في الحوادث ناشطون يدقون ناقوس الخطر(أرقام )","http://amicinfo.com/node/19010","http://amicinfo.com/sites/default/files/hh/436x328_59478_252319.jpg",28201197+""},
                                                                                                    {""+16,"الجزائر تشدد الرقابة على طائرات قطر والإمارات وتركيا","http://www.tawassoul.net/ar/%D8%A3%D8%AE%D8%A8%D8%A7%D8%B1-%D8%B9%D8%B1%D8%A8%D9%8A%D8%A9/%D8%A7%D9%82%D8%AA%D8%B5%D8%A7%D8%AF/item/29201-%D8%A7%D9%84%D8%AC%D8%B2%D8%A7%D8%A6%D8%B1-%D8%AA%D8%B4%D8%AF%D8%AF-%D8%A7%D9%84%D8%B1%D9%82%D8%A7%D8%A8%D8%A9-%D8%B9%D9%84%D9%89-%D8%B7%D8%A7%D8%A6%D8%B1%D8%A7%D8%AA-%D9%82%D8%B7%D8%B1-%D9%88%D8%A7%D9%84%D8%A5%D9%85%D8%A7%D8%B1%D8%A7%D8%AA-%D9%88%D8%AA%D8%B1%D9%83%D9%8A%D8%A7.html","http://www.tawassoul.net/media/k2/items/cache/af145fdd9e5d3283ecc498c8d07a807e_S.jpg",28201197+""},
                                                                                                    {""+18,"ولد اسويد أحمد يدعو للتصويت لمرشحي حزب الاتحاد بتكانت ","http://nawafedh.com/?q=node/8431","NULL",""+30269317+""},
                                                                                                    {""+19,"المجلس الأعلى ينظم دورة تحسيسية بمدينة أطار حول مشاركة الشباب في الحياة السياسية ","http://mourassiloun.com/node/4902","http://mourassiloun.com/sites/default/files/styles/large/public/field/image/%D8%A3%D8%B7%D8%A7%D8%B1%20%D9%80%20%D8%A7%D9%84%D9%85%D8%AC%D9%84%D8%B3.jpg?itok=32W0hRe7",30269317+""},
                                                                                                    {""+21,"والدة بن لادن تكشف سرا خاصا من حياته (صورة)","http://houriyamedia.info/node/4112","NULL",""+30269317+""},
                                                                                                    {""+25,"حملة معا للحد من حوادث السير: يوم أمس شهد حوادث سير متعددة ","http://alakhbar.info/?q=node/12533","http://alakhbar.info/sites/default/files/38297892_2063710433640571_1219076842917462016_n.jpg",30269317+""},
                                                                                                    {""+143,"الطرق الرديئة بموريتانيا تقتل سيدة آخر ى قرب بوتلميت بعد مقتل شاب بالأمس على طريق نواذيبو","http://elistitlaa.info/index.php?option=com_content&view=article&id=6840:2018-08-04-12-53-21&catid=1:2013-08-10-19-14-08&Itemid=1","http://www.alwiam.info/sites/default/files/styles/large/public/field/image/38465692_1834228863279854_7794234936264753152_n.jpg?itok=h5-9r7Vc",30269319+""},
                                                                                                    {""+75,"الشيخ الددو: التصويت على أساس مصلحة دنيوية من اكبر الكبائر ","http://essevir.mr/node/8491","http://essevir.mr/sites/default/files/sites/default/files/img/t%C3%A9l%C3%A9chargement%20%288%29_2.jpg",176738947+""},
                                                                                                    {""+110,"بعد التجميد المثير للجدل قانونيا و انتخابيا إلى أين يتجه الإعصار الأطاري /بقلم عبد الفتاح ولد اعبيدن ","http://alaqssa.org/?q=node/6257","http://alaqssa.org/sites/default/files/styles/medium/public/field/image/76A2187D-F372-4AC8-B3F3-3D8AFFD6D5F2.jpeg?itok=toJ4Pkuc",176738947+""},
                                                                                                    {""+144,"كيفه : مرشح الحزب الحاكم جمال ولد كبود يعتذر للجميع","http://www.wateni.com/2018/08/%d9%83%d9%8a%d9%81%d9%87-%d9%85%d8%b1%d8%b4%d8%ad-%d8%a7%d9%84%d8%ad%d8%b2%d8%a8-%d8%a7%d9%84%d8%ad%d8%a7%d9%83%d9%85-%d8%ac%d9%85%d8%a7%d9%84-%d9%88%d9%84%d8%af-%d9%83%d8%a8%d9%88%d8%af-%d9%8a/","NULL",""+176738947+""},
                                                                                                        {""+76,"رئيس المنظومة يشكر رفاقه ويعلن الانتظام في إطار شباب UPR","http://zouerate.info/node/4383","http://zouerate.info/sites/default/files/styles/medium/public/%D9%A2%D9%A0%D9%A1%D9%A8%D9%A0%D9%A6%D9%A2%D9%A8_%D9%A2%D9%A3%D9%A0%D9%A8%D9%A2%D9%A0_0.jpg?itok=ESJB3OIx",176738947+""},
                                                                                                        {""+20,"حق الرد.. تكذيب لاجتماع ..","http://alaraby.info/node/7081","http://alaraby.info/sites/default/files/download-%D9%A9.jpg",176738947+""},
                                                                                                        {""+111,"فيديو.. نجاة الرئيس الفنزويلي مادورو من محاولة اغتيال","http://sahelnews.info/node/4268","http://sahelnews.info/sites/default/files/venez.jpg",176738947+""},
                                                                                                        {""+21,"اتهام مفوض توجنين 3 وعرفات 4 بالتقصير رغم جهود الادارة العامة في مكافحة الجريمة","http://houriyamedia.info/node/4115","http://houriyamedia.info/sites/default/files/5_25.jpg",176738947+""},
                                                                                                        {""+78,"عمال الموانئ في موريتانيا مستمرون في إضرابهم و يطالبون بتحسين ظروفهم المعيشية","http://www.tawatur.net/عمال-الموانئ-في-موريتانيا-مستمرون-في-إضرابهم-و-يطالبون-بتحسين-ظروفهم-المعيشية","https://i.ytimg.com/vi/NSk_XgQkgYA/sddefault.jpg",176738947+""},
                                                                                                        {""+112,"موريتانيا: 100 حزب سياسي يتنافسون في الانتخابات التشريعية","http://elhiyad.info/node/5310","http://elhiyad.info/sites/default/files/%D9%84%D8%AC%D9%86%D8%A9%20%D8%A7%D9%84%D8%A7%D9%86%D8%AA%D8%AE%D8%A7%D8%A8%D8%A7%D8%AA_5.jpeg",176738947+""},
                                                                                                        {""+145,"أوقفوا حصاد الأرواح ياقوم","http://echamel.info/?p=595","NULL",""+176738947+""},
                                                                                                        {""+22,"رسالة من العلامة الشيخ الددو.. إعلاما عام ..","http://elilam.net/node/4388","http://elilam.net/sites/default/files/%D8%A7%D9%84%D8%AF%D8%AF%D9%881.jpg",176738947+""},
                                                                                                        {""+36,"محاولة اغتيال رئيس فنزويلا بواسطة طائرة بدون طيار","http://royapost.net/89332-2/","NULL",""+176738947+""},
                                                                                                            {""+79,"تفاعلا مع ترشح الخليل النحوي: في كسر جمود النخبة العلمية / بون ولد باهي","http://souhoufi.com/article15106.html","NULL",""+176738947+""},
                                                                                                            {""+37,"صحيفة:  اللواء الغزواني سيكون خليفة الرئيس عزيز","http://www.elmouritany.info/node/3877","http://www.elmouritany.info/sites/default/files/styles/large/public/field/image/kzwwwww_0.jpg?itok=wDXeEnzi",176738947+""},
                                                                                                            {""+23,"بوعماتو: بداية النهاية","http://www.eljemhourriya.info/node/3754","http://www.eljemhourriya.info/sites/default/files/11_95.jpg",176738947+""},
                                                                                                            {""+113,"إضاعة الدولة وتضييع الأمة سعيد يقطين","https://maurinews.info/ararticles/23350/","NULL",""+176738947+""},
                                                                                                                {""+25,"مالي: سومايلا سيسي يتقدم ب20 طعنا أمام المحكمة الدستورية","http://alakhbar.info/?q=node/12549","http://alakhbar.info/sites/default/files/Soumaila%20Cisse%CC%81%20jpeg.jpg",176738947+""},
                                                                                                                {""+80,"وزير الأشغال اليمني يشيد بجهود الإمارات الداعمة لبلاده في كافة المجالات","http://www.essada.info/%d9%88%d8%b2%d9%8a%d8%b1-%d8%a7%d9%84%d8%a3%d8%b4%d8%ba%d8%a7%d9%84-%d8%a7%d9%84%d9%8a%d9%85%d9%86%d9%8a-%d9%8a%d8%b4%d9%8a%d8%af-%d8%a8%d8%ac%d9%87%d9%88%d8%af-%d8%a7%d9%84%d8%a5%d9%85%d8%a7%d8%b1","NULL",""+176738947+""},
                                                                                                                {""+38,"فيديو.. برودة أعصاب “قاتلة” من حارس الرئيس مادور خلال محاولة اغتياله","http://sondagemr.net/?p=17206","NULL",""+176738947+""},
                                                                                                                {""+26,"من المواطنة زينب، إلى رئيس الجمهورية!","http://nouadhiboutoday.info/node/4227","http://nouadhiboutoday.info/sites/default/files/FB_IMG_1533470838035.jpg",176738947+""},
                                                                                                                {""+27,"موريتانيا تخسر أمام الأرجنتين بهدفين دون رد/إينشيري","http://inchiri.net/node/1392","http://inchiri.net/sites/default/files/clubmauritanie.jpg",176738947+""},
                                                                                                                {""+114,"المهندس الأديب محمدن بن محمد سيديا “أد” في رثاء الفقيد محمد بن محمد صالح رحمه الله","http://niefrar.org/wordpress/?p=4306","NULL",""+176738947+""},
                                                                                                                {""+84,"وفاة طالب موريتاني بأمريكا - صورة + تفاصيل ","http://lebjawi.info/index.php/ar/actualites-mauritanie/1270-2018-08-05-12-57-38","http://lebjawi.info/images/taleb00000000000.jpg",176738947+""},
                                                                                                                {""+28,"همسة في أذن ناخب!/بقلم: عبد القادر ولد الصيام","http://kiffainfo.net/article22450.html","NULL",""+176738947+""},
                                                                                                                {""+83," وفاة طالب موريتانى برصاص مجهول ","http://www.alhakika.info/node/8950","http://www.alhakika.info/sites/default/files/FB_IMG_1533463462280.jpg",176738947+""},
                                                                                                                {""+29,"عاجل الشيخ الددو يوجه رسالة إلى جميع المترشحين و الناخبين في موريتانيا .. (نص الرسالة)","http://elitissal.net/archives/22071","NULL",""+176738947+""},
                                                                                                                {""+86," معطيات جديدة حول توقيت تقاعد قائد الجيش الموريتاني ومستقبله بعد التقاعد","http://elhadeth.mr/node/8285","http://elhadeth.mr/sites/default/files/%D8%BA%D8%B2%D9%88%D8%A7%D9%86%D9%8A_0.jpg",176738947+""},
                                                                                                                {""+41,"الفوج الأول من الحجاج الموريتانيين يغادر إلى الديار المقدسة","http://echourouqmedia.net/?q=node/10262","http://echourouqmedia.net/sites/default/files/heja001-1-780x405.jpg",176738947+""},
                                                                                                                {""+30,"أمير اترارزه يثمن ترشيح ولد  ابراهيم ولد السيد على رأس لائحة المجلس الجهوي  ","http://www.rkizinfo.com/node/2425","http://www.rkizinfo.com/sites/default/files/styles/large/public/field/image/IMG-20180805-WA0004.jpg?itok=6zZlPlr5",176738947+""},
                                                                                                                {""+117,"اختتام فعاليات الدورة الأدبية الثالثة ببيت شعر نواكشوط","http://elmohit.net/node/2290","http://elmohit.net/sites/default/files/685426.jpg",176738947+""},
                                                                                                                {""+87,"الفوج الاول من حجاجنا يصل الى المدينة المنورة","http://www.tabrenkout.com/?p=13613","NULL",""+176738947+""},
                                                                                                                {""+43,"جدل في الصين بعد توجيه إهانات عنصرية للاعب سنغالي","https://www.saharamedias.net/%d8%ac%d8%af%d9%84-%d9%81%d9%8a-%d8%a7%d9%84%d8%b5%d9%8a%d9%86-%d8%a8%d8%b9%d8%af-%d8%aa%d9%88%d8%ac%d9%8a%d9%87-%d8%a5%d9%87%d8%a7%d9%86%d8%a7%d8%aa-%d8%b9%d9%86%d8%b5%d8%b1%d9%8a%d8%a9-%d9%84%d9%84/","NULL",""+176738947+""},
                                                                                                                    {""+118,"يسري فودة يعلن عن توقف برنامجه “السلطة الخامسة”","http://elbeyan.info/node/4705","http://elbeyan.info/sites/default/files/C3CAD4AB-9DA2-4D58-B9C2-215F71730995.jpeg",176738947+""},
                                                                                                                    {""+88,"رصاصة طائشة تودي بحياة طالب موريتاني (صورة+ هوية)","http://elistiklal.info/node/2286","http://elistiklal.info/sites/default/files/styles/large/public/field/image/%D8%B9%D9%84%D9%8A%D9%88%D9%86%20%D8%AC%D8%A8%D8%B1%D9%8A%D9%84.jpg?itok=XjWANtON",176738947+""},
                                                                                                                    {""+44,"وفاة طالب موريتانى برصاص مجهول","http://www.zahraa.mr/node/17464","http://www.zahraa.mr/sites/default/files/FB_IMG_1533463462280.jpg",176738947+""},
                                                                                                                    {""+32,"هوية شاب موريتاني قتلته رصاصة طائشة بالولايات المتحدة","http://www.elhourriya.net/77603.html","NULL",""+176738947+""},
                                                                                                                    {""+45,"العبد الفقير والهروب إلى الأمام -افتتاحية جديد اليوم - ","http://jedidtoday.com/node/2495","http://jedidtoday.com/sites/default/files/IMG_6791.JPG",176738947+""},
                                                                                                                    {""+33,"قتلى وجرحى في حادثة سير مميتة بطريق الأمل","http://tawary.com/spip.php?article34893","NULL",""+176738947+""},
                                                                                                                    {""+46,"ارتجالية تحضير موسم الحج هذه السنة","http://meyadin.net/node/12836","http://meyadin.net/sites/default/files/ahllld.jpg",176738947+""},
                                                                                                                    {""+47,"حزب الفضيلة يرشح شخصيات مشهورة على لوائحه(الاسماء)","http://eljewahir.com/node/10031","http://eljewahir.com/sites/default/files/styles/large/public/field/image/arton9795%281%29.jpg?itok=slHnODif",176738947+""},
                                                                                                                    {""+121,"طفل مكسيكي يدخل التاريخ","http://newsmaghreb.info/index.php/2013-07-14-11-16-26/16737-2018-08-05-00-52-38.html","https://newsmedia.tasnimnews.com/Tasnim/Uploaded/Image/1397/05/14/1397051400105094214938154.jpeg",176738947+""},
                                                                                                                    {""+48,"العلامة الددو .. يوجه رسالة غير مُشفرة لساسة موريتانيا ","http://essabq.info/node/6796","http://essabq.info/sites/default/files/t%C3%A9l%C3%A9chsddgfd.jpg",176738947+""},
                                                                                                                    {""+91,"آمرج ..مرض غامض يبيد الحيوانات ووزارة البيطرة عاجزة ومشلولة","http://www.essebil.com/2018/08/05/%d8%a2%d9%85%d8%b1%d8%ac-%d9%85%d8%b1%d8%b6-%d8%ba%d8%a7%d9%85%d8%b6-%d9%8a%d8%a8%d9%8a%d8%af-%d8%a7%d9%84%d8%ad%d9%8a%d9%88%d8%a7%d9%86%d8%a7%d8%aa-%d9%88%d8%b2%d8%a7%d8%b1%d8%a9-%d8%a7%d9%84%d8%a8/","NULL",""+176738947+""},
                                                                                                                        {""+122,"حزب الفضيلة يكشف عن ابرز مرشيحيه للنيابيات والبلدية","http://nemaelan.info/node/2038","http://nemaelan.info/sites/default/files/C6EE2F0E-7CA6-4F3C-9A07-EF4C2F4E6F33_0.jpeg",176738947+""},
                                                                                                                        {""+49,"صورة الطالب الموريتاني الذي قتل برصاصة","http://mushahide.com/node/14913","http://mushahide.com/sites/default/files/styles/medium/public/FB_IMG_1533463462280.jpg?itok=GXco72hD",176738947+""},
                                                                                                                        {""+92,"الاماكن السياحية في مدينة نواذيبو – تقرير الموريتانية","http://mauritania13.com/node/9796","//www.youtube.com/embed/wgFLzv7tEmk?width%3D640%26amp%3Bheight%3D360%26amp%3Bautoplay%3D0%26amp%3Bvq%3Dlarge%26amp%3Brel%3D0%26amp%3Bcontrols%3D1%26amp%3Bautohide%3D2%26amp%3Bshowinfo%3D1%26amp%3Bmodestbranding%3D0%26amp%3Btheme%3Ddark%26amp%3Biv_load_policy%3D1%26amp%3Bwmode%3Dopaque",176738947+""},
                                                                                                                        {""+50," كيتا وسوميلا في جولة ثانية من الانتخابات الرئاسية المالية","http://essirage.net/node/14460","http://essirage.net/sites/files/0208-mali-ibk_0.jpg",176738947+""},
                                                                                                                        {""+51,"موريتانيا: الفوج الأول من الحجاج يغادر نواكشوط متوجها إلى الديار المقدسة","http://elwatan.info/node/10972","http://elwatan.info/sites/default/files/heja001.jpg",176738947+""},
                                                                                                                        {""+52,"تميم موزة أوصلته إدارة أبوما الحكم بعد حرق ميلشيات أبيه للسفير الأمريكي في بنغازي","http://mauripress.net/node/99","http://mauripress.net/sites/default/files/TEM.jpg",176738947+""},
                                                                                                                        {""+54,"الإعلان عن وفاة الشيخ سدينا ولد سيدي محمد ولد أعمر في مال","http://www.elbadil.info/2013/index.php/2015-02-10-12-43-52/18710-2018-08-05-15-24-53.html","http://www.elbadil.info/2013/media/images/IMA.jpg",176738947+""},
                                                                                                                        {""+125,"الاتحادية الموريتانية لسباق الدراجات الهوائية توزع جوائز على الفائزين بمدينة انواذيبو","http://www.ndbnews.info/node/1550","http://www.ndbnews.info/sites/default/files/styles/large/public/field/image/DSC06840.jpg?itok=na1jJSH7",176738947+""},
                                                                                                                        {""+126,"أول فوج من حجاج بيت الله يغادر العاصمة انواكشوط","http://elhawadith.info/node/3229","http://elhawadith.info/sites/default/files/5555_0_0.jpg",176738947+""},
                                                                                                                        {""+55,"مدون يطالبون رئيس الجمهورية بزيارة مفاجئة لطريق الأمل","http://28novembre.info/node/11854","http://28novembre.info/sites/default/files/%D8%AD%D9%88%D8%A7%D8%AF%D8%AB%20%D8%A7%D9%84%D8%B3%D9%8A%D8%B1_0.jpg",176738947+""},
                                                                                                                        {""+95,"بلدية هامد : انضمامات نوعية لحلف “ولد اعليوه”","http://www.kankossatoday.net/?p=20689","NULL",""+176738947+""},
                                                                                                                        {""+127,"حوادث السير في موريتانيا حرب تحصد الأرواح كل ساعة","http://mithak.com/node/5145","http://mithak.com/sites/default/files/015_52.jpg",176738947+""},
                                                                                                                        {""+128,"رسالة هامة الى المترشحين مفادها","http://sawtchargh.net/archives/26040","NULL",""+176738947+""},
                                                                                                                        {""+56,"كيفه: الدكتور جمال يتحدث للوسط عن المغاضبين و عن برنامجه و حظوظه في الفوز(مقابلة)","http://www.elwassat.info/index.php/3amme/13232-2018-08-05-08-34-31","https://scontent-cdt1-1.xx.fbcdn.net/v/t1.15752-9/38457899_863638020498725_5229715830748479488_n.jpg?_nc_cat=0&amp;oh=d9c33b277500a7cf0ad0dd6567fd9577&amp;oe=5BC6B700",176738947+""},
                                                                                                                        {""+129,"دراسة: الهواتف الذكية تسبب الاكتئاب للأطفال","http://chinguitty.net/article833.html","NULL",""+176738947+""},
                                                                                                                        {""+99,"لاول مرة موريتانيا تدخل التصنيف الدولي للاعبي التنس","http://amisports.net/index.php/2015-08-14-16-09-04/1575-2018-08-04-13-37-01.html","http://amisports.net/images/iooo/38448609_2255897391092079_3797320808485879808_n.jpg",176738947+""},
                                                                                                                        {""+57,"من أسباب حوادث السير في البلاد!!!","http://elghavila.info/?p=36290","NULL",""+176738947+""},
                                                                                                                        {""+130," وفاة طالب موريتانى برصاص مجهول ","http://anbaatlas.com/node/6060","http://anbaatlas.com/sites/default/files/FB_IMG_1533463462280.jpg",176738947+""},
                                                                                                                        {""+131,"حزب التحالف الشعبي ينظم مؤتمرا صحفيا بانواذيبو يتحول إلى مهرجان (صور )","http://al-raya.info/2018/08/05/%d8%ad%d8%b2%d8%a8-%d8%a7%d9%84%d8%aa%d8%ad%d8%a7%d9%84%d9%81-%d8%a7%d9%84%d8%b4%d8%b9%d8%a8%d9%8a-%d9%8a%d9%86%d8%b8%d9%85-%d9%85%d8%a4%d8%aa%d9%85%d8%b1%d8%a7-%d8%b5%d8%ad%d9%81%d9%8a%d8%a7-%d8%a8/","NULL",""+176738947+""},
                                                                                                                            {""+101,"“الإيبولا” تظهر من جديد شرق الكونغو بعد القضاء عليها في الشمال","http://arayalmostenir.com/node/6197","http://arayalmostenir.com/sites/default/files/GHJK_15.jpg",176738947+""},
                                                                                                                            {""+133,"افريقيا تبحث تعزيز نموها الاقتصادي مع المؤسسات النقدية","http://birmoghrein.info/index.php?option=com_content&view=article&id=1506:2018-08-05-13-51-55&catid=2&Itemid=103","http://filear.ami.mr/miniatures170/05-08-2018-m02.jpg",176738947+""},
                                                                                                                            {""+103,"رسالة من الفتاة زينب الى رئيس الجمهورية..تدمى لها القلوب","http://sawtak.info/article3580.html","NULL",""+176738947+""},
                                                                                                                            {""+106,"إلى وزير التجهيز والنقل : أين فرق الإنقاذ ؟ /","http://elayam.info/index.php/2016-06-15-16-32-41/item/3356-2018-08-05-09-32-45","http://elayam.info/media/k2/items/cache/76a9f3192c11f934bf5a64b69258bd06_S.jpg",176738947+""},
                                                                                                                            {""+109,"أهل أحمد درگل يشكرون المعزين على مواساتهم في مصابهم الجلل","http://www.guerou.info/?p=12697","NULL",""+176738947+""},
                                                                                                                            {""+63,"رصاصة مجهولة تودي بحياة طالب موريتانى(صورة)","http://ethaira.info/spip.php?article11569","NULL",""+176738947+""},
                                                                                                                            {""+64,"من المواطنة زينب، إلى رئيس الجمهورية! ” رسالة مؤثرة","http://elhakika.info/2018/08/05/%d9%85%d9%86-%d8%a7%d9%84%d9%85%d9%88%d8%a7%d8%b7%d9%86%d8%a9-%d8%b2%d9%8a%d9%86%d8%a8%d8%8c-%d8%a5%d9%84%d9%89-%d8%b1%d8%a6%d9%8a%d8%b3-%d8%a7%d9%84%d8%ac%d9%85%d9%87%d9%88%d8%b1%d9%8a%d8%a9/","NULL",""+176738947+""},
                                                                                                                                {""+65,"حالة وفاة وإصابات في حادث سير قرب أبي تلميت","http://www.nouakchottnow.com/node/1615","http://www.nouakchottnow.com/sites/default/files/styles/large/public/field/image/38465692_1834228863279854_7794234936264753152_n.jpg?itok=hcNXD75y",176738947+""},
                                                                                                                                {""+66,"رصاصة طائشة تودي بحياة شاب موريتاني (صورة)","http://elmiraat.info/%d8%b1%d8%b5%d8%a7%d8%b5%d8%a9-%d8%b7%d8%a7%d8%a6%d8%b4%d8%a9-%d8%aa%d9%88%d8%af%d9%8a-%d8%a8%d8%ad%d9%8a%d8%a7%d8%a9-%d8%b4%d8%a7%d8%a8-%d9%85%d9%88%d8%b1%d9%8a%d8%aa%d8%a7%d9%86%d9%8a-%d8%b5%d9%88/","NULL",""+176738947+""},
                                                                                                                                    {""+67,"وفاة طالب موريتانى برصاص مجهول في الولايات المتحدة (صورة وهوية المعني)","http://elvetach.info/node/9742","http://elvetach.info/sites/default/files/1_549.jpg",176738947+""},
                                                                                                                                    {""+68,"عاجل:أطار  أحد مرشحي حزب الكرامة في يجمد ترشحه (وثيقة)","http://mauri7.info/ar/2018/08/05/%d8%b9%d8%a7%d8%ac%d9%84%d8%a3%d8%b7%d8%a7%d8%b1-%d8%a3%d8%ad%d8%af-%d9%85%d8%b1%d8%b4%d8%ad%d9%8a-%d8%ad%d8%b2%d8%a8-%d8%a7%d9%84%d9%83%d8%b1%d8%a7%d9%85%d8%a9-%d9%81%d9%8a-%d9%8a%d8%ac%d9%85%d8%af/#utm_source=rss&utm_medium=rss&utm_campaign=%25d8%25b9%25d8%25a7%25d8%25ac%25d9%2584%25d8%25a3%25d8%25b7%25d8%25a7%25d8%25b1-%25d8%25a3%25d8%25ad%25d8%25af-%25d9%2585%25d8%25b1%25d8%25b4%25d8%25ad%25d9%258a-%25d8%25ad%25d8%25b2%25d8%25a8-%25d8%25a7%25d9%2584%25d9%2583%25d8%25b1%25d8%25a7%25d9%2585%25d8%25a9-%25d9%2581%25d9%258a-%25d9%258a%25d8%25ac%25d9%2585%25d8%25af","NULL",""+176738947+""},
                                                                                                                                    {""+69,"العلامة الددو يوجه رسالة إلى جميع المترشحين و الناخبين في موريتانيا [نص الرسالة]","https://chinguitmedia.com/2018/08/05/13724/","NULL",""+176738947+""},
                                                                                                                                        {""+70,"أكجوجت : بعثة من المجلس الأعلى للشباب تعقد اجتماعا تحسيسيا حول المشاركة السياسية (صور)","http://www.rabi3.net/%d8%a3%d9%83%d8%ac%d9%88%d8%ac%d8%aa-%d8%a8%d8%b9%d8%ab%d8%a9-%d9%85%d9%86-%d8%a7%d9%84%d9%85%d8%ac%d9%84%d8%b3-%d8%a7%d9%84%d8%a3%d8%b9%d9%84%d9%89-%d9%84%d9%84%d8%b4%d8%a8%d8%a7%d8%a8-%d8%aa%d8%b9/","NULL",""+176738947+""},
                                                                                                                                            {""+71,"وفاة تسعة أشخاص في حادث سير على طريق الأمل","http://alikhbari.net/node/12352","http://alikhbari.net/sites/default/files/Capture_0_0.PNG",176738947+""},
                                                                                                                                            {""+72," التكتل يعلن عن لائحته المرشحة للنيابيات في ازويرات","http://zoueratemedia.info/node/2122","http://zoueratemedia.info/sites/default/files/IMG-20180804-WA0068.jpg",176738947+""},
                                                                                                                                            {""+73,"أبرز أطر UPR المذرذرة يقاطعون اجتماع الفيدرالي","http://tiguend.com/2018/08/%d8%a3%d8%a8%d8%b1%d8%b2-%d8%a3%d8%b7%d8%b1-upr-%d8%a7%d9%84%d9%85%d8%b0%d8%b1%d8%b0%d8%b1%d8%a9-%d9%8a%d9%82%d8%a7%d8%b7%d8%b9%d9%88%d9%86-%d8%a7%d8%ac%d8%aa%d9%85%d8%a7%d8%b9-%d8%a7%d9%84%d9%81/","http://tiguend.com/wp-content/uploads/2018/08/WhatsApp-Image-2018-08-05-at-2.16.19-PM-3-300x169.jpeg",176738947+""},
                                                                                                                                            {""+2,"الفوج الأول من حجاجنا يغادر البلاد إلى الديار المقدسة ","http://aghchorguit.info/node/2942","http://aghchorguit.info/sites/default/files/heja001.jpg",177333034+""},
                                                                                                                                            {""+45,"رسالة مواطنة فقدت أباها وأخاها في حادثين منفصلين","http://jedidtoday.com/node/2496","http://jedidtoday.com/sites/default/files/IMG_6794.JPG",177333035+""},
                                                                                                                                            {""+3,"الحجاج الموريتانيون يبدؤون المغادرة إلى الديار المقدسة","http://essahraa.net/node/2310","http://essahraa.net/sites/default/files/%D8%AA%D9%86%D8%B2%D9%8A%D9%84%20%282%29.jpeg",177333034+""},
                                                                                                                                            {""+4,"الشيخ الددو يوجه رسالة إلى الناخبين والمترشحين","http://elhora.info/?p=15752","NULL",""+177333034+""},
                                                                                                                                            {""+5,"المجلس الأعلى للشباب، نبل الأهداف.. ورداءة التنفيذ","http://taqadoum.mr/node/3965","http://taqadoum.mr/sites/default/files/%D9%85%D8%AC%D9%84%D8%B3%20%D8%A7%D9%84%D8%B4%D8%A8%D8%A7%D8%A8.jpg",177333034+""},
                                                                                                                                            {""+6,"فيديو محاولة اغتيال رئيس فنزويلا على المنصة ","http://www.aqlame.com/article38343.html","NULL",""+177333034+""},
                                                                                                                                            {""+7,"مصر : ولد أجاي يشارك في اجتماع التجمع الأفريقي لمجموعة البنك الدولي وصندوق النقد الدولي حول تعزيز فرص النمو الاقتصادي الشامل في افريقيا.","http://rimtoday.net/?q=node/18196","http://rimtoday.net/sites/default/files/styles/large/public/field/image/5D1FA36A-5D39-496B-B2EA-E4E093097034.jpeg?itok=rehkYB85",177333034+""},
                                                                                                                                            {""+9,"الشيخ الددو يوجه رسالة إلى جميع المترشحين و الناخبين في موريتانيا .. (نص الرسالة)","http://essaha.info/node/4250","http://essaha.info/sites/default/files/2070_0.jpg",177333034+""},
                                                                                                                                            {""+11,"موجة غضب كبيرة  على  الفيسبوك بسبب تجاهل الحكومة لمجزرة طريق الأمل ومطالب بزيارة مفاجئة للرئيس لها","http://www.akhbarwatan.net/%d9%85%d9%88%d8%ac%d8%a9-%d8%ba%d8%b6%d8%a8-%d9%83%d8%a8%d9%8a%d8%b1%d8%a9-%d8%b9%d9%84%d9%89-%d8%a7%d9%84%d9%81%d9%8a%d8%b3%d8%a8%d9%88%d9%83-%d8%a8%d8%b3%d8%a8%d8%a8-%d8%aa%d8%ac%d8%a7%d9%87%d9%84/","NULL",""+177333034+""},
                                                                                                                                                {""+12,"ولد ابراهيم السيد بلا منافس في منصب رئيس المجلس الجهوي على مستوى اترارزة","http://atlanticmedia.info/?q=node/28927","http://atlanticmedia.info/sites/default/files/styles/medium/public/field/image/fki.jpg?itok=kiZOaVI3",177333034+""},
                                                                                                                                                {""+13,"يوم جديد مع المهندس العاقب ولد دحود – قناة الموريتانية","https://www.bellewarmedia.com/%d9%8a%d9%88%d9%85-%d8%ac%d8%af%d9%8a%d8%af-%d9%85%d8%b9-%d8%a7%d9%84%d9%85%d9%87%d9%86%d8%af%d8%b3-%d8%a7%d9%84%d8%b9%d8%a7%d9%82%d8%a8-%d9%88%d9%84%d8%af-%d8%af%d8%ad%d9%88%d8%af-%d9%82%d9%86/","NULL",""+177333034+""},
                                                                                                                                                    {""+117,"محاولة اغتيال رئيس فنزويلا بطائرة بدون طيار (فيديو)","http://elmohit.net/node/2291","http://elmohit.net/sites/default/files/arton38343.jpg",177333035+""},
                                                                                                                                                    {""+14,"فوز لريال مدريد على يوفنتوس وسقوط تاني لبرشلونة في الكاس الدولية للابطال","http://elmourageb.com/25388/%d9%81%d9%88%d8%b2-%d9%84%d8%b1%d9%8a%d8%a7%d9%84-%d9%85%d8%af%d8%b1%d9%8a%d8%af-%d8%b9%d9%84%d9%89-%d9%8a%d9%88%d9%81%d9%86%d8%aa%d9%88%d8%b3-%d9%88%d8%b3%d9%82%d9%88%d8%b7-%d8%aa%d8%a7%d9%86%d9%8a","NULL",""+177333034+""},
                                                                                                                                                    {""+15,"معطيات جديدة حول توقيت تقاعد قائد الجيش الموريتاني ومستقبله بعد التقاعد","http://amicinfo.com/node/19018","http://amicinfo.com/sites/default/files/hh/kzwwwww_0.jpg",177333034+""},
                                                                                                                                                    {""+16,"بالفيديو: نجاة رئيس فنزويلا من محاولة اغتيال بطائرة مسيرة","http://www.tawassoul.net/ar/%D8%A7%D9%84%D9%82%D8%A7%D8%B1%D8%A9-%D8%A7%D9%84%D8%A2%D9%85%D8%B1%D9%8A%D9%83%D9%8A%D8%A9/%D8%AD%D9%88%D8%A7%D8%AF%D8%AB-%D9%88-%D8%BA%D8%B1%D8%A7%D8%A6%D8%A8/item/29210-%D8%A8%D8%A7%D9%84%D9%81%D9%8A%D8%AF%D9%8A%D9%88-%D9%86%D8%AC%D8%A7%D8%A9-%D8%B1%D8%A6%D9%8A%D8%B3-%D9%81%D9%86%D8%B2%D9%88%D9%8A%D9%84%D8%A7-%D9%85%D9%86-%D9%85%D8%AD%D8%A7%D9%88%D9%84%D8%A9-%D8%A7%D8%BA%D8%AA%D9%8A%D8%A7%D9%84-%D8%A8%D8%B7%D8%A7%D8%A6%D8%B1%D8%A9-%D9%85%D8%B3%D9%8A%D8%B1%D8%A9.html","http://www.tawassoul.net/media/k2/items/cache/2721fccdbcf8fb391b7c56fd209513f4_S.jpg",177333034+""},
                                                                                                                                                    {""+18,"أمريكا : وفاة طالب موريتاني برصاصة طائشة (اسمه وصورته )","http://nawafedh.com/?q=node/8441","http://nawafedh.com/sites/default/files/FB_IMG_1533463462280.jpg",177333034+""},
                                                                                                                                                    {""+19,"رابطة تخليد المقاومة تعزي في وفاة ابنة احد  ابطال المقاومة","http://mourassiloun.com/node/4910","NULL",""+177333034+""},
                                                                                                                                                    {""+3,"استطلاع: برلمانيو CEDEAO يؤيدون عودة موريتانيا","http://essahraa.net/node/2321","http://essahraa.net/sites/default/files/%D8%AA%D9%86%D8%B2%D9%8A%D9%84%20%283%29.png",178235847+""},
                                                                                                                                                    {""+10,"رسالة إليّ رئيس الجمهورية ،،، (رسالة مؤثرة)","http://www.al-maraabimedias.net/?p=76780","NULL",""+178235847+""},
                                                                                                                                                    {""+4,"ثاني فوج من حجاج موريتانيا يتوجه إلى الديار المقدسة","http://elhora.info/?p=15766","NULL",""+178235847+""},
                                                                                                                                                    {""+5,"6 أغسطس عزيز رئيسا لنواكشوط .. وولادة حلم «موريتانيا الجديدة»","http://taqadoum.mr/node/3968","http://taqadoum.mr/sites/default/files/coup_0.jpg",178235847+""},
                                                                                                                                                    {""+11,"فضيحة من العيار الثقيل بطلها وزير التشغيل ولد محمد خونه ” مثير “","http://www.akhbarwatan.net/%d9%81%d8%b6%d9%8a%d8%ad%d8%a9-%d9%85%d9%86-%d8%a7%d9%84%d8%b9%d9%8a%d8%a7%d8%b1-%d8%a7%d9%84%d8%ab%d9%82%d9%8a%d9%84-%d8%a8%d8%b7%d9%84%d9%87%d8%a7-%d9%88%d8%b2%d9%8a%d8%b1-%d8%a7%d9%84%d8%aa%d8%b4/","NULL",""+178235847+""},
                                                                                                                                                        {""+12,"مودريتش يبلغ ريال مدريد برغبته","http://atlanticmedia.info/?q=node/28932","http://atlanticmedia.info/?q=sites/default/files/styles/medium/public/field/image/%D8%A7_19.jpg&amp;itok=0Q6qKw0K",178235847+""},
                                                                                                                                                        {""+6,"المنتخب الموريتاني يسحق نظيره الجيبوتي بسبعة أهداف نظيفة","http://www.aqlame.com/article38346.html","NULL",""+178235847+""},
                                                                                                                                                        {""+13,"وصول الوفد الاول من حجاج موريتانيا الى المدينة المنورة – قناة الموريتانية","https://www.bellewarmedia.com/%d9%88%d8%b5%d9%88%d9%84-%d8%a7%d9%84%d9%88%d9%81%d8%af-%d8%a7%d9%84%d8%a7%d9%88%d9%84-%d9%85%d9%86-%d8%ad%d8%ac%d8%a7%d8%ac-%d9%85%d9%88%d8%b1%d9%8a%d8%aa%d8%a7%d9%86%d9%8a%d8%a7-%d8%a7%d9%84%d9%89/","NULL",""+178235847+""},
                                                                                                                                                            {""+14,"وزارة العدل توزع التكوينات الخارجية على عمال ادارة السجون خارج إطار الشفافية","http://elmourageb.com/25401/%d9%88%d8%b2%d8%a7%d8%b1%d8%a9-%d8%a7%d9%84%d8%b9%d8%af%d9%84-%d8%aa%d9%88%d8%b2%d8%b9-%d8%a7%d9%84%d8%aa%d9%83%d9%88%d9%8a%d9%86%d8%a7%d8%aa-%d8%a7%d9%84%d8%ae%d8%a7%d8%b1%d8%ac%d9%8a%d8%a9-%d8%b9","NULL",""+178235847+""},
                                                                                                                                                            {""+15,"الفوج الثاني من حجاجنا الميامين يتوجه إلى الديار المقدسة","http://amicinfo.com/node/19023","http://amicinfo.com/sites/default/files/hh/%D8%AA%D9%86%D8%B2%D9%8A%D9%84%20%282%29_0.jpeg",178235847+""},
                                                                                                                                                            {""+16,"قائد أركان الجيوش يتقاعد رسميا نهاية العام وهو خليفة الرئيس عزيز في الحكم","http://www.tawassoul.net/ar/%D8%A7%D9%84%D8%A3%D8%AE%D8%A8%D8%A7%D8%B1-%D8%A7%D9%84%D9%88%D8%B7%D9%86%D9%8A%D8%A9/%D8%B9%D8%B1%D8%A8%D9%8A%D8%A9-%D9%88-%D8%AF%D9%88%D9%84%D9%8A%D8%A9/item/29220-%D9%82%D8%A7%D8%A6%D8%AF-%D8%A3%D8%B1%D9%83%D8%A7%D9%86-%D8%A7%D9%84%D8%AC%D9%8A%D9%88%D8%B4-%D9%8A%D8%AA%D9%82%D8%A7%D8%B9%D8%AF-%D8%B1%D8%B3%D9%85%D9%8A%D8%A7-%D9%86%D9%87%D8%A7%D9%8A%D8%A9-%D8%A7%D9%84%D8%B9%D8%A7%D9%85-%D9%88%D9%87%D9%88-%D8%AE%D9%84%D9%8A%D9%81%D8%A9-%D8%A7%D9%84%D8%B1%D8%A6%D9%8A%D8%B3-%D8%B9%D8%B2%D9%8A%D8%B2-%D9%81%D9%8A-%D8%A7%D9%84%D8%AD%D9%83%D9%85.html","http://www.tawassoul.net/media/k2/items/cache/b57dd56578af07ed20409ce306188cd3_S.jpg",178235847+""},
                                                                                                                                                            {""+18,"رئيسان موريتانيان فصلا من الجيش في أسبوع واحد تعرف عليها (مراسيم + صور ) ","http://nawafedh.com/?q=node/8445","http://nawafedh.com/sites/default/files/maya%20eli%20%281%29.jpg",178235847+""},
                                                                                                                                                            {""+19,"القبيلة.. بعصبياتٍ جديدة / الدكتور السيد ولد إباه","http://mourassiloun.com/node/4915","http://mourassiloun.com/sites/default/files/styles/large/public/field/image/%D8%A7%D9%84%D8%B3%D9%8A%D8%AF%20%D9%88%D9%84%D8%AF%20%D8%A5%D8%A8%D8%A7%D9%87_2.jpg?itok=-d0QQZj5",178235847+""},
                                                                                                                                                            {""+20,"جدﻭﺍﺋﻴﺔ ﺇﺭﺳﺎﺀ ﺍﻟﻤﺠﺎﻟﺲ ﺍﻟﺠﻬﻮﻳﺔ للولايات ﺑﺒﻼﺩﻧﺎ /الخليل أحمد سيفر","http://alaraby.info/node/7086","http://alaraby.info/sites/default/files/%D9%82%D9%84%D9%85-600x330-1-600x330.jpg",178235847+""},
                                                                                                                                                            {""+21,"فضيحة: حزب سياسي يرشح 18 امرأة من شريحة واحدة (صورة)","http://houriyamedia.info/node/4117","http://houriyamedia.info/sites/default/files/%D8%AD%D8%B2%D8%A8%20%D8%A7%D9%84%D9%85%D8%B3%D8%AA%D9%82%D8%A8%D9%84.jpg",178235847+""},
                                                                                                                                                            {""+22,"مراسيم فصل الرئيسين ولد الطايع وولد محمد فال من الجيش","http://elilam.net/node/4389","http://elilam.net/sites/default/files/ma_2.jpg",178235847+""},
                                                                                                                                                            {""+25,"حاتم يحمل الحكومة مسؤولية الحوادث ويندد بتجالها لوضعية الطرق الكارثية","http://alakhbar.info/?q=node/12554","http://alakhbar.info/sites/default/files/%D8%AA%D9%86%D8%B2%D9%8A%D9%84_14.jpg",178235847+""},
                                                                                                                                                            {""+28,"شاهد كيف تستقبل مدينة كيفه خريف هذا العام(فيديو) ","http://kiffainfo.net/article22290.html","NULL",""+178235847+""},
                                                                                                                                                            {""+30,"عاجل:  محاولة اغتيال الرئيس (فيديو)","http://www.rkizinfo.com/node/2428","http://www.rkizinfo.com/sites/default/files/styles/large/public/field/image/e6d3acb2f49a9c08fd91e02358501866_w700_h450_cp_0.jpg?itok=OTkh3Bm1",178235847+""},
                                                                                                                                                            {""+33,"السعودية تستدعي سفيرها لدى كندا وتطرد السفير الكندي بالرياض","http://tawary.com/spip.php?article34894","NULL",""+178235847+""},
                                                                                                                                                            {""+36,"الشرطة تعثر على طفلة من ذوي الاحتياجات الخاصة (صورة صادمة)","http://royapost.net/35412/","NULL",""+178235847+""},
                                                                                                                                                                {""+37,"رئيس الحزب الحاكم يدعو لتطبيق قوانين السير","http://www.elmouritany.info/node/3884","http://www.elmouritany.info/sites/default/files/styles/large/public/field/image/210_0.jpg?itok=3VkynS7w",178235847+""},
                                                                                                                                                                {""+38,"السعودية تطرد السفير الكندي لديها وتستدعي سفيرها في أوتاوا","http://sondagemr.net/?p=17216","NULL",""+178235847+""},
                                                                                                                                                                {""+41,"مناضلو قوى التقدم يحتجون على إقصاء بدر الدين من الترشحات","http://echourouqmedia.net/?q=node/10263","http://echourouqmedia.net/sites/default/files/3660D581-1DF1-4284-9146-1A0A2DE0ADD0.jpeg",178235847+""},
                                                                                                                                                                {""+43,"موريتانيا.. الفوج الثاني من الحجاج يغادر  إلى الديار المقدسة","https://www.saharamedias.net/%d9%85%d8%ba%d8%a7%d8%af%d8%b1%d8%a9-%d8%a7%d9%84%d9%81%d9%88%d8%ac-%d8%a7%d9%84%d8%ab%d8%a7%d9%86%d9%8a-%d9%85%d9%86-%d8%a7%d9%84%d8%ad%d8%ac%d8%a7%d8%ac-%d8%a7%d9%84%d9%85%d9%88%d8%b1%d9%8a%d8%aa/","NULL",""+178235847+""},
                                                                                                                                                                    {""+44,"قيادى بالإصلاح : نأمل عودة المنشقين عن الحزب واحترام قراراته","http://www.zahraa.mr/node/17467","http://www.zahraa.mr/sites/default/files/212_2%20%281%29_1_6_1_8.jpg",178235847+""},
                                                                                                                                                                    {""+45,"مظاهر من فوضوية النقل هي أسباب الحوادث (صور)","http://jedidtoday.com/node/2497","http://jedidtoday.com/sites/default/files/16E25245-5020-4187-967C-078FF4AE4C10-569-0000013B292B07C3.jpeg",178235847+""},
                                                                                                                                                                    {""+46,"تعيينات خارج العرف في المركز الوطني للخدمات الجامعية","http://meyadin.net/node/12837","http://meyadin.net/sites/default/files/mmrkk.jpg",178235847+""},
                                                                                                                                                                    {""+47,"بيان هام من الحزب الموريتاني للدفاع عن البيئة","http://eljewahir.com/node/10034","http://eljewahir.com/sites/default/files/styles/large/public/field/image/pp4321.jpg?itok=4iMQVOoi",178235847+""},
                                                                                                                                                                    {""+48," تزلف منت لمرابط .. يوردها المهالك الحوالك  ","http://essabq.info/node/6798","http://essabq.info/sites/default/files/38480205_2114047722144661_7835761931372724224_n.jpg",178235847+""},
                                                                                                                                                                    {""+49,"فضيحة تلاعب تهز احد اهم احزاب المعارضة الموريتانية","http://mushahide.com/node/14915","http://mushahide.com/sites/default/files/styles/medium/public/1_104.jpg?itok=wOZiWQBE",178235847+""},
                                                                                                                                                                    {""+50,"ولد اجاي: سنكمل طريق بوتلميت وحصّلنا تمويل طريق  ألاك","http://essirage.net/node/14461","http://essirage.net/sites/files/SmartSelectImage_%D9%A2%D9%A0%D9%A1%D9%A8-%D9%A0%D9%A8-%D9%A0%D9%A6-%D9%A0%D9%A8-%D9%A2%D9%A8-%D9%A0%D9%A9.png",178235847+""},
                                                                                                                                                                    {""+51,"المنتدى يدين إهمال السلطات وعدم اكتراثها بأرواح المواطنين","http://elwatan.info/node/10979","http://elwatan.info/sites/default/files/arton36678_0.jpg",178235847+""},
                                                                                                                                                                    {""+54,"السعودية تطرد السفير الكندي","http://www.elbadil.info/2013/index.php/g5/18716-2018-08-06-09-36-28.html","http://www.elbadil.info/2013/media/images/1d(6).jpg",178235847+""},
                                                                                                                                                                    {""+56,"تفاقم الخلاف بين الوزير الأول، ورئيس ائتلاف الأغلبية (خاص)","http://www.elwassat.info/index.php/3amme/13236-2018-08-06-09-56-05","NULL",""+178235847+""},
                                                                                                                                                                    {""+57,"الذكرى العاشرة للإطاحة بالرئيس ولد الشيخ عبد الله","http://elghavila.info/?p=36303","NULL",""+178235847+""},
                                                                                                                                                                    {""+59,"الوزيرة من اعل سالم تفشل  فشلا ذريعا في خلق قاعدة شعبية لها بولاية لعصابة","http://allarab.info/node/10903","http://allarab.info/sites/default/files/field/image/0000000002.jpg",178235847+""},
                                                                                                                                                                    {""+62,"بيان شديد اللهجة من منظمة شباب حزب التحالف الشعبي التقدمي ( نص البيان) ","http://echarghtoday.com/node/7402","http://echarghtoday.com/sites/default/files/IMG_20180804_192055_0.jpg",178235847+""},
                                                                                                                                                                    {""+63,"ولد يوكه يوجه سؤالا للشيخ الددو حول التصويت وفق الضوابط الشرعية (تدوينات)","http://ethaira.info/spip.php?article11572","NULL",""+178235847+""},
                                                                                                                                                                    {""+64,"المنتدى : طريق الأمل وطريق نواذيبو مجازر حقيقية","http://elhakika.info/2018/08/06/%d8%a7%d9%84%d9%85%d9%86%d8%aa%d8%af%d9%89-%d8%b7%d8%b1%d9%8a%d9%82-%d8%a7%d9%84%d8%a3%d9%85%d9%84-%d9%88%d8%b7%d8%b1%d9%8a%d9%82-%d9%86%d9%88%d8%a7%d8%b0%d9%8a%d8%a8%d9%88-%d9%85%d8%ac%d8%a7%d8%b2/","NULL",""+178235847+""},
                                                                                                                                                                        {""+66,"المنتخب الوطني للشباب ينهي مباراته الأخيرة في بطولة الكوتيف بالتعادل","http://elmiraat.info/%d8%a7%d9%84%d9%85%d9%86%d8%aa%d8%ae%d8%a8-%d8%a7%d9%84%d9%88%d8%b7%d9%86%d9%8a-%d9%84%d9%84%d8%b4%d8%a8%d8%a7%d8%a8-%d9%8a%d9%86%d9%87%d9%8a-%d9%85%d8%a8%d8%a7%d8%b1%d8%a7%d8%aa%d9%87-%d8%a7%d9%84/","NULL",""+178235847+""},
                                                                                                                                                                            {""+67,"المدير العام والمالي لشركة سونادير تحت رقابة قضائية مشددة (أسماء)","http://elvetach.info/node/9758","http://elvetach.info/sites/default/files/8_223.jpg",178235847+""},
                                                                                                                                                                            {""+68,"رئيس الحزب الحاكم السلامة الطرقية هي وعي وثقافة قبل أن تكون بنية تحتية.","http://mauri7.info/ar/2018/08/06/%d8%b1%d8%a6%d9%8a%d8%b3-%d8%a7%d9%84%d8%ad%d8%b2%d8%a8-%d8%a7%d9%84%d8%ad%d8%a7%d9%83%d9%85-%d9%8a%d8%b9%d8%b2%d9%8a-%d9%81%d9%8a-%d8%b6%d8%ad%d8%a7%d9%8a%d8%a7-%d8%ad%d9%88%d8%a7%d8%af%d8%ab-%d8%a7/#utm_source=rss&utm_medium=rss&utm_campaign=%25d8%25b1%25d8%25a6%25d9%258a%25d8%25b3-%25d8%25a7%25d9%2584%25d8%25ad%25d8%25b2%25d8%25a8-%25d8%25a7%25d9%2584%25d8%25ad%25d8%25a7%25d9%2583%25d9%2585-%25d9%258a%25d8%25b9%25d8%25b2%25d9%258a-%25d9%2581%25d9%258a-%25d8%25b6%25d8%25ad%25d8%25a7%25d9%258a%25d8%25a7-%25d8%25ad%25d9%2588%25d8%25a7%25d8%25af%25d8%25ab-%25d8%25a7","NULL",""+178235847+""},
                                                                                                                                                                            {""+69,"ولد سيدي عبد الله على اللائحة الوطنية","https://chinguitmedia.com/2018/08/06/13783/","NULL",""+178235847+""},
                                                                                                                                                                                {""+71,"ضحية من ضحايا حوادث السير تكتب للرئيس","http://alikhbari.net/node/12353","http://alikhbari.net/sites/default/files/36662245_1331264727006615_464667271894138880_n_0.jpg",178235847+""},
                                                                                                                                                                                {""+72,"ازويرات ميديا تسرد طريقة العثور على الخاطفين وخفايا المعركتين","http://zoueratemedia.info/node/2123","http://zoueratemedia.info/sites/default/files/AAA4FFB6-0C90-41E2-84A3-9E0BAAB64F7A.jpeg",178235847+""},
                                                                                                                                                                                {""+73,"أطفال يستغلون خزانات آفطوط الساحلي للاستحمام والتبول – فيديو","http://tiguend.com/2018/08/33907/","NULL",""+178235847+""},
                                                                                                                                                                                        {""+76,"اسنيم تقدم تفاصيل عن قرض بقيمة 109مليون دولار","http://zouerate.info/node/4388","http://zouerate.info/sites/default/files/styles/medium/public/%D8%B4%D8%B9%D8%A7%D8%B1%20%D8%A7%D8%B3%D9%86%D9%8A%D9%85_200.png?itok=O-asCrnI",178235847+""},
                                                                                                                                                                                        {""+78,"المخيمات الصيفية، تكريم للمتفوقين في مسابقة دخول السنة الأولى إعدادية | قناة الموريتانية","http://www.tawatur.net/المخيمات-الصيفية،-تكريم-للمتفوقين-في-مسابقة-دخول-السنة-الأولى-إعدادية-قناة-الموريتانية","https://i.ytimg.com/vi/89neSHVJswE/sddefault.jpg",178235847+""},
                                                                                                                                                                                        {""+80,"“مولتيك” تعلن عن اكتمال عملية الاستحواذ من قبل “سوتشو دونجشان بريسيجن مانيوفاكتتشرينج (دي إس بي جيه)","http://www.essada.info/%d9%85%d9%88%d9%84%d8%aa%d9%8a%d9%83-%d8%aa%d8%b9%d9%84%d9%86-%d8%b9%d9%86-%d8%a7%d9%83%d8%aa%d9%85%d8%a7%d9%84-%d8%b9%d9%85%d9%84%d9%8a%d8%a9-%d8%a7%d9%84%d8%a7%d8%b3%d8%aa%d8%ad%d9%88%d8%a7","NULL",""+178235847+""},
                                                                                                                                                                                        {""+81,"نسب نجاح كبيرة في الدورة التكميلية للبكالوريا","http://nouadibou.info/news/14457-2018-08-06-09-19-02.html","http://nouadibou.info/media/images/images(78).jpg",178235847+""},
                                                                                                                                                                                        {""+84,"تعرض الرئيس الفنزويلي لمحاولة أغتيال بواسطة طائرة من دون طيار","http://lebjawi.info/index.php/ar/actualites-mauritanie/1271-2018-08-05-16-30-43","http://lebjawi.info/images/fenzouila.jpg",178235847+""},
                                                                                                                                                                                        {""+85,"في رثاء محمد ولد محمد صالح","http://www.mederdra.net/index.php/adab/5037-2018-08-06-09-58-49.html","http://legwareb.info/sites/default/files/IMG-20180802-WA0007.jpg",178235847+""},
                                                                                                                                                                                        {""+86,"100 حزب مشارك يتقدمها الحزب الحاكم يليه إسلاميو حزب التجمع","http://elhadeth.mr/node/8289","http://elhadeth.mr/sites/default/files/05qpt962.5.jpg",178235847+""},
                                                                                                                                                                                        {""+87,"خاص .. موريتانيا تشتري طائرتين من نوع امبراير لتعزيز اسطولها","http://www.tabrenkout.com/?p=13620","NULL",""+178235847+""},
                                                                                                                                                                                        {""+88,"كاتب رسالة  زينب  يعترف أنها من نسج الخيال (هوية+ صورة)","http://elistiklal.info/node/2292","http://elistiklal.info/sites/default/files/styles/large/public/field/image/Nda77a_0.jpg?itok=hMQ4zxaU",178235847+""},
                                                                                                                                                                                        {""+92,"محامي ترامب: الرئيس سيقاوم أي استدعاء من المحقق الخاص","http://mauritania13.com/node/9800","http://mauritania13.com/sites/default/files/FB12ACBF-9AAC-465B-82B5-ACEEF71C9BA5.jpeg",178235847+""},
                                                                                                                                                                                        {""+99,"الناشئون يدكون شباك منتخب جيبوتي بسباعية نظيفة خلال معسكر تونس ","http://amisports.net/index.php/2015-08-14-16-09-04/1577-2018-08-05-18-54-16.html","http://amisports.net/images/iooo/_-1.jpg",178235847+""},
                                                                                                                                                                                        {""+109,"ولد الحبيب يعقد اجتماعا بأنصاره ومجموعته السياسية بكيفه","http://www.guerou.info/?p=12701","NULL",""+178235847+""},
                                                                                                                                                                                        {""+111,"السعودية تعلن تجميد كافة التعاملات مع كندا","http://sahelnews.info/node/4270","http://sahelnews.info/sites/default/files/4878.jpg",178235847+""},
                                                                                                                                                                                        {""+113,"كيف برر العسكريون الانقلاب على ولد الشيخ عبد الله قبل عشر سنوات","https://maurinews.info/video/23389/","NULL",""+178235847+""},
                                                                                                                                                                                            {""+117," والدة أسامة بن لادن تتحدث لأول مرة عبر للإعلام ","http://elmohit.net/node/2296","http://elmohit.net/sites/default/files/390785129.jpg",178235847+""},
                                                                                                                                                                                            {""+118,"مراسيم فصل الرئيسان ولد الطايع وولد محمد فال من الجيش","http://elbeyan.info/node/4708","http://elbeyan.info/sites/default/files/FAFBD18E-9D99-4805-AFD2-2953B93AD595.jpeg",178235847+""},
                                                                                                                                                                                            {""+121,"سيدي محمد الملقب الخامس فيلسوف ومصلح اجتماعي يقرر اعتزال السياسة","http://newsmaghreb.info/index.php/2013-10-28-13-27-17/16742-2018-08-06-00-46-07.html","NULL",""+178235847+""},
                                                                                                                                                                                            {""+128,"النعمة / تفاصيل هروب القناص الجزائري من السجن","http://sawtchargh.net/archives/26077","NULL",""+178235847+""},
                                                                                                                                                                                            {""+131,"مرشحون في الشمال المورتاني يسحبون ترشحاتهم ويعلنون (هوياتهم)","http://al-raya.info/2018/08/06/%d9%85%d8%b1%d8%b4%d8%ad%d9%88%d9%86-%d9%81%d9%8a-%d8%a7%d9%84%d8%b4%d9%85%d8%a7%d9%84-%d8%a7%d9%84%d9%85%d9%88%d8%b1%d8%aa%d8%a7%d9%86%d9%8a-%d9%8a%d8%b3%d8%ad%d8%a8%d9%88%d9%86-%d8%aa%d8%b1%d8%b4/","NULL",""+178235847+""},
                                                                                                                                                                                                {""+135,"عاجل: حزب الاتحاد ينذر بعض أعضاء الحكومة (بيان)","http://www.chilouhefchi.net/spip.php?article1664","NULL",""+178235847+""},
                                                                                                                                                                                                {""+144,"كيفه : كم عدد الأصوات التي تنقص جمال ولد كبود عن الفوز؟","http://www.wateni.com/2018/08/%d9%83%d9%8a%d9%81%d9%87-%d9%83%d9%85-%d8%b9%d8%af%d8%af-%d8%a7%d9%84%d8%a3%d8%b5%d9%88%d8%a7%d8%aa-%d8%a7%d9%84%d8%aa%d9%8a-%d8%aa%d9%86%d9%82%d8%b5-%d8%ac%d9%85%d8%a7%d9%84-%d9%88%d9%84%d8%af/","NULL",""+178235847+""},
                                                                                                                                                                                                    {""+0,"موريتانيا تشارك في التجمع الافريقي لمجموعة البنك وصندوق النقد الدوليين","http://www.alwiam.info/node/10011","http://www.alwiam.info/sites/default/files/styles/large/public/field/image/05-08-2018-m02.jpg?itok=fWAZM09A",178771025+""},
                                                                                                                                                                                                    {""+8,"مسؤول حكومي بارز يرسي صفقة لتموين قطاعه على شركة خصوصية يملكها تفاصيل","http://www.taqadoumi.net/index.php/2543-2018-08-06-10-10-05","NULL",""+178771025+""},
                                                                                                                                                                                                    {""+71,"المنتدى يدين عدم اكتراث النظام أرواح المواطنين","http://alikhbari.net/node/12354","http://alikhbari.net/sites/default/files/%D9%85%D9%86%D8%AA%D8%AF%D9%89%20%D8%A7%D9%84%D9%85%D8%B9%D8%A7%D8%B1%D8%B6%D8%A9_20_2.jpg",178771026+""},
                                                                                                                                                                                                    {""+73,"تأخر الأشغال في طريق (المذرذرة – اركيز) أكثر من 37 شهرا","http://tiguend.com/2018/08/33966/","http://tiguend.com/wp-content/uploads/2017/12/طريق-اركيز-300x207.jpg",178771026+""},
                                                                                                                                                                                                    {""+6,"مالي: مرشحو المعارضة يطعنون في نتائج الانتخابات الرئاسية ","http://www.aqlame.com/article38347.html","NULL",""+179228086+""},
                                                                                                                                                                                                    {""+11,"اخبار الوطن  ينشر للرأي العام  هوية زينب التي ابكت الملايين برسالتها إلى رئيس الجمهورية","http://www.akhbarwatan.net/%d8%a7%d8%ae%d8%a8%d8%a7%d8%b1-%d8%a7%d9%84%d9%88%d8%b7%d9%86-%d9%8a%d9%86%d8%b4%d8%b1-%d9%84%d9%84%d8%b1%d8%a3%d9%8a-%d8%a7%d9%84%d8%b9%d8%a7%d9%85-%d9%87%d9%88%d9%8a%d8%a9-%d8%b2%d9%8a%d9%86%d8%a8/","NULL",""+179228086+""},
                                                                                                                                                                                                        {""+29,"زينب تكاتب الرئيس بقصة فقدها لأبيها وعمها وأخيها في حوادث سير متفرقة (قصة مؤثرة","http://elitissal.net/archives/22076","NULL",""+179228087+""},
                                                                                                                                                                                                        {""+46,"دورية من أمن الطرق تحبط عملية سطو في ولاية نواكشوط الجنوبية","http://meyadin.net/node/12845","http://meyadin.net/sites/default/files/100_0440.jpg",179228087+""},
                                                                                                                                                                                                        {""+47,"نجم سنغالي يتعرض لإهانات عنصرية في أكبر دولة بالعالم (صورته)","http://eljewahir.com/node/10035","http://eljewahir.com/sites/default/files/styles/large/public/field/image/media-94906.jpg?itok=x7jyg6Ol",179228087+""},
                                                                                                                                                                                                        {""+73,"مع استمرار انقطاع الكهرباء ببتلميت.. ماذا أضاف مشروع الطاقة الشمسية ؟!","http://tiguend.com/2018/08/32423/","http://tiguend.com/wp-content/uploads/2017/09/02-1-300x168.jpg",179228087+""},
                                                                                                                                                                                                        {""+145,"عبدالله ولد منيه رقم صعب في المعادلة السياسية بحزب الإتحاد:","http://echamel.info/?p=611","NULL",""+179228087+""},
                                                                                                                                                                                                        {""+6,"كندا تعلق على تجميد علاقاتها مع السعودية","http://www.aqlame.com/article38348.html","NULL",""+179671035+""},
                                                                                                                                                                                                        {""+47,"لأول مرة..لاعبو موريتانيا يسحقون منتخبا افريقيا 7 أهداف بدون مقابل","http://eljewahir.com/node/10036","http://eljewahir.com/sites/default/files/styles/large/public/field/image/arton38346.jpg?itok=r6zNYfMr",179671040+""},
                                                                                                                                                                                                        {""+69,"إحباط عملية سطو فجر اليوم من قبل أمن الطرق","https://chinguitmedia.com/2018/08/06/13786/","NULL",""+179671040+""},
                                                                                                                                                                                                            {""+73,"تنافس كبير لمؤسسات تحويل الأموال على الاستثمار في تكنت","http://tiguend.com/2018/08/%d8%aa%d9%86%d8%a7%d9%81%d8%b3-%d9%83%d8%a8%d9%8a%d8%b1-%d9%84%d9%85%d8%a4%d8%b3%d8%b3%d8%a7%d8%aa-%d8%aa%d8%ad%d9%88%d9%8a%d9%84-%d8%a7%d9%84%d8%a3%d9%85%d9%88%d8%a7%d9%84-%d8%b9%d9%84%d9%89-%d8%a7/","http://tiguend.com/wp-content/uploads/2017/08/WhatsApp-Image-2017-08-03-at-18.33.10-1-300x169.jpeg",179671040+""},
                                                                                                                                                                                                            {""+85,"ولد أجاه مرشحا لعمدة بلدية المذرذرة","http://www.mederdra.net/index.php/mederedra/5038-2018-08-06-10-25-55.html","http://legwareb.info/sites/default/files/IMG-20180728-WA0066.jpg",179671040+""},
                                                                                                                                                                                                            {""+29,"رئيسان موريتانيان فصلا من الجيش في أسبوع واحد تعرف عليها (مراسيم + صور )","http://elitissal.net/archives/22079","NULL",""+180271115+""},
                                                                                                                                                                                                            {""+41,"تعيين الفريق ابرور هل هو عودة لسيناريو  ولد بايه؟","http://echourouqmedia.net/?q=node/10264","http://echourouqmedia.net/sites/default/files/_1014090.JPG",180271115+""},
                                                                                                                                                                                                            {""+43,"السعودية تطرد سفير كندا وتجمد العلاقات والأخيرة “قلقة”","https://www.saharamedias.net/%d8%a7%d9%84%d8%b3%d8%b9%d9%88%d8%af%d9%8a%d8%a9-%d8%aa%d8%b7%d8%b1%d8%af-%d8%b3%d9%81%d9%8a%d8%b1-%d9%83%d9%86%d8%af%d8%a7-%d9%88%d8%aa%d8%ac%d9%85%d8%af-%d8%a7%d9%84%d8%b9%d9%84%d8%a7%d9%82%d8%a7/","NULL",""+180271115+""},
                                                                                                                                                                                                                {""+63,"مَوْسِمُ (أَكْلِ أَدْمِغَةِ النًاخِبِين بالبَاطِلِ)","http://ethaira.info/spip.php?article11573","NULL",""+180271115+""},
                                                                                                                                                                                                                {""+73,"رداءة طريق روصو تنعش مهنة الميشلان في تكنت – صور","http://tiguend.com/2018/08/%d8%b1%d8%af%d8%a7%d8%a1%d8%a9-%d8%b7%d8%b1%d9%8a%d9%82-%d8%b1%d9%88%d8%b5%d9%88-%d8%aa%d9%86%d8%b9%d8%b4-%d9%85%d9%87%d9%86%d8%a9-%d8%a7%d9%84%d9%85%d9%8a%d8%b4%d9%84%d8%a7%d9%86-%d9%81%d9%8a-%d8%aa/","http://tiguend.com/wp-content/uploads/2017/02/ميشلان-طريق-روصو‬-‫-‫2-300x169.jpg",180271115+""},
                                                                                                                                                                                                                {""+85,"في رثاء أحمدو سالم ولد آياه","http://www.mederdra.net/index.php/adab/5039-2018-08-06-10-32-53.html","http://www.mederdra.net/images/stories/thumbnails/images-aref%20kerim-177x238.jpg",180271115+""},
                                                                                                                                                                                                                {""+87,"الفوج الثاني من الحجاج الموريتانيين يغادر الى المدينة المنورة","http://www.tabrenkout.com/?p=13623","NULL",""+180271115+""},
                                                                                                                                                                                                                {""+112,"مَوْسِمُ (أَكْلِ أَدْمِغَةِ النًاخِبِين بالبَاطِلِ)","http://elhiyad.info/node/5311","http://elhiyad.info/sites/default/files/%D9%88%D9%84%D8%AF%20%D8%AF%D8%A7%D9%87%D9%8A_0.jpeg",180271116+""},
                                                                                                                                                                                                                {""+11,"فضيحة قناة الموريتانية تتجاهل مجزرة طريق الأمل وتسضيف أبطال ” ظامت “صورة “","http://www.akhbarwatan.net/%d9%81%d8%b6%d9%8a%d8%ad%d8%a9-%d9%82%d9%86%d8%a7%d8%a9-%d8%a7%d9%84%d9%85%d9%88%d8%b1%d9%8a%d8%aa%d8%a7%d9%86%d9%8a%d8%a9-%d8%aa%d8%aa%d8%ac%d8%a7%d9%87%d9%84-%d9%85%d8%ac%d8%b2%d8%b1%d8%a9-%d8%b7/","NULL",""+180660980+""},
                                                                                                                                                                                                                    {""+4,"مقتل موريتاني على يد لصوص بأنغولا","http://elhora.info/?p=15768","NULL",""+181171136+""},
                                                                                                                                                                                                                    {""+13,"المخيمات الصيفية – ملف نشرة  قناة الموريتانية","https://www.bellewarmedia.com/%d8%a7%d9%84%d9%85%d8%ae%d9%8a%d9%85%d8%a7%d8%aa-%d8%a7%d9%84%d8%b5%d9%8a%d9%81%d9%8a%d8%a9-%d9%85%d9%84%d9%81-%d9%86%d8%b4%d8%b1%d8%a9-%d9%82%d9%86%d8%a7%d8%a9-%d8%a7%d9%84%d9%85%d9%88%d8%b1/","NULL",""+181171136}
                                                                                                                                                                                                                    };
        for (int aData1 : data1) db.insertData(aData1);
        for (String[] aData2 : data2)
            db.insertData(Integer.parseInt(aData2[0]), aData2[1], aData2[2], aData2[3], aData2[4]);

    }
}