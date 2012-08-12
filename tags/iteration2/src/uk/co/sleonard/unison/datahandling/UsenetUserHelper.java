package uk.co.sleonard.unison.datahandling;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.datahandling.DAO.Location;
import uk.co.sleonard.unison.datahandling.DAO.UsenetUser;

public class UsenetUserHelper {
	private static Logger logger = Logger.getLogger("PopulateUsenetUser");

	public static void main(String[] args) {
		UsenetUserHelper test = new UsenetUserHelper();
		test.testEmailToUser("Steve <steve@sleonard.co.uk>", null);
		test.testEmailToUser("<steve@sleonard.co.uk> Steve", "localhost");
		test.testEmailToUser("<steve@sleonard.co.uk>", "localhost");
		test.testEmailToUser("\"Steve\" <steve@sleonard.co.uk>", "localhost");
		test.testEmailToUser("steve@sleonard.co.uk", "localhost");
		test.testEmailToUser("steve@sleonard.co.uk (steve)", "localhost");
		test.testEmailToUser("(steve) steve@sleonard.co.uk ", "localhost");
		test.testEmailToUser("() steve@sleonard.co.uk ", "localhost");
		test.testEmailToUser("steve ", "localhost");

	}

	public static String toString(UsenetUser user) {
		return ("Name:" + user.getName() + " eMail: " + user.getEmail()
				+ " Gender: " + user.getGender() + " IP: " + user
				.getIpaddress());
	}

	private static UsenetUser augmentDataAndCreateUser(String name,
			String email, String gender, String ipAddress) {
		UsenetUser user = null;

		if (null == ipAddress || ipAddress.equals("")) {
			ipAddress = "UNKNOWN";
		}

		// TODO throw exception here instead?
		// can only create if have either name or email
		if (null != email || null != name) {
			// create name from email if missing
			if (null == name || name.equals("")) {
				// if just email address
				int atIndex = email.indexOf("@");
				if (atIndex > -1) {
					name = email.substring(0, atIndex).trim();
				} else {
					name = email.trim();
					email = null;
				}
			}
			// create email from name and ipAddress if missing
			if (null == email || email.equals("")) {
				email = name + "@" + ipAddress;
			}

			user = new UsenetUser(name, email, ipAddress,
					new String("Unknown"), new Location());
			logger.debug(UsenetUserHelper.toString(user));

		}
		return user;

	}

	public static UsenetUser createUsenetUser(String emailString,
			String ipAddress) {

		logger.debug("createUser: " + emailString + " " + ipAddress);

		emailString = emailString.replaceAll("\"", "");

		UsenetUser user = createUserByRemovingBrackets(emailString, "<", ">",
				ipAddress);

		if (null != user) {
			return user;
		}

		user = createUserByRemovingBrackets(emailString, "(", ")", ipAddress);

		if (null != user) {
			return user;
		}

		user = augmentDataAndCreateUser(null, emailString, null, ipAddress);

		return user;
	}

	private static UsenetUser createUserByRemovingBrackets(String emailString,
			String leftBracketString, String rightBracketString,
			String ipAddress) {
		String name = null;
		String email = null;
		String gender = null;
		UsenetUser user = null;

		// if in format "name" <email@domain.com>
		int leftBracket = emailString.indexOf(leftBracketString);
		int rightBracket = emailString.indexOf(rightBracketString);

		if (leftBracket > -1) {
			String inBrackets = emailString.substring(leftBracket + 1,
					rightBracket);
			String outsideBrackets = null;

			// if email is first, then take name from after it
			if (leftBracket == 0) {
				outsideBrackets = emailString.substring(rightBracket + 1)
						.trim();
			} else {
				outsideBrackets = emailString.substring(0, leftBracket).trim();
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

			user = augmentDataAndCreateUser(name, email, gender, ipAddress);
		}
		return user;
	}

	private void testEmailToUser(String emailString, String ipAddress) {
		UsenetUser user = createUsenetUser(emailString, ipAddress);
		logger.info(toString(user));
	}
}
