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

import net.oxbeef.magicfile4j.internal.endian.Endian;

public class ShortTest extends NumericTest {
	public ShortTest(Endian endian){
		super(2, endian);
	}

	protected long compare(long l, long l2, boolean signed) {
		if( !signed) {
			return super.compare(l, l2, signed);
		}

		long poo = (long)((short)l - (short)l2);
		return poo;
	}
}