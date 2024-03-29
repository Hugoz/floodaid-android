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
import android.widget.TextView;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.util.FormValidator;

public class RegistrationForm extends Activity {

	private static final String TAG = "RegistrationForm";
	public static final String PREFS_NAME = "Authentication";

	EditText emailEdit, passwordEdit, phoneEdit, streetEdit, postcodeEdit;
	Button submit;
	TextView loginLink;

	boolean needHelp = false;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating personal information form");

		// set the correct layout for the user (only header is different) 
		if (needHelp)
			setContentView(R.layout.form_register_for_help);
		else
			setContentView(R.layout.form_register_to_help);

		emailEdit = (EditText) findViewById(R.id.form_email);
		passwordEdit = (EditText) findViewById(R.id.form_password);
		phoneEdit = (EditText) findViewById(R.id.form_phone);
		streetEdit = (EditText) findViewById(R.id.form_street);
		postcodeEdit = (EditText) findViewById(R.id.form_postcode);
		
		Intent intent = getIntent();
		emailEdit.setText(intent.getStringExtra("au.com.floodaid.email"));
		passwordEdit.setText(intent.getStringExtra("au.com.floodaid.password"));
		phoneEdit.setText(intent.getStringExtra("au.com.floodaid.phone"));
		postcodeEdit.setText(intent.getStringExtra("au.com.floodaid.postcode"));
		streetEdit.setText(intent.getStringExtra("au.com.floodaid.street"));
		needHelp = intent.getBooleanExtra("au.com.floodaid.needHelp", false);

		// ronald
		emailEdit.setText("ronaldnooij@gmail.com");
		passwordEdit.setText("123456");
		phoneEdit.setText("+31625062695");
		streetEdit.setText("Huygensstr 17");
		postcodeEdit.setText("9402");
		
		submit = (Button) findViewById(R.id.form_submit);
		submit.setOnClickListener(submitFormListener);
	}

	/**
	 * A call-back for when the user submits a form
	 */
	private OnClickListener submitFormListener = new OnClickListener() {

		@Override public void onClick(View v) {

			// Init error messages
			StringBuilder errors = new StringBuilder();

			if (!FormValidator.validateEmail(emailEdit.getText().toString())) {
				errors.append("Your email is invalid\n");
			}
			// TODO: password minimum length?
			if (passwordEdit.getText() == null || "".equals(passwordEdit.getText().toString())) {
				errors.append("Your password is invalid\n");
			}
			if (FormValidator.validatePhoneNumber(phoneEdit.getText().toString())) {
				errors.append("Your phone number is invalid\n");
			}
			if (FormValidator.validatePostcode(postcodeEdit.getText().toString())) {
				errors.append("Your postcode is invalid\n");
			}
			if (streetEdit.getText() == null || "".equals(streetEdit.getText().toString())) {
				errors.append("Your street is invalid\n");
			}

			if (errors.length() == 0) {
				Log.d(TAG, "Validation successful");

				Intent intent = new Intent(getBaseContext(), AcceptTerms.class);
				intent.putExtra("au.com.floodaid.email", emailEdit.getText().toString());
				intent.putExtra("au.com.floodaid.password", passwordEdit.getText().toString());
				intent.putExtra("au.com.floodaid.phone", phoneEdit.getText().toString());
				intent.putExtra("au.com.floodaid.postcode", postcodeEdit.getText().toString());
				intent.putExtra("au.com.floodaid.street", streetEdit.getText().toString());
				intent.putExtra("au.com.floodaid.needHelp", needHelp);
				startActivity(intent);
				finish();
			}

			else {
				Log.d(TAG, "Validation error");
				Toast toast = Toast.makeText(getBaseContext(), errors.toString(), Toast.LENGTH_LONG);
				toast.show();
			}

		}

	};

	// start next activity
	private void nextActivity(Class<?> activity) {
		Intent intent = new Intent(getBaseContext(), activity);
		startActivity(intent);
	}
	
	/**
	 * Start next activity with a single boolean extra
	 * 
	 * @param activity
	 * @param parmName
	 * @param parmValue
	 */
	private void nextActivity(Class<?> activity, String parmName, boolean parmValue) {
		Intent intent = new Intent(getBaseContext(), activity);
		intent.putExtra(parmName, parmValue);
		startActivity(intent);
	}
}
