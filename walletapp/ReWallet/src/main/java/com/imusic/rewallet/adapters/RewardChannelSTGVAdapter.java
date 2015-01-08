package com.imusic.rewallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imusic.rewallet.R;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.RewardListItem;
import com.imusic.rewallet.utils.CategoryColorUtil;
import com.imusic.rewallet.utils.Util;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.squareup.picasso.Picasso;

public class RewardChannelSTGVAdapter extends BaseAdapter {
	
	private static final String TAG  = "RewardChannelSTGVAdapter";
	
    private Context _context;
    private Activity _App;
    
    private ArrayList<RewardListItem> _items = new ArrayList<RewardListItem>();

    private RelativeLayout.LayoutParams _layoutParams;
    private RelativeLayout.LayoutParams _layoutTitleParams;
    private int imageWidth = 0;
    private DisplayImageOptions _imageOptions ;//= Util.getImageLoaderOptionForList();
    
    public void setImageLayoutParams(RelativeLayout.LayoutParams params){
    	_layoutParams = params;
    	imageWidth = params.width;
    	if(_layoutTitleParams == null)
    		_layoutTitleParams =  new RelativeLayout.LayoutParams(params.width,params.height/2);
    	else{
    		_layoutTitleParams.width = params.width;
    		_layoutTitleParams.height = params.height /2;
    	}
    }
    
    public RewardChannelSTGVAdapter(Activity app, ArrayList<RewardListItem>  lst) {
    	_context = app.getApplicationContext();
        _App = app;
        _items = lst;
        //getMoreItem();
        //getNewItem();
        _imageOptions = Util.getImageLoaderOptionForList(_context);
    }
    public ArrayList<RewardListItem> getItemList(){
    	return _items;
    }

//    public void getNewItem() {
////    	RewardDummyItem item = new RewardDummyItem();
////        item.url = mData.url[newPos];
////        item.width = mData.width[newPos];
////        item.height = mData.height[newPos];
////        item.coinCount = 1 + (int)(Math.random() * 1000); 
////        item.commentCount = (int)(Math.random() * 1000); 
////        item.gifeCount = 1 + (int)(Math.random() * 300);  
////        item.category = "category A";
////        item.author = "by iMusicTech";
////        item.title = "Fantastic Reward!";
////        mItems.add(0, item);
////        newPos = (int)(Math.random() * 20); //= (newPos - 1) % 19;
//    	Log.i(TAG, "request: " + Constants.Url.getRewardChannelList(0, 0));
//    	HttpRequest request = HttpRequestHelper.getGetRequest(Constants.Url.getRewardChannelList(0, 0));
//		AsyncHttpClient.sendRequest(mApp,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
//			@Override  
//            protected void onSuccess(JSONObject response, String tag){ 
//				Log.i(TAG, "reward list resposnse json succeed : "+response.toString());
//				refreshItemList(response);
//			}
//			@Override  
//            protected void onFailure(Throwable e, String tag) { 
//				Log.i(TAG, "reward list  response json failed." + e.toString());
//				
//			}
//		},"");
//    }
    
