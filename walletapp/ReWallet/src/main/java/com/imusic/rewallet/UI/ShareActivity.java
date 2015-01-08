package com.imusic.rewallet.UI;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ShareActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "ShareActivity";
	
	private TextView _tvTitle;
	private ImageView _imgBanner;
	private TextView _tvUserName;
	private EditText _edContent;
	private Button _btnSubmit;
	private int _id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		String title = getIntent().getExtras().getString("title");
		String img = getIntent().getExtras().getString("img");
		_id = getIntent().getExtras().getInt("id");
		setupView(title, img);
		
	}
	private void setupView(String title, String imgUrl){
		setupActionBar(getString(R.string.shareActivity_title)
				, true, false, null, null);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		_tvTitle = (TextView)findViewById(R.id.share_tvTitle);
		_imgBanner = (ImageView)findViewById(R.id.share_imgBanner);
		_imgBanner.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*9/16));
		_tvTitle.setText(title);
		ImageLoader.getInstance().displayImage(imgUrl, _imgBanner);
		_tvUserName = (TextView)findViewById(R.id.share_tvUserName);
		_tvUserName.setText(LoginUserInfo.getInstance().getLoginUserInfo().nickName);
		
		_btnSubmit = (Button)findViewById(R.id.share_btnSubmit);
		_btnSubmit.setOnClickListener(this);
		_edContent = (EditText)findViewById(R.id.share_edContent);
	}

	@Override
	protected void parseData(JSONObject response) {
		boolean isS = false;
		try {
			int result = response.getInt("result");
			if(result == 1){
				isS = true;
			}else{
				showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		refreshUI(isS);
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	protected void getData() {
		String comment = _edContent.getText().toString().trim();
		if(comment == null || comment.length() <= 0){
			showToast.getInstance(this).showMsg(
					getString(R.string.shareActivity_noData));
			return;
		}
		String url = Constants.Url.getAddShareUrl(_id);
		HttpRequest request = HttpRequestHelper.getGetRequest(url);
		AsyncHttpClient.sendRequest(this,request ,
				new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
	        protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "add share resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
	        protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "add share response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		} ,"");
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.share_btnSubmit){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}

	
}
