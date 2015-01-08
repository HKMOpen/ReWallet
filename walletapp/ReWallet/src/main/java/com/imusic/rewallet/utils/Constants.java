package com.imusic.rewallet.utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class Constants {
	public static final String PRODUCTNAME = "VCoinApp";
	public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	
	public static class Url{
		private static String DomainForloginDev= "http://devlogin.vcoinapp.com";
		private static String DomainForCMSDev= "http://devcms.vcoinapp.com";
		private static String DomainForLoginProduct = "http://login.vcoinapp.com";
		private static String DomainForCMSProduct= "http://cms.vcoinapp.com";
		
		private static String nonceUrl = "api/get_nonce/?controller=auth&method=generate_auth_cookie";
		private static String loginFormatUrl = "api/auth/generate_token/?nonce=%s&username=%s&password=%s&key=%s&hash=%s";
		private static String myProfileDisplayDataUrl = "api/personal/display_user/?token=%s&appkey=%s";
		private static String myProfileChangeDetailUrl = "api/personal/changedetail/?token=%s&appkey=%s";
		private static String missionDataUrl = "api/mission/get_mission_list/?token=%s&appkey=%s";
		private static String myBalanceUrl = "api/vcoin/getbalance/?token=%s&appkey=%s";
		private static String countryCodeListUrl = "api/listing/country_codes_for_user/?lang=%s";
		private static String myVCoinHistoryUrl = "api/vcoin/vcoin_history/?feature=custom&token=%s&appkey=%s&start=%s&end=%s&index=%d&transid=&sort=time&order=desc&needbalance=yes";
		private static String myRewardHistoryUrl = "api/redemption/redeem_list/?token=%s&appkey=%s";
		private static String myRewardScanPickupProcessUrl = "api/redemption/redeem_obtain/?token=%s&appkey=%s&redemption_procedure=%d&note=%s&qr=%s&lang=%s";
		
		
		private static String rewardListUrl = "api/listing/search";
		private static String rewardDetailUrl = "api/single/reward/?id=%s&lang=%s";
		private static String rewardSliderUrl = "api/listing/top_sliders/?lang=%s&ids=%d.%d&type=rewards";
		private static String rewardCategoryUrl = "api/listing/category/?lang=%s&type=rewards&country=%d";
		private static String rewardCountryUrl = "api/listing/country/?lang=%s&country=-1";
		private static String couponDetailUrl = "api/single/coupon/?id=%d";
		private static String watchVideoUrl = "api/mission/watchvideo/?token=%s&appkey=%s&post_id=%d";
		private static String appListUrl = "api/listing/apps/?platform=android";
		private static String appCategoryUrl = "api/listing/category/?lang=%s&type=android&country=%d";
		private static String appCountryUrl = "api/listing/country/?lang=%s&type=android&country=-1";//platform
		private static String appDetailUrl = "api/single/app/?id=%d&lang=%s";
		private static String applistSliderUrl = "api/listing/top_sliders/?lang=%s&ids=%d.%d&type=android";
		private static String appDownloadUrl = "api/mission/vcoinappdownload/?token=%s&appkey=%s&down_app_key=%s";
		
		private static String comment_addUrl = "api/personal/addcomment/?token=%s&appkey=%s&objectid=%d&comment=%s";
		private static String comment_removeUrl = "api/personal/removecomment/?token=%s&appkey=%s&comment_id=%d&reference_id=%d";
		private static String share_addUrl = "api/single/add_share_count/?id=%d";
		
		private static String redeemExtension = "api/stock/stock_count_ext_v2/?id=%d";
		private static String redeemRewardUrl = "api/redemption/redeem_submission/?appkey=%s&token=%s&product_id=%d&checkdoubles=%d&distribution=%s&offer_expiry_date=%s&extension_id=%d&address_id=%d&price=%d";
		private static String redeemECouponUrl = "api/redemption/redeem_coupon_submission/?appkey=%s&token=%s&couponid=%d";
		private static String myCouponUrl = "api/redemption/mi_e_coupon_list_history/?token=%s&appkey=%s";
		
		private static String changePasswordUrl = "api/personal/changedetail/?token=%s&appkey=%s&old_password=%s&password=%s";
		
		
		private static String feedbackUrl_EN = "feedback-form/";
		private static String feedbackUrl_CN = "zh-hant/feedback-chinese/";
		private static String feedbackUrl_JAP = "feedback-form-ja/";
		
		private static String privacyPolicyUrl_EN = "privacy-policy/";
		private static String privacyPolicyUrl_CN = "privacy-policy-cn/";
		private static String privacyPolicyUrl_JAP = "privacy-policy-ja/";
		
		private static String supportUrl_EN = "support/";
		private static String supportUrl_CN = "support-cn/";
		private static String supportUrl_JAP = "support-ja/";
		
		private static String TCUrl_EN = "tc/";
		private static String TCUrl_CN = "tc-cn/";
		private static String TCUrl_JAP = "tc-ja/";

		private static String RegisterUrl_EN = "user_registration_mobile/";
		private static String RegisterUrl_CN = "user_registration_mobile_cn/?lang=cn";
		private static String RegisterUrl_JAP = "user_registration_mobile_ja/?lang=ja";
		
		private static String PushUrl_Server_dev = "http://devpush.vcoinapp.com/api/";
		private static String pushUrl_Server_Pro= "http://push.vcoinapp.com/api/";
		private static String PushPath_Register = "device/register.php";
		private static String PushPath_Unregister = "device/unregister.php";
				
		private static boolean _isDev = false;
		
		private static String getDomainForlogin(){
			 if(_isDev)
				 return DomainForloginDev;
			 else
				 return DomainForLoginProduct;
		}
		private static String getDomainForCMS(){
			if(_isDev)
				 return DomainForCMSDev;
			 else
				 return DomainForCMSProduct;
		}
		private static String getDomainForPush(){
			if(_isDev)
				 return PushUrl_Server_dev;
			 else
				 return pushUrl_Server_Pro;
		}
		public static String getRegisterUrl(String lang){
			String domainUrl  = getDomainForlogin();
			String tagUrl = null;
			if(lang.equals("zh-hant")){
				tagUrl = RegisterUrl_CN;
			}else if(lang.equals("ja")){
				tagUrl = RegisterUrl_JAP;
			}else{
				tagUrl = RegisterUrl_EN;
			}
			return String.format("%s/%s", domainUrl,tagUrl);
		}
		public static String getFeedbackUrl(String lang){
			String domainUrl  = getDomainForlogin();
			String tagUrl = null;
			if(lang.equals("zh-hant")){
				tagUrl = feedbackUrl_CN;
			}else if(lang.equals("ja")){
				tagUrl = feedbackUrl_JAP;
			}else{
				tagUrl = feedbackUrl_EN;
			}
			return String.format("%s/%s", domainUrl,tagUrl);
		}
		public static String getPrivacyPolicyUrl(String lang){
			String domainUrl  = getDomainForlogin();
			String tagUrl = null;
			if(lang.equals("zh-hant")){
				tagUrl = privacyPolicyUrl_CN;
			}else if(lang.equals("ja")){
				tagUrl = privacyPolicyUrl_JAP;
			}else{
				tagUrl = privacyPolicyUrl_EN;
			}
			return String.format("%s/%s", domainUrl,tagUrl);
		}
		public static String getSupportUrl(String lang){
			String domainUrl  = getDomainForlogin();
			String tagUrl = null;
			if(lang.equals("zh-hant")){
				tagUrl = supportUrl_CN;
			}else if(lang.equals("ja")){
				tagUrl = supportUrl_JAP;
			}else{
				tagUrl = supportUrl_EN;
			}
			return String.format("%s/%s", domainUrl,tagUrl);
		}
		public static String getTCUrl(String lang){
			String domainUrl  = getDomainForlogin();
			String tagUrl = null;
			if(lang.equals("zh-hant")){
				tagUrl = TCUrl_CN;
			}else if(lang.equals("ja")){
				tagUrl = TCUrl_JAP;
			}else{
				tagUrl = TCUrl_EN;
			}
			return String.format("%s/%s", domainUrl,tagUrl);
		}
		
		
		public static String getNonceUrl(){
			return String.format("%s/%s", getDomainForlogin(),nonceUrl);
		}
		public static String getLoginUrl(String nonce, String username, String password,String key, String hash){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),loginFormatUrl);
			String url = String.format(urlFormat, nonce,enCodeUrl(username),enCodeUrl(password), key, hash);
			return url;
		}
		public static String getMyBalance(String token, String appKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myBalanceUrl);
			return String.format(urlFormat, token, appKey);
		}
		public static String getChangePasswordUrl(String token, String appKey, String oldPassword, String newPassword){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),changePasswordUrl);
			return String.format(urlFormat, token, appKey, enCodeUrl(oldPassword), enCodeUrl(newPassword));
		}
		public static String getWatchVideoUrl(String token, String appkey, int postId){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),watchVideoUrl);
			return String.format(urlFormat, token, appkey, postId);
		}
		public static String getMyVCoinHistoryUrl(String token, String appKey,
				String startTime, String endTime, int index){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myVCoinHistoryUrl);
			return String.format(urlFormat, token, appKey, startTime, endTime, index);
		}
		public static String getMyRewardHistoryUrl(String token, String appKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myRewardHistoryUrl);
			return String.format(urlFormat, token, appKey);
		}
		public static String getMyRewardScanPickupProcessUrl(String token, String appKey
				,int procedure, String note, String qr, String lang){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myRewardScanPickupProcessUrl);
			return String.format(urlFormat, token, appKey,procedure, note == null? "" : enCodeUrl(note), qr == null? "":enCodeUrl(qr), lang);
		}
		
		public static String getMyCouponUrl(String token, String appKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myCouponUrl);
			return String.format(urlFormat, token, appKey);
		}
		public static String getRedeemECouponUrl(String token, String appKey, int id, String lang){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),redeemECouponUrl);
			return String.format(urlFormat, appKey, token, id);
		}
		public static String getRedeemExtension(int id, String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),redeemExtension);
			String url = String.format(urlFormat,id);
			if(lang != null && lang.length() > 0){
				url += String.format("&lang=%s", lang);
			}
			return url;
		}
		public static String getRedeemRewardUrl(String token, String appKey,
				int pid, boolean checkdouble, String dist, String expDate, int extId, int addressId, int price, String lang){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),redeemRewardUrl);
