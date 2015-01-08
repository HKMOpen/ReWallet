package com.imusic.rewallet.UI;

import android.content.Intent;
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
import com.imusic.rewallet.adapters.RewardDetailCommentListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RewardCommentItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.RewardDetailCommentListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "CommentActivity";
	private static final int REQUESTCODE = 40001;
	
	private RewardDetailCommentListView _lvUserComment;
	private RewardDetailCommentListAdapter _adapter;
	private ArrayList<RewardCommentItem> _lstComment;
	private int _postId;
	
	
	private TextView _tvTitle;
	private ImageView _imgBanner;
	private TextView _tvUserName;
	private Button _btnSubmit;
	private EditText _edContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		String title = getIntent().getExtras().getString("title");
		String img = getIntent().getExtras().getString("img");
		_postId = getIntent().getExtras().getInt("postid");
		_lstComment = (ArrayList<RewardCommentItem>) getIntent().getExtras().getSerializable("list");
		setupView(title, img);
		
	}
	private void setupView(String title, String imgUrl){
		//setDummyData();
		setupActionBar(getString(R.string.commentActivity_title)
				, true, false, null, null);
		_lvUserComment = (RewardDetailCommentListView)findViewById(R.id.comment_lvComments);
		_adapter = new RewardDetailCommentListAdapter(this, _lstComment);
		_lvUserComment.setAdapter(_adapter);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		_tvTitle = (TextView)findViewById(R.id.comment_tvTitle);
		_imgBanner = (ImageView)findViewById(R.id.comment_imgBanner);
		_imgBanner.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*9/16));
		_tvTitle.setText(title);
		ImageLoader.getInstance().displayImage(imgUrl, _imgBanner);
		_tvUserName = (TextView)findViewById(R.id.comment_tvUserName);
		_tvUserName.setText(LoginUserInfo.getInstance().getLoginUserInfo().nickName);
		
		_btnSubmit = (Button)findViewById(R.id.comment_btnSubmit);
		_btnSubmit.setOnClickListener(this);
		_edContent = (EditText)findViewById(R.id.comment_edContent);
	}

	@Override
	protected void parseData(JSONObject response) {
		boolean isS = false;
		try {
			int result = response.getInt("result");
			if(result == 1){
				isS = true;
			}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}else{
				showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
					getString(R.string.commentActivity_noData));
			return;
		}
//		DataRequestUtils.sendAddComment(this, _postId, comment, _listener);
		String url = Constants.Url.getAddCommentUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey(), _postId, comment);
		HttpRequest request = HttpRequestHelper.getGetRequest(url);
		AsyncHttpClient.sendRequest(this,request ,
				new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
	        protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "add comment resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
	        protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "add comment response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		} ,"");
		
	}
	
	
	@Override
	public void onClick(View v) {
		if(v.getId() ==  R.id.comment_btnSubmit){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}

	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}
	

}
