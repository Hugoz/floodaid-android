package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
	
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Main activity");
        
        setContentView(R.layout.main);
        
        //TODO: Add onclick to menus
        
    }


    
}