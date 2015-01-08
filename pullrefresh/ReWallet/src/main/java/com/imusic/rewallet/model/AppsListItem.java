package com.imusic.rewallet.model;

import java.io.Serializable;

public class AppsListItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3465138276467585962L;
	
	public String icon;
	public String store_id;
	public String description;
	public String platform;
	public int developer;
	public String developer_name;
	public String app_name;
	public String cat_term;
	public int redeem_count;
	public int comment_count;
	public int ID;
	public int coin;
	
	public AppsListItem(String img, String sId, String desc, String pf,
			String devName, int devId, String name, String cat, int rCount, int cCount, int aid,
			int coinCount){
		this.icon = img;
		this.store_id = sId;
		this.description = desc;
		this.platform = pf;
		this.developer = devId;
		this.developer_name = devName;
		this.app_name = name;
		this.cat_term = cat;
		this.redeem_count = rCount;
		this.comment_count = cCount;
		this.ID = aid;
		this.coin = coinCount;
	}

}
