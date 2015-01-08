package com.imusic.rewallet.model;

public class GlobalDataInfo {

	static class Nested{
		static GlobalDataInfo _instance = new GlobalDataInfo();
	}
	public static GlobalDataInfo getInstance(){
		return Nested._instance;
	}
	private String _language;
	private int _searchRewardsCategoryId = -1;
	private int _searchRewardsCountryId = -1;
	private int _searchAppsCategoryId = -1;
	private int _searchAppsCountryId = -1;
	//private String _searchCategoryThumb;
	private String _searchRewardsCategoryPressThumb;
	private String _searchRewardsCategoryUnpressThumb;
	private String _searchRewardsCountryPressThumb;
	private String _searchRewardsCountryUnpressThumb;
	private String _searchRewardsCountryDesc;
	
	private String _searchAppsCategoryPressThumb;
	private String _searchAppsCategoryUnpressThumb;
	private String _searchAppsCountryPressThumb;
	private String _searchAppsCountryUnpressThumb;
	private String _searchAppsCountryDesc;
	
	public String getLanguage() {
		if(_language == null || _language.length() == 0)
			return "zh-hant";  //"en","zh","ja"
		return _language;
	}
	public void setLanguage(String language) {
		this._language = language;
	}
	
	public void clearAllData(){
		_searchRewardsCategoryId = 0;
		_searchRewardsCountryId = -1;
		_searchAppsCategoryId = 0;
		_searchAppsCountryId = -1;
		_searchRewardsCategoryPressThumb = null;
		_searchRewardsCategoryUnpressThumb = null;
		 _searchRewardsCountryPressThumb = null;
		_searchRewardsCountryUnpressThumb = null;
		_searchRewardsCountryDesc = null;
		_searchAppsCategoryPressThumb = null;
		_searchAppsCategoryUnpressThumb = null;
		_searchAppsCountryPressThumb = null;
		_searchAppsCountryUnpressThumb = null;
		_searchAppsCountryDesc = null;
	}
	
	
//----------------------------------------------------
	public int getRewardsSearchCategoryId(){
		return _searchRewardsCategoryId;
	}
	public void setRewardsSearchCategoryId(int id){
		this._searchRewardsCategoryId = id;
	}
	public int getAppsSearchCategoryId(){
		return _searchAppsCategoryId;
	}
	public void setAppsSearchCategoryId(int id){
		this._searchAppsCategoryId = id;
	}
//-----------------------------------------------------
	public int getRewardsSearchCountryId(){
		return _searchRewardsCountryId;
	}
	public void setRewardsSearchCountryId(int id){
		this._searchRewardsCountryId = id;
	}
	public int getAppsSearchCountryId(){
		return _searchAppsCountryId;
	}
	public void setAppsSearchCountryId(int id){
		this._searchAppsCountryId = id;
	}
// -----------------------------------------------------------
	public String getRewardsSearchCategoryUnpressThumb(){
		return _searchRewardsCategoryUnpressThumb;
	}
	public void setRewardsSearchCategoryUnpressThumb(String unpress){
		_searchRewardsCategoryUnpressThumb = unpress;
	}
	public String getRewardsSearchCategoryPressThumb(){
		return _searchRewardsCategoryPressThumb;
	}
	public void setRewardsSearchCategoryPressThumb(String press){
		_searchRewardsCategoryPressThumb = press;
	}
	public String getRewardsSearchCountryDesc(){
		return _searchRewardsCountryDesc;
	}
	public void setRewardsSearchCountryDesc(String desc){
		this._searchRewardsCountryDesc = desc;
	}
	public String getRewardsSearchCountryPressThumb(){
		return this._searchRewardsCountryPressThumb;
	}
	public void setRewardsSearchCountryPressThumb(String press){
		this._searchRewardsCountryPressThumb = press;
	}
	public String getRewardsSearchCountryUnpressThumb(){
		return this._searchRewardsCountryUnpressThumb;
	}
	public void setRewardsSearchCountryUnpressThumb(String unpress){
		this._searchRewardsCountryUnpressThumb = unpress;
	}
	
	//================
	public String getAppsSearchCategoryUnpressThumb(){
		return _searchAppsCategoryUnpressThumb;
	}
	public void setAppsSearchCategoryUnpressThumb(String unpress){
		_searchAppsCategoryUnpressThumb = unpress;
	}
	public String getAppsSearchCategoryPressThumb(){
		return _searchAppsCategoryPressThumb;
	}
	public void setAppsSearchCategoryPressThumb(String press){
		_searchAppsCategoryPressThumb = press;
	}
	public String getAppsSearchCountryDesc(){
		return _searchAppsCountryDesc;
	}
	public void setAppsSearchCountryDesc(String desc){
		this._searchAppsCountryDesc = desc;
	}
	public String getAppsSearchCountryPressThumb(){
		return this._searchAppsCountryPressThumb;
	}
	public void setAppsSearchCountryPressThumb(String press){
		this._searchAppsCountryPressThumb = press;
	}
	public String getAppsSearchCountryUnpressThumb(){
		return this._searchAppsCountryUnpressThumb;
	}
	public void setAppsSearchCountryUnpressThumb(String unpress){
		this._searchAppsCountryUnpressThumb = unpress;
	}
	
}
