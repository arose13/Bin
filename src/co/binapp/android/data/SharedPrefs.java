package co.binapp.android.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
	
	private static final String appPackage = "co.binapp.android.";
	
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	public static final int FREE_BIN_LIMIT = 20;
	
	public class Keys {
		public static final String USEREMAIL = appPackage + "USEREMAIL"; // Last logged in email
		public static final String PAYINGUSER = appPackage + "PAYINGUSER"; // 1 = true 0 = false
		public static final String NUM_OF_BINS = appPackage + "NUMBEROFBINS"; // Number of entries in the DS
	}
	
	public SharedPreferences buildSharedPreference(String key, Context context) {
		return context.getSharedPreferences(key, Context.MODE_PRIVATE);
	}
	
	/* UserEmail Methods */
	public SharedPreferences getUserEmailSharedPrefs(Context context) {
		return buildSharedPreference(Keys.USEREMAIL, context);
	}
	
	public boolean saveUserToSharedPrefs(String userEmail, Context context) {
		SharedPreferences.Editor editor = getUserEmailSharedPrefs(context).edit();
		
		if ((!userEmail.isEmpty()) && (userEmail != "")) {
			editor.putString(Keys.USEREMAIL, userEmail);
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
		return getUserEmailSharedPrefs(appContext).getString(Keys.USEREMAIL, "");
	}
	/* End of UserEmail Methods */
	
	/* PayingUser Methods */
	public SharedPreferences getPayingUserSharedPrefs(Context context) {
		return buildSharedPreference(Keys.PAYINGUSER, context);
	}
	/* End of PayingUser Methods */
}
