package com.imusic.rewallet.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.RewardDetailItem;
import com.imusic.rewallet.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

public class RedemptionActivity extends BaseNavibarActivity {

//	private ImageButton _imgbtnAccept;
	private Button _btnAccept;
	private TextView _tvRewardTitle;
	private TextView _tvConditions;
	private ImageView _imgPic;
	private TextView _tvVendor;
	private TextView _tvExpDate;
	private TextView _tvCoinCount;
//	private TextView _tvCountryDesc;
//	private ImageView _imgCategory;
	private RewardDetailItem _item;
	private int _type;
	
	public static RedemptionActivity Instance;
	private DisplayImageOptions _imageOptions;// = Util.getImageLoaderOptionForDetail();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_imageOptions = Util.getImageLoaderOptionForDetail(getApplicationContext());
		if (getIntent().getExtras() != null) {
			setContentView(R.layout.activity_redemption);
			_item = (RewardDetailItem)getIntent().getExtras().get("item");
			_type = getIntent().getExtras().getInt("type");
//			_lstActivity = (ArrayList<Activity>)getIntent().getSerializableExtra("lstActivity");
			
			setupView();
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
		_tvVendor.setText(_item.vendor_name == null? null :Html.fromHtml(_item.vendor_name));
		_tvExpDate = (TextView)findViewById(R.id.redemptionActivity_tvExpDate);
		_tvExpDate.setText(getString(R.string.global_expDate) + " " + _item.expiration_date);
		_tvCoinCount = (TextView)findViewById(R.id.redemptionActivity_tvCoinCount);
		_tvCoinCount.setText(String.valueOf(_item.vcoin_value));
//		_tvCountryDesc = (TextView)findViewById(R.id.redemptionActivity_tvCountryDesc);
//		_tvCountryDesc.setText(Html.fromHtml(_item.countryDescription));
		_tvConditions = (TextView)findViewById(R.id.redemption_tvConditions);
		_tvConditions.setText(_item.tnc == null? null :Html.fromHtml(_item.tnc));
//		_imgbtnAccept = (ImageButton)findViewById(R.id.redemption_imgbtnAccept);
//		_imgCategory = (ImageView)findViewById(R.id.redemptionActivity_imgCategory);
		_btnAccept = (Button)findViewById(R.id.redemption_btnAccept);
		_imgPic = (ImageView)findViewById(R.id.redemption_imgPic);
		_imgPic.setLayoutParams(new LinearLayout.LayoutParams(metric.widthPixels, metric.widthPixels*10/16));
		ImageLoader.getInstance().displayImage(_item.image_banner, _imgPic,_imageOptions);
//		ImageLoader.getInstance().displayImage(_item.categoryImage, _imgCategory,_imageOptions);
//		_imgbtnAccept.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getApplicationContext(), RedemptionOptionActivity.class);
//				intent.putExtra("item", _item);
//				startActivity(intent);
//			}
//		});
		_btnAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				_lstActivity.add(RedemptionActivity.this);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), RedemptionOptionActivity.class);
				intent.putExtra("item", _item);
				intent.putExtra("type", _type);
//				intent.putExtra("lstActivity", _lstActivity);
				startActivity(intent);
			}
		});
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
		
	}

}
