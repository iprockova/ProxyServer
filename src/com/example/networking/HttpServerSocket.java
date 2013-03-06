package com.example.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.cache.CashTestRetreive;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpServerSocket extends AsyncTask<Void, Void, Void>{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private BufferedReader in = null;
	
	private volatile boolean running = true;
	
	private static String ADMOB_SERVER_1 = "media.admob.com";
	private static String ADMOB_SERVER_2 = "googleads.g.doubleclick.net";
	private static String ADMOB_SERVER_3 = "pagead2.googlesyndication.com";
	
	@Override
	protected void onCancelled() {
	        running = false;
	}


	@Override
	protected Void doInBackground(Void... params) {
		try {
			   serverSocket = new ServerSocket(8888);
			   Log.d("myApp", "Listening :8888");
			   String inputLine;

		  while(running){
			  socket = serverSocket.accept();
			  Log.d("myApp", "socket accepted");
			  //dataInputStream = new DataInputStream(socket.getInputStream());
			  dataOutputStream = new DataOutputStream(socket.getOutputStream());
			  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 
			  StringBuffer request = new StringBuffer();
			  while (!(inputLine = in.readLine()).equals("")) {
			      System.out.println(inputLine);
			      request.append(inputLine);
			  }
			  
			  if (request.toString().contains("media.admob.com")){
				  //MyHttpResponse response  = new CashTestRetreive().execute("http://media.admob.com/sdk-core-v40.js").get();
				  System.out.println("Sending data back");
				  dataOutputStream.writeUTF("HTTP/1.1 200 OK");
				  dataOutputStream.writeUTF("Age:3073");
				  dataOutputStream.writeUTF("Cache-Control:public, max-age=3600");
				  dataOutputStream.writeUTF("Content-Type:text/javascript");
				  System.out.println("Sending data back");
			  }
			   }
		}catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e){
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
			    if(dataOutputStream!= null)
				      dataOutputStream.close();
			}catch (IOException e) {
			      e.printStackTrace();
			     }
		}
		return null;
	}
}
