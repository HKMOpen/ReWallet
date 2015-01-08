package com.imusic.rewallet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GcmDataManager {

	private final static String TAG = "GcmDataManager";
	
	//FOR GCM
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_EMAIL = "registration_email";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        
    GoogleCloudMessaging _gcm;
    Context _context;
    Activity _activityContext;
    String _regid;
    String _email;
    //END FOR GCM
    /**
     * initialize with context...
     * @param appContext
     * @param activityContext
     */
    public void setContext(Context appContext, Activity activityContext)
    {
    	_context = appContext;
    	_activityContext = activityContext;
    }
    /**
     * Register the device.
     * @param email
     */
    public void register(String email)
    {
    	_email = email;
    	if (_email == null || _email.isEmpty())
    		return;
    	
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            _gcm = GoogleCloudMessaging.getInstance(_activityContext);
            _regid = getRegistrationId(_context);
                        
            if (_regid.isEmpty()) {
                registerInBackground();
           }
            else
            {
            	String e = getRegistrationEmail(_context);
            	if (e.compareToIgnoreCase(email) != 0)
            	{
            		//unregister the old one then register the new one
            		registerInBackground(e, _regid);
            	}
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    /**
     * Refresh
     *  
     */
    public void refresh()
    {
    	if (_email == null || _email.isEmpty())
    		return;
    	checkPlayServices();
    }
    
    /**
     * Unregister the device.
     * @param email
     */
    public void unregister()
    {
    	String e = getRegistrationEmail(_context);
    	if (e == null || e.isEmpty())
    		return;
    	
    	if (_regid == null || _regid.isEmpty())
    		_regid = getRegistrationId(_context);
    	
    	if (_regid == null || _regid.isEmpty())
    		return;
    	
    	unregisterInBackground(e, _regid);
    }
    
    /**
     * Check if the push settings of this email is enabled.
     * @param email
     * @return
     */
    public boolean isPushEnabled(String email)
    {
    	String e = getRegistrationEmail(_context);
    	
    	if (e == null || e.isEmpty())
    		return false;
    	
    	if (e.compareToIgnoreCase(email) == 0)
    		return true;

    	return false;
    }
    
	/**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_activityContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, _activityContext,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String email, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_EMAIL, email);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    private String getRegistrationEmail(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String email = prefs.getString(PROPERTY_REG_EMAIL, "");
        if (email.isEmpty()) {
            Log.i(TAG, "Registration email not found.");
            return "";
        }
        
        return email;
    }
    
    /**
     * Clear the email and registration id...
     * @param context
     */
    private void clearRegistrationId(Context context) {
    	 final SharedPreferences prefs = getGcmPreferences(context);
         int appVersion = getAppVersion(context);
         Log.i(TAG, "Clearing regId on app version " + appVersion);
         SharedPreferences.Editor editor = prefs.edit();
         editor.remove(PROPERTY_REG_ID);
         editor.remove(PROPERTY_REG_EMAIL);
         editor.commit();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
    	    	
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                	Log.e(TAG, "register in background");
                    if (_gcm == null) {
                        _gcm = GoogleCloudMessaging.getInstance(_context);
                    }
                    _regid = _gcm.register(Constants.GlobalKey.getPushSenderID());
                    msg = "Device registered, registration ID=" + _regid;
                    Log.e(TAG, msg);
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_context, _email, _regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }
    
    /**
     * register after unregister the previous email...
     * @param oldEmail
     * @param regid
     */
    private void registerInBackground(final String oldEmail, final String regid) {
    	
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {                	
                	unregisterToBackend(oldEmail, regid);                	
                	clearRegistrationId(_context);
                	
                	
                	Log.e(TAG, "register in background");
                    if (_gcm == null) {
                        _gcm = GoogleCloudMessaging.getInstance(_context);
                    }
                    _regid = _gcm.register(Constants.GlobalKey.getPushSenderID());
                    msg = "Device registered, registration ID=" + _regid;
                    Log.e(TAG, msg);
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_context, _email, _regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }
    
    private void unregisterInBackground(final String email, final String regid) {
    	
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                	Log.e(TAG, "unregister in background");
                    
                    unregisterToBackend(email, regid);
                    
                    clearRegistrationId(_context);

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }
	
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
    	
        return context.getSharedPreferences("myPushPrefs", Context.MODE_PRIVATE);
    }
    
    
    private void unregisterPreviousEmail(String newEmail)
    {
    	String oldEmail = getRegistrationEmail(_context);
    	if (oldEmail == null || oldEmail.isEmpty()){
    		return; // no email before...
    	}
    	
    	if (oldEmail.compareToIgnoreCase(newEmail) != 0){
    		//unregister previous...
    		
    	}
    }
    
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
      // Your implementation here.
    	Log.i(TAG, "register lo......");
    	GcmServerUtilities.register(_context, _email, _regid);
    }
    
    private void unregisterToBackend(String email, String regid) {
    	Log.i(TAG, "unregister lo......");
    	GcmServerUtilities.unregister(_context, email, regid);
    }
}
