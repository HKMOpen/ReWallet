package com.imusic.rewallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshVerticalScrollView;
import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.AppDownloadsDetailActivity;
import com.imusic.rewallet.adapters.AppsListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.fragments.SearchCategoryFliterFragment.onCategorySelectListener;
import com.imusic.rewallet.fragments.SearchCountryFliterFragment.onCountrySelectListener;
import com.imusic.rewallet.model.AppsListItem;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.ImageSliderItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.ExpandableHeightListView;
import com.imusic.rewallet.widgets.LoginDialog;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.SearchBar;
import com.imusic.rewallet.widgets.SearchBar.SearchBarItemClickListener;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppsFragment extends DataBaseFragment implements OnRefreshListener2<ScrollView>, SearchBarItemClickListener, onCategorySelectListener, onCountrySelectListener{

//	private DisplayMetrics _metric = new DisplayMetrics();
//	private RewardDetailCommentListView _lvCategory;
//	private AppDownloadsCategoryListAdapter _adapter;
//	private List<List<AppDownloads_CategoryListItem>> _lstCategory;
	private String TAG = "AppsFragment";

//	private ArrayList<AppsListItem> _items;
	private SearchBar _searchBar;
	private SearchCategoryFliterFragment _categoryFragment;
	private SearchCountryFliterFragment _countryFragment;
	private FrameLayout _layoutFilter;
	private View _view;
	private PullToRefreshVerticalScrollView _ptrlistView;
	private ExpandableHeightListView _listView;
//	private DataProcessCover _cover;
	private RelativeLayout _layout;
	private AppsListAdapter _adapter;
//	private ImageSlider _imgSlider;
	private ArrayList<ImageSliderItem> _items;
	
	private LoginDialog _dialog;
	
	private boolean _isRefreshSlider;
	private int _pIndex = 1;
	private boolean isInit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		System.out.println("AppsFragment onCreateView"); 
		isInit = true;
		_view = inflater.inflate(R.layout.fragment_apps, container , false);
//		setupView();
//		showDialog(true);
//		_handler.obtainMessage(MSG_PREPAREDATE).sendToTarget();
		return _view;
	}
	@Override  
    public void onViewCreated(View view, Bundle savedInstanceState) {  
        super.onViewCreated(view, savedInstanceState);  
        setupView();
//		showDialog(true);
//		_handler.obtainMessage(MSG_PREPAREDATE).sendToTarget();
    } 

	@Override
	public void onResume() {
		super.onResume();
		// 判断当前fragment是否显示
		if (getUserVisibleHint()) {
			showData();
			Vlog.getInstance().debug(false, TAG, "app fragment on resumt---------------");
		}
		
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// 每次切换fragment时调用的方法
		if (isVisibleToUser) {
			showData();
		}
	}

	private void showData() {
		if (isInit) {
			isInit = false;
			showDialog(true);
			_handler.obtainMessage(MSG_PREPAREDATE).sendToTarget();
		}
	}
