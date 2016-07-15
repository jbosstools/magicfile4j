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

import java.nio.ByteOrder;

import net.oxbeef.magicfile4j.internal.model.TestableNode;

public class pstringTest extends stringTest {
	
	private static final String PSTRING_B = "pstring/B";
	private static final String PSTRING_H = "pstring/H";
	private static final String PSTRING_h = "pstring/h";
	private static final String PSTRING_L = "pstring/L";
	private static final String PSTRING_l = "pstring/l";
	
	
	public byte[] getValue(TestableNode magic, byte[] ba) {
		int o = magic.resolveOffset(ba);
		if( o < ba.length) {
			int sizeLength = 1;
			String type = magic.getType();
			ByteOrder sizeEndian = ByteOrder.BIG_ENDIAN;
			if( PSTRING_B.equals(type)) {
				sizeLength = 1;
			} else if( PSTRING_H.equals(type)) {
				sizeLength = 2;
				sizeEndian = ByteOrder.BIG_ENDIAN;
			} else if( PSTRING_h.equals(type)) {
				sizeLength = 2;
				sizeEndian = ByteOrder.LITTLE_ENDIAN;
			} else if( PSTRING_L.equals(type)) {
				sizeLength = 4;
				sizeEndian = ByteOrder.BIG_ENDIAN;
			} else if( PSTRING_l.equals(type)) {
				sizeLength = 4;
				sizeEndian = ByteOrder.LITTLE_ENDIAN;
			}
			
			
			// can we read sizeLength bytes to find out the size?
			int size = -1;
			if( ba.length >= o+sizeLength) {
				byte[] sizeArray = new byte[sizeLength];
				System.arraycopy(ba, o, sizeArray, 0, sizeLength);
				if( sizeEndian == ByteOrder.LITTLE_ENDIAN) {
					size = (int)NumericTest.getLittleEndianUInt32(sizeArray);
				} else {
					size = (int)NumericTest.getBigEndianUInt32(sizeArray);
				}
			}
			
			if( size != -1 && ba.length >= o+sizeLength+size) {
				byte[] chars = new byte[size];
				System.arraycopy(ba, o+sizeLength, chars, 0, size);
				return chars;
			}
		}
		return null;
	}
	public boolean matches(TestableNode magic, byte[] bytearray) {
		return false;
	}
}