package co.binapp.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.data.DataStoreConstants.Bins.TypeValues;
import co.binapp.android.data.Fonts.Roboto;
import co.binapp.android.data.StringProcessor;

public class EntryView extends ViewActivity {
	
	private int entryType = TypeValues.TEXT;	
	private boolean entryViewMenuVisable = false;
	
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
		inputText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if ((count != 0) && (entryViewMenuVisable == false)) {
					inflateActionBar(R.menu.entryview_menu, entryViewMenu);
					entryViewMenuVisable = true;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	
	private void checkPreferences() {
		
	}
	
	/* This modifies the actionBar button */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		entryViewMenu = menu;
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
			entryType = TypeValues.LINK;
		}
		return false;
	}
	
}
