package com.imusic.rewallet.asyncnetwork;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;



public class HttpRequestHelper {

	public static HttpRequest getPostRequest(String url, JSONObject obj){
		HttpPost httpPost = new HttpPost(url);
		StringEntity se;
		try {
			if (obj != null) {
				se = new StringEntity(obj.toString());
				httpPost.setEntity(se);
			}
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpPost;
	}
	
	public static HttpRequest getGetRequest(String url){
		HttpGet httpGet = new HttpGet();
		try {
			URI uri = new URI(url);
			httpGet.setURI(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpGet;
	}
	
}
