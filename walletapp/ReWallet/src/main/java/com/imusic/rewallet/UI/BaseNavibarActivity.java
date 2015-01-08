package com.imusic.rewallet.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.UserInfo;
import com.imusic.rewallet.widgets.DataProcessCover;

import org.json.JSONObject;

public abstract class BaseNavibarActivity extends Activity {

	protected ImageButton _imgbtnNaviBack;
	protected TextView _txtNavTitle;
	protected Button _btnRight;
	protected DataProcessCover _cover;
	
	//protected final static int MSG_REFREASHVIEW = 1000;
	
	protected void setRightButtonEnable(boolean isEnable){
		if(_btnRight != null && _btnRight.getVisibility()==View.VISIBLE){
			_btnRight.setEnabled(isEnable);
		}
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			LoginUserInfo.getInstance().setToken(savedInstanceState.getString("token"));
			LoginUserInfo.getInstance().setLoginUserInfo((UserInfo)savedInstanceState.get("loginuserdata"));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (LoginUserInfo.getInstance().getLoginUserInfo() != null) {
			outState.putSerializable("loginuserdata", LoginUserInfo.getInstance().getLoginUserInfo());
			outState.putString("token", LoginUserInfo.getInstance().getToken());
		}
		super.onSaveInstanceState(outState);
	}
	protected void setupActionBar(String titleName, boolean isBack, 
			boolean isRightBtn, String rightBtnName, View.OnClickListener l){
		ActionBar bar = getActionBar();
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled( false );
		bar.setCustomView(R.layout.view_actionbar_base);

		_txtNavTitle = (TextView) findViewById(R.id.navigator_tvTitle);
		if(_txtNavTitle != null)
			_txtNavTitle.setText(titleName);
		_imgbtnNaviBack = (ImageButton) findViewById(R.id.navigator_imgbtnBack);
		if(_imgbtnNaviBack != null){
			if(isBack){
				_imgbtnNaviBack.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
			}else{
				_imgbtnNaviBack.setVisibility(View.GONE);
			}
		}
		_btnRight = (Button)findViewById(R.id.navigator_btnRight);
		if(isRightBtn){
			_btnRight.setVisibility(View.VISIBLE);
			_btnRight.setText(rightBtnName);
			_btnRight.setOnClickListener(l);
		}else
			_btnRight.setVisibility(View.GONE);
			
		
//		FontManager.changeFont(_txtNavTitle, this);
//		FontManager.changeFont(_btnRight, this);
	}
	
	protected abstract void parseData(JSONObject response);
	protected abstract void refreshUI(boolean isSuccess);
	protected abstract void getData();
	
//	protected abstract void refreshUI();
	protected final static int MSG_REFREASHVIEW = 1000;
	protected final static int MSG_GETDATA = 1001;
	protected Handler _handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFREASHVIEW:
				refreshUI(true);
				break;
			case MSG_GETDATA:
				getData();
				break;
			}
		};
	};
//	
	
	
	
}
