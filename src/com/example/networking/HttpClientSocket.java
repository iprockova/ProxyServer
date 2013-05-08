package com.example.networking;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


public class HttpClientSocket{
	
	 DataOutputStream dataOutputStream = null;
	 DataInputStream dataInputStream = null;
	 
	public byte[] execute(String input) {
		Socket socket = null;
		//String testServerName = "192.168.0.11";
		String testServerName = "86.50.145.156";
	    int port = 8080;
	    try
	    {
	      // open a socket
	      socket = openSocket(testServerName, port);
	      
	      // write-to and read-from the socket.
	      byte[] response =  writeToAndReadFromSocket(socket, input);
	      
	      // close the socket, and we're done
	      socket.close();
	      
	      return response;
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return null;
	    }finally{
			try {
				if(socket!= null) socket.close();
			}catch (IOException e) {
			      e.printStackTrace();
			}
		}
	}
	private byte[] writeToAndReadFromSocket(Socket socket, String input) throws Exception
	  {
	    try 
	    {
	    	//write to socket 
	    	OutputStream out = socket.getOutputStream();
	    	out.write(input.getBytes());
	        out.flush();
	    	
	    	//read from socket
	        InputStream in = socket.getInputStream();
	        byte [] response = ByteReader.readResponse(in);
		    
		    out.close();
		    in.close();
	    	return response;
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
	      int timeoutInMs = 15*1000;   // 10 seconds
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
