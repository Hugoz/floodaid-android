package au.com.floodaid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Class with utility methods used for connected apps 
 * 
 * @author hsterin
 */
public class ApiUtils {

	// Logger constant
	private static final String TAG = "InternetUtils";

	/**
	 * Execute API call
	 * This is not creating a new thread and therefore it will freeze the main thread until JSONobject has been retrieved.
	 *  
	 * @param url
	 * @return JSONObject
	 */
	public static JSONObject executeApiCall(String url) {
		try {
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			JSONObject json=new JSONObject(result); 
			return json;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Get Terms of use via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 *  
	 * @param url
	 * @return String
	 */
	public static String getTOC(String url) {
		try {
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			// TODO: result needs unescaping
			return result;
			
		} catch (Exception e) {
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
