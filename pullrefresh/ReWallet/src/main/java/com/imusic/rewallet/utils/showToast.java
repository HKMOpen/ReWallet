package com.imusic.rewallet.utils;

import android.content.Context;
import android.widget.Toast;

public class showToast {

	private static class Nested{
		static final showToast instance = new showToast();
	}
	private static Context _context;
	
	public static showToast getInstance(Context context){
		_context = context;
		return Nested.instance;
	}
	
	public void showMsg(String msg){
		Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
	}
	
	
}
