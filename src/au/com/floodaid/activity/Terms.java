package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import au.com.floodaid.R;

public class Terms extends Activity {
	// Logger constant
	private static final String TAG = "Terms";
	TextView txt;
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Terms activity");
        
        setContentView(R.layout.terms);
        
        txt = (TextView)findViewById(R.id.TextView01);
        txt.setText(R.string.termsOfUse);
        
        //TODO: Build menu items dynamically and add links to related activities
	}
}
