package au.com.floodaid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;
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
	//private static ArrayList<String> _categories = new ArrayList<String>();
	private static HashMap<String, String> _categories = new HashMap<String,String>();
	
	///////////////////
	
	/**
	 * Get Terms of use via API
	 * This is not creating a new thread and therefore it will freeze the main thread until Terms of use has been retrieved.
	 * Or threading should be added somewhere else
	 * 
	 * @return A String with the Terms of Use. 
	 */
	public static String getTOU() {
		
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
	public static HashMap<String, String> getCategories() {
		
		if (_categories.size() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"help/categories_list?"+APIKEY);
			//JSONArray list = new JSONArray();
			try {
				//_categories.add(jsonTmp.getString("1"));
				Iterator<String> i = jsonTmp.keys();
				if (i.hasNext()) _categories.clear();
				while(i.hasNext()){
					String ent = i.next();
			      //_categories.add();
			      _categories.put(ent, jsonTmp.getString(ent));
			    }

//				list = jsonTmp.getJSONArray("results");
//				_tou = jsonTmp.getString("text");
//				_tou = _tou.replace("&nbsp;", "\n")+"\n";
			} catch (Exception e) {
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
	// TODO: test registration
	public static String registerUser(String email, String pass, String phone, String street, String postal)
	{
		if (_userKey.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"user/register?"+APIKEY);
			try {
				if (jsonTmp.has("user_key")) _userKey = jsonTmp.getString("user_key");
				else if (jsonTmp.has("error"))
				{
					String error = jsonTmp.getString("error");
					if (error.contains("already registered"))
					{
						_userKey = loginUser(email, pass);
					}
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return _userKey;
		//TODO: handle error: 'email address is already registered' when email not unique.
		//					  With this error it might be possible to just login. Try that too.
		
		//TODO: handle error: 'x parameters required' when required
		//                    Throw Exception??

	}
	
	// TODO: test login
	public static String loginUser(String email, String pass)
	{
		if (_userKey.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"user/login?"+APIKEY);
			try {
				if (jsonTmp.has("user_key")) _userKey = jsonTmp.getString("user_key");
				else if (jsonTmp.has("error"))
				{
					String error = jsonTmp.getString("error");
					if (error.contains("invalid email and\\/or password"))
					{
						_userKey = "ERROR";
					}
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}
		return _userKey;
		//TODO: handle error: 'invalid email and/or password' when as described.
		//                    Throw Exception??

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
		//TODO: handle error: 'invalid user_key' when as described.
		//                    Throw Exception??
		//					  When email and pass available first login.

	}
	
	// TODO: test listing
	public static List<Place> getHelpList(boolean getRequests, int page)
	{
		List<Place> places = new ArrayList<Place>();
		String params = "&page="+page;
		if (getRequests) params += "&type=request";
		else params += "&type=offer";
		
		if (_userKey.length() < 1)
		{
			JSONObject jsonTmp = executeApiCall(URL+"help/list?"+APIKEY);
			JSONArray list = new JSONArray();
			
			try {
				list = jsonTmp.getJSONArray("results");
			}
			catch (Exception e)
			{
			}
			
			for (int i=0;i<list.length();i++)
			{
				try {

					JSONObject t = list.getJSONObject(i);
					//String nid = 
					//if (t.getDouble("latitude") > 0 || t.getDouble("longitude") > 0)
					{
						Place p = new Place(t.getString("nid"), t.getString("title"),
								"", t.getString("postal_code"), 
								t.optDouble("latitude",0), t.optDouble("longitude",0), "", "", t.getString("description"),
								0, 0);
						places.add(p);
					}
					//System.out.println(p.getName());
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return places;
		//TODO: handle error: 'invalid email and/or password' when as described.
		//                    Throw Exception??

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
	///////////////////
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
	
	public static JSONArray executeApiCallArray(String url) {
		try {
			URL urlObj = new URL(url);
			InputStream is = (InputStream) urlObj.getContent();
			String result = convertStreamToString(is);
			JSONArray json=new JSONArray(result); 
			return json;
			
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
