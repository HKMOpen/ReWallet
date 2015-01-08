package com.imusic.rewallet.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontManager {

	public static final int FONT_STYLE_Regular = 0;
	public static final int FONT_STYLE_BOLD = 1;
	
	private static Typeface tf = null ;//= Typeface.createFromAsset(act.getAssets(),"fonts/xxx.ttf");  
	private static Typeface tf_bold = null;

	private static void initFontTypeface(Activity act){
		if (tf == null) {
			tf = Typeface.createFromAsset(act.getAssets(),
					"font/Raleway-Thin.ttf");
		}
		if (tf_bold == null) {
			tf_bold = Typeface.createFromAsset(act.getAssets(),
					"font/Raleway-ExtraLight.ttf");
		}
	}
	public static void changeFont(View v, Activity act){
		initFontTypeface(act);
		if (v instanceof TextView) {
			if (((TextView) v).getTypeface() != null
					&& ((TextView) v).getTypeface().isBold()) {
				((TextView) v).setTypeface(tf_bold);
			} else {
				((TextView) v).setTypeface(tf);
			}
		} else if (v instanceof Button) {
			if (((Button) v).getTypeface()!= null
					&& ((Button) v).getTypeface().isBold()) {
				((Button) v).setTypeface(tf_bold);
			} else {
				((Button) v).setTypeface(tf);
			}
		} else if (v instanceof EditText) {
			if (((EditText) v).getTypeface()!=null
					&& ((EditText) v).getTypeface().isBold()) {
				((EditText) v).setTypeface(tf_bold);
			} else {
				((EditText) v).setTypeface(tf);
			}
		}
	}
	public static void changeFonts(ViewGroup root, Activity act) {
		initFontTypeface(act);

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				if (((TextView) v).getTypeface() != null
						&& ((TextView) v).getTypeface().isBold()) {
					((TextView) v).setTypeface(tf_bold);
				} else {
					((TextView) v).setTypeface(tf);
				}
			} else if (v instanceof Button) {
				if (((Button) v).getTypeface()!= null
						&& ((Button) v).getTypeface().isBold()) {
					((Button) v).setTypeface(tf_bold);
				} else {
					((Button) v).setTypeface(tf);
				}
			} else if (v instanceof EditText) {
				if (((EditText) v).getTypeface()!=null
						&& ((EditText) v).getTypeface().isBold()) {
					((EditText) v).setTypeface(tf_bold);
				} else {
					((EditText) v).setTypeface(tf);
				}
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}
		}

	} 
	
}
