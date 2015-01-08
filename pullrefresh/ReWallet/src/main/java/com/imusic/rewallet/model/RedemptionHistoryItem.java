package com.imusic.rewallet.model;

import android.text.Html;

import java.io.Serializable;
import java.util.Date;

public class RedemptionHistoryItem implements Serializable{

	private static final long serialVersionUID = 1L;

	public int type;
	public int productId;
	public String productName;
	public String imageUrl;
	public String vendor;
	public Date expDate;
	public String countryDescription;
	public int amount;
	public String description;
	public String product_url;
	public int p_status;
	public int c_status;
	public String categoryImage;
	public String category;
	public String country;
	public String qr_a;
	public String qr_b;
	public int shareCount;
	public int commentCount;
	public String imageBanner;
	public String videoCover;
	public int handle;
//	public RedemptionHistoryDummyItem(int id, String img, String n, String v, String exp){
//		this.productId = id;
//		this.imageUrl = img;
//		this.productName = n;
//		this.vendor = v;
//		this.expDate = exp;
//	}
	public RedemptionHistoryItem(int pId, String pName, String img,
			String vender, Date eDate, String cDesc, int count, String desc
			,String pUrl){
		this.productId = pId;
		this.productName = pName == null? null: Html.fromHtml(pName).toString();
		this.imageUrl = img;
		this.vendor = vender == null ? null : Html.fromHtml(vender).toString();
		this.expDate = eDate;
		this.countryDescription = cDesc == null ? null : Html.fromHtml(cDesc).toString();
		this.amount = count;
		this.description = desc == null ? null : Html.fromHtml(desc).toString();
		this.product_url = pUrl;
	}
	
	
}
