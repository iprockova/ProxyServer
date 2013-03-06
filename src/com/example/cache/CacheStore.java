package com.example.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.example.networking.MyHttpResponse;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
 
public class CacheStore {
    private static CacheStore INSTANCE = null;
    private HashMap<String, String> cacheMap;
    private HashMap<String, MyHttpResponse> responseMap;
    private static final String cacheDir = "/sdcard/Android/data/com.example.proxyserver/cache/";
    private static final String CACHE_FILENAME = ".cache";
 
    private CacheStore() {
        cacheMap = new HashMap<String, String>();
        responseMap = new HashMap<String, MyHttpResponse>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        if(!fullCacheDir.exists()) {
            Log.i("CACHE", "Directory doesn't exist");
            cleanCacheStart();
            return;
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
            cacheMap = (HashMap<String,String>)is.readObject();
            is.close();
        } catch (StreamCorruptedException e) {
            Log.i("CACHE", "Corrupted stream");
            cleanCacheStart();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "File not found");
            cleanCacheStart();
        } catch (IOException e) {
            Log.i("CACHE", "Input/Output error");
            cleanCacheStart();
        } catch (ClassNotFoundException e) {
            Log.i("CACHE", "Class not found");
            cleanCacheStart();
        }
    }
 
    private void cleanCacheStart() {
        cacheMap = new HashMap<String, String>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        fullCacheDir.mkdirs();
        File noMedia = new File(fullCacheDir.toString(), ".nomedia");
        try {
            noMedia.createNewFile();
            Log.i("CACHE", "Cache created");
        } catch (IOException e) {
            Log.i("CACHE", "Couldn't create .nomedia file");
            e.printStackTrace();
        }
    }
 
    private synchronized static void createInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CacheStore();
        }
    }
 
    public static CacheStore getInstance() {
        if(INSTANCE == null) createInstance();
        return INSTANCE;
    }
 
    public void saveCacheFile(String cacheUri, MyHttpResponse response) {
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS").format(new java.util.Date());
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(fileUri);
            ObjectOutputStream save = new ObjectOutputStream(outStream);
            save.writeObject(response);
            save.close();
            
            cacheMap.put(cacheUri, fileLocalName);
            Log.i("CACHE", "Saved file "+cacheUri+" (which is now "+fileUri.toString()+") correctly");
            responseMap.put(cacheUri, response);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
            os.writeObject(cacheMap);
            os.close();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "Error: File "+cacheUri+" was not found!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("CACHE", "Error: File could not be stuffed!");
            e.printStackTrace();
        }
    }
 
    public MyHttpResponse getCacheFile(String cacheUri) {
        if(responseMap.containsKey(cacheUri)) return (MyHttpResponse)responseMap.get(cacheUri);
        else return null;
    }
    
    public Bitmap downloadFile(String fileUrl){
    	Bitmap bmImg;
    	URL myFileUrl;
    	
		try {
			 myFileUrl= new URL(fileUrl);
			 
	    	 HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
	    	 conn.setDoInput(true);
	    	 conn.connect();
	    	 
	    	 InputStream is = conn.getInputStream();
	    	 
	    	 bmImg = BitmapFactory.decodeStream(is);
	    	 
	    	 return bmImg;
		 }catch (MalformedURLException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	    	 return null;
		 }catch (IOException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	    	 return null;
		 }
    }
    

}
