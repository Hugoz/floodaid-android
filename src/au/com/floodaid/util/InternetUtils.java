package au.com.floodaid.util;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Class with utility methods used for connected apps 
 * 
 * @author hsterin
 */
public class InternetUtils {

	// Logger constant
	private static final String TAG = "InternetUtils";

	/**
	 * Load a remote image
	 * This is not creating a new thread and therefore it will freeze the main thread until image has been retrieved.
	 *  
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		try {
			Log.d(TAG, "Loading image from " + url);
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			Drawable d = Drawable.createFromStream(is, urlObj.getFile());
			Log.d(TAG, "Loaded image " + urlObj.getFile()); 
			return d;
			
		} catch (Exception e) {
			Log.e(TAG, "Unable to load image from URL " + url, e);
			return null;
		}
	}
}
