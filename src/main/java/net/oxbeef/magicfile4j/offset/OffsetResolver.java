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
package net.oxbeef.magicfile4j.offset;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.oxbeef.magicfile4j.model.internal.Magic;
import net.oxbeef.magicfile4j.model.internal.MagicNode;
import net.oxbeef.magicfile4j.model.internal.TestableNode;
import net.oxbeef.magicfile4j.offset.util.StringUtils;

/**
 *    offset = absoffset | reloffset | indoffset
 *                           ;; The offset in the file at which to apply
 *                           ;; the <test>.
 *   
 *   absoffset = NUMBER      ;; An absolute offset from the start of the file.
 *   
 *   reloffset = "&" NUMBER  ;; The offset relative to the last match offset
 *                           ;; at one level up.
 *                           ;; Not allowed at level == 0.
 *   
 *   indoffset = indoff | relindoff
 *   
 *   indoff = "(" offset1 [ "." size ] [ op disp ] ")"
 *                           ;; Read the file at <offset1> of width <size>.
 *                           ;; If size is not specified, assume a long.
 *                           ;; If <op> is given, then preform that
 *                           ;; operation on the result and the <disp>.
 *   
 *   offset1 = absoffset | reloffset
 *   
 *   size = byte | leshort | beshort | lelong | belong | melong
 *   B b C c s h S H l L m 
 *   byte = "B" | "b" | "C" | "c"    ;; A one-byte value.
 *   leshort = "s" | "h"             ;; A two-byte little-endian value.
 *   beshort = "S" | "H"             ;; A two-byte big-endian value.
 *   lelong = "l"                    ;; A four-byte little-endian value.
 *   belong = "L"                    ;; A four-byte big-endian value.
 *   melong = "m"                    ;; A four-byte middle-endian value.
 *   
 *   op = [ invert ] ( "+" | "-" | "*" | "/" | "%" | "&" | "|" | "^" )
 *   
 *   invert = "~"            ;; Flip the bits on result of the <op>.
 *   
 *   disp =  NUMBER | memvalue
 *   
 *   memvalue = "(" NUMBER ")"
 *                           ;; NUMBER is interpreted as an absolute or
 *                           ;; relative offset matching that of <offset1>.
 *                           ;; Read the file at the resulting offset with
 *                           ;; the same size as <offset1>
 *   
 *   relindoff = "&" indoff  ;; add <indoff> to the last match offset at
 *                           ;; one level up.
 *    
 *    
 */
public class OffsetResolver {
	/*
	 * Return a long value containing the index in fileData where the test
	 * should begin, or -1 if impossible to discover
	 */
	public int getResolutionIndex(String offsetString, TestableNode node, byte[] fileData) {
		int parentOffset = 0;
		// if we have relative indexes
		if (offsetString.charAt(0) == '&' || offsetString.startsWith("(&")) {
			MagicNode parent = node.getParent();
			if (parent instanceof Magic) {
				parentOffset = ((Magic) parent).resolveOffset(fileData);
			} else {
				return -1;
			}
		}
		return getResolutionIndex(offsetString, node, fileData, parentOffset);
	}

	/*
	 * Return a long value containing the index in fileData where the test
	 * should begin, or -1 if impossible to discover
	 */
	protected int getResolutionIndex(String offsetString, TestableNode node, byte[] fileData, int parentOffset) {

		char c = offsetString.charAt(0);
		switch (c) {
		case '&':
			return handleRelative(offsetString.substring(1), node, fileData, parentOffset);
		case '(':
			return handleIndirect(offsetString, node, fileData, parentOffset);
		}
		return (int)StringUtils.stringToLong(offsetString); // NUMBER
	}

	protected int handleRelative(String offsetString, TestableNode node, byte[] fileData, int parentOffset) {
		char c = offsetString.charAt(0);
		if (c == '(') {
			int l = handleIndirect(offsetString, node, fileData, parentOffset);
			// If there was an error during indirection, return -1
			if( l == -1 )
				return -1;
			return l + parentOffset;
		}
		return (int)StringUtils.stringToLong(offsetString) + parentOffset;
	}

