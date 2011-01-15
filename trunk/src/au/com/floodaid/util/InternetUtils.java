package au.com.floodaid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONObject;

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
	
	/**
	 * Execute API call
	 * This is not creating a new thread and therefore it will freeze the main thread until JSONobject has been retrieved.
	 *  
	 * @param url
	 * @return JSONObject
	 */
	public static JSONObject executeApiCall(String url) {
		try {
			Log.d(TAG, "Execute API call: " + url);
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			JSONObject json=new JSONObject(result);
			Log.d(TAG, "Executed API call " + urlObj.getFile()); 
			return json;
			
		} catch (Exception e) {
			Log.e(TAG, "Unable to execute API call " + url, e);
			return null;
		}
	}
	
	/**
    *
    * @param is
    * @return String
    */
   public static String convertStreamToString(InputStream is) {
       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       StringBuilder sb = new StringBuilder();

       String line = null;
       try {
           while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               is.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return sb.toString();
   }


}
