package com.imusic.rewallet.model;

import java.io.Serializable;
import java.util.Date;

public class MyVCoinLogListItem implements Serializable{

	
	private static final long serialVersionUID = 6139144798707393518L;

	public String transid;
	public String InOut;
	public String Description;
	public Date time;
	public int count;
	
	public MyVCoinLogListItem(String trID, String inout, String desc, Date t, int c){
		this.transid = trID;
		this.InOut = inout;
		this.Description = desc;
		this.time = t;
		this.count = c;
	}
	
}
