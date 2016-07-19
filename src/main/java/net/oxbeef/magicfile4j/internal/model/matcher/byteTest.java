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
import java.nio.ByteOrder;

import net.oxbeef.magicfile4j.internal.model.Magic;

public class byteTest extends NumericTest {
	public byteTest() {
		super(1, ByteOrder.LITTLE_ENDIAN);
	}
	protected long compare(long l, long l2, boolean signed) {
		if( !signed) { 
			return super.compare(l, l2, signed);
		}
		return (long)((byte)l - (byte)l2);
	}
	@Override
	public String formatString(Magic m, String out, byte[] val) {
		ByteBuffer bb = ByteBuffer.wrap(val);
		return String.format(out, bb.get());
	}

}