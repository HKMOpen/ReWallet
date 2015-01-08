package com.imusic.rewallet.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.SettingActivity;
import com.imusic.rewallet.utils.KeyBoardUtil;


public class SearchBar extends RelativeLayout implements OnEditorActionListener {

	private ImageButton _imgbtnIcon;
	private EditText _etKeyword;
	private ToggleButton _tgCategory;
	private ToggleButton _tgCountry;
	private Context _context;
	private RelativeLayout _layout;
	private View _view;
	private Button _btnCover;
	private ImageButton _imgbtnSetting;
	private boolean _isSearchMode = false;
	private SearchBarItemClickListener _listener;
//	private View.OnClickListener _searchBarOnClickListener;
	
	private int _searchIndex = 0;
	public void setSearchIndex(int index){
		_searchIndex = index;
		releaseAllFocus();
	}
	public boolean getSearchBarIsSearchStatus(){
		if( _tgCategory.isChecked() || _tgCountry.isChecked())
			return true;
		else
			return false;
	}
	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		_view = LayoutInflater.from(context).inflate(R.layout.view_searchbar, this);
		setupView();
	}
	public void setCustomOnClickListener(View.OnClickListener l){
		_btnCover.setOnClickListener(l);
	}
	private void setupView(){
		_layout = (RelativeLayout)_view.findViewById(R.id.searchBar_layout);
		_imgbtnIcon = (ImageButton)_view.findViewById(R.id.searchBar_imgbtnIcon);
		_btnCover = (Button)_view.findViewById(R.id.searchBar_btnCover);
		_btnCover.setVisibility(View.GONE);
		_imgbtnSetting = (ImageButton)_view.findViewById(R.id.searchBar_imgbtnSetting);
		_imgbtnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(_context, SettingActivity.class);
				_context.startActivity(intent);
			}
		});
