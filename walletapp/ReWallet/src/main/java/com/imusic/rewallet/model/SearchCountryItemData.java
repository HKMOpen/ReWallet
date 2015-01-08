package com.imusic.rewallet.model;

import java.io.Serializable;

public class SearchCountryItemData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585282896136565978L;
	public int Id;
	public String name;
	public String description;
	public String press;
	public String unpress;
	
	public SearchCountryItemData(int id, String n, String desc){
		this.Id = id;
		this.name = n;
		this.description = desc;
	}

}
