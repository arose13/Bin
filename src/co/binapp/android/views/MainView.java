package co.binapp.android.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import co.binapp.android.R;
import co.binapp.android.activities.DSConnectedActivity;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudQuery.Order;
import co.binapp.android.backend.core.CloudQuery.Scope;
import co.binapp.android.backend.core.CloudCallbackHandler;
import co.binapp.android.backend.core.CloudEntity;
import co.binapp.android.data.ListAdapter;
import co.binapp.android.data.AnimationConstants.Transitions;
import co.binapp.android.data.AppObjects.BinObject;
import co.binapp.android.data.DataStoreConstants.Bins;
import co.binapp.android.data.DataStoreConstants.Bins.Keys;

public class MainView extends DSConnectedActivity implements OnListener, OnClickListener {
	
	public static final String TAG = MainView.class.getSimpleName();
	
	/* UI Elements */
	private ImageView addButton;
	private ListView binListView;
	
	/* Data Elements */
	private List<CloudEntity> binList = new ArrayList<CloudEntity>();
	private ArrayList<BinObject> binObjectArrayList = new ArrayList<BinObject>();
	private ListAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		
		initViews();
	}
	
	private void initViews() {
		addButton = (ImageView) findViewById(R.id.addBtn);
		addButton.setOnClickListener(this);
		
		binListView = (ListView) findViewById(R.id.binListView);
		listAdapter = new ListAdapter(this, getAssets(), getWindowManager().getDefaultDisplay());
		binListView.setAdapter(listAdapter);
		binListView.setDividerHeight(0);
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
					binObjectArrayList.add(new BinObject(
							Integer.parseInt(ce.get(Keys.TYPE).toString()), // Type 
							ce.get(Keys.TITLE).toString(), // Title
							ce.get(Keys.CONTENT).toString(), // Content
							ce.get(Keys.IMAGE_URL).toString(), // ImgUrl
							ce.get(Keys.URL).toString(), // Url
							ce.get(Keys.TAGS).toString() // Tags
					));
				}
				if (binObjectArrayList.size() > 0) {
					listAdapter.updateListInAdapter(binObjectArrayList);
					binObjectArrayList = null;
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

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addBtn:
			Intent toEntryViewIntent = new Intent(MainView.this, EntryView.class);
			gotoView(toEntryViewIntent, Transitions.FROM_RIGHT);
			break;

		default:
			break;
		}
	}
	
}
