package com.imusic.rewallet.UI;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.widgets.TextboxForProfileLarge;

import org.json.JSONObject;

public class SettingActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "SettingActivity";
	
	private TextboxForProfileLarge _edPassword;
	private TextboxForProfileLarge _edLanuage;
//	private TextboxForProfileLarge _edEmail;
	private Button _btnToGain;
	private Button _btnToUse;
	private Button _btnFeedback;
	private Button _btnGetSupport;
	private Button _btnTC;
	private Button _btnPrivacyPolicy;
	private Button _btnAbout;
	private Button _btnOK;
	private TextView _tvVersion;
	private ToggleButton _tgTwitter;
	private TextView _tvTwitter;
	private ToggleButton _tgPushNotification;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setupView();
	}

	private void setupView(){
		setupActionBar(getString(R.string.settingActivity_title), true, false, null, null);
		_btnToGain = (Button)findViewById(R.id.setting_btnToGain);
		_btnToGain.setOnClickListener(this);
		_btnToUse = (Button)findViewById(R.id.setting_btnToUse);
		_btnToUse.setOnClickListener(this);
		_btnFeedback = (Button)findViewById(R.id.setting_btnFeedback);
		_btnFeedback.setOnClickListener(this);
		_btnGetSupport = (Button)findViewById(R.id.setting_btnGetSupport);
		_btnGetSupport.setOnClickListener(this);
		_btnTC = (Button)findViewById(R.id.setting_btnTC);
		_btnTC.setOnClickListener(this);
		_btnPrivacyPolicy = (Button)findViewById(R.id.setting_btnPrivacy);
		_btnPrivacyPolicy.setOnClickListener(this);
		_btnAbout = (Button)findViewById(R.id.setting_btnAbout);
		_btnAbout.setOnClickListener(this);
		_btnOK= (Button)findViewById(R.id.setting_btnOK);
		_btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		_edEmail = (TextboxForProfileLarge)findViewById(R.id.setting_edEmail);
//		_edEmail.setArrowShow(false);
//		_edEmail.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().email, false);
		_edPassword = (TextboxForProfileLarge)findViewById(R.id.setting_edPassword);
		_edPassword.setContentEnable(getString(R.string.settingActivity_changePassword), false);
//		_edPassword.resetArrow(getResources().getDrawable(R.drawable.edit_pen));
		_edPassword.setArrowShow(true);
		_edPassword.setOnCustomClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(LoginUserInfo.getInstance().getToken() == null){
					Intent itLogin = new Intent();
					itLogin.setClass(SettingActivity.this, LoginActivity.class);
					startActivity(itLogin);
				}else {
					Intent itPassword = new Intent();
					itPassword.setClass(SettingActivity.this, ChangePasswordActivity.class);
					startActivity(itPassword);
				}
			}
		});
		_edLanuage = (TextboxForProfileLarge)findViewById(R.id.setting_edlanguage);
		_edLanuage.setContentEnable(getString(R.string.settingActivity_language), false);
		_edLanuage.setArrowShow(true);
//		_edLanuage.setOnCustomClickListener(l)
		_tgPushNotification = (ToggleButton)findViewById(R.id.setting_tgbtnPushNotification);
		_tgPushNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				onPushNotificationChanged(isChecked);
			}
		});
		_tvVersion = (TextView)findViewById(R.id.setting_tvVersion);
		String appVersion = null;
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			appVersion = info.versionName; // �汾��
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		_tvVersion.setText(String.format(getString(R.string.settingActivity_softwardVersion), 
				appVersion));
	}
	private void onPushNotificationChanged(Boolean isOpen){
		
	}
	@Override
	protected void parseData(JSONObject response) {
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_btnToGain:
			Intent insIntent= new Intent();
			insIntent.setClass(this, InstructionsActivity.class);
			insIntent.putExtra("index", 0);
			startActivity(insIntent);
			break;
		case R.id.setting_btnToUse:
//			Intent insIntent= new Intent();
//			insIntent.setClass(this, InstructionsActivity.class);
//			int index = 0;
//			if(v.getId() == R.id.setting_btnToUse){
//				index = 1;
//			}
//			insIntent.putExtra("index", index);
//			startActivity(insIntent);
			Intent itUse= new Intent();
			itUse.setClass(this, InstructionsActivity.class);
			itUse.putExtra("index", 1);
			startActivity(itUse);
			break;
		case R.id.setting_btnAbout:
			Intent itAbout = new Intent();
			itAbout.setClass(this, AboutActivity.class);
			startActivity(itAbout);
			break;
		case R.id.setting_btnFeedback:
			openFeedbackUrl();
			break;
		case R.id.setting_btnGetSupport:
			openSupport();
			break;
		case R.id.setting_btnTC:
			openTC();
			break;
		case R.id.setting_btnPrivacy:
			openPrivaryPolicy();
			break;
		default:
			break;
		}
		
	}
	
	private void openFeedbackUrl(){
		String target = Constants.Url.getFeedbackUrl(GlobalDataInfo.getInstance().getLanguage());
//		Uri uri =  Uri.parse(target);
		
		openUri(target, getString(R.string.settingActivity_Feedback));
	}
	private void openUri(String uri, String title){
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, AboutWebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", uri);
		startActivity(intent);
	}
	private void openPrivaryPolicy(){
		String target = Constants.Url.getPrivacyPolicyUrl(GlobalDataInfo.getInstance().getLanguage());
//		Uri uri =  Uri.parse(target);
		openUri(target, getString(R.string.settingActivity_privacyPolicy));
	}
	private void openSupport(){
		String target = Constants.Url.getSupportUrl(GlobalDataInfo.getInstance().getLanguage());
//		Uri uri =  Uri.parse(target);
		openUri(target, getString(R.string.settingActivity_getSupport));
	}
	private void openTC(){
		String target = Constants.Url.getTCUrl(GlobalDataInfo.getInstance().getLanguage());
//		Uri uri =  Uri.parse(target);
		openUri(target, getString(R.string.settingActivity_termCondition));
	}
	
	

}
