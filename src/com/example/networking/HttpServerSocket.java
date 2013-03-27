package com.example.networking;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;

public class HttpServerSocket extends AsyncTask<Void, Void, Void>{
	private ServerSocket serverSocket = null;
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			   serverSocket = new ServerSocket(8888);
			   System.out.println("Listening :8888");
				  while(true){
					  ClientWorker w;
					    try{
					      w = new ClientWorker(serverSocket.accept());
					      Thread t = new Thread(w);
					      t.start();
					    } catch (IOException e) {
					      System.out.println("Accept failed: 8888");
					      System.exit(-1);
					    }
				  }
				  
		}catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		finally{
		}
		return null;
	}
	public class ClientWorker implements Runnable{
		private Socket client;

		  ClientWorker(Socket client) {
		    this.client = client;
		  }
		  
		@Override
		public void run() {
			String inputLine;
		    BufferedReader in = null;
		    OutputStream out = null;
		    try{
			    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			    out = client.getOutputStream();
		   
			    byte [] outputLine;
			    StringBuffer request = new StringBuffer();
		    
			    while(true){
			        inputLine = in.readLine();
			        if(inputLine != null){
				        System.out.println(inputLine);
					    if(!(inputLine.equals("")))
					    	request.append(inputLine + "\r\n");
					    else {
					    	request.append("\r\n");
					    	outputLine = new HttpClientSocket().execute(request.toString());
						    out.write(outputLine);
						    out.flush();
						    request  = new StringBuffer();
					    }
			         }
			     }
			    }catch (IOException e) {
			       System.out.println("Read failed");
			       System.exit(-1);
			   }
		} 
	}
}
