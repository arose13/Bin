package co.binapp.android.data;

public class StringProcessor {
	
	public boolean containsLinkCheck(String string) {
		if (string.contains("http://") || string.contains("https://") || string.contains("www.")) {
			return true;
		} else {
			return false;
		}
	}
	
}
