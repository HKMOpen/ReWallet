package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class MyRewardCaimProcessWithNoteActivity extends BaseNavibarActivity {

	
	private final static String TAG = "MyRewardCaimProcessWithNoteActivity";
	
	private static final int REQUESTCODE = 40321;
	
	private ImageView _imgTop;
	private Button _btnOK;
	private LoginDialog _dialog;
	private LinearLayout _layoutContent;
	private EditText _edNote;
	
	private String _title;
	private String _imageUrl;
	private String _qrCode;
	private int _procedure =0;
	private String _note = "";
	private String _errorMsg;
	private String _trace_id;
	
	public static MyRewardCaimProcessWithNoteActivity Instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reward_caim_process_with_note);
		if(getIntent().getExtras() == null)
			finish();
		else{
			_title = getIntent().getExtras().getString("title");
			_imageUrl = getIntent().getExtras().getString("img");
			_qrCode = getIntent().getExtras().getString("qrcode");
			_procedure = getIntent().getExtras().getInt("procedure");
			setupView();
			Instance = this;
		}
		
	}

	private void setupView(){
		setupActionBar(_title, true, false, null, null);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		_imgTop = (ImageView)findViewById(R.id.myRewardClaimProcess_imgTop);
		_imgTop.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*9/16));
		ImageLoader.getInstance().displayImage(_imageUrl, _imgTop, Util.getImageLoaderOptionForDetail(getApplicationContext()));
		_btnOK = (Button)findViewById(R.id.myRewardClaimProcess_btnOK);
		_btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				claimReward();
			}
		});
		_layoutContent = (LinearLayout)findViewById(R.id.myRewardClaimProcess_layout_content);
		_edNote = (EditText)findViewById(R.id.myRewardClaimProcess_edNote);
//		_edNote.setFocusable(false);
	}
	private void claimReward(){
		_note = _edNote.getText().toString().trim();
		_handler.obtainMessage(MSG_GETDATA).sendToTarget();
	}
	@Override
	protected void parseData(JSONObject response) {
		boolean isSuccess = false;
		try {
			int resultCode = response.getInt("result");
			if(resultCode == ResponseResultCode.RESULT_EXPIREDATE){
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}else if(resultCode != 1){
				_errorMsg = response.getString("msg");
				showToast.getInstance(getApplicationContext()).showMsg(_errorMsg);
			}else{
				JSONObject data = response.getJSONObject("data");
				if(data != null){
//					_errorMsg = data.getString("success_message");
					_trace_id = data.getString("trace_id");
					showToast.getInstance(getApplicationContext()).showMsg(data.getString("success_message"));
				}
				isSuccess = true;
			}
			refreshUI(isSuccess);
		} catch (JSONException e) {
			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		showDialog(false);
		Intent intent = new Intent();
		intent.setClass(this, MyRewardClaimProcessResultActivity.class);
		if(isSuccess){
			intent.putExtra("type", MyRewardClaimProcessResultActivity.TYPE_SUCCESS);
			intent.putExtra("rewardname", _title);
			intent.putExtra("tranid", _trace_id);
		}else{
			intent.putExtra("type", MyRewardClaimProcessResultActivity.TYPE_ERROR);
			intent.putExtra("errormsg", _errorMsg);
		}
		startActivity(intent);
		finish();
	}

	@Override
	protected void getData() {
		showDialog(true);
    	HttpRequest request = HttpRequestHelper
    			.getGetRequest(
    					Constants.Url.getMyRewardScanPickupProcessUrl(
    							LoginUserInfo.getInstance().getToken()
    							, Constants.GlobalKey.getAppKey()
    							,_procedure
    							,_note
    							,_qrCode
    							,GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "redeptionObtain resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "redeptionObtain response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				parseData(null);
			}
		},"redeptionObtain");
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
