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
package net.oxbeef.magicfile4j.model.internal.matcher;

import net.oxbeef.magicfile4j.model.internal.Magic;
import net.oxbeef.magicfile4j.model.internal.TestableNode;
import net.oxbeef.magicfile4j.offset.util.StringUtils;

public class stringTest extends Tester {
	public byte[] getValue(TestableNode magic, byte[] ba) {
		int o = magic.resolveOffset(ba);
		char[] test = StringUtils.getEscapedCharacterArray(((Magic)magic).getTest(), false);
		if( ba.length < (o + test.length)) {
			return null; // current offset + test  is longer than current array, so can't possibly fit. 
		}
		byte[] ret = new byte[test.length];
		System.arraycopy(ba, o, ret, 0, ret.length);
		return ret;
	}
	@Override
	public boolean matches(TestableNode magic, byte[] byteArray, byte[] dataAtOffset) {
		String test = ((Magic)magic).getTest();
		if(test == null || test.isEmpty()) {
			return false; // this should never happen
		}
		if( "x".equals(test)) {
			return true;
		}
		char[] t2 = StringUtils.getEscapedCharacterArray(test, false);
		if( t2.length == dataAtOffset.length) {
			for( int i = 0; i < t2.length; i++ ) {
				if( (byte)t2[i] != (byte)dataAtOffset[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}