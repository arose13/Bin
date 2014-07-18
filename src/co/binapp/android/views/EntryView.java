package co.binapp.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;
import co.binapp.android.data.Fonts.Roboto;
import co.binapp.android.data.StringProcessor;

public class EntryView extends ViewActivity {
	
	public static final String TAG = EntryView.class.getName();
	
	private int entryType = TypeValues.TEXT;
	
	private Menu entryViewMenu;
	private EditText inputText;	
	
	private Intent externalIntent;
	private String externalIntentAction;
	private String externalIntentType;
	
	private StringProcessor mStringProcessor;
	
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
				inputText.setText(sharedSubject + " " + sharedText);
			} else if (sharedText != null) {
				inputText.setText(sharedText);
			}
		}
	}

	private void initViews() {
		inputText = (EditText) findViewById(R.id.entryviewEditText);
		mFonts.typeFaceConstructor(inputText, Roboto.LIGHT, getAssets());
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
			if (addEntry()) {
				finish();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean addEntry() {
		if (mStringProcessor.containsLinkCheck(inputText.getText().toString())) {
			/* Link Content */
			entryType = TypeValues.LINK;
			Log.d(TAG, "addEntry 1st if is TRUE");
		} else {
			/* Ordinary Entry */
			entryType = TypeValues.TEXT;
			Log.d(TAG, "addEntry 1st if is FALSE");
		}
		return false;
	}
	
}
