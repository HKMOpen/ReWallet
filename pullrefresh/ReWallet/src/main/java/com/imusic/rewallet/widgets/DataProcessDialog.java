package com.imusic.rewallet.widgets;

import android.app.Activity;
import android.app.ProgressDialog;

import com.imusic.rewallet.R;
import com.imusic.rewallet.utils.Vlog;

public class DataProcessDialog {

	private static class Nested{
		 static final DataProcessDialog instance = new DataProcessDialog();
	}
	private static final String TAG = "DataProcessDialog";
	private static Activity _activity;
	
	private ProgressDialog _dialog = null;
	public static DataProcessDialog getInstance(Activity act){
		_activity = act;
		return Nested.instance;
	}
	public void show(){
		if(_dialog != null)
			dismiss();
		_dialog = new ProgressDialog(_activity);
		_dialog.setTitle(_activity.getString(R.string.DataProcessDialog_title));
		_dialog.setMessage(_activity.getString(R.string.DataProcessDialog_message));
		_dialog.setCanceledOnTouchOutside(false);
		_dialog.show();
		Vlog.getInstance().error(false, TAG, "dialog.show");
	}
	public void show(String title, String msg){
		if(_dialog != null)
			dismiss();
		_dialog = new ProgressDialog(_activity);
		_dialog.setTitle(title);
		_dialog.setMessage(msg);
		_dialog.setCanceledOnTouchOutside(false);
		_dialog.show();
		Vlog.getInstance().error(false, TAG, "dialog.show");
	}
	public void dismiss(){
		if(_dialog != null){
			_dialog.dismiss();
			_dialog = null;
			Vlog.getInstance().error(false, TAG, "dialog.dismiss");
		}
	}
	
	
}
