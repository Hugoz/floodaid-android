package au.com.floodaid.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import au.com.floodaid.util.ApiUtils;
import au.com.floodaid.util.InternetUtils;
import au.com.floodaid.R;
import au.com.floodaid.maps.MapItemsOverlay;
import au.com.floodaid.maps.PlaceOverlayItem;
import au.com.floodaid.provider.Place;
import au.com.floodaid.util.LocationUtils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class FindMap extends MapActivity implements OnClickListener {
	
	// Logger constant
	private static final String TAG = "FindMap";
	
	// Map variables
	private MapView mapView;
	private MapController mapController;
	
	// Map overlay that contains the markers
	//private ArrayList<Place> pL = new ArrayList<Place>();
	//private List<Place> placesList = Collections.synchronizedList(pL);
	private List<Place> placesList = new ArrayList<Place>();
	private MapItemsOverlay overlay;
	ProgressDialog dialog;
	
	Button btnContacts, btnRegisterToHelp, btnRegisterForHelp;
	
	/** 
     * Method called when activity is created
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating FindMap activity");
        
        setContentView(R.layout.find_map);
        
        // Register onclick for buttons 
        btnRegisterToHelp = (Button) findViewById(R.id.btn_offer_help);
		btnRegisterToHelp.setOnClickListener(this);

		btnRegisterForHelp = (Button) findViewById(R.id.btn_request_help);
		btnRegisterForHelp.setOnClickListener(this);

		btnContacts = (Button) findViewById(R.id.btn_emergency);
		btnContacts.setOnClickListener(this);

	    // Init the map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(false); //Set satellite view
	    mapController = mapView.getController();
	    mapController.setZoom(11); //Fixed Zoom Level
	    
	 // Get Location passed by the Main activity
	    Location currentLocation = getIntent().getParcelableExtra("au.com.floodaid.CurrentLocation"); 
		
	   // if (currentLocation == null) 
	    {
	    	// Use default location Brisbane if cannot find network location
	    	// prefer using lat,lon as using an address involves network traffic 
	    	// which slows down the loading way to much.
	    	//currentLocation = LocationUtils.getLocationFromAddress(this, "Brisbane, QLD, Australia");
	    	currentLocation = new Location("");
	    	currentLocation.setLatitude(-27.377465);
	    	currentLocation.setLongitude(152.970221);
	    	
	    }
	    
	    // center map
	    
	    //setPointer(currentLocation);
	    centerLocation(currentLocation);
	    
	    //createOverlay();
	    placesToOverlay();
	}
	
	private void createOverlay()
	{
	    List<Overlay> listOfOverlays = mapView.getOverlays();
	    listOfOverlays.clear();
	    
	    Drawable defaultMarker = this.getResources().getDrawable(R.drawable.map_marker);
	    defaultMarker.setBounds(0, 0, defaultMarker.getIntrinsicWidth(), defaultMarker.getIntrinsicHeight());
	    overlay = new MapItemsOverlay(defaultMarker, this);
	    
	    //refreshPlaces();
	    placesList = ApiUtils.getHelpList();
	    if (!placesList.isEmpty())
	    {
	    	List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable placeMarker = getResources().getDrawable(R.drawable.map_marker);
	    	placeMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
	    	
	    	try
	    	{
		    	for (Place p : placesList) {
		    		if (p.needHelp()) placeMarker = getResources().getDrawable(R.drawable.map_marker_red);
		    		else placeMarker = getResources().getDrawable(R.drawable.map_marker_blue);
		    		placeMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
		    	    GeoPoint point = LocationUtils.convertLocationToGeoPoint(p.getLocation());
		    	    PlaceOverlayItem placeItem = new PlaceOverlayItem(point, p);
		    	    placeItem.setMarker(placeMarker);
		    	    overlay.addOverlay(placeItem);
		    	}
		    	mapOverlays.clear();
			    mapOverlays.add(overlay);
	    	}
	    	catch (Exception e)
	    	{
	    		Log.e(TAG, "269: "+e.getMessage());
	    	}
	    }
	}
    
	// Need handler for callbacks to the UI thread    
	final Handler mHandler = new Handler();    
	
	// Create runnable for posting    
	final Runnable mUpdateResults = new Runnable() 
	{        
		public void run() 
		{            
			updateResultsInUi();        
		}    
		
	};
	
	public void placesToOverlay() 
	{           
		//dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		Thread t = new Thread() 
		{       
			public void run() 
			{
				int sleepTime = 500;
				this.setName("OverlayCreator");
				//ApiUtils.loadPlaces();
					try 
					{
						while (!ApiUtils.listLoaded) 
						{
							sleep(sleepTime);
						}
					} 
					catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				createOverlay();
				mHandler.post(mUpdateResults);
			}        
		};        
		t.start();    
	} 
    
	private void updateResultsInUi() 
	{        
		// Back in the UI thread -- update our UI elements
		mapView.invalidate();
		//dialog.dismiss();
	}
	
    /**
     * Required by superclass
     */
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    /**
     * Center the map on a given point
     * 
     * @param centerGeoPoint
     */
    private void centerLocation(Location location)
    {	
    	if (location != null) {
    		mapController.animateTo(LocationUtils.convertLocationToGeoPoint(location));
    	}
    }
    
    private void setPointer(Location location)
    {
    	// Draw Current location on the map
    	try
    	{
		    GeoPoint point = LocationUtils.convertLocationToGeoPoint(location);
		    OverlayItem currentLoc = new OverlayItem(point, "Current location", "This is your current location");
		    Drawable currentLocMarker = this.getResources().getDrawable(R.drawable.blue_marker);
		    currentLocMarker.setBounds(0, 0, currentLocMarker.getIntrinsicWidth(), currentLocMarker.getIntrinsicHeight());
	    	currentLoc.setMarker(currentLocMarker);
	    	overlay.addOverlay(currentLoc);
	    	mapView.getOverlays().add(overlay);
	    	mapView.invalidate();
    	}
    	catch (Exception e) {}
    }
   
    	@Override 
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_emergency:
				nextActivity(Contacts.class);
				break;
			case R.id.btn_offer_help:
				if (ApiUtils.isLoggedIn()) nextActivity(OfferHelp.class);
				else nextActivity(RegistrationForm.class, "au.com.floodaid.needHelp", false);
				break;
			case R.id.btn_request_help:
				if (ApiUtils.isLoggedIn()) nextActivity(RequestHelp.class); 
				else nextActivity(RegistrationForm.class, "au.com.floodaid.needHelp", true);
				break;
		}
	}

	/**
	 * Start registration form class, for/to help depending on the boolean.
	 * 
	 * @param activity
	 * @param parmName
	 * @param parmValue
	 */
	private void nextActivity(Class<?> activity, String parmName, boolean parmValue) {
		Intent intent = new Intent(getBaseContext(), activity);
		intent.putExtra(parmName, parmValue);
		startActivity(intent);
	}
	
	/**
	 * Start next activity without intent parameter
	 * @param activity
	 */
	private void nextActivity(Class<?> activity) {
		Intent Intent = new Intent(getBaseContext(), activity);
		startActivity(Intent);
	}
	
	///////////////
	
	
	
	
	class OverlayTask extends AsyncTask<Void, Void, Void> {
		@Override
		public void onPreExecute() {
			if (overlay!=null) {
				mapView.getOverlays().remove(overlay);
				mapView.invalidate();	
				overlay=null;
			}
		}
		
		@Override
		public Void doInBackground(Void... unused) {
			SystemClock.sleep(5000);						// simulated work
			
			//overlay=new SitesOverlay();
			
			return(null);
		}
		
		@Override
		public void onPostExecute(Void unused) {
			mapView.getOverlays().add(overlay);
			mapView.invalidate();			
		}
	}

    
}
