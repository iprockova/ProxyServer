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

public class MainActivity extends Activity {

	 ServerSocket serverSocket = null;
	 Socket socket = null;
	 DataInputStream dataInputStream = null;
	 BufferedReader in = null;
	
	// AdMob 
	private AdView adView;
	private AdRequest adRequest;
	private LinearLayout layout;
	
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
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);              
        adRequest.addTestDevice("8C9CFB4E6D4629F186568482BC555C1C"); 
        adView.setGravity(Gravity.BOTTOM);
        
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        adView.setLayoutParams(layoutParams);
        
      	new Thread(new Runnable() {
	        public void run() {
	        	Looper.prepare();
	        	
	        	adView.loadAd(adRequest);
	        }
	    }).start();
		
		Log.d("myApp", "ads started");
	}
	public void startProxy(View view) {
		Log.d("myApp", "start proxy");
		
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
			  //dataOutputStream = new DataOutputStream(socket.getOutputStream());
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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

}
