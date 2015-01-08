package com.imusic.rewallet.UI;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.model.RedemptionHistoryItem;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.widgets.BtnReversalHighlight;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class MyRewardDetailClaimedActivity extends BaseNavibarActivity {
	private final static String TAG = "RewardDetailActivity";
	
//	private int _Id;
//	private String _lang;
	private RedemptionHistoryItem _item;
	
	private BtnReversalHighlight _btnShare;
	private BtnReversalHighlight _btnComment;
	private ImageView _imgBanner;
	private TextView _tvVendor;
	private TextView _tvExpDate;
	private TextView _tvCoinCount;
	private TextView _tvDescription;
//	private TextView _tvCountryDesc;
//	private ImageView _imgCategory;
//	private TextView _lbCategory;
//	private TextView _lbCountry;
	private RelativeLayout _layout;
	private ScrollView _scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reward_detail_claimed);
		if (getIntent().getExtras() != null) {

			_item = (RedemptionHistoryItem)getIntent().getExtras().get("item");
			if(_item != null){
				setupView();
			}else
				finish();
		}else
			finish();
	}
	private void setupView(){
		setupActionBar(_item.productName, true, false, null, null);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
//		_layout = (RelativeLayout)findViewById(R.id.myRewardDetail_layout);
//		_layout.addView(_cover);
//		_lbCategory = (TextView)findViewById(R.id.myRewardDetail_tvCategory);
//		_lbCountry = (TextView)findViewById(R.id.myRewardDetail_tvCountry);
		_imgBanner = (ImageView)findViewById(R.id.myRewardDetail_imgTop);
		_imgBanner.setLayoutParams(new RelativeLayout.LayoutParams(metric.widthPixels, metric.widthPixels*10/16));
		_tvVendor = (TextView)findViewById(R.id.myRewardDetail_tvVendor);
		_tvExpDate = (TextView)findViewById(R.id.myRewardDetail_tvExpDate);
		_tvCoinCount = (TextView)findViewById(R.id.myRewardDetail_tvCoinCount);
		_tvDescription = (TextView)findViewById(R.id.myRewardDetail_tvMainDescription);
//		_tvCountryDesc = (TextView)findViewById(R.id.myRewardDetail_tvCountryDesc);
		_btnShare = (BtnReversalHighlight)findViewById(R.id.myRewardDetail_btnShare);
		_btnShare.setContent(getString(R.string.appDownloadsDetaillActivity_share), String.valueOf( _item.shareCount), R.drawable.redemptionhistory_detal_share_icon_unpressed);
		_btnComment = (BtnReversalHighlight)findViewById(R.id.myRewardDetail_btnComment);
		_btnComment.setContent(getString(R.string.appDownloadsDetaillActivity_comment), String.valueOf(_item.commentCount), R.drawable.redemptionhistory_detal_comment_icon_unpressed);
//		_imgCategory = (ImageView)findViewById(R.id.myRewardDetail_imgCategory);
		
		
//		_lbCategory.setText(_item.category);
//		_lbCountry.setText(_item.country);
		_tvVendor.setText(_item.vendor == null ? "": Html.fromHtml(_item.vendor));
		_tvExpDate.setText(String.format("%s %s", getString(R.string.global_calimedDate)
				, _item.expDate == null? "" : new SimpleDateFormat("yyyy/MM/dd").format(_item.expDate)));
		_tvCoinCount.setText(String.valueOf(_item.amount));
		_tvDescription.setText(Html.fromHtml(_item.description));
//		_tvCountryDesc.setText(_item.countryDescription);
		ImageLoader.getInstance().displayImage(_item.imageBanner, _imgBanner, Util.getImageLoaderOptionForDetail(getApplicationContext()));
//		ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory, Util.getImageLoaderOptionForDetail(getApplicationContext()));
		
//		Bitmap bitmap = QRCodeUtil.createQRCodeImage(_item.qr_a, metric.widthPixels/2, metric.widthPixels/2);
//		_imgQRCode.setImageBitmap(bitmap);
	}
	@Override
	protected void parseData(JSONObject response) {
//		try {
//			int resultCode = response.getInt("result");
//			if(resultCode != 1){
//				refreshUI(false);
//			}else{
//				_item = new RewardDetailItem();
//				JSONObject data = response.getJSONObject("data");
//				if(data != null){
//					_item.redemption_lock = data.getInt("redemption_lock");
//					_item.share_count = data.getInt("share_count");
//					_item.remarks =  data.getString("remarks");
//					_item.product_description = data.getString("product_description");
//					_item.distribution_type = data.getString("distribution_type");
//					_item.vcoin_value = data.getInt("vcoin_value");
//					_item.redemption_procedure = data.getString("redemption_procedure");
//					_item.title = data.getString("title");
//					_item.video_url = data.getString("video_url");
//					_item.note_1 = data.getString("note_1");
//					_item.note_2 = data.getString("note_2");
//					_item.note_3 = data.getString("note_3");
//					_item.note_4 = data.getString("note_4");
//					_item.vendor_name = data.getString("vendor_name");
//					_item.comment_count = data.getInt("comment_count");   //string??
//					_item.image_video_cover = data.getString("image_video_cover");
//					_item.tnc = data.getString("tnc");
//					_item.stock_id = data.getInt("stock_id");
//					_item.product_url = data.getString("product_url");
//					_item.vendor_id = data.getInt("vendor_id");
//					_item.image_slider = data.getString("image_slider");
//					_item.expiration_date = data.getString("expiration_date");
//					_item.image_small_thumb = data.getString("image_small_thumb");
//					_item.redeem_count = data.getInt("redeem_count");
//					_item.image_banner = data.getString("image_banner");
//					
//					_item.category = "categoryA";//data.getString("category");
//					_item.categoryImage = "";
//					
//					JSONArray aryCountry = data.getJSONArray("country");
//					if(aryCountry != null){
////						for(int i = 0; i < aryCountry.length(); i++){
////							
////						}
//						JSONObject country = aryCountry.getJSONObject(0);
//						if(country != null){
//							_item.country = country.getString("name");
//							_item.countryDescription = country.getString("description");
//						}
//					}
//					
//					
//					//_item.addresses = data.getJSONObject(0).getString("addresses");
//					
////					_item.comment = data.getJSONObject(0).getString("product_description");
////					_item.country = data.getJSONObject(0).getString("product_description");
////					_item.stock_count = data.getJSONObject(0).getString("product_description");
////					_item.extensions = data.getJSONObject(0).getString("extensions");	
//					refreshUI(true);
////					_handler.obtainMessage(MSG_REFREASHVIEW, true).sendToTarget();
//				}
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//			e.printStackTrace();
//		}
	}
	@Override
	protected void refreshUI(boolean isSuccess) {
//		if(isSuccess){
//			setupActionBar(_item.title, true, false, "", null);
//			ImageLoader.getInstance()
//			.displayImage(_item.image_banner, _imgBanner);
//			//todo: reset image banner size??
////			global_vendor_by
//			_tvVendor.setText(getString(R.string.global_vendor_by) + _item.vendor_name);
//			_tvCoinCount.setText(String.valueOf(_item.vcoin_value));
//			_tvDescription.setText(Html.fromHtml(_item.product_description));
//			_tvExpDate.setText(getString(R.string.global_expDate) + " " + _item.expiration_date);
//			
//			_tvCountryDesc.setText(_item.countryDescription);
//			_lbCategory.setText(_item.category);
//			_lbCountry.setText(_item.country);
////			_layout.removeView(_cover);
////			_scrollView.scrollBy(0, -50);
//			
//		}else{
//			
//		}
	}
	@Override
	protected void getData() {
//		Log.i(TAG, "request: " + Constants.Url.getRewardDetailUrl(_Id, _lang));
//    	HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getRewardDetailUrl(_Id, _lang));
//		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
//			@Override  
//            protected void onSuccess(JSONObject response, String tag){ 
//				Vlog.getInstance().info(false,TAG, "reward detail resposnse json succeed : "+response.toString());
//				parseData(response);
//			}
//			@Override  
//            protected void onFailure(Throwable e, String tag) { 
//				Vlog.getInstance().info(false,TAG,  "reward detail  response json failed." + e.toString());
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//			}
//		},"");
	}

}
