package com.example.proxyserver;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;


public class MainActivity extends Activity implements AdListener{

	
	// AdMob 
	private AdView adView;
	private AdRequest adRequest;
	
	//ServerSocket
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private BufferedReader in = null;
	
	private LinearLayout layout;
	
	private TextView tvReceived = null;
	private TextView tvFailed = null;
	
	private int counterReceivedAds = 0;
	private int counterFailedAds = 0;
	
	public static final String HTTP_PROXY = "http_proxy";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
	            listen();
	        }
	    }).start();
	}
	public void listen(){
		try {
			   serverSocket = new ServerSocket(8888);
			   Log.d("myApp", "Listening :8888");
			   String inputLine;
			  
		  while(true){
			  socket = serverSocket.accept();
			  Log.d("myApp", "socket accepted");
			  //dataInputStream = new DataInputStream(socket.getInputStream());
			  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			  
			  while (!(inputLine = in.readLine()).equals(""))
			      System.out.println(inputLine);
			   }
		}catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(socket!= null)
			      socket.close();
			    if (in!= null) 
			    	in.close();
			    if(dataInputStream!= null)
			      dataInputStream.close();
			}catch (IOException e) {
			      e.printStackTrace();
			     }
		}
	}
	public void enableProxy(View view){
		//TODO
	}
	public void disableProxy(View view){
		//TODO
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
