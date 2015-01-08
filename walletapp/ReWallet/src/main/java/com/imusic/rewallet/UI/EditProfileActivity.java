package com.imusic.rewallet.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.imusic.rewallet.R;
import com.imusic.rewallet.R.layout;
import com.imusic.rewallet.asyncnetwork.AbstractAsyncResponseListener;
import com.imusic.rewallet.asyncnetwork.AsyncHttpClient;
import com.imusic.rewallet.asyncnetwork.HttpRequestHelper;
import com.imusic.rewallet.model.GlobalDataInfo;
import com.imusic.rewallet.model.LoginUserInfo;
import com.imusic.rewallet.model.UserInfo;
import com.imusic.rewallet.utils.Constants;
import com.imusic.rewallet.utils.ResponseResultCode;
import com.imusic.rewallet.utils.Vlog;
import com.imusic.rewallet.utils.showToast;
import com.imusic.rewallet.widgets.TextboxForProfileLarge;
import com.imusic.rewallet.widgets.TextboxForProfileSmall;
import com.imusic.rewallet.widgets.WheelView;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends BaseNavibarActivity implements OnClickListener {

	private final static String TAG = "EditProfileActivity";
	
	private static final int REQUESTCODE = 40301;
	
	private TextboxForProfileSmall _edCountry;
	private TextboxForProfileSmall _edGender;
	private TextboxForProfileSmall _edBirthday;
	private TextboxForProfileLarge _edNickName;
	private TextboxForProfileLarge _edFirstName;
	private TextboxForProfileLarge _edLastName;
	private Button _imgbtnOK;
	private UserInfo _userInfo ;//= LoginUserInfo.getInstance().getLoginUserInfo();
	
	private String[] _gender ;//= new String[]{getResources().getString(R.string.global_Gender_Female)
//			,getResources().getString(R.string.global_Gender_Male)};
//	private String 
	private int _selectedGenderIndex ;
	private int _selectedGenderTempIndex ;
	
	private int _selectedCountryIndex;
	private int _selectedCountryTempIndex;
	private String _selectedCountryCode;
	private JSONArray _jsonAryCountry;
	private ArrayList<String> _aryCountryName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		_gender = new String[]{getResources().getString(R.string.global_Gender_Female)
				,getResources().getString(R.string.global_Gender_Male)};
		setupView();
	}

	private void setupView(){
		setupActionBar(getString(R.string.editProfileActivity_title), true, 
				false,null,null);
		_edCountry = (TextboxForProfileSmall)findViewById(R.id.editProfile_edLocation);
		_edGender = (TextboxForProfileSmall)findViewById(R.id.editProfile_edGender);
		_edBirthday = (TextboxForProfileSmall)findViewById(R.id.editProfile_edBirthday);
		_edNickName = (TextboxForProfileLarge)findViewById(R.id.editProfile_edNickName);
		_edFirstName = (TextboxForProfileLarge)findViewById(R.id.editProfile_edFirstName);
		_edLastName = (TextboxForProfileLarge)findViewById(R.id.editProfile_edLastName);
		_imgbtnOK = (Button)findViewById(R.id.editProfile_btnOK);
		_imgbtnOK.setOnClickListener(this);
		setupData();
		getCountryData();
	} 
	private void setupData(){
		_userInfo = new UserInfo();
		_edCountry.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().countryName, false);
		_edCountry.setOnCustomClickListener(countryItemClickListener);
		_edGender.setContentEnable(
				LoginUserInfo.getInstance().getLoginUserInfo().gender
				.equals("M")? getString(R.string.global_Gender_Male): getString(R.string.global_Gender_Female)
						, false);
		_edGender.setOnCustomClickListener(genderItemClickListener);
		if(LoginUserInfo.getInstance().getLoginUserInfo().birthday != null)
			_edBirthday.setContentEnable(new SimpleDateFormat("yyyy-MM-dd").format(LoginUserInfo.getInstance().getLoginUserInfo().birthday), false);
		else
			_edBirthday.setContentEnable(null, true);
		_edNickName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().nickName, true);
		_edFirstName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().firstName, true);
		_edLastName.setContentEnable(LoginUserInfo.getInstance().getLoginUserInfo().lastName, true);
		_edBirthday.setOnCustomClickListener(birthdayItemClickListener);
		
		Vlog.getInstance().debug(false, TAG, "token:"+LoginUserInfo.getInstance().getToken());
	}
	
	private OnClickListener countryItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(_aryCountryName == null || _aryCountryName.size() <1)
				return;
			
			View outerView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.wheel_view, null);
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
			//wv.setOffset(1);
			wv.setItems(_aryCountryName);
			_selectedCountryTempIndex = _selectedCountryIndex;
			wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
				@Override
                public void onSelected(int selectedIndex, String item) {
                    Vlog.getInstance().debug(false,TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    _selectedCountryTempIndex = selectedIndex -1;
                }
            });
			 new AlertDialog.Builder(EditProfileActivity.this)
             .setView(outerView)
             .setPositiveButton("OK", new  DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					_selectedCountryIndex = _selectedCountryTempIndex;
					Vlog.getInstance().debug(false, TAG, "~~~~selected country index :" + _selectedGenderIndex+ " / "+  _aryCountryName.get(_selectedCountryIndex));
					try {
						Vlog.getInstance().debug(false, TAG, "~~~~selected country CODE :" + _selectedGenderIndex+ " / "+  _jsonAryCountry.getJSONObject(_selectedCountryIndex).getString("countrycode"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					_userInfo.countryName = _aryCountryName.get(_selectedCountryIndex);
					_edCountry.setEditContent(_aryCountryName.get(_selectedCountryIndex));
				}
             }) 
             .show();
			 wv.setSeletion(_selectedCountryIndex);
			 Vlog.getInstance().debug(false, TAG, "org selected index: " + _selectedCountryIndex + " / "+ _aryCountryName.get(_selectedCountryIndex));
		
		}
	};
	
	private OnClickListener genderItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//final int intSelected ;
			if(_userInfo.gender != null && _userInfo.gender.length() > 0){
				_selectedGenderIndex = _userInfo.gender.equals("M")?1:0;
			}else{
				_selectedGenderIndex = LoginUserInfo.getInstance()
						.getLoginUserInfo().gender.equals("M")?1:0;
			}
			//= _userInfo.gender.equals("M")?1:0; // _edGender.getEditContent().equals("Male")?1:0;
			
			View outerView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.wheel_view, null);
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
			//wv.setOffset(1);
			wv.setItems(Arrays.asList(_gender));
			_selectedGenderTempIndex = _selectedGenderIndex;
			wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
				@Override
                public void onSelected(int selectedIndex, String item) {
                    Vlog.getInstance().debug(false,TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    _selectedGenderTempIndex = selectedIndex -1;
                }
            });
			 new AlertDialog.Builder(EditProfileActivity.this)
             .setView(outerView)
             .setPositiveButton("OK", new  DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					_selectedGenderIndex = _selectedGenderTempIndex;
					Vlog.getInstance().debug(false, TAG, "~~~~selected gender index :" + _selectedGenderIndex+ " / "+ _gender[_selectedGenderIndex]);
					_userInfo.gender =  _selectedGenderIndex == 0? "F":"M";
					_edGender.setEditContent(_gender[_selectedGenderIndex]);
				}
             }) 
             .show();
			 wv.setSeletion(_selectedGenderIndex);
			 Vlog.getInstance().debug(false, TAG, "org selected index: " + _selectedGenderIndex + " / "+ _gender[_selectedGenderIndex]);
		}
	};
	private OnClickListener birthdayItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
	        Date mydate = _userInfo.birthday == null? 
	        		LoginUserInfo.getInstance().getLoginUserInfo().birthday 
	        		: _userInfo.birthday;
	        if(mydate != null)
	        	mycalendar.setTime(mydate);////ΪCalendar��������ʱ��Ϊ��ǰ����
	        
			DatePickerDialog dpd = new DatePickerDialog(EditProfileActivity.this
					, Datelistener
					, mycalendar.get(Calendar.YEAR)
					, mycalendar.get(Calendar.MONTH)
					, mycalendar.get(Calendar.DAY_OF_MONTH));
			
			dpd.show();	
		}
	};
	private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
			mycalendar.set(year, monthOfYear, dayOfMonth);
			_userInfo.birthday = mycalendar.getTime();
			_edBirthday.setContentEnable(new SimpleDateFormat("yyyy-MM-dd").format(_userInfo.birthday), false);
			Vlog.getInstance().error(false, TAG, LoginUserInfo.getInstance().getLoginUserInfo().birthday == null ? "null" : "login birthday:"+ new SimpleDateFormat("yyyy-MM-dd").format(LoginUserInfo.getInstance().getLoginUserInfo().birthday));
			Vlog.getInstance().error(false, TAG, "now birthday:"+ new SimpleDateFormat("yyyy-MM-dd").format(_userInfo.birthday));
		}
	};
	
	
	
	
	
	
	
	
	@Override
	protected void parseData(JSONObject response) {
		boolean isSuccess = false;
		int resultCode;
		try {
			resultCode = response.getInt("result");
			if(resultCode == 1){
				isSuccess = true;
			}else if(resultCode == ResponseResultCode.RESULT_EXPIREDATE){
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}else{
				showToast.getInstance(getApplicationContext())
				.showMsg(getString(R.string.global_stringForat_getData_Error) 
						+ response.getString("msg"));
			}
		} catch (JSONException e) {
			showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			e.printStackTrace();
		}
		refreshUI(isSuccess);
	}

	@Override
	protected void refreshUI(boolean isSuccess) {
		if(isSuccess){
			setResult(RESULT_OK);
			finish();
		}
	}

	private void getCountryData(){
		HttpRequest request = HttpRequestHelper.getGetRequest(
				Constants.Url.getCountryCodeListUrl(GlobalDataInfo.getInstance().getLanguage()));
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "getCountryData resposnse json succeed : "+response.toString());
				parserCountryCode(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "getCountryData response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"");
	}
	private void parserCountryCode(JSONObject response){
		if(response != null){
			try {
				int result = response.getInt("result");
				if(result == 1){
					_jsonAryCountry = response.getJSONArray("data");
					_aryCountryName = new ArrayList<String>();
					if(_jsonAryCountry != null && _jsonAryCountry.length() > 0){
						for(int i = 0; i < _jsonAryCountry.length(); i++){
							_aryCountryName.add(_jsonAryCountry.getJSONObject(i).getString("name"));
							if(_jsonAryCountry.getJSONObject(i).getString("name").equals(_edCountry.getEditContent().trim())){
								_selectedCountryIndex = i;
							}
						}
					}
					
				}else{
					Vlog.getInstance().error(true, TAG, "parserCountryCode failed."+response.getInt("result") + " / " +response.getString("msg"));
					showToast.getInstance(this).showMsg(response.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	@Override
	protected void getData() {
		Vlog.getInstance().debug(false, TAG, "token:"+LoginUserInfo.getInstance().getToken());
		String url = Constants.Url.getChangeProfileDetailUrl(LoginUserInfo.getInstance().getToken()
				, Constants.GlobalKey.getAppKey()
				, _userInfo.firstName
				, _userInfo.lastName
				, _userInfo.nickName
				, null
				, _userInfo.gender
				, null
				, _selectedCountryCode
				, _userInfo.birthday == null? null: new SimpleDateFormat("dd/MM/yyyy").format(_userInfo.birthday)
				, null, null, null, null
				, _userInfo.countryName);
		Vlog.getInstance().debug(false, TAG, "post:" + url);
    	HttpRequest request = HttpRequestHelper.getPostRequest(url, null);
		AsyncHttpClient.sendRequest(this,request ,new AbstractAsyncResponseListener(AbstractAsyncResponseListener.RESPONSE_TYPE_JSON_OBJECT){
			@Override  
            protected void onSuccess(JSONObject response, String tag){ 
				Vlog.getInstance().info(false,TAG, "change user data resposnse json succeed : "+response.toString());
				parseData(response);
			}
			@Override  
            protected void onFailure(Throwable e, String tag) { 
				Vlog.getInstance().info(false,TAG,  "change user data response json failed." + e.toString());
				showToast.getInstance(getApplicationContext()).showMsg(e.getMessage());
			}
		},"");
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.editProfile_btnOK){
			if(!_edNickName.getEditContent()
					.equals(LoginUserInfo.getInstance().getLoginUserInfo().nickName)){
				_userInfo.nickName = _edNickName.getEditContent();
			}
			if(!_edFirstName.getEditContent()
					.equals(LoginUserInfo.getInstance().getLoginUserInfo().firstName)){
				_userInfo.firstName = _edFirstName.getEditContent();
			}
			if(!_edLastName.getEditContent()
					.equals(LoginUserInfo.getInstance().getLoginUserInfo().lastName)){
				_userInfo.lastName = _edLastName.getEditContent();
			}
//			if(_userInfo.gender != null &&
//					_userInfo.gender.equals(LoginUserInfo.getInstance().getLoginUserInfo().gender)){
//				_userInfo.gender = null;
//			}
			if(_userInfo.birthday != null &&
					_userInfo.birthday == LoginUserInfo.getInstance().getLoginUserInfo().birthday){
				_userInfo.birthday = null;
			}
			if(_userInfo.countryName != null ){
				if(_userInfo.countryName != LoginUserInfo.getInstance().getLoginUserInfo().countryName){
					try {
						_selectedCountryCode = _jsonAryCountry.getJSONObject(_selectedCountryIndex).getString("countrycode");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					_userInfo.countryName  = null;
					_selectedCountryCode = null;
				}
			}
			getData();
		}
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE && resultCode == RESULT_OK){
			_handler.obtainMessage(MSG_GETDATA).sendToTarget();
		}
	}
}
