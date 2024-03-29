package au.com.floodaid.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.util.ApiUtils;

/**
 * Main activity Displays the application dashboard (menu) Called after splash screen
 * @author hsterin
 */
public class Main extends GeoLocatedActivity implements OnClickListener {

	// Logger constant
	private static final String TAG = "Main";

	// Objects in Activity
	Button btnContacts, btnAbout, btnRegisterToHelp, btnRegisterForHelp, btnFind;
	Menu _menu;
	MenuItem _menuItem;
	/**
	 * Method called when activity is created
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating Main activity");

		setContentView(R.layout.main);

		btnFind = (Button) findViewById(R.id.btn_find_people);
		btnFind.setOnClickListener(this);

		btnContacts = (Button) findViewById(R.id.btn_contacts);
		btnContacts.setOnClickListener(this);

		btnAbout = (Button) findViewById(R.id.btn_about);
		btnAbout.setOnClickListener(this);

		btnRegisterToHelp = (Button) findViewById(R.id.btn_offer_help);
		btnRegisterToHelp.setOnClickListener(this);

		btnRegisterForHelp = (Button) findViewById(R.id.btn_request_help);
		btnRegisterForHelp.setOnClickListener(this);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) 
	{		
		_menu = menu;
		if (ApiUtils.isLoggedIn()) menu.add(0, 0, 0, "Logout");
		else menu.add(0, 0, 0, "Login");
		_menuItem = menu.getItem(0);
		return true;
	}
	
	protected void onResume()
    {
    	super.onResume(); 
    	if (ApiUtils.isLoggedIn() && _menuItem != null) _menuItem.setTitle("Logout");
    	else if (_menuItem != null) _menuItem.setTitle("Login");
    }
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
		switch (item.getItemId()) 
		{   
			case 0:        
				if (ApiUtils.isLoggedIn())
				{
					ApiUtils.logOut();
					item.setTitle("Login");
				}
				else
				{
					nextActivity(Login.class, "au.com.floodaid.needHelp", false);
				}
				break;
		}    
		return true;
	}
	
	protected void onDestroy()
    {
    	super.onDestroy(); 
    	android.os.Process.killProcess(android.os.Process.myPid());  //kill everything and leave no threads.
    }

	@Override public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_find_people:
				nextActivity(FindMap.class, "au.com.floodaid.CurrentLocation", currentLocation);
				break;
			case R.id.btn_contacts:
				nextActivity(Contacts.class);
				break;
			case R.id.btn_about:
				nextActivity(About.class);
				break;
			case R.id.btn_offer_help:
				if (ApiUtils.isLoggedIn()) nextActivity(OfferHelp.class); 
				else nextActivity(RegistrationForm.class, "au.com.floodaid.needHelp", false);
				break;
			case R.id.btn_request_help:
				Toast.makeText(this, "not implemented yet", Toast.LENGTH_LONG).show();
				//if (ApiUtils.isLoggedIn()) nextActivity(RequestHelp.class);
				//else nextActivity(RegistrationForm.class, "au.com.floodaid.needHelp", true);
				break;
		}
	}

	/**
	 * Start registration form class, for/to help depending on the boolean.
	 * @param activity
	 * @param parmName
	 * @param parmValue
	 */
	private void nextActivity(Class<?> activity, String parmName, boolean parmValue) {
		Intent intent = new Intent(getBaseContext(), activity);
		intent.putExtra(parmName, parmValue);
		startActivity(intent);
	}

	/**
	 * Start Next activity with a single intent parameter
	 * @param activity
	 * @param parmName
	 * @param parmValue
	 */
	private void nextActivity(Class<?> activity, String parmName, Parcelable parmValue) {
		Intent intent = new Intent(getBaseContext(), activity);
		intent.putExtra(parmName, parmValue);
		startActivity(intent);
	}

	/**
	 * Start next activity without intent parameter
	 * @param activity
	 */
	private void nextActivity(Class<?> activity) {
		Intent Intent = new Intent(getBaseContext(), activity);
		startActivity(Intent);
	}

	@Override protected void locationFailed() {
		// Nothing to do
	}

	@Override protected void locationSuccessful() {
		// Nothing to do
	};
}
