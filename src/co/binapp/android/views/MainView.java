package co.binapp.android.views;

import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.backend.core.CloudBackendFragment;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudEntity;

public class MainView extends ViewActivity implements OnListener {
	
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

	@Override
	public void onCreateFinished() {
		//TODO: List Posts
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		//TODO: I have no idea what this does
	}
	
//	@Override
//    public void onCreateFinished() {
//        listPosts();
//    }
//
//    /**
//     * Method called via OnListener in {@link CloudBackendFragment}.
//     */
//    @Override
//    public void onBroadcastMessageReceived(List<CloudEntity> l) {
//        for (CloudEntity e : l) {
//            String message = (String) e.get(BROADCAST_PROP_MESSAGE);
//            int duration = Integer.parseInt((String) e.get(BROADCAST_PROP_DURATION));
//            Toast.makeText(this, message, duration).show();
//            Log.i(Consts.TAG, "A message was recieved with content: " + message);
//        }
//    }
	
}
