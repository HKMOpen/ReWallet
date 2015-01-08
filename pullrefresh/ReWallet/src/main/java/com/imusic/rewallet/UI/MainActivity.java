package com.imusic.rewallet.UI;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.imusic.rewallet.R;
import com.imusic.rewallet.adapters.MainContentViewPagerAdapter;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.UserInfo;
import com.imusic.rewallet.utils.GcmDataManager;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.MainActionBar.MainActionBarListener;

//import com.imusic.vcoinsdk.apis.ErrorData;
//import com.imusic.vcoinsdk.apis.ResultData;
//import com.imusic.vcoinsdk.apis.VCoin;
//import com.imusic.vcoinsdk.apis.VCoinLoginResponseListener;

public class MainActivity extends FragmentActivity implements OnClickListener,  MainActionBarListener  {
	
	private final static String TAG = "MainActivity";

	public static int  ActivityRequestCode = 1200;
	
//	private ImageButton _imgbtnMyVcoin;
//	private ImageButton _imgbtnMyProfile;
//	private SearchBar _searchBar;
//	private SearchBar _searchBarRewards;
//	private SearchBar _searchBarApps;
	private MainActionBar _actionBar;
//	private AppsFragment _appsFragemnt;
//	private RewardsFragment _rewardFragment;
//	private MissionFragment _missionFragment;
//	private SearchCategoryFliterFragment _categoryFragment;
//	private SearchCountryFliterFragment _countryFragment;
//	private FrameLayout _layoutFilter;
	private ViewPager _viewPager;
	private MainContentViewPagerAdapter _viewpageAdapter;
	
	private GcmDataManager _gcmDataManager;
		
	public int getIndexTitile(){
		if(_actionBar != null)
			return _actionBar.getCurrentIndex();
		return 0;
	}
//	public SearchBar getSearchBar(){
////		return _actionBar.getCurrentIndex() == MainActionBar.MAINACTIONBAR_REWARDS?
////				 _searchBarRewards:  _searchBarApps;
//		return _searchBar;
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Util.initImageLoaderConfig(getApplicationContext());
//		if (savedInstanceState != null)
//			_contentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
//		if (_contentFragment == null){
//			
//		}
//		getSupportFragmentManager().beginTransaction()
//		.add(R.id.layout_mainContent, _contentFragment).commit();
		setupView();
		
		//BEGIN GCM
//		_gcmDataManager = new GcmDataManager();
//		_gcmDataManager.setContext(getApplicationContext(), this);
		
