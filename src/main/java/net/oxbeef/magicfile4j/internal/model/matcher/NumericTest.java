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

import java.nio.ByteBuffer;

import net.oxbeef.magicfile4j.internal.endian.Endian;
import net.oxbeef.magicfile4j.internal.endian.EndianReader;
import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.TestableNode;
import net.oxbeef.magicfile4j.internal.offset.StringUtils;

public class NumericTest extends Tester {
	protected int size;
	protected  EndianReader reader;
	public NumericTest(int size, Endian e) {
		this.size = size;
		this.reader = e.getConverter() ;
	}

	protected boolean matches(String test, byte[] dataAtOffset, boolean signed, char op) {
		long testVal = StringUtils.stringToLong(test);
		long foundVal = getBigEndianUInt32(dataAtOffset);
					
		switch(op) {
		case '<':
			return compare(foundVal,testVal, signed) < 0;
		case '>':
			return compare(foundVal, testVal, signed) > 0;
		case '!':
			return foundVal != testVal; 
		case '&':
			return (foundVal & testVal) == testVal;
		case '^': 
			/*
			 *  the char is xor, but magic man page says 
			 *  '^' is "to specify that the value from the file must
             *  have clear any of the bits that are set in the specified value"
             *  
             *  This is not an xor behavior, so either the manual is wrong, or we are.
             *  
             *  ex:
             *    CA:      1100 1010
             *    24:      0010 0100
             *    ~CA:     0011 0101
             *    ~CA&24:  0010 0100
             *    qed:  return ~CA&24 == 24
			 */
			return ((~foundVal)&testVal) == testVal;
		case '~':
			return ((~testVal)&sizeMask(size)) == foundVal;
		case '=':
			return foundVal == testVal;
		}
		return false;
	}
	
	/**
	 * Compare the two values once cast to the proper primitive type
	 * Subclasses should override when necessary.
	 * It is not necessary to return the actual difference...  only +1, 0, or -1
	 * 
	 * This implementation defaults to a four-byte behavior, and so plays as int
	 * 
	 * @param l
	 * @param l2
	 * @param signed whether the comparison should be done in a signed or unsigned manner
	 * @return
	 */
	protected long compare(long l, long l2, boolean signed) {
		if( !signed ) {
			// we have 8 bytes and are only using 4, so no problem
			return l - l2;
		}
		// Java long is an 8-byte creature, but we use the 4-byte impl as per magic reqs
		// to simulate the 4-byte behavior. 
		int i = (int)l;
		int i2 = (int)l2;
		long ret = ( i == i2 ? 0 : i < i2 ? -1 : 1);
		return ret;
	}
	
	public byte[] getValue(TestableNode magic, byte[] ba) {
		int o = magic.resolveOffset(ba);
		if( o + size > ba.length) {
			// Cannot read 'size' bytes here
			return null;
		}	
		byte[] ret = new byte[size];
		System.arraycopy(ba, o, ret, 0, size);
		
		ret = convertToBigEndian(ret);
		
		if( ret != null ) {
			// Find the mask
			String type = magic.getType();
			int ampInd = type.indexOf('&');
			if( ampInd != -1 ) {
				String mask1 = type.substring(ampInd+1);
				byte[] mask2 = StringUtils.numericStringToBytes(mask1);
				int sizeBytes = ret.length;
				int maskLength = mask2.length;
				int diff = sizeBytes-maskLength;
				for( int i = 0; i < sizeBytes; i++ ) {
					if( i >= diff) {
						ret[i] = (byte)(ret[i] & mask2[i-diff]);
					} else {
						ret[i] = 0;
					}
				}
			}
		}
		
		return ret;
	}
	
	protected byte[] convertToBigEndian(byte[] raw) {
		// If it's little-endian, reverse it to big-endian
		return reader.convertToBigEndian(raw);
	}
	
	
	@Override
	public boolean matches(TestableNode magic, byte[] byteArray, byte[] dataAtOffset) {
		String test = ((Magic)magic).getTest();
		if( test != null && !test.isEmpty()) {
			
			// First we need to find out what our operation is
			char op = test.charAt(0);
			switch(op) {
			case 'x':
				return true;
			case '=':
			case '!':
			case '<':
			case '>':
			case '&':
			case '~':
			case '^':
				test = test.substring(1);
				break;
			default:
				op = '=';
			}
			
			boolean signed = isSigned(magic);
			return matches(test, dataAtOffset, signed, op);
		}
		return false;
	}
	
	protected boolean isSigned(TestableNode magic) {
		return !magic.getType().startsWith("u");
	}
	
	
	
	public static long byteAsULong(byte b) {
	    return ((long)b) & 0x00000000000000FFL; 
	}
	protected static long getLittleEndianUInt32(byte[] bytes) {
		long val = 0;
		int shift = 0;
		for( int i = 0; i < bytes.length; i++ ) {
			val |= (byteAsULong(bytes[i]) << shift);
			shift += 8;
		}
	    return val;
	}
	protected static long getBigEndianUInt32(byte[] bytes) {
		long val = 0;
		int shift = 0;
		for( int i = bytes.length-1; i >= 0; i-- ) {
			val |= (byteAsULong(bytes[i]) << shift);
			shift += 8;
		}
	    return val;
	}
	protected static long sizeMask(int size) {
		long ret = 0;
		for( int i = 0; i < size; i++ ) {
			ret |= (0xFF << (i*8));
		}
		return ret;
	}
	
	public String formatString(Magic m, String out, byte[] val) {
		ByteBuffer bb = ByteBuffer.wrap(val);
		switch(val.length) {
		case 1:
			return String.format(out, bb.get());
		case 2:
			return String.format(out,  bb.getShort());
		case 4: 
			return String.format(out,  bb.getInt());
		case 8: 
			return String.format(out, bb.getDouble());
		}
		return out;
	}
}
