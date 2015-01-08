package com.imusic.rewallet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AppDetailItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3633767102804321197L;

	public String title;
	public String icon_url;
	public String store_id;
	public String description;
	public int developer_id;
	public String app_name;
	public int coin;
	public int share_count;
	public int download_count;
	public String country;
	public String category;
	public int comment_count;
	public String vendor;
	public int Id;
	public String app_key;
	public ArrayList<String> screenshot;
	
	public AppDetailItem(String appName, int devId
			, String t, String i_url, String st_id, String desc,
			int c, int s_c, int d_c,
			String ctry, String cat, int c_c, String v){
		this.title = t;
		this.icon_url = i_url;
		this.store_id = st_id;
		this.description = desc;
		this.developer_id = devId;
		this.app_name = appName;
		this.coin = c;
		this.share_count = s_c;
		this.download_count = d_c;
		this.country = ctry;
		this.category = cat;
		this.comment_count = c_c;
		this.vendor = v;
	}
}
