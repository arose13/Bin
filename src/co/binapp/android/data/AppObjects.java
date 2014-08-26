package co.binapp.android.data;

import android.widget.ImageView;
import android.widget.TextView;

public class AppObjects {
	
	/* Contains the List Objects */
	public static class BinObject {
		public final int type;
		public final String title;
		public final String content;
		public final String imgurl;
		public final String url;
		public final String tags;
		
		public BinObject(int type, String title, String content, String imgurl, String url, String tags) {
			this.type = type;
			this.title = title;
			this.content = content;
			this.imgurl = imgurl;
			this.url = url;
			this.tags = tags;
		}
	}

	/* UI req'd List Objects */
	public static class BinListViewHolder {
		public TextView title;
		public TextView content;
		public ImageView image;
	}
}
