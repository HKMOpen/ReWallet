package com.imusic.rewallet.UI;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.adapters.MyRewardsListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.fragments.MyRewardsListFragment_All;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.RedemptionHistoryItem;
import com.imusic.rewallet.utils.CategoryCountryUtil;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.LoginDialog;
import com.imusic.rewallet.widgets.MainActionBar;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;

public class MyRewarsListActivity extends FragmentActivity implements OnClickListener {

	private static final int REQUESTCODE = 233501;
	
	public final static int STATUS_P_PROCESSING = 0;
	public final static int STATUS_P_SUCCESS = 1;
	public final static int STATUS_P_FAILURE = 2;
	public final static int STATUS_C_SUCCEES_CLIAMED = 1;
	public final static int STATUS_C_SUCCEES_UNCLIAMED = 0;
	
	private final static String TAG = "MyRewarsListActivity" ;
	
	private static int _tabIndex;
	protected TextView _txtNavTitle;
	protected ImageButton _imgbtnNaviBack;
	
	private Button _btnAll;
	private Button _btnClaimed;
	private Button _btnUnClaimed;
//	private MyRewardsListFragment_All _fragmentAll;
//	private MyRewardsListFragment_Claimed _fragmentClaimed;
//	private MyRewardsListFragment_unClaimed _fragmentUnclaimed;
	
	
//	private ListView _lvAllData;
//	private ListView _lvClaimed;
//	private ListView _lvUnClaimed;
//	private MyRewardsListAdapter _allDataAdapter;
//	private MyRewardsListAdapter _claimedDataAdapter;
//	private MyRewardsListAdapter _unClaimedDataAdapter;
	
	private ArrayList<RedemptionHistoryItem> _lstAllData;
	private ArrayList<RedemptionHistoryItem> _lstClaimedData;
	private ArrayList<RedemptionHistoryItem> _lstUnClaimedData;
	private ViewPager _viewPager;
	private LoginDialog _dialog;
	private ActionBar _actionBar;
	private TabFragmentPagerAdapter _tabAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myrewards_list);
		setupView();
		showDialog(true);
		_handler.obtainMessage(MSG_GETDATA).sendToTarget();
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		boolean isRefresh = getIntent().getExtras().getBoolean("isrefrash");
		if(isRefresh){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
			showDialog(true);
		}
	}
	private void setupActionbar(){
		_actionBar = getActionBar();
		_actionBar.setDisplayShowCustomEnabled(true);
		_actionBar.setDisplayShowTitleEnabled(false);
		_actionBar.setDisplayShowHomeEnabled( false );
		_actionBar.setCustomView(R.layout.view_actionbar_base);
		_txtNavTitle = (TextView) findViewById(R.id.navigator_tvTitle);
		if(_txtNavTitle != null)
			_txtNavTitle.setText(getString(R.string.redemptionHistoryActivity_title));
		_imgbtnNaviBack = (ImageButton) findViewById(R.id.navigator_imgbtnBack);
		if(_imgbtnNaviBack != null){
				_imgbtnNaviBack.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		}
		Button _btnRight = (Button)findViewById(R.id.navigator_btnRight);
		_btnRight.setVisibility(View.GONE);
	}
	private void setupView(){
//		setupActionBar(getString(R.string.redemptionHistoryActivity_title)
//				,true, false, null ,null); 
//		_actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		setupActionbar();
		
		_viewPager = (ViewPager)findViewById(R.id.myRewards_viewpager);
		
		_tabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());  
		_viewPager.setAdapter(_tabAdapter);
		_viewPager.setOffscreenPageLimit(3);
//		_viewPager.setCurrentItem(0);
		_viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
//				_actionBar.setSelectedNavigationItem(arg0);
				
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageSelected(int arg0) {
				changeSelectedTab(arg0);
			}  
		});
//		_actionBar.addTab(_actionBar
//				.newTab().setText("TAB1")
//				.setTabListener(this));
//		_actionBar.addTab(_actionBar
//				.newTab().setText("TAB2")
//				.setTabListener(this));
//		_actionBar.addTab(_actionBar
//				.newTab().setText("TAB3")
//				.setTabListener(this));
		
		_btnAll = (Button )findViewById(R.id.redemptionHistory_btnAll);
		_btnAll.setOnClickListener(this);
		_btnClaimed = (Button )findViewById(R.id.redemptionHistory_btnClaimed);
		_btnClaimed.setOnClickListener(this);
		_btnUnClaimed = (Button )findViewById(R.id.redemptionHistory_btnUnClaimed);
		_btnUnClaimed.setOnClickListener(this);
