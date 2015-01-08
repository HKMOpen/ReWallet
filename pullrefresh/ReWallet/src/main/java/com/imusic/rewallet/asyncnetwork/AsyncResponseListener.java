package com.imusic.rewallet.asyncnetwork;

import org.apache.http.HttpEntity;

public interface AsyncResponseListener {
	/** Handle successful response */  
    public void onResponseReceived(HttpEntity response, String tag);  
      
    /** Handle exception */  
    public void onResponseReceived(Throwable response, String tag);  
}
