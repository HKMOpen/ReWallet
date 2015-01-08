package com.imusic.rewallet.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

public abstract class DataBaseFragment extends Fragment {

	protected final static int MSG_GETMOREDATACOMPLETED= 1000;
	protected final static int MSG_REFEASHDATACOMPLETED= 1001;
	protected final static int MSG_GETMOREDATE = 1010;
	protected final static int MSG_REFRESHDATA = 1011;
	protected final static int MSG_PREPAREDATE = 1013;

	protected Handler _handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFRESHDATA:
				onRefreshData();
				break;
			case MSG_GETMOREDATE:
				onGetMoreData();
				break;
			case MSG_PREPAREDATE:
				onPrepareData();
				break;
			case MSG_GETMOREDATACOMPLETED:
				onGetMoreDataCompleted();
				break;
			case MSG_REFEASHDATACOMPLETED:
				onRefreshDataCompleted();
				break;
			}
		};
	};
	protected abstract void onRefreshData();
	
	protected abstract void onGetMoreData();
	
	protected abstract void onPrepareData();
	
	protected abstract void onRefreshDataCompleted();
	protected abstract void onGetMoreDataCompleted();
	
	protected abstract void parseReceviedData_getMore(JSONObject response);
	protected abstract void parseReceviedData_refresh(JSONObject response);
}