//
//		_lvAllData = (ListView)findViewById(R.id.redemptionHistory_lvAll);
//		_lvClaimed = (ListView)findViewById(R.id.redemptionHistory_lvClaimed);
//		_lvUnClaimed = (ListView)findViewById(R.id.redemptionHistory_lvUnClaimed);
//
//		_allDataAdapter = new  MyRewardsListAdapter(this,_lstAllData, MyRewardsListAdapter.TYPE_ALL);
//		_claimedDataAdapter = new  MyRewardsListAdapter(this,_lstClaimedData, MyRewardsListAdapter.TYPE_CLIAMED);
//		_unClaimedDataAdapter = new  MyRewardsListAdapter(this,_lstUnClaimedData , MyRewardsListAdapter.TYPE_UNCLIAMED);
//		
//		_lvAllData.setAdapter(_allDataAdapter);
//		_lvClaimed.setAdapter(_claimedDataAdapter);
//		_lvUnClaimed.setAdapter(_unClaimedDataAdapter);
//		
//		_lvAllData.setOnItemClickListener(this);
//		_lvUnClaimed.setOnItemClickListener(this);
//		_lvClaimed.setOnItemClickListener(this);
//		
		changeSelectedTab(0);
//		changedFragment(0);
		_tabIndex = 0;
	}
//	private void hideAllListView(){
//		_lvAllData.setVisibility(View.GONE);
//		_lvClaimed.setVisibility(View.GONE);
//		_lvUnClaimed.setVisibility(View.GONE);
//	}
	private void changeSelectedTab(int index){
		setAllTabTransparent();
//		hideAllListView();
		switch(index){
		case 0:
			_btnAll.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg_highlight));
			_btnAll.setTextColor(getResources().getColor(R.color.global_Text_Title));
//			_lvAllData.setVisibility(View.VISIBLE);
			break;
		case 1:
			_btnUnClaimed.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg_highlight));
			_btnUnClaimed.setTextColor(getResources().getColor(R.color.global_Text_Title));
//			_lvUnClaimed.setVisibility(View.VISIBLE);
			break;
		case 2:
			_btnClaimed.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg_highlight));
			_btnClaimed.setTextColor(getResources().getColor(R.color.global_Text_Title));
//			_lvClaimed.setVisibility(View.VISIBLE);
			break;
		}
	}
	private void setAllTabTransparent(){
		_btnAll.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg));
		_btnAll.setTextColor(getResources().getColor(R.color.global_Text_SubTitle));
		_btnClaimed.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg));
		_btnClaimed.setTextColor(getResources().getColor(R.color.global_Text_SubTitle));
		_btnUnClaimed.setBackgroundColor(getResources().getColor(R.color.myRewards_tabMenu_Bg));
		_btnUnClaimed.setTextColor(getResources().getColor(R.color.global_Text_SubTitle));
		
//		
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		_fragmentClaimed = new MyRewardsListFragment_Claimed();
//		ft.add(R.id.redemptionHistory_layoutContent, _fragmentClaimed, "claimed");
//		_fragmentUnclaimed = new MyRewardsListFragment_unClaimed();
//		ft.add(R.id.redemptionHistory_layoutContent, _fragmentUnclaimed, "unclaimed");
//		_fragmentAll = new MyRewardsListFragment_All();
//		ft.add(R.id.redemptionHistory_layoutContent, _fragmentAll, "all");
//		ft.commit();
	}