//			redeemRewardUrl = "api/redemption/redeem_submission/?appkey=%s&token=%s&product_id=%d&checkdoubles=%d&distribution=%s&offer_expiry_date=%s&extension_id=%d&address_id=%d&price=%d";
			
			String url = String.format(urlFormat, appKey, token, pid, checkdouble? 1:0, dist, expDate, extId, addressId,price);
			if(lang != null && lang.length() > 0){
				url += String.format("&lang=%s", lang);
			}
			return url;
		}
		public static String getCountryCodeListUrl(String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),countryCodeListUrl);
			String url = String.format(urlFormat, lang);
			return url;
		}
		public static String getMissionUrl(String token, String appKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),missionDataUrl);
			return String.format(urlFormat, token, appKey);
		}
		public static String getProfileDisplayUrl(String token, String appKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myProfileDisplayDataUrl);
			return String.format(urlFormat, token, appKey);
		}
		public static String getChangeProfileDetailUrl(String token, String appKey,
				String firstname, String lastname, String nickname, String description,
				String gender, String user_url, String countryID, String birthday,
				String setting_push_sms ,String sms_number, String password, String email, String countryName){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),myProfileChangeDetailUrl);
			String url = String.format(urlFormat, token,appKey);
			if(firstname != null && firstname.length() >0){
				url += String.format("&firstname=%s", enCodeUrl(firstname));
			}
			if(lastname != null && lastname.length() >0){
				url += String.format("&lastname=%s", enCodeUrl(lastname));
			}
			if(nickname != null && nickname.length() >0){
				url += String.format("&nickname=%s", enCodeUrl(nickname));
			}
			if(description != null && description.length() >0){
				url += String.format("&description=%s", enCodeUrl(description));
			}
			if(gender != null && gender.length() >0){
				url += String.format("&gender=%s", gender);
			}
			if(user_url != null && user_url.length() >0){
				url += String.format("&user_url=%s", enCodeUrl(user_url));
			}
			if(countryID != null && countryID.length() >0){
				url += String.format("&countrycode=%s", enCodeUrl(countryID));
			}
			if(birthday != null && birthday.length() >0){
				url += String.format("&birthday=%s", birthday);
			}
			if(setting_push_sms != null && setting_push_sms.length() >0){
				url += String.format("&setting_push_sms=%s", enCodeUrl(setting_push_sms));
			}
			if(sms_number != null && sms_number.length() >0){
				url += String.format("&sms_number=%s", enCodeUrl(sms_number));
			}
			if(password != null && password.length() >0){
				url += String.format("&password=%s", enCodeUrl(password));
			}
			if(email != null && email.length() >0){
				url += String.format("&email=%s", enCodeUrl(email));
			}
			if(countryName != null && countryName.length() >0){
				url += String.format("&country=%s", enCodeUrl(countryName));
			}
			return url;
		}
		
		public static String getRewardCountryList(String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),rewardCountryUrl);
			return String.format(urlFormat, lang);
		}
		public static String getRewardCategoryList(String lang, int countryId){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),rewardCategoryUrl);
			return String.format(urlFormat, lang, countryId);
		}
		public static String getRewardChannelList(int cat, int country, String keyword, String lang, int p){
			String url = null;
			String urlTemp = null;
			url = urlTemp =  String.format("%s/%s", getDomainForCMS(),rewardListUrl);
			if(cat != 0 && country != 0 ){
				url += String.format("?cat=%s&country=%s", String.valueOf(cat), String.valueOf(country));
			}else if(cat != 0){
				url += String.format("?cat=%s", String.valueOf(cat));
			}else if(country != 0){
				url += String.format("?country=%s", String.valueOf(country));
			}
			if(keyword != null && keyword.length() >0){
				if(url == urlTemp )
					url += String.format("?keyword=%s", enCodeUrl(keyword));
				else
					url += String.format("&keyword=%s", enCodeUrl(keyword));
			}
			if(lang != null && lang.length() >0){
				if(url == urlTemp )
					url += String.format("?lang=%s", enCodeUrl(lang));
				else
					url += String.format("&lang=%s", enCodeUrl(lang));
			}
			if(p > 0){
				if(url == urlTemp )
					url += String.format("?p=%s", String.valueOf(p));
				else
					url += String.format("&p=%s", String.valueOf(p));
			}
			return url;
		}
		
		public static String getRewardChannelSliderUrl(int country,int cat, String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),rewardSliderUrl);
			String url = String.format(urlFormat,lang, country,cat);
			return url;
		}
		public static String getApplistSliderUrl(int country, int cat, String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),applistSliderUrl);
			String url = String.format(urlFormat,lang, country,cat);
			return url;
		}
		public static String getAppDownloadUrl(String token, String appKey, String downloadKey){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),appDownloadUrl);
			String url = String.format(urlFormat,token, appKey,downloadKey);
			return url;
		}
		public static String getRewardDetailUrl(int id, String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),rewardDetailUrl);
			String url = String.format(urlFormat, String.valueOf(id), lang);
			return url;
		}
		public static String getCouponDetailUrl(int id){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),couponDetailUrl);
			return String.format(urlFormat, id);
		}
		
		public static String getAddCommentUrl(String token, String appKey, int postId, String comment){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),comment_addUrl);
			String url = String.format(urlFormat, token, appKey, postId, enCodeUrl(comment));
			return url;
		}
		public static String getRemoveCommentUrl(String token, String appKey, int postId, int refId){
			String urlFormat = String.format("%s/%s", getDomainForlogin(),comment_removeUrl);
			String url = String.format(urlFormat, token, appKey, postId, refId);
			return url;
		}
		public static String getAddShareUrl(int id){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),share_addUrl);
			String url = String.format(urlFormat,id);
			return url;
		}
		public static String getAppList(String keyword, String developerId, int cat, int country, String lang, int p){
			//&cat=%s&keyword=%s&country=%s&developer=%s
			String url = String.format("%s/%s", getDomainForCMS(),appListUrl);
			if(cat != 0){
				url += String.format("&cat=%s", String.valueOf(cat));
			}
			if(country != 0){
				url += String.format("&country=%s", String.valueOf(country));
			}
			if(keyword != null){
				url += String.format("&keyword=%s", enCodeUrl(keyword));
			}
			if(developerId != null){
				url += String.format("&developer=%s", developerId);
			}
			if(lang != null && lang.length() >0){
				url += String.format("&lang=%s", enCodeUrl(lang));
			}
			if(p >0){
				url += String.format("&p=%s", String.valueOf(p));
			}
			return url;
		}
		public static String getAppCategoryList(String lang,int countryId){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),appCategoryUrl);
			return String.format(urlFormat, lang,countryId);
		}
		public static String getAppCountryList(String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(),appCountryUrl);
			return String.format(urlFormat, lang);
		}
		public static String getAppDetailUrl(int id, String lang){
			String urlFormat = String.format("%s/%s", getDomainForCMS(), appDetailUrl);
			return String.format(urlFormat, id,lang);
		}
		private static String enCodeUrl(String url){
			return Uri.encode(url);
		}
		
		public static String getPushNotificationRegisterUrl()
		{
			return String.format("%s%s", getDomainForPush(), PushPath_Register);
		}
		public static String getPushNotificationUnregisterUrl()
		{
			return String.format("%s%s", getDomainForPush(), PushPath_Unregister);
		}		
	}
	public static class GlobalKey{
		private static String appKey = "yoqzLezk";
		private static String appSecret = "a81f4ea20b03e7861af745b03b9cb1785166bb7e0beaa62cdcf3597885a05c7cbfe7f6a54dcb16de7569287b7bac505115a54af7a0fd7e0a67d96d040f775cc9";
		private static String downloadKey = "123";
		
		/**
	     * Substitute you own sender ID here. This is the project number you got
	     * from the API Console, as described in "Getting Started."
	     */
		private static String Push_SenderID = "352828365138";
		
		public static String getAppKey() {
			return appKey;
//			return "yoqzLezk";
		}
//		public static void setAppKey(String appKey) {
//			GlobalKey.appKey = appKey;
//		}
		public static String getAppSecret() {
			return appSecret;
//			return "a81f4ea20b03e7861af745b03b9cb1785166bb7e0beaa62cdcf3597885a05c7cbfe7f6a54dcb16de7569287b7bac505115a54af7a0fd7e0a67d96d040f775cc9";
		}
//		public static void setAppSecret(String appSecret) {
//			GlobalKey.appSecret = appSecret;
//		}
		public static String getDownloadAppKey(){
			return downloadKey;
		}
		
		public static String getPushSenderID()
		{
			return Push_SenderID;
		}
	}
	
	
	public static final class Directory {
		private static String _Foloder;
		public  Directory(){
			String strProductPath = String.format("%s/%s", SDCARD,PRODUCTNAME);
			File file = new File(strProductPath);
			if(!file.exists()){
				file.mkdir();
			}
		}
		public static String getRootFolder(){
			return String.format("%s/%s", SDCARD,PRODUCTNAME);
		}
		
		public static String getFolder(){
			_Foloder = String.format("%s/%s",SDCARD,PRODUCTNAME );
			File file = new File(_Foloder);
			if(!file.exists()){
				file.mkdir();
			}
			return _Foloder;
		}
		public static String getImageCacheFolder(){
			_Foloder = String.format("%s/%s",getFolder(),"ImageCache");
			File file = new File(_Foloder);
			if(!file.exists()){
				file.mkdir();
			}
			return _Foloder;
		}
		public static String getCrashLogFolder(){
			_Foloder = String.format("%s/%s",getFolder(),"CrashLog");
			File file = new File(_Foloder);
			if(!file.exists()){
				file.mkdir();
			}
			return _Foloder;
		}
		
	}
}
