package com.imusic.rewallet.widgets;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;

public class SearchActionbar extends RelativeLayout {

	public static final int SEARCHINDEX_REWARD = 0;
	public static final int SEARCHINDEX_APP = 1;
	
	private Context _context;
	private View _view;
	private ImageButton _imgbtnBack;
	private TextView _mainTitle;
	private TextView _rightTitle;
	
	private int _currentSearchIndex ;
	private SearchIndexChangeListener _listener;

	public SearchActionbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		_view = LayoutInflater.from(context).inflate(R.layout.view_actionbar_search, this);
		setupView();
	}
	public int getCurrentSearchIndex(){
		return _currentSearchIndex;
	}
	public void setSearchIndexChangeListener(SearchIndexChangeListener l){
		_listener = l;
	}
	private void setupView(){
		_currentSearchIndex = SEARCHINDEX_REWARD;
		_imgbtnBack = (ImageButton)_view.findViewById(R.id.searchActionbar_imgbtnBack);
		_mainTitle = (TextView)_view.findViewById(R.id.searchActionbar_tvMainTitle);
		_mainTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
		_rightTitle = (TextView)_view.findViewById(R.id.searchActionbar_tvRightTitle);
		_rightTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_currentSearchIndex = _currentSearchIndex == 
						SEARCHINDEX_REWARD ? SEARCHINDEX_APP : SEARCHINDEX_REWARD;
				if(_listener != null){
					_listener.onSearchTitleChanged(_currentSearchIndex);
				}
				setTitle();
			}
		});
		
	}
	 
	public void setBackbuttonClickListener(OnClickListener l){
		_imgbtnBack.setOnClickListener(l);
	}
	public void setCurrentIndex(int index){
		_currentSearchIndex = index;
		setTitle();
	}

	private void setTitle(){
		if(_currentSearchIndex == SEARCHINDEX_REWARD){
			_mainTitle.setText(_context.getString(R.string.searchActionbar_rewardChannel));
			_rightTitle.setText(_context.getString(R.string.searchActionbar_appDownloads));
		}else if(_currentSearchIndex == SEARCHINDEX_APP){
			_rightTitle.setText(_context.getString(R.string.searchActionbar_rewardChannel));
			_mainTitle.setText(_context.getString(R.string.searchActionbar_appDownloads));
		}
	}
	
	public interface SearchIndexChangeListener{
		public void onSearchTitleChanged(int index);
	}
	
	
}
