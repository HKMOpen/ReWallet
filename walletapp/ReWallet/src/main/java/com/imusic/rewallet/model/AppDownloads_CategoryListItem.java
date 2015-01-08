package com.imusic.rewallet.model;

import java.io.Serializable;

public class AppDownloads_CategoryListItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20021L;
	
	public String appID;
	public String thumbnailUrl;
	public String appName;
	public String slug;
	public String category;
	public String coinAmount;
	
	public AppDownloads_CategoryListItem(String id, String imgUrl, 
			String name, String sl, String c, String count){
		this.appID = id;
		this.thumbnailUrl = imgUrl;
		this.appName = name;
		this.slug = sl;
		this.category = c;
		this.coinAmount = count;
	}

}
