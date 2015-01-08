package com.imusic.rewallet.model;

import java.io.Serializable;

public class SearchCategoryItemData implements Serializable{

	private static final long serialVersionUID = 407835495722462561L;

		public int Id;
//		public String thumb;
		public String name;
		public String description;
		public String press;
		public String unpress;
		public String press_s;
		public String unpress_s;
		
//		public SearchCategoryItemData(int id, String img, String n, String desc){
//			this.Id = id;
//			this.thumb = img;
//			this.name = n;
//			this.description = desc;
//		}
		public SearchCategoryItemData(int id, String name, String desc,
				String p, String up, String p_s, String up_s){
			this.Id = id;
			this.name = name;
			this.description = desc;
			this.press = p;
			this.press_s = p_s;
			this.unpress = up;
			this.unpress_s = up_s;
		}

}
