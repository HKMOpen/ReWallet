package com.imusic.rewallet.model;

import java.io.Serializable;

public class RewardDetailItem implements Serializable{

	private static final long serialVersionUID = -457592958104904906L;

	public int Id;
	public int redemption_lock;
	public int share_count;
	public String remarks;
	public String product_description;
	public String distribution;
//	public List<String> extensions;
	public int vcoin_value;
	public String redemption_procedure;   // int???
	public String title;
	public String video_url;
	public String note_1;
	public String note_2;
	public String note_3;
	public String note_4;
	public String vendor_name;
	public int comment_count;
	public String image_video_cover;
	public String tnc;
	public int stock_id;
	public String product_url;
	public int vendor_id;
	public String image_slider;
	public String category;   // int???
	public String categoryImage;
	public String country;
	public String countryDescription;
	public String expiration_date;
	public String image_small_thumb;
	public int redeem_count;
	public String image_banner;
//	public HashMap<String, String> addresses;
//	public List<RewardDetailStockCount> stockCount;
	public int totalcount;
	
}
