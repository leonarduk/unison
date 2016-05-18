package uk.co.sleonard.unison.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The Class StringUtils.
 */
/*
 * //TODO Add this reference for the compression FRom
 * http://forum.java.sun.com/thread.jspa?threadID=250124&messageID=926638
 */
public class StringUtils {

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
		final ZipOutputStream zout = new ZipOutputStream(out);
		zout.putNextEntry(new ZipEntry("0"));
		zout.write(str.getBytes());
		zout.closeEntry();
		final byte[] compressed = out.toByteArray();
		zout.close();
		return compressed;
	}

	/**
	 * Convert commas to list.
	 *
	 * @param commaSeparatedString
	 *            the comma separated string
	 * @return the list
	 */
	public static List<String> convertCommasToList(final String commaSeparatedString) {
		final Vector<String> words = new Vector<String>();

		final StringTokenizer tok = new StringTokenizer(commaSeparatedString, ",");
		while (tok.hasMoreTokens()) {
			words.add(tok.nextToken());
		}
		return words;
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
		final Vector<String> list = new Vector<String>();
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
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		final ZipInputStream zin = new ZipInputStream(in);
		final ZipEntry entry = zin.getNextEntry();
		entry.getName();
		final byte[] buffer = new byte[1024];
		int offset = -1;
		while ((offset = zin.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		final String decompressed = out.toString();
		out.close();
		zin.close();
		return decompressed;
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
		final StringBuffer buf = new StringBuffer();

		for (int i = 0; i < strings.length - 1; i++) {
			buf.append(strings[i] + delimiter);
		}
		buf.append(strings[strings.length - 1]);

		return buf.toString();
	}

	/**
	 * Read the properties file on src/main/resource and process to String Array.
	 *
	 * @author Elton <elton_12_nunes@hotmail.com>
	 * @return Return the server list on Array
	 */
	public static String[] loadServerList() {

		final Properties prop = new Properties();

		try {
			final String file = "servers.properties";
			final InputStream resources = ClassLoader.getSystemResourceAsStream(file);
			if (null == resources) {
				throw new IOException("can't find " + file);
			}
			prop.load(resources);
			final List<String> list = StringUtils.convertCommasToList(prop.getProperty("servers"));

			final String[] listServers = list.toArray(new String[list.size()]);

			return listServers;
		}
		catch (final IOException io) {
			io.printStackTrace();
		}

		return new String[] { "empty" };
	}

}
