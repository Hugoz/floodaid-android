package au.com.floodaid.activity;

import java.util.List;

import org.json.JSONObject;

import com.google.android.maps.Overlay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.provider.Place;
import au.com.floodaid.util.ApiUtils;

public class AcceptTerms extends Activity {

	CheckBox checkbox;
	Button btnRegister;


	boolean needHelp;

	String email, password, phone, postcode, street, user_key;

	@Override protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.accept_terms);

		checkbox = (CheckBox) findViewById(R.id.agree_checkbox);

		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(registerBtnListener);

		// get data from intent
		Intent intent = getIntent();
		email = intent.getStringExtra("au.com.floodaid.email");
		password = intent.getStringExtra("au.com.floodaid.password");
		phone = intent.getStringExtra("au.com.floodaid.phone");
		postcode = intent.getStringExtra("au.com.floodaid.postcode");
		street = intent.getStringExtra("au.com.floodaid.street");
		needHelp = intent.getBooleanExtra("au.com.floodaid.needHelp", false);

	}

	/**
	 * On click listener for register button
	 */
	private OnClickListener registerBtnListener = new OnClickListener() {

		@Override public void onClick(View v) {
			if (checkbox.isChecked()) {
				//TODO: register with floodaid.com.au using API
				registerUser();
				
				if (needHelp)
					//nextActivity(RequestHelp.class);
					Toast.makeText(getBaseContext(), "need help", Toast.LENGTH_SHORT).show();
				else
					//nextActivity(OfferHelp.class);
					Toast.makeText(getBaseContext(), "give help", Toast.LENGTH_SHORT).show();
			} else {
				//TODO: show error
			}
		}
	};

	/**
	 * Start next activity without intent parameter
	 * @param activity
	 */
	private void nextActivity(Class<?> activity) {
		Intent Intent = new Intent(getBaseContext(), activity);
		startActivity(Intent);
	}

	protected void registerUser() 
	{     
		//ProgressDialog pd = ProgressDialog.show(getBaseContext(), "Working..", "Register/Login user", true);

		ProgressDialog progressDialog;
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("Registering...");
		progressDialog.setCancelable(true);
		progressDialog.show();
		user_key = "";
		user_key = ApiUtils.registerUser(email, password, phone, street, postcode);
		
		//pd.dismiss();
//		Thread t = new Thread() 
//		{           
//			public void run() 
//			{
//				try
//				{
//					String user_key = ApiUtils.registerUser(email, password, phone, street, postcode);
//				}
//				catch (Exception e)
//				{}
//			}        
//		};        
//		t.start();    
	} 
}
