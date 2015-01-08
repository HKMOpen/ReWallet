package com.imusic.rewallet.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.KeyBoardUtil;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

//import com.imusic.vcoinsdk.apis.ErrorData;
//import com.imusic.vcoinsdk.apis.ResultData;
//import com.imusic.vcoinsdk.apis.VCoin;
//import com.imusic.vcoinsdk.apis.VCoinLoginResponseListener;
//import com.imusic.vcoinsdk.apis.VCoinMoveCoinResponseListener;

public class LoginActivity extends BaseNavibarActivity {

	private static final String TAG = "LoginActivity";
	
	private EditText _tvUserId;
	private EditText _tvUserPassword;
	private Button _btnSignIn;
//	private Button _btnSignUp;
//	private ImageView _imgUserIcon;
	private LoginDialog _dialog;
	private TextView _tvCreateAccount;
	RelativeLayout _layout;
	private Boolean _isToMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (getIntent().getExtras() != null) {
			_isToMain = getIntent().getExtras().getBoolean("istomain");
		}else {
			_isToMain = false;
		}
		
		
		setupView();
//		if(LoginUserInfo.getInstance().getLoginUserInfo() != null)
//			setModeRelogin();
//		else
//			setModeNewLogin();
	}
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
//		if(LoginUserInfo.getInstance().getLoginUserInfo() != null)
//			setModeRelogin();
//		else
//			setModeNewLogin();
	}
	
	private void setModeNewLogin(){
//		_tvUserId.setEditContent(null);
//		_tvUserId.setHint(getString(R.string.loginActivity_hint_userid));
//		Drawable cup = getResources().getDrawable(R.drawable.login_signup);
//		cup.setBounds(0, 0, cup.getMinimumWidth(), cup.getMinimumHeight());
//		_btnSignUp.setCompoundDrawables(
//				null, cup, null, null);
//		_btnSignUp.setText(getString(R.string.loginActivity_signUp));
//		_btnSignUp.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//RegisterActivity
//				showDialog(false);
//				LoginUserInfo.getInstance().cleanInfo();
//				Intent in = new Intent();
//				in.setClass(LoginActivity.this, RegisterActivity.class);
//				startActivity(in);
//			}
//		});
	}
	private void setModeRelogin(){
//		ImageLoader.getInstance().displayImage(LoginUserInfo.getInstance().getLoginUserInfo().profile_picture
//				, _imgUserIcon, Util.getImageLoaderOptionForUserProfile(getApplicationContext()));
//		_tvUserId.setEditContent(LoginUserInfo.getInstance().getLoginUserInfo().email);
//		Drawable cup = getResources().getDrawable(R.drawable.login_notyouraccount);
//		cup.setBounds(0, 0, cup.getMinimumWidth(), cup.getMinimumHeight());
//		_btnSignUp.setCompoundDrawables(
//				null, cup, null, null);
//		_btnSignUp.setText(getString(R.string.loginActivity_notYourAC));
//		_btnSignUp.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoginUserInfo.getInstance().cleanInfo();
//				setModeNewLogin();
//			}
//		});
	}
	
	private void setupView(){
		setupActionBar(getString(R.string.loginActivity_title), !_isToMain, 
				false,null,null);
//		VCoin.init("2YVw8u9o", "93ZaX34iE4l3yskR");
//		VCoin.getInstance(this).login("zhenxiong", "password");
		//tryLogin("zhenxiong","password");
		
		
		RelativeLayout bg = (RelativeLayout)findViewById(R.id.login_layout_bg);
		bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//KeyBoardUtil.showKeyboard(getApplicationContext(), false);
				KeyBoardUtil.hideKeyboardFocus(getApplicationContext(), getCurrentFocus());
			}
		});
//		FontManager.changeFonts((ViewGroup)((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0), this);

		
		//		_imgUserIcon = (ImageView)findViewById(R.id.login_imgUserIcon);
//		if(LoginUserInfo.getInstance().getLoginUserInfo() != null){
//			ImageLoader.getInstance().displayImage(LoginUserInfo.getInstance().getLoginUserInfo().profile_picture
//					, _imgUserIcon, Util.getImageLoaderOptionForUserProfile());
//		}
		
		_tvUserId = (EditText)findViewById(R.id.login_tvUserId);
//		if(LoginUserInfo.getInstance().getLoginUserInfo() != null){
//			_tvUserId.setEditContent(LoginUserInfo.getInstance().getLoginUserInfo().email);
//		}else
//			_tvUserId.setHint(getString(R.string.loginActivity_hint_userid));
		
		_tvUserPassword = (EditText)findViewById(R.id.login_tvPassword);
