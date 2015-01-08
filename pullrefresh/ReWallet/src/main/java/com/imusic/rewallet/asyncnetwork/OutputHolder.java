package com.imusic.rewallet.asyncnetwork;

import org.apache.http.HttpEntity;

public class OutputHolder {
	private HttpEntity _response;  
    private Throwable _exception;  
    private AsyncResponseListener _responseListener;  
    private String _tag;
    
    public OutputHolder(HttpEntity response, AsyncResponseListener responseListener,String tag){  
        this._response = response;  
        this._responseListener = responseListener;  
        this._tag = tag;
    }  
      
    public OutputHolder(Throwable exception, AsyncResponseListener responseListener,String tag){  
        this._exception = exception;  
        this._responseListener = responseListener;  
        this._tag = tag;
    }  
  
    public String getTag(){
    	return _tag;
    }
    public HttpEntity getResponse() {  
        return _response;  
    }  
  
    public Throwable getException() {  
        return _exception;  
    }  
      
    public AsyncResponseListener getResponseListener() {  
        return _responseListener;  
    }  
}
