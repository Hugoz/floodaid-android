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
	Button btnSes, btnPolice, btnFire, btnAmbu;
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Contacts activity");
        
        setContentView(R.layout.contacts);
        
        btnSes = (Button) findViewById(R.id.call_ses);
        btnSes.setOnClickListener(this);
        
        btnPolice = (Button) findViewById(R.id.call_police);
        btnPolice.setOnClickListener(this);
      //TODO: Add other phonenumber options.
      //TODO: Please check phonenumbers.
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.call_ses:
				callNumber("000");
			break;
			case R.id.call_police:
				callNumber("131444");
			break;
		}
	}
	
	private void callNumber(String phoneNumber)
	{
		Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
		startActivity(call);
	}
}