//		_tvUserPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		_tvUserPassword.setHint(getString(R.string.loginActivity_hint_userpassword));
//		_tvUserPassword.setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		_btnSignIn = (Button)findViewById(R.id.login_btnSignIn);
		_btnSignIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(_tvUserId.getText() == null || _tvUserId.getText().toString().length() == 0){
					showToast.getInstance(getApplicationContext()).showMsg(getString(R.string.loginActivity_textEmpty_userId));
					return;
				}
				if(_tvUserPassword.getText() == null || _tvUserPassword.getText().toString().trim().length() == 0){
					showToast.getInstance(getApplicationContext()).showMsg(getString(R.string.loginActivity_textEmpty_userPassword));
					return;
				}
				
				showDialog(true);

				tryLogin(_tvUserId.getText().toString().trim(),_tvUserPassword.getText().toString().trim());
			}
		});

		_tvCreateAccount = (TextView)findViewById(R.id.login_tvCreateAccount);
		_tvCreateAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent();
				in.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(in);
			}
		});
		
		
		
		
	}
	
//	private void testing(){
//		VCoin.init("2YVw8u9o", "93ZaX34iE4l3yskR");
//		
//		VCoin.getInstance(this).setLoginResponseListener(new VCoinLoginResponseListener() {
//			@Override
//			public void onLoginError(ErrorData data) {
//			}
//			
//			@Override
//			public void onLoginCompleted(ResultData data) {
//				Log.i(TAG, "login -- >result:" + data.getReturnValueCode() + "  msg:"+data.getReturnMessage());
//				if(data.getReturnValueCode() == 1) 
//					testingMoveCoins();
//			}
//		});
//		VCoin.getInstance(this).login("zhenxiong", "password");
//	}
//	private void testingMoveCoins(){
//		VCoin.getInstance(this).setMoveCoinResponseListener(new VCoinMoveCoinResponseListener() {
//			
//			@Override
//			public void onMoveCoinError(ErrorData data) {
//				
//			}
//			
//			@Override
//			public void onMoveCoinCompleted(ResultData data) {
//				Log.i(TAG, "move coins -- > result:" + data.getReturnValueCode() + "   msg:"+data.getReturnMessage());
//			}
//		});
//		VCoin.getInstance(this).moveCoin(50000000);
//	}
	
	private void tryLogin(final String username, final String password){
		Log.i(TAG, Constants.Url.getNonceUrl());
		HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getNonceUrl());
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Log.i(TAG, "resposnse json succeed : "+response.toString());
				try {
					if(response.get("status").equals("ok")){
						login(response.get("nonce").toString(),username,password);
					}else{
						String err = response.getString("error");
						showToast.getInstance(getApplicationContext()).showMsg(err);
					}
				} catch (JSONException e) {
					showDialog(false);
					showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
					e.printStackTrace();
				}
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				Log.i(TAG, "response json failed." + e.toString());
				showDialog(false);
			}
		},"");
	}	
	private void login(final String nonce, String username, String password){
		Log.i(TAG, Constants.Url.getLoginUrl(nonce, username, password, Constants.GlobalKey.getAppKey(), Constants.GlobalKey.getAppSecret()));
		HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getLoginUrl(nonce, username, password, Constants.GlobalKey.getAppKey(), Constants.GlobalKey.getAppSecret()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Log.i(TAG, "resposnse json succeed : "+response.toString());
				try {
					if(response.get("status").equals("ok")){
						//LoginUserInfo.getInstance().setLoginInfo(nonce,response);
						LoginUserInfo.getInstance().setToken(nonce, response);
						LoginUserInfo.getInstance().setDraftUserInfo(response);
						showToast.getInstance(getApplicationContext()).showMsg(getString(R.string.loginActivity_Login_Successful));
//						Intent intent = new Intent();
//						intent.setClass(getApplicationContext(), MainActivity.class);
//						startActivity(intent);
						saveUserLoginToken(LoginUserInfo.getInstance().getToken()
								,LoginUserInfo.getInstance().getLoginUserInfo().nickName);
						
						showDialog(false);
						
						if (_isToMain) {
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							startActivity(intent);
						}else {
							setResult(RESULT_OK);
						}
						finish();
					}else{
						String err = response.getString("error");
						showToast.getInstance(getApplicationContext()).showMsg(err);
						showDialog(false);
					}
				} catch (JSONException e) {
					showDialog(false);
					showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
					e.printStackTrace();
				}
				
				
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				Log.i(TAG, "response json failed." + e.toString());
				showDialog(false);
			}
		},"");
	}
	private void  saveUserLoginToken(String token,String nickName) {
		SharedPreferences sp = this.getSharedPreferences("session",Context.MODE_WORLD_READABLE);
		SharedPreferences.Editor  editor = sp.edit();
		editor.putString("token", token);
		editor.putString("nickName", nickName);
		
		editor.commit();
	}
	@Override
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void refreshUI(boolean isSuccee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}
	
	private void showDialog(boolean isShow){
		if(isShow){
			if(_dialog != null){
				if(!_dialog.isShowing())
						_dialog.show();
			}else{
				_dialog = new LoginDialog(this, R.style.LoginTransparent);
				_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				_dialog.show();
			}
		}else{
			if(_dialog != null && _dialog.isShowing())
				_dialog.dismiss();
		}
	}
	@Override
	protected void onDestroy(){
		
		showDialog(false);
		_dialog = null;
		super.onDestroy();
	}
	
}
