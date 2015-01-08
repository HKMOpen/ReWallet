package com.imusic.rewallet.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardUtil {

	public static void showKeyboard(Context context, boolean isShow){
		InputMethodManager inputMethodManager= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
        
		if(isShow)
        	inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); 
        else{
        	if(inputMethodManager.isActive())
        		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
	}
	public static void hideKeyboardFocus(Context context, View v){
		if(v != null){
			InputMethodManager inputMethodManager= (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
			inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
