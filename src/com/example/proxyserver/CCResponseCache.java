package com.example.proxyserver;

import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CCResponseCache extends ResponseCache {

	Map mCache = new HashMap();
	
	@Override
	public CacheRequest put(URI uri, URLConnection conn) {
		CacheRequest req;
		try{
		req =  (CacheRequest)new CCCacheRequest();

		Map<String, List<String>> headers = conn.getHeaderFields();
		CacheResponse resp = (CacheResponse) new CCCacheResponse(headers, req.getBody());

		// For some reason the path of the URI being passed is an empty string.
		// Get a good URI from the connection object.
		try {
			uri = conn.getURL().toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		mCache.put(uri, resp);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return req;
	}

	@Override
	public CacheResponse get(URI uri, String requestMethod,
			Map<String, List<String>> requestHeaders) throws IOException {
		CacheResponse resp = (CacheResponse) mCache.get(uri);
		return resp;
	}
}
