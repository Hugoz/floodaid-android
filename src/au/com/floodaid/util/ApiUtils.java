package au.com.floodaid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.maps.PlaceOverlayItem;
import au.com.floodaid.provider.Place;



/**
 * Class with utility methods used for connected apps 
 * 
 * @author hsterin
 */
public class ApiUtils {

	// Logger constant
	private static final String TAG = "InternetUtils";
	
	// API constants
	private static final String URL = "http://dev.floodaid.com.au/api/";
	private static final String APIKEY = "api_key=abcdefg12345";
	
	// private vars for caching
	private static String _about = "";
	private static String _tou = "";
	private static String _contacts = "";
	private static String _userKey = "";
	private static String _lastError = "";
	private static List<Place> placesList = new ArrayList<Place>();
	private static HashMap<String, String> _categories = new HashMap<String,String>();
	public static boolean listLoaded = true;
	public static SharedPreferences prefs;
	///////////////////
	
	/**
	 * Get Terms of use via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @return A String with the Terms of Use. 
	 */
	public static String getTOU() 
	{
		if (_tou.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"tou?"+APIKEY);
			try {
				_tou = jsonTmp.getString("text");
				_tou = _tou.replace("&nbsp;", "\n")+"\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return _tou;
	}
	
	/**
	 * Get About information via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @return A String with the About information. 
	 */
	public static String getAbout() 
	{
		if (_about.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"about?"+APIKEY);
			try {
				_about = jsonTmp.getString("text");
				_about = _about.replace("&nbsp;", "\n")+"\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return _about;
	}
	
	/**
	 * Get important Contact information of use via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @return A String with important contacts 
	 */
	public static String getContacts() 
	{
		if (_contacts.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"aid?"+APIKEY);
			try {
				_contacts = jsonTmp.getString("text");
				_contacts = _contacts.replace("&nbsp;", "\n")+"\n";
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return _contacts;
	}
	
	/**
	 * Get available categories via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @return A Hashmap with the available categories as number/name pair. 
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getCategories() 
	{
		if (_categories.size() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"help/categories_list?"+APIKEY);
			try {
				Iterator<String> i = jsonTmp.keys();
				if (i.hasNext()) _categories.clear();
				while(i.hasNext())
				{					
					String ent = i.next();
					_categories.put(ent, jsonTmp.getString(ent));
			    }
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		return _categories;
	}
	
	/**
	 * Registers a new user account via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @param email 	The user's email address. Will be used as username.
	 * @param pass 		The user's chosen password.
	 * @param phone 	The user's phone number.
	 * @param street 	The user's street address.
	 * @param postal 	The user's postal code.
	 * @return The user's key, required for authentication and submitting help items.
	 * When an empty String is returned this indicated an error 
	 */
	public static String registerUser(String email, String pass, String phone, String street, String postal)
	{
		if (_userKey.length() < 1)
		{
			try
			{
				String paramString = "";
				paramString += "&email="+URLEncoder.encode(email,"UTF-8");
				paramString += "&pass="+URLEncoder.encode(pass,"UTF-8");
				paramString += "&phone="+URLEncoder.encode(phone,"UTF-8");
				paramString += "&street="+URLEncoder.encode(street,"UTF-8");
				paramString += "&postal="+URLEncoder.encode(postal,"UTF-8");
			
				JSONObject jsonTmp = executeApiCall(URL+"user/register?"+APIKEY+paramString);
				if (jsonTmp.has("user_key")) 
				{
					_userKey = jsonTmp.getString("user_key");
					SharedPreferences.Editor editor = prefs.edit();
			        editor.putString("userKey", _userKey);
			        editor.commit();
				}
				else if (jsonTmp.has("error"))
				{
					_lastError = "error:"+jsonTmp.getString("error");
					_userKey = "";
				}
			} catch (Exception e) {
				_lastError = "Communication with server failed."+e.getStackTrace();
			} 
		}
		if (_userKey.length() > 0) return _userKey;
		else return _lastError;
	}
	
	// TODO: test login
	public static String loginUser(String email, String pass)
	{
		if (_userKey.length() < 1)
		{
			String paramString = "";
			paramString += "&email="+email;
			paramString += "&password="+pass;
			JSONObject jsonTmp = executeApiCall(URL+"user/login?"+APIKEY+paramString);
			try {
				if (jsonTmp.has("user_key")) 
				{
					_userKey = jsonTmp.getString("user_key");
					SharedPreferences.Editor editor = prefs.edit();
			        editor.putString("userKey", _userKey);
			        editor.commit();
				}
				else if (jsonTmp.has("error"))
				{
					_lastError = "error:"+jsonTmp.getString("error");
					_userKey = "";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		if (_userKey.length() > 0) return _userKey;
		else return _lastError;
	}
	
	// TODO: test submission
	public static boolean submitHelp(String email, String pass, String phone, String street, String postal)
	{
		// &categories=[5,1,8,6]&help_type=[9]&title=winner!&description=this%20is%20a%20test!
		boolean submitOk = false;
		if (_userKey.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"help/add?"+APIKEY);
			try {
				String tmp = jsonTmp.getString("api_status");
				submitOk = tmp.equals("success");
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return submitOk;
	}
	
	public static List<Place> getHelpList()
	{
		return placesList;
	}
	
	public static List<Place> getHelpList(boolean getRequests, int page)
	{
		String params = "&page="+page;
		if (getRequests) params += "&type=request";
		else params += "&type=offer";
		
		//if (_userKey.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"help/list?"+APIKEY+params);
			JSONArray list = new JSONArray();
			
			try 
			{
				list = jsonTmp.getJSONArray("results");
			}
			catch (Exception e)
			{
			}
			
			for (int i=0;i<list.length();i++)
			{
				try 
				{
					JSONObject t = list.getJSONObject(i);
					boolean needHelp = false;
					try 
					{
						JSONObject tp = t.getJSONObject("type");
						Iterator<String> it = tp.keys();
						needHelp = it.next().equals("10");
					}
					catch (Exception e) 
					{
						
					} 
					Place p = new Place(t.getString("nid"), 
							t.getString("title"),
							"", t.getString("postal_code"), 
							t.optDouble("latitude",0), 
							t.optDouble("longitude",0), 
							"", "", 
							t.getString("description"),
							0, 0, needHelp);
					placesList.add(p);
				} 
				catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return placesList;
	}
	
	///////////////////
	public static String getCategeryName(String id)
	{
		if (_categories.isEmpty()) getCategories();
		try
		{
			return _categories.get(id);
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static boolean isLoggedIn()
	{
		return _userKey.length() > 0;
	}
	
	public static String getUserKey()
	{
		return _userKey;
	}
	
	public static void setUserKey(String key)
	{
		_userKey = key;
	}
	///////////////////
	/**
	 * Execute API call
	 * This is not creating a new thread and therefore it will freeze the main thread until JSONobject has been retrieved.
	 *  
	 * @param url
	 * @return JSONObject
	 */
	public static JSONObject executeApiCall(String url) {
		try 
		{
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			JSONObject json=new JSONObject(result); 
			return json;
			
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
	
	public static JSONArray executeApiCallArray(String url) {
		try 
		{
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			JSONArray json=new JSONArray(result); 
			return json;
		} 
		catch (Exception e) 
		{
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
   
   ///////
   public static void loadPlaces() 
	{             
		Thread t = new Thread() 
		{       
			public void run() 
			{
				this.setName("PlaceLoader");
				listLoaded = false;
				// show not more than 5 pages with markers.
				for (int i=0;i<5;i++)
				{
					List<Place> tmp = ApiUtils.getHelpList(true, i);
					
					// when page has less than 10 markers, it'll be likely to be the last page. So make the for loop stop.
					if (tmp.size() < 10) i=5;
				}
				Log.d("downloader", ""+placesList.size());
				listLoaded = true;
			}        
		};        
		t.start();  
	} 
}
