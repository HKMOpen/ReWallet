package com.imusic.rewallet.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.UI.MainActivity;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.OverlayBtnUtil;
import com.imusic.rewallet.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MainActionBar extends RelativeLayout implements OnClickListener{

	public static final int MAINACTIONBAR_REWARDS = 0;
	public static final int MAINACTIONBAR_APPS = 1;
	public static final int MAINACTIONBAR_MISSIONS = 2;
	
	private Activity _context;
	
	private View _view;
//	private TextView _tvLeft;
	private TextView _tvCenter;
	private TextView _tvRight;
	private ImageView _imgUserProfile;
	
	private ArrayList<String> _aryTitle;
	private int _currentIndex;
	private MainActionBarListener _listener;
	public MainActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = (Activity)context;
		_view = LayoutInflater.from(context).inflate(R.layout.view_actionbar_main_finalr, this);
		setupView();
		_aryTitle = new ArrayList<String>();
		_aryTitle.add(getResources().getString(R.string.mainActionbar_rewardChannel));
		_aryTitle.add(getResources().getString(R.string.mainActionbar_appdownloads));
//		_aryTitle.add(getResources().getString(R.string.mainActionbar_mission));
	}
	
	private void setupView(){
//		_tvLeft = (TextView)_view.findViewById(R.id.MainActionbar_tvLeft);
//		_tvLeft.setOnClickListener(this);
//		_tvLeft.setVisibility(View.GONE);
		_tvCenter = (TextView)_view.findViewById(R.id.MainActionbar_tvCenter);
		_tvRight = (TextView)_view.findViewById(R.id.MainActionbar_tvRight);
		_tvRight.setOnClickListener(this);
		_imgUserProfile = (ImageView)_view.findViewById(R.id.MainActionbar_imgUserProfile);
		_imgUserProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OverlayBtnUtil.myProfileClick(_context, MainActivity.ActivityRequestCode);
			}
		});
		if(LoginUserInfo.getInstance().getLoginUserInfo() != null){
			ImageLoader.getInstance().displayImage(LoginUserInfo.getInstance().getLoginUserInfo().profile_picture
					, _imgUserProfile, Util.getImageLoaderOptionForUserProfile_actionbar(_context));
		}else {
			_imgUserProfile.setImageResource(R.drawable.userprofile_nopic_default_big);
		}
		
		
//		setCurrentIndex(0);
	}
	public void resetUserProfileImage(){
		if(LoginUserInfo.getInstance().getLoginUserInfo() != null){
			ImageLoader.getInstance().displayImage(LoginUserInfo.getInstance().getLoginUserInfo().profile_picture //LoginUserInfo.getInstance().getLoginUserInfo().profile_picture
					, _imgUserProfile, Util.getImageLoaderOptionForUserProfile_actionbar(_context));
		}else {
			_imgUserProfile.setImageResource(R.drawable.userprofile_nopic_default_big);
		}
	}
	public void setCurrentIndex(int index){
		String title = _aryTitle.get(index);
		if(title != null){
			_tvCenter.setText(title);
//			if (index - 1 >= 0) {
//				String leftTitle = _aryTitle.get(index - 1);
//				_tvLeft.setText(leftTitle);
//				_tvLeft.setVisibility(View.VISIBLE);
//
//			} else {
//				_tvLeft.setVisibility(View.GONE);
//			}
//			if (index + 1 < _aryTitle.size()) {
//				String rightTitle = _aryTitle.get(index + 1);
//
//				_tvRight.setText(rightTitle);
//				_tvRight.setVisibility(View.VISIBLE);
//
//			} else {
//				_tvRight.setVisibility(View.GONE);
//			}
			if(index == 0){
				_tvRight.setText(_aryTitle.get(1));
			}else{
				_tvRight.setText(_aryTitle.get(0));
			}
			_currentIndex = index;
			if(_listener != null)
				_listener.onTitleChanged(_currentIndex);
		}
	}
	public int getCurrentIndex(){
		return _currentIndex;
	}
	public void setMainActionBarListener(MainActionBarListener l){
		_listener = l;
	}
	
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.MainActionbar_tvLeft:
//			setCurrentIndex(_currentIndex -1);
//			break;
		case R.id.MainActionbar_tvRight:
			setCurrentIndex(_currentIndex ==0 ?1 :0);
			break;
		}
	}
	
	public interface MainActionBarListener{
		public void onTitleChanged(int index);
	}
	
}
