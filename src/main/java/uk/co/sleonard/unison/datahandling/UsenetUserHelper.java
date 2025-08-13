/**
 * UsenetUserHelper
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison.datahandling;

import lombok.extern.slf4j.Slf4j;

import uk.co.sleonard.unison.datahandling.DAO.EmailAddress;
import uk.co.sleonard.unison.input.NewsArticle;

/**
 * The Class UsenetUserHelper.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
@Slf4j
class UsenetUserHelper {

	/**
	 * Augment data and create user.
	 *
	 * @param name
	 *            the name
	 * @param email
	 *            the email
	 * @param gender
	 *            the gender
	 * @param ipAddress
	 *            the ip address
	 * @return the email address
	 */
	private static EmailAddress augmentDataAndCreateUser(final String nameInput,
	        final String emailInput, final String gender, final String ipAddressInput) {
		EmailAddress emailAddress = null;
		String name = nameInput;
		String email = emailInput;
		String ipAddress = ipAddressInput;
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
				}
				else {
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

	/**
	 * Creates the user by removing brackets.
	 *
	 * @param emailString
	 *            the email string
	 * @param leftBracketString
	 *            the left bracket string
	 * @param rightBracketString
	 *            the right bracket string
	 * @param ipAddress
	 *            the ip address
	 * @return the email address
	 */
	private static EmailAddress createUserByRemovingBrackets(final String emailString,
	        final String leftBracketString, final String rightBracketString,
	        final String ipAddress) {

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
				final String inBrackets = emailString.substring(leftBracket + 1, rightBracket);
				String outsideBrackets = null;

				// if email is first, then take name from after it
				if (leftBracket == 0) {
					outsideBrackets = emailString.substring(rightBracket + 1).trim();
				}
				else {
					outsideBrackets = emailString.substring(0, leftBracket).trim();
				}

				int atIndex = inBrackets.indexOf("@");
				if (atIndex > -1) {
					email = inBrackets;
					name = outsideBrackets;
				}
				else {
					atIndex = outsideBrackets.indexOf("@");
					if (atIndex > -1) {
						email = outsideBrackets;
						name = inBrackets;
					}
				}

				emailAddress = UsenetUserHelper.augmentDataAndCreateUser(name, email, gender,
				        ipAddress);
			}
		}
		catch (final Exception e) {
			log
			        .warn("Couldn't parse " + emailString + " so using it for name and email", e);
			emailAddress = new EmailAddress(emailString, emailString, ipAddress);

		}
		return emailAddress;
	}

	static EmailAddress parseFromField(final NewsArticle article) {
		return UsenetUserHelper.parseFromField(article.getFrom(), article.getPostingHost());
	}

	/**
	 * Parses the from field.
	 *
	 * @param emailString
	 *            the email string
	 * @param ipAddress
	 *            the ip address
	 * @return the email address
	 */
	static EmailAddress parseFromField(final String emailStringInput, final String ipAddress) {
		String emailString = emailStringInput;
		log.debug("createUser: " + emailString + " " + ipAddress);

		emailString = emailString.replaceAll("\"", "");

		EmailAddress emailAddress = UsenetUserHelper.createUserByRemovingBrackets(emailString, "<",
		        ">", ipAddress);

		if (null != emailAddress) {
			return emailAddress;
		}

		emailAddress = UsenetUserHelper.createUserByRemovingBrackets(emailString, "(", ")",
		        ipAddress);

		if (null != emailAddress) {
			return emailAddress;
		}

		emailAddress = UsenetUserHelper.augmentDataAndCreateUser(null, emailString, null,
		        ipAddress);

		return emailAddress;
	}

}
