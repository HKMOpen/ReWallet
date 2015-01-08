package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.adapters.SearchCountryListAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.SearchCountryItemData;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchCountryFliterFragment extends Fragment{

	private final static String TAG = "SearchCountryFliterFragment";
	
	public static final int SEARCH_REWARD = 0;
	public static final int SEARCH_APP = 1;
	
	private View _view;
	private ListView _lvCountry;
	
	private int _searchIndex;
	private ArrayList<SearchCountryItemData> _lstCountry;
	private SearchCountryListAdapter _countryAdapter;
	private onCountrySelectListener _listener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){ 
		_view = inflater.inflate(R.layout.fragment_search_country, container , false);
		setupView();
		return _view;
	}
	private void setupView(){
		_lvCountry = (ListView)_view.findViewById(R.id.searchFilter_lvCountry);
		_countryAdapter = new SearchCountryListAdapter(getActivity(),_lstCountry ,SEARCH_REWARD);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		_countryAdapter.setListViewLayout(dm.widthPixels, dm.widthPixels * 9/ 16);
		_lvCountry.setAdapter(_countryAdapter);
		
		_lvCountry.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				_countryAdapter.setSelectedItem(arg2);
				if(_listener != null){
					_listener.onCountryItemSelected();
				}
			}
		});
		
	}
	public void showFilterList(int searchIndex){
		if(_searchIndex != searchIndex){
			_lstCountry = null;
			_countryAdapter.resetData(_searchIndex, _lstCountry);
			_lvCountry.setVisibility(View.GONE);
		}
		_searchIndex= searchIndex;
		getCountryData();
	}
	public void setItemClickListener(onCountrySelectListener l){
		_listener = l;
	}
	private void getCountryData(){
		HttpRequest request = null ;
		if(_searchIndex == SEARCH_REWARD)
			 request = HttpRequestHelper.getGetRequest(Constants.Url.getRewardCountryList(GlobalDataInfo.getInstance().getLanguage()));
		else if (_searchIndex == SEARCH_APP)
			request = HttpRequestHelper.getGetRequest(Constants.Url.getAppCountryList(GlobalDataInfo.getInstance().getLanguage()));
		else 
			return;
    	
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getCountryData resposnse json succeed : "+response.toString());
				parseCountryData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "getCountryData response json failed." + e.toString());
				
			}
		},"");
	}
	private void parseCountryData(JSONObject response){
		try {
			if(response.getInt("result") == 1){
				SearchCountryItemData item ;
				JSONArray aryData = response.getJSONArray("data");
				if(aryData != null){
					_lstCountry = new ArrayList<SearchCountryItemData>();
					JSONObject data;
					String name;
					String desc;
					String unpress;
					int id ;
					for(int i=0; i < aryData.length(); i++){
						data = aryData.getJSONObject(i);
						if(data != null){
							id = data.getInt("id");
							name = data.getString("name");
							desc = data.getString("description");
							unpress = data.getString("unpress");
							item = new SearchCountryItemData(id,name,desc);
							item.unpress = unpress;
							_lstCountry.add(item);
						}
					}
				}
			}else{
				showToast.getInstance(getActivity()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getActivity().getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		loadCountryList();
	}
	private void loadCountryList(){
		_countryAdapter.resetData(_searchIndex, _lstCountry);
		_lvCountry.setVisibility(View.VISIBLE);
		_lvCountry.setSelection(0);
		
	}
	public void cycleCountryList(){
		_lstCountry = null;
		_countryAdapter.resetData(_searchIndex, _lstCountry);
		_lvCountry.setVisibility(View.GONE);
	}
	public void hideAllDataLayout(){
		_lvCountry.setVisibility(View.GONE);
	}
	public interface onCountrySelectListener{
		public void onCountryItemSelected();
	}
	
}
