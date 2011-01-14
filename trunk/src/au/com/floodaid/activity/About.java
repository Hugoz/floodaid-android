package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.floodaid.R;

public class About extends Activity implements OnClickListener {
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
        btnTerms.setOnClickListener(this);
        
        btnThanks = (Button) findViewById(R.id.Btn_Thanks);
        btnThanks.setOnClickListener(this);
        
      //TODO: Build menu items dynamically and add links to related activities
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) 
		{
			case R.id.Btn_Terms:
				nextActivity(Terms.class);
			break;
			case R.id.Btn_Thanks:
				nextActivity(Thanks.class);
			break;
		}
	}
	
	private void nextActivity(Class<?> intent)
	{
		Intent Intent = new Intent(getBaseContext(), intent);
    	startActivity(Intent);
	}
}