//		if (_searchBarOnClickListener != null) {
//			_btnCover.setOnClickListener(_searchBarOnClickListener);
//		} else {
//			_btnCover.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (!_isSearchMode) {
//						Intent inSearch = new Intent();
//						inSearch.setClass(_context, SearchActivity.class);
//						inSearch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						inSearch.putExtra("index", _searchIndex);
//						_context.startActivity(inSearch);
//					}
//				}
//			});
//		}
		_etKeyword = (EditText)_view.findViewById(R.id.searchBar_etKeyword);
		_etKeyword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		_etKeyword.setOnEditorActionListener(this);
		
		_tgCategory = (ToggleButton)_view.findViewById(R.id.searchBar_tgbtnCategory);
		_tgCategory.setOnClickListener(onFilterItemClick);
		_tgCountry = (ToggleButton)_view.findViewById(R.id.searchBar_tgbtnCountry);
		_tgCountry.setOnClickListener(onFilterItemClick);
		
		_etKeyword.setVisibility(View.GONE);
		_imgbtnIcon.setVisibility(View.GONE);
		
		releaseAllFocus();
	}

	public String getSearchKeyWord(){
		return _etKeyword.getText().toString();
	}
	public void setSearchMode(boolean isSearchMode){
//		_isSearchMode = isSearchMode;
//		_etKeyword.setEnabled(isSearchMode);
//		_imgbtnIcon.setEnabled(!isSearchMode);
//		if(isSearchMode){
//			_btnCover.setVisibility(View.GONE);
//		}else{
//			_btnCover.setVisibility(View.VISIBLE);
//		}
	}
	public boolean getIsSearchMode(){
		return _isSearchMode;
	}
	public void setFilterItemClickListener(SearchBarItemClickListener l){
		_listener = l;
	}

	public void releaseCategoryToggle(){
		_tgCategory.setChecked(false);
		handleBackgroud();
		setCategoryToggleBackgroud();
	}
	public void releaseCountryToggle(){
		_tgCountry.setChecked(false);
		handleBackgroud();
		setCountryToggleBackgroud();
	}
	public void setCategoryToggleBackgroud(){
//		if(_tgCategory.isChecked()){
//			String thumb = _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//					GlobalDataInfo.getInstance().getRewardsSearchCategoryPressThumb()
//					: GlobalDataInfo.getInstance().getAppsSearchCategoryPressThumb();
//			if(thumb != null){
//				try {
//					Drawable d = BitmapFactoryUtil.getDrawableFromImageLoaderCache(
//							_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
//							GlobalDataInfo.getInstance().getRewardsSearchCategoryPressThumb()
//							: GlobalDataInfo.getInstance().getAppsSearchCategoryPressThumb());
//					if(d != null){
//						_tgCategory.setBackground(d);
//					}else{
//						_tgCategory.setBackgroundResource(R.drawable.searchbar_category_highlight);
//					}
//				} catch (IOException e) {
//					showToast.getInstance(_context).showMsg(e.getMessage());
//					e.printStackTrace();
//				}
//			}else{
//				_tgCategory.setBackgroundResource(R.drawable.searchbar_category_highlight);
//			}
//		}else{  // unChecked...
//			String unThumb =  _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//					GlobalDataInfo.getInstance().getRewardsSearchCategoryUnpressThumb()
//					: GlobalDataInfo.getInstance().getAppsSearchCategoryUnpressThumb();
//			if(unThumb != null){
//				try {
//					Drawable d = BitmapFactoryUtil.getDrawableFromImageLoaderCache(
//							_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
//									GlobalDataInfo.getInstance().getRewardsSearchCategoryUnpressThumb()
//									: GlobalDataInfo.getInstance().getAppsSearchCategoryUnpressThumb());
//					if(d != null){
//						_tgCategory.setBackground(d);
//					}else{
//						_tgCategory.setBackgroundResource(R.drawable.searchbar_category);
//					}
//				} catch (IOException e) {
//					showToast.getInstance(_context).showMsg(e.getMessage());
//					e.printStackTrace();
//				}
//			}else{
//				_tgCategory.setBackgroundResource(R.drawable.searchbar_category);
//			}
//		}
	}
	public void setCountryToggleBackgroud(){
//		if(_tgCountry.isChecked()){
//			String thumb = _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//					GlobalDataInfo.getInstance().getRewardsSearchCountryPressThumb()
//					:GlobalDataInfo.getInstance().getAppsSearchCountryPressThumb();
//			if(thumb != null){
//				try {
//					Drawable d = BitmapFactoryUtil.getDrawableFromImageLoaderCache(
//							_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//							GlobalDataInfo.getInstance().getRewardsSearchCountryPressThumb()
//							:GlobalDataInfo.getInstance().getAppsSearchCountryPressThumb());
//					if(d != null){
//						_tgCountry.setBackground(d);
//					}else{
//						_tgCountry.setBackgroundResource(R.drawable.searchbar_country_highlight);
//					}
//				} catch (IOException e) {
//					showToast.getInstance(_context).showMsg(e.getMessage());
//					e.printStackTrace();
//				}
//			}else{
//				_tgCountry.setBackgroundResource(R.drawable.searchbar_country_highlight);
//			}
//		}else{  // unChecked...
//			String unThumb = _searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//					GlobalDataInfo.getInstance().getRewardsSearchCountryUnpressThumb()
//					:GlobalDataInfo.getInstance().getAppsSearchCountryUnpressThumb();
//			if(unThumb != null){
//				try {
//					Drawable d = BitmapFactoryUtil.getDrawableFromImageLoaderCache(
//							_searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ? 
//							GlobalDataInfo.getInstance().getRewardsSearchCountryUnpressThumb()
//							:GlobalDataInfo.getInstance().getAppsSearchCountryUnpressThumb());
//					if(d != null){
//						_tgCountry.setBackground(d);
//					}else{
//						_tgCountry.setBackgroundResource(R.drawable.searchbar_country);
//					}
//				} catch (IOException e) {
//					showToast.getInstance(_context).showMsg(e.getMessage());
//					e.printStackTrace();
//				}
//			}else{
//				_tgCountry.setBackgroundResource(R.drawable.searchbar_country);
//			}
//		}
	}
	
	public void releaseAllFocus(){
		_tgCategory.setChecked(false);
		_tgCountry.setChecked(false);
		if(_etKeyword.isFocused()){
			_etKeyword.setFocusable(false);
		}
		handleBackgroud();
		setToggleBackgroud();
	}
	public void setToggleBackgroud(){
		setCategoryToggleBackgroud();
		setCountryToggleBackgroud();
	}
	public void clearKeyword(){
		_etKeyword.setText("");
	}

	private OnClickListener onFilterItemClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.searchBar_tgbtnCategory){
				handleCategoryClick();
			}else if(v.getId() == R.id.searchBar_tgbtnCountry){
				handleCountryClick();
			}
		}
	};
	private void handleCategoryClick(){
		if(_tgCategory.isChecked()){
			_tgCountry.setChecked(false);
			if(_listener != null)
				_listener.onCategoryItemFocus(_searchIndex,true);
		}else{
			if(_listener != null)
				_listener.onCategoryItemFocus(_searchIndex, false);
		}
		handleBackgroud();
		setCategoryToggleBackgroud();
	}

	private void handleCountryClick(){
		if(_tgCountry.isChecked()){
			_tgCategory.setChecked(false);
			if(_listener != null)
				_listener.onCountryItemFocus(_searchIndex,true);
		}else{
			if(_listener != null)
				_listener.onCountryItemFocus(_searchIndex,false);
		}
		handleBackgroud();
	}
	private void handleBackgroud(){
//		if(!_tgCategory.isChecked() && !_tgCountry.isChecked()){
//			_etKeyword.setVisibility(View.VISIBLE);
//			_btnCover.setClickable(true);
//			_layout.setBackgroundColor(getResources().getColor(R.color.searchBar_bg_default));
//		}else{
//			_etKeyword.setVisibility(View.GONE);
//			_btnCover.setClickable(false);
//			_layout.setBackgroundColor(getResources().getColor(R.color.searchBar_bg_active));
//		} 
	}

	public interface SearchBarItemClickListener{
		public void onCategoryItemFocus(int index, boolean isFocus);
		public void onCountryItemFocus(int index, boolean isFocus);
		public void onKeywordChanged(int index, String keyWord);
	}
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {  
			KeyBoardUtil.hideKeyboardFocus(_context, v);
			_etKeyword.clearFocus();
			if(_listener != null)
				_listener.onKeywordChanged(_searchIndex,_etKeyword.getText().toString());
		}
		return false;
	}
	
	
}
