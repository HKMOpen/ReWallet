package com.imusic.rewallet.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;

public class TextboxForProfileLarge extends RelativeLayout{

	private EditText _edContent;
	private ImageView _arrow;
	private Button _btn;
	
	public TextboxForProfileLarge(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view =  LayoutInflater.from(context).inflate(R.layout.textbox_for_profile_large, this);
		_edContent = (EditText)view.findViewById(R.id.profileView_textbox);
		_arrow = (ImageView)view.findViewById(R.id.profileView_arrow);
		_arrow.setVisibility(View.GONE);
		_btn = (Button)view.findViewById(R.id.profileView_btn);
		_btn.setVisibility(View.GONE);
	}
	public void setContentEnable(String c, boolean isEnable){
		if(_edContent != null){
			_edContent.setText(c);
			_edContent.setEnabled(isEnable);
			_edContent.setFocusable(isEnable);
			if(isEnable){
				_btn.setVisibility(View.GONE);
			}else
				_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setArrowShow(boolean isShow){
		_arrow.setVisibility(isShow? View.VISIBLE: View.GONE);
	}
	public void resetArrow(Drawable d){
		_arrow.setImageDrawable(d);
		_arrow.setVisibility(View.VISIBLE);
	}
	public void setHint(String s){
		if(_edContent != null){
			_edContent.setHint(s);
		}
	}
	public String getEditContent(){
		if(_edContent != null){
			return _edContent.getText().toString();
		}
		return null;
	}
	public void setEditContent(String s){
		if(_edContent != null){
			_edContent.setText(s);
		}
	}
	public void setOnCustomClickListener(OnClickListener l){
		if(_btn != null){
			if(_btn.getVisibility()== View.GONE){
				_btn.setVisibility(View.VISIBLE);
			}
			_btn.setOnClickListener(l);
		}
	}
	public void setEditInputType(int inputType){
		if(_edContent != null){
			_edContent.setInputType(inputType);
		}
	}
}
