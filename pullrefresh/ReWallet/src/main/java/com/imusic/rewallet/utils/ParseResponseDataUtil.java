package com.imusic.rewallet.utils;

import android.app.Activity;
import android.content.Intent;

import com.imusic.rewallet.UI.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseResponseDataUtil {

	public static JSONObject tryParseResult(Activity app, JSONObject response, int requestCode){
		JSONObject data = null;
		try {
			int result = response.getInt("result");
			if(result == 1){
				data = response.getJSONObject("data");
			}else if(result == 1507){  // expire date
				Intent intent = new Intent();
				intent.setClass(app, LoginActivity.class);
				app.startActivityForResult(intent, requestCode);
			}
		} catch (JSONException e) {
			showToast.getInstance(app).showMsg(e.getMessage());
			e.printStackTrace();
		}
		return data;
	}
	
}
