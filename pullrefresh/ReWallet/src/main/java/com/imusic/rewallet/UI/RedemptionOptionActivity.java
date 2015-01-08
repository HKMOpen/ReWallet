package com.imusic.rewallet.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.RedeemExtensionOptionStruct;
import com.imusic.rewallet.model.RedeemExtensionOutputStruct;
import com.imusic.rewallet.model.RewardDetailItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.imusic.rewallet.widgets.TextboxForProfileLarge;
import com.imusic.rewallet.widgets.WheelView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class RedemptionOptionActivity extends BaseNavibarActivity {

//	private ImageButton _imgbtnConfirm;
	private final static String TAG = "RedemptionOptionActivity";
	private Button _btnConfirm;
	private TextView _tvRewardTitle;
	private TextView _tvContent;
	private ImageView _imgPic;
	private TextboxForProfileLarge _edOption1;
	private TextboxForProfileLarge _edOption2;
	private TextboxForProfileLarge _edOption3;
	private TextboxForProfileLarge _edOption4;
	private TextView _tvOption1;
	private TextView _tvOption2;
	private TextView _tvOption3;
	private TextView _tvOption4;
	private RewardDetailItem _item;
	private TextView _tvVendor;
	private TextView _tvExpDate;
	private TextView _tvCoinCount;
//	private TextView _tvCountryDesc;
//	private ImageView _imgCategory;
	private LinearLayout _optionLayout;
//	private ArrayList<Activity> _lstActivity;

	private int _selectedOptionExtenstionIndex =-1;
	private int _selectedOption1TempIndex;
	
//	private String _selectedAddressKey;
	private int _selectedAddressIndex;
	private int _selectedOption2TempIndex;
//	private ArrayList<String> _addressKey;
	private ArrayList<String> _addressValue;
	private ArrayList<String[]> _address;
//	HashMap<String, String> _addresses;
//	HashMap<String, RedeemExtensionOptionStruct> _ext_structure;
	private ArrayList<RedeemExtensionOptionStruct> _ext_structure;
	private ArrayList<RedeemExtensionOutputStruct> _ext_output;
	
	private int _type;
	private String _architecture;
	
	private LoginDialog _dialog;
	
	public static RedemptionOptionActivity Instance;
	
	private final LinearLayout.LayoutParams _lpTextView 
		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	private final LinearLayout.LayoutParams _lpEditView 
		= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150);

	private DisplayImageOptions _imageOptions;// = Util.getImageLoaderOptionForDetail();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_imageOptions = Util.getImageLoaderOptionForDetail(getApplicationContext());
		if (getIntent().getExtras() != null) {
			setContentView(R.layout.activity_redemption_option);
			_item = (RewardDetailItem)getIntent().getExtras().get("item");
			_type = getIntent().getExtras().getInt("type");
//			_lstActivity = (ArrayList<Activity>)getIntent().getExtras().get("lstActivity");
			setupView();
			if(_type != RewardDetailActivity.TYPE_COUPON){
				_dialog = new LoginDialog(RedemptionOptionActivity.this, R.style.LoginTransparent);
				_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				_dialog.show();
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
			Instance = this;
		}else{
			finish();
		}
		
	}
	
	private void setupView(){
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
		_tvContent = (TextView)findViewById(R.id.redemption_tvContent);
		_tvContent.setText(_item.product_description==null?null:Html.fromHtml(_item.product_description));
//		_tvCountryDesc = (TextView)findViewById(R.id.redemptionActivity_tvCountryDesc);
//		_tvCountryDesc.setText(Html.fromHtml(_item.countryDescription));
//		_imgCategory = (ImageView)findViewById(R.id.redemptionActivity_imgCategory);
//		_imgbtnConfirm = (ImageButton)findViewById(R.id.redemption_imgbtnConfirm);
		_btnConfirm = (Button)findViewById(R.id.redemption_btnConfirm);
		_imgPic = (ImageView)findViewById(R.id.redemption_imgPic);
		_imgPic.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*10/16));
		ImageLoader.getInstance().displayImage(_item.image_banner, _imgPic,_imageOptions);