//	private void changedFragment(int index){
//		clearAllFragment();
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		switch (index){
//		case 0:
//			if(_fragmentAll != null){
//				ft.show(_fragmentAll);
//			}else{
//				_fragmentAll = new MyRewardsListFragment_All();
//				ft.add(R.id.redemptionHistory_layoutContent, _fragmentAll, "all");
//			}
//			break;
//		case 1:
//			if(_fragmentUnclaimed != null){
//				ft.show(_fragmentUnclaimed);
//			}else{
//				_fragmentUnclaimed = new MyRewardsListFragment_unClaimed();
//				ft.add(R.id.redemptionHistory_layoutContent, _fragmentUnclaimed, "unclaimed");
//			}
//			break;
//		case 2:
//			if(_fragmentClaimed != null){
//				ft.show(_fragmentClaimed);
//			}else{
//				_fragmentClaimed = new MyRewardsListFragment_Claimed();
//				ft.add(R.id.redemptionHistory_layoutContent, _fragmentClaimed, "claimed");
//			}
//			break;
//		}
//		ft.commit();
//	}
//	private void clearAllFragment(){
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		if(_fragmentAll != null)
//			ft.hide(_fragmentAll);
//		if(_fragmentClaimed != null)
//			ft.hide(_fragmentClaimed);
//		if(_fragmentUnclaimed != null)
//			ft.hide(_fragmentUnclaimed);
//		ft.commit();
//	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.redemptionHistory_btnAll:
			if(_tabIndex  == 0) return;
			changeSelectedTab(0);
			_viewPager.setCurrentItem(0);
//			changedFragment(0);
			_tabIndex = 0;
			break;
		case R.id.redemptionHistory_btnUnClaimed:
			if(_tabIndex  == 1) return;
			changeSelectedTab(1);
			_viewPager.setCurrentItem(1);
//			changedFragment(1);
			_tabIndex = 1;
			break;
		case R.id.redemptionHistory_btnClaimed:
			if(_tabIndex  == 2) return;
			changeSelectedTab(2);
			_viewPager.setCurrentItem(2);
//			changedFragment(2);
			_tabIndex = 2;
			break;
		}
	}
	
	
	
