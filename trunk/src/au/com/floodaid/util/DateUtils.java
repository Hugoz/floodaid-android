package au.com.floodaid.util;

import java.util.Date;

/**
 * Utility class to do calculations on dates 
 * 
 * @author hsterin
 */
public class DateUtils {

	/**
	 * Calculate the time in ms between the given timestamp and now.
	 * 
	 * @param creationTime timestamp
	 *  
	 * @return Age in ms
	 */
	public static long getAge(long creationTime) {
		Date now = new Date();
		return (now.getTime() - creationTime);
	}
}
