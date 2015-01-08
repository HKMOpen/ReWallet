package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.adapters.MyCouponsListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.MyCouponItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyCouponsActivity extends BaseNavibarActivity {

	private final static String TAG = "MyCouponsActivity";
	
	private static final int REQUESTCODE = 10777;
	
	public static int STATUS_PROCESSING = 0;
	public final static int STATUS_SUCCESS = 1;
	public final static int STATUS_FAILURE = 2;
	
	private ListView _lvCoupon;
	private MyCouponsListAdapter _adapter;
	private ArrayList<MyCouponItem> _lstData;
//	private RelativeLayout _layout;
	private LoginDialog _dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_coupons);
		setupView();
		_handler.obtainMessage(MSG_GETDATA).sendToTarget();
	}
	private void setupView(){
		setupActionBar(getString(R.string.myCouponsActivity_title), true, false,
				null, null);
//		_layout = (RelativeLayout)findViewById(R.id.myCoupon_layout);
		_lvCoupon = (ListView)findViewById(R.id.myCoupon_lvCoupon);
		_lstData = new ArrayList<MyCouponItem>();
		_adapter = new MyCouponsListAdapter(this, _lstData);
		_lvCoupon.setAdapter(_adapter);
//		_cover = new DataProcessCover(getApplicationContext(), null);
	}
	
	@Override
	protected void parseData(JSONObject response) {
		boolean isSucceed = false;
		if(response != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				int result = response.getInt("result");
				if(result == 1){
					_lstData = new ArrayList<MyCouponItem>();
					JSONArray aryData = response.getJSONArray("data");
					if(aryData != null){
						MyCouponItem item;
						int id = 0;
						String image_sq = null;
						String title = null;
						String couponCode = null;
						int coin;
						int status = -1;
						String vendor_name = null;
						String vendor_url = null;
						String desc = null;
						String exp_date = null;
						
						for(int i = 0; i < aryData.length(); i++){
							couponCode = aryData.getJSONObject(i).getString("client_coupon_code");
							coin = aryData.getJSONObject(i).getInt("vcoin_expense");
							status = aryData.getJSONObject(i).getInt("status");
							if(status == STATUS_FAILURE)
								continue;
							JSONObject detail = aryData.getJSONObject(i).getJSONObject("detail");
							if(detail != null){
								id = detail.getInt("ID");
								image_sq = detail.getString("image_sq_thumb");
								title = detail.getString("title");
								vendor_name = detail.getString("vendor_name");
								//vendor_url = detail.getString("vendor_url");
								//desc = detail.getString("description");
								exp_date = detail.getString("exp_date");
							}
							if(exp_date == null || exp_date.trim().length() <=0){
								exp_date = null;
							}
							item = new MyCouponItem(id, title, image_sq, 
									vendor_name, exp_date == null? null:  format.parse(exp_date), 
									"", coin, desc, couponCode, vendor_url);
							item.categoryImage = "";
							item.status = status;
							item.countryDescription = "";
							item.description = "";//
							_lstData.add(item);
						}
						
					}
					isSucceed = true;
					
				}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
					Intent intent = new Intent();
					intent.setClass(this, LoginActivity.class);
					startActivityForResult(intent, REQUESTCODE);
				}else{
					showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		refreshUI(isSucceed);
		
		
//		for(int i = 0; i< 10; i++){
//			//int pId, String pName, String img,
////			String vender, Date eDate, String cDesc, int count, String desc
//			strDay = String.format("2014-01-%d 12:56", i+1);
//			MyCouponItem item;
//			try {
//				item = new MyCouponItem(i, "Coupon Name/Title " + i,
//						"", "provider", format.parse(strDay),
//						"HKG", i+100, 
//						"description~description~description~description~",
//						"code:345RT-34-G-DFG34-DFG-DF-34534-DFGGDFG-34F-DFG34-DFG-DF-34534-DFGGDFG-34F");
//				_lstData.add(item);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		refreshUI(true);
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			if(_lstData != null && _lstData.size() > 0){
				ArrayList<MyCouponItem> tempTop = new ArrayList<MyCouponItem>();
				ArrayList<MyCouponItem> temp = new ArrayList<MyCouponItem>();
				MyCouponItem tmpItem;
				for (int i = 0; i < _lstData.size(); i++) {
					tmpItem = _lstData.get(i);
					if (tmpItem.status == STATUS_PROCESSING) {
						tempTop.add(tmpItem);
					}else {
						temp.add(tmpItem);
					}
				}
				_lstData.clear();
				if(tempTop.size() > 0){
					_lstData.addAll(0, tempTop);
				}
				if(temp.size() > 0){
					_lstData.addAll(temp);
				}
				tempTop = null;
				temp = null;
				_adapter.resetDate(_lstData);
				_adapter.notifyDataSetChanged();
			}
		}
//		_layout.removeView(_cover);
		showDialog(false);
	}

	@Override
	protected void getData() {
//		getMyCouponUrl
//		_layout.addView(_cover);
		showDialog(true);
		HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getMyCouponUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "my coupon resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "my coupon response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				parseData(null);
			}
		},"myCoupon");
		
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
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
}
