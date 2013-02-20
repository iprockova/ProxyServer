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
        
        adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);              
        adRequest.addTestDevice("8C9CFB4E6D4629F186568482BC555C1C"); 
        adView.setGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        adView.setLayoutParams(layoutParams);
    	//adView.loadAd(adRequest);
        
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
			   
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
		  while(true){
			  Log.d("myApp", "while");
			  
			   try {
				socket = serverSocket.accept();
				Log.d("myApp", "socket accepted");
			    //dataInputStream = new DataInputStream(socket.getInputStream());
			   // dataOutputStream = new DataOutputStream(socket.getOutputStream());
			    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    String inputLine;
			    //CharSequence[] item = {"m","e", "d", "i", "a", ".", "a", "d", "m", "o", "b"};
			    while (!(inputLine = in.readLine()).equals("")){
			        System.out.println(inputLine);
			        if(inputLine.contains("media.admob")){
			        	//String response = relayRequest();
			        	//System.out.println("response: " + response);
			        	urlconnection();
			        	break;
			        }
			    }
			    
			    
			    
			    //System.out.println("ip: " + socket.getInetAddress());
			   // System.out.println("message: " + dataInputStream.readUTF());
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
			    if (in != null) {
			    	try {
						in.close();
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
	private void urlconnection(){
		HttpURLConnection urlConnection = null;
		   try {
			   URL url = new URL("http://www.android.com/");
			   urlConnection = (HttpURLConnection) url.openConnection();
		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     String result = readInputStreamAsString(in);
		     System.out.println("Result:" + result);
		   }  
		   catch(Exception e){
			   e.printStackTrace();
		   }
		    finally {
		     urlConnection.disconnect();
		   }
		 
	}
	public static String readInputStreamAsString(InputStream in) 
		    throws IOException {

		    BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		    int result = bis.read();
		    while(result != -1) {
		      byte b = (byte)result;
		      buf.write(b);
		      result = bis.read();
		    }        
		    return buf.toString();
		}
	private String relayRequest() {
		HttpClient client = new DefaultHttpClient();
		URI website;
		HttpResponse response = null;
		try {
			website = new URI("http://www.google.com");
			HttpGet request = new HttpGet(website);
			response = client.execute(request);
			Log.v("response code", response.getStatusLine()
                    .getStatusCode() + ""); 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String html = "";
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
			    str.append(line);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		html = str.toString();
		return html;
		
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
