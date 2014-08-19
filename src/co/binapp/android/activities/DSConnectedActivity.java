package co.binapp.android.activities;

import co.binapp.android.backend.core.CloudBackendFragment;
import co.binapp.android.data.AnimationConstants;
import co.binapp.android.views.SignInView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public abstract class DSConnectedActivity extends ViewActivity {
	
	public static final String TAG = DSConnectedActivity.class.getSimpleName();
	private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
	
	protected FragmentManager mFragmentManager;
	protected CloudBackendFragment mProcessingFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFragmentManager = getFragmentManager();
		
		initiateFragments();
	}
	
    private void initiateFragments() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // Check to see if we have retained the fragment which handles
        // asynchronous BackEnd calls
        mProcessingFragment = (CloudBackendFragment) mFragmentManager.findFragmentByTag(PROCESSING_FRAGMENT_TAG);
        // If not retained (or first time running), create a new one
        if (mProcessingFragment == null) {
            mProcessingFragment = new CloudBackendFragment();
            mProcessingFragment.setRetainInstance(true);
            fragmentTransaction.add(mProcessingFragment, PROCESSING_FRAGMENT_TAG);
        }
        
        fragmentTransaction.commit();

    }

	protected void checkPreferences() {
		if (sharedPrefs.readFromSavedUser().equals("")) {
			// TODO This user never logged in
			gotoView(new Intent(this, SignInView.class), AnimationConstants.Transitions.FROM_BOTTOM);
		}
		Log.d(TAG, "checkPreferences ran");
	}
}
