/*
 * Copyright (c) 2016 Red Hat, Inc., Rob Stryker, and Contributors
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
package org.jboss.tools.magicfile4j.internal.model.matcher;

import org.jboss.tools.magicfile4j.internal.model.Magic;
import org.jboss.tools.magicfile4j.internal.model.TestableNode;
import org.jboss.tools.magicfile4j.internal.offset.StringUtils;

public class StringTest extends Tester {
	
	protected static class StringTestValue {
		public char[] test;
		public String flags = "";
		public int offset;
		public String output;
		public StringTestValue() {
		}
	}
	
	public Object getValue(TestableNode magic, byte[] ba) {
		int o = magic.resolveOffset(ba);
		char[] test = StringUtils.getEscapedCharacterArray(((Magic)magic).getTest(), false);
		StringTestValue ret = new StringTestValue();
		ret.test = test;
		ret.offset = o;
		int slashInd = magic.getType().indexOf('/');
		if( slashInd != -1) {
			ret.flags = magic.getType().substring(slashInd);
		}
		if( !ret.flags.contains("w") && ba.length < (o + test.length)) {
			// whitespace isn't optional, so there's no way this can fit
			 // current offset + test  is longer than current array, so can't possibly fit.
			return null; 
		}
		return ret;
	}
	@Override
	public boolean matches(TestableNode magic, byte[] byteArray, Object dataAtOffset) {
		String test = ((Magic)magic).getTest();
		if(dataAtOffset == null || test == null || test.isEmpty()) {
			return false; // this should never happen
		}
		if( "x".equals(test)) {
			// TODO need to get the actual output here
			// read until null?
			return true;
		}
		
		StringTestValue data2 = (StringTestValue)dataAtOffset;
		String out = getMatchedOutput(data2.test, byteArray, data2);
		data2.output = out;
		return out != null;
	}
	
	protected String getMatchedOutput(char[] test, byte[] bytes, StringTestValue dataAtOffset) {
		StringBuffer sb = new StringBuffer();
		int dataOffset = dataAtOffset.offset;
		boolean whitespaceOptional = dataAtOffset.flags.contains("w");
		boolean compressWhitespace = dataAtOffset.flags.contains("W");
		boolean upperTestMatchesAll = dataAtOffset.flags.contains("C");
		boolean lowerTestMatchesAll = dataAtOffset.flags.contains("c");
		for( int testOffset = 0; testOffset < test.length; testOffset++ ) {
			
			if (dataOffset >= bytes.length) {
				return null; // premature end without full match
			}

			
			char fromData = (char)bytes[dataOffset++];
			char fromMagic = test[testOffset];
			
			if( Character.isWhitespace(fromMagic) && Character.isWhitespace(fromData) && compressWhitespace) {
				// A bit complicated here. Let's see how many whitespaces each have consecutively 
				int magicConsec = countWhitespace(test, testOffset);
				int dataConsec  = countWhitespace(bytes, dataOffset-1);
				if( magicConsec > dataConsec ) {
					// test is looking for 4 spaces, we found only 3
					return null; 
				}
				sb.append(fromData);
				testOffset += (magicConsec-1);
				dataOffset += (dataConsec-1);
			} else if(fromMagic == fromData) {
				sb.append(fromData);
			} else if( Character.isUpperCase(fromMagic) && upperTestMatchesAll
					&& Character.toLowerCase(fromMagic) == fromData) {
				sb.append(fromData);
			} else if( Character.isLowerCase(fromMagic) && lowerTestMatchesAll
					&& Character.toUpperCase(fromMagic) == fromData) {
				sb.append(fromData);
			} else if( Character.isWhitespace(fromMagic) && whitespaceOptional && !Character.isWhitespace(fromData)) {
				dataOffset--; // move on to next test character
				continue;
			} else {
				// we failed, no match
				return null;
			}
		}
		
		
		String ret = sb.toString();
		if( dataAtOffset.flags.contains("T")) {
			ret = ret.trim();
		}
		return ret;
	}
	
	private int countWhitespace(char[] test, int testOffset) {
		int count = 0;
		for( int i = testOffset; i < test.length; i++ ) {
			if( Character.isWhitespace(test[i])) {
				count++;
			} else {
				return count;
			}
		}
		return count;
	}
	private int countWhitespace(byte[] bytes, int offset) {
		int count = 0;
		for( int i = offset; i < bytes.length; i++ ) {
			if( Character.isWhitespace((char)bytes[i])) {
				count++;
			} else {
				return count;
			}
		}
		return count;
	}
	@Override
	public String formatString(Magic m, String out, Object val2) {
		StringTestValue val = (StringTestValue)val2;
		String s = new String(val.output);
		return String.format(out, s);
	}
}