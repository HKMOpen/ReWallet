package com.imusic.rewallet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.imusic.rewallet.UI.LoginActivity;
import com.imusic.rewallet.UI.MyVcoinActivity;
import com.imusic.rewallet.model.LoginUserInfo;

public class OverlayBtnUtil {

	public static void myProfileClick(Activity app, int requestCode){
		Intent inProfile = new Intent();
		if(LoginUserInfo.getInstance().getToken() == null ||
				LoginUserInfo.getInstance().getToken().trim().length() == 0){
			inProfile.setClass(app, LoginActivity.class);
		}else{
			inProfile.setClass(app, MyVcoinActivity.class);//MyProfileActivity.class);
		}
//		inProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(inProfile);
		app.startActivityForResult(inProfile, requestCode);
	}
	public static void myVcoinClick(Context context){
		Intent intent = new Intent();
//		intent.setClass(context, MyVocinActivity.class);
//		context.startActivity(intent);
		if(LoginUserInfo.getInstance().getToken() == null ||
				LoginUserInfo.getInstance().getToken().trim().length() == 0){
			intent.setClass(context, LoginActivity.class);
		}else{
			intent.setClass(context, MyVcoinActivity.class);
		}
		context.startActivity(intent);
	}
	
}
