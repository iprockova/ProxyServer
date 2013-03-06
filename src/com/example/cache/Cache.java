package com.example.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.networking.MyHttpResponse;

public class Cache {
	private LinkedList<HashMap<String, MyHttpResponse>> list;
	
	public Cache (){
		list = new LinkedList<HashMap<String, MyHttpResponse>>();
	}
	
	//insert data into cache
	public void insert(int location, HashMap<String, MyHttpResponse> simpleList){
		  list.add(location, simpleList);
	}
	public void insertFirst(HashMap<String, MyHttpResponse> simpleList){
		  list.addFirst(simpleList);
	}
	public void insertLast(HashMap<String, MyHttpResponse> simpleList){
		  list.addLast(simpleList);
	}
	
	//retreive data from cache
	public HashMap<String, MyHttpResponse> retreive(int location){
		System.out.println("Retreive data: " + list.get(location));
		return list.get(location);
	}
	
	//remove data from cache
	public void remove(int location){
		list.remove(location);
	}
	
	//print the whole cache
	public void print(){
		  Iterator<HashMap<String, MyHttpResponse>> iterator = list.iterator(); 
		  while (iterator.hasNext()){
			  System.out.print(iterator.next()+" ");  
		  }
		  System.out.println();
	}
}
