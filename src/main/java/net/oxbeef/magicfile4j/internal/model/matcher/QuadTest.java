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
import net.oxbeef.magicfile4j.internal.model.Magic;

public class QuadTest extends NumericTest {
	public QuadTest(Endian bo) {
		super(8,bo);
	}
	
	protected long compare(long foundVal, long testVal, boolean signed) {
		if( signed ) {
			return foundVal == testVal ? 0 : foundVal > testVal ? 1 : -1; 
		}
		// we have 8 bytes
		return foundVal - testVal;
	}
	
	@Override
	public String formatString(Magic m, String out, byte[] val) {
		ByteBuffer bb = ByteBuffer.wrap(val);
		return String.format(out, bb.getLong());
	}

}