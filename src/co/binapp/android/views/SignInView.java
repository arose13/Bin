package co.binapp.android.views;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.model.people.PersonBuffer;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.data.Fonts.Amatic;
import co.binapp.android.data.LogStrings.ViewNames;
import co.binapp.android.data.AnimationConstants;
import co.binapp.android.data.GPlusConstants;
import co.binapp.android.data.LogStrings;
import co.binapp.android.data.SharedPrefs;

public class SignInView extends ViewActivity implements OnClickListener {
	
	private SharedPrefs sharedPrefs = new SharedPrefs();
	
	private TextView appTitle;
	private View signinButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.signin_view);
		
		/* Initialize Views */
		signinButton = findViewById(R.id.signInButtonGoogle);
		signinButton.setVisibility(View.INVISIBLE);
		signinButton.setOnClickListener(this);
		Log.d(LogStrings.ViewNames.SIGNIN_VIEW, "SigninView page load");
		
		appTitle = (TextView) findViewById(R.id.titleTextView);
		mFonts.typeFaceConstructor(appTitle, Amatic.BOLD, getAssets());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.signInButtonGoogle && !mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
				signinButton.setVisibility(View.VISIBLE);
			} else {
				try {
					mConnectionResult.startResolutionForResult(this, GPlusConstants.REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// Try connecting again
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		super.onConnectionFailed(result);
		signinButton.setVisibility(View.VISIBLE);
		// The user clicked the sign-in button already
		if (mConnectionProgressDialog.isShowing()) {
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this, GPlusConstants.REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}
		mConnectionResult = result;
	}
	
	@Override
	public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
		super.onPeopleLoaded(status, personBuffer, nextPageToken);
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			/* Saving user to Shared Preferences */
			Log.d(ViewNames.SIGNIN_VIEW, "UserPlusID = " + userPlusID);
			if (sharedPrefs.saveUserToSharedPrefs(userPlusID, contextActivity)) {
				Log.d(ViewNames.SIGNIN_VIEW, "UserID saved to sharedPrefs");
			} else {
				Log.e(ViewNames.SIGNIN_VIEW, "UserID was not saved");
			}
			
			// Goto MainView
			gotoView(new Intent(SignInView.this, MainView.class), AnimationConstants.Transitions.FROM_RIGHT);
			finish();
			
		} else if (status.getErrorCode() == ConnectionResult.NETWORK_ERROR) {
			Log.e(ViewNames.SIGNIN_VIEW, getResources().getString(R.string.networkerror));
			Toast.makeText(this, R.string.networkerror, Toast.LENGTH_SHORT).show();
		}
	}
	
}