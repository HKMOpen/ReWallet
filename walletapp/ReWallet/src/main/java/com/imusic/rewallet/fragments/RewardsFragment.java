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
import android.view.ViewGroup.LayoutParams;
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
import com.imusic.rewallet.UI.RewardDetailActivity;
import com.imusic.rewallet.adapters.RewardChannelSTGVAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.fragments.SearchCategoryFliterFragment.onCategorySelectListener;
import com.imusic.rewallet.fragments.SearchCountryFliterFragment.onCountrySelectListener;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.ImageSliderItem;
import com.imusic.rewallet.model.RewardListItem;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.ExpandableHeightGridView;
import com.imusic.rewallet.widgets.ImageSlider;
import com.imusic.rewallet.widgets.LoginDialog;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.SearchBar;
import com.imusic.rewallet.widgets.SearchBar.SearchBarItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.wesoft.staggeredgridviewlibary.StaggeredGridView.StaggeredGridView;
//import com.wesoft.staggeredgridviewlibary.StaggeredGridView.StaggeredGridView.OnItemClickListener;
//import com.wesoft.staggeredgridviewlibary.pulltorefresh.library.PullToRefreshBase;
//import com.wesoft.staggeredgridviewlibary.pulltorefresh.library.PullToRefreshStaggeredGridView;


public class RewardsFragment extends DataBaseFragment implements OnRefreshListener2<ScrollView>, SearchBarItemClickListener, onCategorySelectListener, onCountrySelectListener{
	private final static String TAG = "RewardChannelFragment";
	
	private SearchBar _searchBar;
    private PullToRefreshVerticalScrollView _ptrGridview;
    private ExpandableHeightGridView _gridView;
    private RewardChannelSTGVAdapter _lstAdapter;
	private View _view;
	private ImageSlider _imgSlider;
	private ArrayList<ImageSliderItem> _items;
//	private RelativeLayout _layout;
//	private DataProcessCover _cover;
	
	private LoginDialog _dialog;
	
	private SearchCategoryFliterFragment _categoryFragment;
	private SearchCountryFliterFragment _countryFragment;
	private FrameLayout _layoutFilter;
	
	private boolean _isRefreshSlider;
	private int _pIndex = 1;
	private boolean isInit;
	private float _time_to_next = 3000;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		System.out.println("RewardFragment onCreateView");
		isInit = true;
		_view = inflater.inflate(R.layout.fragment_rewards, container , false);
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
			Vlog.getInstance().debug(false, TAG, "rewards fragment on resumt---------------");
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
	public void pauseImageSlider(boolean isStop){
		if(_imgSlider == null)
			return;
		if(isStop){
			_imgSlider.onStopImageSwitch();
		}else{
			_imgSlider.switchNextImage();
		}
	}
	
	private void setupView(){
//		_cover = new DataProcessCover(getActivity().getApplicationContext(), null);
//		_layout = (RelativeLayout)_view.findViewById(R.id.rewardChannel_layout);
		
		_lstAdapter = new RewardChannelSTGVAdapter(getActivity(), null);
//		_layout.addView(_cover);
//		getGridViewNewItem();
//		getImageSliderNewItem();
		_imgSlider = (ImageSlider)_view.findViewById(R.id.rewardChannel_imageSlider);
		
		//_imgSlider = new ImageSlider(getActivity(), null);
		_ptrGridview = (PullToRefreshVerticalScrollView)_view.findViewById(R.id.rewardChannel_pull_refresh_scrollview);
		_ptrGridview.setMode(Mode.BOTH);
		_ptrGridview.getLoadingLayoutProxy().setPullLabel(getString(R.string.pull_to_refresh_pull));
		_ptrGridview.getLoadingLayoutProxy().setRefreshingLabel(getString(R.string.pull_to_refresh_refreshing));
		_ptrGridview.getLoadingLayoutProxy().setReleaseLabel(getString(R.string.pull_to_refresh_release));
		
		_gridView = (ExpandableHeightGridView)_view.findViewById(R.id.rewardChannel_gridview);//_ptrGridview.getRefreshableView();
		_gridView.setExpanded(true);
		
		_gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		
		
		_ptrGridview.setOnRefreshListener(this);
//		_ptrGridview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
////				getGridViewNewItem();
////				getImageSliderNewItem();
//				_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				_handler.obtainMessage(MSG_GETMOREDATE).sendToTarget();
//			}
//
//		});
		
		fitParams();
		_gridView.setAdapter(_lstAdapter);
		_gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onGirdViewItemClick(arg2);
//				_ptrGridview.setRefreshing(true);
			}
		});
		
		_imgSlider.setImageSliderItemOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//
				ImageSliderItem item = _imgSlider.getCurrentSliderItem();
				if(item == null)
					return;
				if(item.postId <=0)
					return;
				int type = 99;
				String t = item.type;
				if(t.equals("rewards")){
					type = RewardDetailActivity.TYPE_REWARD;
				}else if(t.equals("coupons"))
					type = RewardDetailActivity.TYPE_COUPON;
				else
					return;
				
				Intent intent = new Intent();
				intent.setClass(getActivity(), RewardDetailActivity.class);
				intent.putExtra("type", type);
		    	intent.putExtra("id", item.postId);
		    	intent.putExtra("lang", GlobalDataInfo.getInstance().getLanguage());
		    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    	getActivity().startActivity(intent);
			}
		});
		
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
	private void onGirdViewItemClick(int position){
		
		RewardListItem item = (RewardListItem)_lstAdapter.getItem(position);
		Log.e(TAG, position +  " on item app id click : " + item.id + " name:" + item.title); 
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
	private void fitParams(){
		DisplayMetrics dm = new DisplayMetrics();
    	getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    	
    	LinearLayout sliderLayout = (LinearLayout)_view.findViewById(R.id.rewardChannel_layout_imageSlider);
		sliderLayout.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels * 10 / 16));
    	
    	RelativeLayout.LayoutParams layoutParams =  new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	float w =0;
