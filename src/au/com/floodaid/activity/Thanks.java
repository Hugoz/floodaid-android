package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import au.com.floodaid.R;

public class Thanks extends Activity {
	// Logger constant
	private static final String TAG = "Thanks";
	TextView txt;
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Thanks activity");
        
        setContentView(R.layout.thanks);
        
        txt = (TextView)findViewById(R.id.TextView01);
        txt.setText(R.string.thanksTo);
        
        //TODO: Build menu items dynamically and add links to related activities
	}
}
