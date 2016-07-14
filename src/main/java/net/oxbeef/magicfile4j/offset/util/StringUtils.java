/*
 * Copyright Rob Stryker and Contributors (as per source history)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oxbeef.magicfile4j.offset.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import net.oxbeef.magicfile4j.model.internal.IDataTypes;

public class StringUtils implements IDataTypes {


	public static byte[] hex2Bytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static byte[] string2ASCII(String str) {
		char[] buffer = str.toCharArray();
		byte[] b = new byte[buffer.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) buffer[i];
		}
		return b;
	}
	public static byte[] combine( byte[][] arr) {
		int total = 0;
		for( int i = 0; i < arr.length; i++ ) {
			total += arr[i].length;
		}
		byte[] ret = new byte[total];
		int count = 0;
		for( int i = 0; i < arr.length; i++ ) {
			for( int j = 0; j < arr[i].length; j++ ) {
				ret[count++] = arr[i][j];
			}
		}
		return ret;
	}
	/*
	 * Return a long properly formatted in either octal, hex, or decimal
	 */
	public static long stringToLong(String offsetString) {
		if (offsetString.charAt(0) == '0') {
			if(offsetString.length() > 1 &&  (offsetString.charAt(1) == 'x' || offsetString.charAt(1) == 'X')) {
				String ss = offsetString.substring(2);
				try {
					return Long.parseLong(ss, 16);
				} catch(NumberFormatException nfe) {
					// It's probably out of bounds, if it's a quad, so let's 
					// try hex2bytes and get a long from that
					byte[] bytes = hex2Bytes(ss);
					ByteBuffer bb = ByteBuffer.allocate(8).wrap(bytes).order(ByteOrder.BIG_ENDIAN);
					long l = bb.getLong();
					return l;
				}
			}
			return Long.parseLong(offsetString, 8);
		}
		return Long.parseLong(offsetString);
	}
	
	public static byte[] numericStringToBytes(String str) {
		BigInteger bi = null;
		if (str.charAt(0) == '0') {
			if(str.length() > 1 &&  (str.charAt(1) == 'x' || str.charAt(1) == 'X')) {
				String ss = str.substring(2);
				bi =  new BigInteger(ss, 16);
			} else {
				bi =  new BigInteger(str, 8);
			}
		} else {
			bi = new BigInteger(str);
		}
		return bi.toByteArray();
	}

	public static long stringToLongUnknownLength(String str, int startIndex) {
		for (int i = startIndex; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'x':
				break;
			default:
				return stringToLong(str.substring(startIndex, i));
			}
		}
		return 0;
	}
	
	
	/**
	 * Very simple logic to find the first unescaped space index in the line. 
	 * This will not resolve any hex or octal or escaped values. 
	 * 
	 * 
	 * @param line
	 * @return
	 */
	public static int findFirstUnescapedSpace(String line) {
		int ind = 0;
		while (ind < line.length()) {
			char c = line.charAt(ind++); // c is char at index, THEN index
											// incremented
			if (isspace(c)) {
				break; // loop over, found the space
			}
			if (c == '\\') {
				ind++;
			}
		}
		ind--;
		return ind;
	}
	

	/**
	 * Get a string until whitespace, while handling escaped characters, 
	 * or hex or octal embedded inside.  
	 * 
	 * return a character array with all bytes that must be matched
	 */
	public static char[] getEscapedCharacterArray(String line, boolean regex) {
		CharBuffer buffer = CharBuffer.allocate(100); // line.length()
		int ind = 0;
		boolean abort = false;
		while (!abort && ind < line.length()) {
			char c = line.charAt(ind++); // c is char at index, THEN index
											// incremented
			if (isspace(c)) {
				break; // loop over, found the space
			}
			if (c == '\\') {
				c = line.charAt(ind++); // c is at index, THEN incremented
				switch (c) {
				case '\0':
					abort = true;
					break;
				case '\t':
				default:
					String nextc2 = new Character(c).toString();
					if ("<>&^=!".contains(nextc2) || (regex && "[]().*?^$|{}".contains(nextc2))) {
						// need to warn somehow
					}
				case ' ':
					/* Relations */
				case '>':
				case '<':
				case '&':
				case '^':
				case '=':
				case '!':
					/* and baskslash itself */
				case '\\':
					buffer.append(c);
					break;
				// \a and \v don't work in java?
				case 'b':
					buffer.append('\b');
					break;

				case 'f':
					buffer.append('\f');
					break;

				case 'n':
					buffer.append('\n');
					break;

				case 'r':
					buffer.append('\r');
					break;

				case 't':
					buffer.append('\t');
					break;

				/* \ and up to 3 octal digits */
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					int val = c - '0';
					if( ind < line.length() -1) {
						c = line.charAt(ind++);
						if (c >= '0' && c <= '7') {
							val = (val << 3) | (c - '0');
							if( ind < line.length() -1) {
								c = line.charAt(ind++);
								if (c >= '0' && c <= '7')
									val = (val << 3) | (c - '0');
								else
									--ind;
							}
						} else
							--ind;
					}
					buffer.append((char)val);
					break;
				/* \x and up to 2 hex digits */
				case 'x':
					int total = -1;
					if( ind < line.length()) {
						c = line.charAt(ind++); // c is char after 'x'
						total = hextoint(c); // total is int val
						if (total >= 0) {
							if( ind < line.length()) {
								c = line.charAt(ind++); // c is 2nd digit
								int v = hextoint(c);
								if (v >= 0)
									total = (total << 4) + v;
								else
									--ind;
							}
						} else
							--ind;
					} else {
						// line reads \x   with no numbers after it.  Should error here
					}
					if (total > -1) {
						buffer.append((char) total);
					} else {
						buffer.append('x');
					}
					break;
				}

			} else {
				buffer.append(c);
			}
		}
		buffer.flip();
		char[] arr = buffer.array();
	    final int arrayOffset = buffer.arrayOffset();
	    return Arrays.copyOfRange(arr, arrayOffset + buffer.position(),
	                              arrayOffset + buffer.limit());
	}
	
	/* Single hex char to int; -1 if not a hex char. */
	public static int hextoint(int c) {
		if (!isascii(c))
			return -1;
		if (isdigit(c))
			return c - '0';
		if ((c >= 'a') && (c <= 'f'))
			return c + 10 - 'a';
		if ((c >= 'A') && (c <= 'F'))
			return c + 10 - 'A';
		return -1;
	}
	

	/* Single hex char to int; -1 if not a hex char. */
	public static boolean isascii(int c) {
		return c >= 0 && c <= 0177;
	}

	public static boolean isdigit(int c) {
		return c >= '0' && c <= '9';
	}
	
	
	public static  boolean isFloatType(String type) {
		return MAGIC_FLOAT.equals(type) || MAGIC_BEFLOAT.equals(type) || MAGIC_LEFLOAT.equals(type);
	}
	
	public static  boolean isDoubleType(String type) {
		return MAGIC_DOUBLE.equals(type) || MAGIC_BEDOUBLE.equals(type) || MAGIC_LEDOUBLE.equals(type);
	}
	
	public static  boolean isspace(char c) {
		return new Character(c).toString().trim().isEmpty();
	}

	public static  ArrayList<String> stringTypes = null;
	public static  boolean isStringType(String type) {
		if( stringTypes == null ) {
			stringTypes = new ArrayList<String>();
			stringTypes.add( MAGIC_BESTRING16);
			stringTypes.add( MAGIC_LESTRING16);
			stringTypes.add( MAGIC_STRING);
			stringTypes.add( MAGIC_PSTRING);
			stringTypes.add( MAGIC_REGEX);
			stringTypes.add( MAGIC_SEARCH);
			//stringTypes.add( MAGIC_DER);
		}
		return stringTypes.contains(type);
	}

	public static boolean isNamedType(String type) {
		return MAGIC_NAME.equals(type) || MAGIC_USE.equals(type);
	}
}
