package com.example.networking;

import java.io.Serializable;

public class MyHttpResponse implements Serializable{
	String headers;
	String body;

	public String getBody() {
		return body;
	}
	
	public String getHeaders() {
		return headers;
	}
	
	public void setBody(String body){
		this.body = body; 
	}
	
	public void setHeaders(String headers){
		this.headers = headers;
	}
	
	 @Override
	   public String toString() {
  	   return new StringBuffer(" Headers : ")
  	   .append(this.headers)
  	   .append(" Body : ")
  	   .append(this.body).toString();
	   }
}
