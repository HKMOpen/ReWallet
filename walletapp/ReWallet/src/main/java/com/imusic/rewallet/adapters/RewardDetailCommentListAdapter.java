package com.imusic.rewallet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.RewardCommentItem;

import java.util.List;

public class RewardDetailCommentListAdapter extends BaseAdapter{

	private final String TAG = "RewardDetailCommentListAdapter"; 
	private List<RewardCommentItem> _lstComment;
	private Context _context;
	public RewardDetailCommentListAdapter(Context context, List<RewardCommentItem> lst){
		_context = context;
		_lstComment = lst;
	}
	public void resetListData(List<RewardCommentItem> lst){
		_lstComment = lst;
	}
	@Override
	public int getCount() {
		//Log.e(TAG, String.valueOf(_lstComment.size()));
		return _lstComment == null ? 0 :_lstComment.size();
	}

	@Override
	public Object getItem(int position) {
		return _lstComment == null ? null :_lstComment.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder)convertView.getTag();
		}else{
			holder = new ViewHolder();
			convertView = View.inflate(_context,R.layout.listitem_rewarddetail_comment,null);
			holder.userName = (TextView)convertView.findViewById(R.id.rewardDetail_commentlist_tvUserName);
			holder.commentContent = (TextView)convertView.findViewById(R.id.rewardDetail_commentlist_tvCommentContent);
			holder.imgIcon = (ImageView)convertView.findViewById(R.id.rewardDetail_commentlist_imgUserIcon);
			convertView.setTag(holder);
		}
		RewardCommentItem item = _lstComment.get(position);
		holder.userName.setText(item.userName);
		holder.commentContent.setText(item.commentContent);
		
		return convertView;
	}

	class ViewHolder{
		ImageView imgIcon;
		TextView userName;
		TextView commentContent;
	}
	
}
