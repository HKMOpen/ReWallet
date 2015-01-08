package com.imusic.rewallet.utils;

import android.app.Activity;
import android.content.Intent;

import com.imusic.rewallet.UI.CommentActivity;
import com.imusic.rewallet.UI.LoginActivity;
import com.imusic.rewallet.UI.ShareActivity;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RewardCommentItem;

import java.util.ArrayList;

public class ShareCommentBtnUtil {

	public static final int TYPE_REWARD = 0;
	public static final int TYPE_APP = 1;
	
	public static void onShare(Activity app, int type, String title, String img, int id, int resultCode){
		if(type != TYPE_APP && type != TYPE_REWARD)
			return;
		if(!isLogined(app))
			return;
		Intent intent = new Intent();
		intent.setClass(app, ShareActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("title", title);
		intent.putExtra("img", img);
		intent.putExtra("id", id);
		app.startActivityForResult(intent, resultCode);
	}
	public static void onComment(Activity app, int postid, int type, String title, String img
			,ArrayList<RewardCommentItem> lst, int resultCode){
		if(type != TYPE_APP && type != TYPE_REWARD)
			return;
		if(!isLogined(app))
			return;
		Intent intent = new Intent();
		intent.setClass(app, CommentActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("title", title);
		intent.putExtra("img", img);
		intent.putExtra("list", lst);
		intent.putExtra("postid", postid);
//		context.startActivity(intent);
		app.startActivityForResult(intent, resultCode);
	}
	
	private static boolean isLogined(Activity app){
		if( LoginUserInfo.getInstance().getToken() == null
				 ){
			Intent intent = new Intent();
			intent.setClass(app, LoginActivity.class);
			app.startActivity(intent);
			return false;
		}
		return true;
	}
	
}
