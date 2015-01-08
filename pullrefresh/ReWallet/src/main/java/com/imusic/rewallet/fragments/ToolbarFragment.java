package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.imusic.rewallet.R;

public class ToolbarFragment extends Fragment implements OnClickListener{

	private View _view;
	private ImageButton _imgbtnHome;
	private ImageButton _imgbtnProfile;
	private ImageButton _imgbtnCenter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		if(_view == null ){
			_view = inflater.inflate(R.layout.fragment_toolbar, container , false);
		}
		return _view;
	}
	@Override
	public void onClick(View v) {

	}

}
