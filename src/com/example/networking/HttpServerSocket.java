package com.example.networking;

import java.io.*;
import java.net.*;
import java.util.Map;

import com.example.cache.AdHttpContent;
import com.example.cache.Cache;
import com.example.proxyserver.Util;

import android.os.AsyncTask;
import android.util.Log;

public class HttpServerSocket extends AsyncTask<Void, Void, Void>{
	private ServerSocket serverSocket = null;
	private boolean refreshCache = false;
	@Override
	protected Void doInBackground(Void... params) {
		try {
			   serverSocket = new ServerSocket(8888);
			   System.out.println("Listening :8888");
			   Cache cacheMap = Cache.getInstance();
				  while(true){
					  if(refreshCache){
						  cacheMap.fillUpCache();
						  refreshCache = false;
					  }
					  ClientWorker w;
					    try{
					      Socket client = serverSocket.accept();
					      w = new ClientWorker(client, cacheMap);
					      Thread t = new Thread(w);
					      System.out.println("Thread Name: " + t.getName() + ", thread ID: " + t.getId() + ", local port: " + client.getLocalPort() + ", port: " + client.getPort());
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
		private Cache cacheMap;
		  ClientWorker(Socket client, Cache cacheMap) {
		    this.client = client;
		    this.cacheMap = cacheMap;
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
					    if(!(inputLine.equals("")))
					    	request.append(inputLine + "\r\n");
					    else {
					    	request.append("\r\n");
					    	
					    	String requestStr = request.toString();
					    	
					    	//Log.d("Req", request.toString());
					    	//System.out.println(requestStr);
					    	
					    	outputLine = getReply(requestStr);
					    	
					    	if(outputLine != null){
					    		 out.write(outputLine);
								 out.flush();
					    	}
						    request  = new StringBuffer();
					    }
			         }else{
			        	 break;
			         }
			     }
			    client.close();
			    }catch (IOException e) {
			       System.out.println("Read failed");
			       System.exit(-1);
			   }
		}
		private byte[] getReply(String requestStr) {
			byte[] outputLine = null;
			
			//Ad requests that contain the actual request for the ad (../mads/..)
	    	if(isAdRequest(requestStr)){
	    		
	    		int currentAd = Util.getAdNumber(requestStr);
	    		cacheMap.setCurrentAd(currentAd);
	    		
	    		//Relay first 5 ads and save to CASH 
		    	if(currentAd < 6){
		    		outputLine = new HttpClientSocket().execute(requestStr);
		    		cacheMap.insert(new AdHttpContent(requestStr,outputLine, 0));
		    		
		    	}else{
		    		//Retrieve next ads from CASH
		    		int adCacheIndex = Util.getAdCacheIndex(requestStr); //only 5 ads in the cash allowed(adNumber can be only one of 1,2,3,4 and 5)
		    		AdHttpContent ad = cacheMap.retreive(adCacheIndex);
		    		outputLine = ad.getResponse();
	    			checkAdTTL(ad);
		    	}
	    	} else{
	    		if(requestStr.contains("GET /csi?v") && requestStr.contains("Host: csi.gstatic.com")){
	    			String s = "HTTP/1.1 204 No Content\r\n" + 
	    					"Content-Length: 0\r\n" +
	    					"Pragma: no-cache\r\n" +
	    					"Cache-Control: private, no-cache\r\n" +
	    					"Content-Type: image/gif\r\n" +
	    					"Server: Golfe\r\n" +
	    					"\r\n";
	    			outputLine = s.getBytes();
	    		}else if((requestStr.contains("GET /sdk-core-v40.js") || requestStr.contains("GET /formats/")) && requestStr.contains("Host: media.admob.com")){
	    			byte[] response = cacheMap.retreiveScripts(requestStr);
	    			if(response == null){
	    				outputLine = new HttpClientSocket().execute(requestStr);
	    				cacheMap.insertScripts(requestStr, outputLine);
	    			}else
	    				outputLine = response;
	    			
	    		}
	    		else //Relay all other ad http requests such as scripts, images, etc.
	    			outputLine = new HttpClientSocket().execute(requestStr);
	    	}
	    	
	    	return outputLine;
		}

		private void checkAdTTL(AdHttpContent ad) {
			int adTTL = ad.getTTL();
			if(adTTL <= 1){
				ad.setTTL(adTTL + 1);
			}else{
				refreshCache = true;
				//set ad ttl 0 for all ads
				Map<Integer, AdHttpContent> cacheContent = cacheMap.getCacheMap();
				for(int i=0; i <= 5; i ++){
					AdHttpContent currentAd = cacheContent.get(i);
					currentAd.setTTL(0);
				}
			}
		}

		private boolean isAdRequest(String request){
			if((request.contains("GET /mads/gma?preqs=") && request.contains("Host: googleads.g.doubleclick.net")))
				return true;
			else return false;
		}

	}
}
