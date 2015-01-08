package com.imusic.rewallet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RedeemExtensionOptionStruct implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384286477828155731L;

	public String label_new_name;
	public ArrayList<String> tags;
	public String id;
	public int order;
	
	public RedeemExtensionOptionStruct(String n, String i, int o, ArrayList<String> t){
		this.label_new_name = n;
		this.tags = t;
		this.id = i;
		this.order = o;
	}
	
}