//	public void pauseImageSlider(boolean isStop){
//		if(_imgSlider == null)
//			return;
//		if(isStop){
//			_imgSlider.onStopImageSwitch();
//		}else{
//			_imgSlider.switchNextImage();
//		}
//	}
	private void setupView(){
//		_cover = new DataProcessCover(getActivity().getApplicationContext(), null);
		_layout = (RelativeLayout)_view.findViewById(R.id.appsFragment_layout);
		_ptrlistView = (PullToRefreshVerticalScrollView)_view.findViewById(R.id.appsFragment_pull_refresh_scrollview);
		_ptrlistView.setMode(Mode.PULL_FROM_END);
		
		_ptrlistView.getLoadingLayoutProxy().setPullLabel(getString(R.string.pull_to_refresh_pull));
		_ptrlistView.getLoadingLayoutProxy().setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing));
		_ptrlistView.getLoadingLayoutProxy().setReleaseLabel(getString(R.string.pull_to_refresh_release));
		
		_listView = (ExpandableHeightListView)_view.findViewById(R.id.appsFragment_listView);
		_adapter = new AppsListAdapter(getActivity(), null);
		_listView.setAdapter(_adapter);
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
//		_imgSlider = (ImageSlider)_view.findViewById(R.id.appsFragment_imageSlider);
//		_imgSlider.setImageSliderItemOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//
//				ImageSliderItem item = _imgSlider.getCurrentSliderItem();
//				if(item == null)
//					return;
//				if(item.postId <=0)
//					return;
//				
//				if(!item.type.equals("android"))
//					return;
//				
//				
////				Intent intent = new Intent();
////				intent.setClass(getActivity(), RewardDetailActivity.class);
////		    	intent.putExtra("id", item.postId);
////		    	intent.putExtra("lang", GlobalDataInfo.getInstance().getLanguage());
////		    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////		    	getActivity().startActivity(intent);
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), AppDownloadsDetailActivity.class);
//				intent.putExtra("id", item.Id);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				getActivity().startActivity(intent);
//			}
//		});
//		fitParams();
//		_ptrlistView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//			@Override
//			public void onPullDownToRefresh(
//					PullToRefreshBase<ListView> refreshView) {}
//
//			@Override
//			public void onPullUpToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				_handler.obtainMessage(MSG_GETMOREDATE).sendToTarget();
//			}
//
//			
//		});
		_ptrlistView.setOnRefreshListener(this);
		
		
		_searchBar = (SearchBar) _view.findViewById(R.id.layout_searchBar);
		_searchBar.setSearchMode(false);
		_searchBar.setSearchIndex(MainActionBar.MAINACTIONBAR_APPS);
		_searchBar.setFilterItemClickListener(this);
		_searchBar.setCustomOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent inSearch = new Intent();
//				inSearch.setClass(MainActivity.this, SearchActivity.class);
//				inSearch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				inSearch.putExtra("index", _actionBar.getCurrentIndex());
//				startActivityForResult(inSearch, _requestCode);
			}
		});
		
		
		_layoutFilter = (FrameLayout)_view.findViewById(R.id.layout_mainFilterContent);
		_layoutFilter.setVisibility(View.GONE);
		_categoryFragment = new SearchCategoryFliterFragment(); 
		_categoryFragment.setItemClickListener(this);
		_countryFragment = new SearchCountryFliterFragment();
		_countryFragment.setItemClickListener(this);
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.layout_mainFilterContent,_categoryFragment);
		ft.add(R.id.layout_mainFilterContent, _countryFragment);
		ft.hide(_categoryFragment);
		ft.hide(_countryFragment);
		ft.commit();
	}
	
	private void showFlitterFragment(int index, boolean isShow){
//		hideAllFragment();
		if(isShow)
			_layoutFilter.setVisibility(View.VISIBLE);
		else
			_layoutFilter.setVisibility(View.GONE);
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		if(_categoryFragment != null){
			ft.hide(_categoryFragment);
		}
		if(_countryFragment != null){
			ft.hide(_countryFragment);
		}
		switch (index){
		case 0:
			if(isShow){
				if(_categoryFragment != null){
				ft.show(_categoryFragment);
				}
//				hideVcoinAndProfileButton(true);
			}
			else{
				if(_countryFragment != null){
					ft.hide(_countryFragment);
				}
//				hideVcoinAndProfileButton(false);
			}
			break;
		case 1:
			if(isShow){
				if(_countryFragment != null){
					ft.show(_countryFragment);
				}
//				hideVcoinAndProfileButton(true);
			}
			else{
				if(_countryFragment != null){
					ft.hide(_countryFragment);
				}
//				hideVcoinAndProfileButton(false);
			} 
			break;
		}
		ft.commit();
	}
//	private void refreshImageSlider(){
//		if(_items.size() > 0){
//			_imgSlider.setContent(_items);
//		}
//	}
	@Override
	protected void onRefreshData() {
		//_ptrlistView.onRefreshComplete();
		//_ptrlistView.setRefreshing(true);
		_isRefreshSlider = false;
		_pIndex = 1;
		getApplistNewItem();
//		getImageSliderNewItem();
	}
