package com.imusic.rewallet.model;

import java.io.Serializable;

public class RewardCommentItem implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 200L;
	public int cID;
	public String userName;
	public String commentContent;

	public RewardCommentItem(int id, String n, String c) {
		this.cID = id;
		this.userName = n;
		this.commentContent = c;
	}
		
}
