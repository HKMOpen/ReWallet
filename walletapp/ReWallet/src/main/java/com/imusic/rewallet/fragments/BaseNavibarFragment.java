package com.imusic.rewallet.fragments;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.imusic.rewallet.R;

public class BaseNavibarFragment extends FragmentActivity{

	protected ImageButton _imgbtnNaviBack;
	protected TextView _txtNavTitle;
	protected Button _btnRight;
	
	protected void setupActionBar(String titleName, boolean isBack, 
			boolean isRightBtn, String rightBtnName, View.OnClickListener l){
		ActionBar bar = getActionBar();
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled( false );
		bar.setCustomView(R.layout.view_actionbar_base);
		_txtNavTitle = (TextView) findViewById(R.id.navigator_tvTitle);
		if(_txtNavTitle != null)
			_txtNavTitle.setText(titleName);
		_imgbtnNaviBack = (ImageButton) findViewById(R.id.navigator_imgbtnBack);
		if(_imgbtnNaviBack != null)
			_imgbtnNaviBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		_btnRight = (Button)findViewById(R.id.navigator_btnRight);
		if(isRightBtn){
			_btnRight.setVisibility(View.VISIBLE);
			_btnRight.setText(rightBtnName);
			_btnRight.setOnClickListener(l);
		}else
			_btnRight.setVisibility(View.GONE);
	}
}
