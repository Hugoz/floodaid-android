package au.com.floodaid.activity;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
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

/**
 * This activity shows a Map centered on the selectedLocation and add an Overlay with places if available
 * 
 * @author hsterin
 */
public class PlacesMap extends MapActivity {

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
	    setContentView(R.layout.mapview);
	    
	    // Init the map view
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true); //Set satellite view
	    mapController = mapView.getController();
	    mapController.setZoom(13); //Fixed Zoom Level
	    
	    // TODO: Get data passed by the previous action
	    Location currentLocation = getIntent().getParcelableExtra("au.com.deloitte.online.android.prototype.provider.CurrentLocation"); 
	    int selectedItemIdx = getIntent().getIntExtra("au.com.deloitte.online.android.prototype.provider.SelectedItemIndex", -1); 
	    List<Place> placesList =  getIntent().getParcelableArrayListExtra("au.com.deloitte.online.android.prototype.provider.PlacesList");
	    
	    // TODO: Load data 
	    if (placesList == null) {

	    }
	    
	    // Get selected item from the list
	    if (selectedItemIdx > -1) {
	    	Place selectedPlace = placesList.get(selectedItemIdx);
	    	 // Center the map on the selected item
	    	centerLocation(selectedPlace.getLocation());
	    } else {
	    	centerLocation(currentLocation);
	    }
	    
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

	    // Add markers for all Places
	    drawPlaces(placesList, selectedItemIdx);
	    
	    // Add overlay to the map
	    mapOverlays.add(overlay);
	}
    
    
    /**
     * Put markers on the map for all the places from the list
     * 
     * @param placesList
     */
    private void drawPlaces(List<Place> placesList, int selectedPlaceIndex) {
//    	Drawable placeMarker = this.getResources().getDrawable(R.drawable.icon_green);
//    	placeMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
//    	Drawable selectedPlaceMarker = this.getResources().getDrawable(R.drawable.icon_red);
//    	selectedPlaceMarker.setBounds(0, 0, placeMarker.getIntrinsicWidth(), placeMarker.getIntrinsicHeight());
//    	Drawable placeMarker = mLoader.getDrawable(selectedPlaceIndex);
    	
    	// Create one OverlayItem per Place
    	for (Place p : placesList) {
    	    GeoPoint point = LocationUtils.convertLocationToGeoPoint(p.getLocation());
    	    PlaceOverlayItem placeItem = new PlaceOverlayItem(point, p);
    	    
    	    // Check if this is the selected place to display a different icon
    	    if (placesList.indexOf(p) == selectedPlaceIndex) {
//    	    	placeItem.setMarker(placeMarker);
    	    } else {
//    	    	placeItem.setMarker(placeMarker);
    	    }
    	    
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
