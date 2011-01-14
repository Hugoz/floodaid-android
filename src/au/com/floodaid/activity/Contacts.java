package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import au.com.floodaid.R;

public class Contacts extends Activity {
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
        btnSes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	callNumber("000");
            }
        });
        
        btnPolice = (Button) findViewById(R.id.call_police);
        btnPolice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	callNumber("131444");
            }
        });
      //TODO: Add other phonenumber options.
      //TODO: Please check phonenumbers.
	}
	
	
	private void callNumber(String phoneNumber)
	{
		Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
		startActivity(call);
	}
}
