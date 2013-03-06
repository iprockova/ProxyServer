package com.example.proxyserver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
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
	
	private HttpServerSocket socket;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		socket = new HttpServerSocket();
		socket.execute();
	}
	
	//AdMob: start ads
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
	//AdMob: stop ads
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
	
	//CACHE Test: download file from URL and store the HTTP response to MyHttpResponse
	private MyHttpResponse getFile(String cacheurl){
	   URL url = null;
	   HttpURLConnection urlConnection = null;
	   
	   try {
		   url = new URL(cacheurl);
		   urlConnection = (HttpURLConnection) url.openConnection();
		   
		   //get headers from response
		   Map<String, List<String>> headersMap = urlConnection.getHeaderFields();
		   //String headersString = Util.convertMapToString(headersMap);
		   
		   //get content from response
		   //String contentString = Util.convertInputStreamToString(urlConnection.getInputStream());
		   
		   MyHttpResponse response = new MyHttpResponse();
		   //response.setBody(contentString);
		   //response.setHeaders(headersString);
		   return response;
	   }
	   catch(IOException e){
		   e.printStackTrace();
		   return null;
	   }finally {
	     urlConnection.disconnect();
	   }
	}
	//CACHE Test: cache the HTTP response
	private void saveToCache(String cacheurl, MyHttpResponse response){
		CacheStore cache = CacheStore.getInstance();
		cache.saveCacheFile(cacheurl, response);
	}
	//CACHE Test: retrieve HTTP response from cache
	private MyHttpResponse getFromCache(String cacheurl){
		CacheStore cache = CacheStore.getInstance();
		return cache.getCacheFile(cacheurl);
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
	      if(socket !=null) {
	    	  socket.cancel(true);
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
