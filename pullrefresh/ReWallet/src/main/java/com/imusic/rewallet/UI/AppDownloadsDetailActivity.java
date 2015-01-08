package com.imusic.rewallet.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.adapters.RewardDetailCommentListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.AppDetailItem;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RewardCommentItem;
import com.imusic.rewallet.utils.CategoryCountryUtil;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.DataProcessCover;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.RewardDetailCommentListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppDownloadsDetailActivity extends BaseNavibarActivity implements OnClickListener {

	private static final String TAG = "AppDownloadsDetailActivity" ;
//	private BtnHighlightReversal _btnDownload;
	private Button _btnDownload;
//	private BtnReversalHighlight _btnShare;
//	private BtnReversalHighlight _btnComment;
	private TextView _tvMainDescription;
	private ArrayList<RewardCommentItem> _lstComment ;
//	private ImageButton _imgbtnMyVcoin;
//	private ImageButton _imgbtnMyProfile;
	private RelativeLayout _layout;
	private ImageView _ivIcon;
	private TextView _tvAppName;
	private TextView _tvCoinCount;
	private TextView _tvVendor;
//	private TextView _tvCategory;
	private RewardDetailCommentListView _lvComment;
	private RewardDetailCommentListAdapter _commentAdapter;
	private AppDetailItem _item;
	private boolean _refreshPortion = false;
	private int _id;
	private ScrollView _scrollView;
	private LinearLayout _layoutScreenshot;
	
	private static final int RESULTCODE_COMMENT = 200021;
	private static final int RESULTCODE_LOGIN = 200022;
	
	private DisplayImageOptions _imageOptions ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_downloads_detail);
		_imageOptions = Util.getImageLoaderOptionForDetail(getApplicationContext());
		if(getIntent().getExtras() != null){
			_id = getIntent().getExtras().getInt("id");
			if(_id <= 0)
				finish();
			else{
				_cover = new DataProcessCover(getApplicationContext(), null);
				setupView();
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
		}else{
			finish();
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		getSupportFragmentManager().putFragment(outState, "mContent", _contentFragment);
	}
	private void setupView(){
		setupActionBar(getString(R.string.appDownloadsDetaillActivity_title),true, false,null,null);
		_scrollView = (ScrollView)findViewById(R.id.appDetail_scrollView);
		
		_layout = (RelativeLayout)findViewById(R.id.appDetail_layout);
		_btnDownload = (Button)findViewById(R.id.appDownloads_btnDownload);
//		_btnDownload.setContent(getString(R.string.appDownloadsDetaillActivity_donwload), "10", R.drawable.appdownloads_download_unpressed,R.drawable.appdownloads_download_pressed);
		_btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(LoginUserInfo.getInstance().getToken() == null ||
						LoginUserInfo.getInstance().getToken().trim().length() == 0){
					Intent cI = new Intent();
					cI.setClass(AppDownloadsDetailActivity.this, LoginActivity.class);
					startActivityForResult(cI, RESULTCODE_LOGIN);
				}else{
					postAppDownload(_item.app_key);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id="+_item.store_id));
					startActivity(intent);
				}
			}
		});
//		_btnShare = (BtnReversalHighlight)findViewById(R.id.appDownloads_btnShare);
//		_btnShare.setContent(getString(R.string.appDownloadsDetaillActivity_share), "254", R.drawable.redemptionhistory_detal_share_icon_unpressed);
//		
//		_btnComment = (BtnReversalHighlight)findViewById(R.id.appDownloads_btnComment);
//		_btnComment.setContent(getString(R.string.appDownloadsDetaillActivity_comment), "300", R.drawable.redemptionhistory_detal_comment_icon_unpressed);
		
		_tvMainDescription = (TextView)findViewById(R.id.appDownloadsDetail_tvMainDescription);
		_ivIcon = (ImageView)findViewById(R.id.appDownloadsDetail_imgAppIcon);
		_tvAppName = (TextView)findViewById(R.id.appDownloadsDetail_tvAppName);
		_tvCoinCount = (TextView)findViewById(R.id.appDownloadsDetail_tvCoinCount);
		_tvVendor = (TextView)findViewById(R.id.appDownloadsDetail_tvVendor);
//		_tvCategory = (TextView)findViewById(R.id.appDownloadsDetail_tvCategory);
//		
//		_imgbtnMyVcoin = (ImageButton)findViewById(R.id.appDetail_imgbtnMyVcoin);
//		_imgbtnMyVcoin.setOnClickListener(this);
//		_imgbtnMyProfile = (ImageButton)findViewById(R.id.appDetail_imgbtnMyProfile);
//		_imgbtnMyProfile.setOnClickListener(this);
//		setupDummyData();
		_lvComment = (RewardDetailCommentListView)findViewById(R.id.appDownloads_lvUserComment);
		_commentAdapter = new RewardDetailCommentListAdapter(getApplicationContext(),_lstComment);
		_lvComment.setAdapter(_commentAdapter); 
