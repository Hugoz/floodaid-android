package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.floodaid.R;

public class Contacts extends Activity implements OnClickListener {

	// Logger constant
	private static final String TAG = "Contacts";

	Button btnDisasterRecoveryHotline, btnSes, btnPoliceFireAmbulance, btnPoliceLink;

	/**
	 * Method called when activity is created
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating Contacts activity");

		setContentView(R.layout.contacts);

		btnDisasterRecoveryHotline = (Button) findViewById(R.id.call_disaster_recovery_hotline);
		btnDisasterRecoveryHotline.setOnClickListener(this);

		btnSes = (Button) findViewById(R.id.call_ses);
		btnSes.setOnClickListener(this);

		btnPoliceFireAmbulance = (Button) findViewById(R.id.call_police_fire_ambulance);
		btnPoliceFireAmbulance.setOnClickListener(this);

		btnPoliceLink = (Button) findViewById(R.id.call_police_link);
		btnPoliceLink.setOnClickListener(this);
		//TODO: Add other phonenumber options.
		//TODO: Please check phonenumbers.
		/*	Checked with Graeme;
		 *  	Disaster Recovery Hotline:	1800 173 349
		 *	State Emergency Service:	132 500
		 *	Police, Fire, Ambulance	000
		 *	Non-urgent incidents (PoliceLink)	131 444 
		*/
	}

	@Override public void onClick(View view) {
		switch (view.getId()) {
			case R.id.call_ses:
				callNumber("132500");
				break;
			case R.id.call_police_fire_ambulance:
				callNumber("000");
				break;
			case R.id.call_police_link:
				callNumber("131444");
				break;
			case R.id.call_disaster_recovery_hotline:
				callNumber("1800173349");
				break;
		}
	}

	private void callNumber(String phoneNumber) {
		Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		startActivity(call);
	}
}