//    	if(Integer.valueOf(Build.VERSION.SDK_INT) >= 16)
    		w = dm.widthPixels ;//- getResources().getDimension(R.dimen.activity_horizontal_margin)*2 - _gridView.getHorizontalSpacing();
//    	else
//    		w = dm.widthPixels ;//- getResources().getDimension(R.dimen.activity_horizontal_margin)*2 - 5;
    	layoutParams.width = (int) w/2;
    	layoutParams.height = (int) w/2;
    	_lstAdapter.setImageLayoutParams(layoutParams);
    }
	public void setFragmentFreezed(boolean isFreezed){
		if (_view != null) {
			if (isFreezed) {
//				_view.setEnabled(false);
				//_view.setClickable(false);
				
			} else {
//				_view.setEnabled(true);
				//_view.setClickable(true);
//				ptrstgv.setEnabled(true);
			}
		}
	}
	public void refreshGridViewItem(){
//		if(!_cover.isShown())
//			_cover.setVisibility(View.VISIBLE);L
//		_gridView.setEnabled(false);
		//getGridViewNewItem(0);
		showDialog(true);
		_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
	}
	
	private void getGridViewNewItem(final int p) {
    	HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getRewardChannelList(
    							GlobalDataInfo.getInstance().getRewardsSearchCategoryId()
    							, GlobalDataInfo.getInstance().getRewardsSearchCountryId(),null
    							, GlobalDataInfo.getInstance().getLanguage()
    							,p));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
