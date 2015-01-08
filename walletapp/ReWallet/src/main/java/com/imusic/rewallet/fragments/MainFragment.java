package com.imusic.rewallet.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imusic.rewallet.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainFragment extends Fragment{

	private final String TAG = "MainFragment";
	private View _view;
	private ViewPager _viewPager;
	private PagerTitleStrip _pagerTitle;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		if(_view == null){
			_view = inflater.inflate(R.layout.fragment_maincontent, container , false);
			setupView();
		}
		return _view;
	}

	private void setupFragment(){
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		Fragment f = new RewardsFragment();
		fragments.add(f);
		Fragment ff = new AppsFragment();
		fragments.add(ff);
		String[] titles = new String[]{"RewardChannel","AppDownloads"};
	}
	private void setupView(){
		_viewPager = (ViewPager)_view.findViewById(R.id.mainContent_viewpager);
		_pagerTitle = (PagerTitleStrip)_view.findViewById(R.id.mainContent_pagertitle);
		_pagerTitle.setTextColor(Color.WHITE);
		_pagerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		//_viewPager.setAdapter(new MainContentViewPagerAdapter(getFragmentManager()));
		_viewPager.setOnPageChangeListener(new OnViewPageChangeListener());
		
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		Fragment f = new RewardsFragment();
		fragments.add(f);
		Fragment ff = new AppsFragment();
		fragments.add(ff);
		String[] titles = new String[]{"RewardChannel","AppDownloads"};
//		_viewPager.setAdapter(new MainContentViewPagerAdapter(getFragmentManager(), fragments, titles));
	}
	
	private void changeTitle(){
		
	}
	
	
	@Override
    public void onDetach() {
    	super.onDetach();
    	try {
    	    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);

    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
    }
	
	private class OnViewPageChangeListener implements  OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		@Override
		public void onPageSelected(int arg0) {
			
		}
	}
	
}
