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
}
