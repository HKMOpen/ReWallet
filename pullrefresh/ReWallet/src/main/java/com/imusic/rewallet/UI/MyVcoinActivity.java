package com.imusic.rewallet.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.R.string;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class MyVcoinActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "MyVocinActivity";
	
	private static final int REQUESTCODE = 10501;
	
	private TextView _tvVCoinValue;
	private ImageView _imgUserIcon;
	private TextView _tvUserName;
	private TextView _tvWallet;
	private Button _btnHistory;
	private Button _btnMyReward;
	private Button _btnECoupon;
	private Button _btnEarn;
//	private Button _btnSetting;
//	private ListView _lvLog;
//	private Button _btnShowMore;
//	private Button _btnToGain;
//	private Button _btnToUse;
//	private Button _btnMyRewards;
//	private Button _btnMyCoupon;
//	private MyVCoinLogListAdapter _logAdapter;
//	private ArrayList<MyVCoinLogListItem> _items;
	
	private int _coinValue;
	private LoginDialog _dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_vcoin);
		setupView();
		
		_handler.obtainMessage(MSG_GETDATA).sendToTarget();
	}
	private void  saveUserLoginToken(String token,String nickName) {
		SharedPreferences sp = this.getSharedPreferences("session",Context.MODE_WORLD_READABLE);
		SharedPreferences.Editor  editor = sp.edit();
		editor.putString("token", token);
		editor.putString("nickName", nickName);
		
		editor.commit();
	}
	private void setupView(){
		setupActionBar(getString(R.string.myVcoinActivity_title),true
				,true,getString(R.string.myProfileActivity_signout),new OnClickListener() {
					@Override
					public void onClick(View v) {
						LoginUserInfo.getInstance().setTokenExpiry();
						saveUserLoginToken(null, null);
						Intent intent = new Intent();
						intent.setClass(MyVcoinActivity.this, LoginActivity.class);
						intent.putExtra("istomain", true);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					}
				});
//		DisplayMetrics metric = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		_items = new ArrayList<MyVCoinLogListItem>();
//		_tvVCoinValue = (TextView)findViewById(R.id.myVcoin_tvCoinValue);
//		_btnShowMore = (Button)findViewById(R.id.myVcoin_btnShowMore);
//		_btnShowMore.setOnClickListener(this);
//		_btnToGain = (Button)findViewById(R.id.myVcoin_btnToGain);
//		_btnToUse = (Button)findViewById(R.id.myVcoin_btnToUse);
//		_btnToGain.setOnClickListener(this);
//		_btnToUse.setOnClickListener(this);
//		_btnMyRewards = (Button)findViewById(R.id.myVcoin_btnMyReward);
//		_btnMyRewards.setOnClickListener(this);
//		_btnMyCoupon = (Button)findViewById(R.id.myVcoin_btnECoupons);
//		_btnMyCoupon.setOnClickListener(this);
//		_lvLog = (ListView)findViewById(R.id.myVcoin_lvLog);
//		_logAdapter = new MyVCoinLogListAdapter(this, _items);
//		_lvLog.setAdapter(_logAdapter);
		
		_imgUserIcon = (ImageView)findViewById(R.id.myvcoin_imgUserIcon);
//		_imgUserIcon.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(MyVcoinActivity.this, MyProfileActivity.class);
//				startActivity(intent);
//			}
//		});
		ImageLoader.getInstance().displayImage(LoginUserInfo.getInstance().getLoginUserInfo().profile_picture, _imgUserIcon, Util.getImageLoaderOptionForUserProfile(getApplicationContext()));
		_tvUserName = (TextView)findViewById(R.id.myvcoin_tvUserName);
		_tvUserName.setText(LoginUserInfo.getInstance().getLoginUserInfo().nickName);
		_tvWallet= (TextView)findViewById(R.id.myVcoin_tvWallet);
		_tvWallet.setText(String.format("%s%s", LoginUserInfo.getInstance().getLoginUserInfo().nickName,getString(R.string.myVcoinActivity_wallet)));
		 
		_tvVCoinValue = (TextView)findViewById(R.id.myvcoin_tvVcoinValue);
		_btnHistory = (Button)findViewById(R.id.myvcoin_btnHistory);
		_btnHistory.setOnClickListener(this);
		_btnECoupon = (Button)findViewById(R.id.myvcoin_btnECoupon);
		_btnECoupon.setOnClickListener(this);
		_btnMyReward = (Button)findViewById(R.id.myvcoin_btnMyRewards);
		_btnMyReward.setOnClickListener(this);
		_btnEarn = (Button)findViewById(R.id.myvcoin_btnEarn);
		_btnEarn.setOnClickListener(this);
//		_btnSetting = (Button)findViewById(R.id.myvcoin_btnSetting);
//		_btnSetting.setOnClickListener(this);
		
//		FontManager.changeFonts((ViewGroup)((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0)
//				, this);
	}

	@Override
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
//		MyVCoinLogListItem item;
//		for(int i = 0; i<3; i++){
//			try {
//				item = new MyVCoinLogListItem("xx", "out", "ddd Description!!ddd Description!!ddd Description!!ddd Description!!"
//						, new SimpleDateFormat("yyyy/MM/dd HH:mm").parse("2014/05/10 12:01")
//						, i);
//				_items.add(item);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				e.printStackTrace();
//			} 
//		}
//		refreshUI(true);
	}

	@Override
	protected void refreshUI(boolean isSuccee) {
		if(isSuccee){
//			_logAdapter.resetData(_items);
//			_logAdapter.notifyDataSetChanged();
			_tvVCoinValue.setText(String.valueOf(_coinValue));
		}
//		_tvVCoinValue.setText(String.valueOf(_coinValue));
		
		showDialog(false);
	}

	@Override
	protected void getData() {
		showDialog(true);
		getBalance();
//		getMyVCoinHistory();
		//parseData(null);
//		refreshUI(true);
	}