//				Vlog.getInstance().info(false,TAG, "reward list resposnse json succeed : "+response.toString());
				if(p == 1)
					parseReceviedData_refresh(response);
				else
					parseReceviedData_getMore(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "reward list  response json failed." + e.toString());
				showToast.getInstance(getActivity().getApplicationContext()).showMsg(e.getMessage());
				if(p == 1)
					parseReceviedData_refresh(null);
				else
					parseReceviedData_getMore(null);
			}
		},"");
    }
	
	private void getImageSliderNewItem(){
		//Vlog.getInstance().info(false,TAG, " ImageSlider request: " + Constants.Url.getRewardChannelSlider(3,14,GlobalDataInfo.getInstance().getLanguage()));
    	HttpRequest request = HttpRequestHelper
    			.getGetRequest(Constants.Url
    					.getRewardChannelSliderUrl(
    							GlobalDataInfo.getInstance().getRewardsSearchCountryId(),
    							GlobalDataInfo.getInstance().getRewardsSearchCategoryId(),
    							GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				//Vlog.getInstance().info(false,TAG, "ImageSlider resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "ImageSlider response json failed." + e.toString());
				showToast.getInstance(getActivity().getApplicationContext()).showMsg(e.getMessage());
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
//				float nextToTime;
				int width = 0;
				int height = 0;
				String post_type = null;
				JSONArray sliders;
				if(datas != null){
					for(int i = 0; i < datas.length(); i++){
						data =  datas.getJSONObject(i);
						if(data != null){
							if(data.has("post_type")){
//								if(!data.isNull("post_type")){
//									post_type = data.getString("post_type");
//									if(!post_type.equals("rewards")
//											|| !post_type.equals("coupons"))
//										continue;
//								}
							}
							post_id = data.getInt("post_id");
							id = data.getInt("id");
							collection_name = data.getString("collection_name");
//							nextToTime = data.getLong("time_to_next");
							sliders = data.getJSONArray("slides");
							if(sliders != null){
								for(int j = 0; j < sliders.length(); j++){
									url = ((JSONObject)sliders.get(j)).getString("url");
									if(url == null || url.length() < 1)
										continue;
//									if(url != null){
////										width = ((JSONObject)sliders.get(j)).getInt("width");
////										height = ((JSONObject)sliders.get(j)).getInt("height");
//									}else{
//										width = 0;
//										height = 0;
//									}
									
									item = new ImageSliderItem(post_id,id,
											collection_name, url,
											width, height, _time_to_next
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
				showToast.getInstance(getActivity().getApplicationContext()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getActivity().getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		
	}
	private void refreshImageSlider(){
		if(_items.size() > 0){
			Vlog.getInstance().error(false, TAG, "ImageSlider count:"+ _items.size());
			_imgSlider.setContent(_items);
		}else{
			Vlog.getInstance().error(false, TAG, "no slider item......");
		}
		int h =  _imgSlider.getLayoutParams().height;
		int w = _imgSlider.getLayoutParams().width;
		Vlog.getInstance().error(false, TAG, "slider size : " + w + ", height: "+h);
	}
	
	
	//- ---------------------------------------------------------------
	@Override
	protected void onRefreshData() {
//		getImageSliderNewItem();
		//_imgSlider.onStopImageSwitch();
		_isRefreshSlider = false;
		_pIndex = 1;
		getGridViewNewItem(_pIndex);
		getImageSliderNewItem();
	}
//	private void setRefreshShowing(){
//		
//		
//		_ptrGridview.setRefreshing(true);
//		new Thread() {
//	        public void run() {
//	                try {
//	                    getActivity().runOnUiThread(new Runnable() {
//
//	                        @Override
//	                        public void run() {
//	                        	_ptrGridview.setRefreshing(true); 
//	                        }
//	                    });
//	                } catch (Exception e) {
//	                    e.printStackTrace();
//	                }
//	        }
//	    }.start();
//	}
	
	@Override
	protected void onGetMoreData() {
		getGridViewNewItem(_pIndex + 1);
	}
	@Override
	protected void onPrepareData() {
		Vlog.getInstance().info(false, TAG, "no saved data.. get new data ----");
		_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
	}
	@Override
	protected void onRefreshDataCompleted() {
		if(_ptrGridview.isRefreshing())
			_ptrGridview.onRefreshComplete();
		_gridView.setEnabled(true);
		_pIndex = 1;
		_ptrGridview.getRefreshableView().scrollTo(0, 0);
		if(_isRefreshSlider)
			refreshImageSlider();
		showDialog(false);
	} 
	@Override
	protected void onGetMoreDataCompleted() {
		if(_ptrGridview.isRefreshing())
			_ptrGridview.onRefreshComplete();
		_gridView.setEnabled(true);
	}
	@Override
	protected void parseReceviedData_getMore(JSONObject response) {
		if(response != null){
			_lstAdapter.addMoreItemToList(response);
			_pIndex++;
		}
		_handler.obtainMessage(MSG_GETMOREDATACOMPLETED).sendToTarget();
	}
	@Override
	protected void parseReceviedData_refresh(JSONObject response) {
		if(response != null){
			_lstAdapter.refreshItemList(response);
		}
		_handler.obtainMessage(MSG_REFEASHDATACOMPLETED).sendToTarget();
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		_handler.obtainMessage(MSG_GETMOREDATE).sendToTarget();
		
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
	public void onCategoryItemFocus(int index, boolean isFocus) {
		if(isFocus){
			_ptrGridview.setVisibility(View.GONE);
			_layoutFilter.setVisibility(View.VISIBLE);
			_searchBar.releaseCountryToggle();
			showFlitterFragment(0, isFocus);
			_categoryFragment.showFilterList(SearchCategoryFliterFragment.SEARCH_REWARD);
		}else {
			_ptrGridview.setVisibility(View.VISIBLE);
			_layoutFilter.setVisibility(View.GONE);
			_categoryFragment.cycleCategoryGrid();
		}
		_searchBar.setCategoryToggleBackgroud();
	}
	@Override
	public void onCountryItemFocus(int index, boolean isFocus) {
		if (isFocus) {
			_ptrGridview.setVisibility(View.GONE);
			_layoutFilter.setVisibility(View.VISIBLE);
			_searchBar.releaseCategoryToggle();
			showFlitterFragment(1, isFocus);
			_countryFragment.showFilterList(SearchCategoryFliterFragment.SEARCH_REWARD);
		} else {
			_ptrGridview.setVisibility(View.VISIBLE);
			_layoutFilter.setVisibility(View.GONE);
			_countryFragment.cycleCountryList();
		}
		_searchBar.setCountryToggleBackgroud();
	}
	@Override
	public void onKeywordChanged(int index, String keyWord) {
		
	}
	@Override
	public void onCountryItemSelected() {
		_searchBar.releaseCountryToggle();
		_ptrGridview.setVisibility(View.VISIBLE);
		_layoutFilter.setVisibility(View.GONE);
		refreshGridViewItem();
	}
	@Override
	public void onCategoryItemSelected() {
		_searchBar.releaseCategoryToggle();
		_ptrGridview.setVisibility(View.VISIBLE);
		_layoutFilter.setVisibility(View.GONE);
		refreshGridViewItem();
	}

}