		//TODO: use the actual user email....
//		_gcmDataManager.register("busydavidszhou@sina.cn");
        //END GCM
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (LoginUserInfo.getInstance().getLoginUserInfo() != null) {
			outState.putSerializable("loginuserdata", LoginUserInfo.getInstance().getLoginUserInfo());
			outState.putString("token", LoginUserInfo.getInstance().getToken());
		}
		super.onSaveInstanceState(outState);
//		getSupportFragmentManager().putFragment(outState, "mContent", _contentFragment);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			LoginUserInfo.getInstance().setToken(savedInstanceState.getString("token"));
			LoginUserInfo.getInstance().setLoginUserInfo((UserInfo)savedInstanceState.get("loginuserdata"));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		Vlog.getInstance().debug(false, TAG, "Main activity onPause...");
//		if(_rewardFragment != null){
//			_rewardFragment.pauseImageSlider(true);
//		}
		if(_viewpageAdapter != null)
			_viewpageAdapter.setImageSliderStop(true);
	}
	@Override
	public void onResume(){
		super.onResume();
		Vlog.getInstance().debug(false, TAG, "Main activity onResume...");
//		if(_rewardFragment != null){
//			_rewardFragment.pauseImageSlider(false);
//		}
//		if(_actionBar.getCurrentIndex() == MainActionBar.MAINACTIONBAR_MISSIONS){
//			_missionFragment.refreshMissionList();
//		}

//		if(_searchBar != null){
//			_searchBar.releaseAllFocus();
//			Vlog.getInstance().info(false, TAG, "search bar release all focus....");
//		}
		if(_viewpageAdapter != null)
			_viewpageAdapter.setImageSliderStop(false);
		
		//BEGIN GCM
//		_gcmDataManager.refresh();
		//END FOR GCM
	}
	
	private void setupActionbar(){
		ActionBar bar = getActionBar();
		_actionBar = new MainActionBar(this, null);
		_actionBar.setMainActionBarListener(this);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled( false );
		bar.setCustomView(_actionBar);
		
//		FontManager.changeFonts(_actionBar, this);
	}
	
	private void setupView(){
		setupActionbar();

//		_imgbtnMyVcoin = (ImageButton)findViewById(R.id.main_imgbtnMyVcoin);
//		_imgbtnMyVcoin.setOnClickListener(this);
//		_imgbtnMyProfile = (ImageButton)findViewById(R.id.main_imgbtnMyProfile);
//		_imgbtnMyProfile.setOnClickListener(this);

//		RelativeLayout bg = (RelativeLayout)findViewById(R.id.main_layout);
//		FontManager.changeFonts(bg, this);
		
//		_searchBar = (SearchBar) findViewById(R.id.layout_searchBar);
//		_searchBar.setSearchMode(false);
//		_searchBar.setSearchIndex(MainActionBar.MAINACTIONBAR_APPS);
//		_searchBar.setFilterItemClickListener(this);
//		_searchBar.setCustomOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Intent inSearch = new Intent();
////				inSearch.setClass(MainActivity.this, SearchActivity.class);
////				inSearch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				inSearch.putExtra("index", _actionBar.getCurrentIndex());
////				startActivityForResult(inSearch, _requestCode);
//			}
//		});
		
		_viewPager = (ViewPager)findViewById(R.id.main_viewpager);
		_viewpageAdapter = new MainContentViewPagerAdapter(getSupportFragmentManager());
		_viewPager.setAdapter(_viewpageAdapter);
		_viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				_actionBar.setCurrentIndex(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		
		
//		_categoryFragment = new SearchCategoryFliterFragment(); 
//		_categoryFragment.setItemClickListener(this);
//		_countryFragment = new SearchCountryFliterFragment();
//		_countryFragment.setItemClickListener(this);
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		ft.add(R.id.layout_mainFilterContent,_categoryFragment);
//		ft.add(R.id.layout_mainFilterContent, _countryFragment);
//		ft.hide(_categoryFragment);
//		ft.hide(_countryFragment);
//		ft.commit();
//		_layoutFilter = (FrameLayout)findViewById(R.id.layout_mainFilterContent);
//		_layoutFilter.setVisibility(View.GONE);
		_actionBar.setCurrentIndex(MainActionBar.MAINACTIONBAR_REWARDS);
//		showMainFragement(MainActionBar.MAINACTIONBAR_REWARDS);
//		hideVcoinAndProfileButton(true);
		
//		FontManager.changeFonts(
//				(ViewGroup)((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0)
//				, this);
		 
	}


//	private void hideVcoinAndProfileButton(boolean isHide){
//		if(isHide){
//			if(_imgbtnMyVcoin != null)
//				_imgbtnMyVcoin.setVisibility(View.GONE);
//			if(_imgbtnMyProfile != null)
//				_imgbtnMyProfile.setVisibility(View.GONE);
//		}else{
//			if(_imgbtnMyVcoin != null)
//			_imgbtnMyVcoin.setVisibility(View.VISIBLE);
//			if(_imgbtnMyProfile != null)
//			_imgbtnMyProfile.setVisibility(View.VISIBLE);
//		}
//	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.main_imgbtnMyVcoin:
//			OverlayBtnUtil.myVcoinClick(this);
//			break;
//		case R.id.main_imgbtnMyProfile:
////			OverlayBtnUtil.myProfileClick(this);
//			break;
		}
	}

