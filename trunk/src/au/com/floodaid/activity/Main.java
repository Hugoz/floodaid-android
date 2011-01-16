package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.floodaid.R;

/**
 * Main activity 
 * Displays the application dashboard (menu) 
 * Called after splash screen
 * 
 * @author hsterin
 */
public class Main extends Activity implements OnClickListener {

	// Logger constant
	private static final String TAG = "Main";

	// Objects in Activity
	Button btnContacts, btnAbout, btnRegisterToHelp, btnRegisterForHelp, btnFind;

	/**
	 * Method called when activity is created
	 */
	@Override 
	public void onCreate(Bundle savedInstanceState) {
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

	@Override 
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_find_people:
				nextActivity(FindMap.class);
			break;
			case R.id.btn_contacts:
				nextActivity(Contacts.class);
				break;
			case R.id.btn_about:
				nextActivity(About.class);
				break;
			case R.id.btn_offer_help:
				nextActivity(RegistrationForm.class, "needHelp", false);
				break;
			case R.id.btn_request_help:
				nextActivity(RegistrationForm.class, "needHelp", true);
				break;
		}
	}

	// start registration form class, for/to help depending on the boolean.
	private void nextActivity(Class<?> activity, String parmName, boolean parmValue) {
		Intent intent = new Intent(getBaseContext(), activity);
		intent.putExtra(parmName, parmValue);
		startActivity(intent);
	}

	private void nextActivity(Class<?> activity) {
		Intent Intent = new Intent(getBaseContext(), activity);
		startActivity(Intent);
	}
}
