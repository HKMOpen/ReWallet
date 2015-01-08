package com.imusic.rewallet.model;

import java.io.Serializable;
import java.util.Date;

public class MyCouponItem extends RedemptionHistoryItem implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2843616704462851491L;

	public String code;
	public int status;
	
	public MyCouponItem(int pId, String pName, String img, String vender,
			Date eDate, String cDesc, int count, String desc, String code
			, String vendorUrl){
		super(pId, pName, img, vender, eDate, cDesc, count, desc, vendorUrl);
		this.code = code;
	}
	
}
