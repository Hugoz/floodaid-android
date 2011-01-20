package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import au.com.floodaid.R;

public class AcceptTerms extends Activity {

	CheckBox checkbox;
	Button btnRegister;

	boolean needHelp;

	CharSequence email, phone, postcode, street;

	@Override protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.accept_terms);

		checkbox = (CheckBox) findViewById(R.id.agree_checkbox);

		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(registerBtnListener);

		// get data from intent
		Intent intent = getIntent();
		email = intent.getCharSequenceExtra("au.com.floodaid.email");
		phone = intent.getCharSequenceExtra("au.com.floodaid.phone");
		postcode = intent.getCharSequenceExtra("au.com.floodaid.postcode");
		street = intent.getCharSequenceExtra("au.com.floodaid.street");
		needHelp = intent.getBooleanExtra("au.com.floodaid.needHelp", false);

	}

	/**
	 * On click listener for register button
	 */
	private OnClickListener registerBtnListener = new OnClickListener() {

		@Override public void onClick(View v) {
			if (checkbox.isChecked()) {
				//TODO: register with floodaid.com.au using API
				if (needHelp)
					nextActivity(RequestHelp.class);
				else
					nextActivity(OfferHelp.class);
			} else {
				//TODO: show error
			}
		}
	};

	/**
	 * Start next activity without intent parameter
	 * @param activity
	 */
	private void nextActivity(Class<?> activity) {
		Intent Intent = new Intent(getBaseContext(), activity);
		startActivity(Intent);
	}

}