//		ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory,_imageOptions);
		_optionLayout = (LinearLayout)findViewById(R.id.redemptionActivity_layout_option);
		if(_type != RewardDetailActivity.TYPE_COUPON){
			_tvOption1 = (TextView)findViewById(R.id.redemption_tvOption1);
			_tvOption2 = (TextView)findViewById(R.id.redemption_tvOption2);
			_tvOption3 = (TextView)findViewById(R.id.redemption_tvOption3);
			_tvOption4 = (TextView)findViewById(R.id.redemption_tvOption4);
			_edOption1 = (TextboxForProfileLarge)findViewById(R.id.redemption_edOption1);
			_edOption2 = (TextboxForProfileLarge)findViewById(R.id.redemption_edOption2);
			_edOption3 = (TextboxForProfileLarge)findViewById(R.id.redemption_edOption3);
			_edOption4 = (TextboxForProfileLarge)findViewById(R.id.redemption_edOption4);

//			_optionLayout.setVisibility(View.GONE);
			
			_tvOption1.setVisibility(View.GONE);
			_edOption1.setVisibility(View.GONE);
			_tvOption2.setVisibility(View.GONE);
			_edOption2.setVisibility(View.GONE);
			_tvOption3.setVisibility(View.GONE);
			_edOption3.setVisibility(View.GONE);
			_tvOption4.setVisibility(View.GONE);
			_edOption4.setVisibility(View.GONE);
			
//			if(_item.extensions != null && _item.extensions.size() > 0){
//				if(_item.extensions.size() == 1){
//					if(_item.extensions.get(0).equals("NA")){
//						_tvOption1.setVisibility(View.GONE);
//						_edOption1.setVisibility(View.GONE);
//					}else{
//						_edOption1.setContentEnable(_item.extensions.get(0), false);
//					}
//				}else{
//					_selectedOptionExtenstionIndex = 0;
//					_edOption1.setArrowShow(true);
//					_edOption1.setContentEnable(_item.extensions.get(_selectedOptionExtenstionIndex), false);
//					_edOption1.setOnCustomClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							setOption1();
//						}
//					});
//				}
//			}else{
//				_tvOption1.setVisibility(View.GONE);
//				_edOption1.setVisibility(View.GONE);
//			}
//			
////			_item.addresses .. option2
//			if(_item.addresses != null && _item.addresses.size() > 0){
//				Iterator iter = _item.addresses.entrySet().iterator();
//				_addressKey = new ArrayList<String>();
//				_addressValue = new ArrayList<String>();
//				while(iter.hasNext()){
//					@SuppressWarnings("unchecked")
//					Entry<String, String> entry = (Entry<String, String>)iter.next();
//					_addressKey.add(entry.getKey());
//					_addressValue.add(entry.getValue());
//				}
//				if(_addressKey.size() == 1 || _item.distribution_type.equals("perpetual")){
//					_edOption2.setContentEnable(_addressValue.get(0), false);
//					_selectedAddressKey = _addressKey.get(0);
//				}else{
//					_edOption2.setArrowShow(true);
//					_edOption2.setContentEnable(_addressValue.get(_selectedAddressIndex), false);
//					_edOption2.setOnCustomClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							setOption2();
//						}
//					});
//				}
//			}else{
//				_tvOption2.setVisibility(View.GONE);
//				_edOption2.setVisibility(View.GONE);
//			}
			
			
		}else{
			_optionLayout.setVisibility(View.GONE);
		}

		_btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
//				_lstActivity.add(RedemptionOptionActivity.this);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), RedemptionConfirmActivity.class);
				intent.putExtra("item", _item);
				intent.putExtra("type", _type);
