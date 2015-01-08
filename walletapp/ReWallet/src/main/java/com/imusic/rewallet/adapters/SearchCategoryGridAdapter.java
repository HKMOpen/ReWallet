package com.imusic.rewallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.SearchCategoryItemData;
import com.imusic.rewallet.utils.CategoryColorUtil;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.widgets.MainActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class SearchCategoryGridAdapter extends BaseAdapter{

	private static final String TAG = "SearchCategoryGridAdapter";
	private Context _context;
	private Activity _app;
//	private GridView _gridView;
	private int _searchIndex;
	private ArrayList<SearchCategoryItemData> _lstCategory;
	private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForDetail();
	
	private GridView.LayoutParams _layoutParams;
//	private DisplayMetrics dm = new DisplayMetrics();
	public SearchCategoryGridAdapter(Activity app,
			ArrayList<SearchCategoryItemData> lst,
			int index){
		_app = app;
		_context = _app.getApplicationContext();
		_lstCategory = lst;
		_searchIndex = index;
		_imageOptions = Util.getImageLoaderOptionForDetail(_context);
//		_layoutParams =  new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		DisplayMetrics dm = new DisplayMetrics();
//		_app.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		_layoutParams.width = dm.widthPixels / 2;
//		_layoutParams.height = dm.widthPixels / 2;
	}
	public void resetData(int index, ArrayList<SearchCategoryItemData> lst){
		_lstCategory = lst;
		_searchIndex = index;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if(_lstCategory == null)
			return 0;
		return _lstCategory.size();
	}

	@Override
	public Object getItem(int position) {
		return _lstCategory.get(position);
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
			convertView = View.inflate(_context,R.layout.griditem_search_category,null);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.gridItemCategory_layout);
//			holder.thumb = (ImageView)convertView.findViewById(R.id.gridItemCategory_imgThumb);
			holder.name = (TextView)convertView.findViewById(R.id.gridItemCategory_tvCategoryName);
			convertView.setTag(holder);
		}
		SearchCategoryItemData data = _lstCategory.get(position);
//		int t = _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
//				GlobalDataInfo.getInstance().getRewardsSearchCategoryId()
//				:GlobalDataInfo.getInstance().getAppsSearchCategoryId();
//		if(t == data.Id ){
//			holder.layout.setBackgroundColor(_context.getResources().getColor(R.color.searchBar_bg_default));
//		}else{
//			holder.layout.setBackgroundColor(_context.getResources().getColor(R.color.global_bg_white));
//		}

//		holder.name.setLayoutParams(_layoutParams);
//		if(_layoutParams == null){
//			DisplayMetrics dm = new DisplayMetrics();
//			_app.getWindowManager().getDefaultDisplay().getMetrics(dm);
//			_layoutParams = (LayoutParams)holder.name.getLayoutParams();
//			_layoutParams.height = dm.widthPixels / 2;
//			_layoutParams.width = dm.widthPixels / 2;
//		}
		  
		holder.layout.setLayoutParams(_layoutParams);
		 
		if(_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS){
			holder.layout.setBackgroundColor(_context.getResources().getColor(CategoryColorUtil.getRewardCategoryColor(position%8)));
		}else if(_searchIndex == MainActionBar.MAINACTIONBAR_APPS) {
			holder.layout.setBackgroundColor(_context.getResources().getColor(CategoryColorUtil.getAppCategoryColor(position%8)));
		}
		
		//holder.name.setText(data.name);
//		ImageLoader.getInstance().displayImage(data.unpress, holder.thumb, _imageOptions);
		
		holder.name.setText(Html.fromHtml(data.name));
		
//		FontManager.changeFonts(parent, _app);
		
		return convertView;
	}
	
	public void setSelectedItem(int position){
		if(_lstCategory != null){
			final SearchCategoryItemData data = _lstCategory.get(position);
			if (data == null)
				return;
			if(_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS){
				GlobalDataInfo.getInstance().setRewardsSearchCategoryId(data.Id);
				GlobalDataInfo.getInstance().setRewardsSearchCategoryUnpressThumb(data.unpress);
				GlobalDataInfo.getInstance().setRewardsSearchCategoryPressThumb(data.press);
			}else if(_searchIndex == MainActionBar.MAINACTIONBAR_APPS){
				GlobalDataInfo.getInstance().setAppsSearchCategoryId(data.Id);
				GlobalDataInfo.getInstance().setAppsSearchCategoryUnpressThumb(data.unpress);
				GlobalDataInfo.getInstance().setAppsSearchCategoryPressThumb(data.press);
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
//						((MainActivity)_app).getSearchBar().getCateToggleBtn().setBackgroundDrawable(b);
					}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
//				ImageLoader.getInstance().loadImageSync(data.press);
			}
			
			if(data.unpress != null){
				ImageLoader.getInstance().loadImage(data.unpress
						, Util.getImageLoaderOptionForDetail(_context)
						, new ImageLoadingListener() {
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
//						((MainActivity)_app).getSearchBar().getCateToggleBtn().setBackgroundDrawable(b);
					}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
//				ImageLoader.getInstance().loadImageSync(data.unpress);
			}
			this.notifyDataSetChanged();
		}
	}
	public void setGridViewLayout(int w, int h){
		if(_layoutParams == null){
			_layoutParams = new GridView.LayoutParams(LayoutParams.WRAP_CONTENT
					,LayoutParams.WRAP_CONTENT);
		}
		_layoutParams.height = h;
		_layoutParams.width = w;
		//Vlog.getInstance().error(false, TAG, "~~~!@~!@~!@~!@  size: " + h + "  , :" +w);
	}
	
	
	class ViewHolder{
		RelativeLayout layout;
//		ImageView thumb;
		TextView name;
	}
}
