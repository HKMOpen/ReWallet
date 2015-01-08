package com.imusic.rewallet.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RedemptionHistoryItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.QRCodeUtil;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.BtnReversalHighlight;
import com.imusic.rewallet.widgets.LoginDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxing.activity.CaptureActivity;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class MyRewardDetailUnclaimedActivity extends BaseNavibarActivity {
	private final static String TAG = "RewardDetailActivity";
	
//	private int _Id;
//	private String _lang;
	
	private final static int HANDLE_SHOWQRCODE = 0;
	private final static int HANDLE_SCANNER_WITH_NOTE = 1;
	private final static int HANDLE_SCANNER_WITHOUT_NOTE = 2;
	
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
	private ImageView _imgQRCode;
	private LinearLayout _qrLayout;
	private ImageButton _imgbtnScanner;
	private LoginDialog _dialog;
	
	private String _note;
	private String _qrCode;
	private int _procedure =0;
	private String _errorMsg;
	private String _trace_id;
	
	public static MyRewardDetailUnclaimedActivity Instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reward_detail_unclaimed);
		if (getIntent().getExtras() != null) {
			//RewardListItem item = (RewardListItem) getIntent().getExtras().get("reward");
//			_Id = getIntent().getExtras().getInt("id");
//			_lang = getIntent().getExtras().getString("lang");
			_item = (RedemptionHistoryItem)getIntent().getExtras().get("item");
			
//			_item.handle = 2;
			
			if(_item != null){
//			if(_Id != 0){
//				_cover = new DataProcessCover(getApplicationContext(),null);
				setupView();
//				getData();
//				_handler.obtainMessage(MSG_GETDATA).sendToTarget();
				Instance = this;
			}else
				finish();
		}else
			finish();
	}
	private void setupView(){
		setupActionBar(_item.productName, true, false, null, null);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
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
		_imgQRCode = (ImageView)findViewById(R.id.myRewardDetail_imgQrcode);
//		_imgCategory = (ImageView)findViewById(R.id.myRewardDetail_imgCategory);
		_imgbtnScanner = (ImageButton)findViewById(R.id.myRewardDetail_imgBtnScanner);
		_qrLayout = (LinearLayout)findViewById(R.id.myRewardDetail_layout_qrcode);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _qrLayout.getLayoutParams();
		params.height = metric.widthPixels-150;
		params.width = metric.widthPixels-150;
		_qrLayout.setLayoutParams(params);
		
//		_lbCategory.setText(_item.category);
//		_lbCountry.setText(_item.country);
		_tvVendor.setText(_item.vendor);
		_tvExpDate.setText(String.format("%s %s", getString(R.string.global_expDate)
				, _item.expDate == null? "" : new SimpleDateFormat("yyyy/MM/dd").format(_item.expDate)));
		_tvCoinCount.setText(String.valueOf(_item.amount));
		_tvDescription.setText(Html.fromHtml(_item.description));
//		_tvCountryDesc.setText(_item.countryDescription);
		ImageLoader.getInstance().displayImage(_item.imageBanner, _imgBanner, Util.getImageLoaderOptionForDetail(getApplicationContext()));
//		ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory, Util.getImageLoaderOptionForDetail(getApplicationContext()));
		
		if(_item.handle == HANDLE_SHOWQRCODE){
			Bitmap bitmap = QRCodeUtil.createQRCodeImage(_item.qr_a, metric.widthPixels- 150, metric.widthPixels - 150);
			_imgQRCode.setImageBitmap(bitmap);
			_imgQRCode.setVisibility(View.VISIBLE);
		}else if(_item.handle == HANDLE_SCANNER_WITH_NOTE || _item.handle == HANDLE_SCANNER_WITHOUT_NOTE){
			_imgbtnScanner.setVisibility(View.VISIBLE);
			_imgbtnScanner.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent openCameraIntent = new Intent(MyRewardDetailUnclaimedActivity.this,CaptureActivity.class);
					startActivityForResult(openCameraIntent, 0);
				}
			});
			if(_item.handle == HANDLE_SCANNER_WITH_NOTE)
				_procedure = 1;
			else if(_item.handle == HANDLE_SCANNER_WITHOUT_NOTE)
				_procedure = 2;
		}
		
		
	}
	@Override
	protected void parseData(JSONObject response) {
		boolean isSuccess = false;
		try {
			int resultCode = response.getInt("result");
			if(resultCode != 1){
				_errorMsg = response.getString("msg");
				showToast.getInstance(getApplicationContext()).showMsg(_errorMsg);
			}else{
				JSONObject data = response.getJSONObject("data");
				if(data != null){
//					_errorMsg = data.getString("success_message");
					_trace_id = data.getString("trace_id");
					showToast.getInstance(getApplicationContext()).showMsg(data.getString("success_message"));
				}
				isSuccess = true;
			}
			refreshUI(isSuccess);
		} catch (JSONException e) {
			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void refreshUI(boolean isSuccess) {
		showDialog(false);
		Intent intent = new Intent();
		intent.setClass(this, MyRewardClaimProcessResultActivity.class);
		if(isSuccess){
			intent.putExtra("type", MyRewardClaimProcessResultActivity.TYPE_SUCCESS);
			intent.putExtra("rewardname", _item.productName);
			intent.putExtra("tranid", _trace_id);
		}else{
			intent.putExtra("type", MyRewardClaimProcessResultActivity.TYPE_ERROR);
			intent.putExtra("errormsg", _errorMsg);
		}
		startActivity(intent);
		finish();
	}
	@Override
	protected void getData() {
		showDialog(true);
    	HttpRequest request = HttpRequestHelper
    			.getGetRequest(
    					Constants.Url.getMyRewardScanPickupProcessUrl(
    							LoginUserInfo.getInstance().getToken()
    							, Constants.GlobalKey.getAppKey()
    							,_procedure
    							,_note
    							,_qrCode
    							,GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "redeptionObtain resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "redeptionObtain response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
//				parseData(null);
			}
		},"redeptionObtain");
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            _qrCode = bundle.getString("result");
            Vlog.getInstance().error(false, TAG, _qrCode);
            if(_item.handle == HANDLE_SCANNER_WITH_NOTE){
            	Intent intent = new Intent();
                intent.setClass(this, MyRewardCaimProcessWithNoteActivity.class);
                intent.putExtra("title", _item.productName);
                intent.putExtra("img", _item.imageBanner);
                intent.putExtra("procedure", _procedure);
                intent.putExtra("qrcode", _qrCode);
                startActivity(intent);
            }else if(_item.handle == HANDLE_SCANNER_WITHOUT_NOTE){
            	_note = "";
            	_handler.obtainMessage(MSG_GETDATA).sendToTarget();
            }
            
            
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