    public void refreshItemList(JSONObject response){
    	_items = new ArrayList<RewardListItem>();
    	parseJSONData(response);
    }
    private void parseJSONData(JSONObject response){
    	try {
			int result = response.getInt("result");
			if (result == 1) {
				JSONArray dataArray = response.getJSONArray("data");
				if (dataArray != null) {
//					mItems = new ArrayList<RewardListItem>();
					RewardListItem item = null;// = new RewardDummyItem();
					String title = null;
					String video_image_cover = null;
					String image_sq_thumb = null;
					int vcoin = 0;
					int id = 0;
					String type = null;
					JSONObject jVendor = null;
					String vendor = null;
					String redeemCount = null;
					int commentCount = 0;
					String category = null;
					JSONArray aryCategory = null;
					for (int i = 0; i < dataArray.length(); i++) {
						id = dataArray.getJSONObject(i).getInt("ID");
						title = dataArray.getJSONObject(i).getString("title");
						video_image_cover = dataArray.getJSONObject(i)
								.getString("video_image_cover");
						image_sq_thumb = dataArray.getJSONObject(i).getString(
								"image_sq_thumb");
						jVendor = dataArray.getJSONObject(i).getJSONObject("vendor");
						if(jVendor != null){
							vendor = jVendor.getString("title");
						}
						type =  dataArray.getJSONObject(i).getString("type");
						vcoin = dataArray.getJSONObject(i).getInt("vcoin");
						redeemCount = dataArray.getJSONObject(i).getString("redeem_count");
						commentCount = dataArray.getJSONObject(i).getInt("comment_count");
						aryCategory = dataArray.getJSONObject(i).getJSONArray("category");
//						category = parseCategory(aryCategory);
						String[] cat = getCategory(aryCategory);
						category = cat[1];
						Vlog.getInstance().info(false, TAG, title + " category : " + category);
						item = new RewardListItem(id, category, title,
								video_image_cover, image_sq_thumb, vcoin,
								vendor,
								redeemCount == null ? 0 : Integer.valueOf(redeemCount), 
										commentCount,type);
						item.categoryPressThumb = cat[2];
						item.categoryUnpressThumb = cat[3];
						_items.add(item);
					}
					Vlog.getInstance().error(false, TAG, "~~~~~~~~  reward list refresh~~~~~~");
				}
			}else{
				showToast.getInstance(_App).showMsg(response.getString("msg"));
			}
			this.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
			showToast.getInstance(_App.getApplicationContext()).showMsg(e.getMessage());
		}
    }
    public String[] getCategory(JSONArray aryCategory){
		String[] cat = new String[4];
		String name = null;
		String ID = null;
		String press = null;
		String unPress = null;
		if(aryCategory != null && aryCategory.length() > 0){
			try {
				if (GlobalDataInfo.getInstance().getRewardsSearchCategoryId() > 0) {
					boolean found = false;
					int oId  = GlobalDataInfo.getInstance().getRewardsSearchCategoryId();
					int id = -1;
					int index = 0;
					for (int i = 0; i < aryCategory.length(); i++) {
						id = aryCategory.getJSONObject(i).getInt("id");
						if(oId == id){
							found = true;
							index = i;
							break;
						}
					}
					if(found){
						name = aryCategory.getJSONObject(index).getString("name");
						ID = aryCategory.getJSONObject(index).getString("id");
						press = aryCategory.getJSONObject(index).getString("press_s");
						unPress = aryCategory.getJSONObject(index).getString("unpress_s");
						cat[0] = ID;
						cat[1] = name;
						cat[2] = press;
						cat[3] = unPress;
					}else{
						ID = aryCategory.getJSONObject(0).getString("id");
						name = aryCategory.getJSONObject(0).getString("name");
						press = aryCategory.getJSONObject(0).getString("press_s");
						unPress = aryCategory.getJSONObject(index).getString("unpress_s");
						cat[0] = ID;
						cat[1] = name;
						cat[2] = press;
						cat[3] = unPress;
					}
				}else{
					name = aryCategory.getJSONObject(0).getString("name");
					ID = aryCategory.getJSONObject(0).getString("id");
					press = aryCategory.getJSONObject(0).getString("press_s");
					unPress = aryCategory.getJSONObject(0).getString("unpress_s");
					cat[0] = ID;
					cat[1] = name;
					cat[2] = press;
					cat[3] = unPress;
				}
				
			}catch (JSONException e) {
				showToast.getInstance(_context).showMsg(e.getMessage());
				e.printStackTrace();
			}
		}
		return cat;
	}
//    private String parseCategory(JSONArray aryCategory){
//    	String category = null;
//    	try{
//    		if(aryCategory != null && aryCategory.length() > 0){
//    			if(aryCategory.length() == 1){
//    				category = aryCategory.getJSONObject(0).getString("name");
//    			}else {
//    				if(GlobalDataInfo.getInstance().getSearchCategoryId() > 0){
//    					int id = 0;
//    					for(int j = 0; j < aryCategory.length(); j++){
//    						id = aryCategory.getJSONObject(j).getInt("id");
//    						if(id == GlobalDataInfo.getInstance().getSearchCategoryId()){
//    							category = aryCategory.getJSONObject(j).getString("name");
//    							break;
//    						}
//    					}
//    					if(category == null){
//    						Vlog.getInstance().info(false, TAG, "category foreach is none, get first one!" );
//    						category = aryCategory.getJSONObject(0).getString("name");
//    					}
//    				}else{
//    					String desc = null;
//    					for(int j = 0; j < aryCategory.length(); j++){
//    						desc = Html.fromHtml(aryCategory.getJSONObject(j).getString("description")).toString().trim();
//    						Vlog.getInstance().info(false, TAG, "description~!~!!!!!!!!!!!!!!" + desc);
//    						if(desc.equals("ALL")){
//    							category = aryCategory.getJSONObject(j).getString("name");
//    							Vlog.getInstance().info(false, TAG, "~~~~~~~~  ALL~~~~~~~~~~~!~!!!!!!!!!!!!!!");
//    							break;
//    						}
//    					}
//    					if(category == null){
//    						Vlog.getInstance().info(false, TAG, "category foreach is none, get first one!" );
//    						category = aryCategory.getJSONObject(0).getString("name");
//    					}
//    				}
//    			}
//    		}else
//    			category = null;
//    	}catch (JSONException e){
//    		showToast.getInstance(_App.getApplicationContext()).showMsg(e.getMessage());
//    		e.printStackTrace();
//    	}
//    	return category;
//    }
    
