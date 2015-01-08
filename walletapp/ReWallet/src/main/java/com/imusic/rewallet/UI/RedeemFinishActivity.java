package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;

import org.json.JSONObject;

public class RedeemFinishActivity extends BaseNavibarActivity implements OnClickListener {
	
	private Button _btnMyReward;
	private Button _btnConfirm;
	private TextView _tvNotice;
//	private ArrayList<Activity> _lstActivity;
	private int _type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_redeem_finish);
		
		if (getIntent().getExtras() != null) {
			String title = getIntent().getExtras().getString("title");
			String id = getIntent().getExtras().getString("id");
			String msg = getIntent().getExtras().getString("msg");
			_type = getIntent().getExtras().getInt("type");
			setupView(title, id, msg);
			
			cleanActivity();
		}else
			finish();
	}
	private void cleanActivity(){
		if(RedemptionActivity.Instance != null)
			RedemptionActivity.Instance.finish();
		if(RedemptionOptionActivity.Instance != null)
			RedemptionOptionActivity.Instance.finish();
		if(RedemptionConfirmActivity.Instance != null)
			RedemptionConfirmActivity.Instance.finish();
		if (RewardDetailActivity.Instance != null) {
			RewardDetailActivity.Instance.finish();
		}
	}

	private void setupView(String title, String id, String msg){
		setupActionBar(getString(R.string.redeemFinishActivity_Title)
				, true, false, null, null);
		TextView tvTitle = (TextView)findViewById(R.id.redeemFinish_tvRewardTitle);
		TextView tvID = (TextView)findViewById(R.id.redeemFinish_tvTransectionId);
		tvTitle.setText(Html.fromHtml(title));
		tvID.setText(id);
		_btnMyReward = (Button)findViewById(R.id.redemption_btnMyRewards);
		_btnMyReward.setOnClickListener(this);
		if(_type == RewardDetailActivity.TYPE_COUPON){
//			Drawable cup = getResources().getDrawable(R.drawable.myvcoin_coupon);
//			cup.setBounds(0, 0, cup.getMinimumWidth(), cup.getMinimumHeight());
//			_btnMyReward.setCompoundDrawables(
//					null, cup, null, null);
			_btnMyReward.setText(R.string.myCouponsActivity_title);
		}
		_btnConfirm = (Button)findViewById(R.id.redemption_btnConfirm);
		_btnConfirm.setOnClickListener(this);
		
		_tvNotice = (TextView)findViewById(R.id.redeemFinish_tvNotce);
		_tvNotice.setText(msg == null? null: Html.fromHtml(msg));
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

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.redemption_btnMyRewards){
//			if(RewardDetailActivity.Instance != null)
//				RewardDetailActivity.Instance.finish();
			Intent intent = new Intent();
			if(_type == RewardDetailActivity.TYPE_COUPON)
				intent.setClass(this, MyCouponsActivity.class);
			else
				intent.setClass(this, MyRewarsListActivity.class);
			startActivity(intent);
			finish();
		}else if(v.getId() == R.id.redemption_btnConfirm){
			finish();
		}
	}
	
	

	
}
