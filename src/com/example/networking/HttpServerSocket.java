package com.example.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
			   Log.d("myApp", "server socket listening :8888");

			  socket = serverSocket.accept();
			  
			  //read the request from the socket
			  String request = readFromSocket(socket);
			  
			  //initiate client socket and wait for result
			  String reply = new HttpClientSocket().execute(request);
			  
			  //write the result back to the socket
			   writeToSocket(socket, reply);
			   System.out.println("HttpServerSocket: reply sent to AdMob");
		
		  //close the socket	   
		  socket.close();
		  
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
			}catch (IOException e) {
			      e.printStackTrace();
			}
		}
		return null;
	}
	private String readFromSocket(Socket socket) {
		  try{
			  
			  String inputLine = "";
			  InputStream input = socket.getInputStream();
			  in = new BufferedReader(new InputStreamReader(input));
			  
			  StringBuffer request = new StringBuffer();
			  //print the input
			  while (!(inputLine = in.readLine()).equals("")) {
			      System.out.println(inputLine);
			      if(!(inputLine.contains("Accept-Encoding")))
			    	  request.append(inputLine + "\r\n");
			  }
			  request.append("\r\n");
			  
			  return request.toString();
		  }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	private void writeToSocket(Socket socket, String response) {
		try{
			OutputStream out = socket.getOutputStream();
	    	out.write(response.getBytes());
	        out.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
