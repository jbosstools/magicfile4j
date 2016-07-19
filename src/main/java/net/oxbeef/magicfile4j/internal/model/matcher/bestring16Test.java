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

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.TestableNode;
import net.oxbeef.magicfile4j.internal.offset.StringUtils;

/**
 * A two byte string in unicode  - UCS16  
 * We'll return the data raw,  and let our matches method reverse it
 */
public class bestring16Test extends stringTest {
	public byte[] getValue(TestableNode magic, byte[] bytearray) {
		int o = magic.resolveOffset(bytearray);
		int len = getLength(o, bytearray);
		byte[] ret = new byte[len];
		System.arraycopy(bytearray, o, ret, 0, len);
		return ret;
	}

	private int getLength(int offset, byte[] bytes) {
		int len;
		for (len = offset; len < bytes.length - 1; len += 2) {
			if (bytes[len] == 0 && bytes[len + 1] == 0) {
				return len;
			}
		}
		return -1;
	}
	
	protected char toChar(int f, int s) {
		return (char) (f << 8 + s);
	}
	
	protected boolean compare(String test, byte[] dataAtOffset) {
		char[] testArr = StringUtils.getEscapedCharacterArray(test, false);
		char[] foundArr = toCharArr(dataAtOffset);
		for( int i = 0; i < testArr.length; i++ ) {
			if( i < foundArr.length ) {
				if( testArr[i] != foundArr[i]) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected char[] toCharArr(byte[] dataAtOffset) {
		char[] ret = new char[dataAtOffset.length/2];
		for( int i = 0; i < (dataAtOffset.length / 2); i++ ) {
			char atOffset = toChar(dataAtOffset[2*i], dataAtOffset[(2*i)+1]);
			ret[i] = atOffset;
		}
		return ret;
	}
	
	@Override
	public String formatString(Magic m, String out, byte[] val) {
		String s = new String(toCharArr(val));
		return String.format(out, s);
	}
}