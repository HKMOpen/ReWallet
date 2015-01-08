package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;

import org.json.JSONObject;

public class MyRewardClaimProcessResultActivity extends BaseNavibarActivity {

	public final static int TYPE_SUCCESS = 0;
	public final static int TYPE_ERROR = 1;
	
	private TextView _notice;
	private TextView _content;
	
	private String _rewardName;
	private String _tranId;
	private String _errorMsg;
	private Button _btnContinue;
	
	private int _type = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reward_claim_process_result);
		if(getIntent().getExtras() != null){
			_type = getIntent().getExtras().getInt("type");
			if(_type == TYPE_SUCCESS){
				_rewardName = getIntent().getExtras().getString("rewardname");
				_tranId = getIntent().getExtras().getString("tranid");
			}else if(_type == TYPE_ERROR){
				_errorMsg = getIntent().getExtras().getString("errormsg");
			}else
				finish();
		}
		setupView();
	}

	private void setupView(){
		clearProcessActivity();
		String title = getString(_type == TYPE_SUCCESS?
				R.string.myRewardClaimProcessActivity_success_title : R.string.myRewardClaimProcessActivity_error_title);
		setupActionBar(title, false, false, null, null);
		_notice = (TextView)findViewById(R.id.myRewardClaimProcessResult_tvNotice);
		_content = (TextView)findViewById(R.id.myRewardClaimProcessResult_tvContent);
		_notice.setText(getString(_type == TYPE_SUCCESS?
				R.string.myRewardClaimProcessActivity_success_notice 
				: R.string.myRewardClaimProcessActivity_error_notice));
		String content = _type == TYPE_SUCCESS?
				String.format(getString(R.string.myRewardClaimProcessActivity_success_contentFormat), _tranId, _rewardName)
				:String.format(getString(R.string.myRewardClaimProcessActivity_error_contentFormat), _errorMsg) ;
		_content.setText(content);
		_btnContinue = (Button)findViewById(R.id.myRewardClaimProcessResult_btnContinue);
		_btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoMyRewardList();
			}
		});
	}
	private void clearProcessActivity(){
		if(MyRewardDetailUnclaimedActivity.Instance != null)
			MyRewardDetailUnclaimedActivity.Instance.finish();
		if(MyRewardCaimProcessWithNoteActivity.Instance != null)
			MyRewardCaimProcessWithNoteActivity.Instance.finish();
	}
	
	private void gotoMyRewardList(){
		Intent intent = new Intent();
		intent.setClass(this, MyRewarsListActivity.class);
		intent.putExtra("isrefrash", _type ==TYPE_SUCCESS ? true: false);
		startActivity(intent);
		finish();
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


}
