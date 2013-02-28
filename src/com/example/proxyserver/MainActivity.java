package com.example.proxyserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;


public class MainActivity extends Activity implements AdListener{

	
	// AdMob 
	private AdView adView;
	private AdRequest adRequest;
	
	private LinearLayout layout;
	
	private TextView tvReceived = null;
	private TextView tvFailed = null;
	
	private int counterReceivedAds = 0;
	private int counterFailedAds = 0;
	
	private String url = "http://media.admob.com/sdk-core-v40.js";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		enableHttpCaching();
		
		
		new Thread(new Runnable() {
			public void run() {
				getFileFromURL(url);
				getFileFromURL(url);
				getFileFromURL(url);
			}
		}).start();
		
	}
	public void fetchAds(View view){
		adView = new AdView(this, AdSize.BANNER, "a15122003cf3080");
		
		layout = (LinearLayout)findViewById(R.id.linearLayout);
        layout.addView(adView);
        
        adRequest = new AdRequest();
       // adRequest.addTestDevice(AdRequest.TEST_EMULATOR);              
       // adRequest.addTestDevice("8C9CFB4E6D4629F186568482BC555C1C"); 
        adView.setGravity(Gravity.BOTTOM);
        
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        adView.setLayoutParams(layoutParams);
        adView.setAdListener(this);
        adView.loadAd(adRequest);
        
	}
	public void stopAds(View view){
		if (adView != null) {
			adView.removeAllViews();
	        adView.destroy();
	      }
		tvReceived = (TextView) findViewById(R.id.textView1);
        tvReceived.setText("Received ads: " + counterReceivedAds);
        
        tvFailed = (TextView) findViewById(R.id.textView2);
        tvFailed.setText("Failed ads: " + counterFailedAds);
	}
	public void startProxy(View view) {
		new Thread(new Runnable() {
	        public void run() {
	           new HttpServer().listen();
	        }
	    }).start();
	}
	
	private void enableHttpCaching()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            try {
              File httpCacheDir = new File(getApplicationContext().getExternalCacheDir(), "http");
              
             // ResponseCache.setDefault(new MyResponseCache(getApplicationContext()));
              ResponseCache.setDefault(new CCResponseCache());
              
              long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
              HttpResponseCache.install(httpCacheDir, httpCacheSize);
            } catch (IOException e) {
              System.out.println("OVER ICS: HTTP response cache failed:" + e);
            }        
        } else {
        	 try {
                 File httpCacheDir = new File(getApplicationContext().getExternalCacheDir(), "http");
                 long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
                 Class.forName("android.net.http.HttpResponseCache")
                         .getMethod("install", File.class, long.class)
                         .invoke(null, httpCacheDir, httpCacheSize);
        	 }
              catch (Exception httpResponseCacheNotAvailable) {
            	  System.out.println("UNDER ICS : HTTP response cache  failed:" + httpResponseCacheNotAvailable);
             }
         }
    }
	
	public static void getFileFromURL(String src) {
		 HttpURLConnection connection = null;
		 InputStream input = null;
		 URL url = null;
	     try {
	            url = new URL(src);
	            connection=(HttpURLConnection)url.openConnection();
	            connection.setUseCaches(true);
	           // connection.addRequestProperty("Cache-Control", "only-if-cached" );        
	            input = connection.getInputStream();
	            System.out.println("The resource was cached!");
	        }catch (FileNotFoundException e) {
	        	System.out.println("The resource was not cached! Trying to download it... ");
	        	//download it from server
//	        	try {
//		        	connection.disconnect();
//		        	connection=(HttpURLConnection)url.openConnection();
//		        	connection.setUseCaches(false);
//		            connection.addRequestProperty("Cache-Control", "no-cache" );        
//					input = connection.getInputStream();
//					System.out.println("The resource was downloaded! ");
//				} catch (IOException e1) {
//					System.out.println("The resource was not downloaded! " + e1);
//					e1.printStackTrace();
//				}
	        	
	        }catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	 @Override
	    public void onDestroy() {
	      if (adView != null) {
	        adView.destroy();
	      }
	      super.onDestroy();
	    }
	@Override
	public void onDismissScreen(Ad arg0) {
		
	}
	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		counterFailedAds ++;
	}
	@Override
	public void onLeaveApplication(Ad arg0) {
		
	}
	@Override
	public void onPresentScreen(Ad arg0) {
		
	}
	@Override
	public void onReceiveAd(Ad arg0) {
		counterReceivedAds ++;
		
	}
	

}
