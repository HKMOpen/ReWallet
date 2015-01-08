package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RewardCommentItem;
import com.imusic.rewallet.model.RewardDetailItem;
import com.imusic.rewallet.utils.CategoryCountryUtil;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.DataProcessCover;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.RewardDetailVideoPlayer;
import com.imusic.rewallet.widgets.RewardDetailVideoPlayer.VideoPlayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RewardDetailActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "RewardDetailActivity";
	//private DisplayMetrics _metric = new DisplayMetrics();
	//private HashMap<String, String> _lstInfo;
	
	public static final int TYPE_REWARD = 30005;
	public static final int TYPE_COUPON = 30006;
	
	private ArrayList<RewardCommentItem> _lstComment ;
	private RewardDetailItem _item;
	private int _Id;
	private String _lang;
	private int _type;
	
	private RewardDetailVideoPlayer _videoPlayer;
//	private BtnReversalHighlight _btnShare;
//	private BtnReversalHighlight _btnComment;
//	private BtnHighlightReversal _btnRedeem;
//	private ImageView _imgBanner;
//	private TextView _tvVendor;
//	private TextView _tvExpDate;
	private TextView _tvCoinCount;
	private TextView _tvDescription;
	private Button _btnRedeem;
//	private TextView _tvAddtionalDescription;
//	private TextView _tvCountryDesc;
//	private ImageView _imgCategory;
//	private TextView _lbCategory;
//	private TextView _lbCountry;
	private RelativeLayout _layout;
	private View _rewardDetail_redeemCover;
//	private ScrollView _scrollView;
//	private ImageButton _imgbtnMyVcoin;
//	private ImageButton _imgbtnMyProfile;
//	private RewardDetailCommentListView _lvComment;
//	private RewardDetailCommentListAdapter _commentAdapter;
	
	private boolean _refreshPortion = false;
	private static final int RESULTCODE = 100021;
	
	private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForDetail();
	
	public static RewardDetailActivity Instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setupData();
		setContentView(R.layout.activity_reward_detail);
		_imageOptions = Util.getImageLoaderOptionForDetail(getApplicationContext());
		if (getIntent().getExtras() != null) {
			// RewardListItem item = (RewardListItem)
			// getIntent().getExtras().get("reward");
			_type = getIntent().getExtras().getInt("type");
			if (_type != TYPE_COUPON && _type != TYPE_REWARD)
				finish();
			else {
				_Id = getIntent().getExtras().getInt("id");
				_lang = getIntent().getExtras().getString("lang");
				if (_Id != 0) {
					_cover = new DataProcessCover(getApplicationContext(), null);
					setupView();
					Instance = this;
					_handler.obtainMessage(MSG_GETDATA).sendToTarget();

				} else
					finish();
			}
		} else
			finish();
	}
	
	private void setupView(){
		setupActionBar("", true, false, "", null);
		_refreshPortion = false;
		_lstComment = new ArrayList<RewardCommentItem>();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		_layout = (RelativeLayout)findViewById(R.id.rewardDetail_layout);
		_rewardDetail_redeemCover = (View)findViewById(R.id.rewardDetail_redeemCover); //rewardDetail_redeemCover
//		_layout.addView(_cover);
//		_scrollView = (ScrollView)findViewById(R.id.rewardDetail_scrollView);
		
//		_lbCategory = (TextView)findViewById(R.id.rewardDetail_tvCategory);
//		_lbCountry = (TextView)findViewById(R.id.rewardDetail_tvCountry);
		
//		_imgbtnMyVcoin = (ImageButton)findViewById(R.id.rewardDetail_imgbtnMyVcoin);
//		_imgbtnMyVcoin.setOnClickListener(this);
//		_imgbtnMyProfile = (ImageButton)findViewById(R.id.rewardDetail_imgbtnMyProfile);
//		_imgbtnMyProfile.setOnClickListener(this);
//		_imgBanner = (ImageView)findViewById(R.id.rewardDetail_imgTop);
//		_imgBanner.setLayoutParams(new RelativeLayout.LayoutParams(metric.widthPixels, metric.widthPixels*9/16));
		//setupImage(imgTop,_lstInfo.get("topimg"));
		
//		_imgCategory = (ImageView)findViewById(R.id.rewardDetail_imgCategory);
		
//		_tvVendor = (TextView)findViewById(R.id.rewardDetail_tvVendor);
		//txVendor.setText(_lstInfo.get("vendor"));
//		_tvExpDate = (TextView)findViewById(R.id.rewardDetail_tvExpDate);
		_tvCoinCount = (TextView)findViewById(R.id.rewardDetail_tvCoinCount);
		//txCoinCount.setText(_lstInfo.get("coincount"));
		_tvDescription = (TextView)findViewById(R.id.rewardDetail_tvMainDescription);
		//tvDescription.setText(_lstInfo.get("maindescription"));
		
		//ImageView imgCenter = (ImageView)findViewById(R.id.rewardDetail_imgCenter);
		//setupImage(imgCenter,_lstInfo.get("centerimg"));
//		_tvAddtionalDescription = (TextView)findViewById(R.id.rewardDetail_tvAddtionalDescription);
		//tvAddtionalDescription.setText(_lstInfo.get("addtionaldescription"));
//		_tvCountryDesc = (TextView)findViewById(R.id.rewardDetail_tvCountryDesc);
//		_lvComment = (RewardDetailCommentListView)findViewById(R.id.rewardDetail_lstComments);
//		_commentAdapter = new RewardDetailCommentListAdapter(getApplicationContext(),_lstComment);
//		_lvComment.setAdapter(_commentAdapter); 
		//ListViewUtility.setListViewHeightBasedOnChildren(lvComment);
		
		_videoPlayer = (RewardDetailVideoPlayer)findViewById(R.id.rewardDetail_videoplayer);
		
		_btnRedeem = (Button)findViewById(R.id.rewardDetail_btnRedeem);
//		_btnRedeem.setContent(getString(R.string.rewardDetaillActivity_Redeem), "0", R.drawable.rewarddetail_redeemicon_white,R.drawable.rewarddetail_redeemicon);
		
		_btnRedeem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(_item.redemption_lock == 1){
					showToast.getInstance(getApplicationContext())
						.showMsg(getString(R.string.rewardDetaillActivity_CannotRedeem));
				}else{
					if(_type == TYPE_REWARD && _item.totalcount <= 0){
						showToast.getInstance(getApplicationContext()).showMsg(getString(R.string.redemptionActivity_countNoEnough));
						return;
					}
					
//					ArrayList<Activity> lst = new ArrayList<Activity>();
//					lst.add(RewardDetailActivity.this);
					Intent intent = new Intent();
					intent.setClass(RewardDetailActivity.this, RedemptionActivity.class);
					intent.putExtra("item", _item);
					intent.putExtra("type", _type);
//					intent.putExtra("lstActivity", lst);
//					intent.putParcelableArrayListExtra("lst", lst);
					startActivity(intent);
				}
				
			}
		});
