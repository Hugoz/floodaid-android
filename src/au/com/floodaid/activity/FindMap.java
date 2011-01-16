package au.com.floodaid.activity;

import java.util.Collections;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

public class FindMap extends MapActivity {
	
	// Logger constant
	private static final String TAG = "FindMap";
	
	// Map variables
	private MapView mapView;
	private MapController mapController;
	
	// Map overlay that contains the markers
	private MapItemsOverlay overlay;
	
	/** 
     * Method called when activity is created
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating FindMap activity");
        
        setContentView(R.layout.find_map);
        
	    // Init the map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(false); //Set satellite view
	    mapController = mapView.getController();
	    mapController.setZoom(13); //Fixed Zoom Level
	    
	    // Get Location passed by the Main activity
	    Location currentLocation = getIntent().getParcelableExtra("au.com.floodaid.CurrentLocation"); 
		
	    if (currentLocation == null) {
	    	// Use default location Brisbane if cannot find network location
	    	currentLocation = LocationUtils.getLocationFromAddress(this, "Brisbane, QLD");
	    }
	    
	    // center map
	    centerLocation(currentLocation);
	    
	    // TODO: Load data form Drupal
		// TODO: Check api call
	    List<Place> placesList = Collections.emptyList();
	    
	    // get the JSON response from Drupal. Freezes GUI this way.
//	    JSONObject helpListing = InternetUtils.executeApiCall("http://floodaid.com.au/api/help/list?api_key=abcdefg12345&user_token="+"");
	    // TODO: Parse JSONObject
	    // see http://androidosbeginning.blogspot.com/2010/11/json-parsing-example.html
//	    try {
//			String helpType = helpListing.getString("type");
//		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
//		}

	    
	    // Create overlay items
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable defaultMarker = this.getResources().getDrawable(R.drawable.icon);
	    defaultMarker.setBounds(0, 0, defaultMarker.getIntrinsicWidth(), defaultMarker.getIntrinsicHeight());
	    overlay = new MapItemsOverlay(defaultMarker, this);

	    // Draw Current location on the map
	    GeoPoint point = LocationUtils.convertLocationToGeoPoint(currentLocation);
	    OverlayItem currentLoc = new OverlayItem(point, "Current location", "This is your current location");
	    Drawable currentLocMarker = this.getResources().getDrawable(R.drawable.icon);
	    currentLocMarker.setBounds(0, 0, currentLocMarker.getIntrinsicWidth(), currentLocMarker.getIntrinsicHeight());
    	currentLoc.setMarker(currentLocMarker);
	    overlay.addOverlay(currentLoc);

	    // Add markers for all points
	    drawPlaces(placesList);
	    
	    // Add overlay to the map
	    mapOverlays.add(overlay);
	}
    
    
    /**
     * Put markers on the map for all the places from the list
     * 
     * @param placesList
     */
    private void drawPlaces(List<Place> placesList) {
    	//TODO: use proper marker
    	Drawable placeMarker = this.getResources().getDrawable(R.drawable.icon);
    	placeMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
    	
    	// Create one OverlayItem per Place
    	for (Place p : placesList) {
    	    GeoPoint point = LocationUtils.convertLocationToGeoPoint(p.getLocation());
    	    PlaceOverlayItem placeItem = new PlaceOverlayItem(point, p);
    	    placeItem.setMarker(placeMarker);
    	    overlay.addOverlay(placeItem);
    	}
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
    	mapController.animateTo(LocationUtils.convertLocationToGeoPoint(location));
    }
    
}