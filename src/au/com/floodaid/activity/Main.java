package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import au.com.floodaid.R;

/**
 * Main activity 
 * Display the application dashboard (menu)
 * Called after splash screen
 * 
 * @author hsterin
 */
public class Main extends Activity {
	
	// Logger constant
	private static final String TAG = "Main";
	
	// Objects in Activity
	Button btn;
	
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Main activity");
        
        setContentView(R.layout.main);
        
        btn = (Button) findViewById(R.id.Button01);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent aboutIntent = new Intent(getBaseContext(), About.class);
            	startActivity(aboutIntent);
            	//finish();
            }
        });
        
        //TODO: Build menu items dynamically and add links to related activities
        
        
        
    }


    
}