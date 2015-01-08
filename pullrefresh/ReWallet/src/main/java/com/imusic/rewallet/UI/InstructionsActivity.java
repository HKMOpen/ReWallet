package com.imusic.rewallet.UI;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.fragments.BaseNavibarFragment;
import com.imusic.rewallet.widgets.ImageSlider;

public class InstructionsActivity extends BaseNavibarFragment implements OnClickListener {

	private Button _btnToGain;
	private Button _btnToUse;
	private Button _btnOK;
//	private InstructionsFragment_toGain _toGainFragment;
//	private InstructionsFragment_toUse _toUseFragment;
	private LinearLayout _layoutToGain;
	private LinearLayout _layoutToUse;
	private ImageSlider _imgSlider;
	
	private int _tabIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		int index = 0;
		if(getIntent().getExtras() != null){
			index = getIntent().getExtras().getInt("index");
		}
		_tabIndex = index;
		setupView(index);
		
	}
	private void setupView(int index){
		setupActionBar(getString(R.string.instructionsActivity_title)
				, true, false, null, null);
		_btnToGain = (Button)findViewById(R.id.instructions_btnToGain);
		_btnToGain.setOnClickListener(this);
		_btnToUse = (Button)findViewById(R.id.instructions_btnToUse);
		_btnToUse.setOnClickListener(this);
		_btnOK = (Button)findViewById(R.id.instructions_btnOK);
		_btnOK.setOnClickListener(this);
		_layoutToGain = (LinearLayout)findViewById(R.id.instructions_layout_toGain);
		_layoutToUse = (LinearLayout)findViewById(R.id.instructions_layout_toUse);
		
		_imgSlider = (ImageSlider)findViewById(R.id.instructions_imageSlider);
		fitParams();
	
		
		changeSelectedTab(index);
	}
	private void fitParams(){
		DisplayMetrics dm = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(dm);
    	
    	LinearLayout sliderLayout = (LinearLayout)findViewById(R.id.instructions_layout_imageSlider);
		sliderLayout.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels *9 /16));
    }
	private void changeSelectedTab(int index){
		setAllTabTransparent();
		hideAllText();
		switch(index){
		case 0:
			_btnToGain.setBackgroundColor(getResources().getColor(R.color.appList_listitem_left_bg));
			_btnToGain.setTextColor(getResources().getColor(R.color.global_Text_Title));
			_layoutToGain.setVisibility(View.VISIBLE);
			break;
		case 1:
			_btnToUse.setBackgroundColor(getResources().getColor(R.color.appList_listitem_left_bg));
			_btnToUse.setTextColor(getResources().getColor(R.color.global_Text_Title));
			_layoutToUse.setVisibility(View.VISIBLE);
			break;
		}
	}
//	private void changeFragment(int index){
//		hideAllFragment();
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		if(index == 0){
//			if(_toGainFragment != null){
//				ft.show(_toGainFragment);
//			}else{
//				_toGainFragment = new InstructionsFragment_toGain();
//				ft.add( R.id.instructions_layoutContent,_toGainFragment,"gain");
//			}
//		}else if(index == 1){
//			if(_toUseFragment != null){
//				ft.show(_toUseFragment);
//			}else{
//				_toUseFragment = new InstructionsFragment_toUse();
//				ft.add( R.id.instructions_layoutContent,_toUseFragment,"use");
//			}
//		}
//		ft.commit();
//	}
//	private void hideAllFragment(){
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		if(_toGainFragment != null)
//			ft.hide(_toGainFragment);
//		if(_toUseFragment != null)
//			ft.hide(_toUseFragment);
//		ft.commit();
//	}
	private void setAllTabTransparent(){
		_btnToGain.setBackgroundColor(getResources().getColor(R.color.appList_listitem_right_bg));
		_btnToGain.setTextColor(getResources().getColor(R.color.global_Text_SubTitle));
		_btnToUse.setBackgroundColor(getResources().getColor(R.color.appList_listitem_right_bg));
		_btnToUse.setTextColor(getResources().getColor(R.color.global_Text_SubTitle));
	}
	private void hideAllText(){
		_layoutToGain.setVisibility(View.GONE);
		_layoutToUse.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.instructions_btnToGain:
			if(_tabIndex  == 0) return;
			_tabIndex = 0;
			changeSelectedTab(_tabIndex);
			break;
		case R.id.instructions_btnToUse:
			if(_tabIndex  == 1) return;
			_tabIndex = 1;
			changeSelectedTab(_tabIndex);
			break;
		case R.id.instructions_btnOK:
			finish();
			break;
		}
		
	}
	
	
	

}