//		_btnShare.setCustomOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ShareCommentBtnUtil.onShare(AppDownloadsDetailActivity.this
//						, ShareCommentBtnUtil.TYPE_APP
//						, ""
//						, ""
//						, _item.Id
//						,RESULTCODE_COMMENT);
//			}
//		});
//		_btnComment.setCustomOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ShareCommentBtnUtil.onComment(AppDownloadsDetailActivity.this
//						, _id
//						, ShareCommentBtnUtil.TYPE_APP
//						, _item.title
//						, _item.icon_url
//						,_lstComment,RESULTCODE_COMMENT);
//			}
//		});
		_layoutScreenshot = (LinearLayout)findViewById(R.id.appDownloads_layout_imageContent);
	}
//	private void setupDummyData(){
//		setupImageContent();
//		_tvMainDescription.setText("app jdgj jjf kjf j vwq vkj j wq j fg w wq thtc ti i tdutfcu gqx knx ut fgj gj~! j ggmdufjc j f our jfj jkndfgkjdfgn iufg8rfgjldfgku");
//		_lstComment = new ArrayList<RewardCommentItem>();
//		for(int i = 0; i < 3; i++){
//			RewardCommentItem item = null;
//			if(i==0)
//				item = new RewardCommentItem(0,"User A"+i, "This c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksno");
//			else if(i==1)
//				item = new RewardCommentItem(0,"User A"+i, "This codr worksniksnisno");
//			else if(i==2)
//				item = new RewardCommentItem(0,"User A"+i, "This c3workcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3wworkdr worksnis c3wworkcodr worksnis c3wworkcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksnis c3workcodr worksno");
//			_lstComment.add(item);
//		}
//	}
//	private void setupImageContent(){
//		LinearLayout lyImage = (LinearLayout)findViewById(R.id.appDownloads_layout_imageContent);
//		lyImage.addView(createImageForLayout(R.drawable.app1_c1, true), 0);
//		lyImage.addView(createImageForLayout(R.drawable.app1_c2, false), 1);
//		lyImage.addView(createImageForLayout(R.drawable.app3_c2, false), 2);
//		lyImage.addView(createImageForLayout(R.drawable.app4_c1, false), 3);
//		lyImage.addView(createImageForLayout(R.drawable.app5_c1, false), 4);
//	}
	private void resetScreenshot(){
		if(_item.screenshot != null && _item.screenshot.size() >0){
			for(int i =0; i< _item.screenshot.size(); i++){
				_layoutScreenshot.addView(createImageForLayout(_item.screenshot.get(i), true),i);
			}
		}
	}
	private ImageView createImageForLayout(String url, boolean isFirst){
		ImageView picView = new ImageView(this);
		LinearLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		if(!isFirst)
			param.setMargins(50, 0, 0, 0);
		picView.setLayoutParams(param);
		picView.setAdjustViewBounds(true);
        picView.setScaleType(ScaleType.CENTER_INSIDE);
//        picView.setImageResource(rid);
        ImageLoader.getInstance().displayImage(url, picView,_imageOptions);
        return picView;
	}

	@Override
	protected void parseData(JSONObject response) {
		boolean isSucceed = false;
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					JSONObject aryData = response.getJSONObject("data");
					if(aryData != null){
//						_item = new AppDetailItem();
						
						JSONArray aryComment = aryData.getJSONArray("comment");
						if(aryComment != null){
							RewardCommentItem cItem = null;
							String username = " ";
							String content = null;
							int id = 0;
							_lstComment = new ArrayList<RewardCommentItem>();
							for(int j = 0; j < aryComment.length(); j++){
								id = aryComment.getJSONObject(j).getInt("ID");
								username = aryComment.getJSONObject(j).getString("name");
								content = aryComment.getJSONObject(j).getString("comment");
								cItem = new RewardCommentItem(id,username,content);
								_lstComment.add(cItem);
								Vlog.getInstance().info(false, TAG, "commend added:"+ content);
							}
							_commentAdapter.resetListData(_lstComment);
						}
						String title = aryData.getString("title");
						String icon_url = aryData.getString("icon_url");
						String store_id = aryData.getString("store_id");
						String description = aryData.getString("description");
						int developer_id = aryData.getInt("developer_id");
						String app_name = aryData.getString("app_name");
						String app_key = aryData.getString("app_key");
						int coin = aryData.getInt("coin");
						int share_count = aryData.getInt("share_count");
						int download_count = aryData.getInt("download_count");
						String country = "";
						String category = "" ;
						JSONArray aryCategory = aryData.getJSONArray("category");
						String[] cat = CategoryCountryUtil.getCategory(getApplicationContext()
								,MainActionBar.MAINACTIONBAR_APPS , aryCategory
								,"term_id","name",null);
						category = cat[0];
//						JSONArray aryCountry = aryData.getJSONArray("country");
//						String[] cou = CategoryCountryUtil.getCountryDescription(getApplicationContext()
//								, MainActionBar.MAINACTIONBAR_APPS
//								, aryCountry
//								, "name"
//								, "description");
//						country = cou[0];
						int comment_count = aryData.getInt("comment_count");
						String vendor= aryData.getString("developer_name");
						
//						JSONArray aryScreenshot = aryData.getJSONArray("screenshot");
//						if(aryScreenshot != null && aryScreenshot.length() >0){
//							ArrayList<String> s = new ArrayList<String>();
//							for(int si = 0; si < aryScreenshot.length(); si++){
//								s.add(aryScreenshot.getString(si));
//							}
//							_item.screenshot = s;
//						}
						
						_item = new AppDetailItem(app_name, developer_id, 
								title, icon_url, store_id, 
								description, coin, share_count, 
								download_count, country, category, 
								comment_count, vendor);
						_item.Id = _id;
						_item.app_key = app_key;
						isSucceed = true;
					}
				}else{
					Vlog.getInstance().error(true, TAG, "parseData failed."+response.getInt("result") + " / " +response.getString("msg"));
					showToast.getInstance(this).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
			
		}else{
			
		}
		if(_refreshPortion){
			updatePortion();
		}else
			refreshUI(isSucceed);
	}

	private void updatePortion(){
//		_btnComment.resetCount(_item.comment_count);
//		_btnDownload.resetCount(_item.download_count);
//		_btnShare.resetCount(_item.share_count);
//		_commentAdapter.notifyDataSetChanged();
		_layout.removeView(_cover);
		_scrollView.scrollTo(0, 0);
	}
	@Override
	protected void refreshUI(boolean isSuccee) {
		if(isSuccee){
			setupActionBar(Html.fromHtml(_item.title).toString(), true, false, "", null);
			ImageLoader.getInstance()
			.displayImage(_item.icon_url, _ivIcon, _imageOptions);
			_tvAppName.setText(Html.fromHtml(_item.app_name));
			_tvVendor.setText(_item.vendor == null ? "": Html.fromHtml(_item.vendor));
			_tvCoinCount.setText(String.valueOf(_item.coin));
			_tvMainDescription.setText(Html.fromHtml(_item.description));
//			_btnDownload.resetCount(_item.download_count);
//			_btnShare.resetCount(_item.share_count);
//			_btnComment.resetCount(_item.comment_count);
//			_tvCategory.setText(_item.category);
			resetScreenshot();
			_commentAdapter.notifyDataSetChanged();
			_layout.removeView(_cover);
			_scrollView.scrollTo(0, 0);
		}else
			finish();
	}

	@Override
	protected void getData() {
		_layout.addView(_cover);
		HttpRequest request = HttpRequestHelper
				.getGetRequest(Constants.Url
						.getAppDetailUrl(_id, GlobalDataInfo.getInstance().getLanguage()));
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
		},"");
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()){
//		case R.id.appDetail_imgbtnMyVcoin:
//			OverlayBtnUtil.myVcoinClick(this);
//			finish();
//			break;
//		case R.id.appDetail_imgbtnMyProfile:
////			OverlayBtnUtil.myProfileClick(this);
//			finish();
//			break;
//		
//		}
	}
	@Override   
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RESULTCODE_COMMENT){ 
			if(resultCode == RESULT_OK){
				_refreshPortion = true;
				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			}
	    }else if(requestCode == RESULTCODE_LOGIN){
	    	if(resultCode == RESULT_OK){
//	    		postAppDownload();
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_VIEW);
//				intent.setData(Uri.parse("market://details?id="+_item.store_id));
//				startActivity(intent);
	    	}
	    }
	}
	
	private void postAppDownload(String downloadAppKey){
		if(downloadAppKey == null || downloadAppKey.length() <=0)
			return;
		HttpRequest request = HttpRequestHelper
				.getGetRequest(Constants.Url
						.getAppDownloadUrl(LoginUserInfo.getInstance().getToken()
								, Constants.GlobalKey.getAppKey()
								, downloadAppKey));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "AppDownload resposnse json succeed : "+response.toString());
//				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "AppDownload response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				parseData(null);
			}
		},"appdownload");
	}
	
	
}
