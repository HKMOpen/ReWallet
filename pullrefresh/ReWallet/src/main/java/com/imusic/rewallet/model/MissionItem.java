package com.imusic.rewallet.model;

import java.io.Serializable;

public class MissionItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5834755410710565394L;

	public String title;
	public int mission_id;
	public String description;
	public String thumb_sq;
	public int vcoin_reward;
	public String status;
	public String type;
	
	public MissionItem(){}
	public MissionItem(String t, int id, String desc, String thumbnail,
			int coin, String s, String tp){
		this.title = t;
		this.mission_id = id;
		this.description = desc;
		this.thumb_sq = thumbnail;
		this.vcoin_reward = coin;
		this.status = s;
		this.type = tp;
	}
	
	
}
