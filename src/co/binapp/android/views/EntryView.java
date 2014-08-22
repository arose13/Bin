package co.binapp.android.views;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import co.binapp.android.R;
import co.binapp.android.activities.DSConnectedActivity;
import co.binapp.android.backend.core.CloudBackendFragment.OnListener;
import co.binapp.android.backend.core.CloudCallbackHandler;
import co.binapp.android.backend.core.CloudEntity;
import co.binapp.android.data.DataStoreConstants.Bins;
import co.binapp.android.data.DataStoreConstants.Bins.FavoriteValues;
import co.binapp.android.data.DataStoreConstants.Bins.PrivateValues;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;
import co.binapp.android.data.Fonts.Roboto;
import co.binapp.android.data.DataStoreConstants;
import co.binapp.android.data.StringProcessor;
import co.binapp.android.data.UrlProcessor;

public class EntryView extends DSConnectedActivity implements OnListener {
	
	public static final String TAG = EntryView.class.getSimpleName();
	
	private boolean privateEntry = false;
	private boolean favoriteEntry = false;
	private int entryType = TypeValues.TEXT;
	
	private Menu entryViewMenu;
	private EditText inputTitle;
	private EditText inputBody;
	private EditText inputQuote;
	
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
	private UrlProcessor mUrlProcessor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_view);
		
		mStringProcessor = new StringProcessor();
		
		initViews();
		checkPreferences();
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
		inputQuote = (EditText) findViewById(R.id.entryviewEditTextQuote);
		mFonts.typeFaceConstructor(inputTitle, Roboto.BOLD, getAssets());
		mFonts.typeFaceConstructor(inputBody, Roboto.LIGHT, getAssets());
		mFonts.typeFaceConstructor(inputQuote, Roboto.Slab.REGULAR, getAssets());
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
			if (isEditTextViewsNotEmpty()) {
				getStringsFromEditTextViews();
				EntryView.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new ProcessEntryTaskASync().execute();
					}
				});
			}
			return true;
			
		case R.id.entryviewFavorite:
			if (favoriteEntry == false) {
				favoriteEntry = true;
				// TODO change button to clicked
			} else {
				favoriteEntry = false;
				// TODO change button to unClicked
			}
			return true;
			
		case R.id.entryviewQuote:
			if (entryType != TypeValues.QUOTE) {
				entryType = TypeValues.QUOTE;
				// TODO Modify button state
				// TODO Show quote editText
			} else {
				entryType = TypeValues.TEXT;
				// TODO modify button state
				// TODO Hide and clear quote Edittext
			}
			return true;
			
		case R.id.entryviewPrivate:
			if (privateEntry == false) {
				privateEntry = true;
				// TODO modify button state active
			} else {
				// TODO modify button state back to clear
				privateEntry = false;
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private boolean isEditTextViewsNotEmpty() {
		if ((inputBody.getText().toString().trim() != "") && (inputBody.getText().toString().trim() != null) && (!inputBody.getText().toString().isEmpty())) {
			return true;
		} else {
			return false;
		}
	}
	
	private void getStringsFromEditTextViews() {
		title = inputTitle.getText().toString();
		content = inputBody.getText().toString();
	}
	
	private class ProcessEntryTaskASync extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			processEntry();
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			saveEntry(entryType, title, content, url, imageUrl, tags, hexColor);
			finish(); // Kills the activity
		}
		
	}

	private boolean processEntry() {
		
		if (mStringProcessor.containsLinkCheck(content)) {
			/* URL */
			mUrlProcessor = new UrlProcessor();
			entryType = TypeValues.LINK;
			url = mUrlProcessor.extractUrlFromString(content);
			imageUrl = mUrlProcessor.getImageUrlFromUrl(url);
			
			/* Makes it either a PHOTO or VIDEO depending on URL */
			if (mUrlProcessor.isAnImageURL(url)) {
				entryType = TypeValues.PHOTO;
			} else if (mUrlProcessor.isYoutubeVideo(url)) {
				entryType = TypeValues.VIDEO;
			}

		} else {
			/* Ordinary Entry or Quote */
			entryType = TypeValues.TEXT;
			if (!content.isEmpty() && title.isEmpty()) {
				entryType = TypeValues.QUOTE;
			}
		}
		
		return true;
	}

	private void saveEntry(int type, String title, String content, String url, String imageUrl, String tags, String hexColor) {
				
		CloudEntity newEntry = new CloudEntity(DataStoreConstants.Bins.CLOUD_ENTITY);
		newEntry.put(Bins.Keys.TYPE, type);
		newEntry.put(Bins.Keys.TITLE, title);
		newEntry.put(Bins.Keys.CONTENT, content);
		newEntry.put(Bins.Keys.URL, url);
		newEntry.put(Bins.Keys.IMAGE_URL, imageUrl);
		newEntry.put(Bins.Keys.TAGS, tags);
		newEntry.put(Bins.Keys.USERID, sharedPrefs.readFromSavedUser());
		newEntry.put(Bins.Keys.FAVORITE, isFavoriteEntry());
		newEntry.put(Bins.Keys.PRIVATE, isPrivateEntry());
		
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
	
	private int isPrivateEntry() {
		/* This is called when the entry is being saved */
		if (privateEntry) {
			return PrivateValues.PRIVATE;
		} else {
			return PrivateValues.DEFAULT;
		}
	}
	
	private int isFavoriteEntry() {
		/* This is called when the entry is being saved */
		if (favoriteEntry) {
			return FavoriteValues.FAVORITE;
		} else {
			return FavoriteValues.DEFAULT;
		}
	}
	
	@Override
	public void onCreateFinished() {		
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
	}

	
}
