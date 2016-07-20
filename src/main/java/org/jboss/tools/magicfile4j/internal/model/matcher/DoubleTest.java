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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jboss.tools.magicfile4j.internal.endian.Endian;
import org.jboss.tools.magicfile4j.internal.model.Magic;

public class DoubleTest extends NumericTest {

	public DoubleTest(Endian endian) {
		super(8, endian);
	}

	protected boolean matches(String test, byte[] dataAtOffset, boolean signed, char op) {
		double testVal = Double.parseDouble(test);
		double foundVal = ByteBuffer.wrap(dataAtOffset).getDouble();
					
		switch(op) {
		case '<':
			return foundVal - testVal < 0;
		case '>':
			return foundVal - testVal > 0;
		case '!':
			return foundVal != testVal; 
		case '&':
			return false; //(foundVal & testVal) == testVal;
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
			return false; //((~foundVal)&testVal) == testVal;
		case '~':
			return false; //((~testVal)&sizeMask(size)) == foundVal;
		case '=':
			return foundVal == testVal;
		}
		return false;
	}
	public String formatString(Magic m, String out, Object val) {
		ByteBuffer bb = ByteBuffer.wrap((byte[])val);
		return String.format(out, bb.getDouble());
	}

}