package com.imusic.rewallet.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.widgets.LoginDialog;

import org.json.JSONObject;

public class RegisterActivity extends BaseNavibarActivity {

	private static String TAG = "RegisterActivity";
	private Button _btnOK;
	private WebView _webView;
	private LoginDialog _dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		_dialog = new LoginDialog(RegisterActivity.this, R.style.LoginTransparent);
		_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		_dialog.show();
		setupView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setupView(){
		setupActionBar(getString(R.string.registerActivity_title), true, false, null, null);
		_btnOK = (Button)findViewById(R.id.register_btnOK);
//		_btnOK.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(RegisterActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
		_btnOK.setVisibility(View.GONE);
		_webView = (WebView)findViewById(R.id.register_webView);
//		_webView.getSettings().setJavaScriptEnabled(true);
		_webView.setScrollBarStyle(0);
		WebSettings webSettings = _webView.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setJavaScriptEnabled(true);
//		webSettings.setBuiltInZoomControls(true);
		String registerUrl = Constants.Url.getRegisterUrl(GlobalDataInfo.getInstance().getLanguage());
		Vlog.getInstance().debug(false, TAG, registerUrl);
		_webView.loadUrl(registerUrl);
		_webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
//					MainActivity.this.setTitle("�������");
					if(_dialog != null && _dialog.isShowing())
						_dialog.dismiss();
				}else {
//					MainActivity.this.setTitle("������.......");
				}
			}
		});
	}
	
//	wv = (WebView) findViewById(R.id.webView1);
//	wv.getSettings().setJavaScriptEnabled(true);
//	wv.setScrollBarStyle(0);
//	WebSettings webSettings = wv.getSettings();
//	webSettings.setAllowFileAccess(true);
//	webSettings.setBuiltInZoomControls(true);
//	wv.loadUrl("http://www.baidu.com");
//	//��������
//	wv.setWebChromeClient(new WebChromeClient() {
//	@Override
//	public void onProgressChanged(WebView view, int newProgress) {
//	if (newProgress == 100) {
//	MainActivity.this.setTitle("�������");
//	} else {
//	MainActivity.this.setTitle("������.......");
//
//	}
//	}
//	});
	@Override
	protected void parseData(JSONObject response) {
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		
	}

	@Override
	protected void getData() {
		
	}

	@Override
	protected void onDestroy(){
		showDialog(false);
		_dialog = null;
		super.onDestroy();
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

}
