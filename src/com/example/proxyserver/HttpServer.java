package com.example.proxyserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class HttpServer {
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private BufferedReader in = null;
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
}
