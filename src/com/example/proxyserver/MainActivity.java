package com.example.proxyserver;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.networking.HttpServerSocket;
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
	public void startServer(View view){
		socket = new HttpServerSocket();
		socket.execute();
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
		//System.out.println("Received ad: " + arg0.toString() + ", ad number: " + counterReceivedAds);
		counterReceivedAds ++;
		
	}
	  
	 

}
