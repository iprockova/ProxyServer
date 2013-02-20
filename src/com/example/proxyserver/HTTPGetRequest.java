package com.example.proxyserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class HTTPGetRequest extends AsyncTask<Void, Void, String>{
	
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		URI website;
		HttpResponse response = null;
		try {
			website = new URI("http://media.admob.com/sdk-core-v40.js");
			HttpGet request = new HttpGet(website);
			response = client.execute(request);
			Log.v("response code", response.getStatusLine()
                    .getStatusCode() + ""); 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String html = "";
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
			    str.append(line);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		html = str.toString();
		return html;
	}
	protected void onPostExecute(Long result) {
        System.out.println("poost execute");
    }
}