//	private void getMyVCoinHistory(){
//		Date end = new Date(System.currentTimeMillis());
//		Calendar cal = Calendar.getInstance();  
//		cal.set(2014, 6, 1);
//		
//		Date start = cal.getTime();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		HttpRequest request = HttpRequestHelper
//				.getGetRequest(
//						Constants.Url.getMyVCoinHistoryUrl(
//								LoginUserInfo.getInstance().getToken()
//								,Constants.GlobalKey.getAppKey()
//								,format.format(start)
//								,format.format(end)
//								,1));
//		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
//			@Override  
//            protected void onSuccess(JSONObject response, String tag){ 
//				Vlog.getInstance().info(false,TAG, "getMyVCoinHistory resposnse json succeed : "+response.toString());
//				parseVCoinHistory(response);
//			}
//			@Override  
//            protected void onFailure(Throwable e, String tag) { 
//				Vlog.getInstance().info(false,TAG,  "getMyVCoinHistory response json failed." + e.toString());
//				//parseBanlance(null);
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//			}
//		},"VCoinHistory");
//	}
//	private void parseVCoinHistory(JSONObject response){
//		boolean isSucceed = false;
//		if(response != null){
//			try {
//				int result = response.getInt("result");
//				if(result == 1){
//					_items = new ArrayList<MyVCoinLogListItem>();
//					JSONArray aryData = response.getJSONArray("data");
//					if(aryData != null){
//						MyVCoinLogListItem item;
//						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						String tranId = null;
//						int coin = 0;
//						String time = null;
//						for(int i = 0; i < aryData.length(); i++){
//							tranId = aryData.getJSONObject(i).getString("transid");
//							coin = aryData.getJSONObject(i).getInt("count");
//							time = aryData.getJSONObject(i).getString("time");
//							item = new MyVCoinLogListItem(tranId, coin > 0? getString(R.string.myVcoinActivity_IN): getString(R.string.myVcoinActivity_OUT),
//									"", format.parse(time), coin);
//							_items.add(item);
//						}
//						isSucceed = true;
//					}
//					
//				}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
//					Intent intent = new Intent();
//					intent.setClass(this, LoginActivity.class);
//					startActivityForResult(intent, REQUESTCODE);
//				}else{
//					showToast.getInstance(getApplicationContext())
//					.showMsg(response.getString("msg"));
//				}
//			} catch (JSONException e) {
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				e.printStackTrace();
//			} catch (ParseException e) {
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		refreshUI(isSucceed);
//	}
	private void getBalance(){
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url.getMyBalance(LoginUserInfo.getInstance().getToken()
								, Constants.GlobalKey.getAppKey()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "MyVocinActivity resposnse json succeed : "+response.toString());
				parseBanlance(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "MyVocinActivity response json failed." + e.toString());
				parseBanlance(null);
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"balance");
	}
	private void parseBanlance(JSONObject response){
		boolean isSucceed = false;
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					JSONObject data = response.getJSONObject("data");
					if(data != null){
						_coinValue = data.getInt("coin");
						isSucceed = true;
					}
				}else{
					showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
//			JSONObject data = ParseResponseDataUtil.tryParseResult(this, response, REQUESTCODE);
//			if(data != null){
//				try {
//					_coinValue = data.getInt("coin");
//					refreshUI(false);
//				} catch (JSONException e) {
//					showToast.getInstance(this).showMsg(e.getMessage());
//					e.printStackTrace();
//				}
//			}
			
		}
		refreshUI(isSucceed);
	}
	
	@Override
	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.myVcoin_btnShowMore:
