package com.imusic.rewallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.MissionItem;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.InclineTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MissionListAdapter extends BaseAdapter{

	private final String TAG = "MissionListAdapter";
	private ArrayList<MissionItem> _items;
	private Activity _app;
	private Context _context;
	private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForMissionList();
	public MissionListAdapter(Activity app, ArrayList<MissionItem> lst){
		this._app = app;
		this._context = app.getApplicationContext();
		this._items = lst;
		_imageOptions = Util.getImageLoaderOptionForMissionList(_context);
	}
	public void refreshListItemData(JSONObject response){
		try {
			int result = response.getInt("result");
			if(result == 1){
				JSONArray dataArray = response.getJSONArray("data");
				_items = new ArrayList<MissionItem>();
				if(dataArray != null){
					String title;
					String desc;
					int id;
					String thumb;
					int vcoin;
					String status;
					String type;
					MissionItem item =null;
					for(int i = 0; i< dataArray.length(); i++){
						title = dataArray.getJSONObject(i).getString("title");
						id = dataArray.getJSONObject(i).getInt("mission_id");
						desc =dataArray.getJSONObject(i).getString("description");
						thumb =dataArray.getJSONObject(i).getString("thumb_sq");
						vcoin =dataArray.getJSONObject(i).getInt("vcoin_reward");
						status = dataArray.getJSONObject(i).getString("status");
						type =dataArray.getJSONObject(i).getString("type");
						item = new MissionItem(title, id, desc, thumb, vcoin, status, type);
						_items.add(item);
					}
				}
				this.notifyDataSetChanged();
			}else{
				Vlog.getInstance().error(true, TAG, "parseReceviedData_getMore failed."+response.getInt("result") + " / " +response.getString("msg"));
				showToast.getInstance(_app).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			showToast.getInstance(_app.getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public int getCount() {
		return _items == null? 0 : _items.size();
	}

	@Override
	public Object getItem(int position) {
		return _items == null? null: _items.get(position);
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
            convertView = View.inflate(_context, R.layout.listitem_mission, null);
            holder.title = (TextView)convertView.findViewById(R.id.missionListItem_tvTitle);
            holder.image = (ImageView)convertView.findViewById(R.id.missionListItem_ivImage);
            holder.vcoin = (TextView)convertView.findViewById(R.id.missionListItem_tvCoinCount);
            holder.desc = (TextView)convertView.findViewById(R.id.missionListItem_tvDescription);
            holder.status = (InclineTextView)convertView.findViewById(R.id.missionListItem_tvStatus);
            convertView.setTag(holder);
		}
		MissionItem item = _items.get(position);
		holder.title.setText(item.title);
		holder.vcoin.setText(String.valueOf(item.vcoin_reward));
		holder.desc.setText(item.description);
		ImageLoader.getInstance().displayImage(item.thumb_sq, holder.image,_imageOptions);
		if(item.status.equals("complete")){
			holder.status.setVisibility(View.VISIBLE);
			holder.status.setText(item.status);
		}
		return convertView;
	}

	
	class ViewHolder{
		TextView title;
		ImageView image;
		TextView vcoin;
		TextView desc;
		InclineTextView status;
	}
	
}
