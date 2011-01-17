package au.com.floodaid.util;

import java.util.regex.Pattern;

public class FormValidator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String POSTCODE_PATTERN = "^[0-9]{4}";
	private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{10,13}";

	/**
	 * Validate email address
	 * @param emailAddress for validation
	 * @return true valid email, false invalid email
	 */
	public static boolean validateEmail(final String emailAddress) {
		return validate(EMAIL_PATTERN, emailAddress);
	}

	/**
	 * Validate phone number
	 * @param postcode for validation
	 * @return true valid postcode, false invalid postcode
	 */
	public static boolean validatePostcode(final String postcode) {
		return !validate(POSTCODE_PATTERN, postcode);
	}

	public static boolean validatePhoneNumber(final String phoneNumber) {
		return !validate(PHONE_NUMBER_PATTERN, phoneNumber);
	}

	/**
	 * Validator
	 * @return true if criterias are valid
	 */
	public static boolean validate(String regexp, String input) {
		return Pattern.matches(regexp, input);
	}

}
