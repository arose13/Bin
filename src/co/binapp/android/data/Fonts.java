package co.binapp.android.data;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {
	
	//private static final int MAX_STRING_LENGHT_FONTSIZE = 40;
	private static final String FRONT = "fonts/";
	private static final String BACK = ".ttf";
	
	public static final String APPNAME_FONT = FRONT + "" + BACK;
	
	public static final class Roboto {
		private static final String ROBOTOGROUP = FRONT + "Roboto-";
		
		public static final String THIN = ROBOTOGROUP + "Thin" + BACK;
		public static final String REGULAR = ROBOTOGROUP + "Regular" + BACK;
		public static final String LIGHT = ROBOTOGROUP + "Light" + BACK;
		public static final String LIGHT_CONDENSED = ROBOTOGROUP + "LightCondensed" + BACK;		
	}
	
	/* TypeFace Constructor */
	public void typeFaceConstructor(TextView textview, String fontPath, AssetManager assets) {
		Typeface customtf = Typeface.createFromAsset(assets, fontPath);
		textview.setTypeface(customtf);
	}
}
