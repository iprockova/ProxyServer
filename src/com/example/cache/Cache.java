package com.example.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.networking.HttpClientSocket;
import com.example.proxyserver.Util;


public class Cache {
	private static Cache INSTANCE = null;
    private Map<Integer, AdHttpContent> cacheMap = null;
    private Map<String, byte[]> cacheMapScripts = null;
    private Map<String, byte[]> cacheMapImages = null;
    private int currentAd = 0;
    
	private Cache (){
		if (cacheMap!=null || cacheMapScripts!=null || cacheMapImages!=null)
			return;
		else{
			 cacheMap = Collections.synchronizedMap(new LinkedHashMap<Integer, AdHttpContent>());
			 cacheMapScripts = Collections.synchronizedMap(new LinkedHashMap<String, byte[]>());
			 cacheMapImages =  Collections.synchronizedMap(new LinkedHashMap<String, byte[]>());
		}
	}
	
	 private synchronized static void createInstance() {
	        if(INSTANCE == null) {
	            INSTANCE = new Cache();
	        }
	    }
	 
	 public static Cache getInstance() {
	        if(INSTANCE == null) createInstance();
	        return INSTANCE;
	    }
	
	//insert data into cache
	public void insert(AdHttpContent ad){
		int adIndex = 1 + Util.getAdCacheIndex(ad.getRequest());
		cacheMap.put(adIndex, ad);
	}
	
	public void insertScripts(String request, byte[] response){
		cacheMapScripts.put(request, response);
	}
	public void insertImages(String request, byte[] response){
		cacheMapImages.put(request, response);
	}

	public byte[] retreiveScripts(String request){
		byte[] response = cacheMapScripts.get(request);
		return response;
	}
	public byte[] retreiveImages(String request){
		byte[] response = cacheMapImages.get(request);
		return response;
	}
	//retreive data from cache
	public AdHttpContent  retreive(int adNumber){
		AdHttpContent ad = cacheMap.get(adNumber);
		return ad;
	}
	
	public void remove(int location){
		cacheMap.remove(location);
	}
	public boolean isEmpty(){
		return cacheMap.isEmpty();
	}
	public int getCacheSize(){
		int size = cacheMap.size();
		return size;
	}
	public Map<Integer, AdHttpContent> getCacheMap(){
		return cacheMap;
	}
	public void setCurrentAd(int number){
		currentAd = number;
	}

	public void fillUpCache(){
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			String request = cacheMap.get(1).getRequest();
			int adRequestNumber =  currentAd + 1;
			int adCacheIndex = 1;
			  @Override
			  public void run() {
				  	String adModified = request.substring(0,request.indexOf("=") + 1) + adRequestNumber + request.substring(request.toString().indexOf("&"));
				  	int pos = adModified.indexOf("seq_num=") + 8;
				  	String finalAdString = adModified.substring(0, pos)+ (adRequestNumber + 1) + adModified.substring(adModified.indexOf("&", pos));
					
				  	byte [] outputLine = new HttpClientSocket().execute(finalAdString); 
				  	
				  	//System.out.println("Reply: " + outputLine.toString());
					
					cacheMap.put(adCacheIndex, new AdHttpContent(request.toString(),outputLine,0));
					
					adRequestNumber++;
					currentAd ++ ;
					adCacheIndex++;
					
					if(adCacheIndex > 5){
						timer.cancel();
						return;
					}
			  }
			}, 10, 30*1000);
		return;
		
			
	}
	
}
