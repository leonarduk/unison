package uk.co.sleonard.unison.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 * //TODO Add this reference for the compression
 * FRom http://forum.java.sun.com/thread.jspa?threadID=250124&messageID=926638
 */
public class StringUtils {

	public static Vector<String> convertStringToList(final String field,
			final String delimiters) {
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

	public static List<String> convertCommasToList(
			final String commaSeparatedString) {
		final Vector<String> words = new Vector<String>();

		final StringTokenizer tok = new StringTokenizer(commaSeparatedString,
				",");
		while (tok.hasMoreTokens()) {
			words.add(tok.nextToken());
		}
		return words;
	}

	public static final String decompress(final byte[] compressed)
			throws IOException {
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

	public static String join(final String[] strings, final String delimiter) {
		final StringBuffer buf = new StringBuffer();

		for (int i = 0; i < strings.length - 1; i++) {
			buf.append(strings[i] + delimiter);
		}
		buf.append(strings[strings.length - 1]);

		return buf.toString();
	}

	public static final void main(final String[] args) {
		final String[] words = new String[] { "one", "two", "three" };
		final String result = StringUtils.join(words, ", ");
		System.out.println("Created : " + result);

		final String str = "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n";
		try {
			final byte[] compressed = StringUtils.compress(str);
			final int compressedSize = compressed.length;
			final String decompressed = StringUtils.decompress(compressed);
			final int decompressedSize = decompressed.getBytes().length;
			System.out.println("Compressed Size = " + compressedSize);
			System.out.println("Decompressed Size = " + decompressedSize);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