//				intent.putExtra("lstActivity", _lstActivity);
				if(_type != RewardDetailActivity.TYPE_COUPON){
//					intent.putExtra("opt1", new String[]{});
//					intent.putExtra("opt2", _selectedAddressKey);
//					intent.putExtra("opt3", "");
//					intent.putExtra("opt4", "");
					
//					HashMap<String, String[]> map = new HashMap<String, String[]>();
					
					int extId = verifyOption();
					if(extId < -88){
						return;
					}
					intent.putExtra("extId", extId);
					intent.putExtra("addressId", _address == null ? 0 : Integer.valueOf(_address.get(_selectedAddressIndex)[0]));
//					Vlog.getInstance().debug(false, TAG, "put extra addressid: "+_address.get(_selectedAddressIndex)[0]);
					ArrayList<String[]> ls = new ArrayList<String[]>();
					TextboxForProfileLarge ed;
					TextView tv;
					if(_item.distribution.equals("DECEN") &&
							_address != null && _address.size() > 0){
						tv = getIndexOfOptionTextView(0);
						ed = getIndexOfOptionEditView(0);
						ls.add(new String[]{
								tv.getText().toString().trim()
								, ed.getEditContent().trim()});
					}
					if(_architecture.equals("complex") &&
							_ext_structure != null && _ext_structure.size() >0){
						int index = _item.distribution.equals("DECEN")? 1 : 0;
						for(int i = 0; i<_ext_structure.size(); i++, index++){
							tv = getIndexOfOptionTextView(index);
							ed = getIndexOfOptionEditView(index);
							ls.add(new String[]{
											tv.getText().toString().trim()
											, ed.getEditContent().trim()});
						}
					}
					
					intent.putExtra("opt", ls);
				}
				startActivity(intent);
			}
		});
		
		_lpTextView.setMargins(0, 0, 0, 20);
		_lpEditView.setMargins(15, 0, 0, 20);
	}
	private int verifyOption(){
		int ret = -99 ;
		String lab = "";
		TextboxForProfileLarge ed;
		if(_architecture.equals("complex") &&
				_ext_structure != null && _ext_structure.size() >0){
			int index = _item.distribution.equals("DECEN") ? 1 : 0;
			for(int i = 0; i<_ext_structure.size(); i++, index++){
				ed = getIndexOfOptionEditView(index);
				if(ed != null){
					lab = String.format("%s %s",  lab.trim(),ed.getEditContent().trim());
					Vlog.getInstance().debug(false, TAG, lab);
				}
			}
		}

			Vlog.getInstance().debug(false, TAG, "address id: " + _address.get(_selectedAddressIndex)[0] +", value:" + _address.get(_selectedAddressIndex)[1]);
			for(int j = 0; j < _ext_output.size(); j++){
				if(_architecture.equals("complex")){
					if(_ext_output.get(j).label.trim().equals(lab)){
						if(_item.distribution.equals("CENTRAL")){
							if(_ext_output.get(j).count > 0){
								ret = _ext_output.get(j).extension;
								break;
							}
						}else if(_item.distribution.equals("DECEN")){
							if(_address.get(_selectedAddressIndex)[0].equals(String.valueOf( _ext_output.get(j).location_id))){
								if(_ext_output.get(j).count > 0){
									ret = _ext_output.get(j).extension;
									break;
								}
							}
						}else{
							
						}
					}
				}else if(_architecture.equals("simple")){
					if(_item.distribution.equals("CENTRAL")){
						if(_ext_output.get(j).count > 0){
							ret = _ext_output.get(j).extension;
							break;
						}
					}else if(_item.distribution.equals("DECEN")){
						if(_address.get(_selectedAddressIndex)[0].equals(String.valueOf( _ext_output.get(j).location_id))){
							if(_ext_output.get(j).count > 0){
								ret = _ext_output.get(j).extension;
								break;
							}
						}
					}else{
						
					}
				}
				
				
			}
			if(ret < -98){
				showToast.getInstance(getApplicationContext())
				.showMsg(getString(R.string.redemptionActivity_notStock));
			}
		
		
		return ret;
	}
	private void setOption2(final TextboxForProfileLarge ed){
		View outerView = LayoutInflater.from(RedemptionOptionActivity.this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
		wv.setItems(_addressValue);
		_selectedOption2TempIndex = _selectedAddressIndex;
		wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
            public void onSelected(int selectedIndex, String item) {
                Vlog.getInstance().debug(false,TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                _selectedOption2TempIndex = selectedIndex -1;
            }
        });
		new AlertDialog.Builder(RedemptionOptionActivity.this)
        .setView(outerView)
        .setPositiveButton(getString(R.string.global_ok), new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_selectedAddressIndex = _selectedOption2TempIndex;
//				_selectedAddressKey = _address.get(_selectedAddressIndex);
				Vlog.getInstance().debug(false, TAG,  "/ key:"+_address.get(_selectedAddressIndex)[0] + "~selected OPTION2 index :" + _selectedAddressIndex+ " / "+  _addressValue.get(_selectedAddressIndex) );
				ed.setContentEnable(_addressValue.get(_selectedAddressIndex), false);
			}
        }) 
        .show();
		 wv.setSeletion(_selectedAddressIndex);
		 Vlog.getInstance().debug(false, TAG, "org selected index: " + _selectedAddressIndex + " / "+ _addressValue.get(_selectedAddressIndex));
	}
