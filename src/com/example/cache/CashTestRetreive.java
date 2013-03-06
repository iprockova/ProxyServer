package com.example.cache;

import com.example.networking.MyHttpResponse;

import android.os.AsyncTask;

public class CashTestRetreive extends AsyncTask<String, Void, MyHttpResponse>{
	//CACHE Test: retrieve HTTP response from cache
	private MyHttpResponse getFromCache(String cacheurl){
		CacheStore cache = CacheStore.getInstance();
		return cache.getCacheFile(cacheurl);
	}
	@Override
	protected MyHttpResponse doInBackground(String... params) {
		String url = params[0];
		MyHttpResponse response = getFromCache(url);
		return response;
	}

}
