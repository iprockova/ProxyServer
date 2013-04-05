package com.example.proxyserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class Util {
	 public static String convertMapToString(Map<String, List<String>> headersMap){
		  StringBuffer mapBuffer = new StringBuffer();
		  for (Map.Entry<String,  List<String>> entry : headersMap.entrySet()) {
			    mapBuffer.append(entry.getKey() + ":" + entry.getValue() + ",");
		  }
		  return mapBuffer.toString();
	  }
	 
	 public static String convertInputStreamToString( InputStream inputStream ){
		  
	        String strText = "";
	        if( inputStream != null ){
	            StringWriter sw = new StringWriter();
	            char[] bufText = new char[1024];
	            BufferedReader reader = null;
	            try{
	                reader = new BufferedReader( new InputStreamReader( inputStream ,"UTF-8" ) );
	                int charRead = 0;
	 
	                while((charRead = reader.read( bufText )) != -1) {
	                    sw.write(bufText, 0, charRead);
	                }
	                strText = sw.toString();
	            }catch(Exception es){
	                es.printStackTrace();
	            }finally{
	                try{
	                    reader.close();
	                }catch( IOException es ){
	                }
	                try{
	                    inputStream.close();
	                }catch( IOException es ){
	                }
	                try{
	                    sw.close();
	                }catch( IOException es ){
	                }
	            }
	        }
	        return strText;
	    }
	 
	 /*
	  *  Retreive ad number from the request. Only 1,2,3,4,5 are allowed as adNumbers. 
	  *  If the number is bigger than 5 its module of 5 is returned(adNum%5).
	  */
	 public static int getAdCacheIndex(String request) {
		    String adNumber = request.substring(request.indexOf('=') + 1, request.indexOf('&'));
		    int adNum = Integer.parseInt(adNumber);
		    int num=0;
		    if(adNum > 5 && adNum%5!=0 ) 
		    	num = adNum % 5;
		    else {
		    	if(adNum > 5  && adNum%5 ==0)
					num = 5;
				else num = adNum;
		    }
			return num;
		}
	 public static int getAdNumber(String request){
		 String adNumber = request.substring(request.indexOf('=') + 1, request.indexOf('&'));
		 return Integer.parseInt(adNumber);
	 }
	 
}
