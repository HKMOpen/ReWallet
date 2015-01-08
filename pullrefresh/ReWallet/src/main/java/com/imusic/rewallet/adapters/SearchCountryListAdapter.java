package com.imusic.rewallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.SearchCountryItemData;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.widgets.MainActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class SearchCountryListAdapter  extends BaseAdapter{

	private static final String TAG = "SearchCountryListAdapter";
	private Context _context;
	private ArrayList<SearchCountryItemData> _lstCountry;
	private int _searchIndex;
	private Activity _app;
	private ListView.LayoutParams _layoutParams;
	public SearchCountryListAdapter(Activity app,
			ArrayList<SearchCountryItemData> lst,
			int index){
		_app = app;
		_context = _app.getApplicationContext();
		_lstCountry = lst;
		_searchIndex = index;
	}
	public void resetData(int index ,ArrayList<SearchCountryItemData> lst){
		_lstCountry = lst;
		_searchIndex = index;
		this.notifyDataSetChanged();
	}
	public void setListViewLayout(int w, int h){
		if(_layoutParams == null){
			_layoutParams = new ListView.LayoutParams(LayoutParams.WRAP_CONTENT
					,LayoutParams.WRAP_CONTENT);
		}
		_layoutParams.height = h;
		_layoutParams.width = w;
		//Vlog.getInstance().error(false, TAG, "~~~!@~!@~!@~!@  size: " + h + "  , :" +w);
	}
	@Override
	public int getCount() {
		if(_lstCountry == null)
			return 0;
		return _lstCountry.size();
	}

	@Override
	public Object getItem(int position) {
		return _lstCountry.get(position);
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
			convertView = View.inflate(_context,R.layout.listitem_search_country,null);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.lstItemCountry_layout);
			holder.name = (TextView)convertView.findViewById(R.id.lstItemCountry_tvName);
			holder.thumb = (ImageView)convertView.findViewById(R.id.lstItemCountry_img);
			convertView.setTag(holder);
		}
		SearchCountryItemData data = _lstCountry.get(position);
//		int t = _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
//				GlobalDataInfo.getInstance().getRewardsSearchCountryId()
//				:GlobalDataInfo.getInstance().getAppsSearchCountryId();
				
//		if(t == data.Id ){
//			holder.name.setTextColor(_context.getResources().getColor(R.color.global_bg_white));
//			holder.layout.setBackgroundColor(_context.getResources().getColor(R.color.searchBar_bg_default));
//		}else{
//			holder.name.setTextColor(_context.getResources().getColor(R.color.searchBar_bg_default));
//			holder.layout.setBackgroundColor(_context.getResources().getColor(R.color.global_bg_white));
//		}
//		holder.name.setText(null);//holder.name.setText(data.name);
		ImageLoader.getInstance().displayImage(data.unpress, holder.thumb, Util.getImageLoaderOptionForList(_context));
//		FontManager.changeFonts(parent, _app);
		holder.layout.setLayoutParams(_layoutParams);
		return convertView;
	}
	public void setSelectedItem(int position){
		if(_lstCountry != null){
			final SearchCountryItemData data = _lstCountry.get(position);
			if(data == null)
				return;
			if(_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS){
				GlobalDataInfo.getInstance().setRewardsSearchCountryId(data.Id);
				GlobalDataInfo.getInstance().setRewardsSearchCountryDesc(data.description);
				GlobalDataInfo.getInstance().setRewardsSearchCountryUnpressThumb(data.unpress);
				GlobalDataInfo.getInstance().setRewardsSearchCountryPressThumb(data.press);
			}else if(_searchIndex == MainActionBar.MAINACTIONBAR_APPS){
				GlobalDataInfo.getInstance().setAppsSearchCountryId(data.Id);
				GlobalDataInfo.getInstance().setAppsSearchCountryDesc(data.description);
				GlobalDataInfo.getInstance().setAppsSearchCountryUnpressThumb(data.unpress);
				GlobalDataInfo.getInstance().setAppsSearchCountryPressThumb(data.press);
			}else
				return;
			
			
			if(data.press != null){
				ImageLoader.getInstance().loadImage(data.press
						, Util.getImageLoaderOptionForDetail(_context)
						, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
//						Vlog.getInstance().info(false, TAG, "press onLoadingStarted.."+data.press);
					}
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//						Vlog.getInstance().info(false, TAG, "press onLoadingFailed.."+data.press);
					}
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//						Vlog.getInstance().info(false, TAG, "press onLoadingComplete.."+data.press);
//						Drawable b = BitmapFactoryUtil.getDrawableFromBitmap(arg2);
					}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
			}
			
			if(data.unpress != null){
				ImageLoader.getInstance().loadImage(data.unpress
						, Util.getImageLoaderOptionForDetail(_context)
						, new ImageLoadingListener(){
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
//						Vlog.getInstance().info(false, TAG, "unpress onLoadingStarted.."+data.unpress);
					}
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//						Vlog.getInstance().info(false, TAG, "unpress onLoadingFailed.."+data.unpress);
					}
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//						Vlog.getInstance().info(false, TAG, "unpress onLoadingComplete.."+data.unpress);
//						Drawable b = BitmapFactoryUtil.getDrawableFromBitmap(arg2);
					}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
			}
			this.notifyDataSetChanged();
		}
	}
	
	
	class ViewHolder{
		RelativeLayout layout;
		ImageView thumb;
		TextView name;
	}
}
