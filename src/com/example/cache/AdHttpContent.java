package com.example.cache;

import java.io.Serializable;

public class AdHttpContent implements Serializable{
	String request;
	byte[] response;
	int TTL; //TimeToLive 

	public AdHttpContent(String request, byte[] response, int TTL){
		this.request = request; 
		this.response = response;
		this.TTL = TTL;
	}
	public String getRequest() {
		return request;
	}
	
	public byte[] getResponse() {
		return response;
	}
	public int getTTL() {
		return TTL;
	}
	
	public void setRequest(String request){
		this.request = request; 
	}
	
	public void setResponse(byte[] response){
		this.response = response;
	}
	public void setTTL(int TTL){
		this.TTL = TTL;
	}
	
//	 @Override
//	   public String toString() {
//  	   return new StringBuffer(" Headers : ")
//  	   .append(this.header)
//  	   .append(" Body : ")
//  	   .append(this.body).toString();
//	   }
}
