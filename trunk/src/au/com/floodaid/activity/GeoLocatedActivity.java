package au.com.floodaid.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import au.com.floodaid.util.DateUtils;

/**
 * Abstract class used to add Geolocation capabilities to any activity
 * This class starts geolocation on activity creation, and stops the services after a first valid location is found
 * It calls locationSuccessful() or locationFailed(), these methods must be implemented by the inheriting class
 * 
 * @author hsterin
 */
public abstract class GeoLocatedActivity extends Activity {

	// Logger constant
	private static final String TAG = "GeoLocatedActivity";
	
	// Local vars
	private LocationManager locationManager;
	private LocationListener locationListener;
	protected Location currentLocation;
	
	// Used for geolocation
	private static final long FIVE_MINUTES = 1000 * 60 * 5;
	
    /** 
     * Method called when activity is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "Creating GeoLocated activity");

        // Init location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        // Geolocate if possible
        startGeoLocation();
    }
    
    /**
     * Method to be implemented by the inheriting class
     * Do something if a geolocation fails
     */
    protected abstract void locationFailed();

    /**
     * Method to be implemented by the inheriting class
     * Do something after a successful geolocation
     */
    protected abstract void locationSuccessful();
    
    
    /**
     * Start the process to geolocate the user
     */
    private void startGeoLocation() {
	
    	// Get last known location from network
    	currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    
    	// If the last location is empty or too old, start a listener to get the current location from the system
    	if (currentLocation == null || DateUtils.getAge(currentLocation.getTime()) > FIVE_MINUTES) {
	    	
	    	// Define a listener that responds to location updates
		    locationListener = new LocationListener() {
		        public void onLocationChanged(Location location) {
		          // Called when a new location is found by the network location provider.
		          currentLocation = location;
		          processLocation(true);
		        }
		
		        public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		        public void onProviderEnabled(String provider) {}
		
		        public void onProviderDisabled(String provider) {}
		      };
		
		    // Register the listener with the Location Manager to receive location updates
		    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	    	
	    } else {
	    	processLocation(false);
	    }
    }
    
    /**
     * Try to process a location
     * 
     * @param fromSystem is the method called from the system listener or manually?
     */
    private void processLocation(boolean fromSystem) {
    	
    	// If no location but this is the first init call, wait for the next location update
    	if (currentLocation == null && !fromSystem) {
    		Log.i(TAG, "Cannot find initial location, waiting for the next update from LocationManager");
    		
    	} else if (currentLocation == null) {
    		Log.i(TAG, "LocationManager doesn't know the location.");
    		
    		// Do something
    		locationFailed();
    		
    		// Stop location listener
    		locationManager.removeUpdates(locationListener);
    		
    		
    	} else {
    		Log.i(TAG, "Geolocated at " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
    		
    		// Do something
    		locationSuccessful();
    		
    		// Stop location listener if it exists
    		if (fromSystem) {
    			locationManager.removeUpdates(locationListener);
    		}
    		
    	}
    }
    
    
    
    /** Determines whether one Location reading is better than the current Location fix
      * @param newLocation  The new Location that you want to evaluate
      * @param currentLocation  The current Location fix, to which you want to compare the new one
      */
    protected boolean isBetterLocation(Location newLocation, Location currentLocation) {
    	
        if (currentLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > FIVE_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -FIVE_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than 5 minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than 5 minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
	
}
