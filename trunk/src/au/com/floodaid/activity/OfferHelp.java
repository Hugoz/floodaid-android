package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import au.com.floodaid.R;

public class OfferHelp extends Activity {

	private static final String TAG = "Register to help";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO: check if the user is logged in or not, forward to Registration Form

		Log.d(TAG, "Creating personal information form");

		setContentView(R.layout.offer_help);

	}

}
