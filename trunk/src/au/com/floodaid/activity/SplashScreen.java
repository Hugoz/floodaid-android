package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import au.com.floodaid.R;
import au.com.floodaid.util.ApiUtils;

/**
 * Generic splash screen that disappears after some time
 * A click on the screen also forces the application to load the next activity 
 * 
 * @author hsterin
 */
public class SplashScreen extends Activity {
	
	// Logger constant
	private static final String TAG = "SplashScreen";
	
	// Delay after which the splash screen will be replaced by the next activity
	private final int SPLASH_DISPLAY_LENGTH = 3000;

	// Flah to detect if the splash screen was clicked. This is required to cancel the automated forward.
	private boolean clicked = false;
	
	
	/** 
     * Method called when activity is created
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApiUtils.loadPlaces();
		ApiUtils.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		if (ApiUtils.prefs.contains("userKey")) ApiUtils.setUserKey(ApiUtils.prefs.getString("userKey", ""));
		setContentView(R.layout.splashscreen);

		// Add a shortcut to the next activity (Main.class) if the user clicks on the splash screen
		findViewById(R.id.splashscreen).setOnClickListener(skipSplashListener);
		
		// Add a message to forward to the next activity (Main.class) after the timeout
		new Handler().postDelayed(forwarder, SPLASH_DISPLAY_LENGTH);
	}
	
	/**
     * A call-back for when the user clicks on the screen
     */
    private OnClickListener skipSplashListener = new OnClickListener() {
        public void onClick(View v) {
        	Log.d(TAG, "Clicked on view");
        	
        	// Set flag
        	clicked = true;
        	
        	// Manual forward
        	forwardToNextActivity();
        }
    };
	
	/**
	 * Task to be put in the queue to forward to the next activity 
	 */
	private Runnable forwarder = new Runnable() {
		// @Override
		public void run() {
			// Only forward if the splash screen wasn't clicked. Otherwise the user is already on the expected page.  
			if (!clicked) {
				forwardToNextActivity();
			}
		}
	};
	
    
    /**
     * Forward to the next activity (Main.class) and kill this activity as we'll never come back to it.
     */
    private void forwardToNextActivity() {
    	Intent mainIntent = new Intent(this, Main.class);
		startActivity(mainIntent);
		finish();
    }
}
