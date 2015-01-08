package com.imusic.rewallet.asyncnetwork;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

public abstract  class AbstractAsyncResponseListener implements AsyncResponseListener{
	public static final int RESPONSE_TYPE_STRING = 1;  
    public static final int RESPONSE_TYPE_JSON_ARRAY = 2;  
    public static final int RESPONSE_TYPE_JSON_OBJECT = 3;  
    public static final int RESPONSE_TYPE_STREAM = 4;  
    
    private static final String TAG = "AbstractAsyncResponseListener";
    
    private int responseType;  
      
    public AbstractAsyncResponseListener(){  
        this.responseType = RESPONSE_TYPE_STRING; // default type  
    }  
      
    public AbstractAsyncResponseListener(int responseType){  
        this.responseType = responseType;  
    }  
      
    public void onResponseReceived(HttpEntity response, String tag){  
        try {  
            switch(this.responseType){  
                case RESPONSE_TYPE_JSON_ARRAY:{  
                    String responseBody = EntityUtils.toString(response);     
                    //ShowDebugToast.Debug(false, TAG, "Response type json array :" + responseBody);
                    JSONArray json = null;  
                    if(responseBody!=null && responseBody.trim().length()>0){  
                        json = (JSONArray) new JSONTokener(responseBody).nextValue();  
                    }  
                    onSuccess(json,tag);  
                    break;  
                }  
                case RESPONSE_TYPE_JSON_OBJECT:{  
                    String responseBody = EntityUtils.toString(response);     
                    //ShowDebugToast.Debug(false, TAG, "Response type json object :" + responseBody);
                    //Log.d(TAG, "response json:"+responseBody);
                    JSONObject json = null;  
                    if(responseBody!=null && responseBody.trim().length()>0){ 
                    	try{
                    		json = (JSONObject) new JSONTokener(responseBody).nextValue(); 
                    	}catch(Exception e){
                    		onFailure(e, TAG);
                    		e.printStackTrace();
                    		Log.e(TAG, responseBody);
                    	}
                    }  
                    if(json != null)	
                    	onSuccess(json,tag);      
                    break;  
                }  
                case RESPONSE_TYPE_STREAM:{  
                    onSuccess(response.getContent(),tag);  
                    break;  
                }  
                default:{  
                    String responseBody = EntityUtils.toString(response);  
                    onSuccess(responseBody,tag);  
                }           
            }  
        } catch(IOException e) {  
            onFailure(e,tag);  
        } catch (JSONException e) {  
            onFailure(e,tag);  
        }     
    }  
      
    public void onResponseReceived(Throwable response, String tag){  
        onFailure(response,tag);  
    }  
      
    protected void onSuccess(JSONArray response, String tag){}  
      
    protected void onSuccess(JSONObject response, String tag){}  
      
    protected void onSuccess(InputStream response, String tag){}  
      
    protected void onSuccess(String response, String tag) {}  
  
    protected void onFailure(Throwable e, String tag) {}  
}
