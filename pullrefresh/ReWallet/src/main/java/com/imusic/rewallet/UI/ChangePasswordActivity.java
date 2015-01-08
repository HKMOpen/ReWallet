package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.imusic.rewallet.widgets.TextboxForProfileLarge;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "ChangePasswordActivity";
	
	private static final int REQUESTCODE = 30001;
	
	private TextboxForProfileLarge _edOldPassword;
	private TextboxForProfileLarge _edNewPassword;
	private TextboxForProfileLarge _edConfirmNewPassword;
	private Button _btnOK;
	private LoginDialog _dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		setupView();
	}

	private void setupView(){
		setupActionBar(getString(R.string.changePasswordActivity_title), 
				true, false, null, null);
		_edOldPassword = (TextboxForProfileLarge)findViewById(R.id.changePassword_edOldPassword);
		_edNewPassword = (TextboxForProfileLarge)findViewById(R.id.changePassword_edNewPassword);
		_edConfirmNewPassword = (TextboxForProfileLarge)findViewById(R.id.changePassword_edConfirmNewPassword);
		_edOldPassword.setContentEnable("", true);
		_edNewPassword.setContentEnable("", true);
		_edConfirmNewPassword.setContentEnable("", true);
		_edOldPassword.setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		_edNewPassword.setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		_edConfirmNewPassword.setEditInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		_btnOK= (Button)findViewById(R.id.changePassword_btnOK);
		_btnOK.setOnClickListener(this);
		
	}
	
	
	
	@Override
	protected void parseData(JSONObject response) {
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					showToast.getInstance(getApplicationContext()).showMsg(getString(R.string.changePasswordActivity_changePasswordSucceed));
					showDialog(false);
					finish();
				}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
					Intent intent = new Intent();
					intent.setClass(this, LoginActivity.class);
					startActivityForResult(intent, REQUESTCODE);
				}else{
					showDialog(false);
					showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				showDialog(false);
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getData() {
		String op = _edOldPassword.getEditContent().trim();
		String np = _edNewPassword.getEditContent().trim();
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url.getChangePasswordUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey(),op,np));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "ChangePassword resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "ChangePassword response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"changepassword");
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.changePassword_btnOK){
			if(confirmPassword()){
				showDialog(true);
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
		}
	}
	private boolean confirmPassword(){
		String oP = _edOldPassword.getEditContent().trim();
		String nP = _edNewPassword.getEditContent().trim();
		String cnP = _edConfirmNewPassword.getEditContent().trim();
		if(oP == null || oP.length() <= 0){
			showToast.getInstance(getApplicationContext())
			.showMsg(getString(R.string.changePasswordActivity_NoOldPassword));
			return false;
		}
		if(nP == null || nP.length() <= 0){
			showToast.getInstance(getApplicationContext())
			.showMsg(getString(R.string.changePasswordActivity_NoNewPassword));
			return false;
		}
		if(cnP == null || cnP.length() <= 0){
			showToast.getInstance(getApplicationContext())
			.showMsg(getString(R.string.changePasswordActivity_NoConfirmPassword));
			return false;
		}
		if(!nP.equals(cnP)){
			showToast.getInstance(getApplicationContext())
			.showMsg(getString(R.string.changePasswordActivity_passwordNoSome));
			return false;
		}
		
		return true;
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
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}
}
