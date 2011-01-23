package au.com.floodaid.activity;

import org.json.JSONException;
import org.json.JSONObject;
import au.com.floodaid.util.InternetUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
        try {
			loadTermsViaAPI();
		} catch (Exception e) {
			//as a backup when API is not reachable
			txt.setText(R.string.termsOfUse);
		}
        
	}
	
	String terms;
	protected void loadTermsViaAPI() 
	{             
		Thread t = new Thread() 
		{           
			public void run() 
			{
				JSONObject jsonTmp = InternetUtils.executeApiCall("http://dev.floodaid.com.au/api/tou?api_key=abcdefg12345");
				try {
					terms = jsonTmp.getString("text");
					terms = terms.replace("&nbsp;", "\n")+"\n";
				} catch (JSONException e) {
					e.printStackTrace();
				}                
				handler.post(setTerms);            
			}        
		};        
		t.start();    
	} 
	
	final Handler handler = new Handler();        
	final Runnable setTerms = new Runnable() 
	{        
		public void run() 
		{            
			txt.setText(terms);        
		}    
	};
    
}
