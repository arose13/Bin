package co.binapp.android.views;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import co.binapp.android.R;
import co.binapp.android.activities.DSConnectedActivity;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudQuery.Order;
import co.binapp.android.backend.core.CloudQuery.Scope;
import co.binapp.android.backend.core.CloudCallbackHandler;
import co.binapp.android.backend.core.CloudEntity;
import co.binapp.android.data.DataStoreConstants.Bins;

public class MainView extends DSConnectedActivity implements OnListener {
	
	public static final String TAG = MainView.class.getSimpleName();
	
	private List<CloudEntity> binList = new LinkedList<CloudEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
	}
	
	private void getPosts() {
		// This handler can either receive a result or an error
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {

			@Override
			public void onComplete(List<CloudEntity> results) {
				Log.d(TAG, "CloudCallbackHandler succeeded");
				binList = results;
				for (CloudEntity ce : binList) {
					Log.i(TAG, ce.get(Bins.Keys.CONTENT).toString()); // TODO that's how information is received from the CE
				}
			}
			
			@Override
			public void onError(IOException exception) {
				super.onError(exception);
				Log.e(TAG, "error on getting result from DataStore");
			}
			
		};
		
		// Execute the query with the handler
		mProcessingFragment.getCloudBackend().listByKind(
				Bins.CLOUD_ENTITY, //KindName 
				CloudEntity.PROP_CREATED_AT, // Sort property name
				Order.DESC, // Order
				10, // Limit
				Scope.FUTURE_AND_PAST, // Scope
				handler // Handler
				);
	}
	
	@Override
	public void onCreateFinished() {
		//TODO: List Posts
		getPosts();
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		//TODO: I have no idea what this does
	}
	
}
