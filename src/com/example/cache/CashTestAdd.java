package com.example.cache;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.view.View;

import com.example.networking.MyHttpResponse;
import com.example.proxyserver.Util;

public class CashTestAdd extends AsyncTask<Void, Void, Void>{
	private String url = "http://media.admob.com/sdk-core-v40.js";
	//CACHE Test: add to cash
		private void addToCache(){
			MyHttpResponse file = getFile(url);
			saveToCache(url, file);
		}
	//CACHE Test: download file from URL and store the HTTP response to MyHttpResponse
		private MyHttpResponse getFile(String cacheurl){
		   URL url = null;
		   HttpURLConnection urlConnection = null;
		   
		   try {
			   url = new URL(cacheurl);
			   urlConnection = (HttpURLConnection) url.openConnection();
			   
			   //get headers from response
			   Map<String, List<String>> headersMap = urlConnection.getHeaderFields();
			   String headersString = Util.convertMapToString(headersMap);
			   
			   //get content from response
			   String contentString = Util.convertInputStreamToString(urlConnection.getInputStream());
			   
			   MyHttpResponse response = new MyHttpResponse();
			   response.setBody(contentString);
			   response.setHeaders(headersString);
			   return response;
		   }
		   catch(IOException e){
			   e.printStackTrace();
			   return null;
		   }catch(Exception e){
			   e.printStackTrace();
			   return null;
		   }finally {
		     urlConnection.disconnect();
		   }
		}
		//CACHE Test: cache the HTTP response
		private void saveToCache(String cacheurl, MyHttpResponse response){
			CacheStore cache = CacheStore.getInstance();
			cache.saveCacheFile(cacheurl, response);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			addToCache();
			return null;
		}
}
