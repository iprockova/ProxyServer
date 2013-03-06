package com.example.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class HttpClientSocket extends AsyncTask<Void, Void, Void>{
	 Socket socket = null;
	 DataOutputStream dataOutputStream = null;
	 DataInputStream dataInputStream = null;
	 private volatile boolean running = true;
	 
	 @Override
		protected void onCancelled() {
		        running = false;
		}

	@Override
	protected Void doInBackground(Void... params) {
		String testServerName = "130.233.194.86";
	    int port = 8080;
	    try
	    {
	      // open a socket
	      Socket socket = openSocket(testServerName, port);
	      
	      // write-to, and read-from the socket.
	      // in this case just write a simple command to a web server.
	      String result = writeToAndReadFromSocket(socket, "GET /\n\n");
	      
	      // print out the result we got back from the server
	      System.out.println(result);

	      // close the socket, and we're done
	      socket.close();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
		return null;
	}
	  
	  private String writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception
	  {
	    try 
	    {
	      // write text to the socket
	      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	      bufferedWriter.write(writeTo);
	      bufferedWriter.flush();
	      
	      // read text from the socket
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      StringBuilder sb = new StringBuilder();
	      String str;
	      while ((str = bufferedReader.readLine()) != null)
	      {
	        sb.append(str + "\n");
	      }
	      
	      // close the reader, and return the results as a String
	      bufferedReader.close();
	      return sb.toString();
	    } 
	    catch (IOException e) 
	    {
	      e.printStackTrace();
	      throw e;
	    }
	  }
	  
	  /**
	   * Open a socket connection to the given server on the given port.
	   * This method currently sets the socket timeout value to 10 seconds.
	   * (A second version of this method could allow the user to specify this timeout.)
	   */
	  private Socket openSocket(String server, int port) throws Exception
	  {
	    Socket socket;
	    
	    // create a socket with a timeout
	    try
	    {
	      InetAddress inteAddress = InetAddress.getByName(server);
	      SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);
	  
	      // create a socket
	      socket = new Socket();
	  
	      // this method will block no more than timeout ms.
	      int timeoutInMs = 10*1000;   // 10 seconds
	      socket.connect(socketAddress, timeoutInMs);
	      
	      return socket;
	    } 
	    catch (SocketTimeoutException ste) 
	    {
	      System.err.println("Timed out waiting for the socket.");
	      ste.printStackTrace();
	      throw ste;
	    }
	  }
}
