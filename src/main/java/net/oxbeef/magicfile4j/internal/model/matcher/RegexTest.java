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
package net.oxbeef.magicfile4j.internal.model.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.TestableNode;

public class RegexTest extends Tester {

	private static class RegexData {
		String flags;
		int length = 8*1024;
		Pattern pattern;
		int startOffset;
		String matchedString = null;
	}
	
	
	public Object getValue(TestableNode magic, byte[] bytearray) {
		// where we start looking
		int startOffset = magic.resolveOffset(bytearray);

		String type = magic.getType();
		String[] split = type.split("/");
		
		RegexData rd = new RegexData();
		int patternFlags = 0;
		String size = "";
		if( split.length > 1) {
			StringBuffer sb = new StringBuffer();
			String flags = split[1];
			rd.flags = flags;
			for( int i = 0; i < flags.length(); i++ ) {
				switch(flags.charAt(i)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					sb.append(flags.charAt(i));
					break;
				case 'c':
					patternFlags |= Pattern.CASE_INSENSITIVE;
					break;
					
				}
			}
			size = sb.toString();
			rd.length = Integer.parseInt(size);
		}
		
		Pattern p = Pattern.compile(".*(" + ((Magic)magic).getTest() + ").*", patternFlags);
		rd.pattern = p;
		rd.startOffset = startOffset;
		return rd;
	}
	@Override
	public boolean matches(TestableNode magic, byte[] byteArray, Object dataAtOffset) {
		RegexData rd = (RegexData)dataAtOffset;
		String toSearch = "";
		if( rd.flags != null && rd.flags.contains("l")) {
			toSearch = readLines(byteArray, rd.length, rd.startOffset);
		} else {
			toSearch = readStringAsBytes(byteArray, rd.length, rd.startOffset);
		}
		
		// First search the whole string
		Matcher matcher = rd.pattern.matcher(toSearch);
		String matched = null;
		if (matcher.matches()) {
			matched = matcher.group(1);
		} else {
			// Then split by lines and search each of them instead
			String[] split = toSearch.split("\n");
			for( int i = 0; i < split.length && matched == null; i++ ) {
				matcher = rd.pattern.matcher(toSearch);
				if (matcher.matches()) {
					matched = matcher.group(1);
				}
			}
		}
		
		rd.matchedString = matched;
		return matched != null;
	}
	
	
	public static int nthIndexOf(String source, String sought, int n, int initialOffset) {
	    int index = source.indexOf(sought, initialOffset);
	    if (index == -1) return -1;

	    for (int i = 1; i < n; i++) {
	        index = source.indexOf(sought, index + 1);
	        if (index == -1) return -1;
	    }
	    return index;
	}
	
	// Read a number of lines into a string
	private String readLines(byte[] byteArray, int numLines, int startoffset) {
		String wholeFile = new String(byteArray);
		int nth = nthIndexOf(wholeFile, "\n", numLines, startoffset);
		if( nth != -1 ) {
			return wholeFile.substring(startoffset, nth);
		} else {
			// we didn't find (for example)  20 lines, so return the whole file
			return new String(byteArray, startoffset, byteArray.length - startoffset-1);
		}
	}
	
	// Read a number of bytes into a string
	private String readStringAsBytes(byte[] byteArray, int numBytes, int startoffset) {
		int sizeToCopy = (startoffset + numBytes >= byteArray.length ? byteArray.length - startoffset : numBytes);
		return new String(byteArray, startoffset, sizeToCopy);
	}
	
	
	@Override
	public String formatString(Magic m, String out, Object val) {
		RegexData rd = (RegexData)val;
		return String.format(out, rd.matchedString);
	}
}