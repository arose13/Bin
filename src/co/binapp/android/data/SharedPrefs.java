package co.binapp.android.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
	
	private static final String appPackage = "co.binapp.android.";
	
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	public static final int FREE_BIN_LIMIT = 20;
	
	public class Keys {
		public static final String USERID = appPackage + "USERID"; // Last logged in email
		public static final String USERNAME = appPackage + "USERNAME"; // User Name
		public static final String PAYINGUSER = appPackage + "PAYINGUSER"; // 1 = true 0 = false
		public static final String NUM_OF_BINS = appPackage + "NUMBEROFBINS"; // Number of entries in the DS
	}
	
	/* Shared Preferences Methods Below */
	private SharedPreferences buildSharedPreference(String key, Context context) {
		return context.getSharedPreferences(key, Context.MODE_PRIVATE);
	}
	
	/* User Methods */
	private SharedPreferences getUserNameSharedPrefs(Context context) {
		return buildSharedPreference(Keys.USERNAME, context);
	}
	
	private SharedPreferences getUserIDSharedPrefs(Context context) {
		return buildSharedPreference(Keys.USERID, context);
	}
	
	public boolean saveUserNameToSharedPrefs(String userName, Context context) {
		SharedPreferences.Editor editor = getUserNameSharedPrefs(context).edit();
		
		if ((!userName.isEmpty()) && (userName != "")) {
			editor.putString(Keys.USERNAME, userName);
			editor.commit();
			
			if (!readFromSavedUserName(context).equals("")) {
				// UserName saved correctly
				return true;
			} else {
				// Error on UserName save
				return false;
			}
			
		} else {
			return false;
		}
	}
	
	public boolean saveUserIDToSharedPrefs(String userID, Context context) {
		SharedPreferences.Editor editor = getUserIDSharedPrefs(context).edit();
		
		if ((!userID.isEmpty()) && (userID != "")) {
			editor.putString(Keys.USERID, userID);
			editor.commit();
			
			if (!readFromSavedUser(context).equals("")) {
				// The Email was corrected saved
				return true;
			} else {
				// Email wasn't saved correctly
				return false;
			}
			
		} else {
			return false;
		}
	}
	
	public String readFromSavedUser(Context appContext) {
		return getUserIDSharedPrefs(appContext).getString(Keys.USERID, "");
	}
	
	public String readFromSavedUserName(Context appContext) {
		return getUserNameSharedPrefs(appContext).getString(Keys.USERNAME, "");
	}
	/* End of UserEmail Methods */
	
	/* PayingUser Methods */
	// TODO: Enable paying users
//	private SharedPreferences getPayingUserSharedPrefs(Context context) {
//		return buildSharedPreference(Keys.PAYINGUSER, context);
//	}
	/* End of PayingUser Methods */

}
