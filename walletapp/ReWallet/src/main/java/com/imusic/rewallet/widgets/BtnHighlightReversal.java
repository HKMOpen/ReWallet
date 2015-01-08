package com.imusic.rewallet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;

public class BtnHighlightReversal extends RelativeLayout {

	private ImageView _imgIcon;
	private TextView _tvCount;
	private TextView _tvTitle;
	private Button _btn;
	private RelativeLayout _layout;
	
	private int _defaultIcon;
	private int _pressedIcon;
	
	public BtnHighlightReversal(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.btn_highlight_reversal, this);
		_tvTitle = (TextView)view.findViewById(R.id.btnHighlightReversal_tvButtonName);
		_tvCount = (TextView)view.findViewById(R.id.btnHighlightReversal_tvNumber);
		_imgIcon = (ImageView)view.findViewById(R.id.btnHighlightReversal_imgIcon);
		_btn = (Button)view.findViewById(R.id.btnHighlightReversal_btn);
		_layout = (RelativeLayout)view.findViewById(R.id.btnHighlightReversal_layout);
		
		_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					pressedEffect(false);
					break;
				case MotionEvent.ACTION_DOWN:
					pressedEffect(true);
				}
				return false;
			}
		});
	}
	private void pressedEffect(boolean isPressed){
		if (_btn.isEnabled()) {
			if (isPressed) {
				_tvTitle.setTextColor(getResources().getColor(
						R.color.btn_highlightReversal_pressed_NumberTextColor));
				_tvCount.setTextColor(getResources().getColor(
						R.color.btn_highlightReversal_pressed_NumberTextColor));
				_imgIcon.setImageResource(_pressedIcon);
				_layout.setBackgroundColor(getResources().getColor(
						R.color.btn_highlightReversal_pressed_bg));
			} else {
				_tvTitle.setTextColor(getResources().getColor(
						R.color.btn_highlightReversal_default_NumberTextColor));
				_tvCount.setTextColor(getResources().getColor(
						R.color.btn_highlightReversal_default_NumberTextColor));
				_imgIcon.setImageResource(_defaultIcon);
				_layout.setBackgroundColor(getResources().getColor(
						R.color.btn_highlightReversal_default_bg));
			}
		}
	}
	
	public void setContent(String text, String count, int icon, int pressed){
		_tvTitle.setText(text);
		_tvCount.setText(count);
		_imgIcon.setImageResource(icon);
		_defaultIcon = icon;
		_pressedIcon = pressed;
	}
	public void setOnButtonClickListener(View.OnClickListener l){
		_btn.setOnClickListener(l);
	}
	public void resetCount(int count){
		_tvCount.setText(String.valueOf(count));
	}
	public void setButtonEnable(boolean isEnable){
		if(!isEnable){
			_btn.setBackgroundColor(getResources().getColor(R.color.global_btnTransparent_disable));
			_btn.setEnabled(false);
		}else{
			_btn.setBackgroundColor(getResources().getColor(R.color.global_btnTransparent_default));
			_btn.setEnabled(true);
		}
	}
	

}
