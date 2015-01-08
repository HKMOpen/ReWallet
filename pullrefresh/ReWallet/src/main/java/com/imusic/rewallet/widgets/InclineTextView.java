package com.imusic.rewallet.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class InclineTextView extends TextView{

	public InclineTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public InclineTextView(Context context) {
		super(context);
	}
	public InclineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override  
    protected void onDraw(Canvas canvas) {  
        canvas.rotate(-20, getMeasuredWidth()/2, getMeasuredHeight()/2);  
        
        super.onDraw(canvas);  
    }  
}
