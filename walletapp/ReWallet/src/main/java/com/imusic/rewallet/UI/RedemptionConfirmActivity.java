package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RewardDetailItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedemptionConfirmActivity extends BaseNavibarActivity implements OnClickListener {

//	private ImageButton _imgbtnRedeem;
	private static String TAG = "RedemptionConfirmActivity";
	
	private static final int REQUESTCODE = 197501;
	
	private Button _btnRedeem;
	private TextView _tvRewardTitle;
	private TextView _tvContent;
	private ImageView _imgPic;
	private TextView _tvOption1;
	private TextView _tvOption2;
	private TextView _tvOption3;
	private TextView _tvOption4;
	private EditText _edOption1;
	private EditText _edOption2;
	private EditText _edOption3;
	private EditText _edOption4;
	private TextView _tvVendor;
	private TextView _tvExpDate;
	private TextView _tvCoinCount;
//	private TextView _tvCountryDesc;
//	private ImageView _imgCategory;
	private LinearLayout _optionLayout;
//	private ArrayList<Activity> _lstActivity;
	private RewardDetailItem _item;
	private LoginDialog _dialog;
	private int _type;
	private int _extId;
	private int _addressId;
//	private int _selectedExtentionIndex =  -1;
//	private String _selectedAddressKey = null;
	private final LinearLayout.LayoutParams _lpTextView 
		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	private final LinearLayout.LayoutParams _lpEditView
		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

	public static RedemptionConfirmActivity Instance;
	
	private DisplayImageOptions _imageOptions;// = Util.getImageLoaderOptionForDetail();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_imageOptions = Util.getImageLoaderOptionForDetail(getApplicationContext());
		if (getIntent().getExtras() != null) {
			setContentView(R.layout.activity_redemption_confirm);
			_item = (RewardDetailItem)getIntent().getExtras().get("item");
			_type = getIntent().getExtras().getInt("type");
//			_lstActivity = (ArrayList<Activity>)getIntent().getExtras().get("lstActivity");
//			_selectedExtentionIndex = getIntent().getExtras().getInt("opt1");
//			_selectedAddressKey = getIntent().getExtras().getString("opt2");
			if(_type != RewardDetailActivity.TYPE_COUPON){
//				setupView(_selectedExtentionIndex
//						,_selectedAddressKey
//						,getIntent().getExtras().getString("opt3")
//						,getIntent().getExtras().getString("opt4"));
				_extId = getIntent().getExtras().getInt("extId");
				_addressId =  getIntent().getExtras().getInt("addressId");
				ArrayList<String[]> ls = (ArrayList<String[]>)getIntent().getExtras().get("opt");
				setupView(ls);
			}else{
				setupView(null);
			}
			Instance = this;
		}else{
			finish();
		}
	}
	
	
	private void setupView(ArrayList<String[]> ls ){
		setupActionBar(getString(R.string.redemptionActivity_actionbarTitle)
				, true, false, null, null);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		_tvRewardTitle = (TextView)findViewById(R.id.redemption_tvRewardTitle);
		_tvRewardTitle.setText(Html.fromHtml(_item.title));
		_tvVendor = (TextView)findViewById(R.id.redemptionActivity_tvVendor);
		_tvVendor.setText(_item.vendor_name==null?null:Html.fromHtml(_item.vendor_name));
		_tvExpDate = (TextView)findViewById(R.id.redemptionActivity_tvExpDate);
		_tvExpDate.setText(getString(R.string.global_expDate) + " " + _item.expiration_date);
		_tvCoinCount = (TextView)findViewById(R.id.redemptionActivity_tvCoinCount);
		_tvCoinCount.setText(String.valueOf(_item.vcoin_value));
//		_tvCountryDesc = (TextView)findViewById(R.id.redemptionActivity_tvCountryDesc);
//		_tvCountryDesc.setText(Html.fromHtml(_item.countryDescription));
		_tvContent = (TextView)findViewById(R.id.redemption_tvContent);
		_tvContent.setText(_item.product_description==null? null:Html.fromHtml(_item.product_description));
		_btnRedeem = (Button)findViewById(R.id.redemption_btnRedeem);
		_btnRedeem.setOnClickListener(this);
//		_imgCategory = (ImageView)findViewById(R.id.redemptionActivity_imgCategory);
//		ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory,_imageOptions);
		_imgPic = (ImageView)findViewById(R.id.redemption_imgPic);
		_imgPic.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*10/16));
		ImageLoader.getInstance().displayImage(_item.image_banner, _imgPic,_imageOptions);
		_optionLayout = (LinearLayout)findViewById(R.id.redemptionActivity_layout_option);
		if(_type != RewardDetailActivity.TYPE_COUPON){
			_tvOption1 = (TextView)findViewById(R.id.redemption_tvOption1);
			_tvOption2 = (TextView)findViewById(R.id.redemption_tvOption2);
			_tvOption3 = (TextView)findViewById(R.id.redemption_tvOption3);
			_tvOption4 = (TextView)findViewById(R.id.redemption_tvOption4);
			_edOption1 = (EditText)findViewById(R.id.redemption_edOption1);
			_edOption2 = (EditText)findViewById(R.id.redemption_edOption2);
			_edOption3 = (EditText)findViewById(R.id.redemption_edOption3);
			_edOption4 = (EditText)findViewById(R.id.redemption_edOption4);
			
			_tvOption1.setVisibility(View.GONE);
			_tvOption2.setVisibility(View.GONE);
			_tvOption3.setVisibility(View.GONE);
			_tvOption4.setVisibility(View.GONE);
			_edOption1.setVisibility(View.GONE);
			_edOption2.setVisibility(View.GONE);
			_edOption3.setVisibility(View.GONE);
			_edOption4.setVisibility(View.GONE);
			
			if(ls != null){
				TextView tv;
				EditText et;
				for(int i = 0; i < ls.size(); i++){
					String[] str = ls.get(i);
					tv = getIndexOfOptionTextView(i);
					et = getIndexOfOptionEditView(i);
					if(tv != null){
						tv.setText(str[0]);
						tv.setVisibility(View.VISIBLE);
					}
					if(et != null){
						et.setText(str[1]);
						et.setVisibility(View.VISIBLE);
					}
				}
			}
			
//			if(opt1 >= 0){
////				_edOption1.setText(_item.extensions.get(opt1));
//			}else{
//				_tvOption1.setVisibility(View.GONE);
//				_edOption1.setVisibility(View.GONE);
//			}
//			if(opt2 != null && opt2.length() > 0){
////				_edOption2.setText(_item.addresses.get(opt2));
//			}else{
//				_tvOption2.setVisibility(View.GONE);
//				_edOption2.setVisibility(View.GONE);
//			}
//			if(opt3 != null && opt3.length() > 0){
//				
//			}else{
//				_tvOption3.setVisibility(View.GONE);
//				_edOption3.setVisibility(View.GONE);
//			}
//			if(opt4 != null && opt4.length() > 0){
//				
//			}else{
//				_tvOption4.setVisibility(View.GONE);
//				_edOption4.setVisibility(View.GONE);
//			}
		}else{
			_optionLayout.setVisibility(View.GONE);
		}
		_lpTextView.setMargins(0, 0, 0, 15);
		_lpEditView.setMargins(10, 0, 0, 10);
		
	}

	@Override
	protected void parseData(JSONObject response) {
		showDialog(false);
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					JSONObject data = response.getJSONObject("data");
					if(data != null){
						String code = data.getString("trace_id");
						String msg = data.getString("message");
						if(code != null && code.length() > 0){
//							_lstActivity.add(RedemptionConfirmActivity.this);
							Intent intent = new Intent();
							intent.setClass(this, RedeemFinishActivity.class);
							intent.putExtra("title", _item.title);
							intent.putExtra("id", code);
							intent.putExtra("type", _type);
							intent.putExtra("msg", msg);
//							intent.putExtra("lstActivity", _lstActivity);
							startActivity(intent);
						}
					}
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
			}
		}
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		// TODO Auto-generated method stub
//		if(isSuccess){
//			Intent intent = new Intent();
//			intent.setClass(this, RedeemFinishActivity.class);
//			intent.putExtra("title", _item.title);
//			intent.putExtra("id", "9348503-234-234-66");
//			startActivity(intent);
//		}
	}

	@Override
	protected void getData() {
		showDialog(true);
		HttpRequest request = null;
		if(_type == RewardDetailActivity.TYPE_COUPON){
			request = HttpRequestHelper
				.getGetRequest(
						Constants.Url
						.getRedeemECouponUrl(LoginUserInfo.getInstance().getToken(),Constants.GlobalKey.getAppKey(), _item.Id
								, GlobalDataInfo.getInstance().getLanguage()));
		}else{
			request = HttpRequestHelper.getGetRequest(
					Constants.Url.getRedeemRewardUrl(
							LoginUserInfo.getInstance().getToken()
							,Constants.GlobalKey.getAppKey()
							, _item.stock_id
							, true
							, _item.distribution
							, _item.expiration_date
							, _extId
							, _item.distribution.equals("CENTRAL")? -1 : _addressId
							, _item.vcoin_value
							, GlobalDataInfo.getInstance().getLanguage()));
		}
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getData resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "getData response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				parseData(null);
			}
		},"redemptionConfirm");
	}
