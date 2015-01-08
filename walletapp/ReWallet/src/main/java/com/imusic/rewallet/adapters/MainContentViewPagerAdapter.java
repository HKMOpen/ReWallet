package com.imusic.rewallet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.imusic.rewallet.fragments.AppsFragment;
import com.imusic.rewallet.fragments.RewardsFragment;

public class MainContentViewPagerAdapter extends FragmentPagerAdapter {

	private final static String TAG = "MainContentViewPagerAdapter";
//	private String[] _titles;
//	private ArrayList<Fragment> _fragments;
	private AppsFragment _appsFragemnt;
	private RewardsFragment _rewardFragment;
//	private MissionFragment _missionFragment;
	
	public MainContentViewPagerAdapter(FragmentManager fm) {
		super(fm);
//		InitData();
//		_fragments = new ArrayList<Fragment>();
	}
//	public MainContentViewPagerAdapter(FragmentManager fm, 
//			ArrayList<Fragment> lstFragment, String[] titles){
//		super(fm);
//		_fragments = lstFragment;
//		_titles = titles;
//	}
//	private void InitData(){
////		_fragments = new ArrayList<Fragment>();
//		Fragment f = new RewardsFragment();
//		_fragments.add(f);
//		Fragment ff = new AppsFragment();
////		_fragments.add(ff);
//		
////		_titles = new String[]{"RewardChannel","AppDownloads"};
//	}

	private Fragment getContentItem(int position){
		if(position == 0){
			if(_rewardFragment == null)
				_rewardFragment = new RewardsFragment();
			return _rewardFragment;
		}else{
			if(_appsFragemnt == null)
				_appsFragemnt = new AppsFragment();
			return _appsFragemnt;
		}
			
	}
	@Override
	public Fragment getItem(int arg0) {
		Log.e(TAG, "page title get item at:-------> "+arg0);
//		return _fragments.get(arg0);
		return getContentItem(arg0);
	}

	@Override
	public int getCount() {
		return 2;
	}

//	@Override
//	public CharSequence getPageTitle(int position) {
//		Log.e(TAG, "getPageTitle at :==========>" + position);
//		SpannableStringBuilder ssb = new SpannableStringBuilder(_titles[position]);
//		ForegroundColorSpan fcs = new ForegroundColorSpan(Color.GREEN);
//		ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//����������ɫ
//	    ssb.setSpan(new RelativeSizeSpan(1.4f), 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		return ssb;
//		//return _titles[position];
//	}

//	public void setImageSliderStop(int indexImageSlider, boolean isStop){
//		if(indexImageSlider == 0){
//			if(_rewardFragment != null){
//				_rewardFragment.pauseImageSlider(isStop);
//			}
//		}else if(indexImageSlider == 1){
//			if(_appsFragemnt != null){
//				_appsFragemnt.pauseImageSlider(isStop);
//			}
//		}
//	}
	public void setImageSliderStop(boolean isStop){
			if(_rewardFragment != null){
				_rewardFragment.pauseImageSlider(isStop);
			}
//			if(_appsFragemnt != null){
//				_appsFragemnt.pauseImageSlider(isStop);
//			}
	}
	
	public void refreshPage(int index){
		if(index == 0){
			if(_rewardFragment != null)
				_rewardFragment.refreshGridViewItem();
		}else if(index == 1){
			if(_appsFragemnt != null)
				_appsFragemnt.refreshListViewItem();
		}
	}

}
