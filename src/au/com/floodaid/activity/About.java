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
	Button btnTerms, btnThanks;
	
    /** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating About activity");
        
        setContentView(R.layout.about);
        btnTerms = (Button) findViewById(R.id.Btn_Terms);
        btnThanks = (Button) findViewById(R.id.Btn_Tanks);
        
        btnTerms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent termsIntent = new Intent(getBaseContext(), Terms.class);
            	startActivity(termsIntent);
            	//finish();
            }
        });
        
        btnThanks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent thanksIntent = new Intent(getBaseContext(), Thanks.class);
            	startActivity(thanksIntent);
            	//finish();
            }
        });
        
      //TODO: Build menu items dynamically and add links to related activities
	}
}
