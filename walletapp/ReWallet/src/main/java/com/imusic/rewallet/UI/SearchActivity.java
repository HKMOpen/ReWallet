package com.imusic.rewallet.UI;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.fragments.SearchCategoryFliterFragment;
import com.imusic.rewallet.fragments.SearchCategoryFliterFragment.onCategorySelectListener;
import com.imusic.rewallet.fragments.SearchCountryFliterFragment;
import com.imusic.rewallet.fragments.SearchCountryFliterFragment.onCountrySelectListener;
import com.imusic.rewallet.fragments.SearchFragment;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.widgets.MainActionBar;
import com.imusic.rewallet.widgets.SearchActionbar;
import com.imusic.rewallet.widgets.SearchActionbar.SearchIndexChangeListener;
import com.imusic.rewallet.widgets.SearchBar;
import com.imusic.rewallet.widgets.SearchBar.SearchBarItemClickListener;

public class SearchActivity extends FragmentActivity 
		implements onCategorySelectListener, onCountrySelectListener,
		SearchBarItemClickListener{

	private String TAG = "SearchActivity";
	
	private String _keyword = null;
	
	
	private SearchBar _searchBar;
	private SearchActionbar _actionBar;
	private SearchFragment _fragment;
	
	private SearchCategoryFliterFragment _categoryFragment;
	private SearchCountryFliterFragment _countryFragment;
	private int _currentSearchIndex = MainActionBar.MAINACTIONBAR_REWARDS;
	
	public int getCurrentSearchIndex(){
		return _actionBar.getCurrentSearchIndex();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras() != null){
			_currentSearchIndex = getIntent().getExtras().getInt("index");
		}
		
		setContentView(R.layout.activity_search);
		setupView();
	}

	private void setupView(){
		setupActionbar();
		_searchBar = (SearchBar) findViewById(R.id.search_searchBar);
		_searchBar.setSearchMode(true);
		_searchBar.setSearchIndex(_currentSearchIndex);
		_searchBar.setFilterItemClickListener(this);
		_fragment = new SearchFragment();
		_categoryFragment = new SearchCategoryFliterFragment();
		_categoryFragment.setItemClickListener(this);
		_countryFragment = new SearchCountryFliterFragment();
		_countryFragment.setItemClickListener(this);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.search_mainContent, _categoryFragment );
		ft.add(R.id.search_mainContent, _countryFragment );
		ft.add(R.id.search_mainContent, _fragment );
		ft.commit();
	}
	
	private void setupActionbar(){
		_actionBar = new SearchActionbar(getApplicationContext(),null);
		_actionBar.setCurrentIndex(_currentSearchIndex);
		_actionBar.setBackbuttonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ActionBar bar = getActionBar();
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled( false );
		bar.setCustomView(_actionBar);
		_actionBar.setSearchIndexChangeListener(new SearchIndexChangeListener() {
			@Override
			public void onSearchTitleChanged(int index) {
				_currentSearchIndex = index;
				changeSearchPage();
			}
		});
	}
	private void changeSearchPage(){
		removeFragment(true);
		_searchBar.setSearchIndex(_currentSearchIndex);
		_fragment.hideAllControl();
		_searchBar.releaseAllFocus();
		_searchBar.clearKeyword();
		
	}

	private void changeFliterFragment(int index){
		removeFragment(false);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(index == SearchActionbar.SEARCHINDEX_REWARD){
			ft.show(_categoryFragment);
		}else if(index == SearchActionbar.SEARCHINDEX_APP){
			ft.show(_countryFragment);
		}else{
			ft.show(_fragment);
		}
		ft.commit();
	}
	private void removeFragment(boolean isShowMain){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(isShowMain)
			ft.show(_fragment);
		else
			ft.hide(_fragment);
		if(_categoryFragment != null)
			ft.hide(_categoryFragment);
		if(_countryFragment != null)
			ft.hide(_countryFragment);
		ft.commit();
	}

	@Override
	public void onCategoryItemSelected() {
		removeFragment(true);
		_searchBar.releaseCategoryToggle();
		if(_currentSearchIndex == MainActionBar.MAINACTIONBAR_REWARDS)
			_fragment.showRewardSearchData(_keyword);
		else if(_currentSearchIndex == MainActionBar.MAINACTIONBAR_APPS)
			_fragment.showAppSearchData(_keyword);
	}
	@Override
	public void onCountryItemSelected() {
		removeFragment(true);
		_searchBar.releaseCountryToggle();
		if(_currentSearchIndex == MainActionBar.MAINACTIONBAR_REWARDS)
			_fragment.showRewardSearchData(_keyword);
		else if(_currentSearchIndex == MainActionBar.MAINACTIONBAR_APPS)
			_fragment.showAppSearchData(_keyword);
	}
	@Override
	public void onCountryItemFocus(int index,boolean isFocus) {
		Vlog.getInstance().error(false, TAG, "country item focus changed.");
		if(isFocus){
			changeFliterFragment(1);
			_countryFragment.showFilterList(_currentSearchIndex);
		}else{
			removeFragment(true);
		}
	}
	@Override
	public void onCategoryItemFocus(int index,boolean isFocus) {
		Vlog.getInstance().error(false, TAG, "Category item focus changed.");
		if(isFocus){
			changeFliterFragment(0);
			_categoryFragment.showFilterList(_currentSearchIndex);
		}else{
			removeFragment(true);
		}
	}

	@Override
	public void onKeywordChanged(int index,String keyWord) {
		_keyword = keyWord;
		if(getCurrentSearchIndex() == SearchActionbar.SEARCHINDEX_REWARD){
			_fragment.showRewardSearchData(keyWord);
		}else if(getCurrentSearchIndex() == SearchActionbar.SEARCHINDEX_APP){
			_fragment.showAppSearchData(keyWord);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			boolean isSearchStatus =  _searchBar.getSearchBarIsSearchStatus();
			if(isSearchStatus){
				removeFragment(true);
				_searchBar.releaseAllFocus();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
