package com.imusic.rewallet.UI;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.id;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.widgets.LoginDialog;

import org.json.JSONObject;

public class AboutWebViewActivity extends BaseNavibarActivity {

	public static final String TAG = "AboutWebViewActivity";
	private WebView _webView;
	private String _url;
	private String _title;
	private LoginDialog _dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_web_view);
		if(getIntent().getExtras() != null){
			_url = getIntent().getExtras().getString("url");
			Vlog.getInstance().debug(false, TAG, "url : "+ _url);
			if(_url == null || _url.length() <=0 ){
				finish();
			}else {
				_title = getIntent().getExtras().getString("title");
				setupView();
			}
		}else {
			finish();
		}
	}
	private void setupView() {
		setupActionBar(_title, true, false, null, null);
		showDialog(true);
		_webView = (WebView)findViewById(R.id.AboutWeb_webview);
		_webView.getSettings().setJavaScriptEnabled(true);
		_webView.setScrollBarStyle(0);
		WebSettings webSettings = _webView.getSettings();
		webSettings.setAllowFileAccess(true);
//		webSettings.setBuiltInZoomControls(true);
		_webView.loadUrl(_url);
		_webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					if(_dialog != null && _dialog.isShowing())
						_dialog.dismiss();
				}else {
				}
			}
		});
	}

	@Override
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
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
