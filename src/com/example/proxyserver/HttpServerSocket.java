package com.example.proxyserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpServerSocket extends AsyncTask<Void, Void, Void>{
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	private DataInputStream dataInputStream = null;
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
			  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			  while (!(inputLine = in.readLine()).equals("")) {
			      System.out.println(inputLine);
			      if(inputLine.contains(ADMOB_SERVER_1) || inputLine.contains(ADMOB_SERVER_2) || inputLine.contains(ADMOB_SERVER_3)){
			    	  //contact cache
			      }
			  }
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
		return null;
	}
}
