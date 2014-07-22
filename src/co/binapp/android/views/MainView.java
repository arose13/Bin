package co.binapp.android.views;

import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import co.binapp.android.R;
import co.binapp.android.activities.DSConnectedActivity;
import co.binapp.android.backend.core.CloudBackendFragment;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudEntity;

public class MainView extends DSConnectedActivity implements OnListener {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
	}

	@Override
	public void onCreateFinished() {
		//TODO: List Posts
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		//TODO: I have no idea what this does
	}
	
}
