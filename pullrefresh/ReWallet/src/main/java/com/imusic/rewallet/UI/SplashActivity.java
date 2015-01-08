package com.imusic.rewallet.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.model.LoginUserInfo;

public class SplashActivity extends Activity {

	private Handler _handler = new Handler(){
		public void handleMessage(Message msg) {
			
			autoLogin();
			
			
        }
	};
	private void autoLogin() {
		SharedPreferences sp = this.getSharedPreferences("session",Context.MODE_WORLD_READABLE);
		String token = sp.getString("token", null);
		if(token == null || token.length() == 0){
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, LoginActivity.class);
			intent.putExtra("istomain", true);
			//intent.setClass(getApplicationContext(), RewardChannelActivity.class);
			startActivity(intent);
			finish();
		}else {
			String nickName = sp.getString("nickName", "");
			LoginUserInfo.getInstance().setToken(token);
			LoginUserInfo.getInstance().setUserNickName(nickName);
			Intent mainIntent = new Intent();
			mainIntent.setClass(SplashActivity.this, MainActivity.class);
			startActivity(mainIntent);
			finish();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
//		FontManager.changeFonts(
//				(ViewGroup)((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0)
//				, this);
		doWork();
	}

	private void doWork(){
		Thread profileThread = new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					Thread.sleep(2000);
					_handler.sendMessage(_handler.obtainMessage());
				}catch(Throwable t){
					
				}
			}
		});
		profileThread.start();
	}
}
