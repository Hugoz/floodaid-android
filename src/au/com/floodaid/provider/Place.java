package au.com.floodaid.provider;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import au.com.floodaid.util.LocationUtils;

/**
 * Place DTO used for database mapping
 * 
 * @author hsterin
 */
public class Place implements BaseColumns, Comparable<Place>, Parcelable {

	// Constants used to define the database fields
	public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String POSTCODE = "postcode";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String PHONE = "phone";
    public static final String WEBSITE = "website";
    public static final String DETAILS = "details";
    public static final String START_TIME = "starttime";
    public static final String END_TIME = "endtime";
    
    // Computed field, not stored in the database
    public static final String DISTANCE = "distance";
    public static final String RATING = "rating";

    // Local vars
    private String id;
    private String name;
    private String address;
    private String postcode;
    private double lat;
    private double lng;
    private String phone;
    private String website;
    private String details;
    private long starttime;
    private long endtime;
    private int rating;
    
    // Location used to calculate distance
    private Location location;
    
    // Distance with a reference location - used to sort a list of Places
    private double distance; 
    
    /**
     * Default constructor
     */
    public Place() {
    
    }

    /**
     * Complete constructor
     */
	public Place(String id, String name, String address, String postcode, double lat,
			double lng, String phone, String website, String details,
			long starttime, long endtime) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		this.lat = lat;
		this.lng = lng;
		this.phone = phone;
		this.website = website;
		this.details = details;
		this.starttime = starttime;
		this.endtime = endtime;
		
		// Build a location object so we can use the built-in distanceTo() to order results
		this.location = new Location(LocationManager.NETWORK_PROVIDER);
		this.location.setLatitude(lat);
		this.location.setLongitude(lng);
	}
	
	/**
     * Cconstructor with geolocation - lat and lng are determined based on address
     */
	public Place(Context ctx, String name, String address, String postcode, String phone, String website, String details,
			long starttime, long endtime) {
		super();
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		
		this.phone = phone;
		this.website = website;
		this.details = details;
		this.starttime = starttime;
		this.endtime = endtime;
		
		// Geolocate address if coordinates were not passed
		if (lat == 0 || lng == 0) {
			Location location = LocationUtils.getLocationFromAddress(ctx, this.address);
			if (location != null) {
				this.lat = location.getLatitude();
				this.lng = location.getLongitude();
			}
		}
		
		// Build a location object so we can use the built-in distanceTo() to order results
		this.location = new Location(LocationManager.NETWORK_PROVIDER);
		this.location.setLatitude(lat);
		this.location.setLongitude(lng);
	}
	
	/**
     * Constructor for TA format
     */
	public Place(int id, String description, String website, double lng, double lat) {
		super();
		this.name = description;
		this.details = description;
		this.website = website;
		this.lat = lat;
		this.lng = lng;
		
		// Build a location object so we can use the built-in distanceTo() to order results
		this.location = new Location(LocationManager.NETWORK_PROVIDER);
		this.location.setLatitude(lat);
		this.location.setLongitude(lng);
	}
	
	/**
	 * Calculate the distance between the location of this Place and an external reference location
	 * 
	 * @param refLocation
	 */
	public void calculateDistance(Location refLocation) {
		this.distance = refLocation.distanceTo(this.location);
	}
	
	/**
	 * Custom comparator that uses the distance to sort a list of Places 
	 */
	@Override
	public int compareTo(Place other) {
		if (other == null) return 1;
		if (this.distance == other.distance) {
			return 0;
		}
		return (this.distance > other.distance) ? 1 : -1;
	}
	
	/**
	 * Constructor used when the data is put into a parcel
	 */
	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
	
	/**
     * Create from a parcel, the order needs to be the same than in writeToParcel()
     * 
     * @param in
     */
    private Place(Parcel in) {
    	super();
		this.id = in.readString();
		this.name = in.readString();
		this.address = in.readString();
		this.postcode = in.readString();
		this.lat = in.readDouble();
		this.lng = in.readDouble();
		this.phone = in.readString();
		this.website = in.readString();
		this.details = in.readString();
		this.starttime = in.readLong();
		this.endtime = in.readLong();
		this.distance = in.readDouble();
		this.location = in.readParcelable(null);
    }
    
    /**
     * Write the data into a parcel
     * Similar to serializing
     */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(postcode);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeString(phone);
		dest.writeString(website);
		dest.writeString(details);
		dest.writeLong(starttime);
		dest.writeLong(endtime);
		dest.writeDouble(distance);
		dest.writeParcelable(location, flags);
	}
	
	/**
	 * Method required to implement Parcelable
	 */
    @Override
	public int describeContents() {
		return 0;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}
}