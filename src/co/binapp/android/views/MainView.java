package co.binapp.android.views;

import android.os.Bundle;

import co.binapp.android.R;
import co.binapp.android.activities.ViewActivity;

public class MainView extends ViewActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
	}
	
}
