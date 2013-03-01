package com.example.proxyserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class HttpResponse {
	Map<String, List<String>> headers;
	ByteArrayOutputStream body;

	public HttpResponse(Map<String, List<String>> headers, OutputStream body) {
		this.headers = headers;
		this.body = (ByteArrayOutputStream)body; 
	}


	public InputStream getBody() {
		return new ByteArrayInputStream(body.toByteArray());
	}

	
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	
}
