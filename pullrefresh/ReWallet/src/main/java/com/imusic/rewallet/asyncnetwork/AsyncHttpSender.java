package com.imusic.rewallet.asyncnetwork;

import android.os.AsyncTask;

import com.imusic.rewallet.R;
import com.imusic.rewallet.utils.Vlog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.IllegalFormatException;
 
public class AsyncHttpSender extends AsyncTask<InputHolder, Void, OutputHolder>{
	
	private static final String  TAG = "AsyncHttpSender";
	
	@Override  
    protected OutputHolder doInBackground(InputHolder... params) {  
        HttpEntity entity = null;  
        InputHolder input = params[0];  
        try {
        	Vlog.getInstance().info(false, TAG, "request url:"+  input.getRequest().getRequestLine());
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
            //ShowDebugToast.Debug(false, TAG, e.getMessage());
            return new OutputHolder(e, input.getResponseListener(),input.getTag());  
        }
        catch (SocketTimeoutException e) {
        	return new OutputHolder(new Exception(input.getContext().getResources().getString(R.string.global_connectFail)), input.getResponseListener(),input.getTag());  
		}
        catch (IOException e) {  
           // ShowDebugToast.Debug(false, TAG, e.getMessage());
            return new OutputHolder(e, input.getResponseListener(),input.getTag());  
        }
        catch (IllegalFormatException e) {//global_stringForat_getData_Error
        	return new OutputHolder(new Exception(input.getContext().getResources().getString(R.string.global_stringForat_getData_Error)), input.getResponseListener(),input.getTag());  
		}
        return new OutputHolder(entity, input.getResponseListener(),input.getTag());  
    }  
      
    @Override  
    protected void onPreExecute(){  
        //ShowDebugToast.Debug(false, TAG, "onPreExecute()");
    	super.onPreExecute();  
    }  
      
    @Override  
    protected void onPostExecute(OutputHolder result) {  
        //ShowDebugToast.Debug(false, TAG, "onPostExecute()");
    	super.onPostExecute(result);  
          
        if(isCancelled()){  
            //ShowDebugToast.Debug(false, TAG, "AsyncHttpSender.onPostExecute(): isCancelled() is true");
        	return; //Canceled, do nothing  
        }  
          
        AsyncResponseListener listener = result.getResponseListener();  
        HttpEntity response = result.getResponse();  
        Throwable exception = result.getException();  
        if(response!=null){  
            //ShowDebugToast.Debug(false, TAG, "listener.onResponseReceived(response)");
        	try{
        		listener.onResponseReceived(response, result.getTag());  
        	} catch(Exception e){ 
        		listener.onResponseReceived(e, result.getTag());  
        	}
        }else{ 
        	//ShowDebugToast.Debug(false, TAG, "listener.onResponseReceived(exception)");
        	
        	listener.onResponseReceived(exception, result.getTag());  
        }  
    }  
      
    @Override  
    protected void onCancelled(){  
    	//ShowDebugToast.Debug(false, TAG, "onCancelled()");
    	super.onCancelled();  
        //this.isCancelled = true;  
    }  
}
