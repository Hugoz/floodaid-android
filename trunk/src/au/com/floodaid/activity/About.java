package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import au.com.floodaid.R;

public class About extends Activity {
	// Logger constant
	private static final String TAG = "About";
	
	// Objects in Activity
	Button btn;
	
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating About activity");
        
        setContentView(R.layout.about);
        btn = (Button) findViewById(R.id.Button01);
        
        //TODO: Build menu items dynamically and add links to related activities
        
     // Eventhandlers
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Intent mainIntent = new Intent(this, About.class);
            	Intent termsIntent = new Intent(getBaseContext(), Terms.class);
            	startActivity(termsIntent);
            	//finish();
            }
        });
	}
}
