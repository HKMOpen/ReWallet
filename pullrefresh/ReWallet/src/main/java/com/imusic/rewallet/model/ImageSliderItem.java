package com.imusic.rewallet.model;

import java.io.Serializable;

public class ImageSliderItem implements Serializable{

	
	private static final long serialVersionUID = -1180780441700006747L;
	public int Id;
	public int postId;
	public String collection_name;
	public String url;
	public int width;
	public int height;
	public float nextToTime;
	public String type;
	public ImageSliderItem(int pid,int id, String t, String u, int w, int h, float nextTotime
			,String tp){
		this.postId = pid;
		this.Id = id;
		this.collection_name = t;
		this.url = u;
		this.width = w;
		this.height= h;
		this.nextToTime = nextTotime;
		this.type = tp;
	}
}
