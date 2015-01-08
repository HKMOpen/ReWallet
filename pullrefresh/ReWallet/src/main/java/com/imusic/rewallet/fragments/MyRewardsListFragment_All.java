package com.imusic.rewallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.MyRewardDetailClaimedActivity;
import com.imusic.rewallet.UI.MyRewardDetailUnclaimedActivity;
import com.imusic.rewallet.UI.MyRewarsListActivity;
import com.imusic.rewallet.adapters.MyRewardsListAdapter;
import com.imusic.rewallet.model.RedemptionHistoryItem;

import java.util.ArrayList;

public class MyRewardsListFragment_All extends Fragment implements OnItemClickListener{

	private ListView _lstView;
	private MyRewardsListAdapter _adapter;
	private ArrayList<RedemptionHistoryItem> _lstData;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		View view = inflater.inflate(R.layout.fragment_redemptionhistory, container , false);
		_lstView = (ListView)view.findViewById(R.id.redemptionhistory_listView);
		_adapter = new MyRewardsListAdapter(getActivity(), _lstData, MyRewardsListAdapter.TYPE_ALL);
		_lstView.setAdapter(_adapter);
		_lstView.setOnItemClickListener(this);
//		Vlog.getInstance().error(false, "!!!@@!", "my reward list fragment create view~@~@~!@~!@~!");
		return view;
	}
	public void resetListData(ArrayList<RedemptionHistoryItem> _lst,int type){
		_lstData = _lst;
		if(_lstData != null){
			_adapter.setListData(_lstData, type);
			_adapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		RedemptionHistoryItem item = (RedemptionHistoryItem)arg0.getAdapter().getItem(arg2);//_lstData.get(position);
		if(item == null)
			return;
		if(MyRewarsListActivity.STATUS_P_SUCCESS != item.p_status)
			return;
		Intent intent = new Intent();
		if(item.c_status == MyRewarsListActivity.STATUS_C_SUCCEES_CLIAMED)
			intent.setClass(getActivity(), MyRewardDetailClaimedActivity.class);
		else if(item.c_status == MyRewarsListActivity.STATUS_C_SUCCEES_UNCLIAMED)
			intent.setClass(getActivity(), MyRewardDetailUnclaimedActivity.class);
		else 
			return;
		intent.putExtra("item", item);
		startActivity(intent);
	}
}
