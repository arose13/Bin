package co.binapp.android.data;

public class DataStoreConstants {
	
	public static class Bins {
		public static final String CLOUD_ENTITY = "Bins";
		
		public static class Keys {
			public static final String TYPE = "type";         // The type of Entry, it also checks if it is porn [int]
			public static final String URL = "url";           // URL in content if it exists
			public static final String CONTENT = "content";   // Content
			public static final String TITLE = "title";       // Title
			public static final String IMAGE_URL = "imgurl";  // Image asset URL
			public static final String TAGS = "tags";         // Tags 
			public static final String USERID = "userid";     // User that created the image
			public static final String FAVORITE = "favorite"; // 0 = not favorite 1 = favorite [int]
			public static final String COLOR = "color";       // The hex color for the card
		}
		
		public static class TypeValues {
			public static final int TEXT = 1;
			public static final int QUOTE = 2;
			public static final int LINK = 3;
			public static final int PHOTO = 4;
			public static final int VIDEO = 5;
			public static final int PRIVATE = 9;
		}
		
		public static class FavoriteValues {
			public static final int DEFAULT = 0;
			public static final int FAVORITE = 1;
		}
	}
	
}
