package co.binapp.android.activities;

import android.content.Intent;
import android.view.Menu;

import co.binapp.android.R;
import co.binapp.android.data.AnimationConstants;

public abstract class ViewActivity extends BaseActivity {
	
	public static final String TAG = ViewActivity.class.getName();
	
	protected void gotoView(Intent toThisView, int transitionAnimation) {
		startActivity(toThisView);
		
		switch (transitionAnimation) {
		case AnimationConstants.Transitions.FROM_RIGHT:
			overridePendingTransition(R.anim.activity_fromright, R.anim.activity_nomotion);
			break;
			
		case AnimationConstants.Transitions.FROM_BOTTOM:
			overridePendingTransition(R.anim.activity_frombottom, R.anim.activity_nomotion);

		default:
			break;
		}
	}
	
	protected void inflateActionBar(int resID, Menu menu) {
		getMenuInflater().inflate(resID, menu);
	}
	
}
