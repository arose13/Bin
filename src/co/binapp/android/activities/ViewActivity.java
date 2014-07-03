package co.binapp.android.activities;

import android.content.Intent;

import co.binapp.android.R;
import co.binapp.android.data.AnimationConstants;

public abstract class ViewActivity extends BaseActivity {
	
	protected void gotoView(Intent toThisView, int transitionAnimation) {
		startActivity(toThisView);
		
		switch (transitionAnimation) {
		case AnimationConstants.Transitions.FROM_RIGHT:
			overridePendingTransition(R.anim.activity_fromright, R.anim.activity_nomotion);
			break;

		default:
			break;
		}
	} 
	
}
