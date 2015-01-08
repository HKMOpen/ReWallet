package com.imusic.rewallet.adapters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.MyRewarsListActivity;
import com.imusic.rewallet.model.RedemptionHistoryItem;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.widgets.InclineTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyRewardsListAdapter extends BaseAdapter{

	private List<RedemptionHistoryItem> _lstData;
	private Context _context;
	private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForList();
	
	public final static int TYPE_ALL=0;
	public final static int TYPE_UNCLIAMED=1;
	public final static int TYPE_CLIAMED=2;
	
	private int _type = 99;
	public MyRewardsListAdapter(Context context, List<RedemptionHistoryItem> lst, int type){
		_context = context;
		_lstData = lst;
		_imageOptions = Util.getImageLoaderOptionForList(_context);
		_type = type;
	}
	public void setListData(List<RedemptionHistoryItem> lst, int type){
		_lstData = lst;
		_type = type;
	}
	@Override
	public int getCount() {
		if(_lstData == null) return 0;
		return _lstData.size();
	}

	@Override
	public Object getItem(int position) {
		return _lstData == null ? null :_lstData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder)convertView.getTag();
		}else{
			holder = new ViewHolder();
			convertView = View.inflate(_context,R.layout.listitem_redemptionhistory,null);
			holder.imgIcon = (ImageView)convertView.findViewById(R.id.redemptionHistoryListItem_imgIcon);
			holder.tvRewardName = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvName);
			holder.tvVendor = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvVendor);
			holder.tvExpDate = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvExpDate);
//			holder.tvCountryDesc = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvCountryDesc);
//			holder.imgCategory = (ImageView)convertView.findViewById(R.id.redemptionHistoryListItem_imgCategory);
			holder.tvDescription = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvDescription);
			holder.tvCoinCount = (TextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvCoinCount);
			holder.layoutProcess = (RelativeLayout)convertView.findViewById(R.id.redemptionHistoryListItem_layout_processing);
			holder.tvCliamed = (InclineTextView)convertView.findViewById(R.id.redemptionHistoryListItem_tvCliamed);
			convertView.setTag(holder);
		}
		RedemptionHistoryItem item = _lstData.get(position);
		holder.tvRewardName.setText(item.productName);
		holder.tvVendor.setText(item.vendor);
		holder.tvExpDate.setText(_dateFormat.format(item.expDate));
//		holder.tvCountryDesc.setText(item.countryDescription);
		holder.tvDescription.setText(Html.fromHtml(item.description));
		holder.tvCoinCount.setText(String.valueOf(item.amount));
		if(item.p_status == MyRewarsListActivity.STATUS_P_PROCESSING){
//			holder.layoutProcess.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
			holder.layoutProcess.setVisibility(View.VISIBLE);
		}else{
			holder.layoutProcess.setVisibility(View.GONE);
		}
//		ImageLoader.getInstance().displayImage(item.categoryImage, holder.imgCategory, _imageOptions);
		ImageLoader.getInstance().displayImage(item.imageUrl, holder.imgIcon, _imageOptions);
		holder.tvCliamed.setVisibility(View.GONE);
		if(_type == TYPE_ALL){
			if(item.p_status == MyRewarsListActivity.STATUS_P_SUCCESS
					&& item.c_status == MyRewarsListActivity.STATUS_C_SUCCEES_CLIAMED){
				holder.tvCliamed.setVisibility(View.VISIBLE);
			}	
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView imgIcon;
		TextView tvRewardName;
		TextView tvVendor;
		TextView tvExpDate;
//		TextView tvCountryDesc;
//		ImageView imgCategory;
		TextView tvDescription;
		TextView tvCoinCount;
		RelativeLayout layoutProcess;
		InclineTextView tvCliamed;
	}
	

}
