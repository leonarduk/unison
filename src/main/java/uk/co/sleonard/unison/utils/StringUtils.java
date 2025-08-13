/**
 * StringUtils
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Logger;

import uk.co.sleonard.unison.UNISoNException;

/**
 * The Class StringUtils. </br>
 * </br>
 * TODO Add this reference for the compression FRom
 * http://forum.java.sun.com/thread.jspa?threadID=250124&messageID=926638
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class StringUtils {

        /** Logger for this class. */
        private static final Logger LOG = Logger.getLogger(StringUtils.class);

        static final String[] DATE_SEPARATORS = { "/", "-", ".", "," };

	/**
	 * Compress.
	 *
	 * @param str
	 *            the str
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static final byte[] compress(final String str) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (final ZipOutputStream zout = new ZipOutputStream(out);) {
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			final byte[] compressed = out.toByteArray();
			return compressed;
		}
	}

	/**
	 * Convert commas to list.
	 *
	 * @param commaSeparatedString
	 *            the comma separated string
	 * @return the list
	 */
	static List<String> convertCommasToList(final String commaSeparatedString) {
		final Vector<String> words = new Vector<>();

		final StringTokenizer tok = new StringTokenizer(commaSeparatedString, ",");
		while (tok.hasMoreTokens()) {
			words.add(tok.nextToken());
		}
		return words;
	}

	/**
	 * Convert date to string.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	/*
	 * Returns an NNTP-format date string. This is only required when clients use the NEWGROUPS or
	 * NEWNEWS methods, therefore rarely: we don't cache any of the variables here.
	 */
	public static String convertDateToString(final Date date) {
		final String NNTP_DATE_FORMAT = "yyyyMMdd HHmmss 'GMT'";

		final DateFormat df = new SimpleDateFormat(NNTP_DATE_FORMAT);
		final Calendar cal = new GregorianCalendar();
		final TimeZone gmt = TimeZone.getTimeZone("GMT");
		cal.setTimeZone(gmt);
		df.setCalendar(cal);
		cal.setTime(date);
		return df.format(date);
	}

	/**
	 * Convert string to list.
	 *
	 * @param field
	 *            the field
	 * @param delimiters
	 *            the delimiters
	 * @return the vector
	 */
	public static Vector<String> convertStringToList(final String field, final String delimiters) {
		final Vector<String> list = new Vector<>();
		if (null == field) {
			return list;
		}
		final StringTokenizer fields = new StringTokenizer(field, delimiters);

		while (fields.hasMoreElements()) {
			final String nextToken = fields.nextToken();
			list.add(nextToken);
			// System.out.println("Adding " + nextToken);
		}
		return list;
	}

	/**
	 * Decompress.
	 *
	 * @param compressed
	 *            the compressed
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static final String decompress(final byte[] compressed) throws IOException {
		if (null == compressed) {
			return null;
		}
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
		        final ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		        final ZipInputStream zin = new ZipInputStream(in);) {
			final ZipEntry entry = zin.getNextEntry();
			entry.getName();
			final byte[] buffer = new byte[1024];
			int offset = zin.read(buffer);
			while (offset != -1) {
				out.write(buffer, 0, offset);
				offset = zin.read(buffer);
			}
			final String decompressed = out.toString();
			out.close();
			zin.close();
			return decompressed;
		}
	}

	/**
	 * Join.
	 *
	 * @param strings
	 *            the strings
	 * @param delimiter
	 *            the delimiter
	 * @return the string
	 */
       public static String join(final String[] strings, final String delimiter) {
               if ((strings == null) || (strings.length == 0)) {
                       return "";
               }
               final StringBuilder buf = new StringBuilder();
               for (int i = 0; i < strings.length; i++) {
                       if (i > 0) {
                               buf.append(delimiter);
                       }
                       buf.append(strings[i]);
               }

               return buf.toString();
       }

	/**
	 * Read the properties file on src/main/resource and process to String Array.
	 *
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * @return Return the server list on Array
	 */
        public static String[] loadServerList() {

                final String file = "servers.properties";

                try (final InputStream resources = ClassLoader.getSystemResourceAsStream(file);) {
                        if (null == resources) {
                                throw new IOException("can't find " + file);
                        }
                        return loadServerList(resources);
                }
                catch (final IOException io) {
                        LOG.error("Unable to load server list", io);
                        return new String[0];
                }
        }

        /**
         * Load server list from an input stream.
         *
         * @param resources
         *            the input stream
         * @return the server list on Array
         * @throws IOException
         *             if there is a problem reading from the stream
         */
        public static String[] loadServerList(final InputStream resources) throws IOException {

                if (resources == null) {
                        return new String[0];
                }

                final Properties prop = new Properties();
                prop.load(resources);
                final String servers = prop.getProperty("servers");
                if (servers == null) {
                        return new String[0];
                }
                final List<String> list = StringUtils.convertCommasToList(servers);
                return list.toArray(new String[0]);
        }

        /**
         * Load server list from a properties file path.
         *
         * @param path
         *            path to the properties file
         * @return the server list on Array
         * @throws IOException
         *             if there is a problem reading the file
         */
        public static String[] loadServerList(final Path path) throws IOException {
                try (InputStream in = Files.newInputStream(path)) {
                        return loadServerList(in);
                }
        }

	/**
	 * Convert the String with date to Date Object.
	 *
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * @since 11/06/2016
	 * @return Return the date.
	 */
	public static Date stringToDate(final String text) throws UNISoNException {
		StringBuilder pattern = null;

		if ((null == text) || text.equals("")) {
			return null;
		}

		final String yyyy = "yyyy";
		final String dd = "dd";
		final String mm = "MM";

		if (text.length() == 8) {
			try {
				// 20101229
				pattern = new StringBuilder().append(yyyy).append(mm).append(dd);
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
				final LocalDate localDate = LocalDate.parse(text, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			catch (final DateTimeParseException e) {
				// Try other.
			}
			try {
				// 13122010
				pattern = new StringBuilder().append(dd).append(mm).append(yyyy);
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
				final LocalDate localDate = LocalDate.parse(text, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			catch (final DateTimeParseException e) {
				// Try other.
			}
			try {
				// 12201601
				pattern = new StringBuilder().append(mm).append(yyyy).append(dd);
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
				final LocalDate localDate = LocalDate.parse(text, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			catch (final DateTimeParseException e) {
				throw new UNISoNException("Failed to parse date:" + text, e);
			}
		}
		else if (text.length() == 10) {
			for (final String separator : StringUtils.DATE_SEPARATORS) {
				if (text.contains(separator)) {
					try {
						// Ex.12/05/1994
						pattern = new StringBuilder().append(dd).append(separator).append(mm)
						        .append(separator).append(yyyy);
						final DateTimeFormatter formatter = DateTimeFormatter
						        .ofPattern(pattern.toString());
						final LocalDate localDate = LocalDate.parse(text, formatter);
						return Date
						        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					}
					catch (final DateTimeParseException e) {
					}
					try {
						// Ex.1994/12/05
						pattern = new StringBuilder().append(yyyy).append(separator).append(mm)
						        .append(separator).append(dd);
						final DateTimeFormatter formatter = DateTimeFormatter
						        .ofPattern(pattern.toString());
						final LocalDate localDate = LocalDate.parse(text, formatter);
						return Date
						        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					}
					catch (final DateTimeParseException e) {
					}
					try {
						// Ex.12/1994/05
						pattern = new StringBuilder().append(mm).append(separator).append(yyyy)
						        .append(separator).append(dd);
						final DateTimeFormatter formatter = DateTimeFormatter
						        .ofPattern(pattern.toString());
						final LocalDate localDate = LocalDate.parse(text, formatter);
						return Date
						        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					}
					catch (final DateTimeParseException e) {
					}
				}
			}
			throw new UNISoNException("Failed to parse date:" + text);
		}
		else {
			String dateText = text.trim();
			if (dateText.substring(0, 1).matches("[a-zA-Z]") && dateText.contains("(")) {
				// Sun, 18 Jan 2015 23:40:56 +0000 (UTC)
				// TRIM TO
				// 18 Jan 2015 23:40:56 +0000
				dateText = dateText.substring(4, dateText.indexOf("(")).trim();

				final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
				final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
				return Date.from(zonedDateTime.toInstant());
			}
                        else if (dateText.substring(0, 1).matches("[a-zA-Z]")) {
                                dateText = dateText.substring(4, dateText.length()).trim();
                                // Sun, 18 Jan 2015 23:40:56 +0000
                                // TRIM TO
                                // 18 Jan 2015 23:40:56 +0000
                                final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                                final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
                                return Date.from(zonedDateTime.toInstant());
                        }
                        else {
                                // 30 Jan 2015 23:37:13 GMT
                                final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                                final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
                                return Date.from(zonedDateTime.toInstant());
                        }
                }
        }

}
