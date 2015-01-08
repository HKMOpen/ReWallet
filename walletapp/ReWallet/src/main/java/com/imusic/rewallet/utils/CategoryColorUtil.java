package com.imusic.rewallet.utils;


import com.imusic.rewallet.R;


public class CategoryColorUtil {

//	static List<Color> _lstRewardCategoryColors;
//	private static void initRewardCategoryColor(){
//		if(_lstRewardCategoryColors == null){
//			_lstRewardCategoryColors = new ArrayList<Color>();
//			_lstRewardCategoryColors.add(Resources.getSystem().getColor(R.color.category_reward_color_1));
//		}
//	}
	private final static int[] RewardCategoryColors = new int[]{
		R.color.category_reward_color_1
		,R.color.category_reward_color_2
		,R.color.category_reward_color_3
		,R.color.category_reward_color_4
		,R.color.category_reward_color_5
		,R.color.category_reward_color_6
		,R.color.category_reward_color_7
		,R.color.category_reward_color_8
	};
	private final static int[] AppCategoryColors = new int[]{
		R.color.category_app_color_1
		,R.color.category_app_color_2
		,R.color.category_app_color_3
		,R.color.category_app_color_4
		,R.color.category_app_color_5
		,R.color.category_app_color_6
		,R.color.category_app_color_7
		,R.color.category_app_color_8
	};
	public final static int getRewardCategoryColor(int index){
		int tt = index % 8;
		Vlog.getInstance().error(false, "CategoryColorUtil", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   "+tt +",,POSITION:" + index);
		return RewardCategoryColors[tt];
	}
	public final static int getAppCategoryColor(int index){
		return AppCategoryColors[index%8];
	}
	
}
