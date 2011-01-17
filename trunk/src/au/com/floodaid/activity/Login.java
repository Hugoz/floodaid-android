package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.util.FormValidator;

public class Login extends Activity {
	
	// Logger constant
	private static final String TAG = "Login";
	
	// Layout fields
	EditText emailEdit, passwordEdit;
	Button submit;
	
	/** 
     * Method called when activity is created
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating Login activity");
        
        setContentView(R.layout.login);
        
        emailEdit = (EditText) findViewById(R.id.form_email);
        passwordEdit = (EditText) findViewById(R.id.form_password);
        
        submit = (Button) findViewById(R.id.form_submit);
		submit.setOnClickListener(submitFormListener);
		
	}
	
	
	/**
	 * A call-back for when the user submits a form
	 */
	private OnClickListener submitFormListener = new OnClickListener() {

		@Override public void onClick(View v) {
			
			// Init error messages
			StringBuilder errors = new StringBuilder();
			
			if (!FormValidator.validateEmail(emailEdit.getText().toString())) {
				errors.append("Your email is invalid\n");
			}
			// TODO: password minimum length?
			if (passwordEdit.getText() == null || "".equals(passwordEdit.getText().toString())) {
				errors.append("Your password is invalid\n");
			}

			if (errors.length() == 0) {
				Log.d(TAG, "Validation successful");
				
				 try {
			        	//TODO: API call
						//Api.login(emailEdit.getText(), passwordEdit.getText());
						
					} catch (Exception e) {
						Toast.makeText(getBaseContext(), "Your login details are incorrect, please try again", Toast.LENGTH_SHORT);
					}
			}

			else {
				Log.d(TAG, "Validation error");
				Toast toast = Toast.makeText(getBaseContext(), errors.toString(), Toast.LENGTH_LONG);
				toast.show();
			}

		}

	};
}
