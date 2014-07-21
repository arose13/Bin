package co.binapp.android.views;

import java.io.IOException;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.backend.core.CloudBackendFragment;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudCallbackHandler;
import co.binapp.android.backend.core.CloudEntity;
import co.binapp.android.data.DataStoreConstants.Bins;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;
import co.binapp.android.data.Fonts.Roboto;
import co.binapp.android.data.DataStoreConstants;
import co.binapp.android.data.StringProcessor;

public class EntryView extends ViewActivity implements OnListener {
	
	public static final String TAG = EntryView.class.getName();
	private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
	
	private boolean privateEntry = false;
	private int entryType = TypeValues.TEXT;
	
	private Menu entryViewMenu;
	private EditText inputTitle;
	private EditText inputBody;
	
	private Intent externalIntent;
	private String externalIntentAction;
	private String externalIntentType;
	
	private String title = "";
	private String content = "";
	private String url = "";
	private String imageUrl = "";
	private String tags = "";
	private String hexColor = "";
	
	private StringProcessor mStringProcessor;
	private FragmentManager mFragmentManager;
	private CloudBackendFragment mProcessingFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_view);
		
		mStringProcessor = new StringProcessor();
		mFragmentManager = getFragmentManager();
		
		initViews();
		checkPreferences();
		initiateFragments();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		handleExternalIntent(); /* Deal with Incoming Intent */
	}
	
	/* This handles the shared intents */
	private void handleExternalIntent() {
		externalIntent = getIntent();
		externalIntentAction = externalIntent.getAction();
		externalIntentType = externalIntent.getType();
		
		if (Intent.ACTION_SEND.equals(externalIntentAction) && 
				externalIntentType != null && 
				externalIntentType.equals(getResources().getString(R.string.mimetypeText))
				) {
			String sharedText = externalIntent.getStringExtra(Intent.EXTRA_TEXT);
			String sharedSubject = externalIntent.getStringExtra(Intent.EXTRA_SUBJECT);
			if ((sharedText != null) && (sharedSubject != null)) {
				inputTitle.setText(sharedSubject);
				inputBody.setText(sharedText);
			} else if (sharedText != null) {
				inputBody.setText(sharedText);
			}
		}
	}

	private void initViews() {
		inputTitle = (EditText) findViewById(R.id.entryviewEditTextTitle);
		inputBody = (EditText) findViewById(R.id.entryviewEditTextBody);
		mFonts.typeFaceConstructor(inputTitle, Roboto.BOLD, getAssets());
		mFonts.typeFaceConstructor(inputBody, Roboto.LIGHT, getAssets());
	}
	
	private void checkPreferences() {
		
	}
	
	/* This modifies the actionBar button */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		entryViewMenu = menu;
		inflateActionBar(R.menu.entryview_menu, entryViewMenu);
		return super.onCreateOptionsMenu(menu);
	}

	/* This is an OnClick listener for the actionBar */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.entryviewDoneBtn:
			if (processEntry()) {
				finish();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void getStringsFromEditTextViews() {
		title = inputTitle.getText().toString();
		content = inputBody.getText().toString();
	}

	private boolean processEntry() {
		
		getStringsFromEditTextViews();
		
		if (mStringProcessor.containsLinkCheck(content)) {
			/* URL */
			entryType = TypeValues.LINK;
			Log.d(TAG, "addEntry 1st if is TRUE");
		} else {
			/* Ordinary Entry */
			entryType = TypeValues.TEXT;
			Log.d(TAG, "addEntry 1st if is FALSE");
		}
		
		if (privateEntry) {
			// It is private therefore change the type and save
			entryType = TypeValues.PRIVATE;
		} else {
			// It is not private therefore don't modify the entryType
		}
		
		Log.i(TAG, "[" + entryType + " , " + title + " , " + content + " ]");
		saveEntry(entryType, title, content, url, imageUrl, tags, hexColor);
		
		return true ; /* TODO when the save to DS is complete this must return true! */
	}

	private void saveEntry(int type, String title, String content, String url, String imageUrl, String tags, String hexColor) {
				
		CloudEntity newEntry = new CloudEntity(DataStoreConstants.Bins.CLOUD_ENTITY);
		newEntry.put(Bins.Keys.TYPE, type);
		newEntry.put(Bins.Keys.TITLE, title);
		newEntry.put(Bins.Keys.CONTENT, content);
		newEntry.put(Bins.Keys.URL, url);
		newEntry.put(Bins.Keys.IMAGE_URL, imageUrl);
		newEntry.put(Bins.Keys.TAGS, tags);
		newEntry.put(Bins.Keys.COLOR, hexColor);
		newEntry.put(Bins.Keys.USERID, "01234567"); /* TODO fill in userID */
		
		CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			
			@Override
			public void onComplete(CloudEntity results) {
				Log.d(TAG, "Posted to DS");
			}
			
			@Override
			public void onError(IOException exception) {
				super.onError(exception);
				Log.d(TAG, "Error in DS posting");
			}
		};
		
		// Execute newEntry insertion
		mProcessingFragment.getCloudBackend().insert(newEntry, handler);
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

	@Override
	public void onCreateFinished() {
		// TODO from implements OnListener		
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		// TODO from implements OnListener		
	}

	
}