	/*
	 * Return the index to introspect for the comparison, or -1 if impossible
	 */
	protected int handleIndirect(String offsetString, TestableNode node, byte[] fileData, long parentOffset) {

		int relativeIndex = -1;
		int signIndex = -1;
		int indirectAddr = -1;
		int dotIndex = -1;
		int sizeIndex = -1;
		int invertIndex = -1;
		int opIndex = -1;
		int dispIndex = -1;

		// First we want to find the indexes for all the important parts the offset may have
		// They're all initiailzied to -1 and will be set if we find them. 
		for (int i = 1; i < offsetString.length() - 1; i++) { 
			// ignore first and last char as they are parens
			char c = offsetString.charAt(i);
			switch (c) {
			case '+':
			case '-':
				if (indirectAddr == -1) {
					signIndex = i;
				} else {
					opIndex = i;
				}
				break;
			case '*':
			case '/':
			case '%':
			case '|':
			case '^':
				opIndex = i;
				break;
			case '&':
				if (indirectAddr == -1) {
					relativeIndex = i;
				} else {
					opIndex = i;
				}
				break;

			case '.':
				dotIndex = i;
				break;
			case '~':
				invertIndex = i;
			case 'B':
			case 'b':
			case 'C':
			case 'c':
				if (dotIndex == -1 || opIndex != -1) {
					break;
				}
				// we're between dot and operation
				// fall through
			case 's':
			case 'h':
			case 'S':
			case 'H':
			case 'l':
			case 'L':
			case 'm':
				sizeIndex = i;
				break;

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
				if (indirectAddr == -1) {
					indirectAddr = i;
				} else if (opIndex != -1 && dispIndex == -1) {
					dispIndex = i;
				}
			}
		}

		// Initialize the addr to 0
		int addr = 0;
		
		// If we're relative to previous line, add it in now
		if (relativeIndex != -1)
			addr += parentOffset;
		
		// turn the string representation of the address into an index
		long indirAddrVal = StringUtils.stringToLongUnknownLength(offsetString, indirectAddr);
		
		// there may be a sign here, for example,  &-12  
		// would mean 12 bytes before previous match, so add in the sign 
		if( signIndex != -1  && offsetString.charAt(signIndex) == '-') {
			indirAddrVal *= -1;
		}
		addr += indirAddrVal;

		/*
		 * Deal with sizing... how many bytes to read, and what our mask should be
		 * when doing the various operations.  Also handle endianness
		 * 
		 *   byte = "B" | "b" | "C" | "c"    ;; A one-byte value.
		 *   leshort = "s" | "h"             ;; A two-byte little-endian value.
		 *   beshort = "S" | "H"             ;; A two-byte big-endian value.
		 *   lelong = "l"                    ;; A four-byte little-endian value.
		 *   belong = "L"                    ;; A four-byte big-endian value.
		 *   melong = "m"                    ;; A four-byte middle-endian value.
		 */
		// long postDeref = deref(addr, sizeType, fileData);
		char sizeType = (sizeIndex == -1 ? 'L' : offsetString.charAt(sizeIndex));
		int numBytesToRead = 0;
		ByteOrder endian = null;
		int mask = 0;
		switch(sizeType) {
		case 'B':
		case 'b':
		case 'C':
		case 'c':
			numBytesToRead = 1;
			mask = 0xFF;
			endian = ByteOrder.LITTLE_ENDIAN;
			break;
		case 's':
		case 'h':
			numBytesToRead = 2;
			mask = 0xFFFF;
			endian = ByteOrder.LITTLE_ENDIAN;
			break;
		case 'l':
			numBytesToRead = 4;
			mask = 0xFFFFFFFF;
			endian = ByteOrder.LITTLE_ENDIAN;
			break;
		case 'S':
		case 'H':
			numBytesToRead = 2;
			mask = 0xFFFF;
			endian = ByteOrder.BIG_ENDIAN;
			break;
		case 'L':
			numBytesToRead = 4;
			mask = 0xFFFFFFFF;
			endian = ByteOrder.BIG_ENDIAN;
			break;
		case 'm':
			// TODO unsupported for now
			return -1;
		default:
			// Um... no idea. Should really throw exception here
			endian = ByteOrder.LITTLE_ENDIAN;
		}
		
		//  

		
		if( addr + numBytesToRead <= fileData.length) {
			byte[] toRead = new byte[numBytesToRead];
			System.arraycopy(fileData, addr, toRead, 0, numBytesToRead);
			ByteBuffer bb = ByteBuffer.wrap(toRead);
			bb.order(endian);
			int readValue = -1;
			int result = -1;
			try {
				if( numBytesToRead == 1 ) {
					byte b1 = bb.get();
					readValue = b1;
				} else if( numBytesToRead == 2 ) {
					short s1 = bb.getShort();
					readValue = s1;
				} else if( numBytesToRead == 4) {
					int l1 = bb.getInt();
					readValue = l1;
				}
			} catch(BufferUnderflowException bufe) {
				// byte array isn't long enough. error. 
				return -1;
			}
			// perform the operation
			if( opIndex != -1 && dispIndex != -1 ) {
				// get the op char
				char op = opIndex == -1 ? 0 : offsetString.charAt(opIndex);
				
				// get the 2nd param to the op... ie  for (16.l+3) the 3 is the 2nd param. 
				int opDisp = dispIndex == -1 ? -1 : (int)StringUtils.stringToLongUnknownLength(offsetString, dispIndex);

				switch(op) {
				case '+':
					result = readValue + (opDisp & mask);
					break;
				case '-':
					result = readValue - (opDisp & mask);
					break;
				case '*':
					result = readValue * (opDisp & mask);
					break;
				case '/':
					result = readValue / (opDisp & mask);
					break;
				case '%':
					result = readValue % (opDisp & mask);
					break;
				case '|':
					result = readValue | (opDisp & mask);
					break;
				case '^': // xor
					result = readValue ^ (opDisp & mask);
					break;
				case '&':
					result = readValue & (opDisp & mask);
					break;
				}
				
				// re-mask the result to treat it as the proper size as intended
				result &= mask;
				
				// if we're required to invert, then invert, but re-mask afterwards
				if( invertIndex != -1 ) {
					result = ((~result) & mask);
				}
			}
			return result;
		} else {
			// attempting to read an impossible index
			return -1;
		}
	}
}
