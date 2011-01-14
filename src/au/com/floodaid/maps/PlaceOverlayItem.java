package au.com.floodaid.maps;


import au.com.floodaid.provider.Place;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Extension of the default OverlayItem to hold our custom objects
 * 
 * @author hsterin
 */
public class PlaceOverlayItem extends OverlayItem {
	
	// Custom object
	private Place place;
	
	public PlaceOverlayItem(GeoPoint point, Place place) {
		super(point, place.getName(), place.getDetails());
		this.place = place;
	}

	/**
	 * @return the place
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(Place place) {
		this.place = place;
	}
	
	
	
}
