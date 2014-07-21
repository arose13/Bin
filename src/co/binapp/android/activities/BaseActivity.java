package co.binapp.android.activities;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import co.binapp.android.R;
import co.binapp.android.data.Fonts;
import co.binapp.android.data.GPlusConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

public abstract class BaseActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, OnPeopleLoadedListener {
	
	public static final String TAG = BaseActivity.class.getName();
	
	protected Fonts mFonts = new Fonts();
	
	protected static boolean wifiFlag = false;
	protected static boolean mobileFlag = false;
	protected boolean mSignInClicked;
	
	protected Context contextActivity;
	protected PlusClient mPlusClient;
	protected Person mPerson;
	protected ProgressDialog mConnectionProgressDialog;
	protected ConnectionResult mConnectionResult;
	
	protected String userPlusID;
	protected String userFullName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Starting all app's context */
		contextActivity = getApplicationContext();
		
		/* Google Plus Setup */
		mPlusClient = new PlusClient.Builder(this, this, this)
			.setScopes(GPlusConstants.SCOPES)
			.setActions(GPlusConstants.VISABLE_ACTIVITIES)
			.build();
		// Progress bar to be displayed if the connection failure is not resolved
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage(getString(R.string.signingin));;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
		Log.d(TAG, "Plus Client connected");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// the user clicked the sign-in button already
		if (mConnectionProgressDialog.isShowing()) {
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this, GPlusConstants.REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}
		// Save the intent so that we can start an activity when the user clicks the signin button
		mConnectionResult = result;
	}
	
	@Override
	public void onPeopleLoaded(ConnectionResult status,	PersonBuffer personBuffer, String nextPageToken) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			//Get Person
			mPerson = personBuffer.get(0);
			//User's name and userId
			userFullName = mPerson.getName().getGivenName() + " " + mPerson.getName().getFamilyName();
			userPlusID = mPerson.getId();
			Log.i(TAG, userFullName);
			Log.i(TAG, userPlusID);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == GPlusConstants.REQUEST_CODE_RESOLVE_ERR) && (resultCode == RESULT_OK)) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		mPlusClient.loadPeople(this, "me");
		if (mConnectionProgressDialog.isShowing()) {
			mConnectionProgressDialog.dismiss();
		}
		Log.i(TAG, "User is connected");
	}
	
	@Override
	public void onDisconnected() {
		Log.i(TAG, "gplus disconnected");
	}
	
	/* Custom Methods Below */
		
}