//	@Override
	protected void getData() {
		Log.i(TAG, "getdata -------");
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url
						.getMyRewardHistoryUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "MyRewarsListActivity resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "MyRewarsListActivity response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"MyRewarsListActivity");
	}
	protected void parseData(JSONObject response) {
		boolean isSucceed = false;
		if(response != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				int result = response.getInt("result");
				if(result == 1){
					_lstAllData = new ArrayList<RedemptionHistoryItem>();
					_lstClaimedData = new ArrayList<RedemptionHistoryItem>();
					_lstUnClaimedData = new ArrayList<RedemptionHistoryItem>();
					JSONArray aryData = response.getJSONArray("data");
					if(aryData != null){
						RedemptionHistoryItem item;
						int id = 0;
						String image_sq = null;
						String title = null;
						int coin;
						int p_status;
						int c_status;
						String vendor_name = null;
						String vendor_url = null;
						String desc = null;
						String exp_date = null;
						JSONArray aryCountry = null;
						JSONArray aryCategory = null;
						String cDesc = null;
						String cImage = null;
						String country = null;
						String category = null;
						String qr_a = null;
						String qr_b = null;
						int handle = -1;
						String banner = null;
						String videoCover = null;
						for(int i = 0; i < aryData.length(); i++){
							p_status = aryData.getJSONObject(i).getInt("p_status");
							if(p_status == STATUS_P_FAILURE)
								continue;
							c_status = aryData.getJSONObject(i).getInt("c_status");
							qr_a = aryData.getJSONObject(i).getString("qr_a");
							qr_b = aryData.getJSONObject(i).getString("qr_b");
							handle = aryData.getJSONObject(i).getInt("handle");
							coin = aryData.getJSONObject(i).getInt("amount");
							JSONObject detail = aryData.getJSONObject(i).getJSONObject("detail");
							if(detail != null){
								id = i;//= detail.getInt("handle");
								image_sq = detail.getString("image_small_thumb");
								title = detail.getString("title");
								vendor_name = detail.getString("vendor_name");
								vendor_url = detail.getString("product_url");
								desc = detail.getString("product_description");
								exp_date = detail.getString("exp_date");
								aryCountry = detail.getJSONArray("country");
								cDesc = null;
								cImage = null;
								country = null;
								category = null;
								banner = detail.getString("image_banner");
								videoCover = detail.getString("image_video_cover");
								if(aryCountry != null){
									String[] cy = CategoryCountryUtil.getCountryDescription(this,MainActionBar.MAINACTIONBAR_REWARDS
											, aryCountry, "name", "description");
									cDesc = cy[1];
									country = cy[0];
								}
								aryCategory = detail.getJSONArray("category");
								if(aryCategory != null){
									String[] cc = CategoryCountryUtil.getCategory(this, MainActionBar.MAINACTIONBAR_REWARDS, aryCategory, "term_id", "name", "unpress_s");
									category = cc[0];
									cImage = cc[1];
								}
							}
							if(exp_date == null || exp_date.trim().length() <=0){
								exp_date = null;
							}
							item = new RedemptionHistoryItem(id, title, 
									image_sq, vendor_name, 
									exp_date == null? null:  format.parse(exp_date), 
											cDesc, coin, desc, vendor_url);
							item.videoCover = videoCover;
							item.imageBanner = banner;
							item.categoryImage = cImage;
							item.country = country;
							item.countryDescription = cDesc;
							item.category = category;
							item.p_status = p_status;
							item.c_status = c_status;
							item.qr_a = qr_a;
							item.qr_b = qr_b;
							item.handle = handle;
							_lstAllData.add(item);
//							if(item.status == STATUS_CLIAMED){
//								_lstClaimedData.add(item);
//							}else if(item.status == STATUS_UNCLIAMED){
//								_lstUnClaimedData.add(item);
//							}
							if(item.c_status == STATUS_C_SUCCEES_CLIAMED 
									&& item.p_status == STATUS_P_SUCCESS){
								_lstClaimedData.add(item);
							}else if(item.c_status == STATUS_C_SUCCEES_UNCLIAMED
									&& item.p_status == STATUS_P_SUCCESS){
								_lstUnClaimedData.add(item);
							}
						}
					}
					isSucceed = true;
				}else if(result == ResponseResultCode.RESULT_EXPIREDATE){
					Intent intent = new Intent();
					intent.setClass(this, LoginActivity.class);
					startActivityForResult(intent, REQUESTCODE);
				}else{
					showToast.getInstance(getApplicationContext()).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		_handler.obtainMessage(MSG_REFRESHUI).sendToTarget();
	}

	private void refreshView(){
//		_fragmentAll.resetDataList(_lstAllData);
//		_fragmentClaimed.setDatalist(_lstClaimedData);
//		_fragmentUnclaimed.setDatalist(_lstUnClaimedData);
		
		ArrayList<RedemptionHistoryItem> tempListTop =null;
		ArrayList<RedemptionHistoryItem> tempList = null;
		RedemptionHistoryItem tempItem;
		if (_lstAllData != null && _lstAllData.size() > 0) {
			tempListTop = new ArrayList<RedemptionHistoryItem>();
			tempList = new ArrayList<RedemptionHistoryItem>();
			for(int i = 0; i < _lstAllData.size(); i++){
				tempItem = _lstAllData.get(i);
				if(tempItem.p_status == STATUS_P_PROCESSING){
					tempListTop.add(tempItem);
				}else if(tempItem.p_status == STATUS_P_SUCCESS){
					tempList.add(tempItem);
				}
			}
			_lstAllData.clear();
			if(tempListTop.size() > 0){
				_lstAllData.addAll(0, tempListTop);
			}
			if(tempList.size() > 0){
				_lstAllData.addAll(tempList);
			}
			tempList = null;
			tempListTop = null;
		}
//		if (_lstClaimedData != null && _lstClaimedData.size() > 0) {
//			tempListTop = new ArrayList<RedemptionHistoryItem>();
//			tempList = new ArrayList<RedemptionHistoryItem>();
//			for(int i = 0; i < _lstClaimedData.size(); i++){
//				tempItem = _lstClaimedData.get(i);
//				if(tempItem.p_status == STATUS_P_PROCESSING){
//					tempListTop.add(tempItem);
//				}else if(tempItem.p_status == STATUS_P_SUCCESS){
//					tempList.add(tempItem);
//				}
//			}
//			_lstClaimedData.clear();
//			if(tempListTop.size() > 0){
//				_lstClaimedData.addAll(0, tempListTop);
//			}
//			if(tempList.size() > 0){
//				_lstClaimedData.addAll(tempList);
//			}
//			tempList = null;
//			tempListTop = null;
//		}
//		if (_lstUnClaimedData != null && _lstUnClaimedData.size() > 0) {
//			tempListTop = new ArrayList<RedemptionHistoryItem>();
//			tempList = new ArrayList<RedemptionHistoryItem>();
//			for(int i = 0; i < _lstUnClaimedData.size(); i++){
//				tempItem = _lstUnClaimedData.get(i);
//				if(tempItem.p_status == STATUS_P_PROCESSING){
//					tempListTop.add(tempItem);
//				}else if(tempItem.p_status == STATUS_P_SUCCESS){
//					tempList.add(tempItem);
//				}
//			}
//			_lstUnClaimedData.clear();
//			if(tempListTop.size() > 0){
//				_lstUnClaimedData.addAll(0, tempListTop);
//			}
//			if(tempList.size() > 0){
//				_lstUnClaimedData.addAll( tempList);
//			}
//			tempList = null;
//			tempListTop = null;
//		}
		
//		_allDataAdapter.setListData(_lstAllData, MyRewardsListAdapter.TYPE_ALL);
//		_allDataAdapter.notifyDataSetChanged();
//		_claimedDataAdapter.setListData(_lstClaimedData , MyRewardsListAdapter.TYPE_CLIAMED);
//		_claimedDataAdapter.notifyDataSetChanged();
//		_unClaimedDataAdapter.setListData(_lstUnClaimedData , MyRewardsListAdapter.TYPE_UNCLIAMED);
//		_unClaimedDataAdapter.notifyDataSetChanged();
		
		_tabAdapter.refreshAll(_lstAllData);
		_tabAdapter.refreshCliamed(_lstClaimedData);
		_tabAdapter.refreshUnCliamed(_lstUnClaimedData);
		
		
		showDialog(false);
	}
	

	protected final static int MSG_REFRESHUI = 1000;
	protected final static int MSG_GETDATA = 1001;
	protected Handler _handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFRESHUI:
				refreshView();
				break;
			case MSG_GETDATA:
				getData();
				break;
			}
		};
	};

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		RedemptionHistoryItem item = (RedemptionHistoryItem)arg0.getAdapter().getItem(position);//_lstData.get(position);
//		if(item == null)
//			return;
//		if(STATUS_P_SUCCESS != item.p_status)
//			return;
//		Intent intent = new Intent();
//		if(item.c_status == MyRewarsListActivity.STATUS_C_SUCCEES_CLIAMED)
//			intent.setClass(this, MyRewardDetailClaimedActivity.class);
//		else if(item.c_status == MyRewarsListActivity.STATUS_C_SUCCEES_UNCLIAMED)
//			intent.setClass(this, MyRewardDetailUnclaimedActivity.class);
//		else 
//			return;
//		intent.putExtra("item", item);
//		startActivity(intent);
//	} 
	
	private void showDialog(boolean isShow){
		if(isShow){
			if(_dialog != null){
				if(!_dialog.isShowing())
						_dialog.show();
			}else{
				_dialog = new LoginDialog(this, R.style.LoginTransparent);
				_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				_dialog.show();
			}
		}else{
			if(_dialog != null && _dialog.isShowing())
				_dialog.dismiss();
		}
	}
	@Override
	protected void onDestroy(){
		showDialog(false);
		_dialog = null;
		super.onDestroy();
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}
	
	
	public static class TabFragmentPagerAdapter extends FragmentStatePagerAdapter{

//		private List<Fragment> _lstFragments = new ArrayList<Fragment>();
		private MyRewardsListFragment_All _all; //=  new MyRewardsListFragment_All();
		private MyRewardsListFragment_All _unClaimed;// =  new MyRewardsListFragment_All();
		private MyRewardsListFragment_All _claimed;// = new MyRewardsListFragment_All();
		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
//			_lstFragments.add(_all);
//			_lstFragments.add(_unClaimed);
//			_lstFragments.add(_claimed);
		}
		@Override
		public Fragment getItem(int arg0) {
//			return _lstFragments == null? null : _lstFragments.get(arg0);
			switch (arg0) {
			case 0:
				_all =  new MyRewardsListFragment_All();
				return _all;
			case 1:
				_unClaimed =  new MyRewardsListFragment_All();
				return _unClaimed;
			case 2:
				_claimed = new MyRewardsListFragment_All();
				return _claimed;
			
			}
			return null;
		}
		@Override
		public int getCount() {
			return 3;
		} 
		public void refreshAll(ArrayList<RedemptionHistoryItem> lst){
			_all.resetListData(lst,  MyRewardsListAdapter.TYPE_ALL);
		}
		public void refreshUnCliamed(ArrayList<RedemptionHistoryItem> lst){
			_unClaimed.resetListData(lst,  MyRewardsListAdapter.TYPE_UNCLIAMED);
		}
		public void refreshCliamed(ArrayList<RedemptionHistoryItem> lst){
			_claimed.resetListData(lst, MyRewardsListAdapter.TYPE_CLIAMED);
		}
	}

	
}
