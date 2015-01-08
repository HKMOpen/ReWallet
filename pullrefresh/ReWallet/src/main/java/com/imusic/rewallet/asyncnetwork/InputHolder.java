package com.imusic.rewallet.asyncnetwork;

import android.content.Context;

import org.apache.http.HttpRequest;

public class InputHolder {
	private HttpRequest _request;  
    private AsyncResponseListener _responseListener;  
    private String _tag;
    public InputHolder(HttpRequest request
    		, AsyncResponseListener responseListener
    		, String tag
    		, Context cxt){  
        this._request = request;  
        this._responseListener = responseListener;
        this._tag = tag;
        _context = cxt;
    }  
    
    public String getTag(){
    	return _tag;
    }
    public HttpRequest getRequest() {  
        return _request;  
    }  
  
    public AsyncResponseListener getResponseListener() {  
        return _responseListener;  
    }
    
    private Context _context = null;
    public Context getContext(){
    	return _context;
    }
}
