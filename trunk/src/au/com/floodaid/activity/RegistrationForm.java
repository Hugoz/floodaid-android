package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import au.com.floodaid.R;

public class RegistrationForm extends Activity {

	private static final String TAG = "RegistrationForm";

	EditText email, phone, street, postcode;
	Button submit;

	boolean needHelp = false;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating personal information form");

		// check if the user needs help or provides help
		Bundle extras = getIntent().getExtras();
		needHelp = extras.getBoolean("needHelp");

		// set the correct layout for the user (only header is different) 
		if (needHelp)
			setContentView(R.layout.form_register_for_help);
		else
			setContentView(R.layout.form_register_to_help);

		email = (EditText) findViewById(R.id.form_email);
		phone = (EditText) findViewById(R.id.form_phone);
		street = (EditText) findViewById(R.id.form_street);
		postcode = (EditText) findViewById(R.id.form_postcode);

		submit = (Button) findViewById(R.id.form_submit);
		submit.setOnClickListener(submitFormListener);
	}

	/**
	 * A call-back for when the user submits a form
	 */
	private OnClickListener submitFormListener = new OnClickListener() {

		@Override public void onClick(View v) {
			// TODO form check and continue to terms of use. 
			CharSequence text = email.getText();
			CharSequence phoneText = phone.getText();
		}

	};
}
