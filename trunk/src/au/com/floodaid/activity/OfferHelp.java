package au.com.floodaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import au.com.floodaid.R;
import au.com.floodaid.util.ApiUtils;

public class OfferHelp extends Activity {

	private static final String TAG = "Register to help";

	// enum for the different category buttons
	private enum CategoryButton {
		btnFood(R.id.btn_food, 1, R.drawable.button_food, R.drawable.button_food_check),
		btnConstruction(R.id.btn_construction, 5, R.drawable.button_construction, R.drawable.button_construction_check),
		btnGeneral(R.id.btn_general, 8, R.drawable.button_general, R.drawable.button_general_check),
		btnHealth(R.id.btn_health, 6, R.drawable.button_health, R.drawable.button_health_check),
		btnRecovery(R.id.btn_recovery, 4, R.drawable.button_recovery, R.drawable.button_recovery_check),
		btnSanitation(R.id.btn_sanitation, 7, R.drawable.button_sanitation, R.drawable.button_sanitation_check),
		btnTransport(R.id.btn_transportation, 3, R.drawable.button_transport, R.drawable.button_transport_check),
		btnShelter(R.id.btn_shelter, 2, R.drawable.button_shelter, R.drawable.button_shelter_check);

		private CategoryButton(final int id, final int catid, final int imgIdDefault, final int imgIdSelected) {
			this.id = id;
			this.catid = catid;
			this.imgIdDefault = imgIdDefault;
			this.imgIdSelected = imgIdSelected;

		}

		ImageView icon; // the icon corresponding to this enum instance
		Boolean selected; // the button can be selected or not 
		final Integer id; // the id of the icon imageview
		final Integer catid; // the id of the category according to the API specs
		final Integer imgIdDefault; // the id of the drawable to display when the icon isn't selected
		final Integer imgIdSelected; // the id of the drawable to display in case the icon is selected

		// function to obtain enum instance corresponding to the view with this id
		public static CategoryButton getCategoryButton(int id) {
			for (CategoryButton cb : CategoryButton.values())
				if (cb.id == id)
					return cb;
			return null;
		}
	}

	CategoryButton[] buttons = CategoryButton.values();
	Button nextButton;
	EditText comments;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "Creating personal information form");

		setContentView(R.layout.offer_help);

		comments = (EditText) findViewById(R.id.comments);
		nextButton = (Button) findViewById(R.id.next);
		
		for (CategoryButton button : buttons) {
			button.icon = (ImageView) findViewById(button.id);
			button.selected = false;
			button.icon.setOnClickListener(categoryButtonListener);
		}
		
		// Next button
		nextButton.setOnClickListener(submitFormListener);
	}

	// select/deselect category icons
	OnClickListener categoryButtonListener = new OnClickListener() {

		@Override public void onClick(View v) {
			CategoryButton categoryButton = CategoryButton.getCategoryButton(v.getId());
			categoryButton.selected = !categoryButton.selected;
			if (categoryButton.selected)
				categoryButton.icon.setImageResource(categoryButton.imgIdSelected);
			else
				categoryButton.icon.setImageResource(categoryButton.imgIdDefault);

		}
	};
	
	/**
	 * A call-back for when the user submits the form
	 */
	OnClickListener submitFormListener = new OnClickListener() {

		@Override public void onClick(View v) {

			// Init error messages
			StringBuilder errors = new StringBuilder();
			
			
			// TODO: Check that there is at least 1 category selected
			
			if (comments.getText() == null || "".equals(comments.getText().toString())) {
				errors.append("Please enter a description of the help you can offer\n");
			}
			
			if (errors.length() == 0) {
				Log.d(TAG, "Validation successful");
				
				String categories = "";
				String comma = "";
				for (CategoryButton button : buttons) {
					if (button.selected) 
					{
						categories += comma+button.catid;
						comma = ",";
					}
				}
				//TODO: API Call to submit new help offer
				//help_type: 9 = offer 10 = request
				//ApiUtils.submitHelp(categories, "9", "title", comments.getText().toString());
				// TODO: If success, go to Confirmation/Thank you page

			
			}

			else {
				Log.d(TAG, "Validation error");
				Toast toast = Toast.makeText(getBaseContext(), errors.toString(), Toast.LENGTH_LONG);
				toast.show();
			}

		}

	};

}