//	private void setOption1(){
//		View outerView = LayoutInflater.from(RedemptionOptionActivity.this).inflate(R.layout.wheel_view, null);
//        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
//		wv.setItems(_item.extensions);
//
//		_selectedOption1TempIndex = _selectedOptionExtenstionIndex;
//		
//		wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//			@Override
//            public void onSelected(int selectedIndex, String item) {
//                Vlog.getInstance().debug(false,TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                _selectedOption1TempIndex = selectedIndex -1;
//            }
//        });
//		new AlertDialog.Builder(RedemptionOptionActivity.this)
//        .setView(outerView)
//        .setPositiveButton(getString(R.string.global_ok), new  DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Vlog.getInstance().debug(false, TAG, "~~~~selected OPTION1 index :" + _selectedOption1TempIndex+ " / "+  _item.extensions.get(_selectedOption1TempIndex));
//				if(checkStockCount(_selectedOption1TempIndex)){
//					_selectedOptionExtenstionIndex = _selectedOption1TempIndex;
//					_edOption1.setContentEnable(_item.extensions.get(_selectedOptionExtenstionIndex), false);
//				}else{
//					_edOption1.setContentEnable(_item.extensions.get(_selectedOption1TempIndex), false);
//				}
//			}
//        }) 
//        .show();
//		 wv.setSeletion(_selectedOptionExtenstionIndex);
//		 Vlog.getInstance().debug(false, TAG, "org selected index: " + _selectedOptionExtenstionIndex + " / "+ _item.extensions.get(_selectedOptionExtenstionIndex));
//	
//	}
//	private boolean checkStockCount(int index){
//		RewardDetailStockCount item = null;
//		if(_item.stockCount == null)
//			return false;
//		for(int i =0 ; i< _item.stockCount.size(); i++){
//			item = _item.stockCount.get(i);
//			if(item.extension.equals(String.valueOf(index))){
//				if(Integer.valueOf(item.count) > 0){
//					Vlog.getInstance().info(false, TAG, "extesion:" + item.extension + ", count:"+item.count);
//					return true;
//				}
//				else{
//					showToast.getInstance(getApplicationContext())
//						.showMsg(getString(R.string.redemptionActivity_countNoEnough));
//					return false;
//				}
//			}
//		}
//		showToast.getInstance(getApplicationContext())
//			.showMsg(getString(R.string.redemptionActivity_notStock));
//		return false;
//	}

	@Override
	protected void parseData(JSONObject response) {
		_address = null;
		_ext_structure = null;
		_ext_output = null;
		boolean isSucceed = false;
		if(response != null){
			try {
				int resultCode = response.getInt("result");
				if(resultCode == 1){
					JSONObject data = response.getJSONObject("data");
					if(data != null){
						_architecture = data.getString("architecture");
						JSONArray aryOutput = data.getJSONArray("ext_output");
						if(aryOutput != null){
							_ext_output = new ArrayList<RedeemExtensionOutputStruct>();
							RedeemExtensionOutputStruct outItem = null;
							for(int i = 0; i< aryOutput.length(); i++){
								if(_architecture.equals("simple")){
									outItem = new RedeemExtensionOutputStruct(
											  ""
											, aryOutput.getJSONObject(i).getInt("extension")
//											, aryOutput.getJSONObject(i).getString("distribution")
											, aryOutput.getJSONObject(i).getInt("count")
											, aryOutput.getJSONObject(i).getInt("location_id")
											);
								}else{
									outItem = new RedeemExtensionOutputStruct(
											  aryOutput.getJSONObject(i).getString("label")
											, aryOutput.getJSONObject(i).getInt("extension")
//											, aryOutput.getJSONObject(i).getString("distribution")
											, aryOutput.getJSONObject(i).getInt("count")
											, aryOutput.getJSONObject(i).getInt("location_id")
											);
								}
								
								_ext_output.add(outItem);
							}
						}
						if(data.has("ext_structure")){
							JSONArray aryStruct =  data.getJSONArray("ext_structure");
							if(aryStruct != null){
//								_ext_structure = new HashMap<String, RedeemExtensionOptionStruct>();
								_ext_structure = new ArrayList<RedeemExtensionOptionStruct>();
								RedeemExtensionOptionStruct sItem = null;
								ArrayList<String> tags = null;
								JSONArray jTags = null;
								for(int j = 0; j < aryStruct.length(); j++){
									tags = new ArrayList<String>();
									jTags = aryStruct.getJSONObject(j).getJSONArray("tags");
									if(jTags != null){
										for(int jj = 0; jj < jTags.length(); jj++){
											tags.add(jTags.getString(jj));
										}
									}
									sItem = new RedeemExtensionOptionStruct(
											Html.fromHtml(aryStruct.getJSONObject(j).getString("label_new_name")).toString()
											, aryStruct.getJSONObject(j).getString("id")
											, aryStruct.getJSONObject(j).getInt("order")
											, tags);
//									_ext_structure.put(String.valueOf(aryStruct.getJSONObject(j).getInt("order")) 
//											, sItem);
									_ext_structure.add(sItem);
								}
							}
						}
						
						if(data.has("addresses")){
							JSONObject aryAddress = data.getJSONObject("addresses");
							if(aryAddress != null){
								@SuppressWarnings("unchecked")
								Iterator<String> keyIter = aryAddress.keys();
								String key = null;
								String value = null;
//								_addresses = new HashMap<String, String>();
								_address = new ArrayList<String[]>();
								_addressValue = new ArrayList<String>();
								while(keyIter.hasNext()){
									key = (String)keyIter.next();
									value = aryAddress.getString(key);
//									_addresses.put(key, value);
									_address.add(new String[]{key,value});
									_addressValue.add(Html.fromHtml(value).toString());
								}
							}
						}
						
						_item.distribution = data.getString("distribution");
					}
					isSucceed = true;
				}else{
					showToast.getInstance(getApplicationContext()).showMsg(
							response.getString("msg"));
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext())
					.showMsg(e.getMessage());
				e.printStackTrace();
			}

		}
		refreshUI(isSucceed);
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			setAddressView();
			if(_architecture.equals("complex"))
				setStructView();
			else if(_architecture.equals("simple")){
				
			}
		}
		if(_dialog != null && _dialog.isShowing())
			_dialog.dismiss();
	}

	private void setAddressView() {
		if(_item.distribution.equals("CENTRAL"))
			return;
		else if(_item.distribution.equals("DECEN")){
			if (_address != null && _address.size() > 0) {
				final TextView tv = getIndexOfOptionTextView(0);
				final TextboxForProfileLarge ed = getIndexOfOptionEditView(0);
				if(tv == null || ed == null)
					return;
				tv.setText(getString(R.string.redemptionActivity_address));
//				Iterator iter = _addresses.entrySet().iterator();
//				_addressKey = new ArrayList<String>();
//				_addressValue = new ArrayList<String>();
//				while (iter.hasNext()) {
//					@SuppressWarnings("unchecked")
//					Entry<String, String> entry = (Entry<String, String>) iter
//							.next();
//					_addressKey.add(entry.getKey());
//					_addressValue.add(entry.getValue());
//				}
				
				
//				if (_addressKey.size() == 1
//						|| _item.distribution_type.equals("perpetual")) {
////					ed.setContentEnable(_addressValue.get(0), false);
////					_selectedAddressKey = _addressKey.get(0);
//				} else {
//					ed.setArrowShow(true);
////					ed.setContentEnable(_addressValue.get(_selectedAddressIndex), false);
//					ed.setOnCustomClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							setOption2(ed);
//						}
//					});
//				}
				_selectedAddressIndex = 0;
//				if(_item.distribution.equals("CENTRAL")){
//					ed.setContentEnable(_item.note_2, false);
//				}else{
//					if(_address.size() > 1){
//						ed.setArrowShow(true);
//						ed.setOnCustomClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								setOption2(ed);
//							}
//						});
//					}
//					ed.setContentEnable(_addressValue.get(0), false);
//				}
				if(_address.size() > 1){
					ed.setArrowShow(true);
					ed.setOnCustomClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							setOption2(ed);
						}
					});
				}
				ed.setContentEnable(_addressValue.get(0), false);
				
				
