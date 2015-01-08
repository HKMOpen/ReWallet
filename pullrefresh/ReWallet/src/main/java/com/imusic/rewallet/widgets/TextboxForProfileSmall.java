package com.imusic.rewallet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.imusic.rewallet.R;

public class TextboxForProfileSmall extends RelativeLayout{

	private EditText _edContent;
	private Button _btn;
	public TextboxForProfileSmall(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view =  LayoutInflater.from(context).inflate(R.layout.textbox_for_profile_small, this);
		_edContent = (EditText)view.findViewById(R.id.profileView_textbox);
		_btn = (Button)view.findViewById(R.id.profileView_btn);
	}
	public void setContentEnable(String c, boolean isEnable){
		if(_edContent != null){
			_edContent.setText(c);
			_edContent.setEnabled(isEnable);
		}
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
}
