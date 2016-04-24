package uk.co.sleonard.unison.datahandling;

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

	public static String join(String[] strings, String delimiter) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < strings.length - 1; i++) {
			buf.append(strings[i] + delimiter);
		}
		buf.append(strings[strings.length - 1]);

		return buf.toString();
	}

	public static List<String> convertCommasToList(String commaSeparatedString) {
		Vector<String> words = new Vector<String>();

		StringTokenizer tok = new StringTokenizer(commaSeparatedString, ",");
		while (tok.hasMoreTokens()) {
			words.add(tok.nextToken());
		}
		return words;
	}

	public static final byte[] compress(String str) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(out);
		zout.putNextEntry(new ZipEntry("0"));
		zout.write(str.getBytes());
		zout.closeEntry();
		byte[] compressed = out.toByteArray();
		zout.close();
		return compressed;
	}

	public static final String decompress(byte[] compressed) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(compressed);
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry entry = zin.getNextEntry();
		byte[] buffer = new byte[1024];
		int offset = -1;
		while ((offset = zin.read(buffer)) != -1) {
			out.write(buffer, 0, offset);
		}
		String decompressed = out.toString();
		out.close();
		zin.close();
		return decompressed;
	}

	public static final void main(String[] args) {
		String[] words = new String[] {"one", "two", "three" };
		String result = join(words, ", ");
		System.out.println("Created : " + result);
		
		String str = "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n"
				+ "Compress this string\r\n" + "Compress this string\r\n";
		try {
			byte[] compressed = compress(str);
			int compressedSize = compressed.length;
			String decompressed = decompress(compressed);
			int decompressedSize = decompressed.getBytes().length;
			System.out.println("Compressed Size = " + compressedSize);
			System.out.println("Decompressed Size = " + decompressedSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
