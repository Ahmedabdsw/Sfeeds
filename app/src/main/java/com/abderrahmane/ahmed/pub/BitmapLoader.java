package com.abderrahmane.ahmed.pub;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapLoader  extends Thread{
    public static Bitmap bmp;
    private static String src;

    public BitmapLoader(String src){
        this.src = src;
        try {
            insertBlog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    public void go() {
        try {
            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream in = conn.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(in);
            bmp = image;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/
public static void insertBlog() throws IOException {
        String agent = "Mozilla/5.0 (BeOS; U; Haiku BePC; en-US; rv:1.8.1.21) Gecko/20090218 Firefox/2.0.0.21";
//		String  agent = "Mozilla/5.0 (Android; Tablet; rv:19.0) Gecko/19.0 Firefox/19.0";

        URL url = null;
        try {
             url = new URL(src);
             bmp = BitmapFactory.decodeStream( url.openConnection().getInputStream());
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            Log.e("IoException", e1.getMessage()); // Error
        }
    /*    HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IoException", e.getMessage()); // Error
        }

       HttpURLConnection.setFollowRedirects(true);
        huc.setConnectTimeout(8000);
        huc.setRequestMethod("GET");
        huc.setRequestProperty("User-Agent", agent);
//		huc.getContent();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
 //       while ((length = huc.getInputStream().read(buffer)) != -1) {
 //           result.write(buffer, 0, length);
 //       }
//    InputStream in = huc.getInputStream();
    bmp = BitmapFactory.decodeStream(huc.getInputStream());  */
    }
}
