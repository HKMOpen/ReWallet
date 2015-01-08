package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.adapters.MyVCoinLogListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.MyVCoinLogListItem;
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
import java.util.Calendar;
import java.util.Date;

public class TransectionLogActivity extends BaseNavibarActivity {

	private final static String TAG = "TransectionLogActivity";
	private static final int REQUESTCODE = 154001;
	private TextView _tvVCoinValue;
	private ListView _lvLog;
	private MyVCoinLogListAdapter _logAdapter;
	private ArrayList<MyVCoinLogListItem> _items;
	
	private int _coinValue;
	private LoginDialog _dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent().getExtras() != null){
			_coinValue = getIntent().getExtras().getInt("coin");
			setContentView(R.layout.activity_transection_log);
			setupView();
			showDialog(true);
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}else{
			finish();
		}
		
		
	}
	private void setupView(){
		setupActionBar(getString(R.string.transectionLogActivity_title)
				, true, false, null, null);
		_items = new ArrayList<MyVCoinLogListItem>();
		_tvVCoinValue = (TextView)findViewById(R.id.transectionLog_tvCoinValue);
		_tvVCoinValue.setText(String.valueOf(_coinValue));
		_lvLog = (ListView)findViewById(R.id.transectionLog_lvLog);
		_logAdapter = new MyVCoinLogListAdapter(this, _items);
		_lvLog.setAdapter(_logAdapter);
		
	}
	@Override
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
//		MyVCoinLogListItem item;
//		for(int i = 0; i<24; i++){
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
		boolean isSucceed = false;
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					_items = new ArrayList<MyVCoinLogListItem>();
//					JSONArray aryData = response.getJSONArray("data");
					JSONObject ary = response.getJSONObject("data");
					if(ary != null){
						_coinValue = ary.getInt("balance");
						JSONArray aryData = ary.getJSONArray("history");
						if(aryData != null){
							MyVCoinLogListItem item;
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String tranId = null;
							int coin = 0;
							String time = null;
							for(int i = 0; i < aryData.length(); i++){
								tranId = aryData.getJSONObject(i).getString("transid");
								coin = aryData.getJSONObject(i).getInt("count");
								time = aryData.getJSONObject(i).getString("time");
								item = new MyVCoinLogListItem(tranId, coin > 0? getString(R.string.myVcoinActivity_IN): getString(R.string.myVcoinActivity_OUT),
										"", format.parse(time), coin);
								_items.add(item);
							}
							isSucceed = true;
						}
					}
					
				}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
					Intent intent = new Intent();
					intent.setClass(this, LoginActivity.class);
					startActivityForResult(intent, REQUESTCODE);
				}else{
					showToast.getInstance(getApplicationContext())
					.showMsg(response.getString("msg"));
				};
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		refreshUI(isSucceed);
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			_logAdapter.resetData(_items);
			_logAdapter.notifyDataSetChanged();
			_tvVCoinValue.setText(String.valueOf(_coinValue));
		}
		showDialog(false);
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		Date end = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();  
		cal.set(2014, 6, 1);
		
		Date start = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url.getMyVCoinHistoryUrl(
								LoginUserInfo.getInstance().getToken()
								,Constants.GlobalKey.getAppKey()
								,format.format(start)
								,format.format(end)
								,-1));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getMyVCoinHistory resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "getMyVCoinHistory response json failed." + e.toString());
				//parseBanlance(null);
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"VCoinHistory");
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
