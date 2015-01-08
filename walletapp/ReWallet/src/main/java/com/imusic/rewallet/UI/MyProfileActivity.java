package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.DataProcessCover;
import com.imusic.rewallet.widgets.TextboxForProfileLarge;
import com.imusic.rewallet.widgets.TextboxForProfileSmall;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class MyProfileActivity extends BaseNavibarActivity {

	
	private final static String TAG = "MyProfileActivity";
	
	private static final int GET_CODE = 0;
	private static final int REQUESTCODE = 10621;
	
	private TextboxForProfileSmall _edLocation;
	private TextboxForProfileSmall _edGender;
	private TextboxForProfileSmall _edBirthday;
	private TextboxForProfileLarge _edNickName;
	private TextboxForProfileLarge _edFirstName;
	private TextboxForProfileLarge _edLastName;
	private RelativeLayout _layout;
	private DataProcessCover _cover;
	private Button _btnSetting;
	private Button _btnSignOut;
	private ImageView _imgPic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		//_layout = new LinearLayout(this);
		//setContentView(_layout);
		_cover = new DataProcessCover(getApplicationContext(), null);
		//addContentView(_cover, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		//_layout.addView(_cover);
		setupView();
//		getData();
		_handler.obtainMessage(MSG_GETDATA).sendToTarget();
	}

	private void setupActionbar(){
		setupActionBar(getString(R.string.myProfileActivity_title), true, 
				false//true
				,getString(R.string.myProfileActivity_edit),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), EditProfileActivity.class);
						startActivityForResult(intent, GET_CODE);
					}
				});
		setRightButtonEnable(false);
	}
	
	private void setupView(){
		setupActionbar();
		_layout = (RelativeLayout)findViewById(R.id.myProfile_layout);
		//_layout.addView(_cover);
		_edLocation = (TextboxForProfileSmall)findViewById(R.id.myProfile_edLocation);
		_edLocation.setContentEnable("", false);
		_edGender = (TextboxForProfileSmall)findViewById(R.id.myProfile_edGender);
		_edGender.setContentEnable("", false);
		_edBirthday = (TextboxForProfileSmall)findViewById(R.id.myProfile_edBirthday);
		_edBirthday.setContentEnable("", false);
		_edNickName = (TextboxForProfileLarge)findViewById(R.id.myProfile_edNickName);
		_edNickName.setContentEnable("", false);
		_edFirstName = (TextboxForProfileLarge)findViewById(R.id.myProfile_edFirstName);
		_edFirstName.setContentEnable("", false);
		_edLastName = (TextboxForProfileLarge)findViewById(R.id.myProfile_edLastName);
		_edLastName.setContentEnable("", false);
		_btnSignOut = (Button)findViewById(R.id.myProfile_btn_signOut);
		_btnSignOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginUserInfo.getInstance().setTokenExpiry();
				finish();
			}
		});
		_btnSetting = (Button)findViewById(R.id.myProfile_btn_setting);
		_btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SettingActivity.class);
				startActivity(intent);
			}
		});
//		myProfile_imgPic 
		_imgPic = (ImageView)findViewById(R.id.myProfile_imgPic);
		ImageLoader.getInstance()
			.displayImage(LoginUserInfo
					.getInstance().getLoginUserInfo().profile_picture
					, _imgPic, Util.getImageLoaderOptionForUserProfile(getApplicationContext()));
	}
	
	
	// ------------------------------------------------------------------
	
	@Override
	protected void parseData(JSONObject response) {
		boolean isSuccess = false;
		int resultCode;
		try {
			resultCode = response.getInt("result");
			if(resultCode == 1){
				isSuccess = true;
				LoginUserInfo.getInstance().setDisplayUserInfo( response);
			}else{
				showToast.getInstance(getApplicationContext())
				.showMsg(getString(R.string.global_stringForat_getData_Error) 
						+ response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		refreshUI(isSuccess);
	}
	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			_edLocation.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().countryName, false);
			_edGender.setContentEnable(
					LoginUserInfo.getInstance()
					.getLoginUserInfo().gender
					.equals("M")? getString(R.string.global_Gender_Male): getString(R.string.global_Gender_Female)
							, false);
			if(LoginUserInfo.getInstance().getLoginUserInfo().birthday != null)
				_edBirthday.setContentEnable(new SimpleDateFormat("yyyy-MM-dd").format(LoginUserInfo.getInstance().getLoginUserInfo().birthday), false);
			else
				_edBirthday.setContentEnable(null, false);
			_edNickName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().nickName, false);
			_edFirstName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().firstName, false);
			_edLastName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().lastName, false);
			//setRightButtonEnable(true);
			ImageLoader.getInstance()
			.displayImage(LoginUserInfo
					.getInstance().getLoginUserInfo().profile_picture
					, _imgPic, Util.getImageLoaderOptionForUserProfile(getApplicationContext()));
		}
		_layout.removeView(_cover);
		setRightButtonEnable(true);
	}
	@Override
	protected void getData() {
		_layout.addView(_cover);
		Log.i(TAG, "request: " + Constants.Url.getProfileDisplayUrl(LoginUserInfo.getInstance().getToken(), Constants.GlobalKey.getAppKey()));
    	HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getProfileDisplayUrl(LoginUserInfo.getInstance().getToken(),Constants.GlobalKey.getAppKey()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "display user data resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "display user data response json failed." + e.toString());
				
			}
		},"");
	}
	@Override   
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == GET_CODE)
	    { 
			if(resultCode == RESULT_OK){
//				getData();
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
	    }
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}
}
