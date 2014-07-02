package co.binapp.android.data;

import com.google.android.gms.common.Scopes;

public class GPlusConstants {

	public static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	public static final String[] VISABLE_ACTIVITIES = new String[] {
		"http://schemas.google.com/AddActivity", 
		"http://schemas.google.com/CheckInActivity", 
		"http://schemas.google.com/CommentActivity",
		"http://schemas.google.com/CreateActivity"
	};
	
	public static String[] SCOPES = new String[] {
		Scopes.PLUS_LOGIN, // GPlus login scope
		Scopes.PLUS_ME, // GPlus profile scope
		"https://www.googleapis.com/auth/userinfo.email" // GPlus get Email from user
	};
	
}
