package au.com.floodaid.util;

import java.util.regex.Matcher;
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
	public boolean validateEmail(final String emailAddress) {
		return validate(EMAIL_PATTERN, emailAddress);
	}

	/**
	 * Validate phone number
	 * @param postcode for validation
	 * @return true valid postcode, false invalid postcode
	 */
	public boolean validatePostcode(final String postcode) {
		return validate(POSTCODE_PATTERN, postcode);
	}

	public boolean validatePhoneNumber(final String phoneNumber) {
		return validate(PHONE_NUMBER_PATTERN, phoneNumber);
	}

	/**
	 * Validator
	 */
	public boolean validate(String regexp, String input) {
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

}
