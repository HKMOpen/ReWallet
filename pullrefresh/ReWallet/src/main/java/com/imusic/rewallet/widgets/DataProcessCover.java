package com.imusic.rewallet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;

public class DataProcessCover extends RelativeLayout{

	public DataProcessCover(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.view_dataprocesscover, this);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.dataProcessCover_layout);
	    layout.setClickable(false);
	}

}
