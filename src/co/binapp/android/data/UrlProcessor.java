package co.binapp.android.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.util.Log;

public class UrlProcessor {
	
	public static final String TAG = UrlProcessor.class.getSimpleName();
	
	private StringProcessor mStringProcessor = new StringProcessor();
	
	private String[] imageExts = new String[] {".jpg", ".jpeg", ".png", ".bmp"}; 
	
	/* This hunts and finds the URLs from within a string */
	public String extractUrlFromString(String inputString) {
		String[] parts = inputString.split("\\s+");
		String discoveredURL = "";
		
		// Try to convert every word into an URL, if it fails, it's not a URL
		for (String item : parts) {
			try {
				URL url = new URL(item);
				discoveredURL = url.toString();
				Log.i(TAG, "Found in String: " + discoveredURL);
			} catch (MalformedURLException e) {
				// item ! URL
			}
		} 
		return discoveredURL;
	}
	
	/* This runs several checks to get an imageURL if possible */
	public String getImageUrlFromUrl(String url) {
		if (isAnImageURL(url)) {
			// The URL itself is a ImageURL
			return url;
		} else if (is9gagURL(url)) {
			// 9gag URL
			return get9GagImageUrl(url);
		} else if (isTwitterURL(url)) {
			// Twitter URL
			return getTwitterImageURL(url);
		} else if (isInstagramURL(url)) {
			// InstaGram URL
			return getInstagramURL(url);
		} else if (isYoutubeVideo(url)) {
			// YouTube Video URL
			return getYoutubeImageURL(url);
		} else if (isImgurURL(url)) {
			// Imgur URL
			return getImgurImageURL(url);
		} else {
			// General web site Image check
			return getGeneralImageURL(url);
		}
	}

	private String getGeneralImageURL(String url) {
		// Try to get the web page and try to find a og:image tag
		Document doc = getWebPage(url);
		String imgUrl = "";
		if (doc != null) {
			Element metaTag = doc.select("meta[property=og:image]").first();
			if (metaTag != null) {
				imgUrl = metaTag.attr("content");
				imgUrl = imgUrl.split("\\?")[0];
				Log.d(TAG, "getGeneralImageURL  returned: " + imgUrl);
			}
		}
		return imgUrl;
	}
	
	private String getInstagramURL(String url) {
		return getGeneralImageURL(url);
	}
	
	// TODO Test this
	private String getImgurImageURL(String url) {
		Document doc = getWebPageAsIfBrowser(url);
		String imgUrl = "";
		if (doc != null) {
			Element linkTag = doc.select("link[rel=image_src]").first();
			if (linkTag != null) {
				imgUrl = linkTag.attr("href");
			}
		}
		return imgUrl;
	}	
	
	private String get9GagImageUrl(String url) {
		// Try to get 9gag web page
		Document doc = getWebPage(url);
		String imgUrl = "";
		if (doc != null) {
			Element link = doc.select("link[rel=image_src]").first();
			if (link != null) {
				imgUrl = link.attr("href");
			}
			Log.d(TAG, "get9GagImageUrl returned: " + imgUrl);
		}
		
		if (isAnImageURL(imgUrl)) {
			return imgUrl;
		} else {
			return "";
		}
	}

	private String getTwitterImageURL(String url) {
		// Try to get Twitter web page
		if (!url.contains("mobile.")) {
			url = url.replace("twitter.com", "mobile.twitter.com");
		}
		
		if (url.contains(StringProcessor.HTTP)) {
			url = url.replace(StringProcessor.HTTP, StringProcessor.HTTPS);
		}

		Log.d(TAG, "input URL: " + url);
		Document doc = getWebPageAsIfBrowser(url);
		
		String imgUrl = "";
		if (doc != null) {
			Log.d(TAG, doc.body().html());
			Element imgTag = doc.select("div.media > img").first();
			Log.d(TAG, imgTag.html());
			imgUrl = imgTag.attr("src");
			Log.d(TAG, "getTwitterImageURL returned: " + imgUrl);
		}
		
		if (isAnImageURL(imgUrl)) {
			return imgUrl;
		} else {
			return "";
		}
	}

	private String getYoutubeImageURL(String url) {
		String ytcom = "youtube.com";
		String ytsht = "youtu.be/";
		String wv = "watch?v=";
		String ytcomwv = ytcom + "/" + wv;
		
		String urlPrefix = "http://i4.ytimg.com/vi/";
		String urlSuffix = "/mqdefault.jpg";
		String imageURL = "mqdefault.jpg";
		
		url = mStringProcessor.removeHTTPs(url);
		
		if (url.contains(ytcom) && url.contains(wv)) {
			return urlPrefix + url.replace(ytcomwv, "") + urlSuffix;
			
		} else if (url.contains(ytsht)) {
			return urlPrefix + url.replace(ytsht, "") + urlSuffix;
			
		} else {
			return imageURL;
		}
	}

	@SuppressLint("DefaultLocale")
	public boolean isAnImageURL(String url) {
		String urlPPd = url.toLowerCase();
		if (urlPPd.contains(imageExts[0]) || urlPPd.contains(imageExts[1]) || urlPPd.contains(imageExts[2]) || urlPPd.contains(imageExts[3])) {
			return true;
		} else {
			return false;
		}
	}
	
	/* URL Checks below */
	public boolean isYoutubeVideo(String url) {
		if ((url.contains("youtube.com") && url.contains("watch?v=")) || (url.contains("youtu.be/"))) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isTwitterURL(String url) {
		if (url.contains("twitter.com") && url.contains("status")) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isInstagramURL(String url) {
		if (url.contains("instagram.com/p/")) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isImgurURL(String url) {
		if (url.contains("imgur.com/gallery/")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is9gagURL(String url) {
		if (url.contains("9gag.com/gag/")) {
			return true;
		} else {
			return false;
		}
	}
	/* End of URL checks */
	
	private Document getWebPage(String url) {
		Document doc = null;		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private Document getWebPageAsIfBrowser(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
}