    public void addMoreItemToList(JSONObject response){
    	parseJSONData(response);
//    	JSONArray dataArray;
//		try {
//			int result = response.getInt("result");
//			if (result == 1) {
//				dataArray = response.getJSONArray("data");
//				if (dataArray != null) {
////					mItems = new ArrayList<RewardListItem>();
//					RewardListItem item = null;// = new RewardDummyItem();
//					String title = null;
//					String video_image_cover = null;
//					String image_sq_thumb = null;
//					int vcoin = 0;
//					int id = 0;
//					String type = null;
//					String category = "categoryA";
//					JSONObject jVendor = null;
//					String vendor = null;
//					String redeemCount = null;
//					int commentCount = 0;
//					for (int i = 0; i < dataArray.length(); i++) {
//						id = dataArray.getJSONObject(i).getInt("ID");
//						title = dataArray.getJSONObject(i).getString("title");
//						video_image_cover = dataArray.getJSONObject(i)
//								.getString("video_image_cover");
//						type =  dataArray.getJSONObject(i).getString(
//								"type");
//						jVendor = dataArray.getJSONObject(i).getJSONObject("vendor");
//						if(jVendor != null){
//							vendor = jVendor.getString("title");
//						}
//						image_sq_thumb = dataArray.getJSONObject(i).getString(
//								"image_sq_thumb");
//						vcoin = dataArray.getJSONObject(i).getInt("vcoin");
//						redeemCount = dataArray.getJSONObject(i).getString("redeem_count");
//						commentCount = dataArray.getJSONObject(i).getInt("comment_count");
//						item = new RewardListItem(id, category, title,
//								video_image_cover, image_sq_thumb, vcoin,
//								vendor, redeemCount == null ? 0 : Integer.valueOf(redeemCount), 
//										commentCount, type);
//						_items.add(item);
//					}
//					this.notifyDataSetChanged();
//					Vlog.getInstance().error(false, TAG, "~~~~~~~~  reward list get more data refresh~~~~~~");
//				}
//			}else{
//				showToast.getInstance(_App).showMsg(response.getString("msg"));
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }
    
