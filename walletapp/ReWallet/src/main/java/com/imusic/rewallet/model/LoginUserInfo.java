package com.imusic.rewallet.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LoginUserInfo {
	
	private static class Nested{
		static final LoginUserInfo info = new LoginUserInfo();
	}
	
	// ------------------------------------------------
	private String userToken ;
	private String userNonce;
	private UserInfo userInfo = null;
	
	public static LoginUserInfo getInstance(){
		return Nested.info;
	}
	
	public String getToken(){
		return userToken;
	}
	public void setToken(String nonce,JSONObject info){
		userNonce = nonce;
		try {
			JSONObject user = info.getJSONObject("data");
			if(user != null){
				userToken = user.getString("token");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	public void setTokenExpiry(){
		userToken = null;
		userNonce = null;
	}
	public void cleanInfo(){
		userInfo = null;
		userToken = null;
		userNonce = null;
	}
	
	public void setDraftUserInfo(JSONObject info){
		userInfo = new UserInfo();
		try {
			JSONObject user = info.getJSONObject("data");
			userInfo.displayName = user.getString("displayname");
			userInfo.email = user.getString("email");
			userInfo.id = user.getInt("id");
			userInfo.userName = user.getString("username");
			userInfo.nickName = user.getString("nickname");
			userInfo.firstName = user.getString("firstname");
			userInfo.lastName = user.getString("lastname");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void setUserNickName(String nickName){
		if(userInfo == null){
			userInfo = new UserInfo();
		}
		userInfo.nickName = nickName;
	}
	public void setDisplayUserInfo(JSONObject info){
		//cleanInfo();
		userInfo = null;
		userInfo = new UserInfo();
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("d/M/yy");
//			userToken = info.getString("token");
			JSONObject user = info.getJSONObject("data");
			if(user != null){
				userInfo.displayName = user.getString("displayname");
				userInfo.email = user.getString("email");
				userInfo.id = user.getInt("id");
				userInfo.userName = user.getString("username");
				userInfo.nickName = user.getString("nickname");
				userInfo.firstName = user.getString("firstname");
				userInfo.lastName = user.getString("lastname");
				userInfo.profile_picture = user.getString("profile_picture");
				userInfo.gender = user.getString("gender");  //global_Gender_Male
				JSONObject country = user.getJSONObject("country");
				if(country != null){
					userInfo.countryID = country.getString("ID");
					userInfo.countryName = country.getString("name");
				}
				try {
					userInfo.birthday =  dateformat.parse(user.getString("birthday"));
				} catch (ParseException e) {
					userInfo.birthday = null;
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public UserInfo getLoginUserInfo(){
		return userInfo;
	}
	public void setLoginUserInfo(UserInfo info){
		userInfo = info;
	}
	public void setToken(String token) {
		userToken = token;
	}
	
	
}
