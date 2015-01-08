package com.imusic.rewallet.asyncnetwork;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;


public class HttpSender implements Runnable {
	private static final String  TAG = "HttpSender";
	
	private InputHolder _input;
	private Activity _context;
	public HttpSender(Activity context, InputHolder input){
		_input = input;
		_context = context;
	}
	@Override
	public void run() {
		final OutputHolder result = sendData(_input);
		final AsyncResponseListener listener = result.getResponseListener();  
        final HttpEntity response = result.getResponse();  
        final Throwable exception = result.getException();  
        _context.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				if(response!=null){  
		            //ShowDebugToast.Debug(false, TAG, "listener.onResponseReceived(response)");
		        	listener.onResponseReceived(response, result.getTag());  
		        }else{ 
		        	//ShowDebugToast.Debug(false, TAG, "listener.onResponseReceived(exception)");
		        	listener.onResponseReceived(exception, result.getTag());  
		        } 
			}
        	
        });
         
	}
	
	private OutputHolder sendData(InputHolder input){
		HttpEntity entity = null;  
        try {  
            HttpResponse response = AsyncHttpClient.getClient().execute((HttpUriRequest) input.getRequest());  
            StatusLine status = response.getStatusLine();  
              
            if(status.getStatusCode() >= 300) {  
                return new OutputHolder(  
                        new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),  
                        input.getResponseListener(),input.getTag());  
            }  
              
            entity = response.getEntity();  
            //ShowDebugToast.Debug(false, TAG, "isChunked:" + entity.isChunked());
            if(entity != null) {  
                try{  
                    entity = new BufferedHttpEntity(entity);  
                }catch(Exception e){  
                	//ShowDebugToast.Debug(false, TAG, e.getMessage());
                }  
            }             
        } catch (ClientProtocolException e) {  
            Log.e(TAG, e.getMessage());
            //return new OutputHolder(e, input.getResponseListener(),input.getTag());  
        } catch (IOException e) {  
        	Log.e( TAG, e.getMessage());
            //return new OutputHolder(e, input.getResponseListener(),input.getTag());  
        }  
        return new OutputHolder(entity, input.getResponseListener(),input.getTag());  
	}
}
