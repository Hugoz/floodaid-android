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
 * Display the application dashboard (menu)
 * Called after splash screen
 * 
 * @author hsterin
 */
public class Main extends Activity implements OnClickListener {
	
	// Logger constant
	private static final String TAG = "Main";
	
	// Objects in Activity
	Button btnContacts, btnAbout;
	
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Main activity");
        
        setContentView(R.layout.main);
        
        btnContacts = (Button) findViewById(R.id.btn_contacts);
        btnContacts.setOnClickListener(this);
        
        btnAbout = (Button) findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(this);
        
        //TODO: Build menu items dynamically and add links to related activities
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) 
		{
			case R.id.btn_contacts:
				nextActivity(Contacts.class);
			break;
			case R.id.btn_about:
				nextActivity(About.class);
			break;
		}
	}
	
	private void nextActivity(Class<?> intent)
	{
		Intent Intent = new Intent(getBaseContext(), intent);
    	startActivity(Intent);
	}


    
}