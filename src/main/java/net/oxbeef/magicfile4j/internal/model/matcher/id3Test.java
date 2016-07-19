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

/**
 * A four-byte integer value where the high bit of each byte is ignored.
 */
public class id3Test extends NumericTest {
	public id3Test(ByteOrder order) {
		super(4, order);
	}
	public byte[] getValue(TestableNode magic, byte[] bytearray) {
		byte[] found = super.getValue(magic, bytearray);
		for( int i = 0; i < found.length; i++ ) {
			found[i] = (byte)(found[i] & 0x7F);
		}
		return found;
	}
}