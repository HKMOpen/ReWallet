package com.imusic.rewallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.AppsListItem;
import com.imusic.rewallet.utils.CategoryCountryUtil;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.MainActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppsListAdapter extends BaseAdapter{

	private static final String TAG  = "AppsListAdapter";
	
	private Context _context;
	private Activity _app;
	private ArrayList<AppsListItem> _items;
	private DisplayImageOptions _imageOptions;// = Util.getImageLoaderOptionForApp();
	public AppsListAdapter(Activity app,ArrayList<AppsListItem> items){
		_app = app;
		_context = app.getApplicationContext();
		_items = items;
		_imageOptions = Util.getImageLoaderOptionForApp(_context);
	}
	public void resetData(ArrayList<AppsListItem> items){
		_items = items;
	}
	public void addMoreItemToList(JSONObject response){
		if( _items == null)
			_items = new ArrayList<AppsListItem>();
		parseJSONData(response);
	}
	private void parseJSONData(JSONObject response){
		try {
			int result = response.getInt("result");
			if(result == 1){
				JSONArray dataArray = response.getJSONArray("data");
				if(dataArray != null){
					String icon = null;
					String store_id = null;
					String description = null;
					String platform = null;
					int developer;
					String developer_name = null;
					String app_name = null;
					JSONArray cat_term = null;
					int redeem_count = 0;
					int comment_count;
					int ID;
					int coin = 0;
					String category = null;
					AppsListItem item = null;
					for(int i = 0; i < dataArray.length(); i++){
						ID = dataArray.getJSONObject(i).getInt("ID");
						icon = dataArray.getJSONObject(i).getString("icon");
						store_id = dataArray.getJSONObject(i).getString("store_id");
						description = dataArray.getJSONObject(i).getString("description");
						platform = dataArray.getJSONObject(i).getString("platform");
						developer = dataArray.getJSONObject(i).getInt("developer");
						developer_name = dataArray.getJSONObject(i).getString("developer_name");
						app_name = dataArray.getJSONObject(i).getString("app_name");
						cat_term = dataArray.getJSONObject(i).getJSONArray("cat_term");
						if(cat_term != null){
							String[] cat = CategoryCountryUtil.getCategory(_context, MainActionBar.MAINACTIONBAR_APPS,
									cat_term,
									"term_id","name",null);
							category = cat[0];
						}
//						redeem_count = dataArray.getJSONObject(i).getInt("redeem_count");
						comment_count = dataArray.getJSONObject(i).getInt("comment_count");
						coin = dataArray.getJSONObject(i).getInt("coin");
						item = new AppsListItem(icon, store_id, description,
								platform, developer_name, developer, app_name,
								category, redeem_count, comment_count,ID,coin);
						_items.add(item);
					}
				}
				
				Vlog.getInstance().error(false, TAG, "~~~~~~~~  app list get more data refresh~~~~~~");
			}else{
				Vlog.getInstance().error(true, TAG, "parseReceviedData_getMore failed."+response.getInt("result") + " / " +response.getString("msg"));
				showToast.getInstance(_app).showMsg(response.getString("msg"));
			}
			this.notifyDataSetChanged();
		} catch (JSONException e) {
			Vlog.getInstance().error(true, TAG, "parseReceviedData_getMore failed."+e.getMessage());
			showToast.getInstance(_app.getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	public void refreshListItemData(JSONObject response){
		_items = new ArrayList<AppsListItem>();
		parseJSONData(response);
//		try {
//			int result = response.getInt("result");
//			if(result == 1){
//				
//				JSONArray dataArray = response.getJSONArray("data");
//				if(dataArray != null){
//					String icon = null;
//					String store_id = null;
//					String description = null;
//					String platform = null;
//					int developer;
//					String developer_name = null;
//					String app_name = null;
//					String cat_term = null;
//					int redeem_count;
//					int comment_count;
//					int ID;
//					int coin = 0;
//					AppsListItem item = null;
//					for(int i = 0; i < dataArray.length(); i++){
//						ID = dataArray.getJSONObject(i).getInt("ID");
//						icon = dataArray.getJSONObject(i).getString("icon");
//						store_id = dataArray.getJSONObject(i).getString("store_id");
//						description = dataArray.getJSONObject(i).getString("description");
//						platform = dataArray.getJSONObject(i).getString("platform");
//						developer = dataArray.getJSONObject(i).getInt("developer");
//						developer_name = dataArray.getJSONObject(i).getString("developer_name");
//						app_name = dataArray.getJSONObject(i).getString("app_name");
//						cat_term = dataArray.getJSONObject(i).getString("cat_term");
//						//redeem_count = dataArray.getJSONObject(i).getInt("redeem_count");
//						comment_count = dataArray.getJSONObject(i).getInt("comment_count");
//						coin = dataArray.getJSONObject(i).getInt("coin");
//						item = new AppsListItem(icon, store_id, description,
//								platform, developer_name, developer, app_name,
//								cat_term, 0, comment_count, ID, coin);
//						_items.add(item);
//					}
//				}
//				this.notifyDataSetChanged();
//				Vlog.getInstance().error(false, TAG, "~~~~~~~~  app list get new data refresh~~~~~~" + _items.size());
//			}else{
//				Vlog.getInstance().error(true, TAG, "parseReceviedData_getMore failed."+response.getInt("result") + " / " +response.getString("msg"));
//				showToast.getInstance(_app).showMsg(response.getString("msg"));
//			}
//		} catch (JSONException e) {
//			Vlog.getInstance().error(true, TAG, "parseReceviedData_getMore failed."+e.getMessage());
//			e.printStackTrace();
//		}
	}
	@Override
	public int getCount() {
		return _items == null? 0 : _items.size();
	}

	@Override
	public Object getItem(int position) {
		return _items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		}else{
            holder = new ViewHolder();
            convertView = View.inflate(_context, R.layout.listitem_apps, null);
            holder.imgIcon = (ImageView)convertView.findViewById(R.id.applistItem_imgAppIcon);
            holder.tvAppName = (TextView)convertView.findViewById(R.id.applistItem_tvAppName);
//            holder.tvCategory = (TextView)convertView.findViewById(R.id.appsListItem_tvCategoryName);
            holder.tvCoinCount = (TextView)convertView.findViewById(R.id.applistItem_tvCountValue);
//            holder.tvDescription = (TextView)convertView.findViewById(R.id.appsListItem_tvDescription);
            holder.tvVendor = (TextView)convertView.findViewById(R.id.applistItem_tvVendorName);
            convertView.setTag(holder);
		}
		AppsListItem item = _items.get(position);
		holder.tvAppName.setText(item.app_name == null ? "": Html.fromHtml(item.app_name));
//		holder.tvCategory.setText(item.cat_term == null ? "" : Html.fromHtml(item.cat_term));
		holder.tvCoinCount.setText(String.valueOf(item.coin));
//		holder.tvDescription.setText(item.description == null ? "" :Html.fromHtml(item.description));
		holder.tvVendor.setText(item.developer_name == null ? "" : Html.fromHtml(item.developer_name));
		ImageLoader.getInstance().displayImage(item.icon, holder.imgIcon, _imageOptions);
		
//		FontManager.changeFonts(parent, _app);
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView imgIcon;
		TextView tvAppName;
		TextView tvVendor;
//		TextView tvCategory;
		TextView tvCoinCount;
//		TextView tvDescription;
	}

}
