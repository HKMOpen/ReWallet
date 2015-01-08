package com.imusic.rewallet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;

public class BtnReversalHighlight extends RelativeLayout{

	private ImageView _imgIcon;
	private TextView _tvCount;
	private TextView _tvTitle;
	private Button _btn;
	
	
	public BtnReversalHighlight(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.btn_reversal_highlight, this);
		_tvTitle = (TextView)view.findViewById(R.id.btnReversalHighlight_tvButtonName);
		_tvCount = (TextView)view.findViewById(R.id.btnReversalHighlight_tvNumber);
		_imgIcon = (ImageView)view.findViewById(R.id.btnReversalHighlight_imgIcon);
		_btn = (Button)view.findViewById(R.id.btnReversalHighlight_btn);
	}
	
	public void setContent(String text, String count, int icon){
		_tvTitle.setText(text);
		_tvCount.setText(count);
		_imgIcon.setImageResource(icon);
	}
	public void resetCount(int count){
		_tvCount.setText(String.valueOf(count));
	}
	public void setCustomOnClickListener(View.OnClickListener l){
		_btn.setOnClickListener(l);
	}
	

}
