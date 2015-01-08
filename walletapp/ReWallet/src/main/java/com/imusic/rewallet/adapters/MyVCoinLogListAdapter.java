package com.imusic.rewallet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.MyVCoinLogListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyVCoinLogListAdapter extends BaseAdapter{

	private static final String TAG  = "MyVCoinLogListAdapter";
	
	private Context _context;
	private ArrayList<MyVCoinLogListItem> _items;
	
	public MyVCoinLogListAdapter(Context context, ArrayList<MyVCoinLogListItem> lst){
		_context = context;
		_items = lst;
	}
	public void resetData( ArrayList<MyVCoinLogListItem> lst){
		_items = lst;
	}
	
	@Override
	public int getCount() {
		return _items == null? 0 :  _items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
            convertView = View.inflate(_context, R.layout.listitem_myvcoin_log, null);
            holder.inout = (TextView)convertView.findViewById(R.id.listItemVCoinLog_tvInOut);
            holder.time = (TextView)convertView.findViewById(R.id.listItemVCoinLog_tvDate);
            holder.count = (TextView)convertView.findViewById(R.id.listItemVCoinLog_tvAcount);
            holder.desc = (TextView)convertView.findViewById(R.id.listItemVCoinLog_tvDescription);
            
            convertView.setTag(holder);
		}
		MyVCoinLogListItem item = _items.get(position);
		holder.inout.setText(item.InOut);
		holder.desc.setText(item.Description);
		holder.count.setText(String.valueOf(item.count));
		holder.time.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(item.time));
		return convertView;
	}
	
	class ViewHolder{
		TextView inout;
		TextView time;
		TextView count;
		TextView desc;
	}

}
