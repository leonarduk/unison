/**
 * StringUtils
 *
 * @author Stephen <github@leonarduk.com>
 * @since 22-May-2016
 */
package uk.co.sleonard.unison.utils;

import lombok.extern.slf4j.Slf4j;
import uk.co.sleonard.unison.UNISoNException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The Class StringUtils. </br>
 * </br>
 * Compression algorithm adapted from a discussion on the Sun Java forums:
 * http://forum.java.sun.com/thread.jspa?threadID=250124&messageID=926638
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Slf4j
public class StringUtils {

    /**
     * Default server list used when no properties file is found.
     */
    private static final String[] DEFAULT_SERVERS = {"news.karlsruhe.org"};

    static final String[] DATE_SEPARATORS = {"/", "-", ".", ","};

    /**
     * Compress.
     *
     * @param str the str
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
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
     * @param commaSeparatedString the comma separated string
     * @return the list
     */
    static List<String> convertCommasToList(final String commaSeparatedString) {
        if (commaSeparatedString == null || commaSeparatedString.isEmpty()) {
            return new ArrayList<>();
        }

        var tokens = commaSeparatedString.split(",");
        return Stream.of(tokens).collect(Collectors.toList());
    }

    /**
     * Convert date to string.
     *
     * @param date the date
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
     * @param field      the field
     * @param delimiters the delimiters
     * @return the list
     */
    public static List<String> convertStringToList(final String field, final String delimiters) {
        var list = new ArrayList<String>();
        if (field == null) {
            return list;
        }
        if (delimiters == null || delimiters.isEmpty()) {
            list.add(field);
            return list;
        }
        var regex = "[" + Pattern.quote(delimiters) + "]";
        var fields = field.split(regex);

       for (final String nextToken : fields) {
            list.add(nextToken);
            log.debug("Adding {}", nextToken);
        }
        return list;
    }

    /**
     * Decompress.
     *
     * @param compressed the compressed
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Optional<String> decompress(final byte[] compressed) throws IOException {
        if (compressed == null) {
            return Optional.empty();
        }
        try (var out = new ByteArrayOutputStream();
             var in = new ByteArrayInputStream(compressed);
             var zin = new ZipInputStream(in)) {
            var entry = zin.getNextEntry();
            entry.getName();
            var buffer = new byte[1024];
            for (var offset = zin.read(buffer); offset != -1; offset = zin.read(buffer)) {
                out.write(buffer, 0, offset);
            }
            var decompressed = out.toString();
            out.close();
            zin.close();
            return Optional.of(decompressed);
        }
    }

    /**
     * Join.
     *
     * @param strings   the strings
     * @param delimiter the delimiter
     * @return the string
     */
    public static String join(final String[] strings, final String delimiter) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        return Stream.of(strings).collect(Collectors.joining(delimiter));
    }

    /**
     * Read the properties file on src/main/resource and process to String Array.
     *
     * @return Return the server list on Array
     * @author Elton <elton_12_nunes@hotmail.com>
     */
    public static String[] loadServerList() {

        final String file = "servers.properties";

        try (final InputStream resources = StringUtils.class.getClassLoader()
                .getResourceAsStream(file);) {
            if (resources != null) {
                return loadServerList(resources);
            }
            log.warn("Server list file '{}' not found, using default server list", file);
            return DEFAULT_SERVERS;
        } catch (final IOException io) {
            log.error("Unable to load server list", io);
            return DEFAULT_SERVERS;
        }
    }

    /**
     * Load server list from an input stream.
     *
     * @param resources the input stream
     * @return the server list on Array
     * @throws IOException if there is a problem reading from the stream
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
     * @param path path to the properties file
     * @return the server list on Array
     * @throws IOException if there is a problem reading the file
     */
    public static String[] loadServerList(final Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return loadServerList(in);
        }
    }

    /**
     * Convert the String with date to Date Object.
     *
     * @return Return the date.
     * @author Elton <elton_12_nunes@hotmail.com>
     * @since 11/06/2016
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
            } catch (final DateTimeParseException e) {
                // Try other.
            }
            try {
                // 13122010
                pattern = new StringBuilder().append(dd).append(mm).append(yyyy);
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
                final LocalDate localDate = LocalDate.parse(text, formatter);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (final DateTimeParseException e) {
                // Try other.
            }
            try {
                // 12201601
                pattern = new StringBuilder().append(mm).append(yyyy).append(dd);
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
                final LocalDate localDate = LocalDate.parse(text, formatter);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (final DateTimeParseException e) {
                throw new UNISoNException("Failed to parse date:" + text, e);
            }
        } else if (text.length() == 10) {
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
                    } catch (final DateTimeParseException e) {
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
                    } catch (final DateTimeParseException e) {
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
                    } catch (final DateTimeParseException e) {
                    }
                }
            }
            throw new UNISoNException("Failed to parse date:" + text);
        } else {
            String dateText = text.trim();
            if (dateText.substring(0, 1).matches("[a-zA-Z]") && dateText.contains("(")) {
                // Sun, 18 Jan 2015 23:40:56 +0000 (UTC)
                // TRIM TO
                // 18 Jan 2015 23:40:56 +0000
                dateText = dateText.substring(4, dateText.indexOf("(")).trim();

                final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
                return Date.from(zonedDateTime.toInstant());
            } else if (dateText.substring(0, 1).matches("[a-zA-Z]")) {
                dateText = dateText.substring(4, dateText.length()).trim();
                // Sun, 18 Jan 2015 23:40:56 +0000
                // TRIM TO
                // 18 Jan 2015 23:40:56 +0000
                final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
                final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
                return Date.from(zonedDateTime.toInstant());
            } else {
                // 30 Jan 2015 23:37:13 GMT or 18 Jan 2015 23:40:56 +0000
                final DateTimeFormatter[] formatters = {
                        DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss z", Locale.ENGLISH),
                        DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)};

                for (final DateTimeFormatter formatter : formatters) {
                    try {
                        final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
                        return Date.from(zonedDateTime.toInstant());
                    } catch (final DateTimeParseException e) {
                        // try next pattern
                    }
                }
                throw new UNISoNException("Failed to parse date:" + text);
            }
        }
    }

}