//		
//		
//		_btnRedeem.setEnabled(false);
		
		
//		_btnShare = (BtnReversalHighlight)findViewById(R.id.rewardDetail_btnShare);
//		_btnShare.setContent(getString(R.string.appDownloadsDetaillActivity_share), "0", R.drawable.redemptionhistory_detal_share_icon_unpressed);
//		_btnComment = (BtnReversalHighlight)findViewById(R.id.rewardDetail_btnComment);
//		_btnComment.setContent(getString(R.string.appDownloadsDetaillActivity_comment), "0", R.drawable.redemptionhistory_detal_comment_icon_unpressed);
		
//		_btnShare.setCustomOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ShareCommentBtnUtil.onShare(RewardDetailActivity.this
//						, ShareCommentBtnUtil.TYPE_REWARD
//						, _item.title
//						, _item.image_banner
//						, _item.Id
//						, RESULTCODE);
//			}
//		});
//		_btnComment.setCustomOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(_item == null && _item.stock_id <= 0)
//					return;
//				ShareCommentBtnUtil.onComment(RewardDetailActivity.this
//						,_item.stock_id
//						, ShareCommentBtnUtil.TYPE_REWARD
//						, _item.title
//						, _item.image_banner
//						, _lstComment,RESULTCODE);
//			}
//		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
//		case R.id.rewardDetail_imgbtnMyVcoin:
//
//			OverlayBtnUtil.myVcoinClick(this);
//			finish();
//			break;
//		case R.id.rewardDetail_imgbtnMyProfile:
////			OverlayBtnUtil.myProfileClick(this);
////			finish();
//			break;
		
		}
	}
	@Override
	protected void getData() {
		_layout.addView(_cover);
    	HttpRequest request = null ;
    	if(_type == TYPE_COUPON){
    		request = HttpRequestHelper.getGetRequest(Constants.Url.getCouponDetailUrl(_Id));
    	}else{
    		request = HttpRequestHelper.getGetRequest(Constants.Url.getRewardDetailUrl(_Id, _lang));
    	}
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "reward detail resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "reward detail  response json failed." + e.toString());
				parseData(null);
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"");
	}
	
	@Override
	protected void parseData(JSONObject response) {
		boolean isSucceed = false;
		if (response != null) {
			try {
				int resultCode = response.getInt("result");
				if (resultCode != 1) {
					showToast.getInstance(getApplicationContext()).showMsg(
							response.getString("msg"));
					refreshUI(false);
				} else {
					_item = new RewardDetailItem();
					_item.Id = _Id;
					JSONObject data = response.getJSONObject("data");
					if (data != null) {
						if (_type != TYPE_COUPON) {
							JSONArray aryComment = data.getJSONArray("comment");
							if (aryComment != null) {
								RewardCommentItem cItem = null;
								String username = " ";
								String content = null;
								int id = 0;
								_lstComment = new ArrayList<RewardCommentItem>();
								for (int j = 0; j < aryComment.length(); j++) {
									id = aryComment.getJSONObject(j).getInt(
											"ID");
									username = aryComment.getJSONObject(j)
											.getString("name");
									content = aryComment.getJSONObject(j)
											.getString("comment");
									cItem = new RewardCommentItem(id, username== null? null: Html.fromHtml(username).toString(),
											content== null? null:Html.fromHtml(content).toString());
									_lstComment.add(cItem);
									Vlog.getInstance().info(false, TAG,
											"commend added:" + content);
								}
//								_commentAdapter.resetListData(_lstComment);
							}
							_item.redeem_count = data.getInt("redeem_count");
							_item.totalcount = data.getInt("totalcount");

//							//extensions;
//							ArrayList<String> ext = new ArrayList<String>();
//							JSONArray aryExt = data.getJSONArray("extensions");
//							if(aryExt != null){	
//								for(int b = 0; b < aryExt.length(); b++){
//									ext.add(aryExt.getString(b));
//								}
//								_item.extensions = ext;
//							}else{
//								_item.extensions = null;
//							}
							_item.redemption_lock = data.getInt("redemption_lock");
							_item.distribution = data.getString("distribution");
							_item.note_1 = data.getString("note_1");
							_item.note_2 = data.getString("note_2");
							_item.note_3 = data.getString("note_3");
							_item.note_4 = data.getString("note_4");
							_item.comment_count = data.getInt("comment_count"); // string??
							_item.share_count = data.getInt("share_count");
							_item.stock_id = data.getInt("stock_id");
							_item.redemption_procedure = data
									.getString("redemption_procedure");
							
							_item.vcoin_value = data.getInt("vcoin_value");
							
							_item.vendor_id = data.getInt("vendor_id");    //
							_item.vendor_name = data.getString("vendor_name"); // TODO
							//stock count;
//							JSONArray aryStock = data.getJSONArray("stock_count");
//							if(aryStock != null){
//								ArrayList<RewardDetailStockCount> st = new ArrayList<RewardDetailStockCount>();
//								RewardDetailStockCount sitem = null;
//								for(int k = 0; k < aryStock.length(); k++){
//									sitem = new RewardDetailStockCount();
//									sitem.ID = aryStock.getJSONObject(k).getString("ID");
//									sitem.label = aryStock.getJSONObject(k).getString("label");
//									sitem.stock_id = aryStock.getJSONObject(k).getString("stock_id");
//									sitem.extension = aryStock.getJSONObject(k).getString("extension");
//									sitem.distribution = aryStock.getJSONObject(k).getString("distribution");
//									sitem.count = aryStock.getJSONObject(k).getString("count");
//									sitem.location_id = aryStock.getJSONObject(k).getString("location_id");
////									sitem.qr = aryStock.getJSONObject(k).getString("qr");
////									sitem.extension_id = aryStock.getJSONObject(k).getString("extension_id");
//									st.add(sitem);
//								}
//								_item.stockCount = st;
//							}
							// address;
//							JSONObject aryAddress = data.getJSONObject("addresses");
//							if(aryAddress != null){
//								@SuppressWarnings("unchecked")
//								Iterator<String> keyIter = aryAddress.keys();
//								String key = null;
//								String value = null;
//								HashMap<String, String> map = new HashMap<String, String>();
//								while(keyIter.hasNext()){
//									key = (String)keyIter.next();
//									value = aryAddress.getString(key);
//									map.put(key, value);
//								}
//								_item.addresses = map;
//							}else{
//								_item.addresses = null;
//							}
						
						}else{
							_item.vcoin_value = data.getInt("vcoin");
							_item.redemption_lock = 0;
							_item.stock_id = _Id;
						}

						_item.remarks = data.getString("remarks");
						_item.product_description = data
								.getString("product_description");
						
						
						_item.title = data.getString("title");
						_item.video_url = data.getString("video_url");

						_item.image_banner = data.getString("image_banner");
						_item.image_video_cover = data
								.getString("image_video_cover");
						_item.tnc = data.getString("tnc");

						_item.product_url = data.getString("product_url");

						_item.image_slider = data.getString("image_slider");
						_item.expiration_date = data
								.getString("expiration_date");
						_item.image_small_thumb = data
								.getString("image_small_thumb");
						JSONArray aryCountry = data.getJSONArray("country");
						String[] cc = CategoryCountryUtil.getCountryDescription(getApplicationContext(),
								MainActionBar.MAINACTIONBAR_REWARDS ,aryCountry
								,"name","description");
						_item.country = cc[0];
						_item.countryDescription = cc[1];
						
						JSONArray aryCategory = data.getJSONArray("category");
						String[] cat = CategoryCountryUtil.getCategory(getApplicationContext(),
								MainActionBar.MAINACTIONBAR_REWARDS , aryCategory,
								"id","name","unpress_s");
						_item.category = cat[0];
						_item.categoryImage = cat[1];
						
						isSucceed = true;
					}
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(
						e.getMessage());
				e.printStackTrace();
			}
		}
		if (_refreshPortion) {
			updatePortion();
		} else
			refreshUI(isSucceed);
	}

	private void updatePortion(){
//		_btnComment.resetCount(_item.comment_count);
//		_btnRedeem.resetCount(_item.redeem_count);
//		_btnShare.resetCount(_item.share_count);
//		_commentAdapter.notifyDataSetChanged();
		_layout.removeView(_cover);
	}
	
	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			setupActionBar(Html.fromHtml(_item.title).toString(), true, false, "", null);
//			ImageLoader.getInstance()
//			.displayImage(_item.image_banner, _imgBanner, _imageOptions);
			//todo: reset image banner size??
//			global_vendor_by
//			_tvVendor.setText(_item.vendor_name == null? "": getString(R.string.global_vendor_by) + Html.fromHtml(_item.vendor_name) );
			_tvCoinCount.setText(String.valueOf(_item.vcoin_value));
			_tvDescription.setText(_item.product_description==null? null: Html.fromHtml(_item.product_description));
//			_tvExpDate.setText(getString(R.string.global_expDate) + " " + _item.expiration_date);
			_videoPlayer.prepareVideo(_item.image_video_cover, _item.video_url, vpListener);
//			_tvCountryDesc.setText(_item.countryDescription==null? null:Html.fromHtml(_item.countryDescription));
//			_lbCategory.setText(_item.category==null? null: Html.fromHtml(_item.category));
//			_lbCountry.setText(_item.country == null? null : Html.fromHtml(_item.country));
			
//			_btnRedeem.resetCount(_item.redeem_count);
//			_btnShare.resetCount(_item.share_count);
//			_btnComment.resetCount(_item.comment_count);
//			_scrollView.scrollBy(0, -50);
//			ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory, _imageOptions);
//			_commentAdapter.notifyDataSetChanged();
			
//			if(_item.video_url != null && _item.video_url.length() >0)
				_btnRedeem.setEnabled(false);
				
//			else
//				_btnRedeem.setEnabled(true);
		}else{
			finish();
		}
		_layout.removeView(_cover);
	}

	private VideoPlayListener vpListener = new VideoPlayListener(){
		@Override
		public void onVidePlayCompleted(boolean isCompleted) {
			if(isCompleted){
//				_btnRedeem.setButtonEnable(true);
				_btnRedeem.setEnabled(true);
				_rewardDetail_redeemCover.setVisibility(View.GONE);
				postWatchVideo();
			}
		}
	};
	private void postWatchVideo(){
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url.getWatchVideoUrl(
								LoginUserInfo.getInstance().getToken()
								, Constants.GlobalKey.getAppKey(), _item.Id)
								);
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "WatchVideo resposnse json succeed : "+response.toString());
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "WatchVideo response json failed." + e.toString());
//				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"WatchVideo");
	}
	
	@Override   
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RESULTCODE){ 
			if(resultCode == RESULT_OK){
				_refreshPortion = true;
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
	    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if (_videoPlayer != null) {
//			_videoPlayer.playVideo();
//		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		_videoPlayer.pauseVideo();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		_videoPlayer.cancelTimer();
		_videoPlayer = null;
	}
}
