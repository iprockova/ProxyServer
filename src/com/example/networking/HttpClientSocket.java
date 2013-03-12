package com.example.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;
import android.util.Log;

public class HttpClientSocket{
	 Socket socket = null;
	 DataOutputStream dataOutputStream = null;
	 DataInputStream dataInputStream = null;
	 
	public String execute(String input) {
	    try
	    {
	      // open a socket
	      Socket socket = openSocket("192.168.0.15", 8080);
	      
	      // write-to the socket.
	      writeToSocket(socket, input);
	      
	      String reply = readFromSocket(socket);
	      
	      // close the socket, and we're done
	      socket.close();
	      
	      return reply;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return null;
	    }
	}
	  
	  private void writeToSocket(Socket socket, String input) throws Exception
	  {
	    try 
	    {
	    	//write to socket 
	    	OutputStream out = socket.getOutputStream();
	    	out.write(input.getBytes("US-ASCII"));
	        out.flush();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	private String readFromSocket(Socket socket){
		
		try{
		//read from socket
		String res=null; 
    	String inputLine;
    	StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    while ((inputLine = in.readLine()) != null) {
		  System.out.println(inputLine);
		  response.append(inputLine + "\r\n");
	    }
	    response.append("\r\n");
	    
	    in.close();
	    
	    res = response.toString();
	    return res;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	  
	  
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
