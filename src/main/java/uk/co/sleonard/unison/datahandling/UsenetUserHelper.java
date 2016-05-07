package uk.co.sleonard.unison.datahandling;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;

public class UsenetUserHelper {
	private static Logger logger = Logger.getLogger("PopulateUsenetUser");

	private static EmailAddress augmentDataAndCreateUser(String name,
			String email, final String gender, String ipAddress) {
		EmailAddress emailAddress = null;

		if ((null == ipAddress) || ipAddress.equals("")) {
			ipAddress = "UNKNOWN";
		}

		// TODO throw exception here instead?
		// can only create if have either name or email
		if ((null != email) || (null != name)) {
			// create name from email if missing
			if ((null == name) || name.equals("")) {
				// if just email address
				final int atIndex = email.indexOf("@");
				if (atIndex > -1) {
					name = email.substring(0, atIndex).trim();
				} else {
					name = email.trim();
					email = null;
				}
			}
			// create email from name and ipAddress if missing
			if ((null == email) || email.equals("")) {
				email = name + "@" + ipAddress;
			}

			emailAddress = new EmailAddress(name, email, ipAddress);
		}
		return emailAddress;

	}

	public static EmailAddress parseFromField(String emailString,
			final String ipAddress) {

		UsenetUserHelper.logger.debug("createUser: " + emailString + " "
				+ ipAddress);

		emailString = emailString.replaceAll("\"", "");

		EmailAddress emailAddress = UsenetUserHelper
				.createUserByRemovingBrackets(emailString, "<", ">", ipAddress);

		if (null != emailAddress) {
			return emailAddress;
		}

		emailAddress = UsenetUserHelper.createUserByRemovingBrackets(
				emailString, "(", ")", ipAddress);

		if (null != emailAddress) {
			return emailAddress;
		}

		emailAddress = UsenetUserHelper.augmentDataAndCreateUser(null,
				emailString, null, ipAddress);

		return emailAddress;
	}

	private static EmailAddress createUserByRemovingBrackets(
			final String emailString, final String leftBracketString,
			final String rightBracketString, final String ipAddress) {

		String name = null;
		String email = null;
		final String gender = null;
		EmailAddress emailAddress = null;

		try {
			// if in format "name" <email@domain.com>
			final int leftBracket = emailString.indexOf(leftBracketString);
			int rightBracket = emailString.lastIndexOf(rightBracketString);
			if (leftBracket > -1) {
				if (rightBracket < 0) {
					rightBracket = emailString.length();
				}
				final String inBrackets = emailString.substring(
						leftBracket + 1, rightBracket);
				String outsideBrackets = null;

				// if email is first, then take name from after it
				if (leftBracket == 0) {
					outsideBrackets = emailString.substring(rightBracket + 1)
							.trim();
				} else {
					outsideBrackets = emailString.substring(0, leftBracket)
							.trim();
				}

				int atIndex = inBrackets.indexOf("@");
				if (atIndex > -1) {
					email = inBrackets;
					name = outsideBrackets;
				} else {
					atIndex = outsideBrackets.indexOf("@");
					if (atIndex > -1) {
						email = outsideBrackets;
						name = inBrackets;
					}
				}

				emailAddress = UsenetUserHelper.augmentDataAndCreateUser(name,
						email, gender, ipAddress);
			}
		} catch (Exception e) {
			logger.warn("Couldn't parse " + emailString
					+ " so using it for name and email");
			emailAddress = new EmailAddress(emailString, emailString, ipAddress);

		}
		return emailAddress;
	}

}
