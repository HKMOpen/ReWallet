package com.imusic.rewallet.model;

import java.io.Serializable;

public class RedeemExtensionOutputStruct implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8167472505459151276L;

	public String label;
	public int extension;
	public String distribution;
	public int count;
	public int location_id;
	
	public RedeemExtensionOutputStruct(String lb,int ex,  int c, int local){
		this.label = lb;
		this.extension = ex;
//		this.distribution = dist;
		this.count = c;
		this.location_id = local;
	}
}
