/*******************************************************************************
 * ${licenseText}
 * All rights reserved. This file is made available under the terms of the 
 * Common Development and Distribution License (CDDL) v1.0 which accompanies 
 * this distribution, and is available at 
 * http://www.opensource.org/licenses/cddl1.txt     
 *******************************************************************************/
package net.sf.mcf2pdf.mcfelements.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * InputStream filter for the ugly CLP format. <br>
 * This format is a somewhat "obfuscated" SVG file. Every character of the SVG 
 * file is written as hexadecimal representation of the byte value of that 
 * character, and from time to time, random characters which cannot be interpreted 
 * as hexadecimal are inserted. The first character is always an "a" which
 * also has to be ignored (although this would be a valid hexadecimal character). <br>
 * This class is not optimized for speed. It is kept as simple as possible, as 
 * CLP files tend to be rather small.
 */
public class ClpInputStream extends FilterInputStream {

	private boolean firstByte = true;
	
	public ClpInputStream(InputStream in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		if (firstByte) {
			// first byte has to be ignored and must be "a"
			int b = in.read();
			if (b == -1)
				return -1; // okay - empty stream
			if (b != 0x61)
				throw new IOException("CLP data must start with byte 0x61");
			firstByte = false;
		}
		
		// read two bytes, interpret them as a string and convert that string
		// to a hexadecimal number. Ignore non-hexadecimal characters.
		int i1 = readValidCharacter();
		if (i1 == -1)
			return -1;
		int i2 = readValidCharacter();
		if (i2 == -1)
			throw new EOFException("Unexpected end of CLP data");
		
		char c1 = (char)i1;
		char c2 = (char)i2;
		String s = new StringBuilder().append(c1).append(c2).toString();
		
		try {
			return Integer.valueOf(s, 16).intValue();
		}
		catch (NumberFormatException nfe) {
			// should not occur due to checks in readValidCharacter()
			throw new IOException("Invalid character sequence found: " + s);
		}
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		// fallback to simple read()
		// TODO replace with an optimized version
		
		int cnt = 0;
		for (int i = 0; i < len; i++) {
			int n = read();
			if (n == -1)
				return (cnt == 0 ? -1 : cnt);
			b[off + i] = (byte)n;
			cnt++;
		}
		
		return cnt == 0 ? -1 : cnt;
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	private static Charset CS_ISO = Charset.forName("ISO-8859-1");
	
	private int readValidCharacter() throws IOException {
		String s;
		char c;
		do {
			int b = in.read();
			if (b == -1)
				return -1;
			ByteBuffer bb = ByteBuffer.wrap(new byte[] { (byte)b });
			c = CS_ISO.decode(bb).charAt(0);
			s = "" + c;
			if (!s.matches("[0-9a-zA-Z]"))
				throw new IOException("Unexpected character found in CLP data: " + s);
		}
		while (!s.matches("[0-9a-fA-F]"));
		return c; 
	}

}
