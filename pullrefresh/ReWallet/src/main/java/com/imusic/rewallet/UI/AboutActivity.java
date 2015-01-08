package com.imusic.rewallet.UI;

import android.os.Bundle;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;

import org.json.JSONObject;

public class AboutActivity extends BaseNavibarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setupView();
	}
	private void setupView(){
		setupActionBar(getString(R.string.aboutActivity_title) + " " + getString(R.string.app_name),
				true, false, null, null);
		
	}

	@Override
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}

	
}
