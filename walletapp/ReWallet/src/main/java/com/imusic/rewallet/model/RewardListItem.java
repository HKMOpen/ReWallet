package com.imusic.rewallet.model;

import java.io.Serializable;

public class RewardListItem  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 166L;
    public String title;
    public String category;
    public int redeemCount;
    public int commentCount;
    public int coinCount;
    public String vendor;
    public int id;
    public String image_sq_thumb;
    public String video_image_cover;
    public String type;
    public String categoryPressThumb;
    public String categoryUnpressThumb;
    
    public RewardListItem(int id,String cat,String t, String v, 
    		String img, int vCount, String vd, int g, int c, String tp){
    	this.title = t;
    	this.video_image_cover = v;
    	this.image_sq_thumb = img;
    	this.coinCount = vCount;
    	this.vendor = vd;
    	this.redeemCount = g;
    	this.commentCount = c;
    	this.id = id;
    	this.category = cat;
    	this.type = tp;
    }
}
