package au.com.floodaid.util;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;

/**
 * Utility class to handle geolocation and distance calculations
 * 
 * @author hsterin
 */
public class LocationUtils {

	// Max distance in km to search from a given location
	public static final int MAX_DISTANCE = 500;
	
	/**
	 * For a given latitude and longitude, gets the coordinates of the bouding
	 * box at maxDistance
	 * 
	 * @param centerLat
	 * @param centerLng
	 * @param maxDistance distance in km
	 * 
	 * @return
	 */
	public static double[] getBoundingBox(double centerLat, double centerLng, int maxDistance) {
		final double[] boundingBox = new double[4];
		
		// Calculate the distance in degrees 
		final double latRadian = Math.toRadians(centerLat);
		final double degLatKm = 110.574235;
		final double degLongKm = 110.572833 * Math.cos(latRadian);
		final double deltaLat = maxDistance / degLatKm;
		final double deltaLong = maxDistance / degLongKm;
		
		// Compute the min/max coordinates
		final double minLat = centerLat - deltaLat;
		final double minLong = centerLng - deltaLong;
		final double maxLat = centerLat + deltaLat;
		final double maxLong = centerLng + deltaLong;
		
		// Build return data
		boundingBox[0] = minLat;
		boundingBox[1] = minLong;
		boundingBox[2] = maxLat;
		boundingBox[3] = maxLong;
		return boundingBox;
	}
	
	/**
	 * Convert a Location object into a GeoPoint object
	 * 
	 * @param loc
	 * @return
	 */
	public static GeoPoint convertLocationToGeoPoint(Location loc) {
		// We need to convert the location coordinates in GeoPooint format (int 1E6)
    	Double lat = loc.getLatitude() * 1E6;
    	Double lng = loc.getLongitude() * 1E6;
    	
    	return new GeoPoint(lat.intValue(), lng.intValue());
	}
	
	/**
	 * Uses Google geocoder to convert an address or place (String) into a Location with coordinates.
	 * 
	 * @param ctx
	 * @param address
	 * @return
	 */
	public static Location getLocationFromAddress(Context ctx, String address) {
		Geocoder geoCoder = new Geocoder(ctx);    
        try {
        	// Get a single result
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                Location loc = new Location(LocationManager.NETWORK_PROVIDER);
                loc.setLatitude(addresses.get(0).getLatitude());
                loc.setLongitude(addresses.get(0).getLongitude());
                return loc;
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
}
