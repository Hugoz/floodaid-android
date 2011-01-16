package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import au.com.floodaid.R;
import au.com.floodaid.util.FormValidator;

public class RegistrationForm extends Activity {

	private static final String TAG = "RegistrationForm";
	public static final String PREFS_NAME = "Authentication";

	EditText emailEdit, phoneEdit, streetEdit, postcodeEdit;
	Button submit;

	boolean needHelp = false;
	boolean loggedIn;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating personal information form");

		// check if the user is logged in 
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		loggedIn = settings.getBoolean("loggedIn", false);

		// check if the user needs help or provides help
		Bundle extras = getIntent().getExtras();
		needHelp = extras.getBoolean("au.com.floodaid.needHelp");

		if (loggedIn) {
			if (!needHelp)
				nextActivity(OfferHelp.class);
			else
				nextActivity(RequestHelp.class);

		}

		// set the correct layout for the user (only header is different) 
		if (needHelp)
			setContentView(R.layout.form_register_for_help);
		else
			setContentView(R.layout.form_register_to_help);

		emailEdit = (EditText) findViewById(R.id.form_email);
		phoneEdit = (EditText) findViewById(R.id.form_phone);
		streetEdit = (EditText) findViewById(R.id.form_street);
		postcodeEdit = (EditText) findViewById(R.id.form_postcode);
		submit = (Button) findViewById(R.id.form_submit);
		submit.setOnClickListener(submitFormListener);
	}

	/**
	 * A call-back for when the user submits a form
	 */
	private OnClickListener submitFormListener = new OnClickListener() {

		FormValidator formValidator = new FormValidator();

		@Override public void onClick(View v) {
			// TODO see if form check is corresponding to database requirements 
			CharSequence email = emailEdit.getText();
			boolean validEmail = formValidator.validateEmail(email.toString());

			CharSequence phoneNumber = phoneEdit.getText();
			boolean validPhoneNumber = formValidator.validatePhoneNumber(phoneNumber.toString());

			CharSequence postcode = postcodeEdit.getText();
			boolean validPostcode = formValidator.validatePostcode(postcode.toString());

			CharSequence street = streetEdit.getText();
			boolean validStreet = street.length() > 0;

			if (validEmail && validPhoneNumber && validPostcode && validStreet) {
				Intent intent = new Intent(getBaseContext(), AcceptTerms.class);
				intent.putExtra("email", email);
				intent.putExtra("phone", phoneNumber);
				intent.putExtra("postcode", postcode);
				intent.putExtra("street", street);
				startActivity(intent);
			}

			else {
				//TODO show message (alert?) 
			}

		}

	};

	// start next activity
	private void nextActivity(Class<?> activity) {
		Intent intent = new Intent(getBaseContext(), activity);
		startActivity(intent);
	}

}
