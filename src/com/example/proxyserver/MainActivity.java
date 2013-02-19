package com.example.proxyserver;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends Activity {

	 ServerSocket serverSocket = null;
	  Socket socket = null;
	  DataInputStream dataInputStream = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 Log.w("myApp", "no network");
		 
		 new Thread(new Runnable() {
		        public void run() {
		            server();
		        }
		    }).start();
		 
			    
			
		 
	}
	public void server(){
		try {
			   serverSocket = new ServerSocket(8888);
			   System.out.println("Listening :8888");
			   Log.w("myApp", "no network1");
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
		  while(true){
			   try {
				   System.out.println("Before socket accepted");
			    socket = serverSocket.accept();
			    System.out.println("Socket accepted");
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

}
