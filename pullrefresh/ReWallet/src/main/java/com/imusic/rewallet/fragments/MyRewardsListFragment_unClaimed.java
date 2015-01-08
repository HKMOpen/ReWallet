package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imusic.rewallet.R;

public class MyRewardsListFragment_unClaimed extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		System.out.println("AppDownloadsFragment onCreateView"); 
		View view = inflater.inflate(R.layout.fragment_redemptionhistory, container , false);
		return view;
	}

	
	
	
	
}
