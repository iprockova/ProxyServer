package com.example.proxyserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.content.Context;

class MyResponseCache extends ResponseCache {
	private Context applicationContext;
	public MyResponseCache(Context context) {
		applicationContext = context;
	}
	@Override
	public CacheResponse get(URI uri, String s, Map<String, List<String>> headers) throws IOException {
		 final File file = new File(applicationContext.getExternalCacheDir(), escape(uri.getPath()));
		 if (file.exists()) {
	            return new CacheResponse() {
	                @Override
	                public Map<String, List<String>> getHeaders() throws IOException {
	                    return null;
                }

	                @Override
	                public InputStream getBody() throws IOException {
	                    return new FileInputStream(file);
	                }
	            };
	     } else {
	            return null;
	     }
	}
	
	@Override
	public CacheRequest put(URI uri, URLConnection connection) throws IOException {
		final File file = new File(applicationContext.getExternalCacheDir(), escape(connection.getURL().getPath()));
		return new CacheRequest() {
	        @Override
	        public OutputStream getBody() throws IOException {
	            return new FileOutputStream(file);
	        }
	
	        @Override
	        public void abort() {
	            file.delete();
	        }
		};
	}
	
	 private String escape(String url) {
	       return url.replace("/", "-").replace(".", "-");
	    }
	  
}
