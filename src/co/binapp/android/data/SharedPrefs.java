package co.binapp.android.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
	
	private Context context;
	
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
	
	public SharedPrefs(Context inputContext) {
		context = inputContext;
	}
	
	/* Shared Preferences Methods Below */
	private SharedPreferences buildSharedPreference(String key) {
		return context.getSharedPreferences(key, Context.MODE_PRIVATE);
	}
	
	/* User Methods */
	private SharedPreferences getUserNameSharedPrefs() {
		return buildSharedPreference(Keys.USERNAME);
	}
	
	private SharedPreferences getUserIDSharedPrefs() {
		return buildSharedPreference(Keys.USERID);
	}
	
	public boolean saveUserNameToSharedPrefs(String userName) {
		SharedPreferences.Editor editor = getUserNameSharedPrefs().edit();
		
		if ((!userName.isEmpty()) && (userName != "")) {
			editor.putString(Keys.USERNAME, userName);
			editor.commit();
			
			if (!readFromSavedUserName().equals("")) {
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
	
	public boolean saveUserIDToSharedPrefs(String userID) {
		SharedPreferences.Editor editor = getUserIDSharedPrefs().edit();
		
		if ((!userID.isEmpty()) && (userID != "")) {
			editor.putString(Keys.USERID, userID);
			editor.commit();
			
			if (!readFromSavedUser().equals("")) {
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
	
	public String readFromSavedUser() {
		return getUserIDSharedPrefs().getString(Keys.USERID, "");
	}
	
	public String readFromSavedUserName() {
		return getUserNameSharedPrefs().getString(Keys.USERNAME, "");
	}
	/* End of UserEmail Methods */
	
	/* PayingUser Methods */
	// TODO: Enable paying users
//	private SharedPreferences getPayingUserSharedPrefs(Context context) {
//		return buildSharedPreference(Keys.PAYINGUSER, context);
//	}
	/* End of PayingUser Methods */

}
