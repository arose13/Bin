package co.binapp.android.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.backend.core.CloudBackendFragment;

public class MainView extends ViewActivity {
	
	private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
	
	private FragmentManager mFragmentManager;
	private CloudBackendFragment backendFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		initFrags();
	}

	private void initFrags() {
		mFragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		
		backendFragment = (CloudBackendFragment) mFragmentManager.findFragmentByTag(PROCESSING_FRAGMENT_TAG);
		
		if (backendFragment == null) {
			backendFragment = new CloudBackendFragment();
			backendFragment.setRetainInstance(true);
			fragmentTransaction.add(backendFragment, PROCESSING_FRAGMENT_TAG);
		}
		
		fragmentTransaction.commit();
	}
	
}
