package co.binapp.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;
import co.binapp.android.data.Fonts.Roboto;

public class EntryView extends ViewActivity {
	
	private EditText inputText;
	
	private Intent externalIntent;
	private String externalIntentAction;
	private String externalIntentType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_view);
		
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
	}
	
	private void checkPreferences() {
		
	}
	
}
