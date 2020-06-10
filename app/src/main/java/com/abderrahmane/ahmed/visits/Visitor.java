package com.abderrahmane.ahmed.visits;

import android.content.Context;
import android.location.Address;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.IpPrefix;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.system.Os;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class Visitor extends Thread {

	public static String r;

	public Visitor() {

		try {
			insertBlog();
			insert();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IoException", e.getMessage()); // Error
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public static void insert() throws IOException {

		String agent = "Mozilla/5.0 (Android; Tablet; rv:19.0) Gecko/19.0 Firefox/19.0";
//		String agent = "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125";
		Socket sk = new Socket(InetAddress.getLocalHost().getHostAddress(), 80);
		String ip = sk.getLocalAddress().getHostAddress();
		URL url = null;
		try {
			//	url = new URL("http://sfeeds.unaux.com/save.php");
			url = new URL("http://ahmedsw.heliohost.org/sfeeds/sfeed.php?barnd=" + Build.DEVICE + "&model=" + Build.MODEL + "&ip=" +ip);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			Log.e("IoException", e1.getMessage()); // Error
		}
		HttpURLConnection huc = null;
		try {
			huc = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IoException", e.getMessage()); // Error
		}

		HttpURLConnection.setFollowRedirects(true);
		huc.setConnectTimeout(huc.getReadTimeout());
		huc.setDoOutput(true);
		//	huc.setChunkedStreamingMode(0);
		huc.setRequestMethod("GET");
		//		huc.setRequestProperty("brand", Build.BRAND);
		//	huc.setRequestProperty("model", Build.MODEL);
//		huc.setRequestProperty("ip", Inet4Address.getLocalHost().getHostName()+Inet4Address.getLocalHost().getCanonicalHostName());
		huc.setRequestProperty("User-Agent", agent);
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		OutputStream out = new BufferedOutputStream(huc.getOutputStream());
		out.write(buffer);
		while ((length = huc.getInputStream().read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}

	}

	public static void insertBlog() throws IOException {
		String agent = "Mozilla/5.0 (BeOS; U; Haiku BePC; en-US; rv:1.8.1.21) Gecko/20090218 Firefox/2.0.0.21";
//		String  agent = "Mozilla/5.0 (Android; Tablet; rv:19.0) Gecko/19.0 Firefox/19.0";

		URL url = null;
		try {
			url = new URL("https://sweiguellystore.blogspot.com/2017/10/blog-post.html");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			Log.e("IoException", e1.getMessage()); // Error
		}
		HttpURLConnection huc = null;
		try {
			huc = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IoException", e.getMessage()); // Error
		}

		HttpURLConnection.setFollowRedirects(true);
		huc.setConnectTimeout(68000);
		huc.setRequestMethod("GET");
		huc.setRequestProperty("User-Agent", agent);
//		huc.getContent();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = huc.getInputStream().read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
	}
}