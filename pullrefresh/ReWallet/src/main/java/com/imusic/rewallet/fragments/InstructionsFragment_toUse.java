package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imusic.rewallet.R;

public class InstructionsFragment_toUse extends Fragment{

	private final static String TAG = "InstructionsFragment_toUse" ;
	private View _view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		_view = inflater.inflate(R.layout.fragment_instructions_touse, container , false);
		return _view;
	}
	private void setupView(){
		
	}
	
}