//				_selectedAddressKey = _addressKey.get(0);
				
				
//				tv.setVisibility(View.VISIBLE);
//				ed.setVisibility(View.VISIBLE);
				
			}
		}else 
			showToast.getInstance(getApplicationContext()).showMsg("unknown item distribution:" + _item.distribution);
		

	}
	private void setStructView(){
		if(_ext_structure != null && _ext_structure.size() >0){
			TextView tv = null;
			RedeemExtensionOptionStruct struct = null;
			String name = null;

			int index = _item.distribution.equals("DECEN") ? 1 : 0;
			for(int i = 0; i < _ext_structure.size(); i++, index++){
				struct = _ext_structure.get(i);
				if(struct != null){
					name = struct.label_new_name;
					final ArrayList<String> aryTags = struct.tags;
					tv = getIndexOfOptionTextView(index);
					if(tv != null){
						tv.setText(name);
					}
					final TextboxForProfileLarge ed = getIndexOfOptionEditView(index);
					if(ed != null){
						if(aryTags != null && aryTags.size() > 0){
							ed.setContentEnable(aryTags.get(0), false);
							if(aryTags.size() > 1){
								ed.setArrowShow(true);
								ed.setOnCustomClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										setOption(aryTags, ed);
									}
								});
							}
							
						}
					}
				}
			}
		}
		
	}
	
	@Override
	protected void getData() {
		HttpRequest request = HttpRequestHelper.getGetRequest(
				Constants.Url.getRedeemExtension(_item.Id, GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "redeemextension resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "redeemextension response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				parseData(null);
			}
		},"redeemextension");
	}
	
	ArrayList<TextView> _lsTextView = new ArrayList<TextView>();
	
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
			TextPaint tp = tv.getPaint(); 
			tp.setFakeBoldText(true); 
			tv.setTextColor(getResources().getColor(R.color.global_Text_Title));
			tv.setTag(index);
			_optionLayout.addView(tv);
			_lsTextView.add(tv);
			return tv;
		}else{
			return _lsTextView.get(index);
		}
		
	}
	ArrayList<TextboxForProfileLarge> _lstEditView = new ArrayList<TextboxForProfileLarge>();
	private TextboxForProfileLarge getIndexOfOptionEditView(int index){
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
			final TextboxForProfileLarge ev = new TextboxForProfileLarge(this, null);
			ev.setLayoutParams(_lpEditView);
			ev.setTag(index);
			_optionLayout.addView(ev);
			_lstEditView.add(ev);
			return ev;
		}else{
			return _lstEditView.get(index);
		}
		
	}
