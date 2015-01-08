package com.imusic.rewallet.utils;

import android.content.Context;

import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.widgets.MainActionBar;

import org.json.JSONArray;
import org.json.JSONException;

public class CategoryCountryUtil {

	public static String[] getCategory(Context context, int searchIndex, JSONArray aryCategory
			,String forID, String forName, String forImage){
		String[] cat = new String[2];
		String name = null;
		String url = "";
		if(aryCategory != null && aryCategory.length() > 0){
			try {
				int bb = searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
						GlobalDataInfo.getInstance().getRewardsSearchCategoryId()
						:GlobalDataInfo.getInstance().getAppsSearchCategoryId();
				if (bb > 0) {
					boolean found = false;
					int oId  = bb;
					int id = -1;
					int index = 0;
					for (int i = 0; i < aryCategory.length(); i++) {
						id = aryCategory.getJSONObject(i).getInt(forID);
						if(oId == id){
							found = true;
							index = i;
							break;
						}
					}
					if(found){
						if(forName != null){
							name = aryCategory.getJSONObject(index).getString(forName);
							cat[0] = name;
						}
						if(forImage != null){
							url = aryCategory.getJSONObject(index).getString(forImage);
							cat[1] = url;
						}
//						url = aryCategory.getJSONObject(index).getString("url");
//						cat[1] = url;
						
						
					}else{
//						url = aryCategory.getJSONObject(0).getString("url");
						if(forName != null){
							name = aryCategory.getJSONObject(0).getString(forName);
							cat[0] = name;
						}
						if(forImage != null){
							url = aryCategory.getJSONObject(0).getString(forImage);
							cat[1] = url;
						}
						
					}
				}else{
//					url = aryCategory.getJSONObject(0).getString("url");
					if(forName != null){
						name = aryCategory.getJSONObject(0).getString(forName);
						cat[0] = name;
					}
					if(forImage != null){
						url = aryCategory.getJSONObject(0).getString(forImage);
						cat[1] = url;
					}
					
				}
				
			}catch (JSONException e) {
				showToast.getInstance(context).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		return cat;
	}
	
	public static String[] getCountryDescription(Context context,int searchIndex, JSONArray aryCountry
			, String forName, String forDesc){
		String[] cty = new String[2];
		String desc = null;
		String cname = null;
		if(aryCountry != null && aryCountry.length() > 0){
			try {
				String bb = searchIndex == MainActionBar.MAINACTIONBAR_REWARDS ?
						GlobalDataInfo.getInstance().getRewardsSearchCountryDesc()
						:GlobalDataInfo.getInstance().getAppsSearchCountryDesc();
						
				if (bb != null) {
					boolean found = false;
					String oDesc = bb;
					String dd = null;
					int index = 0;
					for (int i = 0; i < aryCountry.length(); i++) {
						dd = aryCountry.getJSONObject(i).getString(forDesc);
						if(dd.equals(oDesc)){
							found = true;
							index = i;
							break;
						}
					}
					if(found){
						desc = dd;
						cname = aryCountry.getJSONObject(index).getString(forName);
						cty[1] = desc;
						cty[0] = cname;
						
					}else{
						desc = aryCountry.getJSONObject(0).getString(forDesc);
						cname = aryCountry.getJSONObject(0).getString(forName);
						cty[1] = desc;
						cty[0] = cname;
					}
				} else {
					desc = aryCountry.getJSONObject(0).getString(forDesc);
					cname = aryCountry.getJSONObject(0).getString(forName);
					cty[1] = desc;
					cty[0] = cname;
				}
			} catch (JSONException e) {
				showToast.getInstance(context).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		return cty;
	}
	
}
