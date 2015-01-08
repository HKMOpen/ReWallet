package com.imusic.rewallet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.adapters.SearchCategoryGridAdapter;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.SearchCategoryItemData;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchCategoryFliterFragment extends Fragment{

	private final static String TAG = "SearchFilterFragment";

	public static final int SEARCH_REWARD = 0;
	public static final int SEARCH_APP = 1;
	
	private View _view;
	private GridView _gvCategory;
	private int _searchIndex;
	
	private ArrayList<SearchCategoryItemData> _lstCategory;
	private SearchCategoryGridAdapter _categoryAdapter;
	private onCategorySelectListener _listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ){
		Vlog.getInstance().debug(false, TAG, "SearchCategoryFliterFragment  onCreateView-----------------");
		_view = inflater.inflate(R.layout.fragment_search_category, container , false);
		setupView();
		return _view;
	}
	
	private void setupView(){
		_gvCategory = (GridView)_view.findViewById(R.id.searchFilter_gvCategory);
		_categoryAdapter = new SearchCategoryGridAdapter(getActivity(),_lstCategory,SEARCH_REWARD);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		_categoryAdapter.setGridViewLayout(dm.widthPixels /2, dm.widthPixels/2);
		_gvCategory.setAdapter(_categoryAdapter);
		_gvCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//showToast.getInstance(getActivity()).showMsg("position:"+arg2);
				_categoryAdapter.setSelectedItem(arg2);
				if(_listener != null){
					_listener.onCategoryItemSelected();
				}
			}
		});
	}
	public void setItemClickListener(onCategorySelectListener l){
		_listener = l;
	}
	public void showFilterList(int searchIndex){
		if(_searchIndex != searchIndex){
			_lstCategory = null;
			_categoryAdapter.resetData(_searchIndex, _lstCategory);
			_gvCategory.setVisibility(View.GONE);
		}
		_searchIndex= searchIndex;
		getCategoryData();
	}
	
	
	private void getCategoryData(){
		HttpRequest request = null ;
		if(_searchIndex == SEARCH_REWARD)
			 request = HttpRequestHelper.getGetRequest(Constants.Url.getRewardCategoryList(GlobalDataInfo.getInstance().getLanguage()
					 , GlobalDataInfo.getInstance().getRewardsSearchCountryId()));
		else if (_searchIndex == SEARCH_APP)
			request = HttpRequestHelper.getGetRequest(Constants.Url.getAppCategoryList(GlobalDataInfo.getInstance().getLanguage()
					, GlobalDataInfo.getInstance().getRewardsSearchCountryId()));
		else 
			return;
		AsyncHttpClient.sendRequest(getActivity(),request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getCategoryData resposnse json succeed : "+response.toString());
				parseCategoryDate(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "getCategoryData response json failed." + e.toString());
				parseCategoryDate(null);
			}
		},"");
	}
	private void parseCategoryDate(JSONObject response){
		if(response != null){
		try {
			if(response.getInt("result") == 1){
				SearchCategoryItemData item ;
				JSONArray aryData = response.getJSONArray("data");
				if(aryData != null){
					_lstCategory = new ArrayList<SearchCategoryItemData>();
					JSONObject data;
					String name;
					String p, up, p_s, up_s;
					String desc;
					int id ;
					for(int i=0; i < aryData.length(); i++){
						data = aryData.getJSONObject(i);
						if(data != null){
							id = data.getInt("id");
//							thumb = data.getString("thumb");
							p = data.getString("press");
							up = data.getString("unpress");
							p_s = data.getString("press_s");
							up_s = data.getString("unpress_s");
							name = data.getString("name");
							desc = data.getString("description");
							//item = new SearchCategoryItemData(id,thumb,name,desc);
							item = new SearchCategoryItemData(id,name,desc,
									p,up,p_s,up_s);
							_lstCategory.add(item);
						}
					}
				}
			}else{
				showToast.getInstance(getActivity()).showMsg(response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getActivity().getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}}
		loadCategoryGrid(); 
	}
	private void loadCategoryGrid(){
		_categoryAdapter.resetData(_searchIndex, _lstCategory);
		_gvCategory.setVisibility(View.VISIBLE);
	}
	public void cycleCategoryGrid(){
		_lstCategory = null;
		_categoryAdapter.resetData(_searchIndex, _lstCategory);
		_gvCategory.setVisibility(View.GONE);
	}
	public void hideAllDataLayout(){
		_gvCategory.setVisibility(View.GONE);
	}
	public interface onCategorySelectListener{
		public void onCategoryItemSelected();
	}
	
}