//	public void showMainFragement(int index){
//		hideAllFragment();
//		_searchBar.setVisibility(View.VISIBLE);
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		switch (index){
//		case MainActionBar.MAINACTIONBAR_REWARDS:
//			if (_rewardFragment != null){
//				ft.show(_rewardFragment);
//				_rewardFragment.setFragmentFreezed(false);
//			}
//			else {
//				_rewardFragment = new RewardsFragment();
//				ft.add(R.id.layout_mainContent, _rewardFragment);
//				_rewardFragment.setFragmentFreezed(false);
//			}
//			_searchBar.setSearchIndex(MainActionBar.MAINACTIONBAR_REWARDS);
//			break;
//		case MainActionBar.MAINACTIONBAR_APPS:
//			if (_appsFragemnt != null) 
//				ft.show(_appsFragemnt);
//			else {
//				_appsFragemnt = new AppsFragment();
//				ft.add(R.id.layout_mainContent, _appsFragemnt);
//			}
//			_searchBar.setSearchIndex(MainActionBar.MAINACTIONBAR_APPS);
//			break;
//		case MainActionBar.MAINACTIONBAR_MISSIONS:
//			if(_missionFragment != null)
//				ft.show(_missionFragment);
//			else{
//				_missionFragment = new MissionFragment();
//				ft.add(R.id.layout_mainContent, _missionFragment);
//			}
//			_searchBar.setVisibility(View.GONE);
//			_searchBar.setSearchIndex(MainActionBar.MAINACTIONBAR_MISSIONS);
//			break;
//		}
//		ft.commit();
//		hideVcoinAndProfileButton(false);
//		if(index == MainActionBar.MAINACTIONBAR_MISSIONS){
//			_missionFragment.refreshMissionList();
//		}
//	}
//	private void showFlitterFragment(int index, boolean isShow){
////		hideAllFragment();
//		if(isShow)
//			_layoutFilter.setVisibility(View.VISIBLE);
//		else
//			_layoutFilter.setVisibility(View.GONE);
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		if(_categoryFragment != null){
//			ft.hide(_categoryFragment);
//		}
//		if(_countryFragment != null){
//			ft.hide(_countryFragment);
//		}
//		switch (index){
//		case 0:
//			if(isShow){
//				if(_categoryFragment != null){
//				ft.show(_categoryFragment);
//				}
////				hideVcoinAndProfileButton(true);
//			}
//			else{
//				if(_countryFragment != null){
//					ft.hide(_countryFragment);
//				}
////				hideVcoinAndProfileButton(false);
//			}
//			break;
//		case 1:
//			if(isShow){
//				if(_countryFragment != null){
//					ft.show(_countryFragment);
//				}
////				hideVcoinAndProfileButton(true);
//			}
//			else{
//				if(_countryFragment != null){
//					ft.hide(_countryFragment);
//				}
////				hideVcoinAndProfileButton(false);
//			} 
//			break;
//		}
//		ft.commit();
//	}
	
	
//	private void hideAllFragment(){
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//			if(_missionFragment != null){
//				ft.hide(_missionFragment);
//			}
//			if (_appsFragemnt != null){
//				ft.hide(_appsFragemnt);
//			}
//			if (_rewardFragment != null){
//				ft.hide(_rewardFragment);
//			}
//			if(_categoryFragment != null){
//				ft.hide(_categoryFragment);
//			}
//			if(_countryFragment != null){
//				ft.hide(_countryFragment);
//			}
//		ft.commit();
//	}
	


//	@Override
//	public void onCountryItemSelected() {
////		showMainFragement(getIndexTitile());
//		_searchBar.releaseCountryToggle();
////		if(getIndexTitile() == MainActionBar.MAINACTIONBAR_REWARDS){
////			_rewardFragment.refreshGridViewItem();
////		}else if(getIndexTitile() == MainActionBar.MAINACTIONBAR_APPS){
////			_appsFragemnt.refreshListViewItem();
////		}
//		_viewPager.setVisibility(View.VISIBLE);
//		_layoutFilter.setVisibility(View.GONE);
//		_viewpageAdapter.refreshPage(_viewPager.getCurrentItem());
//	}
//
//
//	@Override
//	public void onCategoryItemSelected() {
////		showMainFragement(getIndexTitile());
//		_searchBar.releaseCategoryToggle();
////		if(getIndexTitile() == MainActionBar.MAINACTIONBAR_REWARDS){
////			_rewardFragment.refreshGridViewItem();
////		}else if(getIndexTitile() == MainActionBar.MAINACTIONBAR_APPS){
////			_appsFragemnt.refreshListViewItem();
////		}
//		_viewPager.setVisibility(View.VISIBLE);
//		_layoutFilter.setVisibility(View.GONE);
//		_viewpageAdapter.refreshPage(_viewPager.getCurrentItem());
//	}
//
//
//	@Override
//	public void onCategoryItemFocus(int index, boolean isFocus) {
//		if (isFocus) {
//			_viewPager.setVisibility(View.GONE);
//			_searchBar.releaseCountryToggle();
//			showFlitterFragment(0, isFocus);
//			_categoryFragment.showFilterList(_viewPager.getCurrentItem());
//		} else {
////			showMainFragement(getIndexTitile());
//			_viewPager.setVisibility(View.VISIBLE);
//		}
//		_searchBar.setCategoryToggleBackgroud();
//	}
//
//
//	@Override
//	public void onCountryItemFocus(int index, boolean isFocus) {
//		if (isFocus) {
//			_viewPager.setVisibility(View.GONE);
//			_searchBar.releaseCategoryToggle();
//			showFlitterFragment(1, isFocus);
//			_countryFragment.showFilterList(_viewPager.getCurrentItem());
//		} else {
////			showMainFragement(getIndexTitile());
//			_viewPager.setVisibility(View.VISIBLE);
//		}
//		_searchBar.setCountryToggleBackgroud();
//	}
//
//
//	@Override
//	public void onKeywordChanged(int index, String keyWord) {
//		
//	}
	@Override
	public void onTitleChanged(int index) {
//		showMainFragement(getIndexTitile());
//		_searchBar.setSearchIndex(index);
//		_searchBar.releaseAllFocus();
		_viewPager.setCurrentItem(index);
	}
	
	
	private long exitTime = 0;
	@Override
	public void onBackPressed() {
//		boolean isSearchStatus =  _searchBar.getSearchBarIsSearchStatus();
//		if(isSearchStatus){
//			if(_viewPager.getVisibility()==View.GONE){
//				_viewPager.setVisibility(View.VISIBLE);
//				_layoutFilter.setVisibility(View.GONE);
//			}
//			_searchBar.releaseAllFocus();
//		}else {
			if ((System.currentTimeMillis() - exitTime) > 2000){
                    Toast.makeText(this, getString(R.string.mainActivity_doubleClickToExit), Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
            }else{
                    finish();
                    System.exit(0);
            }
//			super.onBackPressed();
//		}
	}



//	private static Boolean isExit = false; 
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			boolean isSearchStatus =  _searchBar.getSearchBarIsSearchStatus();
//			if(isSearchStatus){
////				showMainFragement(getIndexTitile());
//				if(_viewPager.getVisibility()==View.GONE){
//					_viewPager.setVisibility(View.VISIBLE);
//					_layoutFilter.setVisibility(View.GONE);
//				}
//				_searchBar.releaseAllFocus();
//				return false;
//			}else{
//				Timer tExit = null;  
//			    if (isExit == false) {  
//			        isExit = true; // 准备退出  
//			        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
//			        tExit = new Timer();  
//			        tExit.schedule(new TimerTask() {  
//			            @Override  
//			            public void run() {  
//			                isExit = false; // 取消退出  
//			            }  
//			        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
//			  
//			    } else {  
//			        finish();  
//			        System.exit(0);  
//			    }  
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	@Override
	public void onDestroy(){
//		LoginUserInfo.getInstance().cleanInfo();
//		GlobalDataInfo.getInstance().clearAllData();
		_actionBar = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Vlog.getInstance().debug(false, TAG,
				"~~~~~~~~~~ onActivityResult--------");
		if (requestCode == ActivityRequestCode && resultCode == RESULT_OK) {
			if (_actionBar.getCurrentIndex() == MainActionBar.MAINACTIONBAR_REWARDS) {
//				_rewardFragment.refreshGridViewItem();
			} else if (_actionBar.getCurrentIndex() == MainActionBar.MAINACTIONBAR_APPS) {
//				_appsFragemnt.refreshListViewItem();
			}
		}
		_actionBar.resetUserProfileImage();
	}	

}
