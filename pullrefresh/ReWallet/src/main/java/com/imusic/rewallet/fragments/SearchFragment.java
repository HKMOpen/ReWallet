package com.imusic.rewallet.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.AppDownloadsDetailActivity;
import com.imusic.rewallet.UI.RewardDetailActivity;
import com.imusic.rewallet.adapters.AppsListAdapter;
import com.imusic.rewallet.adapters.RewardChannelSTGVAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.AppsListItem;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.RewardListItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.DataProcessCover;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class SearchFragment extends Fragment{
	
	private final static String TAG = "SearchFragment";
	
	private int _searchIndex = 0;
	
	private View _view;
	private GridView _gridView;
	private ListView _listView;
	private RewardChannelSTGVAdapter _adapterReward;
	private AppsListAdapter _adapterApp;
	private DataProcessCover _cover;
	private RelativeLayout _layout;
	protected final static int MSG_GETREWARDDATA = 9900;
	protected final static int MSG_GETAPPDATA = 9801;
	protected final static int MSG_REFRESHREWARDDATA = 9910;
	protected final static int MSG_REFRESHAPPDATA = 9811;
	protected Handler _handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_GETREWARDDATA:
				String s = msg.getData().getString("keyword");
				getSearchRewardData(s);
				break;
			case MSG_GETAPPDATA:
				String ss = msg.getData().getString("keyword");
				getSearchAppData(ss);
				break;
			case MSG_REFRESHREWARDDATA:
				refreshRewardData();
				break;
			case MSG_REFRESHAPPDATA:
				refreshAppData();
				break;
			}
		};
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){

		if(_searchIndex == 0)
			System.out.println("Reward - SearchPageFragment onCreateView"); 
		else
			System.out.println("appdownload - SearchPageFragment onCreateView"); 
		_view = inflater.inflate(R.layout.fragment_search, container , false);
		setupView();
		return _view;
	}
	private void setupView(){
		_gridView = (GridView)_view.findViewById(R.id.searchFragment_gvReward);
		_listView = (ListView)_view.findViewById(R.id.searchFragment_lvApp);
		_layout = (RelativeLayout)_view.findViewById(R.id.searchFragment_layout);
		_cover = new DataProcessCover(getActivity().getApplicationContext(),null);
		_layout.addView(_cover);
		_cover.setVisibility(View.GONE);
		_adapterReward = new RewardChannelSTGVAdapter(getActivity(),null);
		fitParams();
		_gridView.setAdapter(_adapterReward);
		_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onGirdViewItemClick(arg2);
			}
		});
		_adapterApp = new AppsListAdapter(getActivity(), null);
		_listView.setAdapter(_adapterApp);
		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
					AppsListItem item = (AppsListItem)parent.getAdapter().getItem(position);// _adapter.getItem(position);
					Log.e(TAG, position +  " on item app id click : " + item.ID + " name:" + item.app_name); 
					Intent intent = new Intent();
					intent.setClass(getActivity(), AppDownloadsDetailActivity.class);
					intent.putExtra("id", item.ID);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getActivity().startActivity(intent);
			}
		});
	}
	private void onGirdViewItemClick(int position){
		RewardListItem item = (RewardListItem)_adapterReward.getItem(position);
		if(item == null)
			return;
		int type = 0;
		if(item.type.equals("intangible"))
			type = RewardDetailActivity.TYPE_COUPON;
		else if(item.type.equals("tangible"))
			type = RewardDetailActivity.TYPE_REWARD;
		else 
			return;
    	Intent intent = new Intent();
    	intent.setClass(getActivity(), RewardDetailActivity.class);
    	intent.putExtra("reward", item);
    	intent.putExtra("id", item.id);
    	intent.putExtra("type", type);
    	intent.putExtra("lang", GlobalDataInfo.getInstance().getLanguage());
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	getActivity().startActivity(intent);
    }
	public void setSearchIndex(int index){
		_searchIndex = index;
	}
	private void startSearchPerpare(){
		_cover.setVisibility(View.VISIBLE);
		hideAllControl();
	}
	public void hideAllControl(){
		_listView.setVisibility(View.GONE);
		_gridView.setVisibility(View.GONE);
		_gridView.setEnabled(false);
		_listView.setEnabled(false);
	}
	public void showRewardSearchData(String keyword){
		startSearchPerpare();
		Bundle bundle = new Bundle();
		Message msg = new Message();
		msg.what = MSG_GETREWARDDATA;
		bundle.putString("keyword", keyword);
		msg.setData(bundle);
		_handler.sendMessage(msg);
	}
	public void showAppSearchData(String keyword){
		startSearchPerpare();
		Bundle bundle = new Bundle();
		Message msg = new Message();
		msg.what = MSG_GETAPPDATA;
		bundle.putString("keyword", keyword);
		msg.setData(bundle);
		_handler.sendMessage(msg);
	}
	
	private void getSearchRewardData(String keyword){
		HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getRewardChannelList(
    							GlobalDataInfo.getInstance().getRewardsSearchCategoryId()
    							, GlobalDataInfo.getInstance().getRewardsSearchCountryId()
    							,keyword, GlobalDataInfo.getInstance().getLanguage(), 0));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getSearchRewardData list resposnse json succeed : "+response.toString());
				_adapterReward.refreshItemList(response);
				_handler.obtainMessage(MSG_REFRESHREWARDDATA).sendToTarget();
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().error(false,TAG, "getSearchRewardData list  response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
				_handler.obtainMessage(MSG_REFRESHREWARDDATA).sendToTarget();
			}
		},"");
	}
	private void getSearchAppData(String keyword){
		HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getAppList(keyword, null
    							, GlobalDataInfo.getInstance().getAppsSearchCategoryId()
    							, GlobalDataInfo.getInstance().getAppsSearchCountryId()
    							, GlobalDataInfo.getInstance().getLanguage(), 0));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getSearchRewardData list resposnse json succeed : "+response.toString());
				_adapterApp.refreshListItemData(response);
				_handler.obtainMessage(MSG_REFRESHAPPDATA).sendToTarget();
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().error(false,TAG, "getSearchRewardData list  response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
				_handler.obtainMessage(MSG_REFRESHAPPDATA).sendToTarget();
			}
		},"");
	}
	private void refreshRewardData(){
		if(_cover.isShown())
			_cover.setVisibility(View.GONE);
		_gridView.setEnabled(true);
		_gridView.setVisibility(View.VISIBLE);
	}
	private void refreshAppData(){
		if(_cover.isShown())
			_cover.setVisibility(View.GONE);
		_listView.setEnabled(true);
		_listView.setVisibility(View.VISIBLE);
	}
	private void fitParams(){
		DisplayMetrics dm = new DisplayMetrics();
    	getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    	RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	float w =0;
    	if(Integer.valueOf(Build.VERSION.SDK_INT) >= 16)
    		w = dm.widthPixels - getResources().getDimension(R.dimen.activity_horizontal_margin)*2 - _gridView.getHorizontalSpacing();
    	else
    		w = dm.widthPixels - getResources().getDimension(R.dimen.activity_horizontal_margin)*2 - 5;
    	layoutParams.width = (int) w/2;
    	layoutParams.height = (int) w/2;
    	_adapterReward.setImageLayoutParams(layoutParams);
    }
}
