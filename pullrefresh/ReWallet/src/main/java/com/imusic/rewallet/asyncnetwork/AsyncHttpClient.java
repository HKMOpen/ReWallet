package com.imusic.rewallet.asyncnetwork;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.util.concurrent.ConcurrentHashMap;

public class AsyncHttpClient {
	private static DefaultHttpClient httpClient;  
    
    public static int CONNECTION_TIMEOUT = 8*1000;  
    public static int SOCKET_TIMEOUT  = 8*1000;  
      
    private static ConcurrentHashMap<Activity,AsyncHttpSender> tasks = new ConcurrentHashMap<Activity,AsyncHttpSender>();  

    public static void sendRequest(  
            final Activity currentActitity,  
            final HttpRequest request,  
            AsyncResponseListener callback,
            String tag) {  
          
        sendRequest(currentActitity, request, callback, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,tag);  
    }  
      
    public static void sendRequest(  
            final Activity currentActitity,  
            final HttpRequest request,  
            AsyncResponseListener callback,  
            int timeoutConnection,  
            int timeoutSocket,
            String tag) {  
          
        InputHolder input = new InputHolder(request, callback,tag, currentActitity.getApplicationContext());  
        AsyncHttpSender sender = new AsyncHttpSender();  
//        sender.execute(input);  
        sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        tasks.put(currentActitity, sender);
//        HttpSender sender = new HttpSender(currentActitity,input);
//        Thread thread = new Thread(sender);
//        thread.start();

    }  
//    public Activity getSenderActivity(AsyncHttpSender sender){
//    	tasks.
//    }
    public static void cancelRequest(final Context currentActitity){  
        if(tasks==null || tasks.size()==0) return;  
        for (Context key : tasks.keySet()) {  
            if(currentActitity == key){  
                AsyncTask<?,?,?> task = tasks.get(key);  
                if(task.getStatus()!=null && task.getStatus()!=AsyncTask.Status.FINISHED){  
                    //Log.i(TAG, "AsyncTask of " + task + " cancelled.");  
                    task.cancel(true);  
                }  
                tasks.remove(key);  
            }  
        }  
    }  
   
    public static synchronized HttpClient getClient() {  
        if (httpClient == null){              
            //use following code to solve Adapter is detached error  
            //refer: http://stackoverflow.com/questions/5317882/android-handling-back-button-during-asynctask  
            BasicHttpParams params = new BasicHttpParams();  
//            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            SchemeRegistry schemeRegistry = new SchemeRegistry();  
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
            final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();  
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));  
              
            // Set the timeout in milliseconds until a connection is established.  
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);  
            // Set the default socket timeout (SO_TIMEOUT)   
            // in milliseconds which is the timeout for waiting for data.  
            HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);  
              
            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);  
            httpClient = new DefaultHttpClient(cm, params);   
        }  
        return httpClient;  
    }  
}