//	private void setRefreshShowing(){
//		_dialog = new LoginDialog(getActivity(), R.style.LoginTransparent);
//		_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		_dialog.show();
//		
//		
////		_ptrGridview.setRefreshing(true);
////		new Thread() {
////	        public void run() {
////	                try {
////	                    getActivity().runOnUiThread(new Runnable() {
////
////	                        @Override
////	                        public void run() {
////	                        	_ptrGridview.setRefreshing(true); 
////	                        }
////	                    });
////	                } catch (Exception e) {
////	                    e.printStackTrace();
////	                }
////	        }
////	    }.start();
//	}
	private void getApplistNewItem(){
		HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getAppList(null, null
    							, GlobalDataInfo.getInstance().getAppsSearchCategoryId()
    							, GlobalDataInfo.getInstance().getAppsSearchCountryId()
    							, GlobalDataInfo.getInstance().getLanguage(), _pIndex));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "appfragment onRefreshData resposnse json succeed : "+response.toString());
				parseReceviedData_refresh(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "appfragment onRefreshData response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
				parseReceviedData_refresh(null);
			}
		},"applist");
	}
	private void getImageSliderNewItem(){
		//Vlog.getInstance().info(false,TAG, " ImageSlider request: " + Constants.Url.getRewardChannelSlider(3,14,GlobalDataInfo.getInstance().getLanguage()));
    	HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getApplistSliderUrl(
    							GlobalDataInfo.getInstance().getAppsSearchCountryId(),
    							GlobalDataInfo.getInstance().getAppsSearchCategoryId(),
    							GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "ImageSlider resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "ImageSlider response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
			}
		},"");
	}
	private void parseData(JSONObject response){
		try {
			int resultCode = response.getInt("result");
			if(resultCode == 1){
				_items = new ArrayList<ImageSliderItem>();
				JSONArray datas = response.getJSONArray("data");
				JSONObject data;
				ImageSliderItem item;
				int id;
				int post_id;
				String collection_name;
				String url;
				float nextToTime;
				int width =0;
				int height =0;
				String post_type = null;
				JSONArray sliders;
				if(datas != null){
					for(int i = 0; i < datas.length(); i++){
						data =  datas.getJSONObject(i);
						if(data != null){
							if(data.has("post_type")){
								if(!data.isNull("post_type")){
									post_type = data.getString("post_type");
									if(!post_type.equals("android"))
										continue;
								}
							}
							post_id = data.getInt("post_id");
							id = data.getInt("id");
							collection_name = data.getString("collection_name");
							nextToTime = data.getLong("time_to_next");
							sliders = data.getJSONArray("slides");
							if(sliders != null){
								for(int j = 0; j < sliders.length(); j++){
									url = ((JSONObject)sliders.get(j)).getString("url");
									if(url == null || url.length() <1)
										continue;
//									width = ((JSONObject)sliders.get(j)).getInt("width");
//									height = ((JSONObject)sliders.get(j)).getInt("height");
									item = new ImageSliderItem(post_id,id,
											collection_name, url,
											width, height, nextToTime
											, data.getString("post_type"));
									_items.add(item);
								}
							}
						}
					}
				}
				_isRefreshSlider = true;
//				refreshImageSlider();
			}else{
				showToast.getInstance(getActivity()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getActivity()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onGetMoreData() {
		HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getAppList(null, null
    							, GlobalDataInfo.getInstance().getAppsSearchCategoryId()
    							, GlobalDataInfo.getInstance().getAppsSearchCountryId()
    							, GlobalDataInfo.getInstance().getLanguage(), _pIndex+1));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "appfragment onGetMoreData resposnse json succeed : "+response.toString());
				parseReceviedData_getMore(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "appfragment onGetMoreData response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
				parseReceviedData_getMore(null);
			}
		},"");
		
	}

	@Override
	protected void onPrepareData() {
		Vlog.getInstance().info(false, TAG, "on PrepareData, not data, get more data...");
		_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
	}

	
	@Override
	protected void parseReceviedData_getMore(JSONObject response) {
		Vlog.getInstance().info(false, TAG, "parseReceviedData...");
		if(response != null){
			_adapter.addMoreItemToList(response);
			_pIndex++; 
		}
		_handler.obtainMessage(MSG_GETMOREDATACOMPLETED).sendToTarget();
	}

	@Override
	protected void parseReceviedData_refresh(JSONObject response) {
		Vlog.getInstance().info(false, TAG, "parseReceviedData_refresh...");
		if(response != null){
			_adapter.refreshListItemData(response);
			_pIndex = 1;
		}
		_handler.obtainMessage(MSG_REFEASHDATACOMPLETED).sendToTarget();
	}

	@Override
	protected void onRefreshDataCompleted() {
		Vlog.getInstance().info(false, TAG, "onRefreshDataCompleted...");
		if(_ptrlistView.isRefreshing())
			_ptrlistView.onRefreshComplete();
		
//		if(_isRefreshSlider)
//			refreshImageSlider();
		showDialog(false);
	}

	@Override
	protected void onGetMoreDataCompleted() {
		if(_ptrlistView.isRefreshing())
			_ptrlistView.onRefreshComplete();
	}

	public void refreshListViewItem(){
		showDialog(true);
		_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		_handler.obtainMessage(MSG_GETMOREDATE).sendToTarget();
	}

	private void fitParams(){
		DisplayMetrics dm = new DisplayMetrics();
    	getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    	
    	LinearLayout sliderLayout = (LinearLayout)_view.findViewById(R.id.appsFragment_layout_imageSlider);
		sliderLayout.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 9 / 16));
    }
	
	private void showDialog(boolean isShow){
		if(isShow){
			if(_dialog != null){
				if(!_dialog.isShowing())
						_dialog.show();
			}else{
				_dialog = new LoginDialog(getActivity(), R.style.LoginTransparent);
				_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				_dialog.show();
			}
		}else{
			if(_dialog != null && _dialog.isShowing())
				_dialog.dismiss();
		}
	}
	@Override
	public void onCountryItemSelected() {
		_searchBar.releaseCountryToggle();
		_ptrlistView.setVisibility(View.VISIBLE);
		_layoutFilter.setVisibility(View.GONE);
		refreshListViewItem();
	}
	@Override
	public void onCategoryItemSelected() {
		_searchBar.releaseCategoryToggle();
		_ptrlistView.setVisibility(View.VISIBLE);
		_layoutFilter.setVisibility(View.GONE);
		refreshListViewItem();
	}
	@Override
	public void onCategoryItemFocus(int index, boolean isFocus) {
		if(isFocus){
			_ptrlistView.setVisibility(View.GONE);
			_layoutFilter.setVisibility(View.VISIBLE);
			_searchBar.releaseCountryToggle();
			showFlitterFragment(0, isFocus);
			_categoryFragment.showFilterList(SearchCategoryFliterFragment.SEARCH_APP);
		}else {
			_ptrlistView.setVisibility(View.VISIBLE);
			_layoutFilter.setVisibility(View.GONE);
			_categoryFragment.cycleCategoryGrid();
		}
		_searchBar.setCategoryToggleBackgroud();
	}
	@Override
	public void onCountryItemFocus(int index, boolean isFocus) {
		if (isFocus) {
			_ptrlistView.setVisibility(View.GONE);
			_layoutFilter.setVisibility(View.VISIBLE);
			_searchBar.releaseCategoryToggle();
			showFlitterFragment(1, isFocus);
			_countryFragment.showFilterList(SearchCategoryFliterFragment.SEARCH_APP);
		} else {
			_ptrlistView.setVisibility(View.VISIBLE);
			_layoutFilter.setVisibility(View.GONE);
			_countryFragment.cycleCountryList();
		}
		_searchBar.setCountryToggleBackgroud();
	}
	@Override
	public void onKeywordChanged(int index, String keyWord) {
		// TODO Auto-generated method stub
		
	}
	
}
