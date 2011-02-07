package au.com.floodaid.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	    mapController.setZoom(13); //Fixed Zoom Level
	    
	    List<Overlay> listOfOverlays = mapView.getOverlays();
	    listOfOverlays.clear();
	    
	    Drawable defaultMarker = this.getResources().getDrawable(R.drawable.map_marker);
	    defaultMarker.setBounds(0, 0, defaultMarker.getIntrinsicWidth(), defaultMarker.getIntrinsicHeight());
	    overlay = new MapItemsOverlay(defaultMarker, this);
	    
	    refreshPlaces();
	    
	    
	    // Get Location passed by the Main activity
	    Location currentLocation = getIntent().getParcelableExtra("au.com.floodaid.CurrentLocation"); 
		
	    //if (currentLocation == null) 
	    {
	    	// Use default location Brisbane if cannot find network location
	    	currentLocation = LocationUtils.getLocationFromAddress(this, "Brisbane, QLD, Australia");
	    }
	    
	    // center map
	    
	    //setPointer(currentLocation);
	    centerLocation(currentLocation);
	    
	    
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
	
	protected void refreshPlaces() 
	{             
		Thread t = new Thread() 
		{           
			public void run() 
			{
				// show 5 pages with markers.
				for (int i=0;i<5;i++)
				{
					List<Place> tmp = ApiUtils.getHelpList(true, i);
					if (tmp.size() < 10) i=14;
					placesList.addAll(tmp);

					handler.post(refreshMap);
				}
				
			}        
		};        
		t.start();    
	} 
	
	final Handler handler = new Handler();        
	final Runnable refreshMap = new Runnable() 
	{        
		public void run() 
		{            
			List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable placeMarker = getResources().getDrawable(R.drawable.map_marker);
	    	placeMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
	    	
	    	// Create one OverlayItem per Place
	    	// TODO: take care java.util.ConcurrentModificationException
	    	try
	    	{
		    	for (Place p : placesList) {
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
			mapView.invalidate();
		}    
	};
	
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
