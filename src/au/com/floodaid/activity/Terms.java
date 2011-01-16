package au.com.floodaid.activity;

import org.json.JSONObject;
import au.com.floodaid.util.InternetUtils;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import au.com.floodaid.R;
import au.com.floodaid.util.InternetUtils;

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
        try {
			txt.setText(InternetUtils.getTOC("http://dev.floodaid.com.au/api/toc?api_key=abcdefg12345"));
		} catch (Exception e) {
			//as a backup when API is not reachable
			txt.setText(R.string.termsOfUse);
		}
        
        
        //TODO: Build menu items dynamically and add links to related activities
	}
}