//			Intent intent = new Intent();
//			intent.setClass(this, TransectionLogActivity.class);
//			intent.putExtra("coin", _coinValue);
//			startActivity(intent);
//			break;
//		case R.id.myVcoin_btnMyReward:
//			Intent myRewardIntent = new Intent();
//			myRewardIntent.setClass(this, MyRewarsListActivity.class);
//			startActivity(myRewardIntent);
//			break;
//		case R.id.myVcoin_btnECoupons:
//			Intent myCouponIntent = new Intent();
//			myCouponIntent.setClass(this, MyCouponsActivity.class);
//			startActivity(myCouponIntent);
//			break;
//		case R.id.myVcoin_btnToGain:
//		case R.id.myVcoin_btnToUse:
//			Intent insIntent= new Intent();
//			insIntent.setClass(this, InstructionsActivity.class);
//			int index = 0;
//			if(arg0.getId() == R.id.myVcoin_btnToUse){
//				index = 1;
//			}
//			insIntent.putExtra("index", index);
//			startActivity(insIntent);
//			break;
//		
//		}
		switch (arg0.getId()) {
		case R.id.myvcoin_btnHistory:
			Intent intent = new Intent();
			intent.setClass(this, TransectionLogActivity.class);
			intent.putExtra("coin", _coinValue);
			startActivity(intent);
			break;
		case R.id.myvcoin_btnEarn:
			Intent insIntent= new Intent();
			insIntent.setClass(this, InstructionsActivity.class);
			insIntent.putExtra("index", 0);
			startActivity(insIntent);
			break;
		case R.id.myvcoin_btnECoupon:
			Intent myCouponIntent = new Intent();
			myCouponIntent.setClass(this, MyCouponsActivity.class);
			startActivity(myCouponIntent);
			break;
		case R.id.myvcoin_btnMyRewards:
			Intent myRewardIntent = new Intent();
			myRewardIntent.setClass(this, MyRewarsListActivity.class);
			startActivity(myRewardIntent);
			break;
//		case R.id.myvcoin_btnSetting:
//			Intent settingIntent = new Intent();
//			settingIntent.setClass(this, SettingActivity.class);
//			startActivity(settingIntent);
//			break;
		}
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
