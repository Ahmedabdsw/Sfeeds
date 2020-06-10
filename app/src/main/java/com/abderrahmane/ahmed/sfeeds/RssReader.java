/**
 * Created by Ahmed on 8/17/2017.
 */

package com.abderrahmane.ahmed.sfeeds;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import db.dbHelper;

public class RssReader implements Runnable {
    private final String url;
    public String title = "غير متاح ..................................................................................";
    public String link = null;
    public static int error = 0;
  //  public String title0;
    public int id;
    public long pub = ((int) System.currentTimeMillis() );
    public String inp;
    private String image;

    public RssReader(String u, int id) {
        url = u;
        this.id = id;
    }

    public void run() {
        //setRss();
        try {
            go();
        } catch (Exception e) {
            //            Log.e("Go method","Go Exception"); // Error
        }
    }

    public void go() throws IOException, ParserConfigurationException {
        URL rssUrl = null;
        rssUrl = new URL(url);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) rssUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(conn.getReadTimeout()); // 140 * 1000
        conn.setConnectTimeout(conn.getConnectTimeout());
        conn.setDoInput(true);
        conn.connect();
        //  BufferedReader in = new BufferedReader(newInputStreamReader(conn.getInputStream()));
        //  String sourceCode = null;
        //int i = 0;
        // InputStream is = null;
        //is = conn.getInputStream();
        //     ByteArrayOutputStream result = new ByteArrayOutputStream();
//           byte[] buffer = new byte[1024];
        // int length;
        // while ((length =conn.getInputStream().read(buffer)) != -1) {
        //   result.write(buffer, 0, length);
        //}
        // StandardCharsets.UTF_8.name() > JDK 7
        // String inp = result.toString("UTF-8");

        InputSource source = new InputSource(conn.getInputStream());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = null;
//        char[] cc = new char[100000];
        try {
            doc = dBuilder.parse(source);
  //          ByteArrayOutputStream result = new ByteArrayOutputStream();
    //         byte[] buffer = new byte[1024];
      //       int length;
        //     while ((length =source.getByteStream().read(buffer)) != -1) {
          ///     result.write(buffer, 0, length);
          //  }
///            inp = result.toString("UTF-8");

            if (doc != null) {
                Element element1 = (Element) doc.getElementsByTagName("item").item(0);
                title = element1.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue();
           //     pub = element1.getElementsByTagName("pubDate").item(0).getChildNodes().item(0).getNodeValue();
                link = element1.getElementsByTagName("link").item(0).getChildNodes().item(0).getNodeValue();

                String ptr= "src\\s*=\\s*([\"'])?([^\"']*)";
                Pattern p = Pattern.compile(ptr);
                Matcher m = p.matcher(element1.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue());
                if (m.find()) {
                    image = m.group(2); //Result
                }
                else{
                    image = element1.getElementsByTagName("enclosure").item(0).getAttributes().getNamedItem("url").getNodeValue();
                    if(image == "")
                        image = element1.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getNodeValue();
                    if(image == "")
                    image = element1.getElementsByTagName("image").item(0).getChildNodes().item(0).getNodeValue();

                }


                conn.disconnect();
                conn = null;
                dbHelper db = new dbHelper(UpdateNews.c);
                boolean rep = db.insertData(id, title, link, image,pub +"");
                if(rep==true ) {
                    db.insertData(id, title);
                    int j = (int) (Math.random() * (146 - 30)) + 30; // SELECT RANDOM NUMBER
                    if (j == id) {
                        MyNotification not = new MyNotification(UpdateNews.c, j, title, link);
                    }
                  }
                }
        } catch (SAXException e) {
            error++;
        } catch (IOException e) {
        }
        conn.disconnect();
    }
}