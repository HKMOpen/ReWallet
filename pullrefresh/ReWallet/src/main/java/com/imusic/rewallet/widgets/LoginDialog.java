package com.imusic.rewallet.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.imusic.rewallet.R;

public class LoginDialog extends Dialog{

	private Context _context;
	
	public LoginDialog(Context context, int theme) {
		super(context, theme);
		this._context = context;
	}
	public LoginDialog(Context context) {
		super(context);
		this._context = context;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_login_dialog);
		
//		WindowManager.LayoutParams lp= getWindow().getAttributes();
//		lp.alpha=0.5f;//��0.0-1.0��
		this.setCanceledOnTouchOutside(false);
	}

}
