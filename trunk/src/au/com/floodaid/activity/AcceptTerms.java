package au.com.floodaid.activity;

import java.util.List;

import org.json.JSONObject;

import com.google.android.maps.Overlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import au.com.floodaid.R;
import au.com.floodaid.provider.Place;
import au.com.floodaid.util.ApiUtils;

public class AcceptTerms extends Activity implements OnClickListener {

	CheckBox checkbox;
	Button btnRegister;
	TextView txt;
	ProgressDialog progressDialog;

	boolean needHelp;

	String email, password, phone, postcode, street, user_key;

	@Override protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.accept_terms);

		checkbox = (CheckBox) findViewById(R.id.agree_checkbox);
		txt = (TextView) findViewById(R.id.txt_Terms);
		loadTermsViaAPI();
		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		            btnRegister.setBackgroundResource(R.drawable.blue_background);
		        }
		        else btnRegister.setBackgroundResource(R.drawable.gray_background);

		    }
		});
		
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
	//private OnClickListener registerBtnListener = new OnClickListener() {
	
	
		public void onClick(View v) {
			if (checkbox.isChecked()) {
				user_key = ApiUtils.registerUser(email, password, phone, street, postcode);
				
				if (user_key.contains("error"))
				{
					AlertDialog.Builder dialog = new AlertDialog.Builder(this);
					  dialog.setMessage(user_key.substring(6))
					  .setCancelable(false)             
					  .setNegativeButton("Ok", new DialogInterface.OnClickListener() 
					  {           
						  public void onClick(DialogInterface dialog, int id) 
						  {                
							  dialog.cancel();
							  finish();
						  }       
					  });
					  AlertDialog alert = dialog.create();
					  alert.show();
				}
				else
				{
					if (needHelp)
						nextActivity(RequestHelp.class);
						//Toast.makeText(getBaseContext(), "need help", Toast.LENGTH_SHORT).show();
					else
						nextActivity(OfferHelp.class);
						//Toast.makeText(getBaseContext(), "give help", Toast.LENGTH_SHORT).show();
				}
			} else {
				//TODO: show error
			}
		}
		
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
	} 
	
	Handler progressHandler = new Handler() 
	{         
		public void handleMessage(Message msg) 
		{   
			//progressDialog.incrementProgressBy(increment);         
		}     
	};
	
	String terms;
	protected void loadTermsViaAPI() 
	{             
		Thread t = new Thread() 
		{           
			public void run() 
			{
				terms = ApiUtils.getTOU();
			
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
	final Runnable dismissDialog = new Runnable() 
	{        
		public void run() 
		{            
			progressDialog.dismiss();        
		}    
	};
}