//	private void parseCouponData(JSONObject response){
//		
//	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.redemption_btnRedeem){
			if(LoginUserInfo.getInstance().getToken() == null
					|| LoginUserInfo.getInstance().getToken().length() <= 0){
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
			}else{
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
			
			
		}
	}
	ArrayList<TextView> _lsTextView = new ArrayList<TextView>();
	ArrayList<EditText> _lstEditView = new ArrayList<EditText>();
	private TextView getIndexOfOptionTextView(int index){
//		switch (index) {
//		case 0:
//			return _tvOption1;
//		case 1:
//			return _tvOption2;
//		case 2:
//			return _tvOption3;
//		case 3:
//			return _tvOption4;
//		default:
//				return null;
//		}
		if (_lsTextView.size() <= index ||_lsTextView.get(index) == null) {
			final TextView tv = new TextView(this);
			tv.setLayoutParams(_lpTextView);
			tv.setTextSize(14);
			tv.getPaint().setFakeBoldText(true);
			tv.setTextColor(getResources().getColor(R.color.global_Text_Title));
			tv.setTag(index);
			_optionLayout.addView(tv);
			_lsTextView.add(tv);
			return tv;
		}else{
			return _lsTextView.get(index);
		}
	}
	private EditText getIndexOfOptionEditView(int index){
//		switch (index) {
//		case 0:
//			return _edOption1;
//		case 1:
//			return _edOption2;
//		case 2:
//			return _edOption3;
//		case 3:
//			return _edOption4;
//		default:
//				return null;
//		}
		if (_lstEditView.size() <= index ||_lstEditView.get(index) == null) {
			final EditText ev = new EditText(this);
			ev.setLayoutParams(_lpEditView);
			ev.setTag(index);
			ev.setEnabled(false);
			ev.setFocusable(false);
			ev.setPadding(20, 20, 20, 20);
			ev.setBackground(getResources().getDrawable(R.drawable.textbox_stroke_write));
			_optionLayout.addView(ev);
			_lstEditView.add(ev);
			return ev;
		}else{
			return _lstEditView.get(index);
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
