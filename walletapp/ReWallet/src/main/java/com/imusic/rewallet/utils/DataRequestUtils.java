package com.imusic.rewallet.utils;

import android.app.Activity;

import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;

import org.apache.http.HttpRequest;

public class DataRequestUtils {

	public static void sendAddComment(Activity app,int postId, 
			String comment,AbstractAsyncResponseListener l){
		String url = Constants.Url.getAddCommentUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey(), postId, comment);
		HttpRequest request = HttpRequestHelper.getGetRequest(url);
		sendGetData(app,request,l);
		
	}
	
	
	
	
	private static void sendGetData(Activity app
			, HttpRequest request
			,AbstractAsyncResponseListener l){
		AsyncHttpClient.sendRequest(app,request ,l, null);
	}
	
	
	
}