//	int selected = 0;
	int selectedWheelView = 0;
	private void setOption(final ArrayList<String> tags, final TextboxForProfileLarge edOption){
		View outerView = LayoutInflater.from(RedemptionOptionActivity.this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
		wv.setItems(tags);
		selectedWheelView = 0;
		for(int kk = 0; kk < tags.size(); kk++){
			if(tags.get(kk).trim().equals(edOption.getEditContent().trim())){
				selectedWheelView = kk;
				break;
			}
		}
		wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
            public void onSelected(int selectedIndex, String item) {
                Vlog.getInstance().debug(false,TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                _selectedOption1TempIndex = selectedIndex -1;
                selectedWheelView = selectedIndex -1;
            }
        });
		new AlertDialog.Builder(RedemptionOptionActivity.this)
        .setView(outerView)
        .setPositiveButton(getString(R.string.global_ok), new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Vlog.getInstance().debug(false, TAG, "~~~~selected OPTION index :" + _selectedOption1TempIndex+ " / "+  _item.extensions.get(_selectedOption1TempIndex));
//				if(checkStockCount(_selectedOption1TempIndex)){
//					_selectedOptionExtenstionIndex = _selectedOption1TempIndex;
//					_edOption1.setContentEnable(_item.extensions.get(_selectedOptionExtenstionIndex), false);
//				}else{
//					_edOption1.setContentEnable(_item.extensions.get(_selectedOption1TempIndex), false);
//				}
				edOption.setContentEnable(tags.get(selectedWheelView), false);
			}
        }) 
        .show();
		 wv.setSeletion(selectedWheelView);
//		 Vlog.getInstance().debug(false, TAG, "org selected index: " + _selectedOptionExtenstionIndex + " / "+ _item.extensions.get(_selectedOptionExtenstionIndex));
	
	}

}
