package com.example.proxyserver;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import com.google.ads.*;

public class MainActivity extends Activity {

	 ServerSocket serverSocket = null;
	 Socket socket = null;
	 DataInputStream dataInputStream = null;
	 
	private AdView adView;
	private AdRequest adRequest;
	LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	public void startAds(View view){
		Log.d("myApp", "start ads");
		adView = new AdView(this, AdSize.BANNER, "a15122003cf3080");
		
		layout = (LinearLayout)findViewById(R.id.linearLayout);
        layout.addView(adView);
        
      	new Thread(new Runnable() {
	        public void run() {
	        	Looper.prepare();
	        	adRequest = new AdRequest();
	            adRequest.addTestDevice(AdRequest.TEST_EMULATOR);              
	            adRequest.addTestDevice("8C9CFB4E6D4629F186568482BC555C1C"); 
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
			   
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
		  while(true){
			  Log.d("myApp", "while");
			  
			   try {
				socket = serverSocket.accept();
				Log.d("myApp", "socket accepted");
			    dataInputStream = new DataInputStream(socket.getInputStream());
			   // dataOutputStream = new DataOutputStream(socket.getOutputStream());
			    System.out.println("ip: " + socket.getInetAddress());
			    System.out.println("message: " + dataInputStream.readUTF());
			    //dataOutputStream.writeUTF("Hello!");
			   } catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }
			   finally{
			    if( socket!= null){
			     try {
			      socket.close();
			     } catch (IOException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			     }
			    }
			    
			    if( dataInputStream!= null){
			     try {
			      dataInputStream.close();
			     } catch (IOException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			     }
			    }
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