    @Override
    public int getCount() {
        return _items == null ? 0 : _items.size();
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //View view = null;
    	ViewHolder holder = null;
        final RewardListItem item = _items.get(position);
//
//        String url = item.image_sq_thumb ;
 

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(_context, R.layout.stgv_cell, null);
            holder.img_content = (ImageView) convertView.findViewById(R.id.reward_gridview_cell_imgContent);
            holder.tv_title = (TextView) convertView.findViewById(R.id.reward_gridview_cell_tvTitle);
            holder.tv_category = (TextView)convertView.findViewById(R.id.reward_gridview_cell_tvCategory);
//            holder.tv_gife = (TextView)convertView.findViewById(R.id.reward_gridview_cell_tvGife);
//            holder.tv_comment = (TextView)convertView.findViewById(R.id.reward_gridview_cell_tvComments);
//            holder.tv_by = (TextView)convertView.findViewById(R.id.reward_gridview_cell_tvAuthor);
            holder.tv_coinCount = (TextView)convertView.findViewById(R.id.reward_gridview_cell_tvCoinCount);
//            holder.Rlayout = (RelativeLayout)convertView.findViewById(R.id.reward_gridview_cell_layout);
//            holder.titleLayout = (LinearLayout)convertView.findViewById(R.id.reward_gridview_cell_layout_title);
//            holder.img_category = (ImageView)convertView.findViewById(R.id.reward_gridview_cell_imgCategory);
            holder.marginLeftLayout = (LinearLayout)convertView.findViewById(R.id.reward_gridview_cell_layout_leftMargin);
            holder.categoryLayout = (RelativeLayout)convertView.findViewById(R.id.reward_gridview_cell_layout_category);
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(item.title == null ? null : Html.fromHtml(item.title));
//        holder.tv_by.setText(item.vendor == null? null : Html.fromHtml(item.vendor));
        holder.tv_category.setText(item.category == null ? null : Html.fromHtml(item.category));
//        holder.tv_gife.setText(String.valueOf(item.redeemCount));
//        holder.tv_comment.setText(String.valueOf(item.commentCount));
        holder.tv_coinCount.setText(String.valueOf(item.coinCount));
        if(_layoutParams != null)
        	holder.img_content.setLayoutParams(_layoutParams);
        Vlog.getInstance().error(false, TAG, "reward cell size : " + _layoutParams.width);
        ViewGroup.LayoutParams pMargin = holder.marginLeftLayout.getLayoutParams();
        pMargin.height = _layoutParams.height * 96 / 100;
        holder.marginLeftLayout.setLayoutParams(pMargin);
        
        holder.marginLeftLayout.setBackgroundColor(_context.getResources().getColor(CategoryColorUtil.getRewardCategoryColor(position%8)));
        holder.categoryLayout.setBackgroundColor(_context.getResources().getColor(CategoryColorUtil.getRewardCategoryColor(position%8)));
//        holder.categoryLayout.setBackgroundColor(CategoryColorUtil.RewardCategoryColors[position]);
//        Vlog.getInstance().error(false, TAG,  String.format("category:  %d , %d" ,holder.categoryLayout.getHeight() , holder.categoryLayout.getWidth()));
        
        //Picasso.with(mContext).load(url).into(holder.img_content);
        //holder.Rlayout.setLayoutParams(new LinearLayout.LayoutParams(_layoutParams.width, LayoutParams.WRAP_CONTENT));
        //Vlog.getInstance().debug(false, TAG, "url -->" + url);
        ImageLoader.getInstance().displayImage(item.image_sq_thumb, holder.img_content, _imageOptions);
//        ImageLoader.getInstance().displayImage(item.categoryUnpressThumb, holder.img_category, _imageOptions);
//        holder.titleLayout.setLayoutParams(_layoutTitleParams);
//        ViewGroup.LayoutParams p = holder.titleLayout.getLayoutParams();
//        p.width = imageWidth;
//        p.height = p.width /2;
//        holder.titleLayout.setLayoutParams(p);
        
//        FontManager.changeFonts(parent, _App);
        
        return convertView;
    }
    

    class ViewHolder {
        ImageView img_content;
//        ImageView img_category;
        TextView tv_title;
        TextView tv_category;
//        TextView tv_by;
//        TextView tv_gife;
//        TextView tv_comment;
        TextView tv_coinCount;
//        RelativeLayout Rlayout;
//        LinearLayout titleLayout;
        LinearLayout marginLeftLayout;
        RelativeLayout categoryLayout;
    }

}
