package com.imusic.rewallet.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.MyCouponsActivity;
import com.imusic.rewallet.model.MyCouponItem;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.showToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyCouponsListAdapter extends BaseAdapter{

	private ArrayList<MyCouponItem> _lstData;
	private static Context _context;
	private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private DisplayImageOptions _imageOptions;// = Util.getImageLoaderOptionForList();
	
	public MyCouponsListAdapter(Context context, ArrayList<MyCouponItem> lst){
		_context = context;
		_lstData = lst;
		_imageOptions = Util.getImageLoaderOptionForList(_context);
	}
	public void resetDate(ArrayList<MyCouponItem> lst){
		_lstData = lst;
	}
	
	@Override
	public int getCount() {
		return _lstData == null ? 0 : _lstData.size();
	}

	@Override
	public Object getItem(int position) {
		return _lstData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder)convertView.getTag();
		}else{
			holder = new ViewHolder();
			convertView = View.inflate(_context,R.layout.listitem_mycoupons,null);
			holder.imgIcon = (ImageView)convertView.findViewById(R.id.myCouponsListItem_imgIcon);
			holder.tvRewardName = (TextView)convertView.findViewById(R.id.myCouponsListItemm_tvName);
			holder.tvVendor = (TextView)convertView.findViewById(R.id.myCouponsListItem_tvVendor);
			holder.tvExpDate = (TextView)convertView.findViewById(R.id.myCouponsListItem_tvExpDate);
			holder.btnCopyCode = (Button)convertView.findViewById(R.id.myCouponsListItem_btnCopyCode);
			holder.btnGotoLink = (Button)convertView.findViewById(R.id.myCouponsListItem_btnGotoLink);
			holder.edCode = (EditText)convertView.findViewById(R.id.myCouponsListItem_edCode);
			
			holder.tvCoinCount = (TextView)convertView.findViewById(R.id.myCouponsListItem_tvCoinCount);
//			holder.tvCountryDesc = (TextView)convertView.findViewById(R.id.myCouponsListItem_tvCountryDesc);
//			holder.imgCategory = (ImageView)convertView.findViewById(R.id.myCouponsListItem_imgCategory);
			holder.tvDescription = (TextView)convertView.findViewById(R.id.myCouponsListItem_tvDescription);
			holder.layoutProcess = (RelativeLayout)convertView.findViewById(R.id.myCouponsListItem_layout_processing);
			convertView.setTag(holder);
		}
		final MyCouponItem item = _lstData.get(position);
		holder.tvRewardName.setText(item.productName);
		holder.tvVendor.setText(item.vendor);
		if(item.expDate != null )
			holder.tvExpDate.setText(_dateFormat.format(item.expDate));
		else
			holder.tvExpDate.setText("");
//		holder.imgIcon.setImageResource(R.drawable.redemptionhistory_icon);
		
		holder.edCode.setFocusable(false);
		holder.edCode.setText(item.code);
		holder.tvCoinCount.setText(String.valueOf(item.amount));
//		holder.tvCountryDesc.setText(item.countryDescription);
		holder.tvDescription.setText(item.description);
		ImageLoader.getInstance().displayImage(item.imageUrl, holder.imgIcon, _imageOptions);
//		ImageLoader.getInstance().displayImage(item.categoryImage, holder.imgCategory, _imageOptions);
		
		
		if(item.status == MyCouponsActivity.STATUS_PROCESSING){
			holder.layoutProcess.setVisibility(View.VISIBLE);
			holder.btnCopyCode.setOnClickListener(null);
			holder.btnGotoLink.setOnClickListener(null);
		}else{
			holder.layoutProcess.setVisibility(View.GONE);
			holder.btnCopyCode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					copyCode(item.code);
				}
			});
			holder.btnGotoLink.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(item.product_url != null && item.product_url.length() > 0){
						Uri uri = Uri.parse(item.product_url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						_context.startActivity(intent);
					}
				}
			});
		}
		
		
		return convertView;
	}
	private static void copyCode(String content) {  
		ClipboardManager cmb = (ClipboardManager)_context.getSystemService(Context.CLIPBOARD_SERVICE);  
		cmb.setText(content.trim());  
		showToast.getInstance(_context)
			.showMsg(_context.getResources()
					.getString(R.string.global_textHasbeenCopied) + ": " + content);
	}
	class ViewHolder{
		ImageView imgIcon;
		TextView tvRewardName;
		TextView tvVendor;
		TextView tvExpDate;
		TextView tvCoinCount;
//		ImageView imgCategory;
//		TextView tvCountryDesc;
		TextView tvDescription;
		Button btnCopyCode;
		Button btnGotoLink;
		EditText edCode;
		RelativeLayout layoutProcess;
	}
}
