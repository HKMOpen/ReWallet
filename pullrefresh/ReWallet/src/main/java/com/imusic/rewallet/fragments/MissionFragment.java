package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.adapters.MissionListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

public class MissionFragment extends DataBaseFragment{

	private String TAG = "MissionFragment";
	
	private View _view;
	private ListView _lstView;
	private TextView _tvNotice;
	private MissionListAdapter _adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		System.out.println("MissionFragment onCreateView"); 
		_view = inflater.inflate(R.layout.fragment_mission, container , false);
		setupView();
		_handler.obtainMessage(MSG_PREPAREDATE).sendToTarget();
		return _view;
	}
	
	private void setupView(){
		_tvNotice = (TextView)_view.findViewById(R.id.mission_tvNotice);
		_tvNotice.setVisibility(View.GONE);
		_lstView = (ListView)_view.findViewById(R.id.mission_listview);
		_adapter = new MissionListAdapter(getActivity(), null);
		_lstView.setAdapter(_adapter);
	}
	public int getMissionCount(){
		return _adapter.getCount();
	}
	public void refreshMissionList(){
		_handler.obtainMessage(MSG_PREPAREDATE).sendToTarget();
	}
	
	//----------------------------------------------------------
	@Override
	protected void onRefreshData() {
		HttpRequest request = HttpRequestHelper
				.getGetRequest(
						Constants.Url.getMissionUrl(LoginUserInfo.getInstance().getToken()
								, Constants.GlobalKey.getAppKey()));
    					
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override   
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "MissionFragment onRefreshData resposnse json succeed : "+response.toString());
				parseReceviedData_refresh(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG, "MissionFragment onRefreshData response json failed." + e.toString());
				showToast.getInstance(getActivity()).showMsg(e.getMessage());
				parseReceviedData_refresh(null);
			}
		},"");
	}

	@Override
	protected void onGetMoreData() {
		
	}

	@Override
	protected void onPrepareData() {
		if(_adapter.getCount() == 0){
			Vlog.getInstance().info(false, TAG, "no data, refresh start..");
			if(LoginUserInfo.getInstance().getToken() != null){
				_handler.obtainMessage(MSG_REFRESHDATA).sendToTarget();
				_tvNotice.setVisibility(View.GONE);
			}else{
				_tvNotice.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onRefreshDataCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onGetMoreDataCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parseReceviedData_getMore(JSONObject response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parseReceviedData_refresh(JSONObject response) {
		if(response != null){
			_adapter.refreshListItemData(response);
		}
		_handler.obtainMessage(MSG_REFEASHDATACOMPLETED).sendToTarget();
	}

}
