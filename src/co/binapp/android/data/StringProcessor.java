package co.binapp.android.data;

public class StringProcessor {
	
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String WWW = "www.";
	
	public boolean containsLinkCheck(String string) {
		if (string.contains(HTTP) || string.contains(HTTPS) || string.contains(WWW)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String removeHTTPs(String url) {
		url = url.replace(HTTP, "");
		url = url.replace(HTTPS, "");
		url = url.replace(WWW, "");
		return url;
	}
	
	public static boolean hasContents(String input) {
		if (!input.equals("") && input != null) {
			return true; // Has contents
		} else {
			return false;
		}
	}
	
}
